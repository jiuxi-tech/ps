package com.jiuxi.shared.common.validation;

import com.jiuxi.shared.common.exception.ValidationException;
import org.springframework.stereotype.Component;

import javax.validation.*;
import javax.validation.groups.Default;
import java.util.Set;

/**
 * 简化验证框架
 * 
 * 提供基本的验证功能，与现有架构兼容
 * 
 * @author System
 * @since 2.2.2-SNAPSHOT
 */
@Component
public class SimpleValidationFramework {

    private static final ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
    private static final Validator validator = validatorFactory.getValidator();

    /**
     * 验证对象
     * 
     * @param object 待验证对象
     * @param groups 验证组
     * @param <T> 对象类型
     * @throws ValidationException 验证失败时抛出
     */
    public static <T> void validate(T object, Class<?>... groups) {
        if (object == null) {
            throw ValidationException.of("VALIDATION_FAILED", "验证对象不能为空");
        }

        Class<?>[] validationGroups = groups.length == 0 ? new Class<?>[]{Default.class} : groups;
        Set<ConstraintViolation<T>> violations = validator.validate(object, validationGroups);

        if (!violations.isEmpty()) {
            StringBuilder errorMessage = new StringBuilder();
            for (ConstraintViolation<T> violation : violations) {
                if (errorMessage.length() > 0) {
                    errorMessage.append("; ");
                }
                errorMessage.append(violation.getPropertyPath())
                          .append(": ")
                          .append(violation.getMessage());
            }
            throw ValidationException.of("VALIDATION_FAILED", errorMessage.toString());
        }
    }

    /**
     * 验证对象属性
     * 
     * @param object 待验证对象
     * @param propertyName 属性名
     * @param groups 验证组
     * @param <T> 对象类型
     * @throws ValidationException 验证失败时抛出
     */
    public static <T> void validateProperty(T object, String propertyName, Class<?>... groups) {
        if (object == null) {
            throw ValidationException.of("VALIDATION_FAILED", "验证对象不能为空");
        }

        Class<?>[] validationGroups = groups.length == 0 ? new Class<?>[]{Default.class} : groups;
        Set<ConstraintViolation<T>> violations = validator.validateProperty(object, propertyName, validationGroups);

        if (!violations.isEmpty()) {
            ConstraintViolation<T> violation = violations.iterator().next();
            throw ValidationException.of("VALIDATION_FAILED", 
                propertyName + ": " + violation.getMessage());
        }
    }

    /**
     * 获取验证器实例
     * 
     * @return Validator实例
     */
    public static Validator getValidator() {
        return validator;
    }

    /**
     * 检查验证结果
     * 
     * @param violations 违规集合
     * @param <T> 对象类型
     * @return 是否有违规
     */
    public static <T> boolean hasViolations(Set<ConstraintViolation<T>> violations) {
        return violations != null && !violations.isEmpty();
    }

    /**
     * 格式化验证错误信息
     * 
     * @param violations 违规集合
     * @param <T> 对象类型
     * @return 格式化的错误信息
     */
    public static <T> String formatViolations(Set<ConstraintViolation<T>> violations) {
        if (!hasViolations(violations)) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        for (ConstraintViolation<T> violation : violations) {
            if (sb.length() > 0) {
                sb.append("; ");
            }
            sb.append(violation.getPropertyPath())
              .append(": ")
              .append(violation.getMessage());
        }
        return sb.toString();
    }
}