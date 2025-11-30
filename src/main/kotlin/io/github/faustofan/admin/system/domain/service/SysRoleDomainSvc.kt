package io.github.faustofan.admin.system.domain.service

import io.github.faustofan.admin.system.domain.entity.SysRole
import io.github.faustofan.admin.system.domain.entity.copy

// ==========================================
// 计算属性 & 校验
// ==========================================

/**
 * 是否是系统内置的超级管理员角色
 * 约定：code 为 "admin" 或 "super_admin" 的不可删除/修改关键信息
 */
val SysRole.isSystemAdmin: Boolean
    get() = this.code == "admin" || this.code == "super_admin"

/**
 * 校验角色是否允许被修改/删除
 */
fun SysRole.checkMutable() {
    if (isSystemAdmin) {
        throw IllegalStateException("系统内置管理员角色不允许修改或删除")
    }
}

// ==========================================
// 业务行为
// ==========================================

/**
 * 更新基本信息
 */
fun SysRole.updateInfo(name: String?, description: String?): SysRole {
    this.checkMutable() // 前置守卫

    return this.copy {
        name?.let { this.name = it }
        description?.let { this.description = it }
    }
}

/**
 * (辅助方法) 检查该角色是否包含某个菜单权限
 * 注意：这依赖于 role.menus 已经被 fetch 出来，否则会抛出 Jimmer 的 UnloadedException
 */
fun SysRole.hasMenuPermission(menuId: Long): Boolean {
    // 这是一个内存检查，假设 Entity 已经抓取了 menus
    return this.menus.any { it.id == menuId }
}