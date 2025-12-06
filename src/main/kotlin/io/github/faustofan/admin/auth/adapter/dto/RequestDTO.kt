package io.github.faustofan.admin.auth.adapter.dto

import io.swagger.v3.oas.annotations.media.Schema

/**
 * 用户登录请求 DTO
 */
@Schema(description = "用户登录请求")
data class LoginRequest(
    @field:Schema(
        description = "用户名",
        example = "admin",
        requiredMode = Schema.RequiredMode.REQUIRED,
        minLength = 1,
        maxLength = 50
    )
    val username: String,

    @field:Schema(
        description = "密码（明文）",
        example = "123456",
        requiredMode = Schema.RequiredMode.REQUIRED,
        minLength = 6,
        maxLength = 100
    )
    val password: String,

    @field:Schema(
        description = "租户ID（可选，如果系统从域名解析租户则不需要）",
        example = "1",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED,
        nullable = true
    )
    val tenantId: Long? = null,

    @field:Schema(
        description = "租户编码（可选，可以用租户编码代替ID）",
        example = "default",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED,
        nullable = true
    )
    val tenantCode: String? = null
)

/**
 * 刷新 Token 请求 DTO
 */
@Schema(description = "刷新Token请求")
data class RefreshTokenRequest(
    @field:Schema(
        description = "刷新令牌",
        example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    val refreshToken: String
)

