package com.jiuxi.common.util;

import com.jiuxi.common.bean.ErrorCode;
import com.jiuxi.common.exception.ValidationException;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * @ClassName: ValidationUtils
 * @Description: 参数验证工具类
 * @Author: Qdd
 * @Date: 2025/01/07
 * @Copyright: 2025 www.jiuxi.com Inc. All rights reserved.
 */
public class ValidationUtils {

    private static final ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
    private static final Validator validator = validatorFactory.getValidator();

    // 常用正则表达式
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$"
    );
    
    private static final Pattern PHONE_PATTERN = Pattern.compile(
            "^1[3-9]\\d{9}$"
    );
    
    private static final Pattern ID_CARD_PATTERN = Pattern.compile(
            "^[1-9]\\d{5}(18|19|20)\\d{2}((0[1-9])|(1[0-2]))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$"
    );
    
    private static final Pattern PASSWORD_PATTERN = Pattern.compile(
            "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d@$!%*?&]{8,}$"
    );

    /**
     * 验证对象
     *
     * @param object 待验证对象
     * @param groups 验证组
     * @param <T>    对象类型
     * @throws ValidationException 验证失败时抛出
     */
    public static <T> void validate(T object, Class<?>... groups) {
        Set<ConstraintViolation<T>> violations = validator.validate(object, groups);
        if (!violations.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (ConstraintViolation<T> violation : violations) {
                sb.append(violation.getPropertyPath()).append(": ").append(violation.getMessage()).append("; ");
            }
            throw ValidationException.of(ErrorCode.VALIDATION_FAILED, sb.toString().trim());
        }
    }

    /**
     * 验证对象属性
     *
     * @param object       待验证对象
     * @param propertyName 属性名
     * @param groups       验证组
     * @param <T>          对象类型
     * @throws ValidationException 验证失败时抛出
     */
    public static <T> void validateProperty(T object, String propertyName, Class<?>... groups) {
        Set<ConstraintViolation<T>> violations = validator.validateProperty(object, propertyName, groups);
        if (!violations.isEmpty()) {
            ConstraintViolation<T> violation = violations.iterator().next();
            throw ValidationException.of(ErrorCode.VALIDATION_FAILED, violation.getMessage(), propertyName);
        }
    }

    /**
     * 验证属性值
     *
     * @param beanType     Bean类型
     * @param propertyName 属性名
     * @param value        属性值
     * @param groups       验证组
     * @param <T>          Bean类型
     * @throws ValidationException 验证失败时抛出
     */
    public static <T> void validateValue(Class<T> beanType, String propertyName, Object value, Class<?>... groups) {
        Set<ConstraintViolation<T>> violations = validator.validateValue(beanType, propertyName, value, groups);
        if (!violations.isEmpty()) {
            ConstraintViolation<T> violation = violations.iterator().next();
            throw ValidationException.of(ErrorCode.VALIDATION_FAILED, violation.getMessage(), propertyName);
        }
    }

    /**
     * 检查字符串是否为空或null
     *
     * @param value 待检查的值
     * @param name  字段名
     * @throws ValidationException 验证失败时抛出
     */
    public static void requireNonEmpty(String value, String name) {
        if (value == null || value.trim().isEmpty()) {
            throw ValidationException.of(ErrorCode.PARAM_ERROR, name + "不能为空", name);
        }
    }

    /**
     * 检查对象是否为null
     *
     * @param value 待检查的值
     * @param name  字段名
     * @throws ValidationException 验证失败时抛出
     */
    public static void requireNonNull(Object value, String name) {
        if (value == null) {
            throw ValidationException.of(ErrorCode.PARAM_ERROR, name + "不能为空", name);
        }
    }

    /**
     * 检查数值范围
     *
     * @param value 待检查的值
     * @param min   最小值
     * @param max   最大值
     * @param name  字段名
     * @throws ValidationException 验证失败时抛出
     */
    public static void requireRange(Number value, Number min, Number max, String name) {
        if (value == null) {
            throw ValidationException.of(ErrorCode.PARAM_ERROR, name + "不能为空", name);
        }
        double val = value.doubleValue();
        double minVal = min.doubleValue();
        double maxVal = max.doubleValue();
        if (val < minVal || val > maxVal) {
            throw ValidationException.of(ErrorCode.PARAM_ERROR, 
                    name + "必须在" + min + "和" + max + "之间", name);
        }
    }

    /**
     * 检查字符串长度
     *
     * @param value     待检查的值
     * @param minLength 最小长度
     * @param maxLength 最大长度
     * @param name      字段名
     * @throws ValidationException 验证失败时抛出
     */
    public static void requireLength(String value, int minLength, int maxLength, String name) {
        if (value == null) {
            throw ValidationException.of(ErrorCode.PARAM_ERROR, name + "不能为空", name);
        }
        int length = value.length();
        if (length < minLength || length > maxLength) {
            throw ValidationException.of(ErrorCode.PARAM_ERROR, 
                    name + "长度必须在" + minLength + "和" + maxLength + "之间", name);
        }
    }

    /**
     * 验证邮箱格式
     *
     * @param email 邮箱地址
     * @param name  字段名
     * @throws ValidationException 验证失败时抛出
     */
    public static void requireValidEmail(String email, String name) {
        requireNonEmpty(email, name);
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw ValidationException.of(ErrorCode.PARAM_ERROR, name + "格式不正确", name);
        }
    }

    /**
     * 验证手机号格式
     *
     * @param phone 手机号
     * @param name  字段名
     * @throws ValidationException 验证失败时抛出
     */
    public static void requireValidPhone(String phone, String name) {
        requireNonEmpty(phone, name);
        if (!PHONE_PATTERN.matcher(phone).matches()) {
            throw ValidationException.of(ErrorCode.PARAM_ERROR, name + "格式不正确", name);
        }
    }

    /**
     * 验证身份证号格式
     *
     * @param idCard 身份证号
     * @param name   字段名
     * @throws ValidationException 验证失败时抛出
     */
    public static void requireValidIdCard(String idCard, String name) {
        requireNonEmpty(idCard, name);
        if (!ID_CARD_PATTERN.matcher(idCard).matches()) {
            throw ValidationException.of(ErrorCode.PARAM_ERROR, name + "格式不正确", name);
        }
    }

    /**
     * 验证密码强度
     *
     * @param password 密码
     * @param name     字段名
     * @throws ValidationException 验证失败时抛出
     */
    public static void requireStrongPassword(String password, String name) {
        requireNonEmpty(password, name);
        if (!PASSWORD_PATTERN.matcher(password).matches()) {
            throw ValidationException.of(ErrorCode.PARAM_ERROR, 
                    name + "必须包含大小写字母和数字，且长度不少于8位", name);
        }
    }

    /**
     * 验证正则表达式
     *
     * @param value   待验证的值
     * @param pattern 正则表达式
     * @param name    字段名
     * @param message 错误消息
     * @throws ValidationException 验证失败时抛出
     */
    public static void requirePattern(String value, Pattern pattern, String name, String message) {
        requireNonEmpty(value, name);
        if (!pattern.matcher(value).matches()) {
            throw ValidationException.of(ErrorCode.PARAM_ERROR, message, name);
        }
    }

    /**
     * 验证正则表达式
     *
     * @param value   待验证的值
     * @param regex   正则表达式字符串
     * @param name    字段名
     * @param message 错误消息
     * @throws ValidationException 验证失败时抛出
     */
    public static void requirePattern(String value, String regex, String name, String message) {
        requirePattern(value, Pattern.compile(regex), name, message);
    }

    /**
     * 检查是否为正整数
     *
     * @param value 待检查的值
     * @param name  字段名
     * @throws ValidationException 验证失败时抛出
     */
    public static void requirePositive(Number value, String name) {
        requireNonNull(value, name);
        if (value.doubleValue() <= 0) {
            throw ValidationException.of(ErrorCode.PARAM_ERROR, name + "必须为正数", name);
        }
    }

    /**
     * 检查是否为非负数
     *
     * @param value 待检查的值
     * @param name  字段名
     * @throws ValidationException 验证失败时抛出
     */
    public static void requireNonNegative(Number value, String name) {
        requireNonNull(value, name);
        if (value.doubleValue() < 0) {
            throw ValidationException.of(ErrorCode.PARAM_ERROR, name + "不能为负数", name);
        }
    }

    /**
     * 获取验证器实例
     *
     * @return Validator
     */
    public static Validator getValidator() {
        return validator;
    }
}