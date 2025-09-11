package com.jiuxi.shared.security.config;

import com.jiuxi.shared.security.config.TokenService.TokenInfo;
import com.jiuxi.shared.security.config.TokenService.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 令牌存储管理器
 * 负责令牌的持久化存储、分布式同步和过期管理
 * 
 * @author Security Refactoring
 * @since Phase 4.2.3
 */
@Component
@ConditionalOnProperty(name = "ps.security.unified.enabled", havingValue = "true", matchIfMissing = false)
public class TokenStorageManager {

    private static final Logger logger = LoggerFactory.getLogger(TokenStorageManager.class);

    @Autowired
    private SecurityProperties securityProperties;
    
    @Autowired(required = false)
    private RedisTemplate<String, Object> redisTemplate;

    // Redis键前缀
    private static final String TOKEN_PREFIX = "ps:security:token:";
    private static final String USER_TOKENS_PREFIX = "ps:security:user_tokens:";
    private static final String BLACKLIST_PREFIX = "ps:security:blacklist:";
    private static final String REFRESH_TOKEN_PREFIX = "ps:security:refresh:";
    private static final String TOKEN_METADATA_PREFIX = "ps:security:token_meta:";

    /**
     * 存储令牌信息
     */
    public void storeToken(String tokenId, TokenInfo tokenInfo, UserInfo userInfo) {
        if (redisTemplate == null) {
            logger.warn("Redis template not available, skipping token storage");
            return;
        }

        try {
            String tokenKey = TOKEN_PREFIX + tokenId;
            String userTokensKey = USER_TOKENS_PREFIX + userInfo.getUserId();
            
            // 存储令牌信息
            redisTemplate.opsForValue().set(tokenKey, tokenInfo, 
                securityProperties.getJwt().getExpiration(), TimeUnit.SECONDS);
            
            // 将令牌ID添加到用户的令牌集合中
            redisTemplate.opsForSet().add(userTokensKey, tokenId);
            redisTemplate.expire(userTokensKey, 
                securityProperties.getJwt().getExpiration(), TimeUnit.SECONDS);
            
            // 存储令牌元数据
            TokenMetadata metadata = new TokenMetadata();
            metadata.setUserId(userInfo.getUserId());
            metadata.setUsername(userInfo.getUsername());
            metadata.setIssuedAt(tokenInfo.getIssuedAt());
            metadata.setExpiresAt(tokenInfo.getExpiresAt());
            
            String metadataKey = TOKEN_METADATA_PREFIX + tokenId;
            redisTemplate.opsForValue().set(metadataKey, metadata,
                securityProperties.getJwt().getExpiration(), TimeUnit.SECONDS);
            
            logger.debug("Token stored successfully for user: {}", userInfo.getUsername());

        } catch (Exception e) {
            logger.error("Error storing token for user: {}", userInfo.getUsername(), e);
        }
    }

    /**
     * 获取令牌信息
     */
    public TokenInfo getToken(String tokenId) {
        if (redisTemplate == null) {
            return null;
        }

        try {
            String tokenKey = TOKEN_PREFIX + tokenId;
            Object tokenObj = redisTemplate.opsForValue().get(tokenKey);
            
            if (tokenObj instanceof TokenInfo) {
                return (TokenInfo) tokenObj;
            }

        } catch (Exception e) {
            logger.error("Error retrieving token: {}", tokenId, e);
        }
        
        return null;
    }

    /**
     * 删除令牌
     */
    public void removeToken(String tokenId) {
        if (redisTemplate == null) {
            return;
        }

        try {
            // 获取令牌元数据以获取用户ID
            String metadataKey = TOKEN_METADATA_PREFIX + tokenId;
            Object metadataObj = redisTemplate.opsForValue().get(metadataKey);
            
            if (metadataObj instanceof TokenMetadata) {
                TokenMetadata metadata = (TokenMetadata) metadataObj;
                
                // 从用户令牌集合中移除
                String userTokensKey = USER_TOKENS_PREFIX + metadata.getUserId();
                redisTemplate.opsForSet().remove(userTokensKey, tokenId);
            }
            
            // 删除令牌信息
            String tokenKey = TOKEN_PREFIX + tokenId;
            redisTemplate.delete(tokenKey);
            
            // 删除元数据
            redisTemplate.delete(metadataKey);
            
            logger.debug("Token removed: {}", tokenId);

        } catch (Exception e) {
            logger.error("Error removing token: {}", tokenId, e);
        }
    }

