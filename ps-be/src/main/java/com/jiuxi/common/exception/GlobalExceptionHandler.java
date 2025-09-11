package com.jiuxi.common.exception;

import com.jiuxi.common.bean.ApiResponse;
import com.jiuxi.common.bean.ErrorCode;
import com.jiuxi.common.bean.JsonResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Set;

/**
 * @ClassName: GlobalExceptionHandler
 * @Description: 全局异常处理器
 * @Author: System
 * @Date: 2025/01/07
 * @Copyright: 2025 www.jiuxi.com Inc. All rights reserved.
 * 
 * @deprecated 请使用 {@link com.jiuxi.shared.common.handler.GlobalExceptionHandler} 替代
 */
@Deprecated
@RestControllerAdvice("com.jiuxi.common")
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 获取当前请求路径
     *
     * @return 请求路径
     */
    private String getCurrentRequestPath() {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            return attributes.getRequest().getRequestURI();
        } catch (Exception e) {
            return "unknown";
        }
    }

    /**
     * 处理业务异常
     *
     * @param e       业务异常
     * @param request HTTP请求
     * @return JsonResponse
     */
    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.OK)
    public JsonResponse handleBusinessException(BusinessException e, HttpServletRequest request) {
        String path = request != null ? request.getRequestURI() : getCurrentRequestPath();
        logger.warn("业务异常: {} - {}", path, e.getMessage());
        return JsonResponse.build(e.getCode(), e.getMessage());
    }



    /**
     * 处理参数验证异常
     *
     * @param e       参数验证异常
     * @param request HTTP请求
     * @return JsonResponse
     */
    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public JsonResponse handleValidationException(ValidationException e, HttpServletRequest request) {
        String path = request != null ? request.getRequestURI() : getCurrentRequestPath();
        logger.warn("参数验证异常: {} - {} [字段: {}]", path, e.getMessage(), e.getField());
        return JsonResponse.build(e.getCode(), e.getMessage());
    }



    /**
     * 处理方法参数验证异常
     *
     * @param e       方法参数验证异常
     * @param request HTTP请求
     * @return JsonResponse
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public JsonResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException e, HttpServletRequest request) {
        StringBuilder sb = new StringBuilder();
        for (FieldError error : e.getBindingResult().getFieldErrors()) {
            sb.append(error.getField()).append(": ").append(error.getDefaultMessage()).append("; ");
        }
        String message = sb.toString().trim();
        String path = request != null ? request.getRequestURI() : getCurrentRequestPath();
        logger.warn("方法参数验证异常: {} - {}", path, message);
        return JsonResponse.build(ErrorCode.PARAM_ERROR.getCode(), message);
    }



    /**
     * 处理Bean验证异常
     *
     * @param e       Bean验证异常
     * @param request HTTP请求
     * @return JsonResponse
     */
    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public JsonResponse handleBindException(BindException e, HttpServletRequest request) {
        StringBuilder sb = new StringBuilder();
        for (FieldError error : e.getFieldErrors()) {
            sb.append(error.getField()).append(": ").append(error.getDefaultMessage()).append("; ");
        }
        String message = sb.toString().trim();
        String path = request != null ? request.getRequestURI() : getCurrentRequestPath();
        logger.warn("Bean验证异常: {} - {}", path, message);
        return JsonResponse.build(ErrorCode.PARAM_ERROR.getCode(), message);
    }

    /**
     * 处理约束违反异常
     *
     * @param e       约束违反异常
     * @param request HTTP请求
     * @return JsonResponse
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public JsonResponse handleConstraintViolationException(ConstraintViolationException e, HttpServletRequest request) {
        StringBuilder sb = new StringBuilder();
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        for (ConstraintViolation<?> violation : violations) {
            sb.append(violation.getPropertyPath()).append(": ").append(violation.getMessage()).append("; ");
        }
        String message = sb.toString().trim();
        String path = request != null ? request.getRequestURI() : getCurrentRequestPath();
        logger.warn("约束违反异常: {} - {}", path, message);
        return JsonResponse.build(ErrorCode.PARAM_ERROR.getCode(), message);
    }



    /**
     * 处理系统异常
     *
     * @param e       系统异常
     * @param request HTTP请求
     * @return JsonResponse
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public JsonResponse handleException(Exception e, HttpServletRequest request) {
        String path = request != null ? request.getRequestURI() : getCurrentRequestPath();
        logger.error("系统异常: {} - {}", path, e.getMessage(), e);
        return JsonResponse.build(ErrorCode.SYTEM_ERROR.getCode(), "系统内部错误，请联系管理员");
    }


}