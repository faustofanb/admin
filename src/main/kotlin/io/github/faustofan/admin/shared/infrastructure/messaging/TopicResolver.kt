package io.github.faustofan.admin.shared.infrastructure.messaging

/**
 * Topic 解析器接口
 *
 * 用于根据消息类型自动解析对应的 Topic 名称
 */
interface TopicResolver {

    /**
     * 根据消息类型获取 Topic 名称
     *
     * @param messageType 消息类型
     * @return Topic 名称
     */
    fun <T : Any> resolve(messageType: Class<T>): String

    /**
     * 根据消息实例获取 Topic 名称
     *
     * @param message 消息实例
     * @return Topic 名称
     */
    fun <T : Any> resolve(message: T): String = resolve(message::class.java as Class<T>)
}

/**
 * Topic 注解
 *
 * 用于标记消息类对应的 Topic
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class Topic(
    /** Topic 名称（支持 SpEL 表达式） */
    val value: String,

    /** 是否持久化（Pulsar 特性） */
    val persistent: Boolean = true,

    /** 租户（Pulsar 特性） */
    val tenant: String = "public",

    /** 命名空间（Pulsar 特性） */
    val namespace: String = "default"
)

/**
 * 默认 Topic 解析器实现
 *
 * 解析规则：
 * 1. 如果类上有 @Topic 注解，使用注解值
 * 2. 否则，使用类名转换为 kebab-case 作为 topic 名称
 */
class DefaultTopicResolver(
    private val defaultTenant: String = "public",
    private val defaultNamespace: String = "default"
) : TopicResolver {

    override fun <T : Any> resolve(messageType: Class<T>): String {
        val annotation = messageType.getAnnotation(Topic::class.java)
        return if (annotation != null) {
            buildTopicName(
                name = annotation.value,
                persistent = annotation.persistent,
                tenant = annotation.tenant.ifEmpty { defaultTenant },
                namespace = annotation.namespace.ifEmpty { defaultNamespace }
            )
        } else {
            buildTopicName(
                name = toKebabCase(messageType.simpleName),
                persistent = true,
                tenant = defaultTenant,
                namespace = defaultNamespace
            )
        }
    }

    private fun buildTopicName(
        name: String,
        persistent: Boolean,
        tenant: String,
        namespace: String
    ): String {
        val prefix = if (persistent) "persistent" else "non-persistent"
        return "$prefix://$tenant/$namespace/$name"
    }

    private fun toKebabCase(input: String): String {
        return input.replace(Regex("([a-z])([A-Z])"), "$1-$2")
            .replace(Regex("([A-Z]+)([A-Z][a-z])"), "$1-$2")
            .lowercase()
    }
}

