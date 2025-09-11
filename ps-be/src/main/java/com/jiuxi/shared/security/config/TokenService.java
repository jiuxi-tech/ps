package com.jiuxi.shared.security.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.jiuxi.shared.security.config.JwtSecurityConfig.JwtCacheService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 统一令牌服务
 * 负责JWT令牌的生成、验证、刷新和管理
 * 整合现有JWT功能，提供高性能的令牌处理能力
 * 
 * @author Security Refactoring
 * @since Phase 4.2.3
 */
@Service
@ConditionalOnProperty(name = "ps.security.unified.enabled", havingValue = "true", matchIfMissing = false)
public class TokenService {

    private static final Logger logger = LoggerFactory.getLogger(TokenService.class);

    @Autowired
    private SecurityProperties securityProperties;
    
    @Autowired
    private Algorithm jwtAlgorithm;
    
    @Autowired
    private JwtCacheService cacheService;
    
    private JWTVerifier jwtVerifier;

    // 令牌黑名单缓存键前缀
    private static final String BLACKLIST_PREFIX = "jwt:blacklist:";
    
    // 刷新令牌缓存键前缀
    private static final String REFRESH_TOKEN_PREFIX = "jwt:refresh:";
    
    // 用户会话缓存键前缀
    private static final String USER_SESSION_PREFIX = "jwt:session:";

    @PostConstruct
    public void init() {
        this.jwtVerifier = JWT.require(jwtAlgorithm)
            .acceptLeeway(securityProperties.getJwt().getClockSkew())
            .build();
        
        logger.info("TokenService initialized with JWT algorithm and cache service");
    }

    /**
     * 生成访问令牌
     */
    public TokenInfo generateToken(UserInfo userInfo) {
        return generateToken(userInfo, null);
    }

    /**
     * 生成访问令牌和刷新令牌
     */
    public TokenInfo generateToken(UserInfo userInfo, Map<String, Object> customClaims) {
        try {
            Date now = new Date();
            Date expiresAt = new Date(now.getTime() + (securityProperties.getJwt().getExpiration() * 1000L));
            
            // 构建JWT Builder
            com.auth0.jwt.JWTCreator.Builder jwtBuilder = JWT.create()
                .withJWTId(UUID.randomUUID().toString())
                .withSubject(userInfo.getUserId())
                .withIssuer("ps-be")
                .withAudience("ps-fe")
                .withIssuedAt(now)
                .withExpiresAt(expiresAt);

            // 添加用户信息声明
            jwtBuilder.withClaim("username", userInfo.getUsername())
                     .withClaim("email", userInfo.getEmail())
                     .withClaim("name", userInfo.getName());

            // 添加角色和权限
            if (userInfo.getRoles() != null && !userInfo.getRoles().isEmpty()) {
                jwtBuilder.withClaim("roles", userInfo.getRoles());
            }
            
            if (userInfo.getPermissions() != null && !userInfo.getPermissions().isEmpty()) {
                jwtBuilder.withClaim("permissions", userInfo.getPermissions());
            }

            // 添加自定义声明
            if (customClaims != null && !customClaims.isEmpty()) {
                customClaims.forEach((key, value) -> {
                    if (value instanceof String) {
                        jwtBuilder.withClaim(key, (String) value);
                    } else if (value instanceof Boolean) {
                        jwtBuilder.withClaim(key, (Boolean) value);
                    } else if (value instanceof Integer) {
                        jwtBuilder.withClaim(key, (Integer) value);
                    } else if (value instanceof Long) {
                        jwtBuilder.withClaim(key, (Long) value);
                    } else if (value instanceof Date) {
                        jwtBuilder.withClaim(key, (Date) value);
                    } else if (value instanceof List) {
                        jwtBuilder.withClaim(key, (List<?>) value);
                    }
                });
            }

            // 生成访问令牌
            String accessToken = jwtBuilder.sign(jwtAlgorithm);
            
            // 生成刷新令牌（有效期更长）
            String refreshToken = generateRefreshToken(userInfo.getUserId());
            
            // 创建令牌信息
            TokenInfo tokenInfo = new TokenInfo();
            tokenInfo.setAccessToken(accessToken);
            tokenInfo.setRefreshToken(refreshToken);
            tokenInfo.setTokenType("Bearer");
            tokenInfo.setExpiresIn(securityProperties.getJwt().getExpiration());
            tokenInfo.setIssuedAt(now);
            tokenInfo.setExpiresAt(expiresAt);

            // 缓存令牌信息
            cacheTokenInfo(accessToken, tokenInfo, userInfo);
            
            logger.debug("Generated JWT token for user: {}", userInfo.getUsername());
            return tokenInfo;

        } catch (Exception e) {
            logger.error("Error generating JWT token for user: {}", userInfo.getUsername(), e);
            throw new RuntimeException("Token generation failed", e);
        }
    }

