package com.jiuxi.shared.common.base.vo;

import lombok.Data;
import java.io.Serializable;

/**
 * 基础响应对象
 */
@Data
public class BaseResponse<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 响应码
     */
    private Integer code;

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
     * 请求追踪ID
     */
    private String traceId;

    public BaseResponse() {
        this.timestamp = System.currentTimeMillis();
    }

    public static <T> BaseResponse<T> success() {
        BaseResponse<T> response = new BaseResponse<>();
        response.setCode(200);
        response.setMessage("操作成功");
        return response;
    }

    public static <T> BaseResponse<T> success(T data) {
        BaseResponse<T> response = success();
        response.setData(data);
        return response;
    }

    public static <T> BaseResponse<T> error(Integer code, String message) {
        BaseResponse<T> response = new BaseResponse<>();
        response.setCode(code);
        response.setMessage(message);
        return response;
    }
}