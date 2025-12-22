package io.github.faustofan.admin.shared.distributed.enums;

/**
 * 锁策略枚举
 */
public enum LockStrategy {
    REENTRANT, // 可重入锁
    FAIR, // 公平锁
    READ, // 读锁
    WRITE // 写锁
}
