package com.jiuxi.shared.common.validation.annotations;

import com.jiuxi.shared.common.validation.validators.SafeStringValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * 安全字符串验证注解
 * 
 * 验证字符串是否包含恶意内容，如SQL注入、XSS攻击等
 * 
 * @author System
 * @since 2.2.2-SNAPSHOT
 */
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = SafeStringValidator.class)
public @interface SafeString {

    String message() default "{validation.safeString.invalid}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    /**
     * 是否允许为空
     */
    boolean allowEmpty() default true;

    /**
     * 是否检查SQL注入
     */
    boolean checkSqlInjection() default true;

    /**
     * 是否检查XSS攻击
     */
    boolean checkXss() default true;

    /**
     * 是否检查脚本标签
     */
    boolean checkScript() default true;

    /**
     * 额外的危险关键词
     */
    String[] dangerousKeywords() default {};
}