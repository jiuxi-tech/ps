package com.jiuxi.shared.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Description: 接口限流注解
 * @ClassName: RateLimiter
 * @Author: pand
 * @Date: 2022-12-13 16:03
 * @Copyright: 2022 www.tuxun.net Inc. All rights reserved.
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD}) // 注解可以在方法和类上
public @interface RateLimiterAnnotation {

    /**
     * 资源的key,唯一
     * 作用：不同的接口，不同的流量控制
     */
    String key() default "";

    /**
     * 最多的访问限制次数
     */
    double count();

    /**
     * 限制的时间内，单位毫秒
     */
    long timeout();

    /**
     * 超过限流控制的提示语
     */
    String msg() default "请稍后再试.";

}
