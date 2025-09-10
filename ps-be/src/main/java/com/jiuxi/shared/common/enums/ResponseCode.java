package com.jiuxi.shared.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 响应码枚举
 */
@Getter
@AllArgsConstructor
public enum ResponseCode {

    /**
     * 成功
     */
    SUCCESS(200, "操作成功"),

    /**
     * 客户端错误
     */
    BAD_REQUEST(400, "请求参数错误"),
    UNAUTHORIZED(401, "未认证"),
    FORBIDDEN(403, "无权限"),
    NOT_FOUND(404, "资源不存在"),
    METHOD_NOT_ALLOWED(405, "请求方法不支持"),
    CONFLICT(409, "资源冲突"),
    UNPROCESSABLE_ENTITY(422, "请求参数验证失败"),

    /**
     * 服务端错误
     */
    INTERNAL_ERROR(500, "系统内部错误"),
    SERVICE_UNAVAILABLE(503, "服务不可用"),

    /**
     * 业务错误
     */
    BUSINESS_ERROR(10000, "业务处理失败"),
    VALIDATION_ERROR(10001, "参数验证失败"),
    DATA_NOT_EXIST(10002, "数据不存在"),
    DATA_ALREADY_EXIST(10003, "数据已存在"),
    OPERATION_NOT_ALLOWED(10004, "操作不被允许");

    private final Integer code;
    private final String message;
}