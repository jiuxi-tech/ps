package com.jiuxi.module.sys.domain.service;

import com.jiuxi.module.sys.domain.entity.SystemConfig;
import com.jiuxi.module.sys.domain.entity.ConfigType;
import com.jiuxi.module.sys.domain.entity.ConfigStatus;
import com.jiuxi.module.sys.domain.valueobject.ConfigKey;
import com.jiuxi.module.sys.domain.valueobject.ConfigValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 系统配置缓存领域服务
 * 提供系统配置的多层缓存管理
 * 
 * @author DDD Refactor
 * @date 2025-09-11
 */
@Service
public class SystemConfigCacheService {
    
    /**
     * 缓存键前缀
     */
    private static final String CACHE_PREFIX = "sys:config:";
    
    /**
     * 配置类型缓存前缀
     */
    private static final String TYPE_CACHE_PREFIX = "sys:config:type:";
    
    /**
     * 配置组缓存前缀
     */
    private static final String GROUP_CACHE_PREFIX = "sys:config:group:";
    
    /**
     * 系统级配置缓存前缀
     */
    private static final String SYSTEM_CACHE_PREFIX = "sys:config:system:";
    
    /**
     * 默认缓存过期时间（30分钟）
     */
    private static final Duration DEFAULT_EXPIRY = Duration.ofMinutes(30);
    
    /**
     * 系统级配置缓存过期时间（2小时）
     */
    private static final Duration SYSTEM_CONFIG_EXPIRY = Duration.ofHours(2);
    
    /**
     * 热点配置缓存过期时间（10分钟）
     */
    private static final Duration HOT_CONFIG_EXPIRY = Duration.ofMinutes(10);
    
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    
    /**
     * 缓存单个系统配置
     * @param systemConfig 系统配置
     */
    public void cacheConfig(SystemConfig systemConfig) {
        if (systemConfig == null || systemConfig.getConfigKey() == null) {
            return;
        }
        
        String cacheKey = buildCacheKey(systemConfig.getConfigKey(), systemConfig.getTenantId());
        Duration expiry = determineExpiry(systemConfig);
        
        redisTemplate.opsForValue().set(cacheKey, systemConfig, expiry.toMillis(), TimeUnit.MILLISECONDS);
    }
    
    /**
     * 获取缓存的系统配置
     * @param configKey 配置键
     * @param tenantId 租户ID
     * @return 系统配置，如果不存在则返回null
     */
    public SystemConfig getConfigFromCache(String configKey, String tenantId) {
        String cacheKey = buildCacheKey(configKey, tenantId);
        return (SystemConfig) redisTemplate.opsForValue().get(cacheKey);
    }
    
    /**
     * 使用配置键值对象获取缓存的系统配置
     * @param configKeyObj 配置键值对象
     * @param tenantId 租户ID
     * @return 系统配置，如果不存在则返回null
     */
    public SystemConfig getConfigFromCache(ConfigKey configKeyObj, String tenantId) {
        return getConfigFromCache(configKeyObj.getValue(), tenantId);
    }
    
    /**
     * 批量缓存系统配置
     * @param systemConfigs 系统配置列表
     */
    public void batchCacheConfigs(List<SystemConfig> systemConfigs) {
        if (systemConfigs == null || systemConfigs.isEmpty()) {
            return;
        }
        
        systemConfigs.forEach(this::cacheConfig);
    }
    
    /**
     * 缓存配置类型下的所有配置
     * @param configType 配置类型
     * @param tenantId 租户ID
     * @param systemConfigs 配置列表
     */
    public void cacheConfigsByType(ConfigType configType, String tenantId, List<SystemConfig> systemConfigs) {
        String typeCacheKey = buildTypeCacheKey(configType.getCode(), tenantId);
        Duration expiry = configType.isSystemLevel() ? SYSTEM_CONFIG_EXPIRY : DEFAULT_EXPIRY;
        
        redisTemplate.opsForValue().set(typeCacheKey, systemConfigs, expiry.toMillis(), TimeUnit.MILLISECONDS);
        
        // 同时缓存单个配置
        systemConfigs.forEach(this::cacheConfig);
    }
    
