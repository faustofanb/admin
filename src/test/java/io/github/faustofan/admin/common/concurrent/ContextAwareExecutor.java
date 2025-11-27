package io.github.faustofan.admin.common.concurrent;

import io.github.faustofan.admin.common.context.AppContext;
import io.github.faustofan.admin.common.context.AppContextHolder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicReference;

@DisplayName("ContextAwareExecutor 单元测试")
class ContextAwareExecutorTest {

    @Test
    @DisplayName("execute 应在子线程中传递主线程的上下文")
    void executeShouldPropagateContextToChildThread() throws Exception {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        ContextAwareExecutor contextAwareExecutor = new ContextAwareExecutor(executorService);

        AppContext context = new AppContext("tenant", "user", "req", "trace");
        AppContextHolder.set(context);

        AtomicReference<AppContext> childContext = new AtomicReference<>();
        CountDownLatch latch = new CountDownLatch(1);

        contextAwareExecutor.execute(() -> {
            childContext.set(AppContextHolder.get());
            latch.countDown();
        });

        latch.await(1, TimeUnit.SECONDS);
        Assertions.assertEquals(context, childContext.get());

        AppContextHolder.clear();
        executorService.shutdown();
    }

    @Test
    @DisplayName("submit 应在子线程中传递主线程的上下文并返回结果")
    void submitShouldPropagateContextAndReturnResult() throws Exception {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        ContextAwareExecutor contextAwareExecutor = new ContextAwareExecutor(executorService);

        AppContext context = new AppContext("t", "u", "r", "tr");
        AppContextHolder.set(context);

        Future<AppContext> future = contextAwareExecutor.submit(AppContextHolder::get);
        Assertions.assertEquals(context, future.get());

        AppContextHolder.clear();
        executorService.shutdown();
    }

    @Test
    @DisplayName("execute 应将上下文传递给子线程，并在执行后清理子线程上下文")
    void executeShouldPropagateAndClearContext() throws Exception {
        // 1. 强制使用单线程池，确保后续验证使用的是同一个子线程
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        ContextAwareExecutor contextAwareExecutor = new ContextAwareExecutor(executorService);

        // 2. 准备上下文
        AppContext context = new AppContext("tenant", "user", "req", "trace");
        AppContextHolder.set(context);

        // 用于验证传递性的引用
        AtomicReference<AppContext> capturedInTask = new AtomicReference<>();
        CountDownLatch latch = new CountDownLatch(1);

        // 3. 执行任务
        contextAwareExecutor.execute(() -> {
            // 在子线程内部验证：应该能拿到上下文
            capturedInTask.set(AppContextHolder.get());
            latch.countDown();
        });

        latch.await(1, TimeUnit.SECONDS);

        // --- 验证点 1: 传递性 ---
        Assertions.assertEquals(context, capturedInTask.get(), "上下文应该被传递到子线程中");

        // --- 验证点 2: 主线程状态 ---
        // 主线程的 Context 不应该受影响（依然存在），因为 Executor 不应该清理调用者的 Context
        Assertions.assertNotNull(AppContextHolder.get(), "主线程的 Context 不应被 Executor 清理");

        // --- 验证点 3: 子线程清理（防污染）---
        // 我们向原始的 executor 提交一个新的简单任务，检查当前线程的 Context 是否为空
        Future<AppContext> cleanCheckFuture = executorService.submit(() -> AppContextHolder.get());
        AppContext remainingContext = cleanCheckFuture.get();

        Assertions.assertNull(remainingContext, "任务执行完后，子线程的 Context 应该被清理");

        // 清理资源
        executorService.shutdown();
        AppContextHolder.clear(); // 手动清理测试主线程
    }

    @Test
    @DisplayName("execute 任务为 null 应抛出异常")
    void executeWithNullTaskShouldThrowException() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        ContextAwareExecutor contextAwareExecutor = new ContextAwareExecutor(executorService);

        Assertions.assertThrows(NullPointerException.class, () -> contextAwareExecutor.execute(null));

        executorService.shutdown();
    }

    @Test
    @DisplayName("submit 任务为 null 应抛出异常")
    void submitWithNullTaskShouldThrowException() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        ContextAwareExecutor contextAwareExecutor = new ContextAwareExecutor(executorService);

        Assertions.assertThrows(NullPointerException.class, () -> contextAwareExecutor.submit(null));

        executorService.shutdown();
    }
}