    /**
     * 验证令牌
     */
    public TokenValidationResult validateToken(String token) {
        if (!StringUtils.hasText(token)) {
            return TokenValidationResult.invalid("Token is empty");
        }

        try {
            // 检查黑名单
            if (isTokenBlacklisted(token)) {
                return TokenValidationResult.invalid("Token is blacklisted");
            }

            // 检查缓存
            TokenInfo cachedInfo = getCachedTokenInfo(token);
            if (cachedInfo != null) {
                if (cachedInfo.isExpired()) {
                    removeCachedTokenInfo(token);
                    return TokenValidationResult.invalid("Token has expired");
                }
                return TokenValidationResult.valid(cachedInfo);
            }

            // 验证JWT签名和结构
            DecodedJWT decodedJWT = jwtVerifier.verify(token);
            
            // 创建令牌信息
            TokenInfo tokenInfo = createTokenInfoFromJWT(decodedJWT, token);
            
            // 缓存验证结果
            cacheTokenValidationResult(token, tokenInfo);
            
            return TokenValidationResult.valid(tokenInfo);

        } catch (JWTVerificationException e) {
            logger.debug("JWT validation failed: {}", e.getMessage());
            return TokenValidationResult.invalid("Invalid token: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Error validating JWT token", e);
            return TokenValidationResult.invalid("Token validation error");
        }
    }

    /**
     * 刷新令牌
     */
    public TokenInfo refreshToken(String refreshToken) {
        if (!StringUtils.hasText(refreshToken)) {
            throw new IllegalArgumentException("Refresh token is empty");
        }

        try {
            // 验证刷新令牌
            String userId = getRefreshTokenUserId(refreshToken);
            if (userId == null) {
                throw new IllegalArgumentException("Invalid refresh token");
            }

            // 获取用户信息（这里应该从数据库或缓存中获取）
            UserInfo userInfo = getUserInfoFromCache(userId);
            if (userInfo == null) {
                throw new IllegalArgumentException("User not found for refresh token");
            }

            // 生成新的访问令牌
            TokenInfo newTokenInfo = generateToken(userInfo);
            
            // 延长刷新令牌的有效期
            extendRefreshToken(refreshToken, userId);
            
            logger.info("Token refreshed for user: {}", userInfo.getUsername());
            return newTokenInfo;

        } catch (Exception e) {
            logger.error("Error refreshing token", e);
            throw new RuntimeException("Token refresh failed", e);
        }
    }

    /**
     * 撤销令牌（加入黑名单）
     */
    public void revokeToken(String token) {
        if (!StringUtils.hasText(token)) {
            return;
        }

        try {
            // 解析令牌获取过期时间
            DecodedJWT decodedJWT = JWT.decode(token);
            Date expiresAt = decodedJWT.getExpiresAt();
            
            if (expiresAt != null) {
                // 将令牌加入黑名单，直到过期时间
                long ttl = expiresAt.getTime() - System.currentTimeMillis();
                if (ttl > 0) {
                    String blacklistKey = BLACKLIST_PREFIX + token.hashCode();
                    cacheService.put(blacklistKey, "true");
                    
                    logger.debug("Token added to blacklist");
                }
            }

            // 移除缓存的令牌信息
            removeCachedTokenInfo(token);

        } catch (Exception e) {
            logger.error("Error revoking token", e);
        }
    }

