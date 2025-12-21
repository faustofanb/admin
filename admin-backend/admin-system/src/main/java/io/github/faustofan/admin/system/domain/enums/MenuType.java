package io.github.faustofan.admin.system.domain.enums;

import org.babyfish.jimmer.sql.EnumType;

/**
 * 菜单类型枚举
 */
@EnumType
public enum MenuType {
    DIRECTORY, // 目录
    MENU,      // 菜单
    BUTTON     // 按钮/权限
}

