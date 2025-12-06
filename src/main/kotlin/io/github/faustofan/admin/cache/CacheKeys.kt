package io.github.faustofan.admin.cache

/**
 * 缓存 Key 常量
 *
 * 统一管理所有缓存 Key，便于维护和避免冲突
 */
object CacheKeys {

    // ==========================================
    // 认证相关
    // ==========================================

    /** 用户 Token 黑名单: user:token:blacklist:{tokenId} */
    fun tokenBlacklist(tokenId: String) = "user:token:blacklist:$tokenId"

    /** 用户登录信息: user:login:{userId} */
    fun userLogin(userId: Long) = "user:login:$userId"

    /** 用户权限: user:permissions:{userId} */
    fun userPermissions(userId: Long) = "user:permissions:$userId"

    /** 用户角色: user:roles:{userId} */
    fun userRoles(userId: Long) = "user:roles:$userId"

    // ==========================================
    // 系统管理相关
    // ==========================================

    /** 租户信息: sys:tenant:{tenantId} */
    fun tenant(tenantId: Long) = "sys:tenant:$tenantId"

    /** 租户列表: sys:tenants */
    const val TENANT_LIST = "sys:tenants"

    /** 组织树: sys:org:tree:{tenantId} */
    fun orgTree(tenantId: Long) = "sys:org:tree:$tenantId"

    /** 菜单树: sys:menu:tree:{tenantId} */
    fun menuTree(tenantId: Long) = "sys:menu:tree:$tenantId"

    /** 用户菜单: sys:menu:user:{userId} */
    fun userMenus(userId: Long) = "sys:menu:user:$userId"

    /** 角色权限: sys:role:permissions:{roleId} */
    fun rolePermissions(roleId: Long) = "sys:role:permissions:$roleId"

    // ==========================================
    // 字典相关
    // ==========================================

    /** 字典项: sys:dict:{dictCode} */
    fun dictItems(dictCode: String) = "sys:dict:$dictCode"

    /** 所有字典: sys:dicts */
    const val DICT_ALL = "sys:dicts"

    // ==========================================
    // 通用模式
    // ==========================================

    /** 租户下所有缓存模式: *:tenant:{tenantId}:* */
    fun tenantPattern(tenantId: Long) = "*:tenant:$tenantId:*"

    /** 用户相关缓存模式: user:*:{userId} */
    fun userPattern(userId: Long) = "user:*:$userId"

    /** 系统管理缓存模式 */
    const val SYS_PATTERN = "sys:*"

    // ==========================================
    // 布隆过滤器
    // ==========================================

    /** 用户名布隆过滤器 */
    const val BLOOM_USERNAME = "username"

    /** 手机号布隆过滤器 */
    const val BLOOM_PHONE = "phone"

    /** 邮箱布隆过滤器 */
    const val BLOOM_EMAIL = "email"

    /** 租户编码布隆过滤器 */
    const val BLOOM_TENANT_CODE = "tenant_code"
}
