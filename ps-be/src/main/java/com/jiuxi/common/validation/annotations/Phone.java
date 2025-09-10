package com.jiuxi.common.validation.annotations;

import com.jiuxi.common.validation.validators.PhoneValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * @ClassName: Phone
 * @Description: 手机号验证注解
 * @Author: System
 * @Date: 2025/01/07
 * @Copyright: 2025 www.jiuxi.com Inc. All rights reserved.
 */
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = PhoneValidator.class)
public @interface Phone {
    
    String message() default "手机号格式不正确";
    
    Class<?>[] groups() default {};
    
    Class<? extends Payload>[] payload() default {};
    
    /**
     * 是否允许为空
     */
    boolean allowEmpty() default false;
}