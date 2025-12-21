package io.github.faustofan.admin.system.domain.model;

import io.github.faustofan.admin.shared.persistence.model.TenantAware;
import org.babyfish.jimmer.sql.*;
import java.util.List;

/**
 * 角色实体
 */
@Entity
@Table(name = "sys_role")
public interface SysRole extends TenantAware {

    String name();

    String code();

    // RBAC: 角色关联的菜单
    @ManyToMany
    @JoinTable(
            name = "sys_role_menu",
            joinColumnName = "role_id",
            inverseJoinColumnName = "menu_id"
    )
    List<SysMenu> menus();

    // ABAC: 角色关联的数据策略
    @ManyToMany
    @JoinTable(
            name = "sys_role_policy",
            joinColumnName = "role_id",
            inverseJoinColumnName = "policy_id"
    )
    List<SysAbacPolicy> policies();

    @ManyToMany(mappedBy = "roles")
    List<SysUser> users();
}
