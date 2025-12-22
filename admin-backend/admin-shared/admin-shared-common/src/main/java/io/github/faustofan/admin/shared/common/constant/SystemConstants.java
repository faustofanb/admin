package io.github.faustofan.admin.shared.common.constant;

import io.github.faustofan.admin.shared.common.context.AppContext;
import io.github.faustofan.admin.shared.common.context.RoleDataScopeInfo;
import io.github.faustofan.admin.shared.common.enums.DataScope;
import java.util.List;
import java.util.UUID;

/**
 * 系统级业务常量
 */
public final class SystemConstants {

    private SystemConstants() {}

    public static final String SYSTEM_USERNAME = "SYSTEM_USER";
    public static final long SYSTEM_USER_ID = GlobalConstants.SYS_ID_DEFAULT;
    public static final Long SYSTEM_TENANT_ID = GlobalConstants.SYS_ID_DEFAULT;
    public static final long SYSTEM_ORG_ID = GlobalConstants.SYS_ID_DEFAULT;

    /**
     * 系统身份上下文 (单例/常量模式)
     * 使用静态内部类延迟加载，或直接定义为常量对象
     */
    public static final class Identity {
        private Identity() {}
        
        public static final AppContext SYSTEM_CONTEXT = AppContext.builder()
                .traceId(UUID.randomUUID()) 
                .requestId("SYSTEM_REQ")
                .userId(SYSTEM_USER_ID)
                .tenantId(SYSTEM_TENANT_ID)
                .orgId(SYSTEM_ORG_ID)
                .username(SYSTEM_USERNAME)
                .isSuperAdmin(false)
                .currentUserRoles(List.of(new RoleDataScopeInfo(DataScope.SELF, List.of(SYSTEM_ORG_ID))))
                .build();
    }
}