package com.jiuxi.shared.common.exception;

import com.jiuxi.shared.common.enums.ResponseCodeEnum;

import java.util.List;
import java.util.Map;

/**
 * 验证异常类
 * 用于处理数据验证相关的异常
 * 
 * @author System
 * @since 2.2.2-SNAPSHOT
 */
public class ValidationException extends BaseException {

    private static final long serialVersionUID = 1L;

    /**
     * 验证错误详情
     */
    private Map<String, List<String>> validationErrors;

    /**
     * 默认构造函数
     */
    public ValidationException() {
        super(ResponseCodeEnum.VALIDATION_FAILED);
    }

    /**
     * 构造函数
     */
    public ValidationException(String message) {
        super(ResponseCodeEnum.VALIDATION_FAILED.getCode(), message);
    }

    /**
     * 构造函数
     */
    public ValidationException(String message, Map<String, List<String>> validationErrors) {
        super(ResponseCodeEnum.VALIDATION_FAILED.getCode(), message, validationErrors);
        this.validationErrors = validationErrors;
    }

    /**
     * 构造函数
     */
    public ValidationException(String message, Throwable cause) {
        super(ResponseCodeEnum.VALIDATION_FAILED.getCode(), message, cause);
    }

    /**
     * 构造函数
     */
    public ValidationException(String message, Map<String, List<String>> validationErrors, Throwable cause) {
        super(ResponseCodeEnum.VALIDATION_FAILED.getCode(), message, validationErrors, cause);
        this.validationErrors = validationErrors;
    }

    /**
     * 使用响应码枚举构造异常
     */
    public ValidationException(ResponseCodeEnum responseCode) {
        super(responseCode);
    }

    /**
     * 使用响应码枚举构造异常
     */
    public ValidationException(ResponseCodeEnum responseCode, Map<String, List<String>> validationErrors) {
        super(responseCode, validationErrors);
        this.validationErrors = validationErrors;
    }

    /**
     * 使用响应码枚举构造异常
     */
    public ValidationException(ResponseCodeEnum responseCode, Throwable cause) {
        super(responseCode, cause);
    }

    /**
     * 使用响应码枚举构造异常
     */
    public ValidationException(ResponseCodeEnum responseCode, Map<String, List<String>> validationErrors, Throwable cause) {
        super(responseCode, validationErrors, cause);
        this.validationErrors = validationErrors;
    }

    /**
     * 获取验证错误详情
     */
    public Map<String, List<String>> getValidationErrors() {
        return validationErrors;
    }

    /**
     * 设置验证错误详情
     */
    public void setValidationErrors(Map<String, List<String>> validationErrors) {
        this.validationErrors = validationErrors;
        this.setData(validationErrors);
    }

    /**
     * 静态工厂方法
     */
    public static ValidationException of(String message) {
        return new ValidationException(message);
    }

    /**
     * 静态工厂方法
     */
    public static ValidationException of(String message, Map<String, List<String>> validationErrors) {
        return new ValidationException(message, validationErrors);
    }

    /**
     * 静态工厂方法
     */
    public static ValidationException of(ResponseCodeEnum responseCode) {
        return new ValidationException(responseCode);
    }

    /**
     * 静态工厂方法
     */
    public static ValidationException of(ResponseCodeEnum responseCode, Map<String, List<String>> validationErrors) {
        return new ValidationException(responseCode, validationErrors);
    }

    /**
     * 参数错误异常
     */
    public static ValidationException paramError() {
        return new ValidationException(ResponseCodeEnum.BAD_REQUEST);
    }

    /**
     * 参数错误异常
     */
    public static ValidationException paramError(String message) {
        return new ValidationException(ResponseCodeEnum.BAD_REQUEST);
    }

    /**
     * 参数错误异常
     */
    public static ValidationException paramError(String message, Map<String, List<String>> validationErrors) {
        return new ValidationException(message, validationErrors);
    }

    /**
     * 必填参数缺失异常
     */
    public static ValidationException requiredParamMissing(String paramName) {
        return new ValidationException(String.format("必填参数 [%s] 不能为空", paramName));
    }

    /**
     * 参数格式错误异常
     */
    public static ValidationException paramFormatError(String paramName, String expectedFormat) {
        return new ValidationException(String.format("参数 [%s] 格式错误，期望格式：%s", paramName, expectedFormat));
    }

    /**
     * 参数值超出范围异常
     */
    public static ValidationException paramOutOfRange(String paramName, String range) {
        return new ValidationException(String.format("参数 [%s] 值超出范围，有效范围：%s", paramName, range));
    }

    /**
     * 参数长度错误异常
     */
    public static ValidationException paramLengthError(String paramName, int minLength, int maxLength) {
        return new ValidationException(String.format("参数 [%s] 长度错误，有效长度：%d-%d", paramName, minLength, maxLength));
    }

    /**
     * 判断是否有验证错误详情
     */
    public boolean hasValidationErrors() {
        return validationErrors != null && !validationErrors.isEmpty();
    }

    /**
     * 获取第一个验证错误信息
     */
    public String getFirstValidationError() {
        if (hasValidationErrors()) {
            return validationErrors.values().stream()
                .flatMap(List::stream)
                .findFirst()
                .orElse(getMessage());
        }
        return getMessage();
    }
}