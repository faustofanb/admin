package io.github.faustofan.admin.shared.cache.util;

import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import io.github.faustofan.admin.shared.common.exception.SystemException;
import io.github.faustofan.admin.shared.common.exception.errcode.SystemErrorCode;

/**
 * 生产级函数式缓存工具类
 * 基于 Spring Cache Abstraction，底层自动适配 L1+L2
 */
@Component
public class CacheUtils {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final CacheManager cacheManager;

    // 使用 @Lazy 防止循环依赖，因为 CacheManager 初始化可能依赖其他 Bean
    public CacheUtils(@Lazy CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    /**
     * 核心方法：获取缓存，如果不存在则计算 (Get Or Compute)
     * 自动处理 L1/L2 穿透和防击穿锁 (由底层 LayeredCache.get(key, loader) 保证)
     *
     * @param cacheName 缓存名称 (对应 CachePolicy 枚举)
     * @param key       缓存 Key
     * @param loader    数据加载器 (函数式接口)
     * @param <T>       返回值类型
     */
    @SuppressWarnings("unchecked")
    public <T> T get(String cacheName, Object key, Callable<T> loader) {
        Cache cache = getCache(cacheName);
        // 这里直接调用我们在 LayeredCache 中修复过的 get(key, loader)
        // 它会自动处理：查L1 -> 查L2 -> 分布式锁查DB -> 回填L2 -> 回填L1
        return cache.get(key, loader);
    }

    /**
     * 变体：显式指定类型，避免泛型擦除后的强转警告
     */
    public <T> T get(String cacheName, Object key, Class<T> type, Callable<T> loader) {
        Cache cache = getCache(cacheName);
        return cache.get(key, loader);
    }

    /**
     * 普通获取 (可能为 null)
     */
    public <T> Optional<T> get(String cacheName, Object key, Class<T> type) {
        return Optional.ofNullable(getCache(cacheName).get(key, type));
    }

    /**
     * 手动写入
     */
    public void put(String cacheName, Object key, Object value) {
        getCache(cacheName).put(key, value);
    }

    /**
     * 手动删除
     */
    public void evict(String cacheName, Object key) {
        getCache(cacheName).evict(key);
    }

    /**
     * 进阶特性：异步刷新 (Fire-and-Forget)
     * 场景：主流程返回旧值(或者不等待写缓存)，后台开启虚拟线程去更新缓存，不阻塞 HTTP 响应
     */
    public <T> void refreshAsync(String cacheName, Object key, Supplier<T> valueLoader) {
        // JDK 25 Virtual Thread
        Thread.ofVirtual().start(() -> {
            try {
                T value = valueLoader.get();
                put(cacheName, key, value);
                logger.info("Async cache refresh success. Key: {}", key);
            } catch (Exception e) {
                logger.error("Async cache refresh failed: {}", e.getMessage());
            }
        });
    }

    /**
     * 进阶特性：如果缓存存在则执行操作 (If Present Do)
     * 典型的 Monad 风格
     */
    public <T> void ifPresent(String cacheName, Object key, Class<T> type, Consumer<T> action) {
        T value = getCache(cacheName).get(key, type);
        if (value != null) {
            action.accept(value);
        }
    }

    // --- Private Helper ---

    private Cache getCache(String cacheName) {
        Cache cache = cacheManager.getCache(cacheName);
        if (cache == null) {
            // 生产环境通常抛出自定义业务异常
            throw new SystemException(SystemErrorCode.CACHE_ERROR);
        }
        return cache;
    }
}
