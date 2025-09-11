package com.jiuxi.shared.common.validation.annotations;

import com.jiuxi.shared.common.validation.validators.DateRangeValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * 日期范围验证注解
 * 
 * 验证日期是否在指定范围内
 * 
 * @author System
 * @since 2.2.2-SNAPSHOT
 */
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = DateRangeValidator.class)
public @interface DateRange {

    String message() default "{validation.dateRange.invalid}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    /**
     * 开始日期字段名（用于类级别验证）
     */
    String startDateField() default "";

    /**
     * 结束日期字段名（用于类级别验证）
     */
    String endDateField() default "";

    /**
     * 最小日期（格式：yyyy-MM-dd）
     */
    String minDate() default "";

    /**
     * 最大日期（格式：yyyy-MM-dd）
     */
    String maxDate() default "";

    /**
     * 日期格式
     */
    String pattern() default "yyyy-MM-dd";

    /**
     * 是否允许为空
     */
    boolean allowEmpty() default false;
}