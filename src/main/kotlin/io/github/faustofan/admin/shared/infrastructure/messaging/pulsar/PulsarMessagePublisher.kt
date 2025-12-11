package io.github.faustofan.admin.shared.infrastructure.messaging.pulsar

import io.github.faustofan.admin.shared.domain.event.DomainEvent
import io.github.faustofan.admin.shared.infrastructure.messaging.MessageId
import io.github.faustofan.admin.shared.infrastructure.messaging.MessagePublisher
import io.github.faustofan.admin.shared.infrastructure.messaging.TopicResolver
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.pulsar.core.PulsarTemplate
import org.springframework.stereotype.Component
import java.util.concurrent.CompletableFuture
import java.util.concurrent.TimeUnit
import org.apache.pulsar.client.api.MessageId as PulsarMessageId

/**
 * Pulsar 消息发布者实现
 *
 * 基于 Spring Pulsar 的 PulsarTemplate 实现通用消息发布接口。
 * 支持同步/异步发送、延迟消息、带 Key 的顺序消息等特性。
 */
@Component
@ConditionalOnBean(PulsarAutoConfiguration::class)
class PulsarMessagePublisher(
    private val pulsarTemplate: PulsarTemplate<Any>,
    private val topicResolver: TopicResolver
) : MessagePublisher {

    private val log = LoggerFactory.getLogger(PulsarMessagePublisher::class.java)

    // ======================== 同步发送 ========================

    override fun <T : Any> send(message: T): MessageId {
        val topic = topicResolver.resolve(message)
        return send(topic, message)
    }

    override fun <T : Any> send(topic: String, message: T): MessageId {
        log.debug("Sending message to topic [{}]: {}", topic, message::class.java.simpleName)
        val pulsarMsgId = pulsarTemplate.send(topic, message)
        val id = MessageId(pulsarMsgId.toString())
        log.debug("Message sent successfully, messageId: {}", id)
        return id
    }

    override fun <T : Any> send(topic: String, key: String, message: T): MessageId {
        log.debug("Sending message with key [{}] to topic [{}]: {}", key, topic, message::class.java.simpleName)
        val pulsarMsgId = pulsarTemplate.newMessage(message)
            .withTopic(topic)
            .withMessageCustomizer { it.key(key) }
            .send()
        val id = MessageId(pulsarMsgId.toString())
        log.debug("Message sent successfully, messageId: {}", id)
        return id
    }

    override fun <T : Any> send(topic: String, message: T, properties: Map<String, String>): MessageId {
        log.debug("Sending message with properties to topic [{}]: {}", topic, message::class.java.simpleName)
        val pulsarMsgId = pulsarTemplate.newMessage(message)
            .withTopic(topic)
            .withMessageCustomizer { builder ->
                properties.forEach { (k, v) -> builder.property(k, v) }
            }
            .send()
        val id = MessageId(pulsarMsgId.toString())
        log.debug("Message sent successfully, messageId: {}", id)
        return id
    }

    // ======================== 异步发送 ========================

    override fun <T : Any> sendAsync(message: T): CompletableFuture<MessageId> {
        val topic = topicResolver.resolve(message)
        return sendAsync(topic, message)
    }

    override fun <T : Any> sendAsync(topic: String, message: T): CompletableFuture<MessageId> {
        log.debug("Sending async message to topic [{}]: {}", topic, message::class.java.simpleName)
        return pulsarTemplate.sendAsync(topic, message)
            .thenApply { pulsarMsgId: PulsarMessageId ->
                val id = MessageId(pulsarMsgId.toString())
                log.debug("Async message sent successfully, messageId: {}", id)
                id
            }
    }

    override fun <T : Any> sendAsync(topic: String, key: String, message: T): CompletableFuture<MessageId> {
        log.debug("Sending async message with key [{}] to topic [{}]: {}", key, topic, message::class.java.simpleName)
        return pulsarTemplate.newMessage(message)
            .withTopic(topic)
            .withMessageCustomizer { it.key(key) }
            .sendAsync()
            .thenApply { pulsarMsgId: PulsarMessageId ->
                val id = MessageId(pulsarMsgId.toString())
                log.debug("Async message sent successfully, messageId: {}", id)
                id
            }
    }

    override fun <T : Any> sendAsync(topic: String, message: T, properties: Map<String, String>): CompletableFuture<MessageId> {
        log.debug("Sending async message with properties to topic [{}]: {}", topic, message::class.java.simpleName)
        return pulsarTemplate.newMessage(message)
            .withTopic(topic)
            .withMessageCustomizer { builder ->
                properties.forEach { (k, v) -> builder.property(k, v) }
            }
            .sendAsync()
            .thenApply { pulsarMsgId: PulsarMessageId ->
                val id = MessageId(pulsarMsgId.toString())
                log.debug("Async message sent successfully, messageId: {}", id)
                id
            }
    }

    // ======================== 延迟消息 ========================

    override fun <T : Any> sendDelayed(topic: String, message: T, delaySeconds: Long): MessageId {
        log.debug("Sending delayed message ({}s) to topic [{}]: {}", delaySeconds, topic, message::class.java.simpleName)
        val pulsarMsgId = pulsarTemplate.newMessage(message)
            .withTopic(topic)
            .withMessageCustomizer { it.deliverAfter(delaySeconds, TimeUnit.SECONDS) }
            .send()
        val id = MessageId(pulsarMsgId.toString())
        log.debug("Delayed message sent successfully, messageId: {}", id)
        return id
    }

    override fun <T : Any> sendDelayedAsync(topic: String, message: T, delaySeconds: Long): CompletableFuture<MessageId> {
        log.debug("Sending async delayed message ({}s) to topic [{}]: {}", delaySeconds, topic, message::class.java.simpleName)
        return pulsarTemplate.newMessage(message)
            .withTopic(topic)
            .withMessageCustomizer { it.deliverAfter(delaySeconds, TimeUnit.SECONDS) }
            .sendAsync()
            .thenApply { pulsarMsgId: PulsarMessageId ->
                val id = MessageId(pulsarMsgId.toString())
                log.debug("Async delayed message sent successfully, messageId: {}", id)
                id
            }
    }

    // ======================== 领域事件 ========================

    override fun publishEvent(event: DomainEvent): MessageId {
        val topic = topicResolver.resolve(event)
        log.debug("Publishing domain event to topic [{}]: {}", topic, event::class.java.simpleName)
        val pulsarMsgId = pulsarTemplate.newMessage(event)
            .withTopic(topic)
            .withMessageCustomizer { builder ->
                builder.property("eventType", event::class.java.simpleName)
                builder.property("occurredOn", event.occurredOn.toString())
                event.operatorId?.let { builder.property("operatorId", it.toString()) }
            }
            .send()
        val id = MessageId(pulsarMsgId.toString())
        log.debug("Domain event published successfully, messageId: {}", id)
        return id
    }

    override fun publishEventAsync(event: DomainEvent): CompletableFuture<MessageId> {
        val topic = topicResolver.resolve(event)
        log.debug("Publishing async domain event to topic [{}]: {}", topic, event::class.java.simpleName)
        return pulsarTemplate.newMessage(event)
            .withTopic(topic)
            .withMessageCustomizer { builder ->
                builder.property("eventType", event::class.java.simpleName)
                builder.property("occurredOn", event.occurredOn.toString())
                event.operatorId?.let { builder.property("operatorId", it.toString()) }
            }
            .sendAsync()
            .thenApply { pulsarMsgId: PulsarMessageId ->
                val id = MessageId(pulsarMsgId.toString())
                log.debug("Async domain event published successfully, messageId: {}", id)
                id
            }
    }
}