    /**
     * 获取配置类型下的所有缓存配置
     * @param configType 配置类型
     * @param tenantId 租户ID
     * @return 配置列表
     */
    @SuppressWarnings("unchecked")
    public List<SystemConfig> getConfigsByTypeFromCache(ConfigType configType, String tenantId) {
        String typeCacheKey = buildTypeCacheKey(configType.getCode(), tenantId);
        return (List<SystemConfig>) redisTemplate.opsForValue().get(typeCacheKey);
    }
    
    /**
     * 缓存配置组下的所有配置
     * @param configGroup 配置组
     * @param tenantId 租户ID
     * @param systemConfigs 配置列表
     */
    public void cacheConfigsByGroup(String configGroup, String tenantId, List<SystemConfig> systemConfigs) {
        String groupCacheKey = buildGroupCacheKey(configGroup, tenantId);
        
        redisTemplate.opsForValue().set(groupCacheKey, systemConfigs, 
                DEFAULT_EXPIRY.toMillis(), TimeUnit.MILLISECONDS);
        
        // 同时缓存单个配置
        systemConfigs.forEach(this::cacheConfig);
    }
    
    /**
     * 获取配置组下的所有缓存配置
     * @param configGroup 配置组
     * @param tenantId 租户ID
     * @return 配置列表
     */
    @SuppressWarnings("unchecked")
    public List<SystemConfig> getConfigsByGroupFromCache(String configGroup, String tenantId) {
        String groupCacheKey = buildGroupCacheKey(configGroup, tenantId);
        return (List<SystemConfig>) redisTemplate.opsForValue().get(groupCacheKey);
    }
    
    /**
     * 清除单个配置缓存
     * @param configKey 配置键
     * @param tenantId 租户ID
     */
    public void evictConfig(String configKey, String tenantId) {
        String cacheKey = buildCacheKey(configKey, tenantId);
        redisTemplate.delete(cacheKey);
    }
    
    /**
     * 清除配置类型相关的所有缓存
     * @param configType 配置类型
     * @param tenantId 租户ID
     */
    public void evictConfigsByType(ConfigType configType, String tenantId) {
        // 清除类型缓存
        String typeCacheKey = buildTypeCacheKey(configType.getCode(), tenantId);
        redisTemplate.delete(typeCacheKey);
        
        // 清除相关的单个配置缓存（通过模式匹配）
        String pattern = buildCacheKey("*", tenantId);
        Set<String> keys = redisTemplate.keys(pattern);
        if (keys != null && !keys.isEmpty()) {
            redisTemplate.delete(keys);
        }
    }
    
    /**
     * 清除配置组相关的所有缓存
     * @param configGroup 配置组
     * @param tenantId 租户ID
     */
    public void evictConfigsByGroup(String configGroup, String tenantId) {
        String groupCacheKey = buildGroupCacheKey(configGroup, tenantId);
        redisTemplate.delete(groupCacheKey);
    }
    
    /**
     * 清除租户的所有配置缓存
     * @param tenantId 租户ID
     */
    public void evictAllConfigsForTenant(String tenantId) {
        String pattern = CACHE_PREFIX + tenantId + ":*";
        Set<String> keys = redisTemplate.keys(pattern);
        if (keys != null && !keys.isEmpty()) {
            redisTemplate.delete(keys);
        }
        
        // 清除类型和组缓存
        String typePattern = TYPE_CACHE_PREFIX + "*:" + tenantId;
        Set<String> typeKeys = redisTemplate.keys(typePattern);
        if (typeKeys != null && !typeKeys.isEmpty()) {
            redisTemplate.delete(typeKeys);
        }
        
        String groupPattern = GROUP_CACHE_PREFIX + "*:" + tenantId;
        Set<String> groupKeys = redisTemplate.keys(groupPattern);
        if (groupKeys != null && !groupKeys.isEmpty()) {
            redisTemplate.delete(groupKeys);
        }
    }
    
    /**
     * 智能缓存更新
     * 根据配置的重要性和访问频率决定缓存策略
     * @param systemConfig 系统配置
     */
    public void smartCacheUpdate(SystemConfig systemConfig) {
        if (systemConfig == null) {
            return;
        }
        
        // 先清除旧缓存
        evictConfig(systemConfig.getConfigKey(), systemConfig.getTenantId());
        
        // 如果配置是激活状态，则缓存
        if (systemConfig.isActive()) {
            cacheConfig(systemConfig);
        }
        
        // 清除相关的类型和组缓存以确保一致性
        if (systemConfig.getConfigTypeEnum() != null) {
            evictConfigsByType(systemConfig.getConfigTypeEnum(), systemConfig.getTenantId());
        }
        if (systemConfig.getConfigGroup() != null) {
            evictConfigsByGroup(systemConfig.getConfigGroup(), systemConfig.getTenantId());
        }
    }
    
