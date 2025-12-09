package io.github.faustofan.admin.shared.infrastructure.async.metrics

import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.Tag
import io.micrometer.core.instrument.Timer
import kotlinx.coroutines.CoroutineScope
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.util.concurrent.ConcurrentHashMap
import kotlin.coroutines.CoroutineContext

/**
 * 异步任务监控服务
 *
 * 提供协程和虚拟线程的指标采集功能：
 * 1. 任务执行时间
 * 2. 任务成功/失败率
 * 3. 并发度监控
 * 4. 资源使用情况
 *
 * @author Claude
 * @since 1.0.0
 */
@Component
class AsyncMetricsService(
    private val meterRegistry: MeterRegistry
) {

    private val logger = LoggerFactory.getLogger(AsyncMetricsService::class.java)

    // 任务计数器缓存
    private val timerCache = ConcurrentHashMap<String, Timer>()

    companion object {
        private const val METRIC_PREFIX = "app.async"
        private const val COROUTINE_EXECUTION = "$METRIC_PREFIX.coroutine.execution"
        private const val VIRTUAL_THREAD_EXECUTION = "$METRIC_PREFIX.virtual_thread.execution"
        private const val TASK_EXECUTION = "$METRIC_PREFIX.task.execution"
    }

    /**
     * 记录协程执行指标
     */
    suspend fun <T> recordCoroutineExecution(
        name: String,
        tags: Map<String, String> = emptyMap(),
        block: suspend CoroutineScope.() -> T
    ): T {
        val timer = getOrCreateTimer(COROUTINE_EXECUTION, name, tags)
        val sample = Timer.start(meterRegistry)

        return try {
            val result = kotlinx.coroutines.coroutineScope { block() }
            sample.stop(timer)
            recordSuccess(name, "coroutine", tags)
            result
        } catch (e: Exception) {
            sample.stop(timer)
            recordFailure(name, "coroutine", e, tags)
            throw e
        }
    }

    /**
     * 记录虚拟线程执行指标
     */
    fun <T> recordVirtualThreadExecution(
        name: String,
        tags: Map<String, String> = emptyMap(),
        block: () -> T
    ): T {
        val timer = getOrCreateTimer(VIRTUAL_THREAD_EXECUTION, name, tags)
        val sample = Timer.start(meterRegistry)

        return try {
            val result = block()
            sample.stop(timer)
            recordSuccess(name, "virtual_thread", tags)
            result
        } catch (e: Exception) {
            sample.stop(timer)
            recordFailure(name, "virtual_thread", e, tags)
            throw e
        }
    }

    /**
     * 记录任务执行指标（通用）
     */
    suspend fun <T> recordTaskExecution(
        name: String,
        taskType: String = "default",
        tags: Map<String, String> = emptyMap(),
        block: suspend () -> T
    ): T {
        val allTags = tags + ("task_type" to taskType)
        val timer = getOrCreateTimer(TASK_EXECUTION, name, allTags)
        val sample = Timer.start(meterRegistry)

        return try {
            val result = block()
            sample.stop(timer)
            recordSuccess(name, taskType, allTags)
            result
        } catch (e: Exception) {
            sample.stop(timer)
            recordFailure(name, taskType, e, allTags)
            throw e
        }
    }

    /**
     * 获取或创建计时器
     */
    private fun getOrCreateTimer(
        metricName: String,
        taskName: String,
        tags: Map<String, String>
    ): Timer {
        val cacheKey = "$metricName:$taskName:${tags.hashCode()}"
        return timerCache.getOrPut(cacheKey) {
            Timer.builder(metricName)
                .description("Async task execution time")
                .tag("task", taskName)
                .tags(tags.map { Tag.of(it.key, it.value) })
                .register(meterRegistry)
        }
    }

    /**
     * 记录成功执行
     */
    private fun recordSuccess(
        taskName: String,
        taskType: String,
        tags: Map<String, String>
    ) {
        meterRegistry.counter(
            "$METRIC_PREFIX.task.success",
            listOf(
                Tag.of("task", taskName),
                Tag.of("type", taskType)
            ) + tags.map { Tag.of(it.key, it.value) }
        ).increment()
    }

    /**
     * 记录失败执行
     */
    private fun recordFailure(
        taskName: String,
        taskType: String,
        exception: Exception,
        tags: Map<String, String>
    ) {
        meterRegistry.counter(
            "$METRIC_PREFIX.task.failure",
            listOf(
                Tag.of("task", taskName),
                Tag.of("type", taskType),
                Tag.of("exception", exception.javaClass.simpleName)
            ) + tags.map { Tag.of(it.key, it.value) }
        ).increment()

        logger.warn("Task [$taskName] of type [$taskType] failed", exception)
    }

    /**
     * 记录并发任务数
     */
    fun recordConcurrentTasks(taskType: String, count: Int) {
        meterRegistry.gauge(
            "$METRIC_PREFIX.concurrent.tasks",
            listOf(Tag.of("type", taskType)),
            count
        )
    }

    /**
     * 记录队列大小
     */
    fun recordQueueSize(queueName: String, size: Int) {
        meterRegistry.gauge(
            "$METRIC_PREFIX.queue.size",
            listOf(Tag.of("queue", queueName)),
            size
        )
    }

    /**
     * 记录虚拟线程数量
     */
    fun recordVirtualThreadCount() {
        val threadCount = Thread.getAllStackTraces().keys
            .count { it.isVirtual }

        meterRegistry.gauge(
            "$METRIC_PREFIX.virtual_thread.count",
            threadCount
        )
    }

    /**
     * 记录平台线程数量
     */
    fun recordPlatformThreadCount() {
        val threadCount = Thread.getAllStackTraces().keys
            .count { !it.isVirtual }

        meterRegistry.gauge(
            "$METRIC_PREFIX.platform_thread.count",
            threadCount
        )
    }
}

/**
 * 可监控的协程上下文元素
 * 用于在协程执行前后自动记录指标
 *
 * @author Claude
 * @since 1.0.0
 */
class MetricsCoroutineContext(
    private val metricsService: AsyncMetricsService,
    private val taskName: String,
    private val tags: Map<String, String> = emptyMap()
) : CoroutineContext.Element {

    companion object Key : CoroutineContext.Key<MetricsCoroutineContext>

    override val key: CoroutineContext.Key<*>
        get() = Key
}

/**
 * 协程监控扩展函数
 */
suspend fun <T> withMetrics(
    metricsService: AsyncMetricsService,
    taskName: String,
    tags: Map<String, String> = emptyMap(),
    block: suspend CoroutineScope.() -> T
): T {
    return metricsService.recordCoroutineExecution(taskName, tags, block)
}
