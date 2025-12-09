package io.github.faustofan.admin.shared.infrastructure.async.metrics

import io.micrometer.core.instrument.simple.SimpleMeterRegistry
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

/**
 * 异步监控服务测试
 *
 * @author Claude
 * @since 1.0.0
 */
class AsyncMetricsServiceTest {

    private lateinit var meterRegistry: SimpleMeterRegistry
    private lateinit var metricsService: AsyncMetricsService

    @BeforeEach
    fun setup() {
        meterRegistry = SimpleMeterRegistry()
        metricsService = AsyncMetricsService(meterRegistry)
    }

    @Test
    fun `test recordCoroutineExecution records success metrics`() = runBlocking {
        // Given
        val taskName = "testTask"

        // When
        val result = metricsService.recordCoroutineExecution(taskName) {
            delay(10)
            "success"
        }

        // Then
        assertEquals("success", result)

        // Check timer metric
        val timer = meterRegistry.find("app.async.coroutine.execution")
            .tag("task", taskName)
            .timer()
        assertTrue(timer != null, "Timer should be registered")
        assertEquals(1L, timer?.count(), "Timer should record one execution")

        // Check success counter
        val successCounter = meterRegistry.find("app.async.task.success")
            .tag("task", taskName)
            .tag("type", "coroutine")
            .counter()
        assertTrue(successCounter != null, "Success counter should be registered")
        assertEquals(1.0, successCounter?.count(), "Success counter should be 1")
    }

    @Test
    fun `test recordCoroutineExecution records failure metrics`() = runBlocking {
        // Given
        val taskName = "failingTask"

        // When & Then
        try {
            metricsService.recordCoroutineExecution(taskName) {
                delay(10)
                throw RuntimeException("Test exception")
            }
            fail("Expected exception to be thrown")
        } catch (e: RuntimeException) {
            assertEquals("Test exception", e.message)
        }

        // Check timer metric (should still be recorded)
        val timer = meterRegistry.find("app.async.coroutine.execution")
            .tag("task", taskName)
            .timer()
        assertTrue(timer != null, "Timer should be registered")
        assertEquals(1L, timer?.count(), "Timer should record one execution")

        // Check failure counter
        val failureCounter = meterRegistry.find("app.async.task.failure")
            .tag("task", taskName)
            .tag("type", "coroutine")
            .tag("exception", "RuntimeException")
            .counter()
        assertTrue(failureCounter != null, "Failure counter should be registered")
        assertEquals(1.0, failureCounter?.count(), "Failure counter should be 1")
    }

    @Test
    fun `test recordVirtualThreadExecution records success metrics`() {
        // Given
        val taskName = "virtualThreadTask"

        // When
        val result = metricsService.recordVirtualThreadExecution(taskName) {
            Thread.sleep(10)
            "success"
        }

        // Then
        assertEquals("success", result)

        // Check timer metric
        val timer = meterRegistry.find("app.async.virtual_thread.execution")
            .tag("task", taskName)
            .timer()
        assertTrue(timer != null, "Timer should be registered")
        assertEquals(1L, timer?.count(), "Timer should record one execution")

        // Check success counter
        val successCounter = meterRegistry.find("app.async.task.success")
            .tag("task", taskName)
            .tag("type", "virtual_thread")
            .counter()
        assertTrue(successCounter != null, "Success counter should be registered")
        assertEquals(1.0, successCounter?.count(), "Success counter should be 1")
    }

    @Test
    fun `test recordVirtualThreadExecution records failure metrics`() {
        // Given
        val taskName = "failingVirtualThreadTask"

        // When & Then
        try {
            metricsService.recordVirtualThreadExecution(taskName) {
                throw RuntimeException("Virtual thread exception")
            }
            fail("Expected exception to be thrown")
        } catch (e: RuntimeException) {
            assertEquals("Virtual thread exception", e.message)
        }

        // Check failure counter
        val failureCounter = meterRegistry.find("app.async.task.failure")
            .tag("task", taskName)
            .tag("type", "virtual_thread")
            .tag("exception", "RuntimeException")
            .counter()
        assertTrue(failureCounter != null, "Failure counter should be registered")
        assertEquals(1.0, failureCounter?.count(), "Failure counter should be 1")
    }

