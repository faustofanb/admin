package io.github.faustofan.admin.shared.common.constant;

/**
 * 全局通用常量
 * <p>定义项目级别的基础符号、前缀和通用配置</p>
 */
public final class GlobalConstants {

    private GlobalConstants() {
        throw new UnsupportedOperationException("Constants class cannot be instantiated");
    }

    /** 项目统一根前缀 (用于 Redis, Cache, MQ 等隔离) */
    public static final String ROOT_PREFIX = "ADMIN";

    /** 模块分隔符 */
    public static final String DELIMITER = ":";

    /** 默认超级管理员/系统ID */
    public static final long SYS_ID_DEFAULT = 999999999L;

    /** 通用 Trace ID 请求头 */
    public static final String TRACE_ID_HEADER = "X-Trace-Id";
}