package io.github.faustofan.admin.auth.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Map;
import java.util.Set;

/**
 * 登录响应 DTO
 */
@Schema(description = "登录响应")
public record LoginResponse(
        @Schema(description = "访问令牌", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...") String accessToken,
        @Schema(description = "刷新令牌", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...") String refreshToken,
        @Schema(description = "访问令牌过期时间（秒）", example = "3600") Long expiresIn,
        @Schema(description = "令牌类型", example = "Bearer", allowableValues = {
                "Bearer" }) String tokenType,
        @Schema(description = "用户ID", example = "1") Long userId,
        @Schema(description = "用户名", example = "admin") String username,
        @Schema(description = "用户昵称", example = "管理员", nullable = true) String nickname,
        @Schema(
                description = "用户角色列表",
                example = """
                    {
                        1: "管理员",
                        2: "用户"
                    }
                """
        )
        Map<Long, String> roles,
        @Schema(description = "首页路径", example = "/workpath") String homePath) {
}
