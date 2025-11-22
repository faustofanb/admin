package io.github.faustofanb.admin.core.domain;

import org.babyfish.jimmer.sql.MappedSuperclass;

import java.time.LocalDateTime;

@MappedSuperclass
public interface BaseEntity {
    
    LocalDateTime createdTime();

    LocalDateTime updatedTime();

    Long createdBy();

    Long updatedBy();
}
