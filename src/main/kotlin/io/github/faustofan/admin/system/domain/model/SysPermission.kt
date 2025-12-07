package io.github.faustofan.admin.system.domain.model

import io.github.faustofan.admin.shared.domain.model.TenantAware
import org.babyfish.jimmer.sql.Entity
import org.babyfish.jimmer.sql.Key
import org.babyfish.jimmer.sql.ManyToMany

@Entity
interface SysPermission : TenantAware {

    // 权限标识符 (唯一键)
    // 例如: "user:create", "order:export", "system:log:clean"
    @Key
    val code: String

    // 权限名称
    // 例如: "新增用户", "导出订单"
    val name: String

    // 资源分组 (可选，用于前端分组展示，如: "用户管理")
    val groupName: String?

    // 描述
    val description: String?

    // 关联：拥有此权限的角色 (多对多)
    // 反向维护
    @ManyToMany(mappedBy = "permissions")
    val roles: List<SysRole>
}