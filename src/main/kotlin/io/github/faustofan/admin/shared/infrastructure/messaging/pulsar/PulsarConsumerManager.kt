package io.github.faustofan.admin.shared.infrastructure.messaging.pulsar

import io.github.faustofan.admin.shared.infrastructure.messaging.*
import jakarta.annotation.PreDestroy
import kotlinx.coroutines.runBlocking
import org.apache.pulsar.client.api.*
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.stereotype.Component
import java.time.Instant
import java.time.ZoneId
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger

/**
 * Pulsar 消息消费者管理器实现
 *
 * 负责动态注册、启停消费者，支持协程处理消息
 */
@Component
@ConditionalOnBean(PulsarClient::class)
class PulsarConsumerManager(
    private val pulsarClient: PulsarClient,
    private val topicResolver: TopicResolver,
    private val properties: PulsarCustomProperties
) : MessageConsumerManager {

    private val log = LoggerFactory.getLogger(PulsarConsumerManager::class.java)

    /** 消费者容器：consumerId -> ConsumerWrapper */
    private val consumers = ConcurrentHashMap<String, ConsumerWrapper<*>>()

    /** 消费者 ID 计数器 */
    private val idCounter = AtomicInteger(0)

    override fun <T : Any> register(handler: MessageHandler<T>): String {
        val consumerId = generateConsumerId(handler)
        val topic = handler.topic() ?: topicResolver.resolve(handler.messageType())
        val subscriptionName = handler.subscriptionName()
            ?: "${properties.consumer.subscriptionPrefix}-${handler.messageType().simpleName}"

        log.info("Registering consumer [{}] for topic [{}] with subscription [{}]",
            consumerId, topic, subscriptionName)

        val wrapper = ConsumerWrapper(
            consumerId = consumerId,
            handler = handler,
            topic = topic,
            subscriptionName = subscriptionName,
            pulsarClient = pulsarClient,
            properties = properties
        )

        consumers[consumerId] = wrapper
        return consumerId
    }

    override fun start(consumerId: String) {
        val wrapper = consumers[consumerId]
            ?: throw IllegalArgumentException("Consumer not found: $consumerId")
        wrapper.start()
        log.info("Consumer [{}] started", consumerId)
    }

    override fun stop(consumerId: String) {
        val wrapper = consumers[consumerId]
            ?: throw IllegalArgumentException("Consumer not found: $consumerId")
        wrapper.stop()
        log.info("Consumer [{}] stopped", consumerId)
    }

    override fun startAll() {
        log.info("Starting all {} consumers", consumers.size)
        consumers.values.forEach { it.start() }
    }

    override fun stopAll() {
        log.info("Stopping all {} consumers", consumers.size)
        consumers.values.forEach { it.stop() }
    }

    override fun isRunning(consumerId: String): Boolean {
        return consumers[consumerId]?.isRunning() ?: false
    }

    override fun getAllStatus(): Map<String, Boolean> {
        return consumers.mapValues { it.value.isRunning() }
    }

    @PreDestroy
    fun destroy() {
        log.info("Destroying PulsarConsumerManager, stopping all consumers...")
        stopAll()
        consumers.values.forEach { it.close() }
    }

    private fun <T : Any> generateConsumerId(handler: MessageHandler<T>): String {
        return "consumer-${handler.messageType().simpleName}-${idCounter.incrementAndGet()}"
    }

    /**
     * 消费者包装器
     *
     * 封装 Pulsar Consumer 的生命周期管理和消息处理逻辑
     */
    private class ConsumerWrapper<T : Any>(
        val consumerId: String,
        private val handler: MessageHandler<T>,
        private val topic: String,
        private val subscriptionName: String,
        private val pulsarClient: PulsarClient,
        private val properties: PulsarCustomProperties
    ) {
        private val log = LoggerFactory.getLogger(ConsumerWrapper::class.java)
        private val running = AtomicBoolean(false)
        private var consumer: Consumer<T>? = null
        private var consumerThread: Thread? = null

        fun start() {
            if (running.compareAndSet(false, true)) {
                consumer = createConsumer()
                consumerThread = Thread.ofVirtual()
                    .name("pulsar-consumer-$consumerId")
                    .start { consumeLoop() }
            }
        }

        fun stop() {
            running.set(false)
            consumerThread?.interrupt()
        }

        fun isRunning(): Boolean = running.get()

        fun close() {
            stop()
            try {
                consumer?.close()
            } catch (e: Exception) {
                log.warn("Error closing consumer [{}]: {}", consumerId, e.message)
            }
        }

        private fun createConsumer(): Consumer<T> {
            return pulsarClient.newConsumer(Schema.JSON(handler.messageType()))
                .topic(topic)
                .subscriptionName(subscriptionName)
                .subscriptionType(SubscriptionType.Shared)
                .ackTimeout(properties.consumer.ackTimeoutMs, TimeUnit.MILLISECONDS)
                .deadLetterPolicy(
                    DeadLetterPolicy.builder()
                        .maxRedeliverCount(properties.consumer.maxRetries)
                        .deadLetterTopic("persistent://${properties.defaultTenant}/${properties.defaultNamespace}/${properties.deadLetterTopic}")
                        .build()
                )
                .subscribe()
        }

        private fun consumeLoop() {
            log.info("Consumer loop started for [{}]", consumerId)

            while (running.get()) {
                try {
                    val message = consumer?.receive() ?: continue
                    processMessage(message)
                } catch (_: InterruptedException) {
                    log.debug("Consumer [{}] interrupted", consumerId)
                    Thread.currentThread().interrupt()
                    break
                } catch (e: Exception) {
                    log.error("Error in consumer loop [{}]: {}", consumerId, e.message, e)
                }
            }

            log.info("Consumer loop ended for [{}]", consumerId)
        }

        private fun processMessage(message: Message<T>) {
            val context = buildMessageContext(message)

            try {
                val result = runBlocking {
                    handler.handle(context)
                }

                when (result) {
                    is ConsumeResult.Success -> {
                        consumer?.acknowledge(message)
                        log.debug("Message [{}] processed successfully", message.messageId)
                    }
                    is ConsumeResult.Retry -> {
                        consumer?.negativeAcknowledge(message)
                        log.warn("Message [{}] will be retried: {}", message.messageId, result.reason)
                    }
                    is ConsumeResult.DeadLetter -> {
                        // 直接确认消息，让死信策略处理
                        consumer?.acknowledge(message)
                        log.error("Message [{}] sent to dead letter queue: {}", message.messageId, result.reason)
                    }
                }
            } catch (e: Exception) {
                log.error("Exception processing message [{}]: {}", message.messageId, e.message, e)
                val errorResult = handler.onError(context, e)
                when (errorResult) {
                    is ConsumeResult.Success -> consumer?.acknowledge(message)
                    is ConsumeResult.Retry -> consumer?.negativeAcknowledge(message)
                    is ConsumeResult.DeadLetter -> consumer?.acknowledge(message)
                }
            }
        }

        private fun buildMessageContext(message: Message<T>): MessageContext<T> {
            val metadata = MessageMetadata(
                messageId = message.messageId.toString(),
                key = message.key,
                topic = message.topicName,
                producerName = message.producerName,
                publishTime = Instant.ofEpochMilli(message.publishTime)
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime(),
                eventTime = if (message.eventTime > 0) {
                    Instant.ofEpochMilli(message.eventTime)
                        .atZone(ZoneId.systemDefault())
                        .toLocalDateTime()
                } else null,
                sequenceId = message.sequenceId,
                redeliveryCount = message.redeliveryCount,
                properties = message.properties
            )

            return MessageContext(
                payload = message.value,
                metadata = metadata
            )
        }
    }
}
