package io.github.faustofan.admin.auth.interfaces.rest

import io.github.faustofan.admin.auth.application.AuthService
import io.github.faustofan.admin.auth.domain.model.LoginUser
import io.github.faustofan.admin.auth.interfaces.rest.dto.CurrentUserResponse
import io.github.faustofan.admin.auth.interfaces.rest.dto.LoginRequest
import io.github.faustofan.admin.auth.interfaces.rest.dto.LoginResponse
import io.github.faustofan.admin.auth.interfaces.rest.dto.RefreshTokenRequest
import io.github.faustofan.admin.shared.application.dto.ApiResponse
import io.github.faustofan.admin.shared.infrastructure.config.OpenApiConfig
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import io.swagger.v3.oas.annotations.responses.ApiResponse as SwaggerApiResponse

/**
 * 认证接口控制器
 *
 * 提供登录、登出、刷新 Token、获取当前用户信息等接口
 */
@Tag(name = "01. 认证管理", description = "用户认证相关接口：登录、登出、Token 刷新、获取当前用户信息")
@RestController
@RequestMapping("/api/v1/auth")
class AuthController(
    private val authService: AuthService
) {

    /**
     * 用户登录
     */
    @Operation(
        summary = "用户登录",
        description = """
            使用用户名和密码进行登录认证。
            
            登录成功后返回 accessToken 和 refreshToken：
            - accessToken: 用于访问受保护资源，有效期较短
            - refreshToken: 用于刷新 accessToken，有效期较长
        """,
        operationId = "login"
    )
    @ApiResponses(
        value = [
            SwaggerApiResponse(
                responseCode = "200",
                description = "登录成功",
                content = [Content(schema = Schema(implementation = LoginResponse::class))]
            ),
            SwaggerApiResponse(responseCode = "400", description = "用户名或密码错误"),
            SwaggerApiResponse(responseCode = "423", description = "账号已被锁定")
        ]
    )
    @PostMapping("/login")
    fun login(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "登录请求参数",
            required = true
        )
        @RequestBody request: LoginRequest
    ): ApiResponse<LoginResponse> {
        val tenantId = request.tenantId ?: 1L // 默认租户，实际应从域名或请求头解析
        val response = authService.login(request.username, request.password, tenantId)
        return ApiResponse.success(response)
    }

    /**
     * 刷新 Token
     */
    @Operation(
        summary = "刷新 Token",
        description = """
            使用 refreshToken 获取新的 accessToken。
            
            当 accessToken 过期时，可使用此接口刷新获取新的 Token 对，无需重新登录。
        """,
        operationId = "refreshToken"
    )
    @ApiResponses(
        value = [
            SwaggerApiResponse(
                responseCode = "200",
                description = "刷新成功",
                content = [Content(schema = Schema(implementation = LoginResponse::class))]
            ),
            SwaggerApiResponse(responseCode = "401", description = "refreshToken 无效或已过期")
        ]
    )
    @PostMapping("/refresh")
    fun refresh(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "刷新 Token 请求",
            required = true
        )
        @RequestBody request: RefreshTokenRequest
    ): ApiResponse<LoginResponse> {
        val response = authService.refreshToken(request.refreshToken)
        return ApiResponse.success(response)
    }

    /**
     * 获取当前登录用户信息
     */
    @Operation(
        summary = "获取当前用户信息",
        description = "获取当前登录用户的详细信息，包括用户ID、用户名、昵称、权限列表等",
        operationId = "getCurrentUser",
        security = [SecurityRequirement(name = OpenApiConfig.SECURITY_SCHEME_NAME)]
    )
    @ApiResponses(
        value = [
            SwaggerApiResponse(
                responseCode = "200",
                description = "获取成功",
                content = [Content(schema = Schema(implementation = CurrentUserResponse::class))]
            ),
            SwaggerApiResponse(responseCode = "401", description = "未登录或 Token 已过期")
        ]
    )
    @GetMapping("/me")
    fun getCurrentUser(
        @Parameter(hidden = true)
        @AuthenticationPrincipal loginUser: LoginUser
    ): ApiResponse<CurrentUserResponse> {
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
     */
    @Operation(
        summary = "用户登出",
        description = """
            退出登录，使当前 Token 失效。
            
            注意：JWT 是无状态的，如需实现真正的登出（Token 黑名单），需要配合 Redis。
        """,
        operationId = "logout",
        security = [SecurityRequirement(name = OpenApiConfig.SECURITY_SCHEME_NAME)]
    )
    @ApiResponses(
        value = [
            SwaggerApiResponse(responseCode = "200", description = "登出成功"),
            SwaggerApiResponse(responseCode = "401", description = "未登录")
        ]
    )
    @PostMapping("/logout")
    fun logout(): ApiResponse<String> {
        // TODO: 实现 Token 黑名单机制（需要 Redis）
        return ApiResponse.success("登出成功")
    }
}


