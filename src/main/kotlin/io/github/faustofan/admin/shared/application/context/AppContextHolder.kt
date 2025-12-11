package io.github.faustofan.admin.shared.application.context

import io.github.faustofan.admin.shared.infrastructure.exception.SysException
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
     * @return 当前线程的 AppContext 实例
     * @throws SysException 如果上下文未设置
     */
    fun get(): AppContext {
        return CONTEXT.get() ?: throw SysException(message = "应用上下文未设置")
    }

    /**
     * 获取当前线程的应用上下文（可空版本）。
     *
     * 用于某些场景（如登录）可能没有上下文的情况
     *
     * @return 当前线程的 AppContext 实例，如果未设置则返回 null
     */
    fun getOrNull(): AppContext? {
        return CONTEXT.get()
    }

    /**
     * 清除当前线程的应用上下文，防止内存泄漏。
     */
    fun clear() {
        CONTEXT.remove()
    }
}
