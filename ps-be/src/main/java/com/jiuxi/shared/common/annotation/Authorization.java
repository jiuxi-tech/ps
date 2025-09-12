package com.jiuxi.shared.common.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author 杨攀
 * @description: 鉴权的注解
 * @date 2020/5/22 15:33
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD}) // 注解可以在方法和类上
public @interface Authorization {

    /**
     * 角色鉴权，默认：true
     * true：需求鉴权，
     * false：则不需要进行角色鉴权
     */
    boolean roleAuth() default true;

    /**
     * 水平鉴权，默认: 空字符串
     * 空字符串，则不需要水平鉴权
     * 设置业务主键字段名，则横向鉴权用
     */
    String businessKey() default "";

}
