package io.github.faustofan.admin.shared.persistence.model;

import io.github.faustofan.admin.shared.persistence.DistributedIdGenerator;
import org.babyfish.jimmer.sql.GeneratedValue;
import org.babyfish.jimmer.sql.Id;
import org.babyfish.jimmer.sql.LogicalDeleted;
import org.babyfish.jimmer.sql.MappedSuperclass;
import java.time.Instant;

/**
 * 基类实体，包含通用字段
 */
@MappedSuperclass
public interface BaseEntity {
    @Id
    @GeneratedValue(generatorType = DistributedIdGenerator.class)
    long id();

    Instant createdTime();
    Instant updatedTime();

    // 逻辑删除 (false: 未删除, true: 已删除)
    @LogicalDeleted("true")
    boolean deleted();
}
