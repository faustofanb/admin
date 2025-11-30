package io.github.faustofan.admin.system.domain.events

import io.github.faustofan.admin.system.domain.entity.UserStatus

/**
 * 用户已创建
 * 场景：发送欢迎邮件，初始化用户默认设置
 */
data class UserCreatedEvent(
    val userId: Long,
    val username: String,
    val nickname: String?,
    val email: String?,
    val tenantId: String
) : DomainEvent()

/**
 * 用户密码已修改
 * 场景：发送安全警告邮件，强制下线其他设备
 */
data class UserPasswordChangedEvent(
    val userId: Long,
    val username: String,
) : DomainEvent()

/**
 * 用户状态变更 (锁定/解锁/禁用)
 * 场景：用户被锁定时，清除 Session/Token
 */
data class UserStatusChangedEvent(
    val userId: Long,
    val username: String,
    val oldStatus: UserStatus,
    val newStatus: UserStatus
) : DomainEvent()

/**
 * 用户资料已更新
 * 场景：同步信息到 ES 或其他搜索组件
 */
data class UserProfileUpdatedEvent(
    val userId: Long,
    val username: String
) : DomainEvent()

/**
 * 用户已删除
 * 场景：清理关联数据，记录审计日志
 */
data class UserDeletedEvent(
    val userId: Long,
    val username: String,
    val tenantId: String
) : DomainEvent()