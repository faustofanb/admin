package io.github.faustofan.admin.shared.infrastructure.async

import io.github.faustofan.admin.shared.application.context.AppContext
import io.github.faustofan.admin.shared.application.context.AppContextHolder
import io.github.faustofan.admin.shared.infrastructure.async.metrics.AsyncMetricsService
import io.github.faustofan.admin.shared.infrastructure.config.async.CoroutineConfig
import io.github.faustofan.admin.shared.infrastructure.config.async.VirtualThreadConfig
import io.micrometer.core.instrument.simple.SimpleMeterRegistry
import kotlinx.coroutines.*
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.slf4j.MDC
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.scheduling.annotation.Async
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.test.context.TestPropertySource
import java.util.concurrent.CompletableFuture
import java.util.concurrent.atomic.AtomicInteger

/**
 * 异步功能集成测试
 *
 * 测试虚拟线程、协程、上下文传播、监控等功能的完整集成
 *
 * @author Claude
 * @since 1.0.0
 */
@SpringBootTest(
    classes = [
        AsyncIntegrationTest.TestConfig::class,
        VirtualThreadConfig::class,
        CoroutineConfig::class,
        AsyncIntegrationTest.IntegrationTestService::class
    ]
)
@TestPropertySource(
    properties = [
        "spring.threads.virtual.enabled=true"
    ]
)
class AsyncIntegrationTest {

    @Autowired
    private lateinit var testService: IntegrationTestService

    @Autowired
    @Qualifier("virtualThreadDispatcher")
    private lateinit var virtualThreadDispatcher: CoroutineDispatcher

    @Autowired
    @Qualifier("ioDispatcher")
    private lateinit var ioDispatcher: CoroutineDispatcher

    @Autowired
    @Qualifier("applicationCoroutineScope")
    private lateinit var applicationCoroutineScope: CoroutineScope

    @Autowired
    private lateinit var metricsService: AsyncMetricsService

    @BeforeEach
    fun setup() {
        SecurityContextHolder.clearContext()
        AppContextHolder.clear()
        MDC.clear()
    }

    @AfterEach
    fun cleanup() {
        SecurityContextHolder.clearContext()
        AppContextHolder.clear()
        MDC.clear()
    }

    @Test
    fun `test end-to-end async operation with context propagation`() = runBlocking {
        // Given - Setup complete context
        val authentication = UsernamePasswordAuthenticationToken("testUser", "password")
        SecurityContextHolder.getContext().authentication = authentication

        val appContext = AppContext(
            tenantId = 1L,
            userId = 100L,
            username = "testUser",
            orgId = 10L,
            requestId = "req-integration-test",
            traceId = 999L,
            currentUserRoles = emptyList(),
            isSuperAdmin = false
        )
        AppContextHolder.set(appContext)

        MDC.put("traceId", "trace-integration-test")

        // When - Execute async operation
        val result = testService.completeAsyncFlow()

        // Then
        assertNotNull(result)
        assertTrue(result.contains("testUser"))
        assertTrue(result.contains("1")) // tenantId
        assertTrue(result.contains("trace-integration-test"))
    }

    @Test
    fun `test virtual thread and coroutine interaction`() = runBlocking {
        // Given
        val counter = AtomicInteger(0)
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

        // When - Mix virtual threads and coroutines
        val virtualThreadFuture = testService.virtualThreadOperation(counter)

        val coroutineResult = withContext(ioDispatcher + AppContextElement()) {
            delay(50)
            counter.incrementAndGet()
            AppContextHolder.get().tenantId
        }

        virtualThreadFuture.get()

        // Then
        assertTrue(counter.get() >= 2, "Both operations should increment counter")
        assertEquals(1L, coroutineResult)
    }

    @Test
    fun `test parallel processing with context and metrics`() = runBlocking {
        // Given
        val appContext = AppContext(
            tenantId = 1L,
            userId = 100L,
            username = "testUser",
            orgId = 10L,
            requestId = "req-parallel",
            traceId = 999L,
            currentUserRoles = emptyList(),
            isSuperAdmin = false
        )
        AppContextHolder.set(appContext)

        MDC.put("operation", "parallel-test")

        // When - Process items in parallel with metrics
        val items = (1..50).toList()
        val results = metricsService.recordTaskExecution(
            name = "parallelProcessing",
            taskType = "batch"
        ) {
            CoroutineUtils.parallelMap(items, ioDispatcher) { item ->
                // Verify context is available
                val ctx = AppContextHolder.get()
                val mdc = MDC.get("operation")

                assertEquals(1L, ctx.tenantId)
                assertEquals("parallel-test", mdc)

                delay(5)
                item * 2
            }
        }

        // Then
        assertEquals(50, results.size)
        assertEquals((1..50).map { it * 2 }, results)
    }

