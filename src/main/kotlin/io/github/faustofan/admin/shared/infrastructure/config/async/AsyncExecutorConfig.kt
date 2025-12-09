package io.github.faustofan.admin.shared.infrastructure.config.async

import org.slf4j.LoggerFactory
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableScheduling

/**
 * 异步任务执行器配置属性
 *
 * @author Claude
 * @since 1.0.0
 */
@ConfigurationProperties(prefix = "app.async")
data class AsyncProperties(
    /**
     * 是否启用异步
     */
    var enabled: Boolean = true,

    /**
     * 虚拟线程配置
     */
    var virtualThread: VirtualThreadProperties = VirtualThreadProperties(),

    /**
     * 协程配置
     */
    var coroutine: CoroutineProperties = CoroutineProperties(),

    /**
     * 线程池配置
     */
    var threadPool: ThreadPoolProperties = ThreadPoolProperties()
) {
    data class VirtualThreadProperties(
        /**
         * 是否启用虚拟线程
         */
        var enabled: Boolean = true,

        /**
         * 线程名前缀
         */
        var namePrefix: String = "virt-",

        /**
         * 任务终止超时时间（毫秒）
         */
        var taskTerminationTimeout: Long = 60_000L
    )

    data class CoroutineProperties(
        /**
         * 是否启用协程
         */
        var enabled: Boolean = true,

        /**
         * 是否使用虚拟线程作为协程调度器
         */
        var useVirtualThread: Boolean = true
    )

    data class ThreadPoolProperties(
        /**
         * CPU 密集型线程池核心线程数（默认为 CPU 核心数）
         */
        var cpuCorePoolSize: Int = Runtime.getRuntime().availableProcessors(),

        /**
         * CPU 密集型线程池最大线程数（默认为 CPU 核心数 * 2）
         */
        var cpuMaxPoolSize: Int = Runtime.getRuntime().availableProcessors() * 2,

        /**
         * 队列容量
         */
        var queueCapacity: Int = 1000,

        /**
         * 线程空闲时间（秒）
         */
        var keepAliveSeconds: Int = 60
    )
}

/**
 * 异步任务执行器统一配置
 *
 * @author Claude
 * @since 1.0.0
 */
@Configuration
@EnableScheduling
@EnableConfigurationProperties(AsyncProperties::class)
class AsyncExecutorConfig(
    private val asyncProperties: AsyncProperties
) {

    private val logger = LoggerFactory.getLogger(AsyncExecutorConfig::class.java)

    init {
        logger.info("Async configuration initialized with properties: {}", asyncProperties)
        logger.info("Virtual Thread enabled: {}", asyncProperties.virtualThread.enabled)
        logger.info("Coroutine enabled: {}", asyncProperties.coroutine.enabled)
        logger.info("CPU bound thread pool size: {} - {}",
            asyncProperties.threadPool.cpuCorePoolSize,
            asyncProperties.threadPool.cpuMaxPoolSize
        )
    }
}
