package io.github.faustofan.admin.shared.infrastructure.config.jimmer

import com.fasterxml.jackson.databind.ObjectMapper
import org.babyfish.jimmer.meta.ImmutableProp
import org.babyfish.jimmer.meta.ImmutableType
import org.babyfish.jimmer.sql.cache.Cache
import org.babyfish.jimmer.sql.cache.CacheFactory
import org.babyfish.jimmer.sql.cache.caffeine.CaffeineValueBinder
import org.babyfish.jimmer.sql.cache.chain.ChainCacheBuilder
import org.babyfish.jimmer.sql.cache.redis.spring.RedisValueBinder
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisConnectionFactory
import java.time.Duration

/**
 * Jimmer 缓存配置
 *
 * 使用 Redis 作为二级缓存，Caffeine 作为一级缓存（本地缓存）
 * 支持对象缓存和关联缓存
 */
@Configuration
@ConditionalOnProperty(prefix = "jimmer.cache", name = ["enabled"], havingValue = "true", matchIfMissing = false)
class JimmerCacheConfig {

    private val log = LoggerFactory.getLogger(JimmerCacheConfig::class.java)

    companion object {
        // 对象缓存 TTL
        private val OBJECT_CACHE_TTL = Duration.ofHours(1)

        // 关联缓存 TTL
        private val ASSOCIATION_CACHE_TTL = Duration.ofHours(2)

        // 本地缓存最大条目数
        private const val LOCAL_CACHE_MAX_SIZE = 1000

        // 本地缓存 TTL
        private val LOCAL_CACHE_TTL = Duration.ofMinutes(5)
    }

    /**
     * Jimmer 缓存工厂
     */
    @Bean
    @ConditionalOnBean(RedisConnectionFactory::class)
    fun cacheFactory(
        redisConnectionFactory: RedisConnectionFactory,
        objectMapper: ObjectMapper
    ): CacheFactory {
        log.info("Initializing Jimmer Redis cache factory")

        return object : CacheFactory {

            /**
             * 创建对象缓存 (根据 ID 缓存实体对象)
             */
            override fun createObjectCache(type: ImmutableType): Cache<*, *>? {
                return ChainCacheBuilder<Any, Any>()
                    // 一级缓存: Caffeine 本地缓存
                    .add(
                        CaffeineValueBinder.forObject<Any, Any>(type)
                            .maximumSize(LOCAL_CACHE_MAX_SIZE)
                            .duration(LOCAL_CACHE_TTL)
                            .build()
                    )
                    // 二级缓存: Redis 分布式缓存
                    .add(
                        RedisValueBinder.forObject<Any, Any>(type)
                            .redis(redisConnectionFactory)
                            .objectMapper(objectMapper)
                            .duration(OBJECT_CACHE_TTL)
                            .build()
                    )
                    .build()
            }

            /**
             * 创建关联缓存 (缓存实体之间的关联关系)
             */
            override fun createAssociatedIdCache(prop: ImmutableProp): Cache<*, *>? {
                return ChainCacheBuilder<Any, Any>()
                    .add(
                        CaffeineValueBinder.forProp<Any, Any>(prop)
                            .maximumSize(LOCAL_CACHE_MAX_SIZE)
                            .duration(LOCAL_CACHE_TTL)
                            .build()
                    )
                    .add(
                        RedisValueBinder.forProp<Any, Any>(prop)
                            .redis(redisConnectionFactory)
                            .objectMapper(objectMapper)
                            .duration(ASSOCIATION_CACHE_TTL)
                            .build()
                    )
                    .build()
            }

            /**
             * 创建计算属性缓存
             */
            override fun createResolverCache(prop: ImmutableProp): Cache<*, *>? {
                return ChainCacheBuilder<Any, Any>()
                    .add(
                        CaffeineValueBinder.forProp<Any, Any>(prop)
                            .maximumSize(LOCAL_CACHE_MAX_SIZE)
                            .duration(LOCAL_CACHE_TTL)
                            .build()
                    )
                    .add(
                        RedisValueBinder.forProp<Any, Any>(prop)
                            .redis(redisConnectionFactory)
                            .objectMapper(objectMapper)
                            .duration(ASSOCIATION_CACHE_TTL)
                            .build()
                    )
                    .build()
            }
        }
    }
}

