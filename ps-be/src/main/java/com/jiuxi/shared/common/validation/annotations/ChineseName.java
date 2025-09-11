package com.jiuxi.shared.common.validation.annotations;

import com.jiuxi.shared.common.validation.validators.ChineseNameValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * 中文姓名验证注解
 * 
 * 验证中文姓名格式：只允许中文字符，2-10个字符
 * 
 * @author System
 * @since 2.2.2-SNAPSHOT
 */
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = ChineseNameValidator.class)
public @interface ChineseName {

    String message() default "{validation.chineseName.invalid}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    /**
     * 是否允许为空
     */
    boolean allowEmpty() default false;

    /**
     * 最小长度
     */
    int minLength() default 2;

    /**
     * 最大长度
     */
    int maxLength() default 10;
}