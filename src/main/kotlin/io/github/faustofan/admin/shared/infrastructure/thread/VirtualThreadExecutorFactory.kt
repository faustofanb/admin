package io.github.faustofan.admin.shared.infrastructure.thread

import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/**
 * 虚拟线程执行器工厂类，提供创建基于虚拟线程的 ExecutorService 的方法。
 */
object VirtualThreadExecutorFactory {
    /**
     * 创建一个基于虚拟线程的 ExecutorService，每个任务使用一个虚拟线程执行。
     *
     * @param namePrefix 线程名称前缀
     * @return 虚拟线程的 ExecutorService
     */
    fun newVirtualThreadPerTaskExecutor(
        namePrefix: String?
    ): ExecutorService {
        return Executors.newThreadPerTaskExecutor(
            Thread.ofVirtual()
                .name(namePrefix + "-%", 0)
                .factory()
        )
    }

    /**
     * 创建一个支持应用上下文传递的虚拟线程执行器。
     *
     * @param namePrefix 线程名称前缀
     * @return 支持上下文传递的 ContextAwareExecutor
     */
    fun newContextAwareVirtualExecutor(
        namePrefix: String?
    ): ContextAwareExecutor {
        val virtualExecutor = newVirtualThreadPerTaskExecutor(namePrefix)
        return ContextAwareExecutor(virtualExecutor)
    }
}
