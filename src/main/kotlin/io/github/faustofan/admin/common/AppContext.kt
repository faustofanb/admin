package io.github.faustofan.admin.common

import org.babyfish.jimmer.sql.EnumType

data class AppContext (
    val tenantId: Long?,
    val userId: Long?,
    val username: String?, // 对应 createdBy
    val orgId: Long?,
    val requestId: String?,
    val traceId: Long?,

    // 获取当前用户的所有角色信息（包含 DataScope 和 自定义部门ID列表）
    // 建议在登录时缓存这些信息，不要每次都查库
    val currentUserRoles: List<RoleDataScopeInfo>?,

    // 是否是超级管理员（跳过所有检查）
    val isSuperAdmin: Boolean
)

/**
 * 数据权限枚举
 */
@EnumType
enum class DataScope(val label: String) {
    ALL("全部数据"),             // 能看所有数据
    SAME_DEPT("本部门数据"),      // 只能看 orgId = current.orgId
    SAME_DEPT_TREE("本部门及子部门"), // orgId in (current.orgId and children)
    CUSTOM("自定义部门"),         // orgId in (role.specificOrgs)
    SELF("仅本人数据");           // createdBy = current.username
}

data class RoleDataScopeInfo(
    val dataScope: DataScope,
    val specificOrgIds: List<Long> = emptyList()
)
