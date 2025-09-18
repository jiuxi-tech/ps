package com.jiuxi.module.auth.infra.cache.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 缓存配置类
 * 实现缓存策略的配置化管理
 * 
 * @author DDD Refactor
 * @date 2025-09-18
 */
@Component
@ConfigurationProperties(prefix = "auth.cache")
public class CacheConfig {
    
    /**
     * 是否启用缓存
     */
    private boolean enabled = true;
    
    /**
     * 默认过期时间（秒）
     */
    private long defaultTtl = 3600; // 1小时
    
    /**
     * 最大缓存条目数
     */
    private int maxSize = 10000;
    
    /**
     * 缓存策略配置
     */
    private Map<String, CacheStrategy> strategies = new ConcurrentHashMap<>();
    
    public CacheConfig() {
        initDefaultStrategies();
    }
    
    /**
     * 初始化默认缓存策略
     */
    private void initDefaultStrategies() {
        // 角色权限缓存策略
        CacheStrategy rolePermissionStrategy = new CacheStrategy();
        rolePermissionStrategy.setTtl(1800); // 30分钟
        rolePermissionStrategy.setMaxSize(5000);
        rolePermissionStrategy.setRefreshOnAccess(true);
        rolePermissionStrategy.setEvictionPolicy(EvictionPolicy.LRU);
        strategies.put("rolePermissions", rolePermissionStrategy);
        
        // 用户权限缓存策略
        CacheStrategy userPermissionStrategy = new CacheStrategy();
        userPermissionStrategy.setTtl(1800); // 30分钟
        userPermissionStrategy.setMaxSize(10000);
        userPermissionStrategy.setRefreshOnAccess(true);
        userPermissionStrategy.setEvictionPolicy(EvictionPolicy.LRU);
        strategies.put("userPermissions", userPermissionStrategy);
        
        // 菜单缓存策略
        CacheStrategy menuStrategy = new CacheStrategy();
        menuStrategy.setTtl(7200); // 2小时
        menuStrategy.setMaxSize(2000);
        menuStrategy.setRefreshOnAccess(false);
        menuStrategy.setEvictionPolicy(EvictionPolicy.LRU);
        strategies.put("menus", menuStrategy);
        
        // 角色信息缓存策略
        CacheStrategy roleStrategy = new CacheStrategy();
        roleStrategy.setTtl(3600); // 1小时
        roleStrategy.setMaxSize(3000);
        roleStrategy.setRefreshOnAccess(false);
        roleStrategy.setEvictionPolicy(EvictionPolicy.LRU);
        strategies.put("roles", roleStrategy);
        
        // 权限信息缓存策略
        CacheStrategy permissionStrategy = new CacheStrategy();
        permissionStrategy.setTtl(7200); // 2小时
        permissionStrategy.setMaxSize(5000);
        permissionStrategy.setRefreshOnAccess(false);
        permissionStrategy.setEvictionPolicy(EvictionPolicy.LRU);
        strategies.put("permissions", permissionStrategy);
    }
    
    /**
     * 获取指定缓存的策略
     */
    public CacheStrategy getStrategy(String cacheName) {
        return strategies.getOrDefault(cacheName, getDefaultStrategy());
    }
    
    /**
     * 获取默认缓存策略
     */
    public CacheStrategy getDefaultStrategy() {
        CacheStrategy defaultStrategy = new CacheStrategy();
        defaultStrategy.setTtl(defaultTtl);
        defaultStrategy.setMaxSize(maxSize);
        defaultStrategy.setRefreshOnAccess(false);
        defaultStrategy.setEvictionPolicy(EvictionPolicy.LRU);
        return defaultStrategy;
    }
    
    /**
     * 添加或更新缓存策略
     */
    public void setStrategy(String cacheName, CacheStrategy strategy) {
        strategies.put(cacheName, strategy);
    }
    
    // Getters and Setters
    public boolean isEnabled() {
        return enabled;
    }
    
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
    
    public long getDefaultTtl() {
        return defaultTtl;
    }
    
    public void setDefaultTtl(long defaultTtl) {
        this.defaultTtl = defaultTtl;
    }
    
    public int getMaxSize() {
        return maxSize;
    }
    
    public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
    }
    
    public Map<String, CacheStrategy> getStrategies() {
        return strategies;
    }
    
    public void setStrategies(Map<String, CacheStrategy> strategies) {
        this.strategies = strategies;
    }
    
    /**
     * 缓存策略配置
     */
    public static class CacheStrategy {
        private long ttl = 3600; // 过期时间（秒）
        private int maxSize = 1000; // 最大条目数
        private boolean refreshOnAccess = false; // 访问时是否刷新过期时间
        private EvictionPolicy evictionPolicy = EvictionPolicy.LRU; // 淘汰策略
        private boolean statistics = false; // 是否启用统计
        
        // Getters and Setters
        public long getTtl() {
            return ttl;
        }
        
        public void setTtl(long ttl) {
            this.ttl = ttl;
        }
        
        public Duration getTtlDuration() {
            return Duration.ofSeconds(ttl);
        }
        
        public int getMaxSize() {
            return maxSize;
        }
        
        public void setMaxSize(int maxSize) {
            this.maxSize = maxSize;
        }
        
        public boolean isRefreshOnAccess() {
            return refreshOnAccess;
        }
        
        public void setRefreshOnAccess(boolean refreshOnAccess) {
            this.refreshOnAccess = refreshOnAccess;
        }
        
        public EvictionPolicy getEvictionPolicy() {
            return evictionPolicy;
        }
        
        public void setEvictionPolicy(EvictionPolicy evictionPolicy) {
            this.evictionPolicy = evictionPolicy;
        }
        
        public boolean isStatistics() {
            return statistics;
        }
        
        public void setStatistics(boolean statistics) {
            this.statistics = statistics;
        }
    }
    
    /**
     * 淘汰策略枚举
     */
    public enum EvictionPolicy {
        LRU, // 最近最少使用
        LFU, // 最不经常使用
        FIFO, // 先进先出
        RANDOM // 随机淘汰
    }
}