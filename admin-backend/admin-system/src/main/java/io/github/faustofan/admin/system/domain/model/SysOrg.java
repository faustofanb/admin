package io.github.faustofan.admin.system.domain.model;

import io.github.faustofan.admin.shared.persistence.model.TenantAware;
import org.babyfish.jimmer.sql.*;
import java.util.List;

/**
 * 组织机构实体
 */
@Entity
@Table(name = "sys_org")
public interface SysOrg extends TenantAware {

    String name();

    @ManyToOne
    @OnDissociate(DissociateAction.DELETE)
    SysOrg parent();

    @OneToMany(mappedBy = "parent")
    List<SysOrg> children();

    @OneToMany(mappedBy = "org")
    List<SysUser> users();
}
