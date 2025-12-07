package io.github.faustofan.admin.shared.infrastructure.messaging

import java.time.LocalDateTime

/**
 * 消息元数据
 *
 * 封装消息的公共属性，用于消费者端获取消息上下文信息
 */
data class MessageMetadata(
    /** 消息 ID */
    val messageId: String,

    /** 消息 Key（用于分区/顺序保证） */
    val key: String?,

    /** Topic 名称 */
    val topic: String,

    /** 生产者名称 */
    val producerName: String?,

    /** 发布时间 */
    val publishTime: LocalDateTime,

    /** 事件时间（业务时间） */
    val eventTime: LocalDateTime?,

    /** 序列号 */
    val sequenceId: Long?,

    /** 重试次数 */
    val redeliveryCount: Int,

    /** 自定义属性 */
    val properties: Map<String, String>
)

/**
 * 消息上下文
 *
 * 封装消息体和元数据，消费者可通过此对象访问完整消息信息
 */
data class MessageContext<T>(
    /** 消息体 */
    val payload: T,

    /** 消息元数据 */
    val metadata: MessageMetadata
) {
    /** 便捷方法：获取消息 ID */
    val messageId: String get() = metadata.messageId

    /** 便捷方法：获取 Topic */
    val topic: String get() = metadata.topic

    /** 便捷方法：获取重试次数 */
    val redeliveryCount: Int get() = metadata.redeliveryCount

    /** 便捷方法：判断是否是重试消息 */
    val isRedelivered: Boolean get() = redeliveryCount > 0

    /** 便捷方法：获取属性值 */
    fun getProperty(key: String): String? = metadata.properties[key]
}

