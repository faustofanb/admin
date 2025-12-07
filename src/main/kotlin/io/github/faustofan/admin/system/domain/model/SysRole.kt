package io.github.faustofan.admin.system.domain.model

import io.github.faustofan.admin.shared.application.context.DataScope
import io.github.faustofan.admin.shared.domain.model.TenantAware
import org.babyfish.jimmer.sql.Entity
import org.babyfish.jimmer.sql.JoinTable
import org.babyfish.jimmer.sql.Key
import org.babyfish.jimmer.sql.ManyToMany

@Entity
interface SysRole : TenantAware {

    @Key
    val code: String

    val name: String

    val description: String

    // 1. 关联菜单 (控制页面可见性)
    @ManyToMany
    @JoinTable(
        name = "sys_role_menu_mapping",
        joinColumnName = "role_id",
        inverseJoinColumnName = "menu_id"
    )
    val menus: List<SysMenu>

    // 2. 关联权限 (控制API调用能力)
    // 中间表自动生成: sys_role_permission_mapping
    @ManyToMany
    @JoinTable(
        name = "sys_role_permission_mapping",
        joinColumnName = "role_id",
        inverseJoinColumnName = "permission_id"
    )
    val permissions: List<SysPermission>

    @ManyToMany(mappedBy = "roles")
    val users: List<SysUser>

    // 数据范围策略
    val dataScope: DataScope

    // 当 dataScope = CUSTOM 时，关联具体的部门
    @ManyToMany
    @JoinTable(
        name = "sys_role_org_mapping",
        joinColumnName = "role_id",
        inverseJoinColumnName = "org_id"
    )
    val specificOrgs: List<SysOrg>
}

