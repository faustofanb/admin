package io.github.faustofan.admin.shared.messaging.util;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

/**
 * 重试工具类
 */
public class RetryUtils {

    /**
     * 函数式异步重试工具
     *
     * @param task        任务提供者
     * @param maxAttempts 最大重试次数
     * @param <T>         返回类型
     * @return 包含结果的 CompletableFuture
     */
    public static <T> CompletableFuture<T> retryAsync(Supplier<CompletableFuture<T>> task, int maxAttempts) {
        return task.get().handle((res, ex) -> {
            if (ex == null) {
                return CompletableFuture.completedFuture(res);
            }
            if (maxAttempts <= 1) {
                return CompletableFuture.<T>failedFuture(ex);
            }
            // 简单的指数退避模拟（生产环境可用 Resilience4j 替代）
            try {
                Thread.sleep(100);
            } catch (InterruptedException ignored) {
            }

            return retryAsync(task, maxAttempts - 1);
        }).thenCompose(f -> f); // 扁平化 Future
    }
}
