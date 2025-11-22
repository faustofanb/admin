package io.github.faustofanb.admin.module.audit.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import io.github.faustofanb.admin.module.audit.enums.BusinessType;

@Target({ ElementType.PARAMETER, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Log {
    /**
     * Module title
     */
    String title() default "";

    /**
     * Business type
     */
    BusinessType businessType() default BusinessType.OTHER;

    /**
     * Whether to save request data
     */
    boolean isSaveRequestData() default true;

    /**
     * Whether to save response data
     */
    boolean isSaveResponseData() default true;
}
