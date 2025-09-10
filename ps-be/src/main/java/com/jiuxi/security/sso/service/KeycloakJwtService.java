package com.jiuxi.security.sso.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.jiuxi.security.sso.config.KeycloakSsoProperties;
import com.jiuxi.security.sso.principal.KeycloakUserPrincipal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.security.KeyFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Keycloak JWT 服务
 * 
 * @author SSO Integration
 * @since 2.2.2
 */
@Service
public class KeycloakJwtService {
    
    private static final Logger logger = LoggerFactory.getLogger(KeycloakJwtService.class);
    
    @Autowired
    private KeycloakSsoProperties properties;
    
    /**
     * Token 缓存
     */
    private final Map<String, KeycloakUserPrincipal> tokenCache = new ConcurrentHashMap<>();
    
    /**
     * JWT 验证器缓存
     */
    private volatile JWTVerifier jwtVerifier;
    
    @PostConstruct
    public void init() {
        logger.info("Initializing Keycloak JWT Service with realm: {}", properties.getRealm());
    }
    
    /**
     * 验证并解析 JWT Token
     * 
     * @param token JWT Token
     * @return 用户主体信息
     */
    public KeycloakUserPrincipal verifyAndParseToken(String token) {
        if (!StringUtils.hasText(token)) {
            throw new IllegalArgumentException("Token cannot be null or empty");
        }
        
        // 移除 Bearer 前缀
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        
        // 检查缓存
        if (properties.getJwt().isCacheParsedTokens()) {
            KeycloakUserPrincipal cachedPrincipal = tokenCache.get(token);
            if (cachedPrincipal != null && !cachedPrincipal.isTokenExpired()) {
                return cachedPrincipal;
            }
        }
        
        try {
            // 解码 JWT
            DecodedJWT decodedJWT = JWT.decode(token);
            
            // 验证 Token（这里简化处理，实际应该验证签名）
            validateToken(decodedJWT);
            
            // 解析用户信息
            KeycloakUserPrincipal principal = parseUserFromToken(decodedJWT, token);
            
            // 缓存结果
            if (properties.getJwt().isCacheParsedTokens()) {
                tokenCache.put(token, principal);
                
                // 简单的缓存清理策略
                if (tokenCache.size() > 1000) {
                    cleanExpiredTokens();
                }
            }
            
            return principal;
            
        } catch (JWTVerificationException e) {
            logger.error("JWT verification failed: {}", e.getMessage());
            throw new RuntimeException("Invalid JWT token", e);
        } catch (Exception e) {
            logger.error("Error parsing JWT token: {}", e.getMessage());
            throw new RuntimeException("Error parsing JWT token", e);
        }
    }
    
    /**
     * 验证 Token 基本信息
     */
    private void validateToken(DecodedJWT decodedJWT) {
        // 检查过期时间
        Date expiresAt = decodedJWT.getExpiresAt();
        if (expiresAt != null && expiresAt.before(new Date())) {
            throw new JWTVerificationException("Token has expired");
        }
        
        // 检查签发者
        String issuer = decodedJWT.getIssuer();
        String expectedIssuer = properties.getServerUrl() + "/realms/" + properties.getRealm();
        if (!expectedIssuer.equals(issuer)) {
            logger.warn("Token issuer mismatch. Expected: {}, Actual: {}", expectedIssuer, issuer);
        }
        
        // 检查受众
        List<String> audience = decodedJWT.getAudience();
        if (audience == null || !audience.contains(properties.getClientId())) {
            logger.warn("Token audience mismatch. Expected: {}, Actual: {}", properties.getClientId(), audience);
        }
    }
    
