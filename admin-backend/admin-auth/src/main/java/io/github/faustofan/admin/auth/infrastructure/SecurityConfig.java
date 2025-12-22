package io.github.faustofan.admin.auth.infrastructure;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.faustofan.admin.shared.common.dto.ApiResponse;
import io.github.faustofan.admin.shared.common.exception.errcode.UserErrorCode;

/**
 * 安全配置类，负责配置 Spring Security 的核心安全策略。
 * <p>
 * 主要功能包括：
 * <ul>
 * <li>合并默认和自定义白名单，配置无需认证的接口</li>
 * <li>配置 JWT 认证过滤器，实现无状态认证</li>
 * <li>配置 CORS 跨域策略</li>
 * <li>自定义未认证和权限不足时的 JSON 响应</li>
 * <li>禁用 CSRF、表单登录、HTTP Basic、Session 等</li>
 * </ul>
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true)
@EnableConfigurationProperties(SecurityProperties.class)
public class SecurityConfig {

    /**
     * 框架内置的默认白名单接口
     */
    private static final String[] DEFAULT_WHITE_LIST = {
            "/api/auth/login",
            "/api/auth/refresh",
            "/error",
            "/favicon.ico"
    };

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final ObjectMapper objectMapper;
    private final SecurityProperties securityProperties;

    /**
     * 构造方法，注入所需依赖
     *
     * @param jwtAuthenticationFilter JWT 认证过滤器
     * @param objectMapper            Jackson 对象映射器
     * @param securityProperties      安全相关自定义配置
     */
    public SecurityConfig(
            JwtAuthenticationFilter jwtAuthenticationFilter,
            ObjectMapper objectMapper,
            SecurityProperties securityProperties) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.objectMapper = objectMapper;
        this.securityProperties = securityProperties;
    }

    /**
     * 配置 Spring Security 的过滤器链
     *
     * @param http HttpSecurity 配置对象
     * @return SecurityFilterChain 安全过滤器链
     * @throws Exception 配置异常
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // 合并默认白名单和自定义白名单
        String[] allWhiteList = Stream.concat(
                Arrays.stream(DEFAULT_WHITE_LIST),
                securityProperties.getWhitelist().stream()).toArray(String[]::new);

        http
                // 禁用 CSRF
                .csrf(AbstractHttpConfigurer::disable)
                // 配置 CORS
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                // 无状态会话
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // 禁用 HTTP Basic
                .httpBasic(AbstractHttpConfigurer::disable)
                // 禁用表单登录
                .formLogin(AbstractHttpConfigurer::disable)
                // 禁用 X-Frame-Options
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                // 配置请求授权
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(allWhiteList).permitAll()
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .anyRequest().authenticated())
                // 添加 JWT 认证过滤器
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                // 配置异常处理
                .exceptionHandling(exceptions -> {
                    // 未认证时返回 JSON
                    exceptions.authenticationEntryPoint((request, response, authException) -> {
                        response.setCharacterEncoding("UTF-8");
                        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                        response.setStatus(401);
                        response.getWriter().write(
                                objectMapper.writeValueAsString(
                                        ApiResponse.fail(UserErrorCode.UNAUTHORIZED)));
                    });
                    // 权限不足时返回 JSON
                    exceptions.accessDeniedHandler((request, response, accessDeniedException) -> {
                        response.setCharacterEncoding("UTF-8");
                        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                        response.setStatus(403);
                        response.getWriter().write(
                                objectMapper.writeValueAsString(
                                        ApiResponse.fail(UserErrorCode.FORBIDDEN)));
                    });
                });

        return http.build();
    }

    /**
     * 密码加密器 Bean，使用 BCrypt 算法
     *
     * @return PasswordEncoder 实例
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * CORS 配置源 Bean，允许所有来源和常用方法
     *
     * @return CorsConfigurationSource 实例
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(List.of("*"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}