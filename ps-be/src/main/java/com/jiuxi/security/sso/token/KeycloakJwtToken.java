package com.jiuxi.security.sso.token;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * Keycloak JWT Token
 * 
 * 用于封装 JWT 认证信息的 Shiro Token
 * 
 * @author SSO Integration
 * @since 2.2.2
 */
public class KeycloakJwtToken implements AuthenticationToken {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * JWT Token 字符串
     */
    private final String token;
    
    /**
     * 用户标识（从 Token 中解析）
     */
    private String principal;
    
    /**
     * 构造函数
     * 
     * @param token JWT Token 字符串
     */
    public KeycloakJwtToken(String token) {
        this.token = token;
    }
    
    /**
     * 构造函数
     * 
     * @param token JWT Token 字符串
     * @param principal 用户标识
     */
    public KeycloakJwtToken(String token, String principal) {
        this.token = token;
        this.principal = principal;
    }
    
    /**
     * 获取主体（用户标识）
     * 
     * @return 用户标识
     */
    @Override
    public Object getPrincipal() {
        return principal != null ? principal : token;
    }
    
    /**
     * 获取凭证（JWT Token）
     * 
     * @return JWT Token
     */
    @Override
    public Object getCredentials() {
        return token;
    }
    
    /**
     * 获取 JWT Token 字符串
     * 
     * @return JWT Token
     */
    public String getToken() {
        return token;
    }
    
    /**
     * 设置用户标识
     * 
     * @param principal 用户标识
     */
    public void setPrincipal(String principal) {
        this.principal = principal;
    }
    
    @Override
    public String toString() {
        return "KeycloakJwtToken{" +
                "principal='" + principal + '\'' +
                ", token='" + (token != null ? token.substring(0, Math.min(token.length(), 20)) + "..." : "null") + '\'' +
                '}';
    }
}