    @Test
    fun `test complex business workflow`() = runBlocking {
        // Given - Simulate complete business context
        val authentication = UsernamePasswordAuthenticationToken("admin", "password")
        SecurityContextHolder.getContext().authentication = authentication

        val appContext = AppContext(
            tenantId = 1L,
            userId = 1L,
            username = "admin",
            orgId = 1L,
            requestId = "req-workflow",
            traceId = 999L,
            currentUserRoles = emptyList(),
            isSuperAdmin = true
        )
        AppContextHolder.set(appContext)

        // When - Execute complex workflow
        val workflowResult = CoroutineUtils.withBusinessContext(ioDispatcher) {
            // Step 1: Parallel data fetching
            val dataResults = CoroutineUtils.parallelTasks(
                context = ioDispatcher,
                tasks = List(5) {
                    suspend {
                        delay(10)
                        val ctx = AppContextHolder.get()
                        "data-${ctx.userId}-$it"
                    }
                }
            )

            // Step 2: Processing
            val processed = CoroutineUtils.parallelMap(dataResults, ioDispatcher) { data ->
                delay(5)
                data.uppercase()
            }

            // Step 3: Aggregation
            withContext(Dispatchers.Default) {
                processed.joinToString(",")
            }
        }

        // Then
        assertNotNull(workflowResult)
        assertTrue(workflowResult.contains("DATA-1"))
        assertEquals(5, workflowResult.split(",").size)
    }

    @Test
    fun `test error handling and resilience`() = runBlocking {
        // Given
        val appContext = AppContext(
            tenantId = 1L,
            userId = 100L,
            username = "testUser",
            orgId = 10L,
            requestId = "req-error",
            traceId = 999L,
            currentUserRoles = emptyList(),
            isSuperAdmin = false
        )
        AppContextHolder.set(appContext)

        val successCounter = AtomicInteger(0)
        val failureCounter = AtomicInteger(0)

        // When - Execute tasks with some failures
        val items = (1..10).toList()

        CoroutineUtils.parallelExecute(ioDispatcher, items.map { item ->
            suspend {
                try {
                    if (item % 3 == 0) {
                        throw RuntimeException("Simulated error for item $item")
                    }
                    delay(10)
                    successCounter.incrementAndGet()
                } catch (e: Exception) {
                    failureCounter.incrementAndGet()
                }
                Unit
            }
        })

        // Then - Some tasks succeed, some fail
        assertTrue(successCounter.get() > 0, "Some tasks should succeed")
        assertTrue(failureCounter.get() > 0, "Some tasks should fail")
        assertEquals(10, successCounter.get() + failureCounter.get(), "All tasks should complete")
    }

    @Test
    fun `test concurrent operations maintain isolation`() = runBlocking {
        // Given - Two different tenants
        val results = mutableListOf<Long>()

        // When - Execute operations for different tenants concurrently
        val jobs = listOf(
            launch(ioDispatcher) {
                val ctx1 = AppContext(
                    tenantId = 1L, userId = 100L, username = "user1",
                    orgId = 10L, requestId = "req-1", traceId = 1L,
                    currentUserRoles = emptyList(), isSuperAdmin = false
                )
                withContext(AppContextElement(ctx1)) {
                    delay(20)
                    val ctx = AppContextHolder.get()
                    synchronized(results) {
                        results.add(ctx.tenantId!!)
                    }
                }
            },
            launch(ioDispatcher) {
                val ctx2 = AppContext(
                    tenantId = 2L, userId = 200L, username = "user2",
                    orgId = 20L, requestId = "req-2", traceId = 2L,
                    currentUserRoles = emptyList(), isSuperAdmin = false
                )
                withContext(AppContextElement(ctx2)) {
                    delay(20)
                    val ctx = AppContextHolder.get()
                    synchronized(results) {
                        results.add(ctx.tenantId!!)
                    }
                }
            }
        )

        jobs.forEach { it.join() }

        // Then - Each operation should see its own tenant
        assertEquals(2, results.size)
        assertTrue(results.contains(1L))
        assertTrue(results.contains(2L))
    }

    // ========================================================================
    // Test Service and Configuration
    // ========================================================================

    @Service
    @EnableAsync
    class IntegrationTestService {

        @Async
        fun virtualThreadOperation(counter: AtomicInteger): CompletableFuture<Int> {
            Thread.sleep(30)
            return CompletableFuture.completedFuture(counter.incrementAndGet())
        }

        suspend fun completeAsyncFlow(): String = withContext(Dispatchers.IO +
                SecurityContextElement() +
                AppContextElement() +
                MDCContextElement()) {

            delay(20)

            val auth = SecurityContextHolder.getContext().authentication
            val ctx = AppContextHolder.get()
            val traceId = MDC.get("traceId")

            "User: ${auth?.name}, Tenant: ${ctx.tenantId}, Trace: $traceId"
        }
    }

    @TestConfiguration
    class TestConfig {
        @Bean
        fun asyncMetricsService(): AsyncMetricsService {
            return AsyncMetricsService(SimpleMeterRegistry())
        }
    }
}
