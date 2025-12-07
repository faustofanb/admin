package io.github.faustofan.admin.shared.infrastructure.messaging

import io.github.faustofan.admin.shared.domain.event.DomainEvent
import java.util.concurrent.CompletableFuture

/**
 * 通用消息发布接口
 *
 * 定义消息队列的抽象层，支持多种 MQ 实现（Pulsar、Kafka、RocketMQ 等）。
 * 遵循 DDD + CQRS 架构，用于发布领域事件。
 */
interface MessagePublisher {

    /**
     * 同步发送消息到默认 topic
     *
     * @param message 消息内容
     * @return 消息 ID
     */
    fun <T : Any> send(message: T): MessageId

    /**
     * 同步发送消息到指定 topic
     *
     * @param topic   目标 topic
     * @param message 消息内容
     * @return 消息 ID
     */
    fun <T : Any> send(topic: String, message: T): MessageId

    /**
     * 同步发送消息（带 key，用于顺序消息）
     *
     * @param topic   目标 topic
     * @param key     消息 key（用于分区或顺序保证）
     * @param message 消息内容
     * @return 消息 ID
     */
    fun <T : Any> send(topic: String, key: String, message: T): MessageId

    /**
     * 同步发送带属性的消息
     *
     * @param topic      目标 topic
     * @param message    消息内容
     * @param properties 消息属性（元数据）
     * @return 消息 ID
     */
    fun <T : Any> send(topic: String, message: T, properties: Map<String, String>): MessageId

    /**
     * 异步发送消息到默认 topic
     *
     * @param message 消息内容
     * @return CompletableFuture<MessageId>
     */
    fun <T : Any> sendAsync(message: T): CompletableFuture<MessageId>

    /**
     * 异步发送消息到指定 topic
     *
     * @param topic   目标 topic
     * @param message 消息内容
     * @return CompletableFuture<MessageId>
     */
    fun <T : Any> sendAsync(topic: String, message: T): CompletableFuture<MessageId>

    /**
     * 异步发送消息（带 key）
     *
     * @param topic   目标 topic
     * @param key     消息 key
     * @param message 消息内容
     * @return CompletableFuture<MessageId>
     */
    fun <T : Any> sendAsync(topic: String, key: String, message: T): CompletableFuture<MessageId>

    /**
     * 异步发送带属性的消息
     *
     * @param topic      目标 topic
     * @param message    消息内容
     * @param properties 消息属性
     * @return CompletableFuture<MessageId>
     */
    fun <T : Any> sendAsync(topic: String, message: T, properties: Map<String, String>): CompletableFuture<MessageId>

    /**
     * 发送延迟消息
     *
     * @param topic        目标 topic
     * @param message      消息内容
     * @param delaySeconds 延迟秒数
     * @return 消息 ID
     */
    fun <T : Any> sendDelayed(topic: String, message: T, delaySeconds: Long): MessageId

    /**
     * 异步发送延迟消息
     *
     * @param topic        目标 topic
     * @param message      消息内容
     * @param delaySeconds 延迟秒数
     * @return CompletableFuture<MessageId>
     */
    fun <T : Any> sendDelayedAsync(topic: String, message: T, delaySeconds: Long): CompletableFuture<MessageId>

    /**
     * 发送领域事件
     *
     * 自动根据事件类型路由到对应 topic
     *
     * @param event 领域事件
     * @return 消息 ID
     */
    fun publishEvent(event: DomainEvent): MessageId

    /**
     * 异步发送领域事件
     *
     * @param event 领域事件
     * @return CompletableFuture<MessageId>
     */
    fun publishEventAsync(event: DomainEvent): CompletableFuture<MessageId>
}

/**
 * 消息 ID 封装类
 */
@JvmInline
value class MessageId(val value: String) {
    override fun toString(): String = value
}

