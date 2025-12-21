package io.github.faustofan.admin.shared.common.context;

import java.util.List;
import java.util.UUID;

/**
 * 应用上下文信息记录类，包含请求追踪、租户、用户、组织及角色等关键信息。
 *
 * <p>该类为不可变数据结构，便于在应用各层传递上下文信息。</p>
 *
 * @param traceId           请求链路追踪ID，可为空，若为空则自动生成
 * @param requestId         请求唯一标识
 * @param tenantId          租户ID
 * @param userId            用户ID
 * @param username          用户名
 * @param orgId             组织ID
 * @param currentUserRoles  当前用户角色及数据权限信息列表
 * @param isSuperAdmin      是否为超级管理员
 */
public record AppContext(
        UUID traceId,
        String requestId,
        Long tenantId,
        Long userId,
        String username,
        Long orgId,
        List<RoleDataScopeInfo> currentUserRoles,
        Boolean isSuperAdmin
) {
    /**
     * 获取traceId，如果为null则自动生成一个新的UUID。
     *
     * @return traceId，若原值为null则返回新生成的UUID
     */
    @Override
    public UUID traceId() {
        if (traceId == null) {
            return UUID.randomUUID();
        }
        return traceId;
    }

    public static AppContextBuilder builder() {
        return new AppContextBuilder();
    }

    // 静态内部类：构建者模式
    public static class AppContextBuilder {
        private UUID traceId;
        private String requestId;
        private Long tenantId;
        private Long userId;
        private String username;
        private Long orgId;
        private List<RoleDataScopeInfo> currentUserRoles;
        private Boolean isSuperAdmin;

        public AppContextBuilder() {
        }

        public AppContextBuilder traceId(UUID traceId) {
            this.traceId = traceId;
            return this;
        }

        public AppContextBuilder requestId(String requestId) {
            this.requestId = requestId;
            return this;
        }

        public AppContextBuilder tenantId(Long tenantId) {
            this.tenantId = tenantId;
            return this;
        }

        public AppContextBuilder userId(Long userId) {
            this.userId = userId;
            return this;
        }

        public AppContextBuilder username(String username) {
            this.username = username;
            return this;
        }

        public AppContextBuilder orgId(Long orgId) {
            this.orgId = orgId;
            return this;
        }

        public AppContextBuilder currentUserRoles(List<RoleDataScopeInfo> currentUserRoles) {
            this.currentUserRoles = currentUserRoles;
            return this;
        }

        public AppContextBuilder isSuperAdmin(Boolean isSuperAdmin) {
            this.isSuperAdmin = isSuperAdmin;
            return this;
        }

        public AppContext build() {
            return new AppContext(
                    traceId != null ? traceId : UUID.randomUUID(),
                    requestId,
                    tenantId,
                    userId,
                    username,
                    orgId,
                    currentUserRoles,
                    isSuperAdmin
            );
        }
    }
}
