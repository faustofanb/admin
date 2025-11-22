package io.github.faustofanb.admin.core.domain;

import org.babyfish.jimmer.sql.IdView;
import org.babyfish.jimmer.sql.MappedSuperclass;
import org.jetbrains.annotations.Nullable;

@MappedSuperclass
public interface TreeEntity extends BaseEntity {

    Integer sort();
}
