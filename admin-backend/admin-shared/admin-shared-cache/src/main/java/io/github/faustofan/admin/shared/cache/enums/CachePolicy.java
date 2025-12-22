package io.github.faustofan.admin.shared.cache.enums;

import java.time.Duration;
import java.util.Arrays;

import io.github.faustofan.admin.shared.cache.constants.CacheKeys;

/**
 * 缓存策略配置
 * 统一管理 L1 (Caffeine) 和 L2 (Redis) 的过期时间
 */
public enum CachePolicy {

    // 默认策略：L1=10分钟, L2=1小时
    DEFAULT(CacheKeys.DEFAULT_CACHE, Duration.ofMinutes(10), Duration.ofHours(1), 1000),

    // 系统配置：变动少，缓存久 (L1=1小时, L2=24小时)
    SYS_CONFIG(CacheKeys.SYS_CONFIG_CACHE, Duration.ofHours(1), Duration.ofHours(24), 5000),

    // 用户认证信息：L1 保留 1 分钟防抖，L2 保留 30 分钟
    // 为什么 L1 只有 1 分钟？为了让权限变更/封号能较快在所有节点生效
    USER_AUTH(CacheKeys.AUTH_USER_CACHE, Duration.ofMinutes(1), Duration.ofMinutes(30), 2000);

    private final String cacheName;
    private final Duration l1Ttl;
    private final Duration l2Ttl;
    private final long l1MaxSize;

    CachePolicy(String cacheName, Duration l1Ttl, Duration l2Ttl, long l1MaxSize) {
        this.cacheName = cacheName;
        this.l1Ttl = l1Ttl;
        this.l2Ttl = l2Ttl;
        this.l1MaxSize = l1MaxSize;
    }

    /**
     * 根据缓存名称查找策略
     * JDK 25 Stream 风格
     */
    public static CachePolicy resolve(String name) {
        return Arrays.stream(values())
                .filter(p -> p.cacheName.equals(name))
                .findFirst()
                .orElse(DEFAULT);
    }

    public String cacheName() {
        return cacheName;
    }

    public Duration l1Ttl() {
        return l1Ttl;
    }

    public Duration l2Ttl() {
        return l2Ttl;
    }

    public long l1MaxSize() {
        return l1MaxSize;
    }
}
