package com.jiuxi.shared.common.validation.validators;

import com.jiuxi.shared.common.validation.annotations.DateRange;
import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * 日期范围验证器
 * 
 * @author System
 * @since 2.2.2-SNAPSHOT
 */
public class DateRangeValidator implements ConstraintValidator<DateRange, Object> {

    private String startDateField;
    private String endDateField;
    private String minDate;
    private String maxDate;
    private String pattern;
    private boolean allowEmpty;
    private DateTimeFormatter formatter;

    @Override
    public void initialize(DateRange constraintAnnotation) {
        this.startDateField = constraintAnnotation.startDateField();
        this.endDateField = constraintAnnotation.endDateField();
        this.minDate = constraintAnnotation.minDate();
        this.maxDate = constraintAnnotation.maxDate();
        this.pattern = constraintAnnotation.pattern();
        this.allowEmpty = constraintAnnotation.allowEmpty();
        this.formatter = DateTimeFormatter.ofPattern(pattern);
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null) {
            return allowEmpty;
        }

        // 如果是字符串类型的单个日期验证
        if (value instanceof String) {
            return isValidSingleDate((String) value);
        }

        // 如果是对象类型的日期范围验证
        if (StringUtils.hasText(startDateField) && StringUtils.hasText(endDateField)) {
            return isValidDateRange(value, context);
        }

        return true;
    }

    private boolean isValidSingleDate(String dateStr) {
        if (!StringUtils.hasText(dateStr)) {
            return allowEmpty;
        }

        try {
            LocalDate date = LocalDate.parse(dateStr, formatter);
            
            // 检查最小日期
            if (StringUtils.hasText(minDate)) {
                LocalDate min = LocalDate.parse(minDate, formatter);
                if (date.isBefore(min)) {
                    return false;
                }
            }
            
            // 检查最大日期
            if (StringUtils.hasText(maxDate)) {
                LocalDate max = LocalDate.parse(maxDate, formatter);
                if (date.isAfter(max)) {
                    return false;
                }
            }
            
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    private boolean isValidDateRange(Object obj, ConstraintValidatorContext context) {
        try {
            Field startField = obj.getClass().getDeclaredField(startDateField);
            Field endField = obj.getClass().getDeclaredField(endDateField);
            
            startField.setAccessible(true);
            endField.setAccessible(true);
            
            Object startValue = startField.get(obj);
            Object endValue = endField.get(obj);
            
            // 空值处理
            if (startValue == null || endValue == null) {
                return allowEmpty;
            }
            
            String startDateStr = startValue.toString();
            String endDateStr = endValue.toString();
            
            if (!StringUtils.hasText(startDateStr) || !StringUtils.hasText(endDateStr)) {
                return allowEmpty;
            }
            
            LocalDate startDate = LocalDate.parse(startDateStr, formatter);
            LocalDate endDate = LocalDate.parse(endDateStr, formatter);
            
            // 检查开始日期不能晚于结束日期
            if (startDate.isAfter(endDate)) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("开始日期不能晚于结束日期")
                       .addPropertyNode(startDateField)
                       .addConstraintViolation();
                return false;
            }
            
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}