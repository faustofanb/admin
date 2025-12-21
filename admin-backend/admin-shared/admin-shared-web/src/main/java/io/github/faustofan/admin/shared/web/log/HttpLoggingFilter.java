package io.github.faustofan.admin.shared.web.log;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * HTTP 日志过滤器，用于记录请求和响应的详细信息，包括请求体、响应体、头信息等。
 * 支持敏感字段脱敏、路径过滤、最大体积限制等功能。
 * 适用于 Spring Web 应用，确保每个请求仅记录一次。
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 10)
public class HttpLoggingFilter extends OncePerRequestFilter {

    /** 日志记录器 */
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(HttpLoggingFilter.class);
    /** 脱敏显示内容 */
    private static final String MASK = "***";
    /** 敏感字段正则缓存 */
    private static final Map<String, java.util.regex.Pattern> SENSITIVE_FIELD_REGEX_CACHE = new HashMap<>();
    /** 日志配置属性 */
    private final HttpLogProperties properties;
    /** 美化 JSON 的 ObjectMapper */
    private final ObjectMapper prettyMapper;

    /**
     * 构造方法，注入日志配置属性。
     * @param properties 日志配置
     */
    public HttpLoggingFilter(HttpLogProperties properties) {
        this.properties = properties;
        this.prettyMapper = JsonMapper.builder()
                .enable(SerializationFeature.INDENT_OUTPUT)
                .build();
    }

    /**
     * 过滤器主逻辑，记录请求和响应日志。
     * @param request  HTTP 请求
     * @param response HTTP 响应
     * @param filterChain 过滤器链
     * @throws java.io.IOException IO异常
     * @throws jakarta.servlet.ServletException Servlet异常
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws java.io.IOException, jakarta.servlet.ServletException {
        String path = request.getRequestURI();

        // 如果禁用，直接放行
        if (!properties.isEnabled()) {
            filterChain.doFilter(request, response);
            return;
        }

        if (!shouldLog(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(request, properties.getMaxBodySize());
        ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper(response);

        long startTime = System.currentTimeMillis();
        try {
            filterChain.doFilter(wrappedRequest, wrappedResponse);
        } finally {
            long duration = System.currentTimeMillis() - startTime;
            int status = wrappedResponse.getStatus();

            if (status >= properties.getMinimumStatus()) {
                logRequestAndResponse(wrappedRequest, wrappedResponse, duration);
            }

            // 重要：必须将响应体拷贝回原始响应
            wrappedResponse.copyBodyToResponse();
        }
    }

    /**
     * 判断当前请求是否需要记录日志。
     * @param request HTTP 请求
     * @return 是否记录
     */
    private boolean shouldLog(HttpServletRequest request) {
        String path = request.getRequestURI();

        // 检查排除路径
        if (properties.getExcludePaths().stream().anyMatch(pattern -> matchPath(path, pattern))) {
            return false;
        }

        // 检查包含路径
        return properties.getIncludePaths().isEmpty() || properties.getIncludePaths().stream().anyMatch(pattern -> matchPath(path, pattern));
    }

    /**
     * 路径匹配，支持 Ant 风格通配符。
     * @param path    实际路径
     * @param pattern 匹配模式
     * @return 是否匹配
     */
    private boolean matchPath(String path, String pattern) {
        // 将 Ant 风格的通配符转换为正则表达式
        String regex = "^" + pattern
                .replace(".", "\\.")
                .replace("**", "§§§")  // 临时占位符
                .replace("*", "[^/]*")
                .replace("§§§", ".*") + "$";
        return path.matches(regex);
    }

