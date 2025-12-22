package io.github.faustofan.admin.shared.cache.config;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.redisson.spring.cache.CacheConfig;
import org.redisson.spring.cache.RedissonSpringCacheManager;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import io.github.faustofan.admin.shared.cache.constants.CacheKeys;
import io.github.faustofan.admin.shared.cache.core.CacheInvalidateMsg;
import io.github.faustofan.admin.shared.cache.core.LayeredCache;
import io.github.faustofan.admin.shared.cache.enums.CachePolicy;

@Configuration
@EnableCaching
public class MultiLevelCacheConfig {

    private final String INSTANCE_ID = UUID.randomUUID().toString();

    /**
     * 1. 配置底层的 Redisson Manager (L2)
     * 在这里把 Enum 中的 L2 TTL 注入给 Redisson
     */
    @Bean
    public RedissonSpringCacheManager redissonSpringCacheManager(RedissonClient redissonClient) {
        // 将 CachePolicy 转换为 Redisson 的 Config Map
        Map<String, CacheConfig> configMap = Arrays.stream(CachePolicy.values())
                .collect(Collectors.toMap(
                        CachePolicy::cacheName,
                        policy -> new CacheConfig(
                                policy.l2Ttl().toMillis(),
                                policy.l2Ttl().toMillis() / 2)));

        return new RedissonSpringCacheManager(redissonClient, configMap);
    }

    /**
     * 2. 配置自定义的主 Manager (L1 + L2)
     */
    @Bean
    @Primary
    public CacheManager multiLevelCacheManager(
            RedissonSpringCacheManager redissonCacheManager,
            RedissonClient redissonClient) {
        return new CacheManager() {

            // 本地持有已创建的 Cache 实例
            private final ConcurrentHashMap<String, Cache> cacheMap = new ConcurrentHashMap<>();

            // 订阅逻辑 (只需执行一次)
            {
                RTopic topic = redissonClient.getTopic(CacheKeys.TOPIC_L1_SYNC);
                topic.addListener(CacheInvalidateMsg.class, (channel, msg) -> {
                    Cache cache = cacheMap.get(msg.cacheName());
                    if (cache instanceof LayeredCache layeredCache) {
                        layeredCache.handleMsg(msg);
                    }
                });
            }

            @Override
            public Cache getCache(String name) {
                return cacheMap.computeIfAbsent(name, k -> {
                    // 1. 获取原生的 L2 Cache
                    Cache l2Cache = redissonCacheManager.getCache(name);

                    // 2. 获取策略
                    CachePolicy policy = CachePolicy.resolve(name);

                    // 3. 获取 Topic
                    RTopic topic = redissonClient.getTopic(CacheKeys.TOPIC_L1_SYNC);

                    // 4. 组装
                    return new LayeredCache(name, l2Cache, topic, INSTANCE_ID, policy);
                });
            }

            @Override
            public Collection<String> getCacheNames() {
                return cacheMap.keySet();
            }
        };
    }
}
