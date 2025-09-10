package com.jiuxi.shared.common.constants;

/**
 * API相关常量
 */
public final class ApiConstants {

    private ApiConstants() {
        throw new UnsupportedOperationException("Constants class cannot be instantiated");
    }

    /**
     * API版本
     */
    public static final String API_VERSION_V1 = "/api/v1";

    /**
     * 响应码
     */
    public static final class ResponseCode {
        public static final Integer SUCCESS = 200;
        public static final Integer BAD_REQUEST = 400;
        public static final Integer UNAUTHORIZED = 401;
        public static final Integer FORBIDDEN = 403;
        public static final Integer NOT_FOUND = 404;
        public static final Integer INTERNAL_ERROR = 500;
    }

    /**
     * HTTP头部
     */
    public static final class Headers {
        public static final String AUTHORIZATION = "Authorization";
        public static final String CONTENT_TYPE = "Content-Type";
        public static final String TRACE_ID = "X-Trace-Id";
        public static final String REQUEST_ID = "X-Request-Id";
    }
}