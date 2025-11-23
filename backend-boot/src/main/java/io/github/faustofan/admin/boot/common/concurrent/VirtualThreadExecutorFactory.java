package io.github.faustofan.admin.boot.common.concurrent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 虚拟线程执行器工厂类，提供创建基于虚拟线程的 ExecutorService 的方法。
 */
public class VirtualThreadExecutorFactory {
    private VirtualThreadExecutorFactory() {
    }

    /**
     * 创建一个基于虚拟线程的 ExecutorService，每个任务使用一个虚拟线程执行。
     *
     * @param namePrefix 线程名称前缀
     * @return 虚拟线程的 ExecutorService
     */
    public static ExecutorService newVirtualThreadPerTaskExecutor(
            String namePrefix
    ) {
        return Executors.newThreadPerTaskExecutor(
                Thread.ofVirtual()
                        .name(namePrefix + "-%", 0)
                        .factory()
        );
    }

    /**
     * 创建一个支持应用上下文传递的虚拟线程执行器。
     *
     * @param namePrefix 线程名称前缀
     * @return 支持上下文传递的 ContextAwareExecutor
     */
    public static ContextAwareExecutor newContextAwareVirtualExecutor(
            String namePrefix
    ) {
        var virtualExecutor = newVirtualThreadPerTaskExecutor(namePrefix);
        return new ContextAwareExecutor(virtualExecutor);
    }
}
