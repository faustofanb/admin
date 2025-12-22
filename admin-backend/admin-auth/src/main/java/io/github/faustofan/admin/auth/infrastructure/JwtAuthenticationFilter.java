package io.github.faustofan.admin.auth.infrastructure;

import java.io.IOException;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import io.github.faustofan.admin.auth.domain.service.UserDetailsServiceImpl;
import io.github.faustofan.admin.shared.common.constant.SystemConstants;
import io.github.faustofan.admin.shared.common.context.AppContext;
import io.github.faustofan.admin.shared.common.context.AppContextHolder;
import io.github.faustofan.admin.shared.common.util.SnowflakeIdGenerator;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * JWT 认证过滤器
 * <p>
 * 该过滤器用于拦截每个 HTTP 请求，解析并校验 JWT Token，
 * 若校验通过则将用户信息注入 Spring Security 上下文，并填充自定义 AppContext。
 * 请求结束后会清理上下文，防止内存泄漏。
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    /** 请求头中存放 JWT 的字段名 */
    private static final String AUTHORIZATION_HEADER = "Authorization";
    /** JWT Token 前缀 */
    private static final String BEARER_PREFIX = "Bearer ";

    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailsServiceImpl userDetailsService;

    /**
     * 构造方法，注入依赖
     *
     * @param jwtTokenProvider   JWT Token 处理器
     * @param userDetailsService 用户详情服务
     */
    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider, UserDetailsServiceImpl userDetailsService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userDetailsService = userDetailsService;
    }

    /**
     * 过滤请求，进行 JWT 认证
     *
     * @param request     HTTP 请求
     * @param response    HTTP 响应
     * @param filterChain 过滤器链
     * @throws ServletException 过滤异常
     * @throws IOException      IO 异常
     */
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        try {
            String jwt = extractJwtFromRequest(request);

            if (jwt != null && jwtTokenProvider.validateToken(jwt)) {
                String tokenType = jwtTokenProvider.getTokenType(jwt);

                // 只接受 access token
                if ("access".equals(tokenType)) {
                    Long userId = jwtTokenProvider.getUserIdFromToken(jwt);
                    AppContextHolder.setContext(SystemConstants.SYSTEM_APP_CONTEXT);
                    var loginUser = userDetailsService.loadUserById(userId);

                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            loginUser,
                            null,
                            loginUser.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authentication);

                    // 填充 AppContext
                    AppContext appContext = AppContext.builder()
                            .traceId(UUID.randomUUID())
                            .requestId(SnowflakeIdGenerator.getInstance().nextId().toString())
                            .tenantId(loginUser.getTenantId())
                            .userId(loginUser.getUserId())
                            .username(loginUser.getUsername())
                            .orgId(loginUser.getOrgId())
                            // TODO: ABAC 相关内容待重构
                            .currentUserRoles(null)
                            .isSuperAdmin(loginUser.isSuperAdmin())
                            .build();
                    AppContextHolder.setContext(appContext);
                }
            }
        } catch (Exception e) {
            logger.error("Could not set user authentication in security context", e);
        }

        try {
            // 设置系统级上下文，继续后续过滤器链
            AppContextHolder.setContext(SystemConstants.SYSTEM_APP_CONTEXT);
            filterChain.doFilter(request, response);
        } finally {
            // 请求结束时清理上下文，防止内存泄漏
            AppContextHolder.clearContext();
        }
    }

    /**
     * 从请求头中提取 JWT Token
     *
     * @param request HTTP 请求
     * @return JWT Token 字符串，若不存在则返回 null
     */
    private String extractJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(BEARER_PREFIX.length());
        }
        return null;
    }
}