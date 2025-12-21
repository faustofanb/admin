package io.github.faustofan.admin.shared.common.context;

import io.github.faustofan.admin.shared.common.enums.DataScope;

import java.util.List;

/**
 * 角色的数据权限信息
 */
public record RoleDataScopeInfo(
        DataScope dataScope,
        List<Long> specificOrgIds
) {
}
