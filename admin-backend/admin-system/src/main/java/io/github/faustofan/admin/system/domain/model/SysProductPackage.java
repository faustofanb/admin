package io.github.faustofan.admin.system.domain.model;

import io.github.faustofan.admin.shared.persistence.model.BaseEntity;
import org.babyfish.jimmer.sql.*;
import java.util.List;

/**
 * 系统产品包实体
 * 表示系统中的产品包信息
 */
@Entity
@Table(name = "sys_product_package")
public interface SysProductPackage extends BaseEntity {

    @Key
    String code();

    String name();

    String remark();

    // 多对多关联菜单
    @ManyToMany
    @JoinTable(
            name = "sys_package_menu",
            joinColumnName = "package_id",
            inverseJoinColumnName = "menu_id"
    )
    List<SysMenu> menus();

    // 1对多关联租户
    @OneToMany(mappedBy = "packageInfo")
    List<SysTenant> tenants();
}
