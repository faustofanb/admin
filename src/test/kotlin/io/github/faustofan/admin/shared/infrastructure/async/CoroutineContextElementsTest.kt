package io.github.faustofan.admin.shared.infrastructure.async

import io.github.faustofan.admin.shared.application.context.AppContext
import io.github.faustofan.admin.shared.application.context.AppContextHolder
import io.github.faustofan.admin.shared.application.context.DataScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.slf4j.MDC
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder

/**
 * 协程上下文传播测试
 *
 * @author Claude
 * @since 1.0.0
 */
class CoroutineContextElementsTest {

    @BeforeEach
    fun setup() {
        // Clear all contexts before each test
        SecurityContextHolder.clearContext()
        AppContextHolder.clear()
        MDC.clear()
    }

    @AfterEach
    fun cleanup() {
        // Clean up after each test
        SecurityContextHolder.clearContext()
        AppContextHolder.clear()
        MDC.clear()
    }

    @Test
    fun `test SecurityContext propagation in coroutines`() = runBlocking {
        // Given
        val authentication = UsernamePasswordAuthenticationToken("testUser", "password")
        SecurityContextHolder.getContext().authentication = authentication

        // When
        withContext(Dispatchers.IO + SecurityContextElement()) {
            // Then - SecurityContext should be available in coroutine
            val auth = SecurityContextHolder.getContext().authentication
            assertNotNull(auth, "Authentication should not be null")
            assertEquals("testUser", auth?.name, "Username should match")
        }

        // Cleanup is handled by AfterEach
    }

    @Test
    fun `test AppContext propagation in coroutines`() = runBlocking {
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
        withContext(Dispatchers.IO + AppContextElement()) {
            // Then - AppContext should be available in coroutine
            val ctx = AppContextHolder.get()
            assertNotNull(ctx, "AppContext should not be null")
            assertEquals(1L, ctx.tenantId, "TenantId should match")
            assertEquals(100L, ctx.userId, "UserId should match")
            assertEquals("testUser", ctx.username, "Username should match")
            assertEquals("req-123", ctx.requestId, "RequestId should match")
        }

        // Cleanup is handled by AfterEach
    }

    @Test
    fun `test MDC propagation in coroutines`() = runBlocking {
        // Given
        MDC.put("traceId", "trace-123")
        MDC.put("userId", "user-456")

        // When
        withContext(Dispatchers.IO + MDCContextElement()) {
            // Then - MDC should be available in coroutine
            val traceId = MDC.get("traceId")
            val userId = MDC.get("userId")

            assertEquals("trace-123", traceId, "TraceId should match")
            assertEquals("user-456", userId, "UserId should match")
        }

        // Cleanup is handled by AfterEach
    }

    @Test
    fun `test multiple context elements propagation`() = runBlocking {
        // Given
        val authentication = UsernamePasswordAuthenticationToken("testUser", "password")
        SecurityContextHolder.getContext().authentication = authentication

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

        MDC.put("traceId", "trace-123")

        // When
        withContext(
            Dispatchers.IO +
                    SecurityContextElement() +
                    AppContextElement() +
                    MDCContextElement()
        ) {
            // Then - All contexts should be available
            val auth = SecurityContextHolder.getContext().authentication
            val ctx = AppContextHolder.get()
            val traceId = MDC.get("traceId")

            assertNotNull(auth, "Authentication should not be null")
            assertEquals("testUser", auth?.name)

            assertNotNull(ctx, "AppContext should not be null")
            assertEquals(1L, ctx.tenantId)
            assertEquals(100L, ctx.userId)

            assertEquals("trace-123", traceId)
        }

        // Cleanup is handled by AfterEach
    }

