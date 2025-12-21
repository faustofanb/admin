package io.github.faustofan.admin.shared.async;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LocalLock {

    /**
     * 锁的 Key，支持 SpEL 表达式
     * 例: "#user.id", "'export:' + #type"
     */
    String key();

    /**
     * 等待获取锁的时间 (默认 3秒)
     */
    long waitTime() default 3;

    /**
     * 时间单位
     */
    TimeUnit timeUnit() default TimeUnit.SECONDS;
}