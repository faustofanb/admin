package io.github.faustofan.admin.shared.distributed.enums;

/**
 * 业务处理状态 (用于幂等性状态机)
 */
public enum ProcessStatus {
    PENDING, // 处理中 (占用)
    SUCCESS, // 成功 (可返回结果)
    FAIL // 失败 (可重试)
}