    /**
     * 预热缓存
     * 将热点配置预加载到缓存中
     * @param tenantId 租户ID
     * @param hotConfigs 热点配置列表
     */
    public void warmupCache(String tenantId, List<SystemConfig> hotConfigs) {
        hotConfigs.forEach(config -> {
            String cacheKey = buildCacheKey(config.getConfigKey(), tenantId);
            redisTemplate.opsForValue().set(cacheKey, config, 
                    HOT_CONFIG_EXPIRY.toMillis(), TimeUnit.MILLISECONDS);
        });
    }
    
    /**
     * 构建缓存键
     * @param configKey 配置键
     * @param tenantId 租户ID
     * @return 缓存键
     */
    private String buildCacheKey(String configKey, String tenantId) {
        return CACHE_PREFIX + tenantId + ":" + configKey;
    }
    
    /**
     * 构建类型缓存键
     * @param configTypeCode 配置类型代码
     * @param tenantId 租户ID
     * @return 类型缓存键
     */
    private String buildTypeCacheKey(String configTypeCode, String tenantId) {
        return TYPE_CACHE_PREFIX + configTypeCode + ":" + tenantId;
    }
    
    /**
     * 构建组缓存键
     * @param configGroup 配置组
     * @param tenantId 租户ID
     * @return 组缓存键
     */
    private String buildGroupCacheKey(String configGroup, String tenantId) {
        return GROUP_CACHE_PREFIX + configGroup + ":" + tenantId;
    }
    
    /**
     * 根据配置特性决定缓存过期时间
     * @param systemConfig 系统配置
     * @return 缓存过期时间
     */
    private Duration determineExpiry(SystemConfig systemConfig) {
        // 系统级配置缓存时间更长
        if (systemConfig.isSystemLevel()) {
            return SYSTEM_CONFIG_EXPIRY;
        }
        
        // 只读配置缓存时间更长
        if (systemConfig.getIsReadonly() != null && systemConfig.getIsReadonly()) {
            return SYSTEM_CONFIG_EXPIRY;
        }
        
        // 安全相关配置缓存时间较短
        if (systemConfig.getConfigTypeEnum() == ConfigType.SECURITY) {
            return HOT_CONFIG_EXPIRY;
        }
        
        return DEFAULT_EXPIRY;
    }
    
    /**
     * 获取缓存统计信息
     * @param tenantId 租户ID
     * @return 缓存统计信息
     */
    public CacheStats getCacheStats(String tenantId) {
        String pattern = CACHE_PREFIX + tenantId + ":*";
        Set<String> keys = redisTemplate.keys(pattern);
        int configCount = keys != null ? keys.size() : 0;
        
        String typePattern = TYPE_CACHE_PREFIX + "*:" + tenantId;
        Set<String> typeKeys = redisTemplate.keys(typePattern);
        int typeCount = typeKeys != null ? typeKeys.size() : 0;
        
        String groupPattern = GROUP_CACHE_PREFIX + "*:" + tenantId;
        Set<String> groupKeys = redisTemplate.keys(groupPattern);
        int groupCount = groupKeys != null ? groupKeys.size() : 0;
        
        return new CacheStats(configCount, typeCount, groupCount);
    }
    
    /**
     * 缓存统计信息
     */
    public static class CacheStats {
        private final int configCount;
        private final int typeCount;
        private final int groupCount;
        
        public CacheStats(int configCount, int typeCount, int groupCount) {
            this.configCount = configCount;
            this.typeCount = typeCount;
            this.groupCount = groupCount;
        }
        
        public int getConfigCount() { return configCount; }
        public int getTypeCount() { return typeCount; }
        public int getGroupCount() { return groupCount; }
        public int getTotalCount() { return configCount + typeCount + groupCount; }
        
        @Override
        public String toString() {
            return String.format("CacheStats{configs=%d, types=%d, groups=%d, total=%d}", 
                    configCount, typeCount, groupCount, getTotalCount());
        }
    }
}