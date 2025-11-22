package io.github.faustofanb.admin.core.domain;

import org.babyfish.jimmer.sql.GeneratedValue;
import org.babyfish.jimmer.sql.GenerationType;
import org.babyfish.jimmer.sql.Id;
import org.babyfish.jimmer.sql.MappedSuperclass;

import java.time.LocalDateTime;

@MappedSuperclass
public interface BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id();
    
    LocalDateTime createdTime();

    LocalDateTime updatedTime();

    Long createdBy();

    Long updatedBy();
}
