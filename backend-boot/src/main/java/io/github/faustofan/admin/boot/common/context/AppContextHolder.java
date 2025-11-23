package io.github.faustofan.admin.boot.common.context;

import java.util.Optional;

/**
 * 应用上下文持有者，使用 ThreadLocal 存储每个线程的 AppContext 信息。
 * 提供设置、获取、清除上下文的方法，便于在多线程环境下安全地访问上下文数据。
 */
public class AppContextHolder {
    /**
     * 用于存储每个线程的 AppContext 信息，支持子线程继承。
     */
    private static final ThreadLocal<AppContext> CONTEXT = new InheritableThreadLocal<>();

    /**
     * 设置当前线程的应用上下文。
     *
     * @param context 要设置的 AppContext 实例
     */
    public static void set(final AppContext context) {
        CONTEXT.set(context);
    }

    /**
     * 获取当前线程的应用上下文。
     *
     * @return 当前线程的 AppContext 实例，可能为 null
     */
    public static AppContext get() {
        return CONTEXT.get();
    }

    /**
     * 获取当前线程的应用上下文（Optional 封装）。
     *
     * @return Optional 包装的 AppContext 实例
     */
    public static Optional<AppContext> getContext() {
        return Optional.ofNullable(CONTEXT.get());
    }

    /**
     * 清除当前线程的应用上下文，防止内存泄漏。
     */
    public static void clear() {
        CONTEXT.remove();
    }
}
