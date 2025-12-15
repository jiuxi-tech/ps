package com.jiuxi.common.exception;

import com.jiuxi.common.bean.ErrorCode;

/**
 * @ClassName: ValidationException
 * @Description: 参数验证异常类
 * @Author: Qdd
 * @Date: 2025/01/07
 * @Copyright: 2025 www.jiuxi.com Inc. All rights reserved.
 * 
 * @deprecated 请使用 {@link com.jiuxi.shared.common.exception.ValidationException} 替代
 */
@Deprecated
public class ValidationException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * 错误码
     */
    private int code;

    /**
     * 错误消息
     */
    private String message;

    /**
     * 验证失败的字段名
     */
    private String field;

    public ValidationException() {
        super();
        this.code = ErrorCode.PARAM_ERROR.getCode();
    }

    public ValidationException(String message) {
        super(message);
        this.code = ErrorCode.PARAM_ERROR.getCode();
        this.message = message;
    }

    public ValidationException(String field, String message) {
        super(message);
        this.code = ErrorCode.PARAM_ERROR.getCode();
        this.field = field;
        this.message = message;
    }

    public ValidationException(int code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public ValidationException(String message, Throwable cause) {
        super(message, cause);
        this.code = ErrorCode.PARAM_ERROR.getCode();
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    /**
     * 快速创建参数验证异常
     *
     * @param message 错误消息
     * @return ValidationException
     */
    public static ValidationException of(String message) {
        return new ValidationException(message);
    }

    /**
     * 快速创建参数验证异常
     *
     * @param field   字段名
     * @param message 错误消息
     * @return ValidationException
     */
    public static ValidationException of(String field, String message) {
        return new ValidationException(field, message);
    }

    /**
     * 快速创建参数验证异常
     *
     * @param errorCode 错误码
     * @param message   错误消息
     * @param field     字段名
     * @return ValidationException
     */
    public static ValidationException of(ErrorCode errorCode, String message, String field) {
        ValidationException exception = new ValidationException(field, message);
        exception.setCode(errorCode.getCode());
        return exception;
    }

    /**
     * 快速创建参数验证异常
     *
     * @param errorCode 错误码
     * @param message   错误消息
     * @return ValidationException
     */
    public static ValidationException of(ErrorCode errorCode, String message) {
        ValidationException exception = new ValidationException(message);
        exception.setCode(errorCode.getCode());
        return exception;
    }
}