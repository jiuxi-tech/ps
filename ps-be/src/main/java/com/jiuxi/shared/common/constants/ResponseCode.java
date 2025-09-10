package com.jiuxi.shared.common.constants;

/**
 * 响应状态码常量
 * 统一定义API响应状态码
 * 
 * @author System
 * @since 2.2.2-SNAPSHOT
 */
public final class ResponseCode {

    private ResponseCode() {
        throw new UnsupportedOperationException("Constants class cannot be instantiated");
    }

    /**
     * 成功
     */
    public static final String SUCCESS = "200";

    /**
     * 通用错误
     */
    public static final String ERROR = "500";

    /**
     * 系统错误
     */
    public static final String SYSTEM_ERROR = "500";

    /**
     * 参数错误
     */
    public static final String PARAM_ERROR = "400";

    /**
     * 未授权
     */
    public static final String UNAUTHORIZED = "401";

    /**
     * 禁止访问
     */
    public static final String FORBIDDEN = "403";

    /**
     * 资源不存在
     */
    public static final String NOT_FOUND = "404";

    /**
     * 请求方法不支持
     */
    public static final String METHOD_NOT_ALLOWED = "405";

    /**
     * 请求超时
     */
    public static final String REQUEST_TIMEOUT = "408";

    /**
     * 业务错误
     */
    public static final String BUSINESS_ERROR = "600";

    /**
     * 数据重复
     */
    public static final String DATA_DUPLICATE = "601";

    /**
     * 数据不存在
     */
    public static final String DATA_NOT_FOUND = "602";

    /**
     * 数据状态异常
     */
    public static final String DATA_STATUS_ERROR = "603";
}