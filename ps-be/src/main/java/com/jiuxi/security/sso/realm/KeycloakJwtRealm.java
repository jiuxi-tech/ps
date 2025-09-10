package com.jiuxi.security.sso.realm;

import com.jiuxi.security.sso.principal.KeycloakUserPrincipal;
import com.jiuxi.security.sso.service.KeycloakJwtService;
import com.jiuxi.security.sso.token.KeycloakJwtToken;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;
import java.util.HashSet;

/**
 * Keycloak JWT Realm
 * 
 * 负责验证 Keycloak JWT Token 并提供用户认证和授权信息
 * 
 * @author SSO Integration
 * @since 2.2.2
 */
public class KeycloakJwtRealm extends AuthorizingRealm {
    
    private static final Logger logger = LoggerFactory.getLogger(KeycloakJwtRealm.class);
    
    @Autowired
    private KeycloakJwtService keycloakJwtService;
    
    public KeycloakJwtRealm() {
        super();
        // 设置支持的 Token 类型
        setAuthenticationTokenClass(KeycloakJwtToken.class);
        // 禁用缓存，因为 JWT 是无状态的
        setAuthenticationCachingEnabled(false);
        setAuthorizationCachingEnabled(false);
    }
    
    @Override
    public String getName() {
        return "KeycloakJwtRealm";
    }
    
    /**
     * 支持的 Token 类型检查
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof KeycloakJwtToken;
    }
    
    /**
     * 认证方法
     * 
     * @param authenticationToken 认证 Token
     * @return 认证信息
     * @throws AuthenticationException 认证异常
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) 
            throws AuthenticationException {
        
        logger.debug("Performing Keycloak JWT authentication");
        
        KeycloakJwtToken jwtToken = (KeycloakJwtToken) authenticationToken;
        String token = jwtToken.getToken();
        
        if (token == null || token.trim().isEmpty()) {
            throw new AuthenticationException("JWT token is null or empty");
        }
        
        try {
            // 验证并解析 JWT Token
            KeycloakUserPrincipal principal = keycloakJwtService.verifyAndParseToken(token);
            
            if (principal == null) {
                throw new AuthenticationException("Failed to parse user information from JWT token");
            }
            
            logger.debug("Successfully authenticated user: {} ({})", principal.getUsername(), principal.getUserId());
            
            // 返回认证信息
            return new SimpleAuthenticationInfo(
                principal,           // principal
                token,              // credentials
                getName()           // realm name
            );
            
        } catch (Exception e) {
            logger.error("JWT authentication failed: {}", e.getMessage());
            throw new AuthenticationException("JWT authentication failed: " + e.getMessage(), e);
        }
    }
    
    /**
     * 授权方法
     * 
     * @param principalCollection 主体集合
     * @return 授权信息
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        
        logger.debug("Performing Keycloak JWT authorization");
        
        // 获取用户主体
        KeycloakUserPrincipal principal = (KeycloakUserPrincipal) principalCollection.getPrimaryPrincipal();
        
        if (principal == null) {
            logger.warn("No principal found for authorization");
            return null;
        }
        
        logger.debug("Authorizing user: {} with roles: {} and permissions: {}", 
                    principal.getUsername(), principal.getRoles(), principal.getPermissions());
        
        // 创建授权信息
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        
        // 添加角色
        if (principal.getRoles() != null && !principal.getRoles().isEmpty()) {
            Set<String> roles = new HashSet<>(principal.getRoles());
            authorizationInfo.setRoles(roles);
            logger.debug("Added roles: {}", roles);
        }
        
        // 添加权限
        if (principal.getPermissions() != null && !principal.getPermissions().isEmpty()) {
            Set<String> permissions = new HashSet<>(principal.getPermissions());
            authorizationInfo.setStringPermissions(permissions);
            logger.debug("Added permissions: {}", permissions);
        }
        
        return authorizationInfo;
    }
    
    /**
     * 清除指定用户的授权信息缓存
     * @param principals 用户主体集合
     */
    @Override
    protected void clearCachedAuthorizationInfo(PrincipalCollection principals) {
        if (principals != null && !principals.isEmpty()) {
            Object principal = principals.getPrimaryPrincipal();
            if (principal instanceof KeycloakUserPrincipal) {
                KeycloakUserPrincipal userPrincipal = (KeycloakUserPrincipal) principal;
                logger.debug("Clearing cached authorization info for user: {}", userPrincipal.getUsername());
            }
        }
        super.clearCachedAuthorizationInfo(principals);
    }
    
    /**
     * 清除所有缓存
     */
    public void clearAllCache() {
        logger.debug("Clearing all cached authentication and authorization info");
        getAuthenticationCache().clear();
        getAuthorizationCache().clear();
    }
}