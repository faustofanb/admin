package io.github.faustofan.admin.auth.interfaces.rest.dto

import io.swagger.v3.oas.annotations.media.Schema

/**
 * 登录响应 DTO
 */
@Schema(description = "登录响应")
data class LoginResponse(
    @field:Schema(
        description = "访问令牌",
        example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
    )
    val accessToken: String,

    @field:Schema(
        description = "刷新令牌",
        example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
    )
    val refreshToken: String,

    @field:Schema(
        description = "访问令牌过期时间（秒）",
        example = "3600"
    )
    val expiresIn: Long,

    @field:Schema(
        description = "令牌类型",
        example = "Bearer",
        allowableValues = ["Bearer"]
    )
    val tokenType: String,

    @field:Schema(
        description = "用户ID",
        example = "1"
    )
    val userId: Long,

    @field:Schema(
        description = "用户名",
        example = "admin"
    )
    val username: String,

    @field:Schema(
        description = "用户昵称",
        example = "管理员",
        nullable = true
    )
    val nickname: String?,

    @field:Schema(
        description = "用户权限列表",
        example = "[\"user:read\", \"user:write\", \"role:read\"]"
    )
    val permissions: Set<String>
)

/**
 * 当前用户信息响应
 */
@Schema(description = "当前登录用户信息")
data class CurrentUserResponse(
    @field:Schema(
        description = "用户ID",
        example = "1"
    )
    val userId: Long,

    @field:Schema(
        description = "用户名",
        example = "admin"
    )
    val username: String,

    @field:Schema(
        description = "用户昵称",
        example = "管理员",
        nullable = true
    )
    val nickname: String?,

    @field:Schema(
        description = "租户ID",
        example = "1"
    )
    val tenantId: Long,

    @field:Schema(
        description = "组织机构ID",
        example = "100",
        nullable = true
    )
    val orgId: Long?,

    @field:Schema(
        description = "用户权限列表",
        example = "[\"user:read\", \"user:write\", \"role:read\"]"
    )
    val permissions: Set<String>,

    @field:Schema(
        description = "是否为超级管理员",
        example = "false"
    )
    val isSuperAdmin: Boolean
)