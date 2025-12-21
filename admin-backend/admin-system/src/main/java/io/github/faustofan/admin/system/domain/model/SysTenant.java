package io.github.faustofan.admin.system.domain.model;

import io.github.faustofan.admin.shared.persistence.model.BaseEntity;
import io.github.faustofan.admin.system.domain.enums.TenantStatus;
import org.babyfish.jimmer.sql.Entity;
import org.babyfish.jimmer.sql.Key;
import org.babyfish.jimmer.sql.*;
import java.time.LocalDateTime;

/**
 * 系统租户实体
 * 表示系统中的租户信息
 */
@Entity
@Table(name = "sys_tenant")
public interface SysTenant extends BaseEntity {

    @Key
    String tenantCode();

    String name();

    // 租户关联一个产品包
    @ManyToOne
    @JoinColumn(name = "package_id")
    SysProductPackage packageInfo();

    LocalDateTime expireTime();

    // 修改为枚举类型
    @Column(name = "status")
    // 可以设置默认值，insert时如果未赋值则使用此值
    @Default("ENABLE")
    TenantStatus status();
}