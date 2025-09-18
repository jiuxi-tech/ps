package com.jiuxi.module.auth.intf.web.exception;

import com.jiuxi.common.bean.JsonResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Auth模块全局异常处理器
 * 提供统一的异常处理和错误响应格式
 * 
 * @author DDD Refactor
 * @date 2025-09-19
 */
@RestControllerAdvice(basePackages = "com.jiuxi.module.auth.intf.web.controller")
public class AuthGlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(AuthGlobalExceptionHandler.class);

    /**
     * 处理业务逻辑异常
     */
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public JsonResponse<Void> handleIllegalArgumentException(IllegalArgumentException e) {
        logger.warn("业务参数异常: {}", e.getMessage());
        return JsonResponse.buildFailure("参数错误: " + e.getMessage());
    }

    /**
     * 处理资源不存在异常
     */
    @ExceptionHandler(IllegalStateException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public JsonResponse<Void> handleIllegalStateException(IllegalStateException e) {
        logger.warn("资源状态异常: {}", e.getMessage());
        return JsonResponse.buildFailure("操作失败: " + e.getMessage());
    }

    /**
     * 处理空指针异常
     */
    @ExceptionHandler(NullPointerException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public JsonResponse<Void> handleNullPointerException(NullPointerException e) {
        logger.error("空指针异常", e);
        return JsonResponse.buildFailure("系统内部错误，请联系管理员");
    }

    /**
     * 处理运行时异常
     */
    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public JsonResponse<Void> handleRuntimeException(RuntimeException e) {
        logger.error("运行时异常: {}", e.getMessage(), e);
        return JsonResponse.buildFailure("操作失败: " + e.getMessage());
    }

    /**
     * 处理其他未捕获的异常
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public JsonResponse<Void> handleGenericException(Exception e) {
        logger.error("系统异常", e);
        return JsonResponse.buildFailure("系统错误，请稍后重试");
    }
}