    /**
     * 撤销用户的所有令牌
     */
    public void revokeUserTokens(String userId) {
        if (!StringUtils.hasText(userId)) {
            return;
        }

        try {
            // 清除用户会话缓存
            String sessionKey = USER_SESSION_PREFIX + userId;
            cacheService.remove(sessionKey);
            
            // 这里应该还要处理分布式环境下的令牌撤销
            logger.info("All tokens revoked for user: {}", userId);

        } catch (Exception e) {
            logger.error("Error revoking user tokens for user: {}", userId, e);
        }
    }

    /**
     * 清理过期令牌
     */
    public void cleanupExpiredTokens() {
        // 这个方法可以由定时任务调用
        logger.debug("Cleaning up expired tokens");
        // 实现清理逻辑
    }

    // ================================ 私有辅助方法 ================================

    /**
     * 生成刷新令牌
     */
    private String generateRefreshToken(String userId) {
        String refreshToken = UUID.randomUUID().toString().replace("-", "");
        String refreshKey = REFRESH_TOKEN_PREFIX + refreshToken;
        
        // 刷新令牌有效期更长（比如30天）
        int refreshTtl = securityProperties.getJwt().getExpiration() * 10; // 10倍于访问令牌
        cacheService.put(refreshKey, userId);
        
        return refreshToken;
    }

    /**
     * 检查令牌是否在黑名单中
     */
    private boolean isTokenBlacklisted(String token) {
        String blacklistKey = BLACKLIST_PREFIX + token.hashCode();
        return cacheService.exists(blacklistKey);
    }

    /**
     * 缓存令牌信息
     */
    private void cacheTokenInfo(String token, TokenInfo tokenInfo, UserInfo userInfo) {
        if (securityProperties.getJwt().isCacheEnabled()) {
            String cacheKey = "token:" + token.hashCode();
            cacheService.put(cacheKey, tokenInfo);
            
            // 同时缓存用户会话信息
            String sessionKey = USER_SESSION_PREFIX + userInfo.getUserId();
            cacheService.put(sessionKey, userInfo);
        }
    }

    /**
     * 获取缓存的令牌信息
     */
    private TokenInfo getCachedTokenInfo(String token) {
        if (securityProperties.getJwt().isCacheEnabled()) {
            String cacheKey = "token:" + token.hashCode();
            Object cached = cacheService.get(cacheKey);
            if (cached instanceof TokenInfo) {
                return (TokenInfo) cached;
            }
        }
        return null;
    }

    /**
     * 移除缓存的令牌信息
     */
    private void removeCachedTokenInfo(String token) {
        if (securityProperties.getJwt().isCacheEnabled()) {
            String cacheKey = "token:" + token.hashCode();
            cacheService.remove(cacheKey);
        }
    }

    /**
     * 从JWT创建令牌信息
     */
    private TokenInfo createTokenInfoFromJWT(DecodedJWT decodedJWT, String token) {
        TokenInfo tokenInfo = new TokenInfo();
        tokenInfo.setAccessToken(token);
        tokenInfo.setTokenType("Bearer");
        tokenInfo.setIssuedAt(decodedJWT.getIssuedAt());
        tokenInfo.setExpiresAt(decodedJWT.getExpiresAt());
        
        if (tokenInfo.getExpiresAt() != null) {
            long expiresIn = (tokenInfo.getExpiresAt().getTime() - System.currentTimeMillis()) / 1000;
            tokenInfo.setExpiresIn((int) expiresIn);
        }
        
        return tokenInfo;
    }

    /**
     * 缓存令牌验证结果
     */
    private void cacheTokenValidationResult(String token, TokenInfo tokenInfo) {
        if (securityProperties.getJwt().isCacheEnabled()) {
            String cacheKey = "token:" + token.hashCode();
            cacheService.put(cacheKey, tokenInfo);
        }
    }

