package io.github.faustofan.admin.shared.cache.constants;

/**
 * 缓存常量池 (扁平化设计)
 * 仅包含 Spring Cache 相关的 CacheName 和 SpEL Key 前缀
 */
public final class CacheKeys {

    private CacheKeys() {
        throw new UnsupportedOperationException("Constants class cannot be instantiated");
    }

    // 全局前缀 (内部使用)
    private static final String APP_PREFIX = "ADMIN:CACHE:";

    // ========================================================================
    // 1. Cache Names (缓存区名称)
    // 用于 @CacheConfig(cacheNames = ...) 和 CachePolicy
    // ========================================================================

    /** 默认缓存区 */
    public static final String DEFAULT_CACHE = APP_PREFIX + "DEFAULT";

    /** 认证用户缓存 (Map结构) */
    public static final String AUTH_USER_CACHE = APP_PREFIX + "AUTH:USER";

    /** 系统配置缓存 */
    public static final String SYS_CONFIG_CACHE = APP_PREFIX + "SYS:CONFIG";

    // ========================================================================
    // 2. SpEL Key Prefixes (Key前缀)
    // 用于 @Cacheable(key = ...) 拼接
    // 注意：值必须包含单引号 "'...'"，这是为了符合 Spring EL 语法
    // ========================================================================

    // --- Auth 模块 ---
    /** 这里的 key 最终解析为: ID:123 */
    public static final String AUTH_KEY_ID = "'ID:'";

    /** 这里的 key 最终解析为: NAME:1001:admin */
    public static final String AUTH_KEY_NAME = "'NAME:'";

    // --- System 模块 ---
    public static final String SYS_KEY_GLOBAL = "'GLOBAL:'";

    // ========================================================================
    // 3. Topics (缓存同步广播通道)
    // ========================================================================
    public static final String TOPIC_L1_SYNC = APP_PREFIX + "TOPIC:L1_SYNC";
}
