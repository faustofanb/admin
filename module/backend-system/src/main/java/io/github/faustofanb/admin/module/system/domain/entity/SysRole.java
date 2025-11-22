package io.github.faustofanb.admin.module.system.domain.entity;

import io.github.faustofanb.admin.core.domain.BaseEntity;
import org.babyfish.jimmer.sql.Entity;
import org.babyfish.jimmer.sql.ManyToMany;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * 角色表
 */
@Entity
public interface SysRole extends BaseEntity {

    /**
     * 角色名称
     */
    String roleName();

    /**
     * 角色权限字符串
     */
    String roleKey();

    /**
     * 显示顺序
     */
    int roleSort();

    /**
     * 数据范围（1：全部数据权限 2：自定数据权限 3：本部门数据权限 4：本部门及以下数据权限）
     */
    @Nullable
    String dataScope();

    /**
     * 角色状态（0正常 1停用）
     */
    int status();

    /**
     * 备注
     */
    @Nullable
    String remark();

    /**
     * 菜单组
     */
    @ManyToMany
    List<SysMenu> menus();

    /**
     * 部门组（数据权限）
     */
    @ManyToMany
    List<SysDept> depts();
}
