package io.github.faustofan.admin.shared.infrastructure.messaging

/**
 * 消息消费处理结果
 */
sealed class ConsumeResult {
    /** 消费成功，确认消息 */
    data object Success : ConsumeResult()

    /** 消费失败，需要重试 */
    data class Retry(val reason: String? = null) : ConsumeResult()

    /** 消费失败，直接进入死信队列 */
    data class DeadLetter(val reason: String) : ConsumeResult()
}

/**
 * 通用消息消费者处理器接口
 *
 * 业务代码实现此接口来处理消息
 *
 * @param T 消息类型
 */
interface MessageHandler<T : Any> {

    /**
     * 处理消息
     *
     * @param context 消息上下文（包含消息体和元数据）
     * @return 消费结果
     */
    suspend fun handle(context: MessageContext<T>): ConsumeResult

    /**
     * 获取处理的消息类型
     */
    fun messageType(): Class<T>

    /**
     * 订阅的 Topic（默认由消息类型决定）
     */
    fun topic(): String? = null

    /**
     * 订阅名称（默认由应用名 + 消息类型决定）
     */
    fun subscriptionName(): String? = null

    /**
     * 异常处理（默认重试）
     */
    fun onError(context: MessageContext<T>, exception: Throwable): ConsumeResult {
        return ConsumeResult.Retry(exception.message)
    }
}

/**
 * 消息消费者管理接口
 *
 * 用于动态注册、启停消费者
 */
interface MessageConsumerManager {

    /**
     * 注册消息处理器
     *
     * @param handler 消息处理器
     * @return 消费者 ID
     */
    fun <T : Any> register(handler: MessageHandler<T>): String

    /**
     * 启动指定消费者
     *
     * @param consumerId 消费者 ID
     */
    fun start(consumerId: String)

    /**
     * 停止指定消费者
     *
     * @param consumerId 消费者 ID
     */
    fun stop(consumerId: String)

    /**
     * 启动所有消费者
     */
    fun startAll()

    /**
     * 停止所有消费者
     */
    fun stopAll()

    /**
     * 获取消费者状态
     *
     * @param consumerId 消费者 ID
     * @return 是否正在运行
     */
    fun isRunning(consumerId: String): Boolean

    /**
     * 获取所有消费者状态
     *
     * @return Map<消费者ID, 是否运行>
     */
    fun getAllStatus(): Map<String, Boolean>
}

