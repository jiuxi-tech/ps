package com.jiuxi.common.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * @ClassName: HttpClientUtil
 * @Description: HTTP客户端工具类
 * @Author: System
 * @Date: 2025/01/07
 * @Copyright: 2025 www.jiuxi.com Inc. All rights reserved.
 */
public class HttpClientUtil {
    
    private static final Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);
    private static final RestTemplate restTemplate = new RestTemplate();
    private static final ObjectMapper objectMapper = new ObjectMapper();
    
    /**
     * GET请求
     */
    public static String get(String url) {
        return get(url, null, String.class);
    }
    
    /**
     * GET请求（带请求头）
     */
    public static String get(String url, HttpHeaders headers) {
        return get(url, headers, String.class);
    }
    
    /**
     * GET请求（泛型返回）
     */
    public static <T> T get(String url, HttpHeaders headers, Class<T> responseType) {
        try {
            HttpEntity<?> entity = new HttpEntity<>(headers);
            ResponseEntity<T> response = restTemplate.exchange(url, HttpMethod.GET, entity, responseType);
            return response.getBody();
        } catch (Exception e) {
            logger.error("GET请求失败: {}", url, e);
            throw new RuntimeException("GET请求失败: " + e.getMessage(), e);
        }
    }
    
    /**
     * POST请求（JSON）
     */
    public static String post(String url, Object requestBody) {
        return post(url, requestBody, null, String.class);
    }
    
    /**
     * POST请求（JSON，带请求头）
     */
    public static String post(String url, Object requestBody, HttpHeaders headers) {
        return post(url, requestBody, headers, String.class);
    }
    
    /**
     * POST请求（JSON，泛型返回）
     */
    public static <T> T post(String url, Object requestBody, HttpHeaders headers, Class<T> responseType) {
        try {
            if (headers == null) {
                headers = new HttpHeaders();
            }
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            HttpEntity<Object> entity = new HttpEntity<>(requestBody, headers);
            ResponseEntity<T> response = restTemplate.exchange(url, HttpMethod.POST, entity, responseType);
            return response.getBody();
        } catch (Exception e) {
            logger.error("POST请求失败: {}", url, e);
            throw new RuntimeException("POST请求失败: " + e.getMessage(), e);
        }
    }
    
    /**
     * POST表单请求
     */
    public static String postForm(String url, MultiValueMap<String, String> formData) {
        return postForm(url, formData, null, String.class);
    }
    
    /**
     * POST表单请求（带请求头）
     */
    public static String postForm(String url, MultiValueMap<String, String> formData, HttpHeaders headers) {
        return postForm(url, formData, headers, String.class);
    }
    
    /**
     * POST表单请求（泛型返回）
     */
    public static <T> T postForm(String url, MultiValueMap<String, String> formData, HttpHeaders headers, Class<T> responseType) {
        try {
            if (headers == null) {
                headers = new HttpHeaders();
            }
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            
            HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(formData, headers);
            ResponseEntity<T> response = restTemplate.exchange(url, HttpMethod.POST, entity, responseType);
            return response.getBody();
        } catch (Exception e) {
            logger.error("POST表单请求失败: {}", url, e);
            throw new RuntimeException("POST表单请求失败: " + e.getMessage(), e);
        }
    }
    
    /**
     * PUT请求
     */
    public static String put(String url, Object requestBody) {
        return put(url, requestBody, null, String.class);
    }
    
    /**
     * PUT请求（带请求头）
     */
    public static String put(String url, Object requestBody, HttpHeaders headers) {
        return put(url, requestBody, headers, String.class);
    }
    
    /**
     * PUT请求（泛型返回）
     */
    public static <T> T put(String url, Object requestBody, HttpHeaders headers, Class<T> responseType) {
        try {
            if (headers == null) {
                headers = new HttpHeaders();
            }
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            HttpEntity<Object> entity = new HttpEntity<>(requestBody, headers);
            ResponseEntity<T> response = restTemplate.exchange(url, HttpMethod.PUT, entity, responseType);
            return response.getBody();
        } catch (Exception e) {
            logger.error("PUT请求失败: {}", url, e);
            throw new RuntimeException("PUT请求失败: " + e.getMessage(), e);
        }
    }
    
    /**
     * DELETE请求
     */
    public static String delete(String url) {
        return delete(url, null, String.class);
    }
    
    /**
     * DELETE请求（带请求头）
     */
    public static String delete(String url, HttpHeaders headers) {
        return delete(url, headers, String.class);
    }
    
    /**
     * DELETE请求（泛型返回）
     */
    public static <T> T delete(String url, HttpHeaders headers, Class<T> responseType) {
        try {
            HttpEntity<?> entity = new HttpEntity<>(headers);
            ResponseEntity<T> response = restTemplate.exchange(url, HttpMethod.DELETE, entity, responseType);
            return response.getBody();
        } catch (Exception e) {
            logger.error("DELETE请求失败: {}", url, e);
            throw new RuntimeException("DELETE请求失败: " + e.getMessage(), e);
        }
    }
    
    /**
     * 创建基础认证请求头
     */
    public static HttpHeaders createBasicAuthHeaders(String username, String password) {
        HttpHeaders headers = new HttpHeaders();
        String auth = username + ":" + password;
        String encodedAuth = java.util.Base64.getEncoder().encodeToString(auth.getBytes());
        headers.set("Authorization", "Basic " + encodedAuth);
        return headers;
    }
    
    /**
     * 创建Bearer Token请求头
     */
    public static HttpHeaders createBearerTokenHeaders(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        return headers;
    }
    
    /**
     * 创建JSON请求头
     */
    public static HttpHeaders createJsonHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }
    
    /**
     * 创建表单请求头
     */
    public static HttpHeaders createFormHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        return headers;
    }
    
    /**
     * 对象转JSON字符串
     */
    public static String toJson(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            logger.error("对象转JSON失败", e);
            throw new RuntimeException("对象转JSON失败: " + e.getMessage(), e);
        }
    }
    
    /**
     * JSON字符串转对象
     */
    public static <T> T fromJson(String json, Class<T> clazz) {
        try {
            return objectMapper.readValue(json, clazz);
        } catch (Exception e) {
            logger.error("JSON转对象失败", e);
            throw new RuntimeException("JSON转对象失败: " + e.getMessage(), e);
        }
    }
    
    /**
     * 构建URL参数
     */
    public static String buildUrlWithParams(String baseUrl, Map<String, Object> params) {
        if (params == null || params.isEmpty()) {
            return baseUrl;
        }
        
        StringBuilder url = new StringBuilder(baseUrl);
        if (!baseUrl.contains("?")) {
            url.append("?");
        } else {
            url.append("&");
        }
        
        boolean first = true;
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            if (!first) {
                url.append("&");
            }
            url.append(entry.getKey()).append("=").append(entry.getValue());
            first = false;
        }
        
        return url.toString();
    }
    
    /**
     * 检查HTTP状态码是否成功
     */
    public static boolean isSuccessful(int statusCode) {
        return statusCode >= 200 && statusCode < 300;
    }
    
    /**
     * 获取RestTemplate实例
     */
    public static RestTemplate getRestTemplate() {
        return restTemplate;
    }
}