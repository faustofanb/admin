package io.github.faustofan.admin.shared.common.constant;

import io.github.faustofan.admin.shared.common.context.AppContext;
import io.github.faustofan.admin.shared.common.context.RoleDataScopeInfo;
import io.github.faustofan.admin.shared.common.enums.DataScope;

import java.util.List;
import java.util.UUID;

/**
 * 自定义常量接口，定义了系统用户相关的常量。
 */
public interface CustomConstants {

    /**
     * 系统用户名
     */
    String SYSTEM_USERNAME = "SYSTEM_USER";

    /**
     * 系统用户ID
     */
    long SYSTEM_USER_ID = 999999999L;

    /**
     * 系统租户ID
     */
    Long SYSTEM_TENANT_ID = 999999999L;

    /**
     * 系统组织ID
     */
    long SYSTEM_ORG_ID = 999999999L;

    /**
     * 系统应用上下文
     */
    AppContext SYSTEM_APP_CONTEXT = AppContext.builder()
            .traceId(UUID.randomUUID())
            .requestId("SYSTEM_REQUEST_ID")
            .userId(SYSTEM_USER_ID)
            .tenantId(SYSTEM_TENANT_ID)
            .orgId(SYSTEM_ORG_ID)
            .username(SYSTEM_USERNAME)
            .isSuperAdmin(false)
            .currentUserRoles(List.of(new RoleDataScopeInfo(
                    DataScope.SELF,
                    List.of(SYSTEM_ORG_ID)))
            )
            .build();
}