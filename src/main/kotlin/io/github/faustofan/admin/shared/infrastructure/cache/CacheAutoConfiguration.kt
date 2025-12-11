package io.github.faustofan.admin.shared.infrastructure.cache

import jakarta.annotation.PostConstruct
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration

/**
 * 缓存自定义配置属性
 */
@ConfigurationProperties(prefix = "app.cache")
data class CacheProperties(
    /** 是否启用缓存系统 */
    val enabled: Boolean = true,

    /** Redis 缓存配置 */
    val redis: RedisConfig = RedisConfig()
) {
    data class RedisConfig(
        /** 是否启用 Redis 缓存 */
        val enabled: Boolean = false
    )
}

/**
 * 缓存自动配置类
 *
 * 统一管理缓存系统的配置，支持可插拔
 */
@Configuration
@ConditionalOnProperty(prefix = "app.cache", name = ["enabled"], havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties(CacheProperties::class)
class CacheAutoConfiguration(
    private val properties: CacheProperties
) {
    private val log = LoggerFactory.getLogger(CacheAutoConfiguration::class.java)

    @PostConstruct
    fun init() {
        log.info("========================================")
        log.info("缓存系统已启用")
        log.info("Redis 缓存: {}", if (properties.redis.enabled) "已启用" else "已禁用")
        if (!properties.redis.enabled) {
            log.info("将使用本地缓存 (Caffeine) 作为默认实现")
        }
        log.info("========================================")
    }
}
