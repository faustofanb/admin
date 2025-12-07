package io.github.faustofan.admin.shared.infrastructure.cache

/**
 * 缓存 Key 常量
 *
 * 统一管理所有缓存 Key，便于维护和避免冲突
 *
 * 命名规范：
 * - 使用冒号分隔层级
 * - 格式：模块:实体:标识[:子项]
 * - 示例：sys:user:1, sys:user:1:permissions
 */
object CacheKeys {

    // ==========================================
    // 用户相关
    // ==========================================

    /** 用户基本信息: sys:user:{userId} */
    fun user(userId: Long) = "sys:user:$userId"

    /** 用户登录信息: sys:user:{userId}:login */
    fun userLogin(userId: Long) = "sys:user:$userId:login"

    /** 用户登录信息（按用户名）: sys:user:login:{tenantId}:{username} */
    fun userLoginByUsername(tenantId: Long, username: String) = "sys:user:login:$tenantId:$username"

    /** 用户权限: sys:user:{userId}:permissions */
    fun userPermissions(userId: Long) = "sys:user:$userId:permissions"

    /** 用户角色: sys:user:{userId}:roles */
    fun userRoles(userId: Long) = "sys:user:$userId:roles"

    /** 用户菜单: sys:user:{userId}:menus */
    fun userMenus(userId: Long) = "sys:user:$userId:menus"

    /** 用户相关缓存模式: sys:user:{userId}:* */
    fun userPattern(userId: Long) = "sys:user:$userId:*"

    // ==========================================
    // 认证相关
    // ==========================================

    /** 用户 Token 黑名单: auth:token:blacklist:{tokenId} */
    fun tokenBlacklist(tokenId: String) = "auth:token:blacklist:$tokenId"

    /** 刷新 Token: auth:refresh:{userId} */
    fun refreshToken(userId: Long) = "auth:refresh:$userId"

    // ==========================================
    // 租户相关
    // ==========================================

    /** 租户信息: sys:tenant:{tenantId} */
    fun tenant(tenantId: Long) = "sys:tenant:$tenantId"

    /** 租户列表: sys:tenant:list */
    const val TENANT_LIST = "sys:tenant:list"

    /** 租户下所有缓存模式: *:tenant:{tenantId}:* */
    fun tenantPattern(tenantId: Long) = "*tenant_id:$tenantId*"

    // ==========================================
    // 组织相关
    // ==========================================

    /** 组织信息: sys:org:{orgId} */
    fun org(orgId: Long) = "sys:org:$orgId"

    /** 组织树: sys:org:tree:{tenantId} */
    fun orgTree(tenantId: Long) = "sys:org:tree:$tenantId"

    // ==========================================
    // 菜单相关
    // ==========================================

    /** 菜单信息: sys:menu:{menuId} */
    fun menu(menuId: Long) = "sys:menu:$menuId"

    /** 菜单树: sys:menu:tree:{tenantId} */
    fun menuTree(tenantId: Long) = "sys:menu:tree:$tenantId"

    // ==========================================
    // 角色相关
    // ==========================================

    /** 角色信息: sys:role:{roleId} */
    fun role(roleId: Long) = "sys:role:$roleId"

    /** 角色权限: sys:role:{roleId}:permissions */
    fun rolePermissions(roleId: Long) = "sys:role:$roleId:permissions"

    /** 角色菜单: sys:role:{roleId}:menus */
    fun roleMenus(roleId: Long) = "sys:role:$roleId:menus"

    // ==========================================
    // 字典相关
    // ==========================================

    /** 字典项: sys:dict:{dictCode} */
    fun dictItems(dictCode: String) = "sys:dict:$dictCode"

    /** 所有字典: sys:dict:all */
    const val DICT_ALL = "sys:dict:all"

    // ==========================================
    // 权限相关
    // ==========================================

    /** 权限信息: sys:permission:{permissionId} */
    fun permission(permissionId: Long) = "sys:permission:$permissionId"

    /** 权限列表: sys:permission:list */
    const val PERMISSION_LIST = "sys:permission:list"

    // ==========================================
    // 通用模式（用于批量删除）
    // ==========================================

    /** 系统模块缓存模式 */
    const val SYS_PATTERN = "sys:*"

    /** 认证模块缓存模式 */
    const val AUTH_PATTERN = "auth:*"

    // ==========================================
    // 布隆过滤器
    // ==========================================

    /** 用户名布隆过滤器 */
    const val BLOOM_USERNAME = "bloom:username"

    /** 手机号布隆过滤器 */
    const val BLOOM_PHONE = "bloom:phone"

    /** 邮箱布隆过滤器 */
    const val BLOOM_EMAIL = "bloom:email"

    /** 租户编码布隆过滤器 */
    const val BLOOM_TENANT_CODE = "bloom:tenant_code"
}
