package com.jiuxi.shared.common.annotation;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.jiuxi.shared.common.serializer.SensitiveInfoSerialize;
import com.jiuxi.shared.common.enums.SensitiveType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 脱敏注解
 * 加在字段属性上面 如：@SensitiveInfo(type = SensitiveType.OTHER, start = 1, end = 10)
 *
 * @author pand
 * @date 2020-08-28 20:45
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@JacksonAnnotationsInside
// 使用SensitiveInfoSerialize类实现注解逻辑
@JsonSerialize(using = SensitiveInfoSerialize.class)
public @interface SensitiveInfo {

    /**
     * 脱敏类型
     *
     * @return
     */
    SensitiveType type() default SensitiveType.MOBILE_PHONE;

    /**
     * 开始模糊位置
     *
     * @return
     */
    int start() default -1;

    /**
     * 模糊结束位置
     *
     * @return
     */
    int end() default -1;
}
