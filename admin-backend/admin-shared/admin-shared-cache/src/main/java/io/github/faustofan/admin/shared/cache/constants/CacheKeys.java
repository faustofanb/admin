package io.github.faustofan.admin.shared.cache.constants;

import io.github.faustofan.admin.shared.common.constant.GlobalConstants;

/**
 * Spring Cache 常量池
 */
public final class CacheKeys {

    private CacheKeys() {
        throw new UnsupportedOperationException("Constants class cannot be instantiated");
    }

    // 统一缓存前缀: ADMIN:CACHE:
    private static final String CACHE_PREFIX = GlobalConstants.ROOT_PREFIX + ":CACHE:";

    // ========================================================================
    // Cache Names (用于 @Cacheable(cacheNames = ...))
    // 建议按 TTL 策略或大业务块命名
    // ========================================================================
    
    public static final String CACHE_DEFAULT = CACHE_PREFIX + "DEFAULT";
    public static final String CACHE_AUTH_USER = CACHE_PREFIX + "AUTH:USER";
    public static final String CACHE_SYS_CONFIG = CACHE_PREFIX + "SYS:CONFIG";

    // ========================================================================
    // Key Prefixes (用于 @Cacheable(key = ...))
    // 提供 raw 字符串，SpEL 拼接建议在业务层或封装的 Util 中处理，
    // 或者提供明确带单引号的常量
    // ========================================================================

    /** ID前缀 -> 'ID:' */
    public static final String KEY_ID_SPEL = "'ID:'";
    public static final String KEY_ID = spelToRaw(KEY_ID_SPEL);
    /** Name前缀 -> 'NAME:' */
    public static final String KEY_NAME_SPEL = "'NAME:'";
    public static final String KEY_NAME = spelToRaw(KEY_NAME_SPEL);

    /** 
     * 辅助方法：如果不想在代码里写 SpEL 字符串，可以使用 KeyGenerator，
     * 但如果必须用 SpEL 常量，保持上面的格式即可。
     */
    // ========================================================================
    // 3. Topics (缓存同步广播通道)
    // ========================================================================
    public static final String TOPIC_L1_SYNC = CACHE_PREFIX + "TOPIC:L1_SYNC";

    // 辅助方法：将 SpEL 常量转换为 raw 字符串
    private static String spelToRaw(String spel) {
        return spel.replace("'", "");
    }
}