    /**
     * 从 Token 中解析用户信息
     */
    private KeycloakUserPrincipal parseUserFromToken(DecodedJWT decodedJWT, String token) {
        KeycloakUserPrincipal principal = new KeycloakUserPrincipal();
        
        // 基本信息
        principal.setUserId(decodedJWT.getSubject());
        principal.setUsername(decodedJWT.getClaim("preferred_username").asString());
        principal.setEmail(decodedJWT.getClaim("email").asString());
        principal.setName(decodedJWT.getClaim("name").asString());
        principal.setToken(token);
        
        // 过期时间
        Date expiresAt = decodedJWT.getExpiresAt();
        if (expiresAt != null) {
            principal.setExpiresAt(expiresAt.getTime() / 1000);
        }
        
        // 解析角色
        List<String> roles = extractRoles(decodedJWT);
        principal.setRoles(roles);
        
        // 解析权限（从角色映射或自定义声明）
        List<String> permissions = extractPermissions(decodedJWT, roles);
        principal.setPermissions(permissions);
        
        // 扩展属性
        Map<String, Object> attributes = new HashMap<>();
        decodedJWT.getClaims().forEach((key, claim) -> {
            if (!isStandardClaim(key)) {
                attributes.put(key, claim.as(Object.class));
            }
        });
        principal.setAttributes(attributes);
        
        return principal;
    }
    
    /**
     * 提取角色信息
     */
    @SuppressWarnings("unchecked")
    private List<String> extractRoles(DecodedJWT decodedJWT) {
        List<String> roles = new ArrayList<>();
        
        // 从 realm_access.roles 提取
        Map<String, Object> realmAccess = decodedJWT.getClaim("realm_access").asMap();
        if (realmAccess != null && realmAccess.containsKey("roles")) {
            List<String> realmRoles = (List<String>) realmAccess.get("roles");
            if (realmRoles != null) {
                roles.addAll(realmRoles);
            }
        }
        
        // 从 resource_access.{client_id}.roles 提取
        Map<String, Object> resourceAccess = decodedJWT.getClaim("resource_access").asMap();
        if (resourceAccess != null) {
            Map<String, Object> clientAccess = (Map<String, Object>) resourceAccess.get(properties.getClientId());
            if (clientAccess != null && clientAccess.containsKey("roles")) {
                List<String> clientRoles = (List<String>) clientAccess.get("roles");
                if (clientRoles != null) {
                    roles.addAll(clientRoles);
                }
            }
        }
        
        return roles;
    }
    
    /**
     * 提取权限信息
     */
    private List<String> extractPermissions(DecodedJWT decodedJWT, List<String> roles) {
        List<String> permissions = new ArrayList<>();
        
        // 从自定义声明提取权限
        List<String> customPermissions = decodedJWT.getClaim("permissions").asList(String.class);
        if (customPermissions != null) {
            permissions.addAll(customPermissions);
        }
        
        // 基于角色映射权限（简单示例）
        for (String role : roles) {
            switch (role) {
                case "admin":
                    permissions.addAll(Arrays.asList("user:read", "user:write", "user:delete", "system:admin"));
                    break;
                case "user":
                    permissions.addAll(Arrays.asList("user:read", "profile:update"));
                    break;
                case "manager":
                    permissions.addAll(Arrays.asList("user:read", "user:write", "report:read"));
                    break;
            }
        }
        
        return permissions;
    }
    
    /**
     * 检查是否为标准 JWT 声明
     */
    private boolean isStandardClaim(String claimName) {
        return Arrays.asList(
            "iss", "sub", "aud", "exp", "nbf", "iat", "jti",
            "preferred_username", "email", "name", "given_name", "family_name",
            "realm_access", "resource_access", "scope", "email_verified",
            "acr", "allowed-origins", "azp", "session_state", "typ"
        ).contains(claimName);
    }
    
    /**
     * 清理过期的 Token 缓存
     */
    private void cleanExpiredTokens() {
        tokenCache.entrySet().removeIf(entry -> entry.getValue().isTokenExpired());
    }
    
    /**
     * 清空 Token 缓存
     */
    public void clearTokenCache() {
        tokenCache.clear();
    }
}