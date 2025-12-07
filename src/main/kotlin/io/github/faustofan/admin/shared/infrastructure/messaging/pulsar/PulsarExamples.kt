package io.github.faustofan.admin.shared.infrastructure.messaging.pulsar

import io.github.faustofan.admin.shared.infrastructure.messaging.ConsumeResult
import io.github.faustofan.admin.shared.infrastructure.messaging.MessageContext
import io.github.faustofan.admin.shared.infrastructure.messaging.MessageHandler
import io.github.faustofan.admin.shared.infrastructure.messaging.Topic
import org.slf4j.LoggerFactory
import org.springframework.pulsar.annotation.PulsarListener
import org.springframework.stereotype.Component

/**
 * 示例：用户创建事件
 *
 * 使用 @Topic 注解指定事件对应的 Topic
 */
@Topic("user-created-events")
data class UserCreatedEvent(
    val userId: Long,
    val username: String,
    val email: String?,
    val tenantId: Long
)

/**
 * 示例：使用 Spring Pulsar 注解方式消费消息
 *
 * 适用于简单场景，Spring 自动管理消费者生命周期
 */
@Component
class UserCreatedEventListener {

    private val log = LoggerFactory.getLogger(UserCreatedEventListener::class.java)

    /**
     * 使用 @PulsarListener 注解消费消息
     *
     * Spring Pulsar 会自动：
     * - 创建消费者
     * - 反序列化消息
     * - 处理 ACK
     */
    @PulsarListener(
        topics = ["persistent://public/default/user-created-events"],
        subscriptionName = "admin-backend-user-created",
        concurrency = "4"
    )
    fun onUserCreated(event: UserCreatedEvent) {
        log.info("Received UserCreatedEvent: userId={}, username={}, tenantId={}",
            event.userId, event.username, event.tenantId)

        // 业务处理逻辑
        // 例如：发送欢迎邮件、初始化用户配置等
    }
}

/**
 * 示例：使用通用接口方式消费消息
 *
 * 适用于需要更多控制的场景（重试策略、死信处理等）
 */
@Component
class UserCreatedEventHandler : MessageHandler<UserCreatedEvent> {

    private val log = LoggerFactory.getLogger(UserCreatedEventHandler::class.java)

    override suspend fun handle(context: MessageContext<UserCreatedEvent>): ConsumeResult {
        val event = context.payload

        log.info("Processing UserCreatedEvent [msgId={}]: userId={}, username={}, retryCount={}",
            context.messageId, event.userId, event.username, context.redeliveryCount)

        return try {
            // 业务处理逻辑
            processUserCreated(event)
            ConsumeResult.Success
        } catch (e: Exception) {
            log.error("Failed to process UserCreatedEvent: {}", e.message)

            // 根据重试次数决定处理策略
            if (context.redeliveryCount >= 3) {
                ConsumeResult.DeadLetter("Max retries exceeded: ${e.message}")
            } else {
                ConsumeResult.Retry(e.message)
            }
        }
    }

    override fun messageType(): Class<UserCreatedEvent> = UserCreatedEvent::class.java

    override fun topic(): String = "persistent://public/default/user-created-events"

    override fun subscriptionName(): String = "admin-backend-user-created-handler"

    private suspend fun processUserCreated(event: UserCreatedEvent) {
        // 模拟业务处理
        log.debug("Processing user: {}", event.username)
    }
}
