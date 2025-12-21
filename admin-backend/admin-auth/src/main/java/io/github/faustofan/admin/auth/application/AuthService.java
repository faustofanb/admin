package io.github.faustofan.admin.auth.application;

import io.github.faustofan.admin.auth.application.dto.LoginResponse;
import io.github.faustofan.admin.auth.domain.model.LoginUser;
import io.github.faustofan.admin.auth.domain.service.UserDetailsServiceImpl;
import io.github.faustofan.admin.auth.infrastructure.JwtTokenProvider;
import io.github.faustofan.admin.shared.common.exception.BizException;
import io.github.faustofan.admin.shared.common.exception.UserErrorCode;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * 认证服务
 * 处理登录、登出、刷新 Token 等业务
 */
@Service
public class AuthService {

    private final UserDetailsServiceImpl userDetailsService;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    public AuthService(
            UserDetailsServiceImpl userDetailsService,
            JwtTokenProvider jwtTokenProvider,
            PasswordEncoder passwordEncoder) {
        this.userDetailsService = userDetailsService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * 用户登录
     *
     * @param username 用户名
     * @param password 密码（明文）
     * @param tenantId 租户ID
     * @return 登录响应（包含 Token）
     */
    public LoginResponse login(String username, String password, Long tenantId) {
        // 加载用户信息
        LoginUser loginUser = userDetailsService.loadUserByUsernameAndTenant(username, tenantId);

        // 验证密码
        if (!passwordEncoder.matches(password, loginUser.getPassword())) {
            throw new BizException(UserErrorCode.PASSWORD_ERROR);
        }

        // 生成 Token
        return generateTokens(loginUser);
    }

    /**
     * 刷新 Token
     *
     * @param refreshToken 刷新令牌
     * @return 新的登录响应
     */
    public LoginResponse refreshToken(String refreshToken) {
        // 验证 refresh token
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new BizException(UserErrorCode.INVALID_REFRESH_TOKEN);
        }

        String tokenType = jwtTokenProvider.getTokenType(refreshToken);
        if (!"refresh".equals(tokenType)) {
            throw new BizException(UserErrorCode.INVALID_REFRESH_TOKEN);
        }

        // 获取用户信息
        Long userId = jwtTokenProvider.getUserIdFromToken(refreshToken);
        LoginUser loginUser = userDetailsService.loadUserById(userId);

        // 生成新 Token
        return generateTokens(loginUser);
    }

    /**
     * 生成访问令牌和刷新令牌
     */
    private LoginResponse generateTokens(LoginUser loginUser) {
        String accessToken = jwtTokenProvider.generateAccessToken(
                loginUser.getUserId(),
                loginUser.getTenantId(),
                loginUser.getUsername(),
                loginUser.getPermissions());

        String refreshToken = jwtTokenProvider.generateRefreshToken(
                loginUser.getUserId(),
                loginUser.getTenantId());

        return new LoginResponse(
                accessToken,
                refreshToken,
                jwtTokenProvider.getAccessTokenExpiration() / 1000, // 转为秒
                "Bearer",
                loginUser.getUserId(),
                loginUser.getUsername(),
                loginUser.getNickname(),
                loginUser.getRoles(),
                "/workpath");
    }
}
