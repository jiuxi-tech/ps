package com.jiuxi.admin.domain;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 模块边界注解
 * 
 * 用于标识模块的边界和依赖关系，确保模块间的清晰分离
 * 
 * @author 系统重构
 * @since 2.2.2
 */
@Target({ElementType.TYPE, ElementType.PACKAGE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ModuleBoundary {
    
    /**
     * 模块名称
     */
    String module();
    
    /**
     * 允许依赖的模块列表
     */
    String[] allowedDependencies() default {};
    
    /**
     * 禁止依赖的模块列表
     */
    String[] forbiddenDependencies() default {};
    
    /**
     * 是否为公共API
     */
    boolean publicApi() default false;
    
    /**
     * 模块描述
     */
    String description() default "";
}