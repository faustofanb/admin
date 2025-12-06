package io.github.faustofan.admin.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.web.filter.OncePerRequestFilter
import org.springframework.web.util.ContentCachingRequestWrapper
import org.springframework.web.util.ContentCachingResponseWrapper
import java.nio.charset.StandardCharsets

/**
 * HTTP 日志配置属性
 */
@ConfigurationProperties(prefix = "app.http-log")
data class HttpLogProperties(
    /** 是否启用 HTTP 日志 */
    val enabled: Boolean = true,
    /** 是否启用敏感信息脱敏 */
    val maskEnabled: Boolean = true,
    /** 最小响应状态码（只记录 >= 此状态码的响应） */
    val minimumStatus: Int = 0,
    /** 请求体最大记录长度 */
    val maxBodySize: Int = 8192,
    /** 需要包含的路径模式 */
    val includePaths: List<String> = listOf("/api/**"),
    /** 需要排除的路径模式 */
    val excludePaths: List<String> = listOf(
        "/actuator/**",
        "/openapi.yml",
        "/ts.zip",
        "/doc.html",
        "/assets/**",
        "/favicon.ico"
    ),
    /** 需要脱敏的请求头 */
    val sensitiveHeaders: Set<String> = setOf(
        "authorization",
        "x-auth-token",
        "x-api-key",
        "cookie",
        "set-cookie"
    ),
    /** 需要脱敏的 JSON 字段 */
    val sensitiveFields: Set<String> = setOf(
        "password",
        "passwordHash",
        "newPassword",
        "oldPassword",
        "accessToken",
        "refreshToken",
        "token",
        "secret",
        "apiKey",
        "secretKey"
    )
)

/**
 * HTTP 日志配置
 */
@Configuration
@EnableConfigurationProperties(HttpLogProperties::class)
class HttpLogConfig {

    @Bean
    fun httpLoggingFilter(properties: HttpLogProperties): HttpLoggingFilter {
        return HttpLoggingFilter(properties)
    }
}

/**
 * HTTP 请求/响应日志过滤器
 *
 * 功能：
 * - 记录请求方法、路径、耗时
 * - 记录请求体和响应体（可配置大小限制）
 * - 敏感信息脱敏（密码、Token 等）
 * - 可配置路径过滤
 */
