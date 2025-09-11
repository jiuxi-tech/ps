package com.jiuxi.shared.common.exception;

import com.jiuxi.shared.common.enums.ResponseCodeEnum;

/**
 * 安全异常类
 * 用于处理安全相关的异常
 * 
 * @author System
 * @since 2.2.2-SNAPSHOT
 */
public class SecurityException extends BaseException {

    private static final long serialVersionUID = 1L;

    /**
     * 默认构造函数
     */
    public SecurityException() {
        super(ResponseCodeEnum.FORBIDDEN);
    }

    /**
     * 构造函数
     */
    public SecurityException(String message) {
        super(ResponseCodeEnum.FORBIDDEN.getCode(), message);
    }

    /**
     * 构造函数
     */
    public SecurityException(String message, Object data) {
        super(ResponseCodeEnum.FORBIDDEN.getCode(), message, data);
    }

    /**
     * 构造函数
     */
    public SecurityException(String message, Throwable cause) {
        super(ResponseCodeEnum.FORBIDDEN.getCode(), message, cause);
    }

    /**
     * 构造函数
     */
    public SecurityException(String message, Object data, Throwable cause) {
        super(ResponseCodeEnum.FORBIDDEN.getCode(), message, data, cause);
    }

    /**
     * 使用响应码枚举构造异常
     */
    public SecurityException(ResponseCodeEnum responseCode) {
        super(responseCode);
    }

    /**
     * 使用响应码枚举构造异常
     */
    public SecurityException(ResponseCodeEnum responseCode, Object data) {
        super(responseCode, data);
    }

    /**
     * 使用响应码枚举构造异常
     */
    public SecurityException(ResponseCodeEnum responseCode, Throwable cause) {
        super(responseCode, cause);
    }

    /**
     * 使用响应码枚举构造异常
     */
    public SecurityException(ResponseCodeEnum responseCode, Object data, Throwable cause) {
        super(responseCode, data, cause);
    }

    /**
     * 静态工厂方法
     */
    public static SecurityException of(String message) {
        return new SecurityException(message);
    }

    /**
     * 静态工厂方法
     */
    public static SecurityException of(String message, Object data) {
        return new SecurityException(message, data);
    }

    /**
     * 静态工厂方法
     */
    public static SecurityException of(ResponseCodeEnum responseCode) {
        return new SecurityException(responseCode);
    }

    /**
     * 静态工厂方法
     */
    public static SecurityException of(ResponseCodeEnum responseCode, Object data) {
        return new SecurityException(responseCode, data);
    }

    /**
     * 认证失败异常
     */
    public static SecurityException authFailed() {
        return new SecurityException(ResponseCodeEnum.AUTH_FAILED);
    }

    /**
     * 认证失败异常
     */
    public static SecurityException authFailed(String message) {
        return new SecurityException(ResponseCodeEnum.AUTH_FAILED.getCode(), message);
    }

    /**
     * 未授权访问异常
     */
    public static SecurityException unauthorized() {
        return new SecurityException(ResponseCodeEnum.UNAUTHORIZED);
    }

    /**
     * 未授权访问异常
     */
    public static SecurityException unauthorized(String message) {
        return new SecurityException(ResponseCodeEnum.UNAUTHORIZED.getCode(), message);
    }

    /**
     * 权限不足异常
     */
    public static SecurityException forbidden() {
        return new SecurityException(ResponseCodeEnum.FORBIDDEN);
    }

    /**
     * 权限不足异常
     */
    public static SecurityException forbidden(String message) {
        return new SecurityException(ResponseCodeEnum.FORBIDDEN.getCode(), message);
    }

    /**
     * 权限被拒绝异常
     */
    public static SecurityException permissionDenied() {
        return new SecurityException(ResponseCodeEnum.PERMISSION_DENIED);
    }

    /**
     * 权限被拒绝异常
     */
    public static SecurityException permissionDenied(String message) {
        return new SecurityException(ResponseCodeEnum.PERMISSION_DENIED.getCode(), message);
    }

    /**
     * 令牌无效异常
     */
    public static SecurityException tokenInvalid() {
        return new SecurityException(ResponseCodeEnum.TOKEN_INVALID);
    }

    /**
     * 令牌无效异常
     */
    public static SecurityException tokenInvalid(String message) {
        return new SecurityException(ResponseCodeEnum.TOKEN_INVALID.getCode(), message);
    }

    /**
     * 令牌过期异常
     */
    public static SecurityException tokenExpired() {
        return new SecurityException(ResponseCodeEnum.TOKEN_EXPIRED);
    }

    /**
     * 令牌过期异常
     */
    public static SecurityException tokenExpired(String message) {
        return new SecurityException(ResponseCodeEnum.TOKEN_EXPIRED.getCode(), message);
    }

    /**
     * 账户被锁定异常
     */
    public static SecurityException accountLocked() {
        return new SecurityException(ResponseCodeEnum.ACCOUNT_LOCKED);
    }

    /**
     * 账户被锁定异常
     */
    public static SecurityException accountLocked(String message) {
        return new SecurityException(ResponseCodeEnum.ACCOUNT_LOCKED.getCode(), message);
    }

    /**
     * 账户被禁用异常
     */
    public static SecurityException accountDisabled() {
        return new SecurityException(ResponseCodeEnum.ACCOUNT_DISABLED);
    }

    /**
     * 账户被禁用异常
     */
    public static SecurityException accountDisabled(String message) {
        return new SecurityException(ResponseCodeEnum.ACCOUNT_DISABLED.getCode(), message);
    }

    /**
     * 密码已过期异常
     */
    public static SecurityException passwordExpired() {
        return new SecurityException(ResponseCodeEnum.PASSWORD_EXPIRED);
    }

    /**
     * 密码已过期异常
     */
    public static SecurityException passwordExpired(String message) {
        return new SecurityException(ResponseCodeEnum.PASSWORD_EXPIRED.getCode(), message);
    }

    /**
     * 验证码错误异常
     */
    public static SecurityException captchaError() {
        return new SecurityException(ResponseCodeEnum.CAPTCHA_ERROR);
    }

    /**
     * 验证码错误异常
     */
    public static SecurityException captchaError(String message) {
        return new SecurityException(ResponseCodeEnum.CAPTCHA_ERROR.getCode(), message);
    }

    /**
     * 需要登录异常
     */
    public static SecurityException loginRequired() {
        return new SecurityException(ResponseCodeEnum.LOGIN_REQUIRED);
    }

    /**
     * 需要登录异常
     */
    public static SecurityException loginRequired(String message) {
        return new SecurityException(ResponseCodeEnum.LOGIN_REQUIRED.getCode(), message);
    }
}