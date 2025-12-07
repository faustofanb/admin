package io.github.faustofan.admin.system.domain.event

import io.github.faustofan.admin.shared.domain.event.DomainEvent

/**
 * 角色已创建
 */
data class RoleCreatedEvent(
    val roleId: Long,
    val name: String,
    val code: String
) : DomainEvent()

/**
 * 角色关联菜单/权限已更新
 * 场景：【关键】清除 Redis 中的 URL-Role 映射缓存，或者让所有拥有该角色的用户 Token 失效
 */
data class RolePermissionsUpdatedEvent(
    val roleId: Long,
    val roleCode: String,
    val tenantId: String
) : DomainEvent()

/**
 * 角色已删除
 */
data class RoleDeletedEvent(
    val roleId: Long,
    val roleCode: String,
    val tenantId: String
) : DomainEvent()