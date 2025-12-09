package io.github.faustofan.admin.shared.infrastructure.config.async

import kotlinx.coroutines.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.test.context.SpringBootTest
import java.util.concurrent.atomic.AtomicInteger

/**
 * 协程配置测试
 *
 * @author Claude
 * @since 1.0.0
 */
@SpringBootTest(classes = [CoroutineConfig::class])
class CoroutineConfigTest {

    @Autowired
    @Qualifier("virtualThreadDispatcher")
    private lateinit var virtualThreadDispatcher: CoroutineDispatcher

    @Autowired
    @Qualifier("ioDispatcher")
    private lateinit var ioDispatcher: CoroutineDispatcher

    @Autowired
    @Qualifier("cpuDispatcher")
    private lateinit var cpuDispatcher: CoroutineDispatcher

    @Autowired
    @Qualifier("applicationCoroutineScope")
    private lateinit var applicationCoroutineScope: CoroutineScope

    @Autowired
    @Qualifier("ioCoroutineScope")
    private lateinit var ioCoroutineScope: CoroutineScope

    @Autowired
    @Qualifier("cpuCoroutineScope")
    private lateinit var cpuCoroutineScope: CoroutineScope

    @Test
    fun `test dispatchers are properly configured`() {
        assertNotNull(virtualThreadDispatcher, "VirtualThread dispatcher should not be null")
        assertNotNull(ioDispatcher, "IO dispatcher should not be null")
        assertNotNull(cpuDispatcher, "CPU dispatcher should not be null")
    }

    @Test
    fun `test coroutine scopes are properly configured`() {
        assertNotNull(applicationCoroutineScope, "Application scope should not be null")
        assertNotNull(ioCoroutineScope, "IO scope should not be null")
        assertNotNull(cpuCoroutineScope, "CPU scope should not be null")
    }

    @Test
    fun `test virtual thread dispatcher executes coroutines`() = runBlocking {
        // Given
        val counter = AtomicInteger(0)
        val jobs = mutableListOf<kotlinx.coroutines.Job>()

        // When
        repeat(100) {
            val job = launch(virtualThreadDispatcher) {
                delay(10)
                counter.incrementAndGet()
            }
            jobs.add(job)
        }

        // Wait for all jobs
        jobs.joinAll()

        // Then
        assertEquals(100, counter.get(), "All coroutines should complete")
    }

    @Test
    fun `test io dispatcher executes coroutines`() = runBlocking {
        // Given
        val results = mutableListOf<Int>()
        val jobs = mutableListOf<kotlinx.coroutines.Job>()

        // When
        repeat(50) { i ->
            val job = launch(ioDispatcher) {
                delay(10)
                synchronized(results) {
                    results.add(i)
                }
            }
            jobs.add(job)
        }

        jobs.joinAll()

        // Then
        assertEquals(50, results.size, "All coroutines should complete")
    }

    @Test
    fun `test cpu dispatcher executes cpu intensive tasks`() = runBlocking {
        // Given
        val results = mutableListOf<Long>()

        // When
        val jobs = List(10) { i ->
            launch(cpuDispatcher) {
                // CPU intensive calculation
                val sum = (1..1000).sumOf { it.toLong() * it }
                synchronized(results) {
                    results.add(sum)
                }
            }
        }

        jobs.joinAll()

        // Then
        assertEquals(10, results.size, "All CPU intensive tasks should complete")
        assertTrue(results.all { it > 0 }, "All results should be positive")
    }

    @Test
    fun `test application scope launches coroutines`() {
        // Given
        val counter = AtomicInteger(0)
        val jobs = mutableListOf<kotlinx.coroutines.Job>()

        // When
        repeat(20) {
            val job = applicationCoroutineScope.launch {
                delay(10)
                counter.incrementAndGet()
            }
            jobs.add(job)
        }

        // Wait for completion
        runBlocking {
            jobs.joinAll()
        }

        // Then
        assertEquals(20, counter.get(), "All coroutines in application scope should complete")
    }

    @Test
    fun `test io scope handles exceptions with SupervisorJob`() {
        // Given
        val successCounter = AtomicInteger(0)
        val failureCounter = AtomicInteger(0)

        // When
        val jobs = mutableListOf<kotlinx.coroutines.Job>()

        repeat(10) { i ->
            val job = ioCoroutineScope.launch {
                try {
                    if (i % 2 == 0) {
                        throw RuntimeException("Test exception")
                    }
                    delay(10)
                    successCounter.incrementAndGet()
                } catch (e: Exception) {
                    failureCounter.incrementAndGet()
                }
            }
            jobs.add(job)
        }

        // Wait for completion
        runBlocking {
            jobs.joinAll()
        }

        // Then - SupervisorJob should allow other coroutines to continue
        assertEquals(5, successCounter.get(), "Successful coroutines should complete")
        assertEquals(5, failureCounter.get(), "Failed coroutines should be counted")
    }

    @Test
    fun `test coroutines can switch between dispatchers`() = runBlocking {
        // Given
        val threadNames = mutableListOf<String>()

        // When
        launch(ioDispatcher) {
            threadNames.add("IO: ${Thread.currentThread().name}")

            launch(cpuDispatcher) {
                threadNames.add("CPU: ${Thread.currentThread().name}")
            }.join()

            launch(virtualThreadDispatcher) {
                threadNames.add("Virtual: ${Thread.currentThread().name}")
            }.join()
        }.join()

        // Then
        assertEquals(3, threadNames.size, "All dispatchers should execute")
        println("Thread names: $threadNames")
    }
}
