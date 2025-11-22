package io.github.faustofanb.admin.module.audit.domain.entity;

import java.time.LocalDateTime;

import org.babyfish.jimmer.sql.Entity;
import org.babyfish.jimmer.sql.Table;
import org.jetbrains.annotations.Nullable;

import io.github.faustofanb.admin.core.domain.BaseEntity;

@Entity
@Table(name = "sys_logininfor")
public interface SysLogininfor extends BaseEntity {

    @Nullable
    String userName();

    @Nullable
    String ipaddr();

    @Nullable
    String status(); // 0=Success, 1=Fail

    @Nullable
    String msg();

    LocalDateTime accessTime();
}
