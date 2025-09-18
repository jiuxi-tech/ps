package com.jiuxi.module.sys.domain.service;

import com.jiuxi.module.sys.domain.entity.Dictionary;
import com.jiuxi.module.sys.domain.entity.ConfigType;
import com.jiuxi.module.sys.domain.entity.ConfigStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 字典缓存领域服务
 * 提供字典数据的多层缓存管理
 * 
 * @author DDD Refactor
 * @date 2025-09-18
 */
@Service
public class DictionaryCacheService {
    
    /**
     * 缓存键前缀
     */
    private static final String CACHE_PREFIX = "sys:dict:";
    
    /**
     * 字典类型缓存前缀
     */
    private static final String TYPE_CACHE_PREFIX = "sys:dict:type:";
    
    /**
     * 字典组缓存前缀
     */
    private static final String GROUP_CACHE_PREFIX = "sys:dict:group:";
    
    /**
     * 父级字典缓存前缀
     */
    private static final String PARENT_CACHE_PREFIX = "sys:dict:parent:";
    
    /**
     * 默认缓存过期时间（30分钟）
     */
    private static final Duration DEFAULT_EXPIRY = Duration.ofMinutes(30);
    
    /**
     * 系统预置字典缓存过期时间（2小时）
     */
    private static final Duration SYSTEM_DICT_EXPIRY = Duration.ofHours(2);
    
    /**
     * 热点字典缓存过期时间（10分钟）
     */
    private static final Duration HOT_DICT_EXPIRY = Duration.ofMinutes(10);
    
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    
    /**
     * 缓存单个字典
     * @param dictionary 字典
     */
    public void cacheDict(Dictionary dictionary) {
        if (dictionary == null || dictionary.getDictCode() == null) {
            return;
        }
        
        String cacheKey = buildCacheKey(dictionary.getDictCode(), dictionary.getTenantId());
        Duration expiry = determineExpiry(dictionary);
        
        redisTemplate.opsForValue().set(cacheKey, dictionary, expiry.toMillis(), TimeUnit.MILLISECONDS);
    }
    
    /**
     * 获取缓存的字典
     * @param dictCode 字典编码
     * @param tenantId 租户ID
     * @return 字典，如果不存在则返回null
     */
    public Dictionary getDictFromCache(String dictCode, String tenantId) {
        String cacheKey = buildCacheKey(dictCode, tenantId);
        return (Dictionary) redisTemplate.opsForValue().get(cacheKey);
    }
    
    /**
     * 批量缓存字典
     * @param dictionaries 字典列表
     */
    public void batchCacheDicts(List<Dictionary> dictionaries) {
        if (dictionaries == null || dictionaries.isEmpty()) {
            return;
        }
        
        dictionaries.forEach(this::cacheDict);
    }
    
    /**
     * 缓存字典类型下的所有字典
     * @param dictType 字典类型
     * @param tenantId 租户ID
     * @param dictionaries 字典列表
     */
    public void cacheDictsByType(ConfigType dictType, String tenantId, List<Dictionary> dictionaries) {
        String typeCacheKey = buildTypeCacheKey(dictType.getCode(), tenantId);
        Duration expiry = dictType.isSystemLevel() ? SYSTEM_DICT_EXPIRY : DEFAULT_EXPIRY;
        
        redisTemplate.opsForValue().set(typeCacheKey, dictionaries, expiry.toMillis(), TimeUnit.MILLISECONDS);
        
        // 同时缓存单个字典
        dictionaries.forEach(this::cacheDict);
    }
    
    /**
     * 获取字典类型下的所有缓存字典
     * @param dictType 字典类型
     * @param tenantId 租户ID
     * @return 字典列表
     */
    @SuppressWarnings("unchecked")
    public List<Dictionary> getDictsByTypeFromCache(ConfigType dictType, String tenantId) {
        String typeCacheKey = buildTypeCacheKey(dictType.getCode(), tenantId);
        return (List<Dictionary>) redisTemplate.opsForValue().get(typeCacheKey);
    }
    
    /**
     * 缓存字典组下的所有字典
     * @param dictGroup 字典组
     * @param tenantId 租户ID
     * @param dictionaries 字典列表
     */
    public void cacheDictsByGroup(String dictGroup, String tenantId, List<Dictionary> dictionaries) {
        String groupCacheKey = buildGroupCacheKey(dictGroup, tenantId);
        
        redisTemplate.opsForValue().set(groupCacheKey, dictionaries, 
                DEFAULT_EXPIRY.toMillis(), TimeUnit.MILLISECONDS);
        
        // 同时缓存单个字典
        dictionaries.forEach(this::cacheDict);
    }
    
