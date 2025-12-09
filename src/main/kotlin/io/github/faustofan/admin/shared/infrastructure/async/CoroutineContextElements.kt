package io.github.faustofan.admin.shared.infrastructure.async

import io.github.faustofan.admin.shared.application.context.AppContext
import io.github.faustofan.admin.shared.application.context.AppContextHolder
import kotlinx.coroutines.ThreadContextElement
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import kotlin.coroutines.CoroutineContext

/**
 * Spring Security 上下文协程传播器
 *
 * 在协程切换时自动传播 SecurityContext，确保：
 * 1. 认证信息在协程间传递
 * 2. 权限校验正常工作
 * 3. 审计日志记录当前用户
 *
 * 使用示例：
 * ```kotlin
 * withContext(Dispatchers.IO + SecurityContextElement()) {
 *     // 这里可以获取到当前登录用户信息
 *     val authentication = SecurityContextHolder.getContext().authentication
 * }
 * ```
 *
 * @author Claude
 * @since 1.0.0
 */
class SecurityContextElement(
    private val securityContext: SecurityContext = SecurityContextHolder.getContext()
) : ThreadContextElement<SecurityContext?> {

    companion object Key : CoroutineContext.Key<SecurityContextElement>

    override val key: CoroutineContext.Key<SecurityContextElement>
        get() = Key

    override fun updateThreadContext(context: CoroutineContext): SecurityContext? {
        val previousContext = SecurityContextHolder.getContext()
        SecurityContextHolder.setContext(securityContext)
        return previousContext
    }

    override fun restoreThreadContext(context: CoroutineContext, oldState: SecurityContext?) {
        if (oldState == null) {
            SecurityContextHolder.clearContext()
        } else {
            SecurityContextHolder.setContext(oldState)
        }
    }
}

/**
 * 应用上下文协程传播器
 *
 * 在协程切换时自动传播应用上下文，包括：
 * 1. 租户信息（tenantId）
 * 2. 用户信息（userId、username、orgId）
 * 3. 请求追踪（requestId、traceId）
 * 4. 角色权限（currentUserRoles、isSuperAdmin）
 *
 * 使用示例：
 * ```kotlin
 * withContext(Dispatchers.IO + AppContextElement()) {
 *     // 这里可以获取到当前应用上下文
 *     val appContext = AppContextHolder.get()
 *     val tenantId = appContext.tenantId
 *     val userId = appContext.userId
 * }
 * ```
 *
 * @author Claude
 * @since 1.0.0
 */
class AppContextElement(
    private val appContext: AppContext? = runCatching { AppContextHolder.get() }.getOrNull()
) : ThreadContextElement<AppContext?> {

    companion object Key : CoroutineContext.Key<AppContextElement>

    override val key: CoroutineContext.Key<AppContextElement>
        get() = Key

    override fun updateThreadContext(context: CoroutineContext): AppContext? {
        val previousContext = runCatching { AppContextHolder.get() }.getOrNull()
        appContext?.let { AppContextHolder.set(it) }
        return previousContext
    }

    override fun restoreThreadContext(context: CoroutineContext, oldState: AppContext?) {
        if (oldState == null) {
            AppContextHolder.clear()
        } else {
            AppContextHolder.set(oldState)
        }
    }
}

/**
 * MDC（Mapped Diagnostic Context）协程传播器
 *
 * 在协程切换时自动传播日志上下文（如 traceId、userId 等）
 *
 * @author Claude
 * @since 1.0.0
 */
class MDCContextElement(
    private val contextMap: Map<String, String> = org.slf4j.MDC.getCopyOfContextMap() ?: emptyMap()
) : ThreadContextElement<Map<String, String>?> {

    companion object Key : CoroutineContext.Key<MDCContextElement>

    override val key: CoroutineContext.Key<MDCContextElement>
        get() = Key

    override fun updateThreadContext(context: CoroutineContext): Map<String, String>? {
        val previousContextMap = org.slf4j.MDC.getCopyOfContextMap()
        org.slf4j.MDC.setContextMap(contextMap)
        return previousContextMap
    }

    override fun restoreThreadContext(context: CoroutineContext, oldState: Map<String, String>?) {
        if (oldState == null) {
            org.slf4j.MDC.clear()
        } else {
            org.slf4j.MDC.setContextMap(oldState)
        }
    }
}
