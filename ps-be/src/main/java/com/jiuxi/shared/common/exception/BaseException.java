package com.jiuxi.shared.common.exception;

import com.jiuxi.shared.common.enums.ResponseCodeEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 基础异常类
 * 所有自定义异常的基类
 * 
 * @author System
 * @since 2.2.2-SNAPSHOT
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BaseException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * 错误码
     */
    private String code;

    /**
     * 错误消息
     */
    private String message;

    /**
     * 详细错误信息
     */
    private Object data;

    /**
     * 异常发生时间
     */
    private Long timestamp;

    /**
     * 默认构造函数
     */
    public BaseException() {
        super();
        this.timestamp = System.currentTimeMillis();
    }

    /**
     * 构造函数
     */
    public BaseException(String message) {
        super(message);
        this.message = message;
        this.code = ResponseCodeEnum.INTERNAL_SERVER_ERROR.getCode();
        this.timestamp = System.currentTimeMillis();
    }

    /**
     * 构造函数
     */
    public BaseException(String code, String message) {
        super(message);
        this.code = code;
        this.message = message;
        this.timestamp = System.currentTimeMillis();
    }

    /**
     * 构造函数
     */
    public BaseException(String code, String message, Object data) {
        super(message);
        this.code = code;
        this.message = message;
        this.data = data;
        this.timestamp = System.currentTimeMillis();
    }

    /**
     * 构造函数
     */
    public BaseException(String message, Throwable cause) {
        super(message, cause);
        this.message = message;
        this.code = ResponseCodeEnum.INTERNAL_SERVER_ERROR.getCode();
        this.timestamp = System.currentTimeMillis();
    }

    /**
     * 构造函数
     */
    public BaseException(String code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
        this.message = message;
        this.timestamp = System.currentTimeMillis();
    }

    /**
     * 构造函数
     */
    public BaseException(String code, String message, Object data, Throwable cause) {
        super(message, cause);
        this.code = code;
        this.message = message;
        this.data = data;
        this.timestamp = System.currentTimeMillis();
    }

    /**
     * 使用响应码枚举构造异常
     */
    public BaseException(ResponseCodeEnum responseCode) {
        super(responseCode.getMessage());
        this.code = responseCode.getCode();
        this.message = responseCode.getMessage();
        this.timestamp = System.currentTimeMillis();
    }

    /**
     * 使用响应码枚举构造异常
     */
    public BaseException(ResponseCodeEnum responseCode, Object data) {
        super(responseCode.getMessage());
        this.code = responseCode.getCode();
        this.message = responseCode.getMessage();
        this.data = data;
        this.timestamp = System.currentTimeMillis();
    }

    /**
     * 使用响应码枚举构造异常
     */
    public BaseException(ResponseCodeEnum responseCode, Throwable cause) {
        super(responseCode.getMessage(), cause);
        this.code = responseCode.getCode();
        this.message = responseCode.getMessage();
        this.timestamp = System.currentTimeMillis();
    }

    /**
     * 使用响应码枚举构造异常
     */
    public BaseException(ResponseCodeEnum responseCode, Object data, Throwable cause) {
        super(responseCode.getMessage(), cause);
        this.code = responseCode.getCode();
        this.message = responseCode.getMessage();
        this.data = data;
        this.timestamp = System.currentTimeMillis();
    }

    /**
     * 重写getMessage方法
     */
    @Override
    public String getMessage() {
        return this.message != null ? this.message : super.getMessage();
    }

    /**
     * 获取格式化的异常信息
     */
    public String getFormattedMessage() {
        return String.format("[%s] %s", code, message);
    }

    /**
     * 判断是否有详细数据
     */
    public boolean hasData() {
        return data != null;
    }

    /**
     * 创建异常构建器
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * 异常构建器
     */
    public static class Builder {
        private String code;
        private String message;
        private Object data;
        private Throwable cause;

        public Builder code(String code) {
            this.code = code;
            return this;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Builder data(Object data) {
            this.data = data;
            return this;
        }

        public Builder cause(Throwable cause) {
            this.cause = cause;
            return this;
        }

        public Builder responseCode(ResponseCodeEnum responseCode) {
            this.code = responseCode.getCode();
            this.message = responseCode.getMessage();
            return this;
        }

        public BaseException build() {
            if (cause != null) {
                return new BaseException(code, message, data, cause);
            } else {
                return new BaseException(code, message, data);
            }
        }
    }
}