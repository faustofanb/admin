package io.github.faustofan.admin.system.domain.entity

import io.github.faustofan.admin.common.TenantAware
import org.babyfish.jimmer.sql.*

@Entity
interface SysMenu : TenantAware {

    @Key // 同一层级下 title 不能重复，结合 parentId
    val title: String

    // 树形结构
    @ManyToOne
    @Key
    val parent: SysMenu?

    @OneToMany(mappedBy = "parent")
    val children: List<SysMenu>

    // 类型
    val type: MenuType

    // 路由路径 (如: /system/user)
    val path: String?

    // 组件路径 (如: layout/System/User/index)
    val component: String?

    // 权限标识 (如: user:add, user:delete)
    val permission: String?

    // 图标
    val icon: String?

    // 排序号
    val sortOrder: Int

    // 反向关联：拥有此菜单的角色
    @ManyToMany(mappedBy = "menus")
    val roles: List<SysRole>
}

@EnumType
enum class MenuType {
    DIRECTORY, // 目录
    MENU,      // 菜单
    BUTTON     // 按钮/权限
}