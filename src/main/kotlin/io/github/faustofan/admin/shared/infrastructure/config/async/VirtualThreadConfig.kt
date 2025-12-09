package io.github.faustofan.admin.shared.infrastructure.config.async

import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.autoconfigure.task.TaskExecutionAutoConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.core.task.AsyncTaskExecutor
import org.springframework.core.task.SimpleAsyncTaskExecutor
import org.springframework.core.task.support.ContextPropagatingTaskDecorator
import org.springframework.scheduling.annotation.AsyncConfigurer
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import java.util.concurrent.Executor
import java.util.concurrent.ThreadFactory

/**
 * 虚拟线程配置
 *
 * 基于 JDK 21+ 虚拟线程的异步任务执行器配置。
 * 虚拟线程特点：
 * 1. 轻量级：占用内存小，可创建百万级线程
 * 2. 高吞吐：适合 I/O 密集型任务
 * 3. 简化编程：使用同步风格编写异步代码
 *
 * @author Claude
 * @since 1.0.0
 */
@Configuration
@EnableAsync
@ConditionalOnProperty(
    prefix = "spring.threads.virtual",
    name = ["enabled"],
    havingValue = "true",
    matchIfMissing = false
)
class VirtualThreadConfig: AsyncConfigurer {

    private val logger = LoggerFactory.getLogger(VirtualThreadConfig::class.java)

    /**
     * 主虚拟线程执行器
     * 用于 @Async 注解的默认执行器
     */
    @Bean(TaskExecutionAutoConfiguration.APPLICATION_TASK_EXECUTOR_BEAN_NAME)
    @Primary
    override fun getAsyncExecutor(): AsyncTaskExecutor {
        logger.info("Initializing Virtual Thread AsyncTaskExecutor")

        return SimpleAsyncTaskExecutor("virt-async-").apply {
            setVirtualThreads(true)
            setTaskDecorator(ContextPropagatingTaskDecorator())
            setTaskTerminationTimeout(60_000) // 60 seconds
        }
    }

    /**
     * 虚拟线程工厂
     * 用于自定义线程名称和未捕获异常处理
     */
    @Bean("virtualThreadFactory")
    fun virtualThreadFactory(): ThreadFactory {
        return Thread.ofVirtual()
            .name("virt-worker-", 0)
            .uncaughtExceptionHandler { thread, exception ->
                logger.error("Uncaught exception in virtual thread [{}]", thread.name, exception)
            }
            .factory()
    }

    /**
     * I/O 密集型虚拟线程执行器
     * 用于数据库、网络 I/O 等操作
     */
    @Bean("ioVirtualExecutor")
    fun ioVirtualExecutor(): Executor {
        logger.info("Initializing I/O Virtual Thread Executor")

        return SimpleAsyncTaskExecutor("virt-io-").apply {
            setVirtualThreads(true)
            setTaskDecorator(ContextPropagatingTaskDecorator())
            // 虚拟线程可以创建大量并发，不设置限制
        }
    }

    /**
     * CPU 密集型平台线程执行器（降级方案）
     * 虚拟线程不适合 CPU 密集型任务，使用传统线程池
     */
    @Bean("cpuBoundExecutor")
    fun cpuBoundExecutor(): ThreadPoolTaskExecutor {
        val processors = Runtime.getRuntime().availableProcessors()
        logger.info("Initializing CPU-bound ThreadPoolTaskExecutor with {} cores", processors)

        return ThreadPoolTaskExecutor().apply {
            corePoolSize = processors
            maxPoolSize = processors * 2
            queueCapacity = 1000
            keepAliveSeconds = 60
            setThreadNamePrefix("cpu-bound-")
            setWaitForTasksToCompleteOnShutdown(true)
            setAwaitTerminationSeconds(60)
            setTaskDecorator(ContextPropagatingTaskDecorator())
            setRejectedExecutionHandler(java.util.concurrent.ThreadPoolExecutor.CallerRunsPolicy())
            initialize()
        }
    }

    /**
     * 批量处理虚拟线程执行器
     * 用于批量任务、报表生成等
     */
    @Bean("batchVirtualExecutor")
    fun batchVirtualExecutor(): Executor {
        logger.info("Initializing Batch Virtual Thread Executor")

        return SimpleAsyncTaskExecutor("virt-batch-").apply {
            setVirtualThreads(true)
            setTaskDecorator(ContextPropagatingTaskDecorator())
            // 批量任务可能较多，不限制并发
        }
    }

    /**
     * 定时任务虚拟线程执行器
     * 用于 @Scheduled 注解的任务
     */
    @Bean("scheduledVirtualExecutor")
    fun scheduledVirtualExecutor(): Executor {
        logger.info("Initializing Scheduled Virtual Thread Executor")

        return SimpleAsyncTaskExecutor("virt-scheduled-").apply {
            setVirtualThreads(true)
            setTaskDecorator(ContextPropagatingTaskDecorator())
            setConcurrencyLimit(100) // 限制并发定时任务数量
        }
    }

    /**
     * 获取异步异常处理器
     */
    override fun getAsyncUncaughtExceptionHandler() = AsyncExceptionHandler()
}
