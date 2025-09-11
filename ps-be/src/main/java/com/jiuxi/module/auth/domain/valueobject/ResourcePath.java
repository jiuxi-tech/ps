package com.jiuxi.module.auth.domain.valueobject;

import java.util.Objects;

/**
 * 资源路径值对象
 * 表示API资源的访问路径
 * 
 * @author DDD Refactor
 * @date 2025-09-11
 */
public class ResourcePath {
    
    private final String path;
    private final String httpMethod;
    
    public ResourcePath(String path, String httpMethod) {
        if (path == null || path.trim().isEmpty()) {
            throw new IllegalArgumentException("资源路径不能为空");
        }
        if (httpMethod == null || httpMethod.trim().isEmpty()) {
            throw new IllegalArgumentException("HTTP方法不能为空");
        }
        
        this.path = path.trim();
        this.httpMethod = httpMethod.trim().toUpperCase();
        
        validateHttpMethod();
    }
    
    private void validateHttpMethod() {
        if (!isValidHttpMethod(this.httpMethod)) {
            throw new IllegalArgumentException("不支持的HTTP方法: " + this.httpMethod);
        }
    }
    
    private boolean isValidHttpMethod(String method) {
        return "GET".equals(method) || "POST".equals(method) || 
               "PUT".equals(method) || "DELETE".equals(method) || 
               "PATCH".equals(method) || "HEAD".equals(method) || 
               "OPTIONS".equals(method);
    }
    
    /**
     * 检查路径是否匹配
     * 支持通配符匹配，如 /api/v1/users/* 匹配 /api/v1/users/123
     */
    public boolean matches(String requestPath, String requestMethod) {
        if (!this.httpMethod.equals(requestMethod.toUpperCase())) {
            return false;
        }
        
        if (this.path.equals(requestPath)) {
            return true;
        }
        
        // 支持通配符匹配
        if (this.path.endsWith("/*")) {
            String prefix = this.path.substring(0, this.path.length() - 2);
            return requestPath.startsWith(prefix);
        }
        
        // 支持路径参数匹配，如 /api/v1/users/{id} 匹配 /api/v1/users/123
        if (this.path.contains("{") && this.path.contains("}")) {
            return matchesWithPathVariables(requestPath);
        }
        
        return false;
    }
    
    private boolean matchesWithPathVariables(String requestPath) {
        String[] pathSegments = this.path.split("/");
        String[] requestSegments = requestPath.split("/");
        
        if (pathSegments.length != requestSegments.length) {
            return false;
        }
        
        for (int i = 0; i < pathSegments.length; i++) {
            String pathSegment = pathSegments[i];
            String requestSegment = requestSegments[i];
            
            // 如果是路径变量（被{}包围），则跳过检查
            if (pathSegment.startsWith("{") && pathSegment.endsWith("}")) {
                continue;
            }
            
            // 否则必须完全匹配
            if (!pathSegment.equals(requestSegment)) {
                return false;
            }
        }
        
        return true;
    }
    
    public String getPath() {
        return path;
    }
    
    public String getHttpMethod() {
        return httpMethod;
    }
    
    /**
     * 获取完整的资源标识符
     */
    public String getFullIdentifier() {
        return httpMethod + " " + path;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ResourcePath that = (ResourcePath) o;
        return Objects.equals(path, that.path) && Objects.equals(httpMethod, that.httpMethod);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(path, httpMethod);
    }
    
    @Override
    public String toString() {
        return getFullIdentifier();
    }
}