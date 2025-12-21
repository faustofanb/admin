package io.github.faustofan.admin.auth.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 刷新 Token 请求 DTO
 */
@Schema(description = "刷新Token请求")
public record RefreshTokenRequest(
    @Schema(
        description = "刷新令牌",
        example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    String refreshToken
) {}
