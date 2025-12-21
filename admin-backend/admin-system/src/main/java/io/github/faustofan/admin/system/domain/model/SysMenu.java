package io.github.faustofan.admin.system.domain.model;

import io.github.faustofan.admin.shared.persistence.model.BaseEntity;
import io.github.faustofan.admin.system.domain.enums.MenuType;
import org.babyfish.jimmer.sql.*;
import org.jspecify.annotations.Nullable;

import java.util.List;

/**
 * 系统菜单实体
 * 表示系统中的菜单项，可以是目录、菜单或按钮
 */
@Entity
@Table(name = "sys_menu")
public interface SysMenu extends BaseEntity {

    @Key // 唯一性约束辅助
    @Nullable
    String permCode();

    String menuName();

    @Column(name = "menu_type")
    MenuType menuType(); // Enum: DIRECTORY, MENU, BUTTON

    @Nullable
    String routePath();

    @Nullable
    String componentPath();

    int sortOrder();

    boolean visible();

    @Nullable
    String icon();

    // 父子关系（递归）
    @ManyToOne
    @OnDissociate(DissociateAction.DELETE)
    SysMenu parent();

    @OneToMany(mappedBy = "parent")
    List<SysMenu> children();

    // 关联：属于哪些产品包
    @ManyToMany(mappedBy = "menus")
    List<SysProductPackage> packages();

    // 关联：哪些角色拥有此菜单
    @ManyToMany(mappedBy = "menus")
    List<SysRole> roles();
}