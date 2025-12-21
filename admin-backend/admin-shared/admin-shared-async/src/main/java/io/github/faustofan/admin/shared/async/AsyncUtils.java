package io.github.faustofan.admin.shared.async;

import io.github.faustofan.admin.shared.common.context.AppContext;
import io.github.faustofan.admin.shared.common.context.AppContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * 异步编程工具类 (JDK 25 Standard Edition)
 * <p>
 * 特点：
 * 1. 禁用预览特性 (StructuredTaskScope)，完全基于 CompletableFuture。
 * 2. 深度适配虚拟线程：利用虚拟线程 "阻塞不昂贵" 的特性，简化并发等待逻辑。
 * 3. 自动上下文透传：解决异步调用中 TraceId/MDC 丢失问题。
 * 4. 静态方法封装：方便在非 Bean 类中调用 Spring 线程池。
 *
 */
@Component
public class AsyncUtils implements ApplicationContextAware {

    private static final Logger log = LoggerFactory.getLogger(AsyncUtils.class);

    private static Executor defaultExecutor; // 对应 applicationTaskExecutor
    private static Executor ioExecutor;      // 对应 ioExecutor
    private static Executor batchExecutor;   // 对应 batchExecutor
    private static Executor cpuExecutor;     // 对应 cpuExecutor

    /**
     * 从 Spring 容器中提取线程池 Bean
     */
    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        // 这里的 Bean 名称需要和 AsyncConfig 中定义的一致
        defaultExecutor = context.getBean("applicationTaskExecutor", Executor.class);
        ioExecutor = context.getBean("ioExecutor", Executor.class);
        batchExecutor = context.getBean("batchExecutor", Executor.class);
        cpuExecutor = context.getBean("cpuExecutor", Executor.class);

        log.info("AsyncUtils initialized. Virtual Threads are ready.");
    }

    // =================================================================================
    // 1. 基础异步调用
    // =================================================================================

    /**
     * 异步执行 (无返回值) - 使用默认虚拟线程
     */
    public static CompletableFuture<Void> run(Runnable runnable) {
        return CompletableFuture.runAsync(wrap(runnable), defaultExecutor);
    }

    /**
     * 异步执行 (有返回值) - 使用默认虚拟线程
     */
    public static <T> CompletableFuture<T> supply(Supplier<T> supplier) {
        return CompletableFuture.supplyAsync(wrap(supplier), defaultExecutor);
    }

    /**
     * IO 密集型异步任务 (推荐 HTTP请求/文件读写)
     */
    public static <T> CompletableFuture<T> supplyIO(Supplier<T> supplier) {
        return CompletableFuture.supplyAsync(wrap(supplier), ioExecutor);
    }

    /**
     * CPU 密集型异步任务 (推荐 复杂计算/图像处理)
     * 注意：这会调度到平台线程池
     */
    public static <T> CompletableFuture<T> supplyCPU(Supplier<T> supplier) {
        return CompletableFuture.supplyAsync(wrap(supplier), cpuExecutor);
    }

    // =================================================================================
    // 2. 组合并发 (替代 StructuredTaskScope)
    // =================================================================================

    /**
     * 并行执行多个任务，并等待所有任务完成 (Join All)
     * <p>
     * 在虚拟线程环境下，CompletableFuture.join() 不会阻塞 OS 线程，
     * 因此这种写法既简单又高效，无需复杂的回调链。
     *
     * @param tasks 任务列表
     * @param <T>   返回类型
     * @return 结果列表（顺序与输入一致）
     */
    public static <T> List<T> joinAll(Collection<Supplier<T>> tasks) {
        if (tasks == null || tasks.isEmpty()) {
            return List.of();
        }

        // 1. 将所有任务提交到虚拟线程池
        List<CompletableFuture<T>> futures = tasks.stream()
                .map(task -> CompletableFuture.supplyAsync(wrap(task), defaultExecutor))
                .toList();

        // 2. 等待所有任务完成
        // join() 在虚拟线程中是轻量级操作
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

        // 3. 收集结果
        return futures.stream()
                .map(CompletableFuture::join) // 此时已确信完成，直接获取
                .collect(Collectors.toList());
    }

    /**
     * 并行批量处理 (使用限流线程池)
     * <p>
     * 适用于：批量处理 1000 条数据，但只允许同时并发 50 个（受 batchExecutor 限制），
     * 防止压垮数据库或下游服务。
     *
     * @param inputs    输入数据列表
     * @param processor 处理逻辑
     * @param <T>       输入类型
     * @param <R>       输出类型
     * @return 处理结果列表
     */
    public static <T, R> List<R> parallelBatch(List<T> inputs, Function<T, R> processor) {
        if (inputs == null || inputs.isEmpty()) {
            return List.of();
        }

        // 提交到 batchExecutor (在 AsyncConfig 中配置了 setConcurrencyLimit)
        List<CompletableFuture<R>> futures = inputs.stream()
                .map(input -> CompletableFuture.supplyAsync(wrap(() -> processor.apply(input)), batchExecutor))
                .toList();

        // 等待全部完成
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

        return futures.stream()
                .map(CompletableFuture::join)
                .toList();
    }

    // =================================================================================
    // 3. 辅助方法
    // =================================================================================

    /**
     * 虚拟线程友好的 Sleep
     * 不会阻塞平台线程
     */
    public static void sleep(Duration duration) {
        try {
            Thread.sleep(duration);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Thread interrupted during sleep", e);
        }
    }

    public static void sleep(long millis) {
        sleep(Duration.ofMillis(millis));
    }

    // =================================================================================
    // 4. MDC 上下文装饰器 (关键)
    // =================================================================================

    /**
     * 包装 Runnable，透传 MDC
     */
    private static Runnable wrap(Runnable runnable) {
        // 1. 在调用线程捕获上下文
        Map<String, String> context = MDC.getCopyOfContextMap();
        AppContext appContext = AppContextHolder.getContext();
        return () -> {
            // 2. 在执行线程(虚拟线程)恢复上下文
            if (context != null) {
                MDC.setContextMap(context);
            }
            if (appContext != null) {
                AppContextHolder.setContext(appContext);
            }
            try {
                runnable.run();
            } catch (Exception e) {
                // 确保异常日志能打印出 TraceId
                log.error("Async execution error: {}", e.getMessage(), e);
                throw e;
            } finally {
                // 3. 清理，防止污染
                MDC.clear();
                AppContextHolder.clearContext();
            }
        };
    }

    /**
     * 包装 Supplier，透传 MDC
     */
    private static <T> Supplier<T> wrap(Supplier<T> supplier) {
        Map<String, String> context = MDC.getCopyOfContextMap();
        AppContext appContext = AppContextHolder.getContext();
        return () -> {
            if (context != null) {
                MDC.setContextMap(context);
            }
            if (appContext != null) {
                AppContextHolder.setContext(appContext);
            }
            try {
                return supplier.get();
            } catch (Exception e) {
                log.error("Async execution error: {}", e.getMessage(), e);
                throw e;
            } finally {
                MDC.clear();
                AppContextHolder.clearContext();
            }
        };
    }
}
