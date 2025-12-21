package io.github.faustofan.admin.system.domain.model;

import io.github.faustofan.admin.shared.persistence.model.TenantAware;
import io.github.faustofan.admin.system.domain.enums.UserStatus;
import org.babyfish.jimmer.sql.*;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Map;

/**
 * 系统用户实体
 */
@Entity
@Table(name = "sys_user")
public interface SysUser extends TenantAware {

    @Key
    String username();

    String password(); // 记得加密存储

    @Nullable
    String nickname();

    @Nullable
    String email();

    @Nullable
    String mobile();

    // 归属部门
    @ManyToOne
    @JoinColumn(name = "org_id")
    SysOrg org();

    @Column(name = "is_super_admin")
    boolean isSuperAdmin();

    // ABAC 核心：用户自身的动态属性 (Postgres JSONB -> Java Map)
    @Serialized
    @Nullable
    Map<String, Object> attributes();

    // 多对多关联角色
    @ManyToMany
    @JoinTable(
            name = "sys_user_role",
            joinColumnName = "user_id",
            inverseJoinColumnName = "role_id"
    )
    List<SysRole> roles();

    // 修改为枚举类型
    @Column(name = "status")
    @Default("ACTIVE")
    UserStatus status();

}