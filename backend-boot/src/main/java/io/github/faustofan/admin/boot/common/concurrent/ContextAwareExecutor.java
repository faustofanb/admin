package io.github.faustofan.admin.boot.common.concurrent;

import io.github.faustofan.admin.boot.common.context.AppContextHolder;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * 支持应用上下文传递的线程池执行器。
 * 在异步任务执行前，将主线程的 AppContext 传递到子线程，保证上下文一致性。
 *
 * @param delegate 被包装的实际 ExecutorService 实例
 */
public record ContextAwareExecutor(ExecutorService delegate) {

    /**
     * 执行带有上下文传递的 Runnable 任务。
     * 在任务执行前设置主线程的 AppContext，执行后清理上下文。
     *
     * @param task 需要执行的 Runnable 任务
     */
    public void execute(Runnable task) {
        var context = AppContextHolder.get();
        delegate.execute(() -> {
            try {
                AppContextHolder.set(context);
                task.run();
            } finally {
                AppContextHolder.clear();
            }
        });
    }

    /**
     * 提交带有上下文传递的 Callable 任务，并返回 Future。
     * 在任务执行前设置主线程的 AppContext，执行后清理上下文。
     *
     * @param task 需要执行的 Callable 任务
     * @param <T>  返回结果类型
     * @return Future\<T\> 任务执行结果
     */
    public <T> Future<T> submit(Callable<T> task) {
        var context = AppContextHolder.get();
        return delegate.submit(() -> {
            try {
                AppContextHolder.set(context);
                return task.call();
            } finally {
                AppContextHolder.clear();
            }
        });
    }
}
