package io.github.faustofan.admin.shared.distributed.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface GlobalIdempotent {

    /** 唯一业务Key，支持SpEL (例如 "#orderId") */
    String key();

    /** 提示信息 */
    String message() default "请求正在处理中或已完成，请勿重复操作";
}
