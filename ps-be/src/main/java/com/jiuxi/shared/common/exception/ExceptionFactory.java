package com.jiuxi.shared.common.exception;

import com.jiuxi.shared.common.enums.ResponseCodeEnum;

import java.util.List;
import java.util.Map;

/**
 * 异常工厂类
 * 提供统一的异常创建方法
 * 
 * @author System
 * @since 2.2.2-SNAPSHOT
 */
public final class ExceptionFactory {

    private ExceptionFactory() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    // ==================== 业务异常 ====================

    /**
     * 创建业务异常
     */
    public static BusinessException business(String message) {
        return BusinessException.of(message);
    }

    /**
     * 创建业务异常
     */
    public static BusinessException business(ResponseCodeEnum responseCode) {
        return BusinessException.of(responseCode);
    }

    /**
     * 创建业务异常
     */
    public static BusinessException business(ResponseCodeEnum responseCode, Object data) {
        return BusinessException.of(responseCode, data);
    }

    /**
     * 数据不存在异常
     */
    public static BusinessException dataNotFound() {
        return BusinessException.dataNotFound();
    }

    /**
     * 数据不存在异常
     */
    public static BusinessException dataNotFound(String entityName) {
        return BusinessException.dataNotFound(String.format("%s 不存在", entityName));
    }

    /**
     * 数据不存在异常
     */
    public static BusinessException dataNotFound(String entityName, Object id) {
        return BusinessException.dataNotFound(String.format("%s [%s] 不存在", entityName, id));
    }

    /**
     * 数据重复异常
     */
    public static BusinessException dataDuplicate() {
        return BusinessException.dataDuplicate();
    }

    /**
     * 数据重复异常
     */
    public static BusinessException dataDuplicate(String fieldName) {
        return BusinessException.dataDuplicate(String.format("%s 已存在", fieldName));
    }

    /**
     * 数据重复异常
     */
    public static BusinessException dataDuplicate(String fieldName, Object value) {
        return BusinessException.dataDuplicate(String.format("%s [%s] 已存在", fieldName, value));
    }

    /**
     * 数据状态异常
     */
    public static BusinessException dataStatusError() {
        return BusinessException.dataStatusError();
    }

    /**
     * 数据状态异常
     */
    public static BusinessException dataStatusError(String message) {
        return BusinessException.dataStatusError(message);
    }

    /**
     * 操作不被允许异常
     */
    public static BusinessException operationNotAllowed() {
        return BusinessException.operationNotAllowed();
    }

    /**
     * 操作不被允许异常
     */
    public static BusinessException operationNotAllowed(String operation) {
        return BusinessException.operationNotAllowed(String.format("操作 [%s] 不被允许", operation));
    }

    // ==================== 验证异常 ====================

    /**
     * 创建验证异常
     */
    public static ValidationException validation(String message) {
        return ValidationException.of(message);
    }

    /**
     * 创建验证异常
     */
    public static ValidationException validation(String message, Map<String, List<String>> validationErrors) {
        return ValidationException.of(message, validationErrors);
    }

    /**
     * 参数错误异常
     */
    public static ValidationException paramError() {
        return ValidationException.paramError();
    }

    /**
     * 参数错误异常
     */
    public static ValidationException paramError(String message) {
        return ValidationException.paramError(message);
    }

    /**
     * 必填参数缺失异常
     */
    public static ValidationException requiredParamMissing(String paramName) {
        return ValidationException.requiredParamMissing(paramName);
    }

    /**
     * 参数格式错误异常
     */
    public static ValidationException paramFormatError(String paramName, String expectedFormat) {
        return ValidationException.paramFormatError(paramName, expectedFormat);
    }

    /**
     * 参数值超出范围异常
     */
    public static ValidationException paramOutOfRange(String paramName, String range) {
        return ValidationException.paramOutOfRange(paramName, range);
    }

    /**
     * 参数长度错误异常
     */
    public static ValidationException paramLengthError(String paramName, int minLength, int maxLength) {
        return ValidationException.paramLengthError(paramName, minLength, maxLength);
    }

    // ==================== 安全异常 ====================

    /**
     * 创建安全异常
     */
    public static SecurityException security(String message) {
        return SecurityException.of(message);
    }

    /**
     * 创建安全异常
     */
    public static SecurityException security(ResponseCodeEnum responseCode) {
        return SecurityException.of(responseCode);
    }

    /**
     * 认证失败异常
     */
    public static SecurityException authFailed() {
        return SecurityException.authFailed();
    }

    /**
     * 认证失败异常
     */
    public static SecurityException authFailed(String message) {
        return SecurityException.authFailed(message);
    }

