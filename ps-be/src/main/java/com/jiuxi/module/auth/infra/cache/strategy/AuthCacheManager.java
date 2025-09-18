package com.jiuxi.module.auth.infra.cache.strategy;

import com.jiuxi.module.auth.infra.cache.config.CacheConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 权限模块缓存管理器
 * 实现缓存的自动刷新和失效机制
 * 
 * @author DDD Refactor
 * @date 2025-09-18
 */
@Component
public class AuthCacheManager {
    
    private static final Logger logger = LoggerFactory.getLogger(AuthCacheManager.class);
    
    @Autowired
    private CacheManager cacheManager;
    
    @Autowired
    private CacheConfig cacheConfig;
    
    // 缓存依赖关系映射
    private final ConcurrentHashMap<String, Set<String>> dependencyMap = new ConcurrentHashMap<>();
    
    public AuthCacheManager() {
        initializeDependencies();
    }
    
    /**
     * 初始化缓存依赖关系
     */
    private void initializeDependencies() {
        // 角色变更时需要清理的缓存
        addDependency("role", Set.of("rolePermissions", "userPermissions"));
        
        // 权限变更时需要清理的缓存
        addDependency("permission", Set.of("rolePermissions", "userPermissions"));
        
        // 菜单变更时需要清理的缓存
        addDependency("menu", Set.of("userMenus", "roleMenus"));
        
        // 用户角色关系变更时需要清理的缓存
        addDependency("userRole", Set.of("userPermissions", "userMenus"));
    }
    
    /**
     * 添加缓存依赖关系
     */
    public void addDependency(String triggerEntity, Set<String> dependentCaches) {
        dependencyMap.put(triggerEntity, dependentCaches);
        logger.debug("添加缓存依赖关系: {} -> {}", triggerEntity, dependentCaches);
    }
    
    /**
     * 安全获取缓存值
     */
    public <T> T get(String cacheName, String key, Class<T> type) {
        if (!cacheConfig.isEnabled()) {
            return null;
        }
        
        try {
            Cache cache = getCache(cacheName);
            if (cache != null) {
                Cache.ValueWrapper wrapper = cache.get(key);
                if (wrapper != null) {
                    Object value = wrapper.get();
                    if (type.isInstance(value)) {
                        logger.debug("缓存命中: cache={}, key={}", cacheName, key);
                        return type.cast(value);
                    }
                }
            }
            logger.debug("缓存未命中: cache={}, key={}", cacheName, key);
            return null;
        } catch (Exception e) {
            logger.warn("获取缓存值失败: cache={}, key={}", cacheName, key, e);
            return null;
        }
    }
    
    /**
     * 安全获取缓存值，如果不存在则通过valueLoader加载
     */
    public <T> T get(String cacheName, String key, Callable<T> valueLoader) {
        if (!cacheConfig.isEnabled()) {
            try {
                return valueLoader.call();
            } catch (Exception e) {
                logger.error("加载数据失败: cache={}, key={}", cacheName, key, e);
                return null;
            }
        }
        
        try {
            Cache cache = getCache(cacheName);
            if (cache != null) {
                T value = cache.get(key, valueLoader);
                logger.debug("缓存获取/加载: cache={}, key={}", cacheName, key);
                return value;
            } else {
                return valueLoader.call();
            }
        } catch (Exception e) {
            logger.warn("获取/加载缓存值失败: cache={}, key={}", cacheName, key, e);
            try {
                return valueLoader.call();
            } catch (Exception ex) {
                logger.error("加载数据失败: cache={}, key={}", cacheName, key, ex);
                return null;
            }
        }
    }
    
    /**
     * 缓存值
     */
    public void put(String cacheName, String key, Object value) {
        if (!cacheConfig.isEnabled()) {
            return;
        }
        
        try {
            Cache cache = getCache(cacheName);
            if (cache != null) {
                cache.put(key, value);
                logger.debug("缓存存储: cache={}, key={}", cacheName, key);
            }
        } catch (Exception e) {
            logger.warn("缓存存储失败: cache={}, key={}", cacheName, key, e);
        }
    }
    
    /**
     * 删除指定缓存项
     */
    public void evict(String cacheName, String key) {
        try {
            Cache cache = getCache(cacheName);
            if (cache != null) {
                cache.evict(key);
                logger.debug("缓存清除: cache={}, key={}", cacheName, key);
            }
        } catch (Exception e) {
            logger.warn("缓存清除失败: cache={}, key={}", cacheName, key, e);
        }
    }
    
    /**
     * 清空指定缓存
     */
    public void clear(String cacheName) {
        try {
            Cache cache = getCache(cacheName);
            if (cache != null) {
                cache.clear();
                logger.info("缓存清空: cache={}", cacheName);
            }
        } catch (Exception e) {
            logger.warn("缓存清空失败: cache={}", cacheName, e);
        }
    }
    
    /**
     * 根据实体变更清理相关缓存
     */
    public void evictByEntity(String entityType, String entityId) {
        Set<String> dependentCaches = dependencyMap.get(entityType);
        if (dependentCaches != null) {
            logger.info("实体变更触发缓存清理: entityType={}, entityId={}, 影响缓存={}", 
                    entityType, entityId, dependentCaches);
            
            for (String cacheName : dependentCaches) {
                clear(cacheName);
            }
        }
    }
    
    /**
     * 根据租户清理缓存
     */
    public void evictByTenant(String tenantId) {
        logger.info("租户缓存清理: tenantId={}", tenantId);
        
        // 清理所有与租户相关的缓存
        Collection<String> cacheNames = cacheManager.getCacheNames();
        for (String cacheName : cacheNames) {
            if (isAuthRelatedCache(cacheName)) {
                clear(cacheName);
            }
        }
    }
    
    /**
     * 清理所有权限相关缓存
     */
    public void evictAllAuthCaches() {
        logger.info("清理所有权限相关缓存");
        
        Collection<String> cacheNames = cacheManager.getCacheNames();
        for (String cacheName : cacheNames) {
            if (isAuthRelatedCache(cacheName)) {
                clear(cacheName);
            }
        }
    }
    
    /**
     * 获取缓存实例
     */
    private Cache getCache(String cacheName) {
        try {
            return cacheManager.getCache(cacheName);
        } catch (Exception e) {
            logger.warn("获取缓存实例失败: cache={}", cacheName, e);
            return null;
        }
    }
    
    /**
     * 检查是否是权限相关缓存
     */
    private boolean isAuthRelatedCache(String cacheName) {
        return cacheName.contains("role") || 
               cacheName.contains("permission") || 
               cacheName.contains("menu") || 
               cacheName.contains("user") ||
               cacheName.contains("auth");
    }
    
    /**
     * 获取缓存统计信息
     */
    public CacheStatistics getStatistics(String cacheName) {
        // 这里返回缓存统计信息
        // 实际实现需要根据具体的缓存实现来获取统计数据
        return new CacheStatistics(cacheName);
    }
    
    /**
     * 缓存统计信息
     */
    public static class CacheStatistics {
        private final String cacheName;
        private long hitCount = 0;
        private long missCount = 0;
        private long evictionCount = 0;
        
        public CacheStatistics(String cacheName) {
            this.cacheName = cacheName;
        }
        
        public String getCacheName() {
            return cacheName;
        }
        
        public long getHitCount() {
            return hitCount;
        }
        
        public long getMissCount() {
            return missCount;
        }
        
        public long getEvictionCount() {
            return evictionCount;
        }
        
        public double getHitRate() {
            long total = hitCount + missCount;
            return total == 0 ? 0.0 : (double) hitCount / total;
        }
    }
}