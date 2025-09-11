package com.jiuxi.shared.common.exception;

import com.jiuxi.shared.common.enums.ResponseCodeEnum;

/**
 * 系统异常类
 * 用于处理系统级别的异常
 * 
 * @author System
 * @since 2.2.2-SNAPSHOT
 */
public class SystemException extends BaseException {

    private static final long serialVersionUID = 1L;

    /**
     * 默认构造函数
     */
    public SystemException() {
        super(ResponseCodeEnum.INTERNAL_SERVER_ERROR);
    }

    /**
     * 构造函数
     */
    public SystemException(String message) {
        super(ResponseCodeEnum.INTERNAL_SERVER_ERROR.getCode(), message);
    }

    /**
     * 构造函数
     */
    public SystemException(String message, Object data) {
        super(ResponseCodeEnum.INTERNAL_SERVER_ERROR.getCode(), message, data);
    }

    /**
     * 构造函数
     */
    public SystemException(String message, Throwable cause) {
        super(ResponseCodeEnum.INTERNAL_SERVER_ERROR.getCode(), message, cause);
    }

    /**
     * 构造函数
     */
    public SystemException(String message, Object data, Throwable cause) {
        super(ResponseCodeEnum.INTERNAL_SERVER_ERROR.getCode(), message, data, cause);
    }

    /**
     * 使用响应码枚举构造异常
     */
    public SystemException(ResponseCodeEnum responseCode) {
        super(responseCode);
    }

    /**
     * 使用响应码枚举构造异常
     */
    public SystemException(ResponseCodeEnum responseCode, Object data) {
        super(responseCode, data);
    }

    /**
     * 使用响应码枚举构造异常
     */
    public SystemException(ResponseCodeEnum responseCode, Throwable cause) {
        super(responseCode, cause);
    }

    /**
     * 使用响应码枚举构造异常
     */
    public SystemException(ResponseCodeEnum responseCode, Object data, Throwable cause) {
        super(responseCode, data, cause);
    }

    /**
     * 静态工厂方法
     */
    public static SystemException of(String message) {
        return new SystemException(message);
    }

    /**
     * 静态工厂方法
     */
    public static SystemException of(String message, Object data) {
        return new SystemException(message, data);
    }

    /**
     * 静态工厂方法
     */
    public static SystemException of(ResponseCodeEnum responseCode) {
        return new SystemException(responseCode);
    }

    /**
     * 静态工厂方法
     */
    public static SystemException of(ResponseCodeEnum responseCode, Object data) {
        return new SystemException(responseCode, data);
    }

    /**
     * 数据库错误异常
     */
    public static SystemException databaseError() {
        return new SystemException(ResponseCodeEnum.DATABASE_ERROR);
    }

    /**
     * 数据库错误异常
     */
    public static SystemException databaseError(String message) {
        return new SystemException(ResponseCodeEnum.DATABASE_ERROR.getCode(), message);
    }

    /**
     * 数据库错误异常
     */
    public static SystemException databaseError(String message, Throwable cause) {
        return new SystemException(ResponseCodeEnum.DATABASE_ERROR.getCode(), message, cause);
    }

    /**
     * Redis错误异常
     */
    public static SystemException redisError() {
        return new SystemException(ResponseCodeEnum.REDIS_ERROR);
    }

    /**
     * Redis错误异常
     */
    public static SystemException redisError(String message) {
        return new SystemException(ResponseCodeEnum.REDIS_ERROR.getCode(), message);
    }

    /**
     * Redis错误异常
     */
    public static SystemException redisError(String message, Throwable cause) {
        return new SystemException(ResponseCodeEnum.REDIS_ERROR.getCode(), message, cause);
    }

    /**
     * 文件存储错误异常
     */
    public static SystemException fileStorageError() {
        return new SystemException(ResponseCodeEnum.FILE_STORAGE_ERROR);
    }

    /**
     * 文件存储错误异常
     */
    public static SystemException fileStorageError(String message) {
        return new SystemException(ResponseCodeEnum.FILE_STORAGE_ERROR.getCode(), message);
    }

    /**
     * 文件存储错误异常
     */
    public static SystemException fileStorageError(String message, Throwable cause) {
        return new SystemException(ResponseCodeEnum.FILE_STORAGE_ERROR.getCode(), message, cause);
    }

    /**
     * 配置错误异常
     */
    public static SystemException configError() {
        return new SystemException(ResponseCodeEnum.CONFIG_ERROR);
    }

    /**
     * 配置错误异常
     */
    public static SystemException configError(String message) {
        return new SystemException(ResponseCodeEnum.CONFIG_ERROR.getCode(), message);
    }

    /**
     * 网络错误异常
     */
    public static SystemException networkError() {
        return new SystemException(ResponseCodeEnum.NETWORK_ERROR);
    }

    /**
     * 网络错误异常
     */
    public static SystemException networkError(String message) {
        return new SystemException(ResponseCodeEnum.NETWORK_ERROR.getCode(), message);
    }

    /**
     * 网络错误异常
     */
    public static SystemException networkError(String message, Throwable cause) {
        return new SystemException(ResponseCodeEnum.NETWORK_ERROR.getCode(), message, cause);
    }

    /**
     * 第三方服务错误异常
     */
    public static SystemException thirdPartyError() {
        return new SystemException(ResponseCodeEnum.THIRD_PARTY_ERROR);
    }

    /**
     * 第三方服务错误异常
     */
    public static SystemException thirdPartyError(String message) {
        return new SystemException(ResponseCodeEnum.THIRD_PARTY_ERROR.getCode(), message);
    }

    /**
     * 第三方服务错误异常
     */
    public static SystemException thirdPartyError(String message, Throwable cause) {
        return new SystemException(ResponseCodeEnum.THIRD_PARTY_ERROR.getCode(), message, cause);
    }

    /**
     * Keycloak服务错误异常
     */
    public static SystemException keycloakError() {
        return new SystemException(ResponseCodeEnum.KEYCLOAK_ERROR);
    }

    /**
     * Keycloak服务错误异常
     */
    public static SystemException keycloakError(String message) {
        return new SystemException(ResponseCodeEnum.KEYCLOAK_ERROR.getCode(), message);
    }

    /**
     * Keycloak服务错误异常
     */
    public static SystemException keycloakError(String message, Throwable cause) {
        return new SystemException(ResponseCodeEnum.KEYCLOAK_ERROR.getCode(), message, cause);
    }

    /**
     * 系统过载异常
     */
    public static SystemException systemOverload() {
        return new SystemException(ResponseCodeEnum.SYSTEM_OVERLOAD);
    }

    /**
     * 系统过载异常
     */
    public static SystemException systemOverload(String message) {
        return new SystemException(ResponseCodeEnum.SYSTEM_OVERLOAD.getCode(), message);
    }

    /**
     * 系统维护异常
     */
    public static SystemException maintenanceMode() {
        return new SystemException(ResponseCodeEnum.MAINTENANCE_MODE);
    }

    /**
     * 系统维护异常
     */
    public static SystemException maintenanceMode(String message) {
        return new SystemException(ResponseCodeEnum.MAINTENANCE_MODE.getCode(), message);
    }
}