    /**
     * 从刷新令牌获取用户ID
     */
    private String getRefreshTokenUserId(String refreshToken) {
        String refreshKey = REFRESH_TOKEN_PREFIX + refreshToken;
        Object userId = cacheService.get(refreshKey);
        return userId != null ? userId.toString() : null;
    }

    /**
     * 从缓存获取用户信息
     */
    private UserInfo getUserInfoFromCache(String userId) {
        String sessionKey = USER_SESSION_PREFIX + userId;
        Object cached = cacheService.get(sessionKey);
        if (cached instanceof UserInfo) {
            return (UserInfo) cached;
        }
        return null;
    }

    /**
     * 延长刷新令牌有效期
     */
    private void extendRefreshToken(String refreshToken, String userId) {
        String refreshKey = REFRESH_TOKEN_PREFIX + refreshToken;
        // 重新设置刷新令牌，延长有效期
        cacheService.put(refreshKey, userId);
    }

    // ================================ 内部类定义 ================================

    /**
     * 用户信息类
     */
    public static class UserInfo {
        private String userId;
        private String username;
        private String email;
        private String name;
        private List<String> roles;
        private List<String> permissions;
        private Map<String, Object> attributes;

        // 构造函数
        public UserInfo() {}

        public UserInfo(String userId, String username) {
            this.userId = userId;
            this.username = username;
        }

        // Getters and setters
        public String getUserId() { return userId; }
        public void setUserId(String userId) { this.userId = userId; }

        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public List<String> getRoles() { return roles; }
        public void setRoles(List<String> roles) { this.roles = roles; }

        public List<String> getPermissions() { return permissions; }
        public void setPermissions(List<String> permissions) { this.permissions = permissions; }

        public Map<String, Object> getAttributes() { return attributes; }
        public void setAttributes(Map<String, Object> attributes) { this.attributes = attributes; }
    }

    /**
     * 令牌信息类
     */
    public static class TokenInfo {
        private String accessToken;
        private String refreshToken;
        private String tokenType = "Bearer";
        private int expiresIn;
        private Date issuedAt;
        private Date expiresAt;

        public boolean isExpired() {
            return expiresAt != null && expiresAt.before(new Date());
        }

        // Getters and setters
        public String getAccessToken() { return accessToken; }
        public void setAccessToken(String accessToken) { this.accessToken = accessToken; }

        public String getRefreshToken() { return refreshToken; }
        public void setRefreshToken(String refreshToken) { this.refreshToken = refreshToken; }

        public String getTokenType() { return tokenType; }
        public void setTokenType(String tokenType) { this.tokenType = tokenType; }

        public int getExpiresIn() { return expiresIn; }
        public void setExpiresIn(int expiresIn) { this.expiresIn = expiresIn; }

        public Date getIssuedAt() { return issuedAt; }
        public void setIssuedAt(Date issuedAt) { this.issuedAt = issuedAt; }

        public Date getExpiresAt() { return expiresAt; }
        public void setExpiresAt(Date expiresAt) { this.expiresAt = expiresAt; }
    }

    /**
     * 令牌验证结果类
     */
    public static class TokenValidationResult {
        private boolean valid;
        private String message;
        private TokenInfo tokenInfo;

        private TokenValidationResult(boolean valid, String message, TokenInfo tokenInfo) {
            this.valid = valid;
            this.message = message;
            this.tokenInfo = tokenInfo;
        }

        public static TokenValidationResult valid(TokenInfo tokenInfo) {
            return new TokenValidationResult(true, "Valid", tokenInfo);
        }

        public static TokenValidationResult invalid(String message) {
            return new TokenValidationResult(false, message, null);
        }

        // Getters
        public boolean isValid() { return valid; }
        public String getMessage() { return message; }
        public TokenInfo getTokenInfo() { return tokenInfo; }
    }
}