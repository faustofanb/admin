package io.github.faustofan.admin.system.domain.service

import io.github.faustofan.admin.system.domain.entity.MenuType
import io.github.faustofan.admin.system.domain.entity.SysMenu


// ==========================================
// 计算属性 (Computed Properties)
// ==========================================

/**
 * 是否是目录
 */
val SysMenu.isDirectory: Boolean
    get() = this.type == MenuType.DIRECTORY

/**
 * 是否是链接/页面
 */
val SysMenu.isMenu: Boolean
    get() = this.type == MenuType.MENU

/**
 * 是否是按钮/权限点
 */
val SysMenu.isButton: Boolean
    get() = this.type == MenuType.BUTTON

/**
 * 获取路由地址 (如果是按钮则没有路由)
 */
fun SysMenu.getEffectiveRoute(): String? {
    return if (isButton) null else this.path
}