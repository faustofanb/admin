package io.github.faustofan.admin.shared.persistence.model;

import org.babyfish.jimmer.sql.MappedSuperclass;

/**
 * 多租户实体，包含租户ID字段
 */
@MappedSuperclass
public interface TenantAware extends BaseEntity {
    long tenantId();
    // 审计字段：创建人和更新人
    long createdBy();
    long updatedBy();
}