    @Test
    fun `test context restoration after coroutine completes`() = runBlocking {
        // Given
        val originalAuth = UsernamePasswordAuthenticationToken("originalUser", "password")
        SecurityContextHolder.getContext().authentication = originalAuth

        val originalContext = AppContext(
            tenantId = 1L,
            userId = 100L,
            username = "originalUser",
            orgId = 10L,
            requestId = "original-req",
            traceId = 999L,
            currentUserRoles = emptyList(),
            isSuperAdmin = false
        )
        AppContextHolder.set(originalContext)

        // When - Execute coroutine with different context
        val newAuth = UsernamePasswordAuthenticationToken("newUser", "password")
        val newContext = AppContext(
            tenantId = 2L,
            userId = 200L,
            username = "newUser",
            orgId = 20L,
            requestId = "new-req",
            traceId = 888L,
            currentUserRoles = emptyList(),
            isSuperAdmin = false
        )

        withContext(Dispatchers.IO) {
            launch(SecurityContextElement(SecurityContextHolder.createEmptyContext().apply {
                authentication = newAuth
            }) + AppContextElement(newContext)) {
                // Inside coroutine - should have new context
                val auth = SecurityContextHolder.getContext().authentication
                val ctx = AppContextHolder.get()

                assertEquals("newUser", auth?.name)
                assertEquals(2L, ctx.tenantId)
            }.join()
        }

        // Then - Original context should be restored
        val restoredAuth = SecurityContextHolder.getContext().authentication
        val restoredContext = AppContextHolder.get()

        assertEquals("originalUser", restoredAuth?.name)
        assertEquals(1L, restoredContext.tenantId)
    }

    @Test
    fun `test context propagation across multiple coroutine launches`() = runBlocking {
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

        val results = mutableListOf<Long?>()

        // When
        withContext(Dispatchers.IO + AppContextElement()) {
            repeat(5) {
                launch {
                    val ctx = AppContextHolder.get()
                    synchronized(results) {
                        results.add(ctx.tenantId)
                    }
                }
            }
        }

        // Then - All coroutines should have the same tenantId
        assertEquals(5, results.size)
        results.forEach { tenantId ->
            assertEquals(1L, tenantId)
        }
    }

    @Test
    fun `test AppContextElement handles missing context gracefully`() = runBlocking {
        // Given - No AppContext set
        AppContextHolder.clear()

        // When & Then - Should not throw exception
        withContext(Dispatchers.IO + AppContextElement()) {
            // Context element created with null appContext
            // Should not fail, just not set anything
        }
    }

    @Test
    fun `test context with SuperAdmin role`() = runBlocking {
        // Given
        val appContext = AppContext(
            tenantId = 1L,
            userId = 1L,
            username = "admin",
            orgId = 1L,
            requestId = "req-123",
            traceId = 999L,
            currentUserRoles = emptyList(),
            isSuperAdmin = true
        )
        AppContextHolder.set(appContext)

        // When
        withContext(Dispatchers.IO + AppContextElement()) {
            // Then
            val ctx = AppContextHolder.get()
            assertEquals(true, ctx.isSuperAdmin, "Should be super admin")
        }
    }

    @Test
    fun `test context with role data scope`() = runBlocking {
        // Given
        val appContext = AppContext(
            tenantId = 1L,
            userId = 100L,
            username = "testUser",
            orgId = 10L,
            requestId = "req-123",
            traceId = 999L,
            currentUserRoles = listOf(
                io.github.faustofan.admin.shared.application.context.RoleDataScopeInfo(
                    dataScope = DataScope.SAME_DEPT,
                    specificOrgIds = emptyList()
                )
            ),
            isSuperAdmin = false
        )
        AppContextHolder.set(appContext)

        // When
        withContext(Dispatchers.IO + AppContextElement()) {
            // Then
            val ctx = AppContextHolder.get()
            assertNotNull(ctx.currentUserRoles)
            assertEquals(1, ctx.currentUserRoles?.size)
            assertEquals(DataScope.SAME_DEPT, ctx.currentUserRoles?.first()?.dataScope)
        }
    }
}
