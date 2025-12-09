package io.github.faustofan.admin.shared.infrastructure.async

import io.github.faustofan.admin.shared.application.context.AppContext
import io.github.faustofan.admin.shared.application.context.AppContextHolder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.concurrent.atomic.AtomicInteger

/**
 * 协程工具类测试
 *
 * @author Claude
 * @since 1.0.0
 */
class CoroutineUtilsTest {

    @BeforeEach
    fun setup() {
        AppContextHolder.clear()
    }

    @AfterEach
    fun cleanup() {
        AppContextHolder.clear()
    }

    @Test
    fun `test parallelTasks executes all tasks in parallel`() = runBlocking {
        // Given
        val counter = AtomicInteger(0)
        val tasks = List(10) {
            suspend {
                delay(10)
                counter.incrementAndGet()
            }
        }

        // When
        val results = CoroutineUtils.parallelTasks(Dispatchers.IO, tasks)

        // Then
        assertEquals(10, results.size)
        assertEquals(10, counter.get())
        results.forEach { result ->
            assertTrue(result in 1..10)
        }
    }

    @Test
    fun `test parallelMap transforms all items in parallel`() = runBlocking {
        // Given
        val items = (1..100).toList()

        // When
        val results = CoroutineUtils.parallelMap(items, Dispatchers.IO) { item ->
            delay(5)
            item * 2
        }

        // Then
        assertEquals(100, results.size)
        assertEquals((1..100).map { it * 2 }, results)
    }

    @Test
    fun `test chunkedParallelMap processes items in batches`() = runBlocking {
        // Given
        val items = (1..500).toList()

        // When
        val results = CoroutineUtils.chunkedParallelMap(
            items = items,
            batchSize = 50,
            context = Dispatchers.IO
        ) { item ->
            delay(1)
            item * 2
        }

        // Then
        assertEquals(500, results.size)
        assertEquals((1..500).map { it * 2 }, results)
    }

    @Test
    fun `test parallelExecute runs all tasks without returning values`() = runBlocking {
        // Given
        val counter = AtomicInteger(0)
        val tasks = List(20) {
            suspend {
                delay(5)
                counter.incrementAndGet()
                Unit
            }
        }

        // When
        CoroutineUtils.parallelExecute(Dispatchers.IO, tasks)

        // Then
        assertEquals(20, counter.get())
    }

    @Test
    fun `test batchFlow emits items in batches`() = runBlocking {
        // Given
        val items = (1..250).toList()

        // When
        val batches = CoroutineUtils.batchFlow(items, batchSize = 50).toList()

        // Then
        assertEquals(5, batches.size) // 250 / 50 = 5 batches
        batches.forEach { batch ->
            assertEquals(50, batch.size)
        }
    }

    @Test
    fun `test retry succeeds on first attempt`() = runBlocking {
        // Given
        var attemptCount = 0

        // When
        val result = CoroutineUtils.retry(times = 3) {
            attemptCount++
            "success"
        }

        // Then
        assertEquals("success", result)
        assertEquals(1, attemptCount)
    }

    @Test
    fun `test retry succeeds after failures`() = runBlocking {
        // Given
        var attemptCount = 0

        // When
        val result = CoroutineUtils.retry(times = 3, initialDelay = 10) {
            attemptCount++
            if (attemptCount < 3) {
                throw RuntimeException("Attempt $attemptCount failed")
            }
            "success"
        }

        // Then
        assertEquals("success", result)
        assertEquals(3, attemptCount)
    }

    @Test
    fun `test retry fails after all attempts`() = runBlocking {
        // Given
        var attemptCount = 0

        // When & Then
        try {
            CoroutineUtils.retry(times = 3, initialDelay = 10) {
                attemptCount++
                throw RuntimeException("Always fails")
            }
            fail("Expected exception to be thrown")
        } catch (e: RuntimeException) {
            assertEquals("Always fails", e.message)
            assertEquals(3, attemptCount)
        }
    }

    @Test
    fun `test withBusinessContext propagates AppContext`() = runBlocking {
        // Given
        val appContext = AppContext(
            tenantId = 1L,
            userId = 100L,
            username = "testUser",
            orgId = 10L,
            requestId = "req-123",
            traceId = 999L,
            currentUserRoles = emptyList(),
            isSuperAdmin = false
        )
        AppContextHolder.set(appContext)

        // When
        val result = CoroutineUtils.withBusinessContext(Dispatchers.IO) {
            val ctx = AppContextHolder.get()
            "${ctx.username}-${ctx.tenantId}"
        }

        // Then
        assertEquals("testUser-1", result)
    }

