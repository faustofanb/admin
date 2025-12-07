package io.github.faustofan.admin.config

import com.github.vertical_blank.sqlformatter.SqlFormatter
import com.github.vertical_blank.sqlformatter.languages.Dialect
import com.p6spy.engine.spy.appender.MessageFormattingStrategy

/**
 * 自定义 P6Spy SQL 日志格式化器
 *
 * 功能：
 * - 显示数据源、连接信息、执行时间
 * - 从调用栈中提取业务方法名，显示 SQL 用途
 * - 使用 sql-formatter 库格式化 SQL 便于阅读
 */
class P6SpyMessageFormatter : MessageFormattingStrategy {

    companion object {
        // 业务代码包前缀
        private val BUSINESS_PACKAGES = listOf(
            "io.github.faustofan.admin"
        )

        // 需要排除的类/方法（框架层）
        private val EXCLUDED_PATTERNS = listOf(
            "P6SpyMessageFormatter",
            "HttpLoggingFilter",
            "JwtAuthenticationFilter",
            "$\$FastClassBySpringCGLIB",
            "$\$EnhancerBySpringCGLIB",
            "lambda$"
        )
    }

    override fun formatMessage(
        connectionId: Int,
        now: String,
        elapsed: Long,
        category: String,
        prepared: String,
        sql: String,
        url: String
    ): String {
        if (sql.isBlank()) return ""

        // 从调用栈中提取业务方法信息
        val businessContext = extractBusinessContext()

        // 提取数据库名
//        val dbName = extractDbName(url)

        return buildString {
            append("\n--   ──────────────────────────────────────────────────────────────────────\n")
            appendLine("-- 📊 $now | ⚡${elapsed}ms | conn:$connectionId | $category | $url")
            if (businessContext.isNotEmpty()) {
                appendLine("-- 📍 $businessContext")
            }
            appendLine("-- SQL ────────────────────────────────────────────────────────────────────")
            appendLine(formatSql(sql))
            append("---────────────────────────────────────────────────────────────────────────")
        }
    }

    /**
     * 从调用栈中提取业务上下文信息
     */
    private fun extractBusinessContext(): String {
        val stackTrace = Thread.currentThread().stackTrace

        val businessFrames = stackTrace
            .filter { frame ->
                BUSINESS_PACKAGES.any { frame.className.startsWith(it) } &&
                EXCLUDED_PATTERNS.none { frame.className.contains(it) || frame.methodName.contains(it) }
            }
            .take(3) // 只取前3个业务调用
            .map { frame ->
                val simpleClassName = frame.className.substringAfterLast(".")
                "$simpleClassName.${frame.methodName}(:${frame.lineNumber})"
            }

        return businessFrames.joinToString(" <- ")
    }

    /**
     * 从 URL 中提取数据库名
     */
    private fun extractDbName(url: String): String {
        return try {
            // jdbc:postgresql://127.0.0.1:5432/admin -> admin
            url.substringAfterLast("/").substringBefore("?")
        } catch (_: Exception) {
            url
        }
    }

    /**
     * 使用 sql-formatter 库格式化 SQL
     */
    private fun formatSql(sql: String): String {
        return try {
            SqlFormatter.of(Dialect.PostgreSql).format(sql)
        } catch (_: Exception) {
            sql
        }
    }
}
