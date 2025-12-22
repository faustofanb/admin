package io.github.faustofan.admin.shared.async;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import io.github.faustofan.admin.shared.common.exception.SystemException;
import io.github.faustofan.admin.shared.common.exception.errcode.SystemErrorCode;

/**
 * 本地锁切面
 */
@Aspect
@Component
public class LocalLockAspect {

    private static final Logger log = LoggerFactory.getLogger(LocalLockAspect.class);

    // 存储锁对象。生产环境建议使用 Caffeine Cache 设置过期时间，防止长期无用的 Key 占用内存
    private final ConcurrentHashMap<String, ReentrantLock> locks = new ConcurrentHashMap<>();

    private final SpelExpressionParser parser = new SpelExpressionParser();
    private final DefaultParameterNameDiscoverer discoverer = new DefaultParameterNameDiscoverer();

    @Around("@annotation(localLock)")
    public Object around(ProceedingJoinPoint point, LocalLock localLock) throws Throwable {
        String key = parseKey(localLock.key(), point);

        // 获取锁对象 (JDK 25 虚拟线程中，ReentrantLock 优于 synchronized)
        ReentrantLock lock = locks.computeIfAbsent(key, k -> new ReentrantLock());

        boolean acquired = false;
        try {
            // 尝试加锁
            acquired = lock.tryLock(localLock.waitTime(), localLock.timeUnit());
            if (acquired) {
                return point.proceed();
            } else {
                log.warn("Failed to acquire local lock for key: {}", key);
                throw new SystemException(SystemErrorCode.TIMEOUT);
            }
        } finally {
            if (acquired) {
                lock.unlock();
                // 简单的清理策略：如果此时没有其他线程在等待该锁，可以尝试移除(非原子操作，仅作优化参考)
                locks.remove(key, lock);
            }
        }
    }

    /**
     * 解析 SpEL 表达式
     */
    private String parseKey(String spel, ProceedingJoinPoint point) {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        Object[] args = point.getArgs();

        if (!spel.contains("#")) {
            return spel;
        }

        StandardEvaluationContext context = new StandardEvaluationContext();
        String[] paramNames = discoverer.getParameterNames(method);
        if (paramNames != null) {
            for (int i = 0; i < paramNames.length; i++) {
                context.setVariable(paramNames[i], args[i]);
            }
        }
        return parser.parseExpression(spel).getValue(context, String.class);
    }
}