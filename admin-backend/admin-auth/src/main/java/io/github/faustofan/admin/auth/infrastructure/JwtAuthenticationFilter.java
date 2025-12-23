package io.github.faustofan.admin.auth.infrastructure;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

/**
 * JWT 认证过滤器。
 * <p>
 * 该过滤器用于拦截所有 HTTP 请求，校验 JWT Token 的有效性，处理用户认证信息，
 * 并将认证信息填充到 Spring Security 的上下文和自定义业务上下文中。
 * 所有异常会统一交由全局异常处理器处理。
 * </p>
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailsServiceImpl userDetailsService;
    private final HandlerExceptionResolver handlerExceptionResolver;

    /**
     * 构造方法，注入依赖。
     *
     * @param jwtTokenProvider         JWT Token 提供者
     * @param userDetailsService       用户信息服务
     * @param handlerExceptionResolver 全局异常处理器
     */
    public JwtAuthenticationFilter(
            JwtTokenProvider jwtTokenProvider,
            UserDetailsServiceImpl userDetailsService,
            @Qualifier("handlerExceptionResolver") HandlerExceptionResolver handlerExceptionResolver
    ) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userDetailsService = userDetailsService;
        this.handlerExceptionResolver = handlerExceptionResolver;
    }

    /**
     * 过滤器主逻辑。
     * <ul>
     *     <li>初始化基础上下文（生成 TraceId）</li>
     *     <li>提取并校验 JWT Token</li>
     *     <li>校验 Token 是否在黑名单</li>
     *     <li>仅处理 access token，填充认证信息</li>
     *     <li>异常统一交由全局异常处理器</li>
     *     <li>清理上下文，防止内存泄漏</li>
     * </ul>
     *
     * @param request     HTTP 请求
     * @param response    HTTP 响应
     * @param filterChain 过滤器链
     * @throws ServletException 过滤器异常
     * @throws IOException      IO 异常
     */
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        try {
            // 1. 优先初始化基础上下文（生成 TraceId），确保后续报错也能带上 ID
            initInitialContext();

            String jwt = jwtTokenProvider.extractJwtFromRequest(request);

            if (jwt != null) {
                // 验证 Token 有效性
                if(!jwtTokenProvider.validateToken(jwt)) {
                    throw new UserException(UserErrorCode.INVALID_ACCESS_TOKEN);
                }

                // 检查黑名单
                if (userDetailsService.isLogOut(jwt)) {
                    throw new UserException(UserErrorCode.UNAUTHORIZED);
                }

                // 只接受 access token
                if ("access".equals(jwtTokenProvider.getTokenType(jwt))) {
                    processAuthentication(jwt, request);
                }
            }

            // 2. 继续过滤器链
            filterChain.doFilter(request, response);

        } catch (Exception e) {
            // 3. 核心改进：将所有异常转发给 GlobalExceptionHandler
            logger.warn("JWT Filter 处理异常: {}", e.getMessage());
            handlerExceptionResolver.resolveException(request, response, null, e);
        } finally {
            // 4. 无论成功失败，必须清理上下文，防止内存泄漏和信息污染
            AppContextHolder.clearContext();
            // 注意：SecurityContextHolder 也可以在这里清理，但通常 Spring Security 会自行管理已认证的上下文
        }
    }

    /**
     * 处理身份认证并填充 AppContext。
     * <ul>
     *     <li>根据 JWT 获取用户 ID</li>
     *     <li>加载用户信息</li>
     *     <li>设置 Spring Security 认证上下文</li>
     *     <li>填充自定义业务上下文</li>
     * </ul>
     *
     * @param jwt     JWT Token
     * @param request HTTP 请求
     */
    private void processAuthentication(String jwt, HttpServletRequest request) {
        Long userId = jwtTokenProvider.getUserIdFromToken(jwt);
        // 加载用户信息
        var loginUser = userDetailsService.loadUserById(userId);

        // 设置 SecurityContext
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                loginUser, null, loginUser.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 重新填充完整的业务上下文 (覆盖 initInitialContext 中的临时上下文)
        AppContext appContext = AppContext.builder()
                .traceId(AppContextHolder.getContext().traceId()) // 保留已生成的 traceId
                .requestId(SnowflakeIdGenerator.getInstance().nextId().toString())
                .tenantId(loginUser.getTenantId())
                .userId(loginUser.getUserId())
                .username(loginUser.getUsername())
                .orgId(loginUser.getOrgId())
                .isSuperAdmin(loginUser.isSuperAdmin())
                .build();
        AppContextHolder.setContext(appContext);
    }

    /**
     * 初始化基础上下文（主要是为了 TraceId）。
     * <p>
     * 在过滤器链最开始调用，确保后续日志和异常都能带上 TraceId。
     * </p>
     */
    private void initInitialContext() {
        AppContextHolder.setContext(SystemConstants.Identity.SYSTEM_CONTEXT);
    }
}