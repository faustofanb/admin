package io.github.faustofan.admin.shared.distributed.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

import io.github.faustofan.admin.shared.distributed.enums.LockStrategy;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface GlobalLock {

    /** 锁的后缀Key，支持SpEL (例如 "#dto.id") */
    String key();

    LockStrategy strategy() default LockStrategy.REENTRANT;

    long waitTime() default 5;

    long leaseTime() default -1; // -1 启用看门狗

    TimeUnit unit() default TimeUnit.SECONDS;
}
