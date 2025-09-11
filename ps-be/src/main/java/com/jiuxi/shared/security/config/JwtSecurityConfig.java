package com.jiuxi.shared.security.config;

import com.auth0.jwt.algorithms.Algorithm;
import com.jiuxi.security.sso.service.KeycloakJwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.ConcurrentHashMap;

/**
 * JWT安全配置类
 * 负责JWT相关的Bean配置和缓存管理
 * 
 * @author Security Refactoring
 * @since Phase 4.2.2
 */
@Configuration
@EnableConfigurationProperties(SecurityProperties.class)
@ConditionalOnProperty(name = "ps.security.unified.enabled", havingValue = "true", matchIfMissing = false)
public class JwtSecurityConfig {

    @Autowired
    private SecurityProperties securityProperties;
    
    @Autowired(required = false)
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * JWT签名算法配置
     */
    @Bean
    public Algorithm jwtAlgorithm() {
        // 使用HMAC256算法，生产环境应该使用更复杂的密钥
        return Algorithm.HMAC256(securityProperties.getJwt().getSecret());
    }

    /**
     * JWT缓存服务
     * 优先使用Redis，如果不可用则使用内存缓存
     */
    @Bean
    public JwtCacheService jwtCacheService() {
        if (redisTemplate != null && securityProperties.getJwt().isCacheEnabled()) {
            return new RedisJwtCacheService(redisTemplate, securityProperties);
        }
        return new MemoryJwtCacheService(securityProperties);
    }

    /**
     * Keycloak JWT服务（当Keycloak SSO启用时）
     */
    @Bean
    @ConditionalOnProperty(name = "ps.security.keycloak.enabled", havingValue = "true")
    public KeycloakJwtService keycloakJwtService() {
        return new KeycloakJwtService();
    }

    /**
     * JWT缓存服务接口
     */
    public interface JwtCacheService {
        void put(String key, Object value);
        Object get(String key);
        void remove(String key);
        boolean exists(String key);
        void clear();
    }

    /**
     * Redis实现的JWT缓存服务
     */
    public static class RedisJwtCacheService implements JwtCacheService {
        private final RedisTemplate<String, Object> redisTemplate;
        private final SecurityProperties securityProperties;
        private final String cachePrefix = "jwt:cache:";

        public RedisJwtCacheService(RedisTemplate<String, Object> redisTemplate, 
                                   SecurityProperties securityProperties) {
            this.redisTemplate = redisTemplate;
            this.securityProperties = securityProperties;
        }

        @Override
        public void put(String key, Object value) {
            String cacheKey = cachePrefix + key;
            redisTemplate.opsForValue().set(cacheKey, value, 
                securityProperties.getJwt().getCacheExpiration());
        }

        @Override
        public Object get(String key) {
            String cacheKey = cachePrefix + key;
            return redisTemplate.opsForValue().get(cacheKey);
        }

        @Override
        public void remove(String key) {
            String cacheKey = cachePrefix + key;
            redisTemplate.delete(cacheKey);
        }

        @Override
        public boolean exists(String key) {
            String cacheKey = cachePrefix + key;
            return Boolean.TRUE.equals(redisTemplate.hasKey(cacheKey));
        }

        @Override
        public void clear() {
            // 清空所有JWT缓存
            redisTemplate.delete(redisTemplate.keys(cachePrefix + "*"));
        }
    }

    /**
     * 内存实现的JWT缓存服务
     */
    public static class MemoryJwtCacheService implements JwtCacheService {
        private final ConcurrentHashMap<String, CacheItem> cache = new ConcurrentHashMap<>();
        private final SecurityProperties securityProperties;

        public MemoryJwtCacheService(SecurityProperties securityProperties) {
            this.securityProperties = securityProperties;
        }

        @Override
        public void put(String key, Object value) {
            long expireTime = System.currentTimeMillis() + 
                (securityProperties.getJwt().getCacheExpiration() * 1000L);
            cache.put(key, new CacheItem(value, expireTime));
        }

        @Override
        public Object get(String key) {
            CacheItem item = cache.get(key);
            if (item == null) {
                return null;
            }
            
            // 检查是否过期
            if (System.currentTimeMillis() > item.expireTime) {
                cache.remove(key);
                return null;
            }
            
            return item.value;
        }

        @Override
        public void remove(String key) {
            cache.remove(key);
        }

        @Override
        public boolean exists(String key) {
            CacheItem item = cache.get(key);
            if (item == null) {
                return false;
            }
            
            // 检查是否过期
            if (System.currentTimeMillis() > item.expireTime) {
                cache.remove(key);
                return false;
            }
            
            return true;
        }

        @Override
        public void clear() {
            cache.clear();
        }

        /**
         * 缓存项
         */
        private static class CacheItem {
            final Object value;
            final long expireTime;

            CacheItem(Object value, long expireTime) {
                this.value = value;
                this.expireTime = expireTime;
            }
        }
    }

    /**
     * JWT工具类
     */
    @Bean
    public JwtUtils jwtUtils() {
        return new JwtUtils(jwtAlgorithm(), securityProperties, jwtCacheService());
    }

    /**
     * JWT工具实现类
     */
    public static class JwtUtils {
        private final Algorithm algorithm;
        private final SecurityProperties securityProperties;
        private final JwtCacheService cacheService;

        public JwtUtils(Algorithm algorithm, SecurityProperties securityProperties, 
                       JwtCacheService cacheService) {
            this.algorithm = algorithm;
            this.securityProperties = securityProperties;
            this.cacheService = cacheService;
        }

        public Algorithm getAlgorithm() {
            return algorithm;
        }

        public SecurityProperties getSecurityProperties() {
            return securityProperties;
        }

        public JwtCacheService getCacheService() {
            return cacheService;
        }

        /**
         * 生成缓存键
         */
        public String generateCacheKey(String token) {
            // 使用token的hash作为缓存键，避免存储完整token
            return "token:" + Math.abs(token.hashCode());
        }

        /**
         * 缓存token解析结果
         */
        public void cacheTokenResult(String token, Object result) {
            if (securityProperties.getJwt().isCacheEnabled()) {
                String cacheKey = generateCacheKey(token);
                cacheService.put(cacheKey, result);
            }
        }

        /**
         * 获取缓存的token解析结果
         */
        public Object getCachedTokenResult(String token) {
            if (securityProperties.getJwt().isCacheEnabled()) {
                String cacheKey = generateCacheKey(token);
                return cacheService.get(cacheKey);
            }
            return null;
        }

        /**
         * 移除缓存的token结果
         */
        public void removeCachedTokenResult(String token) {
            if (securityProperties.getJwt().isCacheEnabled()) {
                String cacheKey = generateCacheKey(token);
                cacheService.remove(cacheKey);
            }
        }
    }
}