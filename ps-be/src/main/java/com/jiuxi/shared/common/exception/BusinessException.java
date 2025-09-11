package com.jiuxi.shared.common.exception;

import com.jiuxi.shared.common.enums.ResponseCodeEnum;

/**
 * 业务异常类
 * 用于处理业务逻辑相关的异常
 * 
 * @author System
 * @since 2.2.2-SNAPSHOT
 */
public class BusinessException extends BaseException {

    private static final long serialVersionUID = 1L;

    /**
     * 默认构造函数
     */
    public BusinessException() {
        super(ResponseCodeEnum.BUSINESS_ERROR);
    }

    /**
     * 构造函数
     */
    public BusinessException(String message) {
        super(ResponseCodeEnum.BUSINESS_ERROR.getCode(), message);
    }

    /**
     * 构造函数
     */
    public BusinessException(String message, Object data) {
        super(ResponseCodeEnum.BUSINESS_ERROR.getCode(), message, data);
    }

    /**
     * 构造函数
     */
    public BusinessException(String message, Throwable cause) {
        super(ResponseCodeEnum.BUSINESS_ERROR.getCode(), message, cause);
    }

    /**
     * 构造函数
     */
    public BusinessException(String message, Object data, Throwable cause) {
        super(ResponseCodeEnum.BUSINESS_ERROR.getCode(), message, data, cause);
    }

    /**
     * 使用响应码枚举构造异常
     */
    public BusinessException(ResponseCodeEnum responseCode) {
        super(responseCode);
    }

    /**
     * 使用响应码枚举构造异常
     */
    public BusinessException(ResponseCodeEnum responseCode, Object data) {
        super(responseCode, data);
    }

    /**
     * 使用响应码枚举构造异常
     */
    public BusinessException(ResponseCodeEnum responseCode, Throwable cause) {
        super(responseCode, cause);
    }

    /**
     * 使用响应码枚举构造异常
     */
    public BusinessException(ResponseCodeEnum responseCode, Object data, Throwable cause) {
        super(responseCode, data, cause);
    }

    /**
     * 静态工厂方法
     */
    public static BusinessException of(String message) {
        return new BusinessException(message);
    }

    /**
     * 静态工厂方法
     */
    public static BusinessException of(String message, Object data) {
        return new BusinessException(message, data);
    }

    /**
     * 静态工厂方法
     */
    public static BusinessException of(ResponseCodeEnum responseCode) {
        return new BusinessException(responseCode);
    }

    /**
     * 静态工厂方法
     */
    public static BusinessException of(ResponseCodeEnum responseCode, Object data) {
        return new BusinessException(responseCode, data);
    }

    /**
     * 数据不存在异常
     */
    public static BusinessException dataNotFound() {
        return new BusinessException(ResponseCodeEnum.DATA_NOT_FOUND);
    }

    /**
     * 数据不存在异常
     */
    public static BusinessException dataNotFound(String message) {
        return new BusinessException(ResponseCodeEnum.DATA_NOT_FOUND.getCode(), message);
    }

    /**
     * 数据重复异常
     */
    public static BusinessException dataDuplicate() {
        return new BusinessException(ResponseCodeEnum.DATA_DUPLICATE);
    }

    /**
     * 数据重复异常
     */
    public static BusinessException dataDuplicate(String message) {
        return new BusinessException(ResponseCodeEnum.DATA_DUPLICATE.getCode(), message);
    }

    /**
     * 数据状态异常
     */
    public static BusinessException dataStatusError() {
        return new BusinessException(ResponseCodeEnum.DATA_STATUS_ERROR);
    }

    /**
     * 数据状态异常
     */
    public static BusinessException dataStatusError(String message) {
        return new BusinessException(ResponseCodeEnum.DATA_STATUS_ERROR.getCode(), message);
    }

    /**
     * 操作不被允许异常
     */
    public static BusinessException operationNotAllowed() {
        return new BusinessException(ResponseCodeEnum.OPERATION_NOT_ALLOWED);
    }

    /**
     * 操作不被允许异常
     */
    public static BusinessException operationNotAllowed(String message) {
        return new BusinessException(ResponseCodeEnum.OPERATION_NOT_ALLOWED.getCode(), message);
    }

    /**
     * 业务规则违反异常
     */
    public static BusinessException businessRuleViolation() {
        return new BusinessException(ResponseCodeEnum.BUSINESS_RULE_VIOLATION);
    }

    /**
     * 业务规则违反异常
     */
    public static BusinessException businessRuleViolation(String message) {
        return new BusinessException(ResponseCodeEnum.BUSINESS_RULE_VIOLATION.getCode(), message);
    }

    /**
     * 资源被锁定异常
     */
    public static BusinessException resourceLocked() {
        return new BusinessException(ResponseCodeEnum.RESOURCE_LOCKED);
    }

    /**
     * 资源被锁定异常
     */
    public static BusinessException resourceLocked(String message) {
        return new BusinessException(ResponseCodeEnum.RESOURCE_LOCKED.getCode(), message);
    }
}