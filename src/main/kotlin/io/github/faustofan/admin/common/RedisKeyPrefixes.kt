package io.github.faustofan.admin.common

/**
 * Redis 键前缀工具类，统一管理各类缓存键的前缀生成。
 */
object RedisKeyPrefixes {
    /** 分隔符，用于拼接 Redis 键各部分 */
    const val PREFIX_SPITTER = ":"
    /** 租户前缀 */
    const val TENANT_PREFIX = "TENANT"
    /** 用户缓存前缀 */
    const val USER_CACHE_PREFIX = "USER"
    /** 权限缓存前缀 */
    const val PERMISSION_CACHE_PREFIX = "PERM"
    /** 菜单缓存前缀 */
    const val MENU_CACHE_PREFIX = "MENU"
    /** 资源缓存前缀 */
    const val RESOURCE_CACHE_PREFIX = "RES"
    /** Excel 操作缓存前缀 */
    const val EXCEL_PREFIX = "EXCEL"
    /** 限流缓存前缀 */
    const val RATE_LIMIT_PREFIX = "RATE"

    /**
     * 生成租户相关的 Redis 键前缀。
     * @param tenantId 租户 ID
     * @return 租户前缀字符串
     */
    fun tenant(tenantId: String) = TENANT_PREFIX + PREFIX_SPITTER + tenantId + PREFIX_SPITTER

    /**
     * 生成租户下用户相关的 Redis 键前缀。
     * @param tenantId 租户 ID
     * @param userId 用户 ID
     * @return 用户缓存前缀字符串
     */
    fun tenantUser(tenantId: String, userId: String) = tenant(tenantId) + USER_CACHE_PREFIX + userId

    /**
     * 生成租户下用户权限相关的 Redis 键前缀。
     * @param tenantId 租户 ID
     * @param userId 用户 ID
     * @return 权限缓存前缀字符串
     */
    fun tenantPerm(tenantId: String, userId: String) =  tenantUser(tenantId, userId) + PREFIX_SPITTER + PERMISSION_CACHE_PREFIX

    /**
     * 生成租户下菜单相关的 Redis 键前缀。
     * @param tenantId 租户 ID
     * @param menuId 菜单 ID
     * @return 菜单缓存前缀字符串
     */
    fun tenantMenu(tenantId: String, menuId: String) = tenant(tenantId) + MENU_CACHE_PREFIX + menuId

    /**
     * 生成租户下资源相关的 Redis 键前缀。
     * @param tenantId 租户 ID
     * @param resourceCode 资源编码
     * @return 资源缓存前缀字符串
     */
    fun tenantRes(tenantId: String, resourceCode: String) = tenant(tenantId) + RESOURCE_CACHE_PREFIX + resourceCode

    /**
     * 生成租户下 Excel 操作相关的 Redis 键前缀。
     * @param tenantId 租户 ID
     * @param excelOpt Excel 操作标识
     * @return Excel 操作缓存前缀字符串
     */
    fun tenantExcel(tenantId: String, excelOpt: String) = tenant(tenantId) + EXCEL_PREFIX + excelOpt

    /**
     * 生成租户下限流相关的 Redis 键前缀。
     * @param tenantId 租户 ID
     * @param resourceCode 资源编码
     * @return 限流缓存前缀字符串
     */
    fun tenantRateKey(tenantId: String, resourceCode: String) = tenant(tenantId) + RATE_LIMIT_PREFIX + resourceCode
}