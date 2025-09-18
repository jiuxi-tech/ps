package com.jiuxi.module.sys.domain.service;

import com.jiuxi.module.sys.domain.entity.Dictionary;
import com.jiuxi.module.sys.domain.entity.ConfigType;
import com.jiuxi.module.sys.domain.entity.ConfigStatus;
import com.jiuxi.module.sys.domain.vo.ConfigKey;
import com.jiuxi.module.sys.domain.repo.DictionaryRepository;
import com.jiuxi.module.sys.domain.event.DictionaryChangedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * 字典领域服务
 * 封装字典的核心业务逻辑和规则
 * 
 * @author DDD Refactor
 * @date 2025-09-18
 */
@Service
public class DictionaryDomainService {
    
    @Autowired
    private DictionaryRepository dictionaryRepository;
    
    @Autowired
    private DictionaryCacheService dictionaryCacheService;
    
    @Autowired
    private ApplicationEventPublisher eventPublisher;
    
    /**
     * 创建新的字典
     * @param dictCode 字典编码
     * @param dictName 字典名称
     * @param dictType 字典类型
     * @param dictGroup 字典组
     * @param operator 操作者
     * @param tenantId 租户ID
     * @return 创建的字典
     */
    @Transactional
    public Dictionary createDictionary(String dictCode, String dictName, 
            ConfigType dictType, String dictGroup, String operator, String tenantId) {
        
        try {
            // 检查字典编码是否已存在
            if (dictionaryRepository.existsByDictCode(dictCode, tenantId)) {
                throw new IllegalArgumentException("字典编码已存在: " + dictCode);
            }
            
            // 创建字典实体
            Dictionary dictionary = new Dictionary();
            dictionary.setDictId(UUID.randomUUID().toString());
            dictionary.setDictCode(dictCode);
            dictionary.setDictName(dictName);
            dictionary.setDictType(dictType.getCode());
            dictionary.setDictGroup(dictGroup);
            dictionary.setStatus(ConfigStatus.ACTIVE.getCode());
            dictionary.setTenantId(tenantId);
            dictionary.setCreator(operator);
            dictionary.setCreateTime(LocalDateTime.now());
            
            // 设置系统级和只读属性
            dictionary.setIsSystemPreset(dictType.isSystemLevel());
            dictionary.setIsReadonly(dictType == ConfigType.SECURITY);
            
            // 保存到数据库
            Dictionary savedDictionary = dictionaryRepository.save(dictionary);
            
            // 缓存字典
            dictionaryCacheService.cacheDict(savedDictionary);
            
            // 发布字典创建事件
            publishDictionaryChangedEvent(savedDictionary, null, savedDictionary.getDictName(), 
                    DictionaryChangedEvent.ChangeType.CREATED, operator);
            
            return savedDictionary;
            
        } catch (Exception e) {
            throw new RuntimeException("创建字典失败: " + e.getMessage(), e);
        }
    }
    
    /**
     * 更新字典
     * @param dictId 字典ID
     * @param dictName 新字典名称
     * @param operator 操作者
     * @return 更新后的字典
     */
    @Transactional
    public Dictionary updateDictionary(String dictId, String dictName, String operator) {
        
        try {
            // 查找现有字典
            Optional<Dictionary> existingDictOpt = dictionaryRepository.findById(dictId);
            if (!existingDictOpt.isPresent()) {
                throw new IllegalArgumentException("字典不存在: " + dictId);
            }
            
            Dictionary existingDict = existingDictOpt.get();
            String oldName = existingDict.getDictName();
            
            // 更新字典名称
            existingDict.setDictName(dictName);
            existingDict.setUpdator(operator);
            existingDict.setUpdateTime(LocalDateTime.now());
            
            // 保存更新
            Dictionary updatedDict = dictionaryRepository.save(existingDict);
            
            // 智能缓存更新
            dictionaryCacheService.smartCacheUpdate(updatedDict);
            
            // 发布字典变更事件
            publishDictionaryChangedEvent(updatedDict, oldName, updatedDict.getDictName(), 
                    DictionaryChangedEvent.ChangeType.NAME_CHANGED, operator);
            
            return updatedDict;
            
        } catch (Exception e) {
            throw new RuntimeException("更新字典失败: " + e.getMessage(), e);
        }
    }
    
