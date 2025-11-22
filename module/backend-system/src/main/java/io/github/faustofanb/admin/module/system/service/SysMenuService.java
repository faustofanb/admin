package io.github.faustofanb.admin.module.system.service;

import io.github.faustofanb.admin.module.system.domain.entity.SysMenu;

import java.util.List;
import java.util.Set;

public interface SysMenuService {

    /**
     * 根据用户ID查询菜单树信息
     *
     * @param userId 用户ID
     * @return 菜单列表
     */
    List<SysMenu> selectMenuTreeByUserId(Long userId);

    /**
     * 根据用户ID查询权限标识
     *
     * @param userId 用户ID
     * @return 权限列表
     */
    Set<String> selectPermsByUserId(Long userId);
}
