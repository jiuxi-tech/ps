package com.jiuxi.shared.common.enums;

/**
 * 响应码枚举
 * 定义系统统一的响应状态码
 * 
 * @author System
 * @since 2.2.2-SNAPSHOT
 */
public enum ResponseCodeEnum {

    /**
     * 成功响应 (200-299)
     */
    SUCCESS("200", "操作成功"),
    CREATED("201", "创建成功"),
    ACCEPTED("202", "请求已接受"),
    NO_CONTENT("204", "无内容"),

    /**
     * 客户端错误 (400-499)
     */
    BAD_REQUEST("400", "请求参数错误"),
    UNAUTHORIZED("401", "未授权访问"),
    FORBIDDEN("403", "权限不足"),
    NOT_FOUND("404", "资源不存在"),
    METHOD_NOT_ALLOWED("405", "请求方法不支持"),
    NOT_ACCEPTABLE("406", "不可接受的请求"),
    REQUEST_TIMEOUT("408", "请求超时"),
    CONFLICT("409", "资源冲突"),
    GONE("410", "资源已删除"),
    PRECONDITION_FAILED("412", "前置条件失败"),
    PAYLOAD_TOO_LARGE("413", "请求体过大"),
    UNSUPPORTED_MEDIA_TYPE("415", "不支持的媒体类型"),
    TOO_MANY_REQUESTS("429", "请求过于频繁"),

    /**
     * 服务器错误 (500-599)
     */
    INTERNAL_SERVER_ERROR("500", "服务器内部错误"),
    NOT_IMPLEMENTED("501", "功能未实现"),
    BAD_GATEWAY("502", "网关错误"),
    SERVICE_UNAVAILABLE("503", "服务不可用"),
    GATEWAY_TIMEOUT("504", "网关超时"),
    HTTP_VERSION_NOT_SUPPORTED("505", "HTTP版本不支持"),

    /**
     * 业务错误 (600-699)
     */
    BUSINESS_ERROR("600", "业务处理失败"),
    DATA_NOT_FOUND("601", "数据不存在"),
    DATA_DUPLICATE("602", "数据重复"),
    DATA_STATUS_ERROR("603", "数据状态异常"),
    DATA_INTEGRITY_VIOLATION("604", "数据完整性约束违反"),
    BUSINESS_RULE_VIOLATION("605", "业务规则违反"),
    WORKFLOW_ERROR("606", "工作流处理错误"),
    VALIDATION_FAILED("607", "数据验证失败"),
    OPERATION_NOT_ALLOWED("608", "操作不被允许"),
    RESOURCE_LOCKED("609", "资源被锁定"),

    /**
     * 认证授权错误 (700-799)
     */
    AUTH_FAILED("700", "认证失败"),
    TOKEN_INVALID("701", "令牌无效"),
    TOKEN_EXPIRED("702", "令牌已过期"),
    REFRESH_TOKEN_INVALID("703", "刷新令牌无效"),
    PERMISSION_DENIED("704", "权限被拒绝"),
    ACCOUNT_LOCKED("705", "账户被锁定"),
    ACCOUNT_DISABLED("706", "账户被禁用"),
    PASSWORD_EXPIRED("707", "密码已过期"),
    CAPTCHA_ERROR("708", "验证码错误"),
    LOGIN_REQUIRED("709", "需要登录"),

    /**
     * 第三方服务错误 (800-899)
     */
    THIRD_PARTY_ERROR("800", "第三方服务错误"),
    DATABASE_ERROR("801", "数据库错误"),
    REDIS_ERROR("802", "Redis错误"),
    MQ_ERROR("803", "消息队列错误"),
    FILE_STORAGE_ERROR("804", "文件存储错误"),
    SMS_ERROR("805", "短信服务错误"),
    EMAIL_ERROR("806", "邮件服务错误"),
    KEYCLOAK_ERROR("807", "Keycloak服务错误"),
    EXTERNAL_API_ERROR("808", "外部API错误"),
    NETWORK_ERROR("809", "网络错误"),

    /**
     * 系统配置错误 (900-999)
     */
    CONFIG_ERROR("900", "系统配置错误"),
    ENVIRONMENT_ERROR("901", "环境配置错误"),
    DEPENDENCY_ERROR("902", "依赖服务错误"),
    RESOURCE_EXHAUSTED("903", "系统资源耗尽"),
    MAINTENANCE_MODE("904", "系统维护中"),
    VERSION_INCOMPATIBLE("905", "版本不兼容"),
    FEATURE_DISABLED("906", "功能已禁用"),
    QUOTA_EXCEEDED("907", "配额超限"),
    RATE_LIMIT_EXCEEDED("908", "频率限制超限"),
    SYSTEM_OVERLOAD("909", "系统过载");

    private final String code;
    private final String message;

    ResponseCodeEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    /**
     * 根据代码获取枚举
     */
    public static ResponseCodeEnum fromCode(String code) {
        for (ResponseCodeEnum responseCode : values()) {
            if (responseCode.getCode().equals(code)) {
                return responseCode;
            }
        }
        return null;
    }

    /**
     * 判断是否为成功响应
     */
    public boolean isSuccess() {
        return this.code.startsWith("2");
    }

    /**
     * 判断是否为错误响应
     */
    public boolean isError() {
        return !isSuccess();
    }

    /**
     * 判断是否为客户端错误
     */
    public boolean isClientError() {
        return this.code.startsWith("4");
    }

    /**
     * 判断是否为服务器错误
     */
    public boolean isServerError() {
        return this.code.startsWith("5");
    }

    /**
     * 判断是否为业务错误
     */
    public boolean isBusinessError() {
        return this.code.startsWith("6");
    }

    /**
     * 判断是否为认证错误
     */
    public boolean isAuthError() {
        return this.code.startsWith("7");
    }

    @Override
    public String toString() {
        return String.format("[%s] %s", code, message);
    }
}