package io.github.faustofan.admin.system.domain.event

import io.github.faustofan.admin.shared.domain.event.DomainEvent

/**
 * 组织机构变更 (创建/修改/删除)
 * 场景：如果前端缓存了组织树，需要通知前端刷新，或者清除后端组织树缓存
 */
data class OrgUpdatedEvent(
    val orgId: Long,
    val tenantId: String,
    val type: UpdateType //枚举：CREATED, UPDATED, DELETED
) : DomainEvent()

/**
 * 菜单变更
 * 场景：清除菜单树缓存
 */
data class MenuUpdatedEvent(
    val menuId: Long,
    val title: String,
    val type: UpdateType
) : DomainEvent()

enum class UpdateType {
    CREATED, UPDATED, DELETED
}