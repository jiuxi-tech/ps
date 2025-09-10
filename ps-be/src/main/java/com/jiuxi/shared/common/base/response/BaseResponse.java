package com.jiuxi.shared.common.base.response;

import com.jiuxi.shared.common.constants.ResponseCode;
import lombok.Data;
import java.io.Serializable;

/**
 * 基础响应类
 * 统一API响应格式
 * 
 * @author System
 * @since 2.2.2-SNAPSHOT
 */
@Data
public class BaseResponse<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 响应状态码
     */
    private String code;

    /**
     * 响应消息
     */
    private String message;

    /**
     * 响应数据
     */
    private T data;

    /**
     * 时间戳
     */
    private Long timestamp;

    /**
     * 请求跟踪ID
     */
    private String traceId;

    /**
     * 默认构造函数
     */
    public BaseResponse() {
        this.timestamp = System.currentTimeMillis();
    }

    /**
     * 构造函数
     */
    public BaseResponse(String code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.timestamp = System.currentTimeMillis();
    }

    /**
     * 成功响应
     */
    public static <T> BaseResponse<T> success(T data) {
        return new BaseResponse<>(ResponseCode.SUCCESS, "操作成功", data);
    }

    /**
     * 成功响应（无数据）
     */
    public static <T> BaseResponse<T> success() {
        return success(null);
    }

    /**
     * 成功响应（自定义消息）
     */
    public static <T> BaseResponse<T> success(String message, T data) {
        return new BaseResponse<>(ResponseCode.SUCCESS, message, data);
    }

    /**
     * 失败响应
     */
    public static <T> BaseResponse<T> error(String code, String message) {
        return new BaseResponse<>(code, message, null);
    }

    /**
     * 失败响应
     */
    public static <T> BaseResponse<T> error(String message) {
        return error(ResponseCode.ERROR, message);
    }

    /**
     * 系统错误响应
     */
    public static <T> BaseResponse<T> systemError() {
        return error(ResponseCode.SYSTEM_ERROR, "系统繁忙，请稍后重试");
    }

    /**
     * 参数错误响应
     */
    public static <T> BaseResponse<T> paramError(String message) {
        return error(ResponseCode.PARAM_ERROR, message);
    }

    /**
     * 未授权响应
     */
    public static <T> BaseResponse<T> unauthorized() {
        return error(ResponseCode.UNAUTHORIZED, "未授权访问");
    }

    /**
     * 禁止访问响应
     */
    public static <T> BaseResponse<T> forbidden() {
        return error(ResponseCode.FORBIDDEN, "权限不足");
    }

    /**
     * 资源不存在响应
     */
    public static <T> BaseResponse<T> notFound() {
        return error(ResponseCode.NOT_FOUND, "资源不存在");
    }

    /**
     * 设置跟踪ID
     */
    public BaseResponse<T> traceId(String traceId) {
        this.traceId = traceId;
        return this;
    }

    /**
     * 判断是否成功
     */
    public boolean isSuccess() {
        return ResponseCode.SUCCESS.equals(this.code);
    }

    /**
     * 判断是否失败
     */
    public boolean isError() {
        return !isSuccess();
    }
}