    /**
     * 更新字典状态
     * @param dictId 字典ID
     * @param newStatus 新状态
     * @param operator 操作者
     * @return 更新后的字典
     */
    @Transactional
    public Dictionary updateDictionaryStatus(String dictId, ConfigStatus newStatus, String operator) {
        
        try {
            Optional<Dictionary> existingDictOpt = dictionaryRepository.findById(dictId);
            if (!existingDictOpt.isPresent()) {
                throw new IllegalArgumentException("字典不存在: " + dictId);
            }
            
            Dictionary existingDict = existingDictOpt.get();
            String oldStatus = existingDict.getStatus();
            
            // 更新状态
            existingDict.setStatus(newStatus.getCode());
            existingDict.setUpdator(operator);
            existingDict.setUpdateTime(LocalDateTime.now());
            
            // 保存更新
            Dictionary updatedDict = dictionaryRepository.save(existingDict);
            
            // 智能缓存更新
            dictionaryCacheService.smartCacheUpdate(updatedDict);
            
            // 发布状态变更事件
            publishDictionaryChangedEvent(updatedDict, oldStatus, newStatus.getCode(), 
                    DictionaryChangedEvent.ChangeType.STATUS_CHANGED, operator);
            
            return updatedDict;
            
        } catch (Exception e) {
            throw new RuntimeException("更新字典状态失败: " + e.getMessage(), e);
        }
    }
    
    /**
     * 获取字典（带缓存）
     * @param dictCode 字典编码
     * @param tenantId 租户ID
     * @return 字典
     */
    public Optional<Dictionary> getDictionary(String dictCode, String tenantId) {
        
        try {
            // 先从缓存获取
            Dictionary cachedDict = dictionaryCacheService.getDictFromCache(dictCode, tenantId);
            if (cachedDict != null) {
                return Optional.of(cachedDict);
            }
            
            // 缓存未命中，从数据库获取
            Optional<Dictionary> dictOpt = dictionaryRepository.findByDictCode(dictCode, tenantId);
            
            // 缓存结果（如果存在）
            dictOpt.ifPresent(dictionaryCacheService::cacheDict);
            
            return dictOpt;
            
        } catch (Exception e) {
            throw new RuntimeException("获取字典失败: " + e.getMessage(), e);
        }
    }
    
    /**
     * 根据字典类型获取字典列表
     * @param dictType 字典类型
     * @param tenantId 租户ID
     * @return 字典列表
     */
    public List<Dictionary> getDictionariesByType(ConfigType dictType, String tenantId) {
        
        try {
            // 先从缓存获取
            List<Dictionary> cachedDicts = dictionaryCacheService.getDictsByTypeFromCache(dictType, tenantId);
            if (cachedDicts != null && !cachedDicts.isEmpty()) {
                return cachedDicts;
            }
            
            // 缓存未命中，从数据库获取
            List<Dictionary> dicts = dictionaryRepository.findByDictType(dictType, tenantId);
            
            // 缓存结果
            if (!dicts.isEmpty()) {
                dictionaryCacheService.cacheDictsByType(dictType, tenantId, dicts);
            }
            
            return dicts;
            
        } catch (Exception e) {
            throw new RuntimeException("根据类型获取字典失败: " + e.getMessage(), e);
        }
    }
    
