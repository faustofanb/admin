package io.github.faustofan.admin.shared.persistence.log;

import com.github.vertical_blank.sqlformatter.SqlFormatter;
import com.github.vertical_blank.sqlformatter.languages.Dialect;
import com.p6spy.engine.spy.appender.MessageFormattingStrategy;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 自定义 P6Spy SQL 日志格式化器
 * <p>
 * 功能：
 * - 显示数据源、连接信息、执行时间
 * - 从调用栈中提取业务方法名，显示 SQL 用途
 * - 使用 sql-formatter 库格式化 SQL 便于阅读
 */
public class P6SpyMessageFormatter implements MessageFormattingStrategy {

    private static final List<String> BUSINESS_PACKAGES = List.of(
            "io.github.faustofan.admin"
    );

    private static final List<String> EXCLUDED_PATTERNS = List.of(
            "P6SpyMessageFormatter",
            "HttpLoggingFilter",
            "JwtAuthenticationFilter",
            "$$FastClassBySpringCGLIB",
            "$$EnhancerBySpringCGLIB",
            "lambda$"
    );

    @Override
    public String formatMessage(int connectionId, String now, long elapsed, String category, String prepared, String sql, String url) {
        if (sql == null || sql.isEmpty()) {
            return "";
        }

        // 从调用栈中提取业务方法信息
        String businessContext = extractBusinessContext();

        return "\n--   ======================================================================\n" +
                "-- 📊 " + now + " | ⚡" + elapsed + "ms | conn:" + connectionId +
                " | " + category + " | " + url + "\n" +
                (businessContext.isEmpty() ? "" : "-- 📍 " + businessContext + "\n") +
                "-- SQL --------------------------------------------------------------------\n\n" +
                formatSql(sql) +
                "\n\n--   ======================================================================";
    }

    /**
     * 从调用栈中提取业务上下文信息
     */
    private String extractBusinessContext() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();

        List<String> businessFrames = Arrays.stream(stackTrace)
                .filter(frame ->
                        BUSINESS_PACKAGES.stream().anyMatch(frame.getClassName()::startsWith)
                                &&
                        EXCLUDED_PATTERNS.stream().noneMatch(pattern ->
                                frame.getClassName().contains(pattern) || frame.getMethodName().contains(pattern)
                        )
                )
                .limit(6) // 只取前3个业务调用
                .map(frame -> {
                    String simpleClassName = frame.getClassName().substring(frame.getClassName().lastIndexOf('.') + 1);
                    return simpleClassName + "." + frame.getMethodName() + "(:"
                            + frame.getLineNumber() + ")";
                })
                .collect(Collectors.toList());

        return String.join(" <- ", businessFrames);
    }

    /**
     * 使用 sql-formatter 库格式化 SQL
     */
    private String formatSql(String sql) {
        try {
            return SqlFormatter.of(Dialect.PostgreSql).format(sql);
        } catch (Exception e) {
            return sql;
        }
    }
}