@Order(Ordered.HIGHEST_PRECEDENCE + 10)
class HttpLoggingFilter(
    private val properties: HttpLogProperties
) : OncePerRequestFilter() {

    private val log = LoggerFactory.getLogger(HttpLoggingFilter::class.java)

    // 用于格式化 JSON 输出
    private val prettyMapper = ObjectMapper().apply {
        enable(SerializationFeature.INDENT_OUTPUT)
    }

    companion object {
        private const val MASK = "***"
        private val SENSITIVE_FIELD_REGEX_CACHE = mutableMapOf<String, Regex>()
    }

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val path = request.requestURI

        // 如果禁用，直接放行
        if (!properties.enabled) {
            filterChain.doFilter(request, response)
            return
        }

        if (!shouldLog(request)) {
            filterChain.doFilter(request, response)
            return
        }

        val wrappedRequest = ContentCachingRequestWrapper(request, properties.maxBodySize)
        val wrappedResponse = ContentCachingResponseWrapper(response)

        val method = request.method
        val query = request.queryString?.let { "?$it" } ?: ""

        // 打印请求开始日志
        log.info("\n═══════════════════════════════════════════════════════════════════════════\n" +
                "➡️  $method $path$query\n" +
                "═══════════════════════════════════════════════════════════════════════════")

        val startTime = System.currentTimeMillis()
        try {
            filterChain.doFilter(wrappedRequest, wrappedResponse)
        } finally {
            val duration = System.currentTimeMillis() - startTime
            val status = wrappedResponse.status

            if (status >= properties.minimumStatus) {
                logRequestAndResponse(wrappedRequest, wrappedResponse, duration)
            }

            // 重要：必须将响应体拷贝回原始响应
            wrappedResponse.copyBodyToResponse()
        }
    }

    private fun shouldLog(request: HttpServletRequest): Boolean {
        val path = request.requestURI

        // 检查排除路径
        if (properties.excludePaths.any { matchPath(path, it) }) {
            return false
        }

        // 检查包含路径
        return properties.includePaths.isEmpty() || properties.includePaths.any { matchPath(path, it) }
    }

    private fun matchPath(path: String, pattern: String): Boolean {
        // 将 Ant 风格的通配符转换为正则表达式
        val regex = "^" + pattern
            .replace(".", "\\.")
            .replace("**", "§§§")  // 临时占位符
            .replace("*", "[^/]*")
            .replace("§§§", ".*") + "$"
        return regex.toRegex().matches(path)
    }

    private fun logRequestAndResponse(
        request: ContentCachingRequestWrapper,
        response: ContentCachingResponseWrapper,
        duration: Long
    ) {
        val method = request.method
        val uri = request.requestURI
        val query = request.queryString?.let { "?$it" } ?: ""
        val status = response.status

        // 获取请求体（根据开关决定是否脱敏）
        val requestBody = getRequestBody(request)
        val displayRequestBody = if (properties.maskEnabled) maskSensitiveData(requestBody) else requestBody

        // 获取响应体（根据开关决定是否脱敏）
        val responseBody = getResponseBody(response)
        val displayResponseBody = if (properties.maskEnabled) maskSensitiveData(responseBody) else responseBody

        // 获取请求头（根据开关决定是否脱敏）
        val headers = getHeaders(request)

        val logMessage = buildString {
            appendLine()
            appendLine("┌──────────────────────────────────────────────────────────────────────────")
            appendLine("│ ✅ $method $uri$query")
            appendLine("│ ⏱️  ${duration}ms | Status: $status")
            if (headers.isNotEmpty()) {
                appendLine("│ 📋 Headers: $headers")
            }
            if (displayRequestBody.isNotEmpty()) {
                appendLine("│ 📥 Request:")
                formatJsonBody(displayRequestBody).lines().forEach { line ->
                    appendLine("│     $line")
                }
            }
            if (displayResponseBody.isNotEmpty()) {
                appendLine("│ 📤 Response:")
                formatJsonBody(truncate(displayResponseBody)).lines().forEach { line ->
                    appendLine("│     $line")
                }
            }
            append("└──────────────────────────────────────────────────────────────────────────")
        }

        when {
            status >= 500 -> log.error(logMessage)
            status >= 400 -> log.warn(logMessage)
            else -> log.info(logMessage)
        }
    }

    /**
     * 格式化 JSON 为多行
     */
    private fun formatJsonBody(json: String): String {
        if (json.isBlank()) return json
        return try {
            val obj = prettyMapper.readTree(json)
            prettyMapper.writeValueAsString(obj)
        } catch (_: Exception) {
            // 如果不是有效的 JSON，直接返回原内容
            json
        }
    }

    private fun getHeaders(request: HttpServletRequest): Map<String, String> {
        val headers = mutableMapOf<String, String>()
        request.headerNames?.toList()?.forEach { name ->
            val value = if (properties.maskEnabled && properties.sensitiveHeaders.contains(name.lowercase())) {
                MASK
            } else {
                request.getHeader(name)
            }
            headers[name] = value
        }
        return headers.filterKeys { it.lowercase() in listOf("content-type", "user-agent", "x-request-id") }
    }

    private fun getRequestBody(request: ContentCachingRequestWrapper): String {
        val content = request.contentAsByteArray
        if (content.isEmpty()) return ""
        return String(content, StandardCharsets.UTF_8)
    }

    private fun getResponseBody(response: ContentCachingResponseWrapper): String {
        val content = response.contentAsByteArray
        if (content.isEmpty()) return ""
        return String(content, StandardCharsets.UTF_8)
    }

    private fun maskSensitiveData(content: String): String {
        if (content.isEmpty()) return content

        var masked = content
        properties.sensitiveFields.forEach { field ->
            val regex = SENSITIVE_FIELD_REGEX_CACHE.getOrPut(field) {
                // 匹配 JSON 格式: "field": "value" 或 "field":"value"
                """"$field"\s*:\s*"[^"]*"""".toRegex(RegexOption.IGNORE_CASE)
            }
            masked = masked.replace(regex) { """"$field":"$MASK"""" }
        }
        return masked
    }

    private fun truncate(content: String): String {
        return if (content.length > properties.maxBodySize) {
            content.take(properties.maxBodySize) + "...(truncated)"
        } else {
            content
        }
    }
}

