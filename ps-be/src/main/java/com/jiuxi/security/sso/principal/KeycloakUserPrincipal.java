package com.jiuxi.security.sso.principal;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Keycloak 用户主体信息
 * 
 * @author SSO Integration
 * @since 2.2.2
 */
public class KeycloakUserPrincipal implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 用户 ID
     */
    private String userId;
    
    /**
     * 用户名
     */
    private String username;
    
    /**
     * 邮箱
     */
    private String email;
    
    /**
     * 姓名
     */
    private String name;
    
    /**
     * 角色列表
     */
    private List<String> roles;
    
    /**
     * 权限列表
     */
    private List<String> permissions;
    
    /**
     * 扩展属性
     */
    private Map<String, Object> attributes;
    
    /**
     * JWT Token
     */
    private String token;
    
    /**
     * Token 过期时间
     */
    private Long expiresAt;
    
    public KeycloakUserPrincipal() {
    }
    
    public KeycloakUserPrincipal(String userId, String username) {
        this.userId = userId;
        this.username = username;
    }
    
    /**
     * 检查用户是否具有指定角色
     */
    public boolean hasRole(String role) {
        return roles != null && roles.contains(role);
    }
    
    /**
     * 检查用户是否具有指定权限
     */
    public boolean hasPermission(String permission) {
        return permissions != null && permissions.contains(permission);
    }
    
    /**
     * 检查 Token 是否过期
     */
    public boolean isTokenExpired() {
        if (expiresAt == null) {
            return false;
        }
        return System.currentTimeMillis() / 1000 > expiresAt;
    }
    
    // getters and setters
    public String getUserId() {
        return userId;
    }
    
    public void setUserId(String userId) {
        this.userId = userId;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public List<String> getRoles() {
        return roles;
    }
    
    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
    
    public List<String> getPermissions() {
        return permissions;
    }
    
    public void setPermissions(List<String> permissions) {
        this.permissions = permissions;
    }
    
    public Map<String, Object> getAttributes() {
        return attributes;
    }
    
    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }
    
    public String getToken() {
        return token;
    }
    
    public void setToken(String token) {
        this.token = token;
    }
    
    public Long getExpiresAt() {
        return expiresAt;
    }
    
    public void setExpiresAt(Long expiresAt) {
        this.expiresAt = expiresAt;
    }
    
    @Override
    public String toString() {
        return "KeycloakUserPrincipal{" +
                "userId='" + userId + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", roles=" + roles +
                ", permissions=" + permissions +
                ", expiresAt=" + expiresAt +
                '}';
    }
}