    @Test
    fun `test recordTaskExecution with custom tags`() = runBlocking {
        // Given
        val taskName = "customTask"
        val tags = mapOf("customTag" to "customValue")

        // When
        val result = metricsService.recordTaskExecution(
            name = taskName,
            taskType = "batch",
            tags = tags
        ) {
            delay(10)
            "result"
        }

        // Then
        assertEquals("result", result)

        // Check timer metric with custom tag
        val timer = meterRegistry.find("app.async.task.execution")
            .tag("task", taskName)
            .tag("task_type", "batch")
            .tag("customTag", "customValue")
            .timer()
        assertTrue(timer != null, "Timer with custom tag should be registered")
        assertEquals(1L, timer?.count(), "Timer should record one execution")
    }

    @Test
    fun `test multiple executions accumulate metrics`() = runBlocking {
        // Given
        val taskName = "repeatedTask"

        // When
        repeat(5) {
            metricsService.recordCoroutineExecution(taskName) {
                delay(5)
                "success"
            }
        }

        // Then
        val timer = meterRegistry.find("app.async.coroutine.execution")
            .tag("task", taskName)
            .timer()
        assertTrue(timer != null, "Timer should be registered")
        assertEquals(5L, timer?.count(), "Timer should record 5 executions")

        val successCounter = meterRegistry.find("app.async.task.success")
            .tag("task", taskName)
            .tag("type", "coroutine")
            .counter()
        assertEquals(5.0, successCounter?.count(), "Success counter should be 5")
    }

    @Test
    fun `test recordConcurrentTasks sets gauge`() {
        // When
        metricsService.recordConcurrentTasks("io", 10)

        // Then
        val gauge = meterRegistry.find("app.async.concurrent.tasks")
            .tag("type", "io")
            .gauge()
        assertTrue(gauge != null, "Gauge should be registered")
        // Note: SimpleMeterRegistry may not hold the exact value,
        // but the registration should succeed
    }

    @Test
    fun `test recordQueueSize sets gauge`() {
        // When
        metricsService.recordQueueSize("mainQueue", 100)

        // Then
        val gauge = meterRegistry.find("app.async.queue.size")
            .tag("queue", "mainQueue")
            .gauge()
        assertTrue(gauge != null, "Gauge should be registered")
    }

    @Test
    fun `test withMetrics extension function`() = runBlocking {
        // Given
        val taskName = "extensionTask"

        // When
        val result = withMetrics(metricsService, taskName) {
            delay(10)
            "success"
        }

        // Then
        assertEquals("success", result)

        // Verify metrics were recorded
        val timer = meterRegistry.find("app.async.coroutine.execution")
            .tag("task", taskName)
            .timer()
        assertTrue(timer != null, "Timer should be registered")
        assertEquals(1L, timer?.count(), "Timer should record one execution")
    }

    @Test
    fun `test different task types have separate metrics`() = runBlocking {
        // Given & When
        metricsService.recordCoroutineExecution("task1") {
            delay(5)
            "success"
        }

        metricsService.recordVirtualThreadExecution("task2") {
            Thread.sleep(5)
            "success"
        }

        metricsService.recordTaskExecution("task3", "custom") {
            delay(5)
            "success"
        }

        // Then
        val coroutineTimer = meterRegistry.find("app.async.coroutine.execution")
            .tag("task", "task1")
            .timer()
        assertEquals(1L, coroutineTimer?.count())

        val virtualThreadTimer = meterRegistry.find("app.async.virtual_thread.execution")
            .tag("task", "task2")
            .timer()
        assertEquals(1L, virtualThreadTimer?.count())

        val customTimer = meterRegistry.find("app.async.task.execution")
            .tag("task", "task3")
            .timer()
        assertEquals(1L, customTimer?.count())
    }

    @Test
    fun `test timer caching works correctly`() = runBlocking {
        // Given
        val taskName = "cachedTask"

        // When - Execute same task multiple times
        repeat(3) {
            metricsService.recordCoroutineExecution(taskName) {
                delay(5)
                "success"
            }
        }

        // Then - Should use cached timer
        val timers = meterRegistry.find("app.async.coroutine.execution")
            .tag("task", taskName)
            .timers()

        assertEquals(1, timers.size, "Should only have one timer instance")
        assertEquals(3L, timers.first().count(), "Timer should record 3 executions")
    }
}
