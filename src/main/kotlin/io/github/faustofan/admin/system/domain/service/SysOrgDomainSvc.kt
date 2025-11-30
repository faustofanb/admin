package io.github.faustofan.admin.system.domain.service

import io.github.faustofan.admin.system.domain.entity.SysOrg


// ==========================================
// 树形结构逻辑
// ==========================================

/**
 * 校验是否可以将当前组织移动到新的父节点下
 * (防止将自己移动到自己的子节点下，形成环)
 *
 * 注：这需要加载了 children 的实体才能校验，或者在 Service 层配合递归查询
 */
fun SysOrg.canMoveTo(targetParent: SysOrg?): Boolean {
    if (targetParent == null) return true // 移动到根节点
    if (targetParent.id == this.id) return false // 不能做自己的父亲

    // 这里只是简单的内存校验，复杂的环检测通常需要数据库配合
    return true
}

/**
 * 创建子部门
 */
fun SysOrg.createChild(name: String): SysOrg {
    // 这里返回一个新的 Org 对象，并设置好 Parent
    // 注意：Jimmer 的 new 语法
    return SysOrg {
        this.name = name
        this.parent = this@createChild // 设置父节点为当前对象
        this.tenantId = this@createChild.tenantId // 继承租户
    }
}