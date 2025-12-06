package io.github.faustofan.admin.config

import io.github.faustofan.admin.auth.infra.filter.JwtAuthenticationFilter
import io.github.faustofan.admin.common.api.ApiResponse
import org.springframework.boot.context.properties.ConfigurationProperties
import tools.jackson.databind.ObjectMapper
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true)
@EnableConfigurationProperties(SecurityProperties::class)
class SecurityConfig(
    private val jwtAuthenticationFilter: JwtAuthenticationFilter,
    private val objectMapper: ObjectMapper,
    private val securityProperties: SecurityProperties
) {

    companion object {
        /**
         * 固定白名单（框架必需）
         */
        private val DEFAULT_WHITE_LIST = arrayOf(
            "/api/v1/auth/login",
            "/api/v1/auth/refresh",
            "/actuator/**",
            "/error",
            "/favicon.ico"
        )
    }

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        // 合并固定白名单和配置白名单
        val allWhiteList = DEFAULT_WHITE_LIST + securityProperties.whiteList.toTypedArray()

        http
            .csrf { it.disable() }
            .cors { it.configurationSource(corsConfigurationSource()) }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .httpBasic { it.disable() }
            .formLogin { it.disable() }
            .headers { it.frameOptions { frame -> frame.disable() } }
            .authorizeHttpRequests { auth ->
                auth
                    .requestMatchers(*allWhiteList).permitAll()
                    .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                    .anyRequest().authenticated()
            }
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)
            .exceptionHandling { exceptions ->
                exceptions.authenticationEntryPoint { _, response, _ ->
                    response.contentType = MediaType.APPLICATION_JSON_VALUE
                    response.status = 401
                    response.writer.write(
                        objectMapper.writeValueAsString(
                            ApiResponse.failure<Nothing>(401, "未登录或登录已过期")
                        )
                    )
                }
                exceptions.accessDeniedHandler { _, response, _ ->
                    response.contentType = MediaType.APPLICATION_JSON_VALUE
                    response.status = 403
                    response.writer.write(
                        objectMapper.writeValueAsString(
                            ApiResponse.failure<Nothing>(403, "权限不足")
                        )
                    )
                }
            }

        return http.build()
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val configuration = CorsConfiguration().apply {
            allowedOriginPatterns = listOf("*")
            allowedMethods = listOf("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")
            allowedHeaders = listOf("*")
            allowCredentials = true
            maxAge = 3600
        }
        return UrlBasedCorsConfigurationSource().apply {
            registerCorsConfiguration("/**", configuration)
        }
    }
}

@ConfigurationProperties(prefix = "app.security")
data class SecurityProperties(
    /**
     * 额外的白名单路径（从配置文件读取）
     */
    val whiteList: List<String> = emptyList()
)