    /**
     * 根据字典组获取字典列表
     * @param dictGroup 字典组
     * @param tenantId 租户ID
     * @return 字典列表
     */
    public List<Dictionary> getDictionariesByGroup(String dictGroup, String tenantId) {
        
        try {
            // 先从缓存获取
            List<Dictionary> cachedDicts = dictionaryCacheService.getDictsByGroupFromCache(dictGroup, tenantId);
            if (cachedDicts != null && !cachedDicts.isEmpty()) {
                return cachedDicts;
            }
            
            // 缓存未命中，从数据库获取
            List<Dictionary> dicts = dictionaryRepository.findByDictGroup(dictGroup, tenantId);
            
            // 缓存结果
            if (!dicts.isEmpty()) {
                dictionaryCacheService.cacheDictsByGroup(dictGroup, tenantId, dicts);
            }
            
            return dicts;
            
        } catch (Exception e) {
            throw new RuntimeException("根据组获取字典失败: " + e.getMessage(), e);
        }
    }
    
    /**
     * 获取子字典列表
     * @param parentDictId 父级字典ID
     * @param tenantId 租户ID
     * @return 子字典列表
     */
    public List<Dictionary> getChildDictionaries(String parentDictId, String tenantId) {
        
        try {
            // 先从缓存获取
            List<Dictionary> cachedChildren = dictionaryCacheService.getChildDictsFromCache(parentDictId, tenantId);
            if (cachedChildren != null && !cachedChildren.isEmpty()) {
                return cachedChildren;
            }
            
            // 缓存未命中，从数据库获取
            List<Dictionary> children = dictionaryRepository.findByParentDictId(parentDictId, tenantId);
            
            // 缓存结果
            if (!children.isEmpty()) {
                dictionaryCacheService.cacheChildDicts(parentDictId, tenantId, children);
            }
            
            return children;
            
        } catch (Exception e) {
            throw new RuntimeException("获取子字典失败: " + e.getMessage(), e);
        }
    }
    
    /**
     * 获取激活状态的字典
     * @param tenantId 租户ID
     * @return 激活字典列表
     */
    public List<Dictionary> getActiveDictionaries(String tenantId) {
        return dictionaryRepository.findActiveDictionaries(tenantId);
    }
    
    /**
     * 获取系统预置字典
     * @param tenantId 租户ID
     * @return 系统预置字典列表
     */
    public List<Dictionary> getSystemPresetDictionaries(String tenantId) {
        return dictionaryRepository.findSystemPresetDictionaries(tenantId);
    }
    
    /**
     * 批量创建字典
     * @param dictionaries 字典数据列表
     * @param operator 操作者
     * @param tenantId 租户ID
     * @return 创建的字典列表
     */
    @Transactional
    public List<Dictionary> batchCreateDictionaries(List<Dictionary> dictionaries, 
            String operator, String tenantId) {
        
        try {
            // 设置公共属性
            dictionaries.forEach(dict -> {
                dict.setDictId(UUID.randomUUID().toString());
                dict.setStatus(ConfigStatus.ACTIVE.getCode());
                dict.setTenantId(tenantId);
                dict.setCreator(operator);
                dict.setCreateTime(LocalDateTime.now());
            });
            
            // 批量保存
            List<Dictionary> savedDicts = dictionaryRepository.batchSave(dictionaries);
            
            // 批量缓存
            dictionaryCacheService.batchCacheDicts(savedDicts);
            
            // 批量发布事件
            savedDicts.forEach(dict -> publishDictionaryChangedEvent(dict, null, 
                    dict.getDictName(), DictionaryChangedEvent.ChangeType.CREATED, operator));
            
            return savedDicts;
            
        } catch (Exception e) {
            throw new RuntimeException("批量创建字典失败: " + e.getMessage(), e);
        }
    }
    
    /**
     * 删除字典
     * @param dictId 字典ID
     * @param operator 操作者
     */
    @Transactional
    public void deleteDictionary(String dictId, String operator) {
        
        try {
            Optional<Dictionary> dictOpt = dictionaryRepository.findById(dictId);
            if (!dictOpt.isPresent()) {
                throw new IllegalArgumentException("字典不存在: " + dictId);
            }
            
            Dictionary dict = dictOpt.get();
            
            // 检查是否为只读字典
            if (dict.isReadonly()) {
                throw new IllegalArgumentException("只读字典不允许删除: " + dict.getDictCode());
            }
            
            // 删除数据库记录
            dictionaryRepository.deleteById(dictId);
            
            // 清除缓存
            dictionaryCacheService.evictDict(dict.getDictCode(), dict.getTenantId());
            
            // 发布删除事件
            publishDictionaryChangedEvent(dict, dict.getDictName(), null, 
                    DictionaryChangedEvent.ChangeType.DELETED, operator);
                    
        } catch (Exception e) {
            throw new RuntimeException("删除字典失败: " + e.getMessage(), e);
        }
    }
    
