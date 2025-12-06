package io.github.faustofan.admin.auth.adapter.rest

import io.github.faustofan.admin.auth.adapter.dto.CurrentUserResponse
import io.github.faustofan.admin.auth.adapter.dto.LoginRequest
import io.github.faustofan.admin.auth.adapter.dto.LoginResponse
import io.github.faustofan.admin.auth.adapter.dto.RefreshTokenRequest
import io.github.faustofan.admin.auth.app.AuthService
import io.github.faustofan.admin.auth.domain.entity.LoginUser
import io.github.faustofan.admin.common.api.ApiResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

/**
 * 认证接口控制器
 *
 * 提供登录、登出、刷新 Token、获取当前用户信息等接口
 */
@Tag(name = "认证管理", description = "登录、登出、Token 刷新等")
@RestController
@RequestMapping("/api/v1/auth")
class AuthController(
    private val authService: AuthService
) {

    /**
     * 用户登录
     */
    @Operation(summary = "用户登录", description = "使用用户名密码登录，返回 JWT Token")
    @PostMapping("/login")
    fun login(@RequestBody request: LoginRequest): ApiResponse<LoginResponse> {
        val tenantId = request.tenantId ?: 1L // 默认租户，实际应从域名或请求头解析
        val response = authService.login(request.username, request.password, tenantId)
        return ApiResponse.success(response)
    }

    /**
     * 刷新 Token
     */
    @Operation(summary = "刷新 Token", description = "使用 Refresh Token 获取新的 Access Token")
    @PostMapping("/refresh")
    fun refresh(@RequestBody request: RefreshTokenRequest): ApiResponse<LoginResponse> {
        val response = authService.refreshToken(request.refreshToken)
        return ApiResponse.success(response)
    }

    /**
     * 获取当前登录用户信息
     */
    @Operation(summary = "获取当前用户信息", description = "获取当前登录用户的详细信息")
    @GetMapping("/me")
    fun getCurrentUser(@AuthenticationPrincipal loginUser: LoginUser): ApiResponse<CurrentUserResponse> {
        val response = CurrentUserResponse(
            userId = loginUser.userId,
            username = loginUser.username,
            nickname = loginUser.nickname,
            tenantId = loginUser.tenantId,
            orgId = loginUser.orgId,
            permissions = loginUser.permissions,
            isSuperAdmin = loginUser.isSuperAdmin
        )
        return ApiResponse.success(response)
    }

    /**
     * 登出
     *
     * 注意：JWT 是无状态的，服务端不存储 Token
     * 如果需要实现真正的登出（如 Token 黑名单），需要配合 Redis
     */
    @Operation(summary = "用户登出", description = "退出登录")
    @PostMapping("/logout")
    fun logout(): ApiResponse<String> {
        // TODO: 实现 Token 黑名单机制（需要 Redis）
        return ApiResponse.success("登出成功")
    }
}


