package io.github.faustofan.admin.system.domain.service

import io.github.faustofan.admin.system.domain.entity.SysUser
import io.github.faustofan.admin.system.domain.entity.UserStatus
import io.github.faustofan.admin.system.domain.entity.copy
import org.springframework.security.crypto.password.PasswordEncoder


// ==========================================
// 业务校验逻辑 (Invariants)
// ==========================================

/**
 * 校验密码是否正确
 */
fun SysUser.validatePassword(rawPassword: String, encoder: PasswordEncoder): Boolean {
    return encoder.matches(rawPassword, this.passwordHash)
}

/**
 * 校验账号是否可用
 */
fun SysUser.checkActive() {
    if (this.status == UserStatus.DISABLED) {
        throw IllegalStateException("账号已被禁用")
    }
    if (this.status == UserStatus.LOCKED) {
        throw IllegalStateException("账号已被锁定")
    }
}

/**
 * 判断是否是超级管理员 (假设 admin 用户名为 superadmin)
 */
val SysUser.isSuperAdmin: Boolean
    get() = this.username == "admin" || this.username == "superadmin"

// ==========================================
// 状态变更行为 (State Transitions)
// 返回修改后的新实体，供 Service 层保存
// ==========================================

/**
 * 修改密码
 * @return 变更后的实体
 */
fun SysUser.changePassword(newPasswordRaw: String, encoder: PasswordEncoder): SysUser {
    return this.copy {
        passwordHash = encoder.encode(newPasswordRaw).toString()
    }
}

/**
 * 锁定账号
 */
fun SysUser.lock(): SysUser {
    if (this.isSuperAdmin) throw IllegalArgumentException("无法锁定超级管理员")
    return this.copy {
        status = UserStatus.LOCKED
    }
}

/**
 * 解锁账号
 */
fun SysUser.unlock(): SysUser {
    return this.copy {
        status = UserStatus.ACTIVE
    }
}

/**
 * 更新个人资料 (演示部分更新)
 */
fun SysUser.updateProfile(nickname: String?, email: String?, phone: String?): SysUser {
    return this.copy {
        nickname?.let { this.nickname = it }
        email?.let { this.email = it }
        phone?.let { this.phone = it }
    }
}