    /**
     * 热重载字典
     * @param dictCode 字典编码
     * @param tenantId 租户ID
     * @return 是否重载成功
     */
    public boolean hotReloadDictionary(String dictCode, String tenantId) {
        try {
            // 从数据库重新加载字典
            Optional<Dictionary> dictOpt = dictionaryRepository.findByDictCode(dictCode, tenantId);
            
            if (dictOpt.isPresent()) {
                Dictionary dict = dictOpt.get();
                
                // 重新缓存字典
                dictionaryCacheService.cacheDict(dict);
                
                // 发布字典重载事件
                publishDictionaryChangedEvent(dict, null, dict.getDictName(), 
                        DictionaryChangedEvent.ChangeType.RELOADED, "SYSTEM");
                
                return true;
            }
            
            return false;
        } catch (Exception e) {
            throw new RuntimeException("热重载字典失败: " + e.getMessage(), e);
        }
    }
    
    /**
     * 批量热重载字典
     * @param dictCodes 字典编码列表
     * @param tenantId 租户ID
     * @return 重载成功的字典数量
     */
    @Async
    public int batchHotReloadDictionaries(List<String> dictCodes, String tenantId) {
        int successCount = 0;
        
        for (String dictCode : dictCodes) {
            try {
                if (hotReloadDictionary(dictCode, tenantId)) {
                    successCount++;
                }
            } catch (Exception e) {
                // 记录日志，但继续处理其他字典
                System.err.println("热重载字典失败: " + dictCode + ", 错误: " + e.getMessage());
            }
        }
        
        return successCount;
    }
    
    /**
     * 智能缓存刷新
     * @param tenantId 租户ID
     */
    @Async
    public void smartCacheRefresh(String tenantId) {
        try {
            // 获取系统预置字典进行预热
            List<Dictionary> systemDicts = getSystemPresetDictionaries(tenantId);
            dictionaryCacheService.batchCacheDicts(systemDicts);
            
            // 获取激活字典进行预热
            List<Dictionary> activeDicts = getActiveDictionaries(tenantId);
            dictionaryCacheService.warmupCache(tenantId, activeDicts);
            
        } catch (Exception e) {
            throw new RuntimeException("智能缓存刷新失败: " + e.getMessage(), e);
        }
    }
    
    /**
     * 清理租户缓存
     * @param tenantId 租户ID
     */
    public void clearCache(String tenantId) {
        try {
            dictionaryCacheService.evictAllDictsForTenant(tenantId);
        } catch (Exception e) {
            throw new RuntimeException("清理字典缓存失败: " + e.getMessage(), e);
        }
    }
    
    /**
     * 获取缓存健康状态
     * @param tenantId 租户ID
     * @return 缓存统计信息
     */
    public DictionaryCacheService.CacheStats getCacheHealthStatus(String tenantId) {
        return dictionaryCacheService.getCacheStats(tenantId);
    }
    
    /**
     * 发布字典变更事件
     */
    private void publishDictionaryChangedEvent(Dictionary dict, String oldValue, String newValue, 
            DictionaryChangedEvent.ChangeType changeType, String operator) {
        
        DictionaryChangedEvent event = new DictionaryChangedEvent(
                UUID.randomUUID().toString(),
                dict.getDictId(),
                dict.getDictCode(),
                dict.getDictName(),
                dict.getDictType(),
                changeType,
                oldValue,
                newValue,
                operator,
                dict.getTenantId()
        );
        
        eventPublisher.publishEvent(event);
    }
}