package io.github.faustofanb.admin.module.system.domain.entity;

import io.github.faustofanb.admin.core.domain.TreeEntity;
import org.babyfish.jimmer.sql.Entity;
import org.jetbrains.annotations.Nullable;

/**
 * 菜单权限表
 */
@Entity
public interface SysMenu extends TreeEntity<SysMenu> {

    /**
     * 菜单名称
     */
    String menuName();

    /**
     * 菜单类型（M目录 C菜单 F按钮）
     */
    String menuType();

    /**
     * 路由地址
     */
    @Nullable
    String path();

    /**
     * 组件路径
     */
    @Nullable
    String component();

    /**
     * 权限标识
     */
    @Nullable
    String perms();

    /**
     * 菜单图标
     */
    @Nullable
    String icon();

    /**
     * 是否显示
     */
    boolean visible();

    /**
     * 菜单状态（0正常 1停用）
     */
    int status();
}
