package io.github.faustofan.admin.shared.common.enums;

/**
 * 数据权限枚举
 *
 * @author faustofan
 */
public enum DataScope {
    ALL("全部数据权限"),
    CUSTOM("自定义数据权限"),
    DEPT("本部门数据权限"),
    DEPT_AND_CHILD("本部门及以下数据权限"),
    SELF("仅本人数据权限");

    private final String description;

    DataScope(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
