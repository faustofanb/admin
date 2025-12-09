package io.github.faustofan.admin.shared.infrastructure.config.async

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.scheduling.annotation.Async
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.stereotype.Service
import org.springframework.test.context.TestPropertySource
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executor
import java.util.concurrent.atomic.AtomicInteger
import kotlin.test.assertEquals

/**
 * 虚拟线程配置测试
 *
 * @author Claude
 * @since 1.0.0
 */
@SpringBootTest(
    classes = [
        VirtualThreadConfig::class,
        VirtualThreadConfigTest.TestAsyncService::class
    ]
)
@TestPropertySource(
    properties = [
        "spring.threads.virtual.enabled=true"
    ]
)
class VirtualThreadConfigTest {

    @Autowired
    private lateinit var testService: TestAsyncService

    @Autowired
    @Qualifier("ioVirtualExecutor")
    private lateinit var ioVirtualExecutor: Executor

    @Autowired
    @Qualifier("cpuBoundExecutor")
    private lateinit var cpuBoundExecutor: Executor

    @Test
    fun `test async annotation uses virtual threads`() {
        // Given
        val result = testService.asyncOperation("test")

        // When
        val threadName = result.get()

        // Then
        assertTrue(threadName.contains("virt-async"),
            "Expected virtual thread name to contain 'virt-async', got: $threadName")
    }

    @Test
    fun `test io virtual executor creates virtual threads`() {
        // Given
        val threadNames = mutableListOf<String>()
        val latch = java.util.concurrent.CountDownLatch(10)

        // When
        repeat(10) {
            ioVirtualExecutor.execute {
                threadNames.add(Thread.currentThread().name)
                latch.countDown()
            }
        }

        // Then
        latch.await()
        threadNames.forEach { threadName ->
            assertTrue(threadName.contains("virt-io"),
                "Expected virtual thread name to contain 'virt-io', got: $threadName")
        }
    }

    @Test
    fun `test cpu bound executor uses platform threads`() {
        // Given
        val threadNames = mutableListOf<String>()
        val latch = java.util.concurrent.CountDownLatch(5)

        // When
        repeat(5) {
            cpuBoundExecutor.execute {
                threadNames.add(Thread.currentThread().name)
                latch.countDown()
            }
        }

        // Then
        latch.await()
        threadNames.forEach { threadName ->
            assertTrue(threadName.contains("cpu-bound"),
                "Expected platform thread name to contain 'cpu-bound', got: $threadName")
        }
    }

    @Test
    fun `test virtual threads can handle many concurrent tasks`() {
        // Given
        val taskCount = 10000
        val counter = AtomicInteger(0)
        val latch = java.util.concurrent.CountDownLatch(taskCount)

        // When
        repeat(taskCount) {
            ioVirtualExecutor.execute {
                counter.incrementAndGet()
                Thread.sleep(10) // Simulate I/O
                latch.countDown()
            }
        }

        // Then
        latch.await()
        assertEquals(taskCount, counter.get(),
            "All tasks should complete successfully")
    }

    @Test
    fun `test exception in async method is handled`() {
        // Given & When
        val result = testService.asyncOperationWithError()

        // Then
        try {
            result.get()
            throw AssertionError("Expected exception to be thrown")
        } catch (e: Exception) {
            assertTrue(e.cause is RuntimeException,
                "Expected RuntimeException as cause")
        }
    }

    // ========================================================================
    // Test Service
    // ========================================================================

    @Service
    @EnableAsync
    class TestAsyncService {

        @Async
        fun asyncOperation(value: String): CompletableFuture<String> {
            return CompletableFuture.completedFuture(
                Thread.currentThread().name
            )
        }

        @Async
        fun asyncOperationWithError(): CompletableFuture<Void> {
            return CompletableFuture.failedFuture(
                RuntimeException("Test exception")
            )
        }
    }
}
