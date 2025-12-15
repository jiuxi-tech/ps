package com.jiuxi.admin.core.interceptor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jiuxi.common.bean.JsonResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * @ClassName: ApiResponseBodyAdvice
 * @Description: API响应体拦截器，用于记录业务错误信息
 * @Author system
 * @Date 2025-11-30
 * @Copyright: Hangzhou Jiuxi Inc. All rights reserved.
 */
@ControllerAdvice(basePackages = "com.jiuxi.admin.core.controller.openapi")
public class ApiResponseBodyAdvice implements ResponseBodyAdvice<Object> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiResponseBodyAdvice.class);
    
    private static final String RESPONSE_BODY_ATTRIBUTE = "apiResponseBody";
    private static final String BUSINESS_STATUS_ATTRIBUTE = "businessStatus";
    private static final String BUSINESS_ERROR_ATTRIBUTE = "businessErrorMessage";

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        // 只拦截 open-api 包下的接口
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                 Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                 ServerHttpRequest request, ServerHttpResponse response) {
        try {
            if (request instanceof ServletServerHttpRequest) {
                HttpServletRequest servletRequest = ((ServletServerHttpRequest) request).getServletRequest();
                
                // 将响应体存储到 request attribute 中，供拦截器使用
                servletRequest.setAttribute(RESPONSE_BODY_ATTRIBUTE, body);
                
                // 如果是 JsonResponse，提取业务状态码和错误信息
                if (body instanceof JsonResponse) {
                    JsonResponse jsonResponse = (JsonResponse) body;
                    Integer businessStatus = jsonResponse.getCode();
                    // 存储业务状态码（1:成功, -1:失败, 403:权限不足等）
                    servletRequest.setAttribute(BUSINESS_STATUS_ATTRIBUTE, businessStatus);
                    
                    if (businessStatus != 1) {
                        // 业务失败，记录错误信息
                        String errorMessage = jsonResponse.getMessage();
                        servletRequest.setAttribute(BUSINESS_ERROR_ATTRIBUTE, errorMessage);
                        LOGGER.debug("检测到业务错误响应，businessStatus={}, errorMessage={}", businessStatus, errorMessage);
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.error("处理响应体时出错", e);
        }
        
        return body;
    }
}
