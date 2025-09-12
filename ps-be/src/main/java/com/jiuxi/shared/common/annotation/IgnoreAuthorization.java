package com.jiuxi.shared.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author 杨攀
 * @description: 忽略鉴权的注解
 * @date 2020/5/22 15:53
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD}) // 注解只能在方法上
public @interface IgnoreAuthorization {
}
