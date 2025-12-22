package io.github.faustofan.admin.shared.distributed.aspect;

import java.util.Optional;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import io.github.faustofan.admin.shared.common.exception.UserException;
import io.github.faustofan.admin.shared.common.exception.errcode.UserErrorCode;
import io.github.faustofan.admin.shared.distributed.annotation.GlobalIdempotent;
import io.github.faustofan.admin.shared.distributed.constants.RedisKeyRegistry;
import io.github.faustofan.admin.shared.distributed.core.IdempotencyRecord;
import io.github.faustofan.admin.shared.distributed.core.RedisUtil;
import io.github.faustofan.admin.shared.distributed.util.SpelParserUtils;

/**
 * 全局幂等切面
 */
@Aspect
@Component
public class GlobalIdempotentAspect {

    private static final Logger log = LoggerFactory.getLogger(GlobalIdempotentAspect.class);
    private final RedisUtil redisOps;

    public GlobalIdempotentAspect(RedisUtil redisOps) {
        this.redisOps = redisOps;
    }

    @Around("@annotation(idempotent)")
    public Object handleIdempotency(ProceedingJoinPoint joinPoint, GlobalIdempotent idempotent) throws Throwable {
        String uniqueKey = SpelParserUtils.parseKey(idempotent.key(), joinPoint);

        // 1. 获取当前状态
        Optional<IdempotencyRecord<Object>> recordOpt = redisOps.get(RedisKeyRegistry.GOV_IDEMPOTENCY, uniqueKey);

        if (recordOpt.isPresent()) {
            IdempotencyRecord<Object> record = recordOpt.get();

            // Java 25 Switch Expression 处理状态流转
            return switch (record.status()) {
                case SUCCESS -> {
                    log.info("幂等命中: 直接返回缓存结果 Key=[{}]", uniqueKey);
                    yield record.result();
                }
                case PENDING -> throw new UserException(UserErrorCode.IDEMPOTENT_PENDING, idempotent.message());
                case FAIL -> {
                    log.info("上一次执行失败，允许重试 Key=[{}]", uniqueKey);
                    yield executeAndSave(joinPoint, uniqueKey);
                }
            };
        }

        // 2. 首次执行: 尝试设置 PENDING 状态
        // 使用 setIfAbsent 实现简单的加锁效果，防止高并发穿透
        boolean locked = redisOps.setIfAbsent(RedisKeyRegistry.GOV_IDEMPOTENCY, uniqueKey, IdempotencyRecord.pending());
        if (!locked) {
            // 如果设置失败，说明刚才一瞬间有其他线程插队了，抛出异常让用户稍后重试
            throw new UserException(UserErrorCode.IDEMPOTENT_FAIL, idempotent.message());
        }

        return executeAndSave(joinPoint, uniqueKey);
    }
    
    /**
     * 执行目标方法并保存结果
     * @param joinPoint     切点
     * @param key           唯一键
     * @return              方法执行结果    
     * @throws Throwable    方法执行异常  
     */
    private Object executeAndSave(ProceedingJoinPoint joinPoint, String key) throws Throwable {
        try {
            Object result = joinPoint.proceed();
            // 执行成功 -> 更新为 SUCCESS
            redisOps.set(RedisKeyRegistry.GOV_IDEMPOTENCY, key, IdempotencyRecord.success(result));
            return result;
        } catch (Throwable ex) {
            // 执行异常 -> 更新为 FAIL (允许下次重试)
            redisOps.set(RedisKeyRegistry.GOV_IDEMPOTENCY, key, IdempotencyRecord.fail(ex.getMessage()));
            throw ex;
        }
    }
}