    /**
     * 将令牌加入黑名单
     */
    public void blacklistToken(String tokenId, long ttlSeconds) {
        if (redisTemplate == null) {
            return;
        }

        try {
            String blacklistKey = BLACKLIST_PREFIX + tokenId;
            redisTemplate.opsForValue().set(blacklistKey, "true", ttlSeconds, TimeUnit.SECONDS);
            
            // 同时从正常存储中移除
            removeToken(tokenId);
            
            logger.debug("Token blacklisted: {}", tokenId);

        } catch (Exception e) {
            logger.error("Error blacklisting token: {}", tokenId, e);
        }
    }

    /**
     * 检查令牌是否在黑名单中
     */
    public boolean isTokenBlacklisted(String tokenId) {
        if (redisTemplate == null) {
            return false;
        }

        try {
            String blacklistKey = BLACKLIST_PREFIX + tokenId;
            return Boolean.TRUE.equals(redisTemplate.hasKey(blacklistKey));

        } catch (Exception e) {
            logger.error("Error checking token blacklist: {}", tokenId, e);
            return false;
        }
    }

    /**
     * 存储刷新令牌
     */
    public void storeRefreshToken(String refreshToken, String userId) {
        if (redisTemplate == null) {
            return;
        }

        try {
            String refreshKey = REFRESH_TOKEN_PREFIX + refreshToken;
            int refreshTtl = securityProperties.getJwt().getExpiration() * 10; // 10倍于访问令牌
            
            redisTemplate.opsForValue().set(refreshKey, userId, refreshTtl, TimeUnit.SECONDS);
            
            logger.debug("Refresh token stored for user: {}", userId);

        } catch (Exception e) {
            logger.error("Error storing refresh token for user: {}", userId, e);
        }
    }

    /**
     * 获取刷新令牌对应的用户ID
     */
    public String getRefreshTokenUserId(String refreshToken) {
        if (redisTemplate == null) {
            return null;
        }

        try {
            String refreshKey = REFRESH_TOKEN_PREFIX + refreshToken;
            Object userIdObj = redisTemplate.opsForValue().get(refreshKey);
            
            return userIdObj != null ? userIdObj.toString() : null;

        } catch (Exception e) {
            logger.error("Error retrieving refresh token user: {}", refreshToken, e);
            return null;
        }
    }

    /**
     * 删除刷新令牌
     */
    public void removeRefreshToken(String refreshToken) {
        if (redisTemplate == null) {
            return;
        }

        try {
            String refreshKey = REFRESH_TOKEN_PREFIX + refreshToken;
            redisTemplate.delete(refreshKey);
            
            logger.debug("Refresh token removed: {}", refreshToken);

        } catch (Exception e) {
            logger.error("Error removing refresh token: {}", refreshToken, e);
        }
    }

    /**
     * 获取用户的所有令牌
     */
    public Set<Object> getUserTokens(String userId) {
        if (redisTemplate == null) {
            return null;
        }

        try {
            String userTokensKey = USER_TOKENS_PREFIX + userId;
            return redisTemplate.opsForSet().members(userTokensKey);

        } catch (Exception e) {
            logger.error("Error retrieving user tokens for user: {}", userId, e);
            return null;
        }
    }