    /**
     * 获取字典组下的所有缓存字典
     * @param dictGroup 字典组
     * @param tenantId 租户ID
     * @return 字典列表
     */
    @SuppressWarnings("unchecked")
    public List<Dictionary> getDictsByGroupFromCache(String dictGroup, String tenantId) {
        String groupCacheKey = buildGroupCacheKey(dictGroup, tenantId);
        return (List<Dictionary>) redisTemplate.opsForValue().get(groupCacheKey);
    }
    
    /**
     * 缓存父级字典下的子字典
     * @param parentDictId 父级字典ID
     * @param tenantId 租户ID
     * @param dictionaries 子字典列表
     */
    public void cacheChildDicts(String parentDictId, String tenantId, List<Dictionary> dictionaries) {
        String parentCacheKey = buildParentCacheKey(parentDictId, tenantId);
        
        redisTemplate.opsForValue().set(parentCacheKey, dictionaries, 
                DEFAULT_EXPIRY.toMillis(), TimeUnit.MILLISECONDS);
        
        // 同时缓存单个字典
        dictionaries.forEach(this::cacheDict);
    }
    
    /**
     * 获取父级字典下的缓存子字典
     * @param parentDictId 父级字典ID
     * @param tenantId 租户ID
     * @return 子字典列表
     */
    @SuppressWarnings("unchecked")
    public List<Dictionary> getChildDictsFromCache(String parentDictId, String tenantId) {
        String parentCacheKey = buildParentCacheKey(parentDictId, tenantId);
        return (List<Dictionary>) redisTemplate.opsForValue().get(parentCacheKey);
    }
    
    /**
     * 清除单个字典缓存
     * @param dictCode 字典编码
     * @param tenantId 租户ID
     */
    public void evictDict(String dictCode, String tenantId) {
        String cacheKey = buildCacheKey(dictCode, tenantId);
        redisTemplate.delete(cacheKey);
    }
    
    /**
     * 清除字典类型相关的所有缓存
     * @param dictType 字典类型
     * @param tenantId 租户ID
     */
    public void evictDictsByType(ConfigType dictType, String tenantId) {
        // 清除类型缓存
        String typeCacheKey = buildTypeCacheKey(dictType.getCode(), tenantId);
        redisTemplate.delete(typeCacheKey);
        
        // 清除相关的单个字典缓存（通过模式匹配）
        String pattern = buildCacheKey("*", tenantId);
        Set<String> keys = redisTemplate.keys(pattern);
        if (keys != null && !keys.isEmpty()) {
            redisTemplate.delete(keys);
        }
    }
    
    /**
     * 清除字典组相关的所有缓存
     * @param dictGroup 字典组
     * @param tenantId 租户ID
     */
    public void evictDictsByGroup(String dictGroup, String tenantId) {
        String groupCacheKey = buildGroupCacheKey(dictGroup, tenantId);
        redisTemplate.delete(groupCacheKey);
    }
    
    /**
     * 清除租户的所有字典缓存
     * @param tenantId 租户ID
     */
    public void evictAllDictsForTenant(String tenantId) {
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
        
        String parentPattern = PARENT_CACHE_PREFIX + "*:" + tenantId;
        Set<String> parentKeys = redisTemplate.keys(parentPattern);
        if (parentKeys != null && !parentKeys.isEmpty()) {
            redisTemplate.delete(parentKeys);
        }
    }
    
    /**
     * 智能缓存更新
     * 根据字典的重要性和访问频率决定缓存策略
     * @param dictionary 字典
     */
    public void smartCacheUpdate(Dictionary dictionary) {
        if (dictionary == null) {
            return;
        }
        
        // 先清除旧缓存
        evictDict(dictionary.getDictCode(), dictionary.getTenantId());
        
        // 如果字典是激活状态，则缓存
        if (dictionary.isActive()) {
            cacheDict(dictionary);
        }
        
        // 清除相关的类型和组缓存以确保一致性
        if (dictionary.getDictTypeEnum() != null) {
            evictDictsByType(dictionary.getDictTypeEnum(), dictionary.getTenantId());
        }
        if (dictionary.getDictGroup() != null) {
            evictDictsByGroup(dictionary.getDictGroup(), dictionary.getTenantId());
        }
    }
    
