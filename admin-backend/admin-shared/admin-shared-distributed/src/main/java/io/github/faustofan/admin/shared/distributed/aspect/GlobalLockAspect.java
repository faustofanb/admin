package io.github.faustofan.admin.shared.distributed.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import io.github.faustofan.admin.shared.distributed.annotation.GlobalLock;
import io.github.faustofan.admin.shared.distributed.core.DistributedLockTemplate;
import io.github.faustofan.admin.shared.distributed.util.SpelParserUtils;

@Aspect
@Component
public class GlobalLockAspect {

    private final DistributedLockTemplate lockTemplate;

    public GlobalLockAspect(DistributedLockTemplate lockTemplate) {
        this.lockTemplate = lockTemplate;
    }

    @Around("@annotation(globalLock)")
    public Object around(ProceedingJoinPoint joinPoint, GlobalLock globalLock) throws Throwable {
        String businessKey = SpelParserUtils.parseKey(globalLock.key(), joinPoint);

        return lockTemplate.execute(
                businessKey,
                globalLock.strategy(),
                globalLock.waitTime(),
                globalLock.leaseTime(),
                () -> {
                    try {
                        return joinPoint.proceed();
                    } catch (Throwable e) {
                        // 将受检异常包装抛出
                        throw new RuntimeException(e);
                    }
                });
    }
}
