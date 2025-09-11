package com.jiuxi.shared.common.validation.annotations;

import com.jiuxi.shared.common.validation.validators.BusinessCodeValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * 业务编码验证注解
 * 
 * 验证业务编码格式，如部门编码、用户编码等
 * 规则：字母数字组合，3-20位，必须以字母开头
 * 
 * @author System
 * @since 2.2.2-SNAPSHOT
 */
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = BusinessCodeValidator.class)
public @interface BusinessCode {

    String message() default "{validation.businessCode.invalid}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    /**
     * 是否允许为空
     */
    boolean allowEmpty() default false;

    /**
     * 最小长度
     */
    int minLength() default 3;

    /**
     * 最大长度
     */
    int maxLength() default 20;

    /**
     * 自定义正则表达式
     */
    String pattern() default "";
}