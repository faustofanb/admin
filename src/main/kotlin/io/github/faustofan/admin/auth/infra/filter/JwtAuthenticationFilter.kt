package io.github.faustofan.admin.auth.infra.filter

import io.github.faustofan.admin.auth.domain.service.UserDetailsServiceImpl
import io.github.faustofan.admin.auth.infra.jwt.JwtTokenProvider
import io.github.faustofan.admin.common.context.AppContext
import io.github.faustofan.admin.common.context.AppContextHolder
import io.github.faustofan.admin.common.unique.RequestIdGenerator
import io.github.faustofan.admin.common.unique.SnowflakeIdGenerator
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils
import org.springframework.web.filter.OncePerRequestFilter

/**
 * JWT 认证过滤器
 *
 * 从请求头中提取 JWT Token，验证并设置 SecurityContext
 * 同时填充 AppContext 用于业务层获取当前用户信息
 */
@Component
class JwtAuthenticationFilter(
    private val jwtTokenProvider: JwtTokenProvider,
    private val userDetailsService: UserDetailsServiceImpl
) : OncePerRequestFilter() {

    companion object {
        private const val AUTHORIZATION_HEADER = "Authorization"
        private const val BEARER_PREFIX = "Bearer "
    }

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        try {
            val jwt = extractJwtFromRequest(request)

            if (jwt != null && jwtTokenProvider.validateToken(jwt)) {
                val tokenType = jwtTokenProvider.getTokenType(jwt)

                // 只接受 access token
                if (tokenType == "access") {
                    val userId = jwtTokenProvider.getUserIdFromToken(jwt)
                    val loginUser = userDetailsService.loadUserById(userId)

                    val authentication = UsernamePasswordAuthenticationToken(
                        loginUser,
                        null,
                        loginUser.authorities
                    )
                    authentication.details = WebAuthenticationDetailsSource().buildDetails(request)

                    SecurityContextHolder.getContext().authentication = authentication

                    // 填充 AppContext
                    val appContext = AppContext(
                        tenantId = loginUser.tenantId,
                        userId = loginUser.userId,
                        username = loginUser.username,
                        orgId = loginUser.orgId,
                        requestId = RequestIdGenerator.next(),
                        traceId = SnowflakeIdGenerator.instance!!.nextId(),
                        currentUserRoles = loginUser.roleDataScopes,
                        isSuperAdmin = loginUser.isSuperAdmin
                    )
                    AppContextHolder.set(appContext)
                }
            }
        } catch (e: Exception) {
            logger.error("Could not set user authentication in security context", e)
        }

        try {
            filterChain.doFilter(request, response)
        } finally {
            // 请求结束时清理上下文，防止内存泄漏
            AppContextHolder.clear()
        }
    }

    /**
     * 从请求头中提取 JWT Token
     */
    private fun extractJwtFromRequest(request: HttpServletRequest): String? {
        val bearerToken = request.getHeader(AUTHORIZATION_HEADER)
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(BEARER_PREFIX.length)
        }
        return null
    }
}

