package com.jiuxi.shared.security.authentication;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * JWT认证令牌
 * 用于Spring Security认证流程中的JWT令牌封装
 * 
 * @author Security Refactoring
 * @since Phase 4.2.2
 */
public class JwtAuthenticationToken extends AbstractAuthenticationToken {

    private final String token;
    private final Object principal;
    private final Object credentials;

    /**
     * 创建未认证的JWT令牌（用于认证前）
     */
    public JwtAuthenticationToken(String token) {
        super(null);
        this.token = token;
        this.principal = null;
        this.credentials = token;
        setAuthenticated(false);
    }

    /**
     * 创建已认证的JWT令牌（用于认证后）
     */
    public JwtAuthenticationToken(String token, Object principal, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.token = token;
        this.principal = principal;
        this.credentials = token;
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return credentials;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }

    /**
     * 获取JWT令牌字符串
     */
    public String getToken() {
        return token;
    }

    @Override
    public void eraseCredentials() {
        // JWT token通常不需要清除，因为它本身就是凭证
        super.eraseCredentials();
    }
}