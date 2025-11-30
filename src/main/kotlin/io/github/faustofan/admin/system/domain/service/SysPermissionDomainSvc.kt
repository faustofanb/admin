package io.github.faustofan.admin.system.domain.service

import io.github.faustofan.admin.system.domain.entity.SysPermission
import io.github.faustofan.admin.system.domain.entity.copy

// ==========================================
// 权限标识符逻辑
// ==========================================

/**
 * 校验权限代码格式
 * 例如强制要求格式为 "resource:action" (user:add)
 */
fun SysPermission.validateCodeFormat() {
    if (!this.code.contains(":")) {
        throw IllegalArgumentException("权限标识符 [${this.code}] 格式错误，建议格式为 'resource:action'")
    }
}

/**
 * 权限匹配逻辑 (支持简单的通配符)
 * 场景：判断当前权限 "user:view" 是否被 target "user:*" 所包含
 */
fun SysPermission.isImpliedBy(wildcardPermissionCode: String): Boolean {
    // 1. 完全相等
    if (this.code == wildcardPermissionCode) return true

    // 2. 通配符逻辑
    // 如果拥有 "user:*"，则包含 "user:add", "user:delete"
    if (wildcardPermissionCode.endsWith(":*")) {
        val prefix = wildcardPermissionCode.removeSuffix(":*")
        return this.code.startsWith("$prefix:")
    }

    return false
}

// ==========================================
// 状态变更
// ==========================================

fun SysPermission.rename(newName: String, newDescription: String?): SysPermission {
    return this.copy {
        this.name = newName
        newDescription?.let { this.description = it }
    }
}