package io.github.faustofan.admin.shared.distributed.constants;

import java.time.Duration;

/**
 * Redis Key 注册中心
 */
public enum RedisKeyRegistry implements KeyDefinition {

    /** 幂等性控制: ADMIN:SYS:GOV:IDEM:{token} */
    GOV_IDEMPOTENCY("SYS", "GOV:IDEM", Duration.ofHours(48), "接口幂等性Token"),

    /** 分布式锁: ADMIN:SYS:GOV:LOCK:{key} */
    GOV_LOCK("SYS", "GOV:LOCK", Duration.ZERO, "分布式锁"),

    /** 黑名单: ADMIN:SEC:BLOCK:{id} */
    SEC_BLACKLIST("SEC", "BLOCK", Duration.ofDays(7), "安全管控黑名单"),

    /** 系统配置: ADMIN:SYS:CONF:{key} */
    SYS_CONFIG("SYS", "CONF", Duration.ofDays(30), "系统全局配置");

    private final String module;
    private final String function;
    private final Duration ttl;
    private final String desc;

    RedisKeyRegistry(String module, String function, Duration ttl, String desc) {
        this.module = module;
        this.function = function;
        this.ttl = ttl;
        this.desc = desc;
    }

    @Override public String getModule() { return module; }
    @Override public String getFunction() { return function; }
    @Override public Duration getTtl() { return ttl; }
    @Override public String getDescription() { return desc; }
}