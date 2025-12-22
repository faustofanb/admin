package io.github.faustofan.admin.shared.distributed.constants;

import java.time.Duration;

/**
 * Redis Key 注册中心
 * <p>
 * 所有 Redis Key 必须在此枚举，禁止在业务代码中出现魔法字符串
 * </p>
 */
public enum RedisKeyRegistry implements KeyDefinition {

    /** 幂等性控制 Key (默认保留48小时以供查询结果) */
    GOV_IDEMPOTENCY("SYS:GOV:IDEM:", Duration.ofHours(48), "接口幂等性控制Token"),

    /** 分布式锁 Key (TTL为0表示依赖看门狗机制) */
    GOV_LOCK("SYS:GOV:LOCK:", Duration.ZERO, "分布式锁前缀"),

    /** 安全黑名单 (如 JWT 踢出，默认7天) */
    SEC_BLACKLIST("SYS:SEC:BLOCK:", Duration.ofDays(7), "安全管控黑名单"),

    /** 系统动态配置 (默认30天) */
    SYS_CONFIG("SYS:CONF:GLOBAL:", Duration.ofDays(30), "系统全局配置");

    private final String prefix;
    private final Duration ttl;
    private final String description;

    RedisKeyRegistry(String prefix, Duration ttl, String description) {
        this.prefix = prefix;
        this.ttl = ttl;
        this.description = description;
    }

    @Override
    public String getPrefix() {
        return prefix;
    }

    @Override
    public Duration getTtl() {
        return ttl;
    }

    @Override
    public String getDescription() {
        return description;
    }
}