    /**
     * 预热缓存
     * 将热点字典预加载到缓存中
     * @param tenantId 租户ID
     * @param hotDicts 热点字典列表
     */
    public void warmupCache(String tenantId, List<Dictionary> hotDicts) {
        hotDicts.forEach(dict -> {
            String cacheKey = buildCacheKey(dict.getDictCode(), tenantId);
            redisTemplate.opsForValue().set(cacheKey, dict, 
                    HOT_DICT_EXPIRY.toMillis(), TimeUnit.MILLISECONDS);
        });
    }
    
    /**
     * 构建缓存键
     * @param dictCode 字典编码
     * @param tenantId 租户ID
     * @return 缓存键
     */
    private String buildCacheKey(String dictCode, String tenantId) {
        return CACHE_PREFIX + tenantId + ":" + dictCode;
    }
    
    /**
     * 构建类型缓存键
     * @param dictTypeCode 字典类型代码
     * @param tenantId 租户ID
     * @return 类型缓存键
     */
    private String buildTypeCacheKey(String dictTypeCode, String tenantId) {
        return TYPE_CACHE_PREFIX + dictTypeCode + ":" + tenantId;
    }
    
    /**
     * 构建组缓存键
     * @param dictGroup 字典组
     * @param tenantId 租户ID
     * @return 组缓存键
     */
    private String buildGroupCacheKey(String dictGroup, String tenantId) {
        return GROUP_CACHE_PREFIX + dictGroup + ":" + tenantId;
    }
    
    /**
     * 构建父级缓存键
     * @param parentDictId 父级字典ID
     * @param tenantId 租户ID
     * @return 父级缓存键
     */
    private String buildParentCacheKey(String parentDictId, String tenantId) {
        return PARENT_CACHE_PREFIX + parentDictId + ":" + tenantId;
    }
    
    /**
     * 根据字典特性决定缓存过期时间
     * @param dictionary 字典
     * @return 缓存过期时间
     */
    private Duration determineExpiry(Dictionary dictionary) {
        // 系统预置字典缓存时间更长
        if (dictionary.isSystemPreset()) {
            return SYSTEM_DICT_EXPIRY;
        }
        
        // 只读字典缓存时间更长
        if (dictionary.isReadonly()) {
            return SYSTEM_DICT_EXPIRY;
        }
        
        // 根据字典类型决定缓存时间
        if (dictionary.getDictTypeEnum() != null && dictionary.getDictTypeEnum().isSystemLevel()) {
            return SYSTEM_DICT_EXPIRY;
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
        int dictCount = keys != null ? keys.size() : 0;
        
        String typePattern = TYPE_CACHE_PREFIX + "*:" + tenantId;
        Set<String> typeKeys = redisTemplate.keys(typePattern);
        int typeCount = typeKeys != null ? typeKeys.size() : 0;
        
        String groupPattern = GROUP_CACHE_PREFIX + "*:" + tenantId;
        Set<String> groupKeys = redisTemplate.keys(groupPattern);
        int groupCount = groupKeys != null ? groupKeys.size() : 0;
        
        String parentPattern = PARENT_CACHE_PREFIX + "*:" + tenantId;
        Set<String> parentKeys = redisTemplate.keys(parentPattern);
        int parentCount = parentKeys != null ? parentKeys.size() : 0;
        
        return new CacheStats(dictCount, typeCount, groupCount, parentCount);
    }
    
    /**
     * 缓存统计信息
     */
    public static class CacheStats {
        private final int dictCount;
        private final int typeCount;
        private final int groupCount;
        private final int parentCount;
        
        public CacheStats(int dictCount, int typeCount, int groupCount, int parentCount) {
            this.dictCount = dictCount;
            this.typeCount = typeCount;
            this.groupCount = groupCount;
            this.parentCount = parentCount;
        }
        
        public int getDictCount() { return dictCount; }
        public int getTypeCount() { return typeCount; }
        public int getGroupCount() { return groupCount; }
        public int getParentCount() { return parentCount; }
        public int getTotalCount() { return dictCount + typeCount + groupCount + parentCount; }
        
        @Override
        public String toString() {
            return String.format("DictCacheStats{dicts=%d, types=%d, groups=%d, parents=%d, total=%d}", 
                    dictCount, typeCount, groupCount, parentCount, getTotalCount());
        }
    }
}