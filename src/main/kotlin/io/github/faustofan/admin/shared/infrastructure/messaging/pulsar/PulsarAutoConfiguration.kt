package io.github.faustofan.admin.shared.infrastructure.messaging.pulsar

import io.github.faustofan.admin.shared.infrastructure.messaging.DefaultTopicResolver
import io.github.faustofan.admin.shared.infrastructure.messaging.TopicResolver
import jakarta.annotation.PostConstruct
import org.apache.pulsar.client.api.PulsarClient
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.pulsar.core.DefaultPulsarProducerFactory
import org.springframework.pulsar.core.PulsarProducerFactory
import org.springframework.pulsar.core.PulsarTemplate
import java.util.concurrent.TimeUnit

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
    val consumer: ConsumerConfig = ConsumerConfig(),

    /** Pulsar 客户端配置 */
    val client: ClientConfig = ClientConfig()
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

    data class ClientConfig(
        /** Pulsar 服务地址 */
        val serviceUrl: String = "pulsar://localhost:6650",

        /** 操作超时（秒） */
        val operationTimeoutSeconds: Long = 30,

        /** 连接超时（秒） */
        val connectionTimeoutSeconds: Long = 10
    )
}

/**
 * Pulsar 自动配置类
 *
 * 当配置启用时自动配置 Pulsar 相关 Bean，支持可插拔配置
 */
@Configuration
@ConditionalOnClass(PulsarClient::class)
@ConditionalOnProperty(prefix = "app.messaging.pulsar", name = ["enabled"], havingValue = "true", matchIfMissing = false)
@EnableConfigurationProperties(PulsarCustomProperties::class)
class PulsarAutoConfiguration(
    private val properties: PulsarCustomProperties
) {
    private val log = LoggerFactory.getLogger(PulsarAutoConfiguration::class.java)

    @PostConstruct
    fun init() {
        log.info("========================================")
        log.info("Pulsar 消息队列已启用")
        log.info("服务地址: {}", properties.client.serviceUrl)
        log.info("租户/命名空间: {}/{}", properties.defaultTenant, properties.defaultNamespace)
        log.info("操作超时: {}s, 连接超时: {}s",
            properties.client.operationTimeoutSeconds,
            properties.client.connectionTimeoutSeconds)
        log.info("========================================")
    }

    /**
     * PulsarClient Bean
     *
     * 当 Spring Boot 的 PulsarAutoConfiguration 被排除时，提供自定义的 PulsarClient
     */
    @Bean
    @ConditionalOnMissingBean(PulsarClient::class)
    fun pulsarClient(): PulsarClient {
        log.info("正在创建 PulsarClient，连接到: {}", properties.client.serviceUrl)
        val client = PulsarClient.builder()
            .serviceUrl(properties.client.serviceUrl)
            .operationTimeout(properties.client.operationTimeoutSeconds.toInt(), TimeUnit.SECONDS)
            .connectionTimeout(properties.client.connectionTimeoutSeconds.toInt(), TimeUnit.SECONDS)
            .build()
        log.info("✅ PulsarClient 创建成功")
        return client
    }

    /**
     * PulsarProducerFactory Bean
     *
     * PulsarTemplate 需要的生产者工厂
     */
    @Bean
    @ConditionalOnMissingBean(PulsarProducerFactory::class)
    fun pulsarProducerFactory(pulsarClient: PulsarClient): PulsarProducerFactory<Any> {
        log.info("正在创建 PulsarProducerFactory")
        return DefaultPulsarProducerFactory(pulsarClient)
    }

    /**
     * PulsarTemplate Bean
     *
     * 当 Spring Boot 的 PulsarTemplate 不存在时，提供自定义的 PulsarTemplate
     */
    @Bean
    @ConditionalOnMissingBean(PulsarTemplate::class)
    fun pulsarTemplate(pulsarProducerFactory: PulsarProducerFactory<Any>): PulsarTemplate<Any> {
        log.info("正在创建 PulsarTemplate")
        return PulsarTemplate(pulsarProducerFactory)
    }

    /**
     * Topic 解析器
     */
    @Bean
    @ConditionalOnMissingBean(TopicResolver::class)
    fun topicResolver(): TopicResolver {
        log.info("正在创建 TopicResolver")
        return DefaultTopicResolver(
            defaultTenant = properties.defaultTenant,
            defaultNamespace = properties.defaultNamespace
        )
    }
}