    /**
     * 记录请求和响应日志。
     * @param request  包装后的请求
     * @param response 包装后的响应
     * @param duration 请求耗时
     */
    private void logRequestAndResponse(ContentCachingRequestWrapper request, ContentCachingResponseWrapper response, long duration) {
        String method = request.getMethod();
        String uri = request.getRequestURI();
        String query = request.getQueryString() != null ? "?" + request.getQueryString() : "";
        int status = response.getStatus();

        // 获取请求体（根据开关决定是否脱敏）
        String requestBody = getRequestBody(request);
        String displayRequestBody = properties.isMaskEnabled() ? maskSensitiveData(requestBody) : requestBody;

        // 获取响应体（根据开关决定是否脱敏）
        String responseBody = getResponseBody(response);
        String displayResponseBody = properties.isMaskEnabled() ? maskSensitiveData(responseBody) : responseBody;

        // 获取请求头（根据开关决定是否脱敏）
        Map<String, String> headers = getHeaders(request);

        StringBuilder logMessage = new StringBuilder();
        logMessage.append("\n┌──────────────────────────────────────────────────────────────────────────")
                .append("\n ✅ ").append(method).append(" ").append(uri).append(query)
                .append("\n ⏱️  ").append(duration).append("ms | Status: ").append(status);

        if (!headers.isEmpty()) {
            logMessage.append("\n 📋 Headers: ").append(headers);
        }
        if (!displayRequestBody.isEmpty()) {
            logMessage.append("\n 📥 Request:");
            Arrays.stream(formatJsonBody(displayRequestBody).split("\n")).forEach(line -> logMessage.append("\n     ").append(line));
        }
        if (!displayResponseBody.isEmpty()) {
            logMessage.append("\n 📤 Response:");
            Arrays.stream(formatJsonBody(truncate(displayResponseBody)).split("\n")).forEach(line -> logMessage.append("\n     ").append(line));
        }
        logMessage.append("\n└──────────────────────────────────────────────────────────────────────────");

        if (status >= 500) {
            log.error(logMessage.toString());
        } else if (status >= 400) {
            log.warn(logMessage.toString());
        } else {
            log.info(logMessage.toString());
        }
    }

    /**
     * 格式化 JSON 为多行字符串。
     * @param json 原始 JSON 字符串
     * @return 格式化后的字符串
     */
    private String formatJsonBody(String json) {
        if (json.isBlank()) return json;
        try {
            Object obj = prettyMapper.readTree(json);
            return prettyMapper.writeValueAsString(obj);
        } catch (Exception e) {
            // 如果不是有效的 JSON，直接返回原内容
            return json;
        }
    }

    /**
     * 获取请求头信息，仅保留部分常用头。
     * @param request HTTP 请求
     * @return 头信息映射
     */
    private Map<String, String> getHeaders(HttpServletRequest request) {
        Map<String, String> headers = new HashMap<>();
        Enumeration<String> headerNames = request.getHeaderNames();
        if (headerNames != null) {
            while (headerNames.hasMoreElements()) {
                String name = headerNames.nextElement();
                String value = (properties.isMaskEnabled() && properties.getSensitiveHeaders().contains(name.toLowerCase()))
                        ? MASK
                        : request.getHeader(name);
                headers.put(name, value);
            }
        }
        return headers.entrySet().stream()
                .filter(entry -> Arrays.asList("content-type", "user-agent", "x-request-id").contains(entry.getKey().toLowerCase()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    /**
     * 获取请求体内容。
     * @param request 包装后的请求
     * @return 请求体字符串
     */
    private String getRequestBody(ContentCachingRequestWrapper request) {
        byte[] content = request.getContentAsByteArray();
        if (content.length == 0) return "";
        return new String(content, StandardCharsets.UTF_8);
    }

    /**
     * 获取响应体内容。
     * @param response 包装后的响应
     * @return 响应体字符串
     */
    private String getResponseBody(ContentCachingResponseWrapper response) {
        byte[] content = response.getContentAsByteArray();
        if (content.length == 0) return "";
        return new String(content, StandardCharsets.UTF_8);
    }

    /**
     * 对内容中的敏感字段进行脱敏处理。
     * @param content 原始内容
     * @return 脱敏后的内容
     */
    private String maskSensitiveData(String content) {
        if (content.isEmpty()) return content;

        String masked = content;
        for (String field : properties.getSensitiveFields()) {
            java.util.regex.Pattern regex = SENSITIVE_FIELD_REGEX_CACHE.computeIfAbsent(field, k ->
                    java.util.regex.Pattern.compile("\"" + field + "\"\\s*:\\s*\"[^\"]*\"", java.util.regex.Pattern.CASE_INSENSITIVE)
            );
            masked = regex.matcher(masked).replaceAll("\"" + field + "\":\"" + MASK + "\"");
        }
        return masked;
    }

    /**
     * 超出最大体积时截断内容。
     * @param content 原始内容
     * @return 截断后的内容
     */
    private String truncate(String content) {
        return content.length() > properties.getMaxBodySize()
                ? content.substring(0, properties.getMaxBodySize()) + "...(truncated)"
                : content;
    }
}