package io.github.faustofan.admin.auth.interfaces;

import java.util.Set;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.faustofan.admin.auth.application.AuthService;
import io.github.faustofan.admin.auth.application.dto.CurrentUserResponse;
import io.github.faustofan.admin.auth.application.dto.LoginRequest;
import io.github.faustofan.admin.auth.application.dto.LoginResponse;
import io.github.faustofan.admin.auth.application.dto.RefreshTokenRequest;
import io.github.faustofan.admin.auth.domain.model.LoginUser;
import io.github.faustofan.admin.shared.common.constant.SystemConstants;
import io.github.faustofan.admin.shared.common.dto.ApiResponse;
import io.github.faustofan.admin.shared.web.config.OpenApiConfig;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;

@Tag(name = "01. 认证管理", description = "用户认证相关接口：登录、登出、Token 刷新、获取当前用户信息")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * 用户登录
     */
    @Operation(summary = "用户登录", description = """
                使用用户名和密码进行登录认证。

                登录成功后返回 accessToken 和 refreshToken：
                - accessToken: 用于访问受保护资源，有效期较短
                - refreshToken: 用于刷新 accessToken，有效期较长
            """, operationId = "login")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "登录成功", content = @Content(schema = @Schema(implementation = LoginResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "用户名或密码错误"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "423", description = "账号已被锁定")
    })
    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "登录请求参数", required = true) @RequestBody LoginRequest request) {
        Long tenantId = request.tenantId() != null ? request.tenantId() : SystemConstants.SYSTEM_TENANT_ID;
        LoginResponse response = authService.login(request.username(), request.password(), tenantId);
        return ApiResponse.success(response);
    }

    /**
     * 刷新 Token
     */
    @Operation(summary = "刷新 Token", description = """
                使用 refreshToken 获取新的 accessToken。

                当 accessToken 过期时，可使用此接口刷新获取新的 Token 对，无需重新登录。
            """, operationId = "refreshToken")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "刷新成功", content = @Content(schema = @Schema(implementation = LoginResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "refreshToken 无效或已过期")
    })
    @PostMapping("/refresh")
    public ApiResponse<LoginResponse> refresh(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "刷新 Token 请求", required = true) @RequestBody RefreshTokenRequest request) {
        LoginResponse response = authService.refreshToken(request.refreshToken());
        return ApiResponse.success(response);
    }

    /**
     * 获取当前登录用户信息
     */
    @Operation(summary = "获取当前用户信息", description = "获取当前登录用户的详细信息，包括用户ID、用户名、昵称、权限列表等", operationId = "getCurrentUser", security = @SecurityRequirement(name = OpenApiConfig.SECURITY_SCHEME_NAME))
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "获取成功", content = @Content(schema = @Schema(implementation = CurrentUserResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "未登录或 Token 已过期")
    })
    @GetMapping("/me")
    public ApiResponse<CurrentUserResponse> getCurrentUser(
            @Parameter(hidden = true) @AuthenticationPrincipal LoginUser loginUser) {
        CurrentUserResponse response = new CurrentUserResponse(
                loginUser.getUserId(),
                loginUser.getUsername(),
                loginUser.getNickname(),
                loginUser.getTenantId(),
                loginUser.getOrgId(),
                loginUser.getRoles(),
                loginUser.getPermissions(),
                loginUser.isSuperAdmin());
        return ApiResponse.success(response);
    }

    /**
     * 获取当前用户权限码列表
     * 
     * @param loginUser
     * @return 当前用户权限码列表
     */
    @GetMapping("/codes")
    public ApiResponse<Set<String>> getPermCodes(@AuthenticationPrincipal LoginUser loginUser) {
        return ApiResponse.success(loginUser.getPermissions());
    }

    /**
     * 登出
     */
    @Operation(summary = "用户登出", description = """
                退出登录，使当前 Token 失效。

                注意：JWT 是无状态的，如需实现真正的登出（Token 黑名单），需要配合 Redis。
            """, operationId = "logout", security = @SecurityRequirement(name = OpenApiConfig.SECURITY_SCHEME_NAME))
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "登出成功"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "未登录")
    })
    @PostMapping("/logout")
    public ApiResponse<String> logout(HttpServletRequest request) {
        authService.logout(request);
        return ApiResponse.success("登出成功");
    }
}