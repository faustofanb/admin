package io.github.faustofan.admin.shared.distributed.core;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import io.github.faustofan.admin.shared.common.exception.SystemException;
import io.github.faustofan.admin.shared.common.exception.errcode.SystemErrorCode;
import io.github.faustofan.admin.shared.distributed.constants.RedisKeyRegistry;
import io.github.faustofan.admin.shared.distributed.enums.LockStrategy;

/**
 * 分布式锁模板
 * <p>
 * 职责：封装锁的获取、释放及异常处理，提供函数式调用接口。
 * </p>
 */
@Component
public class DistributedLockTemplate {

    private static final Logger log = LoggerFactory.getLogger(DistributedLockTemplate.class);
    private final RedissonClient redissonClient;

    public DistributedLockTemplate(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    /**
     * 核心执行方法 (带返回值)
     */
    public <T> T execute(String businessKey, LockStrategy strategy, long waitTime, long leaseTime,
            Supplier<T> supplier) {
        // 强制使用 Registry 生成 Key
        String fullKey = RedisKeyRegistry.GOV_LOCK.buildKey(businessKey);
        RLock lock = getLockInstance(fullKey, strategy);

        try {
            // 尝试加锁
            boolean acquired = lock.tryLock(waitTime, leaseTime, TimeUnit.SECONDS);

            if (acquired) {
                try {
                    return supplier.get();
                } finally {
                    // 仅当锁被当前线程持有时释放
                    if (lock.isHeldByCurrentThread()) {
                        lock.unlock();
                    }
                }
            } else {
                log.warn("Lock acquisition failed for key: {}", fullKey);
                throw new SystemException(SystemErrorCode.DISTRIBUTED_ACQUIRE_LOCK_FAIL, "系统繁忙，请稍候重试");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new SystemException(SystemErrorCode.INTERRUPT_EXCEPTION, e);
        }
    }

    /**
     * 核心执行方法 (无返回值)
     */
    public void run(String businessKey, Runnable action) {
        execute(businessKey, LockStrategy.REENTRANT, 5, -1, () -> {
            action.run();
            return null;
        });
    }

    private RLock getLockInstance(String key, LockStrategy strategy) {
        return switch (strategy) {
            case REENTRANT -> redissonClient.getLock(key);
            case FAIR -> redissonClient.getFairLock(key);
            case READ -> redissonClient.getReadWriteLock(key).readLock();
            case WRITE -> redissonClient.getReadWriteLock(key).writeLock();
        };
    }
}