    /**
     * 未授权访问异常
     */
    public static SecurityException unauthorized() {
        return SecurityException.unauthorized();
    }

    /**
     * 权限不足异常
     */
    public static SecurityException forbidden() {
        return SecurityException.forbidden();
    }

    /**
     * 权限不足异常
     */
    public static SecurityException forbidden(String resource) {
        return SecurityException.forbidden(String.format("无权限访问资源 [%s]", resource));
    }

    /**
     * 令牌无效异常
     */
    public static SecurityException tokenInvalid() {
        return SecurityException.tokenInvalid();
    }

    /**
     * 令牌过期异常
     */
    public static SecurityException tokenExpired() {
        return SecurityException.tokenExpired();
    }

    /**
     * 账户被锁定异常
     */
    public static SecurityException accountLocked() {
        return SecurityException.accountLocked();
    }

    /**
     * 账户被禁用异常
     */
    public static SecurityException accountDisabled() {
        return SecurityException.accountDisabled();
    }

    /**
     * 验证码错误异常
     */
    public static SecurityException captchaError() {
        return SecurityException.captchaError();
    }

    /**
     * 需要登录异常
     */
    public static SecurityException loginRequired() {
        return SecurityException.loginRequired();
    }

    // ==================== 系统异常 ====================

    /**
     * 创建系统异常
     */
    public static SystemException system(String message) {
        return SystemException.of(message);
    }

    /**
     * 创建系统异常
     */
    public static SystemException system(String message, Throwable cause) {
        return new SystemException(message, cause);
    }

    /**
     * 创建系统异常
     */
    public static SystemException system(ResponseCodeEnum responseCode) {
        return SystemException.of(responseCode);
    }

    /**
     * 数据库错误异常
     */
    public static SystemException databaseError() {
        return SystemException.databaseError();
    }

    /**
     * 数据库错误异常
     */
    public static SystemException databaseError(String message) {
        return SystemException.databaseError(message);
    }

    /**
     * 数据库错误异常
     */
    public static SystemException databaseError(Throwable cause) {
        return SystemException.databaseError("数据库操作失败", cause);
    }

    /**
     * Redis错误异常
     */
    public static SystemException redisError() {
        return SystemException.redisError();
    }

    /**
     * Redis错误异常
     */
    public static SystemException redisError(String message) {
        return SystemException.redisError(message);
    }

    /**
     * Redis错误异常
     */
    public static SystemException redisError(Throwable cause) {
        return SystemException.redisError("Redis操作失败", cause);
    }

    /**
     * 文件存储错误异常
     */
    public static SystemException fileStorageError() {
        return SystemException.fileStorageError();
    }

    /**
     * 文件存储错误异常
     */
    public static SystemException fileStorageError(String message) {
        return SystemException.fileStorageError(message);
    }

    /**
     * 网络错误异常
     */
    public static SystemException networkError() {
        return SystemException.networkError();
    }

    /**
     * 网络错误异常
     */
    public static SystemException networkError(String message) {
        return SystemException.networkError(message);
    }

    /**
     * 网络错误异常
     */
    public static SystemException networkError(Throwable cause) {
        return SystemException.networkError("网络连接失败", cause);
    }

    /**
     * 第三方服务错误异常
     */
    public static SystemException thirdPartyError() {
        return SystemException.thirdPartyError();
    }

    /**
     * 第三方服务错误异常
     */
    public static SystemException thirdPartyError(String serviceName) {
        return SystemException.thirdPartyError(String.format("%s 服务调用失败", serviceName));
    }

    /**
     * 第三方服务错误异常
     */
    public static SystemException thirdPartyError(String serviceName, Throwable cause) {
        return SystemException.thirdPartyError(String.format("%s 服务调用失败", serviceName), cause);
    }

    /**
     * Keycloak服务错误异常
     */
    public static SystemException keycloakError() {
        return SystemException.keycloakError();
    }

    /**
     * Keycloak服务错误异常
     */
    public static SystemException keycloakError(String message) {
        return SystemException.keycloakError(message);
    }

    /**
     * Keycloak服务错误异常
     */
    public static SystemException keycloakError(Throwable cause) {
        return SystemException.keycloakError("Keycloak服务调用失败", cause);
    }

    /**
     * 配置错误异常
     */
    public static SystemException configError() {
        return SystemException.configError();
    }

    /**
     * 配置错误异常
     */
    public static SystemException configError(String configName) {
        return SystemException.configError(String.format("配置 [%s] 错误", configName));
    }

    /**
     * 系统过载异常
     */
    public static SystemException systemOverload() {
        return SystemException.systemOverload();
    }

    /**
     * 系统维护异常
     */
    public static SystemException maintenanceMode() {
        return SystemException.maintenanceMode();
    }
}