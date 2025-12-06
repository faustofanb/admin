package io.github.faustofan.admin.common.thread

import io.github.faustofan.admin.common.context.AppContextHolder
import java.util.Objects
import java.util.concurrent.Callable
import java.util.concurrent.ExecutorService
import java.util.concurrent.Future

/**
 * 支持应用上下文传递的线程池执行器。
 * 在异步任务执行前，将主线程的 AppContext 传递到子线程，保证上下文一致性。
 *
 * @param delegate 被包装的实际 ExecutorService 实例
 */
data class ContextAwareExecutor(val delegate: ExecutorService?) {
    /**
     * 执行带有上下文传递的 Runnable 任务。
     * 在任务执行前设置主线程的 AppContext，执行后清理上下文。
     *
     * @param task 需要执行的 Runnable 任务
     */
    fun execute(task: Runnable?) {
        Objects.requireNonNull<Runnable?>(task, "task must not be null")
        val context = AppContextHolder.get()
        delegate!!.execute(Runnable {
            try {
                AppContextHolder.set(context)
                task!!.run()
            } finally {
                AppContextHolder.clear()
            }
        })
    }

    /**
     * 提交带有上下文传递的 Callable 任务，并返回 Future。
     * 在任务执行前设置主线程的 AppContext，执行后清理上下文。
     *
     * @param task 需要执行的 Callable 任务
     * @param <T>  返回结果类型
     * @return Future\<T></T>\> 任务执行结果
    </T> */
    fun <T> submit(task: Callable<T?>?): Future<T?> {
        Objects.requireNonNull<Callable<T?>?>(task, "task must not be null")
        val context = AppContextHolder.get()
        return delegate!!.submit<T?>(Callable {
            try {
                AppContextHolder.set(context)
                task!!.call()
            } finally {
                AppContextHolder.clear()
            }
        })
    }
}