package io.github.faustofan.admin.shared.infrastructure.async.metrics

import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.lang.management.ManagementFactory
import java.lang.management.ThreadMXBean
import java.util.concurrent.TimeUnit

/**
 * 异步任务监控定时任务
 *
 * 定期采集线程和协程的运行时指标
 *
 * @author Claude
 * @since 1.0.0
 */
@Component
@ConditionalOnProperty(
    prefix = "app.async",
    name = ["enabled"],
    havingValue = "true",
    matchIfMissing = true
)
class AsyncMetricsCollector(
    private val metricsService: AsyncMetricsService
) {

    private val logger = LoggerFactory.getLogger(AsyncMetricsCollector::class.java)
    private val threadMXBean: ThreadMXBean = ManagementFactory.getThreadMXBean()

    /**
     * 每 30 秒采集一次线程指标
     */
    @Scheduled(fixedRate = 30, timeUnit = TimeUnit.SECONDS)
    fun collectThreadMetrics() {
        try {
            // 虚拟线程数量
            metricsService.recordVirtualThreadCount()

            // 平台线程数量
            metricsService.recordPlatformThreadCount()

            // 线程状态统计
            val threadInfo = threadMXBean.dumpAllThreads(false, false)
            val threadStates = threadInfo.groupBy { it.threadState }

            logger.debug(
                "Thread metrics - Total: {}, Virtual: {}, Platform: {}, States: {}",
                threadInfo.size,
                threadInfo.count { it.threadName.contains("virt") },
                threadInfo.count { !it.threadName.contains("virt") },
                threadStates.mapValues { it.value.size }
            )

        } catch (e: Exception) {
            logger.error("Failed to collect thread metrics", e)
        }
    }

    /**
     * 每分钟记录一次系统资源使用情况
     */
    @Scheduled(fixedRate = 1, timeUnit = TimeUnit.MINUTES)
    fun collectSystemMetrics() {
        try {
            val runtime = Runtime.getRuntime()
            val usedMemory = (runtime.totalMemory() - runtime.freeMemory()) / 1024 / 1024
            val maxMemory = runtime.maxMemory() / 1024 / 1024
            val memoryUsage = (usedMemory.toDouble() / maxMemory * 100).toInt()

            logger.info(
                "System metrics - Memory: {} MB / {} MB ({}%), CPU cores: {}, Thread count: {}",
                usedMemory,
                maxMemory,
                memoryUsage,
                runtime.availableProcessors(),
                threadMXBean.threadCount
            )

        } catch (e: Exception) {
            logger.error("Failed to collect system metrics", e)
        }
    }
}
