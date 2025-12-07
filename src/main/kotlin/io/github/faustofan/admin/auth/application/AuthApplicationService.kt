package io.github.faustofan.admin.auth.application

import io.github.faustofan.admin.auth.domain.model.LoginUser
import io.github.faustofan.admin.auth.domain.service.UserDetailsServiceImpl
import io.github.faustofan.admin.auth.infrastructure.security.JwtTokenProvider
import io.github.faustofan.admin.auth.interfaces.rest.dto.LoginResponse
import io.github.faustofan.admin.shared.infrastructure.exception.BizException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

/**
 * 认证服务
 * 处理登录、登出、刷新 Token 等业务
 */
@Service
class AuthService(
    private val userDetailsService: UserDetailsServiceImpl,
    private val jwtTokenProvider: JwtTokenProvider,
    private val passwordEncoder: PasswordEncoder
) {

    /**
     * 用户登录
     *
     * @param username 用户名
     * @param password 密码（明文）
     * @param tenantId 租户ID
     * @return 登录响应（包含 Token）
     */
    fun login(username: String, password: String, tenantId: Long): LoginResponse {
        // 加载用户信息
        val loginUser = userDetailsService.loadUserByUsernameAndTenant(username, tenantId)

        // 验证密码
        if (!passwordEncoder.matches(password, loginUser.password)) {
            throw BizException(message = "用户名或密码错误")
        }

        // 生成 Token
        return generateTokens(loginUser)
    }

    /**
     * 刷新 Token
     *
     * @param refreshToken 刷新令牌
     * @return 新的登录响应
     */
    fun refreshToken(refreshToken: String): LoginResponse {
        // 验证 refresh token
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw BizException(message = "刷新令牌已过期，请重新登录")
        }

        val tokenType = jwtTokenProvider.getTokenType(refreshToken)
        if (tokenType != "refresh") {
            throw BizException(message = "无效的刷新令牌")
        }

        // 获取用户信息
        val userId = jwtTokenProvider.getUserIdFromToken(refreshToken)
        val loginUser = userDetailsService.loadUserById(userId)

        // 生成新 Token
        return generateTokens(loginUser)
    }

    /**
     * 生成访问令牌和刷新令牌
     */
    private fun generateTokens(loginUser: LoginUser): LoginResponse {
        val accessToken = jwtTokenProvider.generateAccessToken(
            userId = loginUser.userId,
            tenantId = loginUser.tenantId,
            username = loginUser.username,
            permissions = loginUser.permissions
        )

        val refreshToken = jwtTokenProvider.generateRefreshToken(
            userId = loginUser.userId,
            tenantId = loginUser.tenantId
        )

        return LoginResponse(
            accessToken = accessToken,
            refreshToken = refreshToken,
            expiresIn = jwtTokenProvider.getAccessTokenExpiration() / 1000, // 转为秒
            tokenType = "Bearer",
            userId = loginUser.userId,
            username = loginUser.username,
            nickname = loginUser.nickname,
            permissions = loginUser.permissions
        )
    }
}

