package io.github.faustofan.admin.shared.persistence.model;

import org.babyfish.jimmer.sql.MappedSuperclass;

/**
 * 数据范围实体，包含数据所属组织ID字段
 */
@MappedSuperclass
public interface DataScoped extends TenantAware {
    long ownerOrgId();
}
