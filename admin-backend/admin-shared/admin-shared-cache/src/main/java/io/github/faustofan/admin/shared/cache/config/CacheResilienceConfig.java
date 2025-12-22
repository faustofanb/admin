package io.github.faustofan.admin.shared.cache.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 缓存容错配置
 * 当 Redis 等缓存服务不可用时，不抛出异常中断业务，而是记录日志
 */
@Configuration
public class CacheResilienceConfig {

    private static final Logger log = LoggerFactory.getLogger(CacheResilienceConfig.class);

    /**
     * 自定义缓存错误处理器
     */
    @Bean
    public CacheErrorHandler errorHandler() {
        return new CacheErrorHandler() {
            @Override
            public void handleCacheGetError(RuntimeException exception, Cache cache, Object key) {
                // Redis 挂了，降级为查数据库，只打印日志，不抛出异常中断业务
                log.error("Cache GET failed for key: {}", key, exception);
            }

            @Override
            public void handleCachePutError(RuntimeException exception, Cache cache, Object key, Object value) {
                log.error("Cache PUT failed for key: {}", key, exception);
            }

            @Override
            public void handleCacheEvictError(RuntimeException exception, Cache cache, Object key) {
                log.error("Cache EVICT failed for key: {}", key, exception);
            }

            @Override
            public void handleCacheClearError(RuntimeException exception, Cache cache) {
                log.error("Cache CLEAR failed", exception);
            }
        };
    }
}