    @Test
    fun `test parallelTasks propagates business context`() = runBlocking {
        // Given
        val appContext = AppContext(
            tenantId = 1L,
            userId = 100L,
            username = "testUser",
            orgId = 10L,
            requestId = "req-123",
            traceId = 999L,
            currentUserRoles = emptyList(),
            isSuperAdmin = false
        )
        AppContextHolder.set(appContext)

        val tasks = List(5) {
            suspend {
                val ctx = AppContextHolder.get()
                ctx.tenantId
            }
        }

        // When
        val results = CoroutineUtils.parallelTasks(Dispatchers.IO, tasks)

        // Then
        results.forEach { tenantId ->
            assertEquals(1L, tenantId)
        }
    }

    @Test
    fun `test parallelMap handles exceptions`() = runBlocking {
        // Given
        val items = (1..10).toList()

        // When & Then
        try {
            CoroutineUtils.parallelMap(items, Dispatchers.IO) { item ->
                if (item == 5) {
                    throw RuntimeException("Error at item 5")
                }
                item * 2
            }
            fail("Expected exception to be thrown")
        } catch (e: RuntimeException) {
            assertEquals("Error at item 5", e.message)
        }
    }

    @Test
    fun `test chunkedParallelMap with small batch size`() = runBlocking {
        // Given
        val items = (1..10).toList()

        // When
        val results = CoroutineUtils.chunkedParallelMap(
            items = items,
            batchSize = 2,
            context = Dispatchers.IO
        ) { item ->
            item * 3
        }

        // Then
        assertEquals(10, results.size)
        assertEquals((1..10).map { it * 3 }, results)
    }

    @Test
    fun `test parallelExecute continues on individual task failure`() = runBlocking {
        // Given
        val successCounter = AtomicInteger(0)
        val tasks = List(10) { index ->
            suspend {
                if (index == 5) {
                    throw RuntimeException("Task $index failed")
                }
                delay(5)
                successCounter.incrementAndGet()
                Unit
            }
        }

        // When
        CoroutineUtils.parallelExecute(Dispatchers.IO, tasks)

        // Then - Even though one task failed, others should complete
        // Note: parallelExecute catches exceptions internally
        Thread.sleep(100) // Wait for completion
        assertEquals(9, successCounter.get())
    }

    @Test
    fun `test batchFlow with uneven batch size`() = runBlocking {
        // Given
        val items = (1..105).toList()

        // When
        val batches = CoroutineUtils.batchFlow(items, batchSize = 50).toList()

        // Then
        assertEquals(3, batches.size) // 50 + 50 + 5
        assertEquals(50, batches[0].size)
        assertEquals(50, batches[1].size)
        assertEquals(5, batches[2].size)
    }

    @Test
    fun `test retry with exponential backoff`() = runBlocking {
        // Given
        var attemptCount = 0
        val startTime = System.currentTimeMillis()

        // When
        try {
            CoroutineUtils.retry(
                times = 3,
                initialDelay = 50,
                maxDelay = 200,
                factor = 2.0
            ) {
                attemptCount++
                throw RuntimeException("Test")
            }
        } catch (e: RuntimeException) {
            // Expected
        }

        val elapsedTime = System.currentTimeMillis() - startTime

        // Then
        assertEquals(3, attemptCount)
        // Should have delays: 50ms + 100ms = 150ms minimum
        assertTrue(elapsedTime >= 150, "Expected at least 150ms, got $elapsedTime")
    }

    @Test
    fun `test businessContext creates combined context`() {
        // Given
        val appContext = AppContext(
            tenantId = 1L,
            userId = 100L,
            username = "testUser",
            orgId = 10L,
            requestId = "req-123",
            traceId = 999L,
            currentUserRoles = emptyList(),
            isSuperAdmin = false
        )
        AppContextHolder.set(appContext)

        // When
        val context = CoroutineUtils.businessContext()

        // Then
        val securityElement = context[SecurityContextElement]
        val appElement = context[AppContextElement]
        val mdcElement = context[MDCContextElement]

        assertTrue(securityElement != null, "Should have SecurityContextElement")
        assertTrue(appElement != null, "Should have AppContextElement")
        assertTrue(mdcElement != null, "Should have MDCContextElement")
    }
}
