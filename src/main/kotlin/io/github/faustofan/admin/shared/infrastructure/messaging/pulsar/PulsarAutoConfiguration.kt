package io.github.faustofan.admin.shared.infrastructure.messaging.pulsar

import io.github.faustofan.admin.shared.infrastructure.messaging.DefaultTopicResolver
import io.github.faustofan.admin.shared.infrastructure.messaging.TopicResolver
import org.apache.pulsar.client.api.PulsarClient
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * Pulsar 自定义配置属性
 */
@ConfigurationProperties(prefix = "app.messaging.pulsar")
data class PulsarCustomProperties(
    /** 是否启用 Pulsar 消息队列 */
    val enabled: Boolean = true,

    /** 默认租户 */
    val defaultTenant: String = "public",

    /** 默认命名空间 */
    val defaultNamespace: String = "default",

    /** 领域事件 Topic 前缀 */
    val eventTopicPrefix: String = "domain-events",

    /** 死信队列 Topic */
    val deadLetterTopic: String = "dlq",

    /** 消费者配置 */
    val consumer: ConsumerConfig = ConsumerConfig()
) {
    data class ConsumerConfig(
        /** 默认订阅名称前缀 */
        val subscriptionPrefix: String = "admin-backend",

        /** 消费者并发数 */
        val concurrency: Int = 4,

        /** 最大重试次数 */
        val maxRetries: Int = 3,

        /** 确认超时（毫秒） */
        val ackTimeoutMs: Long = 30000
    )
}

/**
 * Pulsar 自动配置类
 *
 * 当类路径中存在 PulsarClient 且配置启用时自动配置 Pulsar 相关 Bean
 */
@Configuration
@ConditionalOnClass(PulsarClient::class)
@ConditionalOnProperty(prefix = "app.messaging.pulsar", name = ["enabled"], havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties(PulsarCustomProperties::class)
class PulsarAutoConfiguration(
    private val properties: PulsarCustomProperties
) {

    /**
     * Topic 解析器
     */
    @Bean
    @ConditionalOnMissingBean(TopicResolver::class)
    fun topicResolver(): TopicResolver {
        return DefaultTopicResolver(
            defaultTenant = properties.defaultTenant,
            defaultNamespace = properties.defaultNamespace
        )
    }
}