    /**
     * 撤销用户的所有令牌
     */
    public void revokeUserTokens(String userId) {
        if (redisTemplate == null) {
            return;
        }

        try {
            // 获取用户的所有令牌
            Set<Object> tokenIds = getUserTokens(userId);
            
            if (tokenIds != null && !tokenIds.isEmpty()) {
                for (Object tokenIdObj : tokenIds) {
                    String tokenId = tokenIdObj.toString();
                    
                    // 获取令牌信息以确定黑名单TTL
                    TokenInfo tokenInfo = getToken(tokenId);
                    if (tokenInfo != null && tokenInfo.getExpiresAt() != null) {
                        long ttl = (tokenInfo.getExpiresAt().getTime() - System.currentTimeMillis()) / 1000;
                        if (ttl > 0) {
                            blacklistToken(tokenId, ttl);
                        }
                    } else {
                        // 如果无法确定过期时间，使用默认TTL
                        blacklistToken(tokenId, securityProperties.getJwt().getExpiration());
                    }
                }
            }
            
            // 清除用户令牌集合
            String userTokensKey = USER_TOKENS_PREFIX + userId;
            redisTemplate.delete(userTokensKey);
            
            logger.info("All tokens revoked for user: {}", userId);

        } catch (Exception e) {
            logger.error("Error revoking user tokens for user: {}", userId, e);
        }
    }

    /**
     * 清理过期令牌
     * 这个方法可以由定时任务调用
     */
    public void cleanupExpiredTokens() {
        if (redisTemplate == null) {
            return;
        }

        try {
            // Redis会自动处理过期键，但我们可以主动清理一些相关数据
            logger.debug("Starting expired tokens cleanup");
            
            // 这里可以添加更复杂的清理逻辑
            // 比如清理孤立的用户令牌集合等
            
            logger.debug("Expired tokens cleanup completed");

        } catch (Exception e) {
            logger.error("Error during expired tokens cleanup", e);
        }
    }

    /**
     * 获取令牌统计信息
     */
    public TokenStatistics getTokenStatistics() {
        if (redisTemplate == null) {
            return new TokenStatistics();
        }

        try {
            TokenStatistics stats = new TokenStatistics();
            
            // 统计活跃令牌数量
            Set<String> tokenKeys = redisTemplate.keys(TOKEN_PREFIX + "*");
            stats.setActiveTokenCount(tokenKeys != null ? tokenKeys.size() : 0);
            
            // 统计黑名单令牌数量
            Set<String> blacklistKeys = redisTemplate.keys(BLACKLIST_PREFIX + "*");
            stats.setBlacklistedTokenCount(blacklistKeys != null ? blacklistKeys.size() : 0);
            
            // 统计刷新令牌数量
            Set<String> refreshKeys = redisTemplate.keys(REFRESH_TOKEN_PREFIX + "*");
            stats.setRefreshTokenCount(refreshKeys != null ? refreshKeys.size() : 0);
            
            return stats;

        } catch (Exception e) {
            logger.error("Error retrieving token statistics", e);
            return new TokenStatistics();
        }
    }

    // ================================ 内部类定义 ================================

    /**
     * 令牌元数据
     */
    public static class TokenMetadata {
        private String userId;
        private String username;
        private java.util.Date issuedAt;
        private java.util.Date expiresAt;

        // Getters and setters
        public String getUserId() { return userId; }
        public void setUserId(String userId) { this.userId = userId; }

        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }

        public java.util.Date getIssuedAt() { return issuedAt; }
        public void setIssuedAt(java.util.Date issuedAt) { this.issuedAt = issuedAt; }

        public java.util.Date getExpiresAt() { return expiresAt; }
        public void setExpiresAt(java.util.Date expiresAt) { this.expiresAt = expiresAt; }
    }

    /**
     * 令牌统计信息
     */
    public static class TokenStatistics {
        private int activeTokenCount;
        private int blacklistedTokenCount;
        private int refreshTokenCount;

        // Getters and setters
        public int getActiveTokenCount() { return activeTokenCount; }
        public void setActiveTokenCount(int activeTokenCount) { this.activeTokenCount = activeTokenCount; }

        public int getBlacklistedTokenCount() { return blacklistedTokenCount; }
        public void setBlacklistedTokenCount(int blacklistedTokenCount) { this.blacklistedTokenCount = blacklistedTokenCount; }

        public int getRefreshTokenCount() { return refreshTokenCount; }
        public void setRefreshTokenCount(int refreshTokenCount) { this.refreshTokenCount = refreshTokenCount; }
    }
}