package com.jiuxi.common.exception;

import com.jiuxi.common.bean.ErrorCode;

/**
 * @ClassName: BusinessException
 * @Description: 业务异常类
 * @Author: System
 * @Date: 2025/01/07
 * @Copyright: 2025 www.jiuxi.com Inc. All rights reserved.
 * 
 * @deprecated 请使用 {@link com.jiuxi.shared.common.exception.BusinessException} 替代
 */
@Deprecated
public class BusinessException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * 错误码
     */
    private int code;

    /**
     * 错误消息
     */
    private String message;

    public BusinessException() {
        super();
    }

    public BusinessException(String message) {
        super(message);
        this.code = ErrorCode.ERROR.getCode();
        this.message = message;
    }

    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMsg());
        this.code = errorCode.getCode();
        this.message = errorCode.getMsg();
    }

    public BusinessException(ErrorCode errorCode, String message) {
        super(message);
        this.code = errorCode.getCode();
        this.message = message;
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
        this.code = ErrorCode.ERROR.getCode();
        this.message = message;
    }

    public BusinessException(int code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
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

    /**
     * 快速创建业务异常
     *
     * @param message 错误消息
     * @return BusinessException
     */
    public static BusinessException of(String message) {
        return new BusinessException(message);
    }

    /**
     * 快速创建业务异常
     *
     * @param code    错误码
     * @param message 错误消息
     * @return BusinessException
     */
    public static BusinessException of(int code, String message) {
        return new BusinessException(code, message);
    }

    /**
     * 快速创建业务异常
     *
     * @param errorCode 错误码枚举
     * @return BusinessException
     */
    public static BusinessException of(ErrorCode errorCode) {
        return new BusinessException(errorCode);
    }

    /**
     * 快速创建业务异常
     *
     * @param errorCode 错误码枚举
     * @param message   自定义错误消息
     * @return BusinessException
     */
    public static BusinessException of(ErrorCode errorCode, String message) {
        return new BusinessException(errorCode, message);
    }
}