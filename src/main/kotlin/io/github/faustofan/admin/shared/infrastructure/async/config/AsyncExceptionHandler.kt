package io.github.faustofan.admin.shared.infrastructure.async.config

import org.slf4j.LoggerFactory
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler
import java.lang.reflect.Method

/**
 * 异步异常处理器
 *
 * 处理 @Async 方法中未捕获的异常
 *
 * @author Claude
 * @since 1.0.0
 */
class AsyncExceptionHandler : AsyncUncaughtExceptionHandler {

    private val logger = LoggerFactory.getLogger(AsyncExceptionHandler::class.java)

    override fun handleUncaughtException(ex: Throwable, method: Method, vararg params: Any?) {
        logger.error(
            "Async method [{}] threw exception with params: {}",
            method.toGenericString(),
            params.joinToString(", "),
            ex
        )

        // TODO: 可根据异常类型进行告警、记录到数据库等操作
        // 例如：集成钉钉、企业微信告警
        // 或：记录到错误日志表，用于后续分析
    }
}