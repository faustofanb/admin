package io.github.faustofan.admin.auth.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Set;

import org.babyfish.jimmer.sql.ast.impl.CaseBuilder.Str;

/**
 * 当前用户信息响应
 */
@Schema(description = "当前登录用户信息")
public record CurrentUserResponse(
        @Schema(description = "用户ID", example = "1")
        Long userId,
        @Schema(description = "用户名", example = "admin")
        String username,
        @Schema(description = "用户昵称", example = "管理员", nullable = true)
        String nickname,
        @Schema(description = "租户ID", example = "1")
        Long tenantId,
        @Schema(description = "组织机构ID", example = "100", nullable = true)
        Long orgId,
        @Schema(description = "用户角色列表", example = "[\"ADMIN\", \"USER\"]")
        Set<String> roles,
        @Schema(description = "用户权限列表", example = "[\"user:read\", \"user:write\", \"role:read\"]")
        Set<String> permissions,
        @Schema(description = "是否为超级管理员", example = "false")
        Boolean isSuperAdmin
) {}

