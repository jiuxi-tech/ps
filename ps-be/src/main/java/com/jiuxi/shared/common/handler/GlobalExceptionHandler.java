package com.jiuxi.shared.common.handler;

import com.jiuxi.shared.common.base.response.BaseResponse;
import com.jiuxi.shared.common.enums.ResponseCodeEnum;
import com.jiuxi.shared.common.exception.BaseException;
import com.jiuxi.shared.common.exception.BusinessException;
import com.jiuxi.shared.common.exception.SecurityException;
import com.jiuxi.shared.common.exception.SystemException;
import com.jiuxi.shared.common.exception.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 增强版全局异常处理器
 * 统一处理各种异常并返回标准响应格式
 * 
 * @author System
 * @since 2.2.2-SNAPSHOT
 */
@Slf4j
@RestControllerAdvice(basePackages = {"com.jiuxi.shared", "com.jiuxi.admin", "com.jiuxi.module"})
@Component("enhancedGlobalExceptionHandler")
public class GlobalExceptionHandler {

    /**
     * 处理基础异常
     */
    @ExceptionHandler(BaseException.class)
    public BaseResponse<Object> handleBaseException(BaseException e, HttpServletRequest request) {
        log.warn("业务异常: {} - {}", e.getCode(), e.getMessage(), e);
        logExceptionDetails(e, request);
        
        return BaseResponse.error(e.getCode(), e.getMessage())
                .traceId(getTraceId(request));
    }

    /**
     * 处理业务异常
     */
    @ExceptionHandler(BusinessException.class)
    public BaseResponse<Object> handleBusinessException(BusinessException e, HttpServletRequest request) {
        log.warn("业务异常: {} - {}", e.getCode(), e.getMessage(), e);
        logExceptionDetails(e, request);
        
        BaseResponse<Object> response = BaseResponse.error(e.getCode(), e.getMessage());
        if (e.hasData()) {
            response.setData(e.getData());
        }
        return response.traceId(getTraceId(request));
    }

    /**
     * 处理验证异常
     */
    @ExceptionHandler(ValidationException.class)
    public BaseResponse<Object> handleValidationException(ValidationException e, HttpServletRequest request) {
        log.warn("验证异常: {} - {}", e.getCode(), e.getMessage(), e);
        logExceptionDetails(e, request);
        
        BaseResponse<Object> response = BaseResponse.error(e.getCode(), e.getMessage());
        if (e.hasValidationErrors()) {
            response.setData(e.getValidationErrors());
        }
        return response.traceId(getTraceId(request));
    }

    /**
     * 处理安全异常
     */
    @ExceptionHandler(SecurityException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public BaseResponse<Object> handleSecurityException(SecurityException e, HttpServletRequest request) {
        log.warn("安全异常: {} - {}", e.getCode(), e.getMessage(), e);
        logExceptionDetails(e, request);
        
        return BaseResponse.error(e.getCode(), e.getMessage())
                .traceId(getTraceId(request));
    }

    /**
     * 处理系统异常
     */
    @ExceptionHandler(SystemException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public BaseResponse<Object> handleSystemException(SystemException e, HttpServletRequest request) {
        log.error("系统异常: {} - {}", e.getCode(), e.getMessage(), e);
        logExceptionDetails(e, request);
        
        return BaseResponse.error(e.getCode(), e.getMessage())
                .traceId(getTraceId(request));
    }

    /**
     * 处理TopinfoRuntimeException异常
     */
    @ExceptionHandler(com.jiuxi.shared.common.exception.TopinfoRuntimeException.class)
    public BaseResponse<Object> handleTopinfoRuntimeException(com.jiuxi.shared.common.exception.TopinfoRuntimeException e, HttpServletRequest request) {
        log.warn("业务运行时异常: {} - {}", e.getErrcode(), e.getMessage(), e);
        logExceptionDetails(e, request);
        
        return BaseResponse.error(String.valueOf(e.getErrcode()), e.getMessage())
                .traceId(getTraceId(request));
    }

    /**
     * 处理参数验证异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public BaseResponse<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException e, HttpServletRequest request) {
        log.warn("参数验证异常: {}", e.getMessage());
        
        Map<String, List<String>> validationErrors = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            validationErrors.computeIfAbsent(fieldName, k -> new ArrayList<>()).add(errorMessage);
        });
        
        return BaseResponse.error(ResponseCodeEnum.VALIDATION_FAILED.getCode(), "参数验证失败")
                .traceId(getTraceId(request));
    }

    /**
     * 处理绑定异常
     */
    @ExceptionHandler(BindException.class)
    public BaseResponse<Object> handleBindException(BindException e, HttpServletRequest request) {
        log.warn("绑定异常: {}", e.getMessage());
        
        Map<String, List<String>> validationErrors = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            validationErrors.computeIfAbsent(fieldName, k -> new ArrayList<>()).add(errorMessage);
        });
        
        return BaseResponse.error(ResponseCodeEnum.VALIDATION_FAILED.getCode(), "参数绑定失败")
                .traceId(getTraceId(request));
    }

