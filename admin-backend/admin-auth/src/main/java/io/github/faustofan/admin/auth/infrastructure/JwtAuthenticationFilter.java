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
import io.github.faustofan.admin.shared.common.exception.UserException;
import io.github.faustofan.admin.shared.common.exception.errcode.UserErrorCode;
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
            FilterChain filterChain
    ) throws ServletException, IOException {
        try {
            String jwt = jwtTokenProvider.extractJwtFromRequest(request);
            
            if (jwt != null && jwtTokenProvider.validateToken(jwt)) {
                // 检查 Token 是否在登出黑名单中
                if (userDetailsService.isLogOut(jwt)) {
                    throw new UserException(UserErrorCode.UNAUTHORIZED);
                }

                String tokenType = jwtTokenProvider.getTokenType(jwt);

                // 只接受 access token
                if ("access".equals(tokenType)) {
                    Long userId = jwtTokenProvider.getUserIdFromToken(jwt);
                    AppContextHolder.setContext(SystemConstants.Identity.SYSTEM_CONTEXT);
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
            } else if(jwt == null){
                //第一次登录，没有携带token，设置系统级上下文
                AppContextHolder.setContext(SystemConstants.Identity.SYSTEM_CONTEXT);
            }
        } catch (Exception e) {
            logger.error("Could not set user authentication in security context", e);
        }

        try {
            // 设置系统级上下文，继续后续过滤器链
            filterChain.doFilter(request, response);
        } finally {
            // 请求结束时清理上下文，防止内存泄漏
            AppContextHolder.clearContext();
        }
    }


}