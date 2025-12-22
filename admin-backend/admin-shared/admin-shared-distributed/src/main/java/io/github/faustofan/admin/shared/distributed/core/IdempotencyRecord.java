package io.github.faustofan.admin.shared.distributed.core;

import java.io.Serializable;
import java.time.Instant;

import io.github.faustofan.admin.shared.distributed.enums.ProcessStatus;

/**
 * 幂等上下文记录 (Java 25 Record)
 * <p>
 * 用于存储一次请求的执行状态和结果
 * </p>
 */
public record IdempotencyRecord<T>(
        ProcessStatus status,
        T result, // 成功结果 (需可序列化)
        String errorMessage, // 失败信息
        Instant createTime) implements Serializable {

    // 工厂方法：初始化中
    public static <T> IdempotencyRecord<T> pending() {
        return new IdempotencyRecord<>(ProcessStatus.PENDING, null, null, Instant.now());
    }

    // 工厂方法：成功
    public static <T> IdempotencyRecord<T> success(T result) {
        return new IdempotencyRecord<>(ProcessStatus.SUCCESS, result, null, Instant.now());
    }

    // 工厂方法：失败
    public static <T> IdempotencyRecord<T> fail(String error) {
        return new IdempotencyRecord<>(ProcessStatus.FAIL, null, error, Instant.now());
    }
}
