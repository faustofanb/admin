package io.github.faustofan.admin.shared.domain.model

import org.babyfish.jimmer.sql.MappedSuperclass

/**
 * 租户隔离接口
 * 所有需要租户隔离的表都继承此接口
 */
@MappedSuperclass
interface TenantAware : BaseEntity {
    // 这里的 tenantId 通常不需要手动 set，
    // 而是通过 Jimmer 的 DraftInterceptor 自动注入，
    // 并在查询时通过 Global Filter 自动过滤。
    val tenantId: Long
}