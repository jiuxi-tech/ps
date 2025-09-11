package com.jiuxi.shared.common.validation;

import com.jiuxi.shared.common.exception.ValidationException;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.validation.ConstraintViolation;
import javax.validation.groups.Default;
import java.util.Collection;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * 验证工具类
 * 
 * 提供常用的验证方法和便捷的验证操作
 * 
 * @author System
 * @since 2.2.2-SNAPSHOT
 */
public class ValidationUtils {

    // 常用正则表达式
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$"
    );
    
    private static final Pattern PHONE_PATTERN = Pattern.compile("^1[3-9]\\d{9}$");
    
    private static final Pattern ID_CARD_PATTERN = Pattern.compile(
        "^[1-9]\\d{5}(18|19|20)\\d{2}((0[1-9])|(1[0-2]))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$"
    );

    /**
     * 验证对象
     * 
     * @param object 待验证对象
     * @param groups 验证组
     * @param <T> 对象类型
     * @throws ValidationException 验证失败时抛出
     */
    public static <T> void validate(T object, Class<?>... groups) {
        SimpleValidationFramework.validate(object, groups);
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
        SimpleValidationFramework.validateProperty(object, propertyName, groups);
    }

    /**
     * 检查验证结果
     * 
     * @param violations 违规集合
     * @param <T> 对象类型
     * @throws ValidationException 存在违规时抛出
     */
    public static <T> void checkViolations(Set<ConstraintViolation<T>> violations) {
        if (SimpleValidationFramework.hasViolations(violations)) {
            String errorMessage = SimpleValidationFramework.formatViolations(violations);
            throw ValidationException.of("VALIDATION_FAILED", errorMessage);
        }
    }

    /**
     * 要求非空
     * 
     * @param value 值
     * @param fieldName 字段名
     * @throws ValidationException 为空时抛出
     */
    public static void requireNonNull(Object value, String fieldName) {
        if (value == null) {
            throw ValidationException.of("PARAM_ERROR", fieldName + "不能为空");
        }
    }

    /**
     * 要求字符串非空
     * 
     * @param value 值
     * @param fieldName 字段名
     * @throws ValidationException 为空时抛出
     */
    public static void requireNonEmpty(String value, String fieldName) {
        if (!StringUtils.hasText(value)) {
            throw ValidationException.of("PARAM_ERROR", fieldName + "不能为空");
        }
    }

    /**
     * 要求集合非空
     * 
     * @param collection 集合
     * @param fieldName 字段名
     * @throws ValidationException 为空时抛出
     */
    public static void requireNonEmpty(Collection<?> collection, String fieldName) {
        if (CollectionUtils.isEmpty(collection)) {
            throw ValidationException.of("PARAM_ERROR", fieldName + "不能为空");
        }
    }

    /**
     * 要求字符串长度在范围内
     * 
     * @param value 值
     * @param minLength 最小长度
     * @param maxLength 最大长度
     * @param fieldName 字段名
     * @throws ValidationException 不在范围内时抛出
     */
    public static void requireLength(String value, int minLength, int maxLength, String fieldName) {
        requireNonEmpty(value, fieldName);
        int length = value.length();
        if (length < minLength || length > maxLength) {
            throw ValidationException.of("PARAM_ERROR", 
                fieldName + "长度必须在" + minLength + "和" + maxLength + "之间");
        }
    }

    /**
     * 要求数值在范围内
     * 
     * @param value 值
     * @param min 最小值
     * @param max 最大值
     * @param fieldName 字段名
     * @throws ValidationException 不在范围内时抛出
     */
    public static void requireRange(Number value, Number min, Number max, String fieldName) {
        requireNonNull(value, fieldName);
        double val = value.doubleValue();
        double minVal = min.doubleValue();
        double maxVal = max.doubleValue();
        
        if (val < minVal || val > maxVal) {
            throw ValidationException.of("PARAM_ERROR", 
                fieldName + "必须在" + min + "和" + max + "之间");
        }
    }

    /**
     * 要求正数
     * 
     * @param value 值
     * @param fieldName 字段名
     * @throws ValidationException 不是正数时抛出
     */
    public static void requirePositive(Number value, String fieldName) {
        requireNonNull(value, fieldName);
        if (value.doubleValue() <= 0) {
            throw ValidationException.of("PARAM_ERROR", fieldName + "必须为正数");
        }
    }

    /**
     * 要求非负数
     * 
     * @param value 值
     * @param fieldName 字段名
     * @throws ValidationException 是负数时抛出
     */
    public static void requireNonNegative(Number value, String fieldName) {
        requireNonNull(value, fieldName);
        if (value.doubleValue() < 0) {
            throw ValidationException.of("PARAM_ERROR", fieldName + "不能为负数");
        }
    }

    /**
     * 验证邮箱格式
     * 
     * @param email 邮箱
     * @param fieldName 字段名
     * @throws ValidationException 格式不正确时抛出
     */
    public static void requireValidEmail(String email, String fieldName) {
        requireNonEmpty(email, fieldName);
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw ValidationException.of("PARAM_ERROR", fieldName + "格式不正确");
        }
    }

    /**
     * 验证手机号格式
     * 
     * @param phone 手机号
     * @param fieldName 字段名
     * @throws ValidationException 格式不正确时抛出
     */
    public static void requireValidPhone(String phone, String fieldName) {
        requireNonEmpty(phone, fieldName);
        if (!PHONE_PATTERN.matcher(phone).matches()) {
            throw ValidationException.of("PARAM_ERROR", fieldName + "格式不正确");
        }
    }

    /**
     * 验证身份证号格式
     * 
     * @param idCard 身份证号
     * @param fieldName 字段名
     * @throws ValidationException 格式不正确时抛出
     */
    public static void requireValidIdCard(String idCard, String fieldName) {
        requireNonEmpty(idCard, fieldName);
        if (!ID_CARD_PATTERN.matcher(idCard).matches()) {
            throw ValidationException.of("PARAM_ERROR", fieldName + "格式不正确");
        }
    }

    /**
     * 验证正则表达式
     * 
     * @param value 值
     * @param pattern 正则模式
     * @param fieldName 字段名
     * @param errorMessage 错误消息
     * @throws ValidationException 不匹配时抛出
     */
    public static void requirePattern(String value, Pattern pattern, String fieldName, String errorMessage) {
        requireNonEmpty(value, fieldName);
        if (!pattern.matcher(value).matches()) {
            throw ValidationException.of("PARAM_ERROR", errorMessage);
        }
    }

    /**
     * 验证正则表达式
     * 
     * @param value 值
     * @param regex 正则表达式
     * @param fieldName 字段名
     * @param errorMessage 错误消息
     * @throws ValidationException 不匹配时抛出
     */
    public static void requirePattern(String value, String regex, String fieldName, String errorMessage) {
        requirePattern(value, Pattern.compile(regex), fieldName, errorMessage);
    }

    /**
     * 条件验证
     * 
     * @param condition 条件
     * @param errorMessage 错误消息
     * @throws ValidationException 条件不满足时抛出
     */
    public static void requireTrue(boolean condition, String errorMessage) {
        if (!condition) {
            throw ValidationException.of("VALIDATION_FAILED", errorMessage);
        }
    }

    /**
     * 条件验证
     * 
     * @param condition 条件
     * @param errorCode 错误码
     * @param errorMessage 错误消息
     * @throws ValidationException 条件不满足时抛出
     */
    public static void requireTrue(boolean condition, String errorCode, String errorMessage) {
        if (!condition) {
            throw ValidationException.of(errorCode, errorMessage);
        }
    }
}