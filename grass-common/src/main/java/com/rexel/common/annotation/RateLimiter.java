package com.rexel.common.annotation;

import com.rexel.common.constant.Constants;
import com.rexel.common.enums.LimitType;

import java.lang.annotation.*;

/**
 * 限流注解
 *
 * @author ids-admin
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RateLimiter {
    /**
     * 限流key
     */
    String key() default Constants.RATE_LIMIT_KEY;

    /**
     * 限流时间,单位秒
     */
    int time() default 60;

    /**
     * 限流次数
     */
    int count() default 100;

    /**
     * 限流类型
     */
    LimitType limitType() default LimitType.DEFAULT;
}
