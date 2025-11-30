package io.github.faustofan.admin.system.domain.service

import io.github.faustofan.admin.system.domain.entity.SysTenant
import io.github.faustofan.admin.system.domain.entity.TenantStatus
import io.github.faustofan.admin.system.domain.entity.copy
import java.time.LocalDateTime

// ==========================================
// 业务校验 (Invariants)
// ==========================================

/**
 * 校验租户是否有效 (状态开启 + 未过期)
 * 用于登录拦截器或核心业务检查
 */
fun SysTenant.checkValid() {
    if (this.status != TenantStatus.ENABLE) {
        throw IllegalStateException("租户 [${this.name}] 已被禁用")
    }

    val now = LocalDateTime.now()
    if (this.expireTime != null && now.isAfter(this.expireTime)) {
        throw IllegalStateException("租户 [${this.name}] 服务已过期，请续费")
    }
}

/**
 * 是否即将过期 (例如剩余不到7天，用于提示)
 */
fun SysTenant.isAboutToExpire(daysBuffer: Long = 7): Boolean {
    if (this.expireTime == null) return false // 永久租户

    val warningLine = LocalDateTime.now().plusDays(daysBuffer)
    return this.expireTime!!.isBefore(warningLine) && this.expireTime!!.isAfter(LocalDateTime.now())
}

// ==========================================
// 状态变更 (State Transitions)
// ==========================================

/**
 * 租户续费/延长有效期
 */
fun SysTenant.renew(days: Long): SysTenant {
    if (days <= 0) throw IllegalArgumentException("续费天数必须大于0")

    return this.copy {
        // 如果原本是永久(null)或已过期，则从现在开始算；否则在原基础上累加
        val baseTime = if (this@renew.expireTime == null || this@renew.expireTime!!.isBefore(LocalDateTime.now())) {
            LocalDateTime.now()
        } else {
            this@renew.expireTime!!
        }

        expireTime = baseTime.plusDays(days)
        // 续费通常意味着重新激活
        if (status == TenantStatus.DISABLE) {
            status = TenantStatus.ENABLE
        }
    }
}

/**
 * 禁用租户
 */
fun SysTenant.disable(): SysTenant {
    return this.copy {
        status = TenantStatus.DISABLE
    }
}

/**
 * 启用租户
 */
fun SysTenant.enable(): SysTenant {
    return this.copy {
        status = TenantStatus.ENABLE
    }
}