    /**
     * 处理约束违反异常
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public BaseResponse<Object> handleConstraintViolationException(ConstraintViolationException e, HttpServletRequest request) {
        log.warn("约束违反异常: {}", e.getMessage());
        
        Map<String, List<String>> validationErrors = new HashMap<>();
        for (ConstraintViolation<?> violation : e.getConstraintViolations()) {
            String fieldName = violation.getPropertyPath().toString();
            String errorMessage = violation.getMessage();
            validationErrors.computeIfAbsent(fieldName, k -> new ArrayList<>()).add(errorMessage);
        }
        
        return BaseResponse.error(ResponseCodeEnum.VALIDATION_FAILED.getCode(), "约束验证失败")
                .traceId(getTraceId(request));
    }

    /**
     * 处理缺少请求参数异常
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public BaseResponse<Object> handleMissingServletRequestParameterException(MissingServletRequestParameterException e, HttpServletRequest request) {
        log.warn("缺少请求参数: {}", e.getMessage());
        
        String message = String.format("缺少必要参数: %s", e.getParameterName());
        return BaseResponse.error(ResponseCodeEnum.BAD_REQUEST.getCode(), message)
                .traceId(getTraceId(request));
    }

    /**
     * 处理方法参数类型不匹配异常
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public BaseResponse<Object> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e, HttpServletRequest request) {
        log.warn("方法参数类型不匹配: {}", e.getMessage());
        
        String message = String.format("参数 %s 类型错误，期望类型: %s", 
            e.getName(), e.getRequiredType() != null ? e.getRequiredType().getSimpleName() : "未知");
        return BaseResponse.error(ResponseCodeEnum.BAD_REQUEST.getCode(), message)
                .traceId(getTraceId(request));
    }

    /**
     * 处理HTTP消息不可读异常
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public BaseResponse<Object> handleHttpMessageNotReadableException(HttpMessageNotReadableException e, HttpServletRequest request) {
        log.warn("HTTP消息不可读: {}", e.getMessage());
        
        return BaseResponse.error(ResponseCodeEnum.BAD_REQUEST.getCode(), "请求体格式错误")
                .traceId(getTraceId(request));
    }

    /**
     * 处理不支持的HTTP方法异常
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public BaseResponse<Object> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e, HttpServletRequest request) {
        log.warn("不支持的HTTP方法: {}", e.getMessage());
        
        String message = String.format("不支持的请求方法: %s，支持的方法: %s", 
            e.getMethod(), String.join(", ", e.getSupportedMethods()));
        return BaseResponse.error(ResponseCodeEnum.METHOD_NOT_ALLOWED.getCode(), message)
                .traceId(getTraceId(request));
    }

    /**
     * 处理不支持的媒体类型异常
     */
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public BaseResponse<Object> handleHttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException e, HttpServletRequest request) {
        log.warn("不支持的媒体类型: {}", e.getMessage());
        
        String message = String.format("不支持的媒体类型: %s", e.getContentType());
        return BaseResponse.error(ResponseCodeEnum.UNSUPPORTED_MEDIA_TYPE.getCode(), message)
                .traceId(getTraceId(request));
    }

    /**
     * 处理文件上传大小超限异常
     */
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public BaseResponse<Object> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException e, HttpServletRequest request) {
        log.warn("文件上传大小超限: {}", e.getMessage());
        
        return BaseResponse.error(ResponseCodeEnum.PAYLOAD_TOO_LARGE.getCode(), "上传文件大小超出限制")
                .traceId(getTraceId(request));
    }

    /**
     * 处理404异常
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public BaseResponse<Object> handleNoHandlerFoundException(NoHandlerFoundException e, HttpServletRequest request) {
        log.warn("404异常: {}", e.getMessage());
        
        String message = String.format("请求路径不存在: %s %s", e.getHttpMethod(), e.getRequestURL());
        return BaseResponse.error(ResponseCodeEnum.NOT_FOUND.getCode(), message)
                .traceId(getTraceId(request));
    }

    /**
     * 处理Spring Security访问拒绝异常
     */
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public BaseResponse<Object> handleAccessDeniedException(AccessDeniedException e, HttpServletRequest request) {
        log.warn("访问拒绝: {}", e.getMessage());
        
        return BaseResponse.forbidden()
                .traceId(getTraceId(request));
    }

    /**
     * 处理认证凭据错误异常
     */
    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public BaseResponse<Object> handleBadCredentialsException(BadCredentialsException e, HttpServletRequest request) {
        log.warn("认证凭据错误: {}", e.getMessage());
        
        return BaseResponse.error(ResponseCodeEnum.AUTH_FAILED.getCode(), "用户名或密码错误")
                .traceId(getTraceId(request));
    }

    /**
     * 处理认证不足异常
     */
    @ExceptionHandler(InsufficientAuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public BaseResponse<Object> handleInsufficientAuthenticationException(InsufficientAuthenticationException e, HttpServletRequest request) {
        log.warn("认证不足: {}", e.getMessage());
        
        return BaseResponse.unauthorized()
                .traceId(getTraceId(request));
    }

    /**
     * 处理数据访问异常
     */
    @ExceptionHandler(DataAccessException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public BaseResponse<Object> handleDataAccessException(DataAccessException e, HttpServletRequest request) {
        log.error("数据访问异常: {}", e.getMessage(), e);
        
        return BaseResponse.error(ResponseCodeEnum.DATABASE_ERROR.getCode(), "数据库操作失败")
                .traceId(getTraceId(request));
    }

    /**
     * 处理数据完整性违反异常
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public BaseResponse<Object> handleDataIntegrityViolationException(DataIntegrityViolationException e, HttpServletRequest request) {
        log.warn("数据完整性违反: {}", e.getMessage());
        
        return BaseResponse.error(ResponseCodeEnum.DATA_INTEGRITY_VIOLATION.getCode(), "数据完整性约束违反")
                .traceId(getTraceId(request));
    }

    /**
     * 处理SQL异常
     */
    @ExceptionHandler(SQLException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public BaseResponse<Object> handleSQLException(SQLException e, HttpServletRequest request) {
        log.error("SQL异常: {}", e.getMessage(), e);
        
        return BaseResponse.error(ResponseCodeEnum.DATABASE_ERROR.getCode(), "数据库操作异常")
                .traceId(getTraceId(request));
    }

    /**
     * 处理所有未捕获的异常
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public BaseResponse<Object> handleException(Exception e, HttpServletRequest request) {
        log.error("未处理异常: {}", e.getMessage(), e);
        logExceptionDetails(e, request);
        
        return BaseResponse.systemError()
                .traceId(getTraceId(request));
    }

    /**
     * 记录异常详细信息
     */
    private void logExceptionDetails(Throwable e, HttpServletRequest request) {
        log.info("异常详情 - 请求URL: {}, 请求方法: {}, 客户端IP: {}, User-Agent: {}", 
            request.getRequestURL(), 
            request.getMethod(),
            getClientIpAddress(request),
            request.getHeader("User-Agent"));
    }

    /**
     * 获取客户端IP地址
     */
    private String getClientIpAddress(HttpServletRequest request) {
        String[] headers = {"X-Forwarded-For", "X-Real-IP", "Proxy-Client-IP", "WL-Proxy-Client-IP"};
        
        for (String header : headers) {
            String ip = request.getHeader(header);
            if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
                // 多级代理的情况，第一个IP为客户端真实IP
                return ip.split(",")[0].trim();
            }
        }
        
        return request.getRemoteAddr();
    }

    /**
     * 获取请求跟踪ID
     */
    private String getTraceId(HttpServletRequest request) {
        String traceId = request.getHeader("X-Trace-Id");
        if (traceId == null || traceId.isEmpty()) {
            traceId = request.getHeader("X-Request-Id");
        }
        return traceId;
    }
}