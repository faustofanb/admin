package io.github.faustofanb.admin.core.domain;

import org.babyfish.jimmer.sql.MappedSuperclass;

@MappedSuperclass
public interface TreeEntity extends BaseEntity {

    Integer sort();
}
