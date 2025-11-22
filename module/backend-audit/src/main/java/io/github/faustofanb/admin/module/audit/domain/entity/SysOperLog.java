package io.github.faustofanb.admin.module.audit.domain.entity;

import java.time.LocalDateTime;

import org.babyfish.jimmer.sql.Entity;
import org.babyfish.jimmer.sql.Table;
import org.jetbrains.annotations.Nullable;

import io.github.faustofanb.admin.core.domain.BaseEntity;

@Entity
@Table(name = "sys_oper_log")
public interface SysOperLog extends BaseEntity {

    @Nullable
    String title();

    int businessType(); // 0=Other, 1=Insert, 2=Update, 3=Delete, ...

    @Nullable
    String method();

    @Nullable
    String requestMethod();

    @Nullable
    String operName();

    @Nullable
    String operUrl();

    @Nullable
    String operIp();

    @Nullable
    String operParam();

    @Nullable
    String jsonResult();

    int status(); // 0=Normal, 1=Error

    @Nullable
    String errorMsg();

    LocalDateTime operTime();

    long costTime();
}
