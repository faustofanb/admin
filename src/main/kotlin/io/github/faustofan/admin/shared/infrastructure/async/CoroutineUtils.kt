package io.github.faustofan.admin.shared.infrastructure.async

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.flow
import org.slf4j.LoggerFactory
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

/**
 * 协程工具类
 *
 * 提供常用协程操作的便捷方法，集成上下文传播
 *
 * @author Claude
 * @since 1.0.0
 */
object CoroutineUtils {

    private val logger = LoggerFactory.getLogger(CoroutineUtils::class.java)

    /**
     * 创建完整的业务上下文
     * 自动传播 SecurityContext、AppContext、MDC
     */
    fun businessContext(): CoroutineContext {
        return SecurityContextElement() +
                AppContextElement() +
                MDCContextElement()
    }

    /**
     * 在指定上下文中执行挂起函数，自动传播业务上下文
     */
    suspend fun <T> withBusinessContext(
        context: CoroutineContext = EmptyCoroutineContext,
        block: suspend CoroutineScope.() -> T
    ): T {
        return withContext(businessContext() + context, block)
    }

    /**
     * 并行执行多个任务，返回结果列表
     * 自动传播业务上下文
     *
     * @param tasks 任务列表
     * @return 结果列表
     */
    suspend fun <T> parallelTasks(
        context: CoroutineContext = EmptyCoroutineContext,
        tasks: List<suspend () -> T>
    ): List<T> = coroutineScope {
        val businessCtx = businessContext() + context
        tasks.map { task ->
            async(businessCtx) {
                try {
                    task()
                } catch (e: Exception) {
                    logger.error("Parallel task failed", e)
                    throw e
                }
            }
        }.awaitAll()
    }

    /**
     * 批量处理集合元素（并行）
     * 自动传播业务上下文
     *
     * @param items 待处理元素
     * @param transform 转换函数
     * @return 转换结果列表
     */
    suspend fun <T, R> parallelMap(
        items: Collection<T>,
        context: CoroutineContext = EmptyCoroutineContext,
        transform: suspend (T) -> R
    ): List<R> = coroutineScope {
        val businessCtx = businessContext() + context
        items.map { item ->
            async(businessCtx) {
                try {
                    transform(item)
                } catch (e: Exception) {
                    logger.error("Parallel map failed for item: {}", item, e)
                    throw e
                }
            }
        }.awaitAll()
    }

    /**
     * 批量处理集合元素（分批并行）
     * 避免创建过多协程，控制并发度
     *
     * @param items 待处理元素
     * @param batchSize 批次大小
     * @param transform 转换函数
     * @return 转换结果列表
     */
    suspend fun <T, R> chunkedParallelMap(
        items: Collection<T>,
        batchSize: Int = 100,
        context: CoroutineContext = EmptyCoroutineContext,
        transform: suspend (T) -> R
    ): List<R> = coroutineScope {
        val businessCtx = businessContext() + context
        items.chunked(batchSize).flatMap { chunk ->
            chunk.map { item ->
                async(businessCtx) {
                    try {
                        transform(item)
                    } catch (e: Exception) {
                        logger.error("Chunked parallel map failed for item: {}", item, e)
                        throw e
                    }
                }
            }.awaitAll()
        }
    }

    /**
     * 并发执行多个任务（不关心返回值）
     * 自动传播业务上下文
     *
     * @param tasks 任务列表
     */
    suspend fun parallelExecute(
        context: CoroutineContext = EmptyCoroutineContext,
        tasks: List<suspend () -> Unit>
    ) = coroutineScope {
        val businessCtx = businessContext() + context
        tasks.map { task ->
            launch(businessCtx) {
                try {
                    task()
                } catch (e: Exception) {
                    logger.error("Parallel execution failed", e)
                }
            }
        }.joinAll()
    }

    /**
     * 创建批处理 Flow
     * 用于处理大量数据，自动分批和背压控制
     *
     * @param items 数据集合
     * @param batchSize 批次大小
     * @param bufferCapacity 缓冲区大小
     */
    fun <T> batchFlow(
        items: Collection<T>,
        batchSize: Int = 100,
        bufferCapacity: Int = 64
    ): Flow<List<T>> = flow {
        items.chunked(batchSize).forEach { batch ->
            emit(batch)
        }
    }.buffer(bufferCapacity)

    /**
     * 异步执行任务但不阻塞
     * 返回 Job 用于后续控制
     *
     * @param scope 协程作用域
     * @param context 协程上下文
     * @param block 任务块
     */
    fun fireAndForget(
        scope: CoroutineScope,
        context: CoroutineContext = EmptyCoroutineContext,
        block: suspend CoroutineScope.() -> Unit
    ): Job {
        val businessCtx = businessContext() + context
        return scope.launch(businessCtx) {
            try {
                block()
            } catch (e: Exception) {
                logger.error("Fire-and-forget task failed", e)
            }
        }
    }

    /**
     * 重试包装器
     * 在失败时自动重试指定次数
     *
     * @param times 重试次数
     * @param initialDelay 初始延迟（毫秒）
     * @param maxDelay 最大延迟（毫秒）
     * @param factor 延迟倍率
     * @param block 任务块
     */
    suspend fun <T> retry(
        times: Int = 3,
        initialDelay: Long = 100,
        maxDelay: Long = 1000,
        factor: Double = 2.0,
        block: suspend () -> T
    ): T {
        var currentDelay = initialDelay
        repeat(times - 1) { attempt ->
            try {
                return block()
            } catch (e: Exception) {
                logger.warn("Retry attempt {} failed", attempt + 1, e)
            }
            kotlinx.coroutines.delay(currentDelay)
            currentDelay = (currentDelay * factor).toLong().coerceAtMost(maxDelay)
        }
        return block() // 最后一次尝试，失败则抛出异常
    }
}
