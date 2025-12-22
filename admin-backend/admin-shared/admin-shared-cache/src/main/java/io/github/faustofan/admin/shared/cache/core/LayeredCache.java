package io.github.faustofan.admin.shared.cache.core;

import java.util.concurrent.Callable;

import org.redisson.api.RTopic;
import org.slf4j.Logger;
import org.springframework.cache.support.AbstractValueAdaptingCache;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import io.github.faustofan.admin.shared.cache.enums.CachePolicy;

/**
 * L1 (Caffeine) + L2 (Redisson) 多级缓存装饰器
 */
public class LayeredCache extends AbstractValueAdaptingCache {

    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(LayeredCache.class);

    private final String name;
    private final Cache<Object, Object> l1Cache; // L1
    private final org.springframework.cache.Cache l2Cache; // L2 (Redisson的原生Cache)
    private final RTopic topic; // 广播通道
    private final String instanceId; // 实例ID

    public LayeredCache(
            String name,
            org.springframework.cache.Cache l2Cache,
            RTopic topic,
            String instanceId,
            CachePolicy policy) {
        super(true); // 允许 null 值
        this.name = name;
        this.l2Cache = l2Cache;
        this.topic = topic;
        this.instanceId = instanceId;

        // 初始化 L1 (Caffeine)
        this.l1Cache = Caffeine.newBuilder()
                .expireAfterWrite(policy.l1Ttl())
                .maximumSize(policy.l1MaxSize())
                // 生产环境建议开启统计
                .recordStats()
                .build();
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Object getNativeCache() {
        return this.l1Cache;
    }

    /**
     * 简单的 Get 操作
     */
    @Override
    @Nullable
    protected Object lookup(Object key) {
        // 1. 查 L1
        Object l1Value = l1Cache.getIfPresent(key);
        if (l1Value != null) {
            logger.info("[L1 Cache Hit] key={}", key);
            return l1Value;
        }

        // 2. 查 L2 (委托给 Redisson)
        // Redisson 返回的是 ValueWrapper，需要解包
        ValueWrapper l2Wrapper = l2Cache.get(key);
        if (l2Wrapper != null) {
            logger.info("[L2 Cache Hit] key={}", key);
            Object l2Value = l2Wrapper.get();
            // 3. 回填 L1 (如果 L2 有值)
            if (l2Value != null) {
                // 注意：这里要存 storeValue 还是原始值取决于 Caffeine 的用法
                // Caffeine 可以直接存对象，StoreValue 处理通常由 AbstractValueAdaptingCache 负责
                // 但这里为了稳妥，直接存原始对象即可，因为 getIfPresent 拿出来也是原始对象
                l1Cache.put(key, l2Value);
            }
            return l2Value;
        }

        return null;
    }

    /**
     * 逻辑：L1 -> L2 (带锁/Loader) -> 回填 L1
     */
    @Override
    @SuppressWarnings("unchecked")
    public <T> T get(@NonNull Object key, @NonNull Callable<T> valueLoader) {
        // 1. Fast Path: 查 L1
        // 注意：Caffeine 存储的是 raw value，这里需要 wrap 吗？
        // AbstractValueAdaptingCache 的 get 方法会自动处理 fromStoreValue
        // 所以我们在这里只要返回 raw value 即可。
        Object l1Value = l1Cache.getIfPresent(key);
        if (l1Value != null) {
            logger.info("[L1 Cache Hit] key={}", key);
            return (T) fromStoreValue(l1Value);
        }

        // 2. Delegate to L2 (Redisson)
        // RedissonSpringCache 实现了 get(key, loader)，它内部处理了 sync/lock 逻辑
        // 如果 L2 也没有，Redisson 会调用 loader 加载数据并存入 Redis
        T l2Value = l2Cache.get(key, valueLoader);

        // 3. 回填 L1
        // 只有当数据加载成功后才回填
        if (l2Value != null) {
            logger.info("[L2 Cache Hit] key={}", key);
            l1Cache.put(key, toStoreValue(l2Value));
        }

        return l2Value;
    }

    @Override
    public void put(@NonNull Object key, @Nullable Object value) {
        logger.info("[Cache Put] key={}", key);
        // 1. 写 L2
        l2Cache.put(key, value);
        // 2. 写 L1
        l1Cache.put(key, toStoreValue(value));
        // 3. 广播失效
        publishInvalidate(key);
    }

    @Override
    public void evict(@NonNull Object key) {
        logger.info("[Cache Evict] key={}", key);
        l2Cache.evict(key);
        l1Cache.invalidate(key);
        publishInvalidate(key);
    }

    @Override
    public void clear() {
        logger.info("[Cache Clear]");
        l2Cache.clear();
        l1Cache.invalidateAll();
        publishInvalidate(null);
    }

    // --- 消息通知逻辑 ---
    private void publishInvalidate(Object key) {
        // JDK 25 虚拟线程：轻量级异步发送
        Thread.ofVirtual().start(() -> {
            try {
                topic.publish(new CacheInvalidateMsg(this.name, key, this.instanceId));
            } catch (Exception e) {
                // 生产级：建议接入 Prometheus/Micrometer 监控计数
                logger.error("Cache Sync Failed: {}", e.getMessage());
            }
        });
    }

    /**
     * 处理接收到的失效消息
     */
    public void handleMsg(CacheInvalidateMsg msg) {
        if (this.instanceId.equals(msg.sourceInstanceId()))
            return;

        switch (msg.key()) {
            case null -> l1Cache.invalidateAll();
            default -> l1Cache.invalidate(msg.key());
        }
    }
}
