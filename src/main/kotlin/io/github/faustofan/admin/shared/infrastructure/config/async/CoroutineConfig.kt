package io.github.faustofan.admin.shared.infrastructure.config.async

import kotlinx.coroutines.*
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.concurrent.Executors

/**
 * Kotlin 协程配置
 *
 * 配置协程调度器和作用域，支持结构化并发：
 * 1. IO Dispatcher：I/O 密集型任务（数据库、网络、文件操作）
 * 2. Default Dispatcher：CPU 密集型任务（计算、加密、序列化）
 * 3. Unconfined Dispatcher：不限制在特定线程
 * 4. Virtual Thread Dispatcher：基于虚拟线程的调度器（JDK 21+）
 *
 * @author Claude
 * @since 1.0.0
 */
@Configuration
class CoroutineConfig {

    private val logger = LoggerFactory.getLogger(CoroutineConfig::class.java)

    /**
     * 全局协程异常处理器
     */
    @Bean
    fun coroutineExceptionHandler(): CoroutineExceptionHandler {
        return CoroutineExceptionHandler { context, exception ->
            logger.error("Coroutine exception in context [$context]", exception)
            // TODO: 可添加告警、指标上报等
        }
    }

    /**
     * 基于虚拟线程的协程调度器
     * 适用于 I/O 密集型协程任务，充分利用虚拟线程的优势
     */
    @Bean("virtualThreadDispatcher")
    fun virtualThreadDispatcher(): CoroutineDispatcher {
        logger.info("Initializing Virtual Thread CoroutineDispatcher")

        return Executors.newVirtualThreadPerTaskExecutor()
            .asCoroutineDispatcher()
    }

    /**
     * I/O 协程调度器（使用 Kotlin 标准 IO Dispatcher）
     * 适用于：数据库查询、HTTP 调用、文件读写
     */
    @Bean("ioDispatcher")
    fun ioDispatcher(): CoroutineDispatcher {
        logger.info("Initializing IO CoroutineDispatcher")
        return Dispatchers.IO
    }

    /**
     * CPU 密集型协程调度器（使用 Kotlin 标准 Default Dispatcher）
     * 适用于：计算、加密、压缩、序列化
     */
    @Bean("cpuDispatcher")
    fun cpuDispatcher(): CoroutineDispatcher {
        logger.info("Initializing CPU CoroutineDispatcher")
        return Dispatchers.Default
    }

    /**
     * 应用级协程作用域（使用 SupervisorJob）
     * SupervisorJob：子协程失败不会影响其他子协程和父协程
     * 适用于：长生命周期的业务逻辑
     */
    @Bean("applicationCoroutineScope")
    fun applicationCoroutineScope(
        virtualThreadDispatcher: CoroutineDispatcher,
        exceptionHandler: CoroutineExceptionHandler
    ): CoroutineScope {
        logger.info("Initializing Application CoroutineScope with SupervisorJob")

        return CoroutineScope(
            SupervisorJob() +
                    virtualThreadDispatcher +
                    exceptionHandler
        )
    }

    /**
     * I/O 协程作用域
     * 用于数据库、网络等 I/O 操作
     */
    @Bean("ioCoroutineScope")
    fun ioCoroutineScope(
        ioDispatcher: CoroutineDispatcher,
        exceptionHandler: CoroutineExceptionHandler
    ): CoroutineScope {
        logger.info("Initializing IO CoroutineScope")

        return CoroutineScope(
            SupervisorJob() +
                    ioDispatcher +
                    exceptionHandler
        )
    }

    /**
     * CPU 密集型协程作用域
     * 用于计算密集型任务
     */
    @Bean("cpuCoroutineScope")
    fun cpuCoroutineScope(
        cpuDispatcher: CoroutineDispatcher,
        exceptionHandler: CoroutineExceptionHandler
    ): CoroutineScope {
        logger.info("Initializing CPU CoroutineScope")

        return CoroutineScope(
            SupervisorJob() +
                    cpuDispatcher +
                    exceptionHandler
        )
    }
}
