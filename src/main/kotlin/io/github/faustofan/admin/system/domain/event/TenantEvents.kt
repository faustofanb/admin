package io.github.faustofan.admin.system.domain.event

import io.github.faustofan.admin.shared.domain.event.DomainEvent
import io.github.faustofan.admin.system.domain.model.TenantStatus
import java.time.LocalDateTime

/**
 * 租户已创建
 * 场景：初始化租户资源，发送开通成功的邮件
 */
data class TenantCreatedEvent(
    val tenantId: Long,
    val name: String,
    val code: String,
    val contactName: String?,
    val email: String? // 联系人邮箱
) : DomainEvent()

/**
 * 租户已更新
 * 场景：清除缓存，同步信息
 */
data class TenantUpdatedEvent(
    val tenantId: Long,
    val name: String,
    val code: String
) : DomainEvent()

/**
 * 租户状态变更
 * 场景：租户被禁用时，强制踢出该租户下的所有在线用户
 */
data class TenantStatusChangedEvent(
    val tenantId: Long,
    val name: String,
    val oldStatus: TenantStatus,
    val newStatus: TenantStatus
) : DomainEvent()

/**
 * 租户已续费
 * 场景：发送续费成功通知，更新计费系统
 */
data class TenantRenewedEvent(
    val tenantId: Long,
    val name: String,
    val daysAdded: Long,
    val newExpireTime: LocalDateTime?
) : DomainEvent()

/**
 * 租户已删除
 * 场景：清理租户资源，清除所有缓存
 */
data class TenantDeletedEvent(
    val tenantId: Long,
    val code: String
) : DomainEvent()
