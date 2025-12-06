package io.github.faustofan.admin.common.context

import io.github.faustofan.admin.common.unique.RequestIdGenerator
import io.github.faustofan.admin.common.unique.SnowflakeIdGenerator

/**
 * 应用上下文持有者，使用 ThreadLocal 存储每个线程的 AppContext 信息。
 * 提供设置、获取、清除上下文的方法，便于在多线程环境下安全地访问上下文数据。
 */
object AppContextHolder {
    /**
     * 用于存储每个线程的 AppContext 信息，支持子线程继承。
     */
    private val CONTEXT: ThreadLocal<AppContext> = InheritableThreadLocal()

    /**
     * 设置当前线程的应用上下文。
     *
     * @param context 要设置的 AppContext 实例
     */
    fun set(context: AppContext) {
        CONTEXT.set(context)
    }

    /**
     * 获取当前线程的应用上下文。
     *
     * @return 当前线程的 AppContext 实例，可能为 null
     */
    fun get(): AppContext {
        return CONTEXT.get() ?: AppContext(
            999999999L,
            888888888L,
            "Super Admin",
            777777777L,
            RequestIdGenerator.next(),
            SnowflakeIdGenerator.Companion.instance!!.nextId(),
            listOf(RoleDataScopeInfo(DataScope.ALL)),
            true
        )
    }
    /**
     * 清除当前线程的应用上下文，防止内存泄漏。
     */
    fun clear() {
        CONTEXT.remove()
    }
}
