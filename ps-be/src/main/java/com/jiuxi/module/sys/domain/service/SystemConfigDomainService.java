package com.jiuxi.module.sys.domain.service;

import com.jiuxi.module.sys.domain.entity.SystemConfig;
import com.jiuxi.module.sys.domain.entity.ConfigType;
import com.jiuxi.module.sys.domain.entity.ConfigStatus;
import com.jiuxi.module.sys.domain.vo.ConfigKey;
import com.jiuxi.module.sys.domain.vo.ConfigValue;
import com.jiuxi.module.sys.domain.repo.SystemConfigRepository;
import com.jiuxi.module.sys.domain.event.ConfigurationChangedEvent;
import com.jiuxi.module.sys.domain.service.ConfigValidationService.ValidationResult;
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
 * 系统配置领域服务
 * 封装系统配置的核心业务逻辑和规则
 * 
 * @author DDD Refactor
 * @date 2025-09-11
 */
@Service
public class SystemConfigDomainService {
    
    @Autowired
    private SystemConfigRepository systemConfigRepository;
    
    @Autowired
    private SystemConfigCacheService systemConfigCacheService;
    
    @Autowired
    private ConfigValidationService configValidationService;
    
    @Autowired
    private ApplicationEventPublisher eventPublisher;
    
    /**
     * 创建新的系统配置
     * @param configKey 配置键
     * @param configValue 配置值
     * @param configName 配置名称
     * @param configType 配置类型
     * @param configGroup 配置组
     * @param operator 操作者
     * @param tenantId 租户ID
     * @return 创建的系统配置
     */
    @Transactional
    public SystemConfig createConfig(ConfigKey configKey, ConfigValue configValue, 
            String configName, ConfigType configType, String configGroup, 
            String operator, String tenantId) {
        
        // 验证配置数据
        ValidationResult validation = configValidationService.validateConfiguration(
            configKey, configValue, configType, ConfigStatus.ACTIVE);
        
        if (!validation.isValid()) {
            throw new IllegalArgumentException("配置数据验证失败: " + validation.getErrors());
        }
        
        // 检查配置键是否已存在
        if (systemConfigRepository.existsByConfigKey(configKey.getValue(), tenantId)) {
            throw new IllegalArgumentException("配置键已存在: " + configKey.getValue());
        }
        
        // 创建配置实体
        SystemConfig systemConfig = new SystemConfig(configKey, configValue, configName, configType);
        systemConfig.setConfigGroup(configGroup);
        systemConfig.setTenantId(tenantId);
        systemConfig.setCreator(operator);
        systemConfig.setCreateTime(LocalDateTime.now());
        systemConfig.setConfigId(UUID.randomUUID().toString());
        
        // 设置系统级和只读属性
        systemConfig.setIsSystemLevel(configType.isSystemLevel());
        systemConfig.setIsReadonly(configType == ConfigType.SECURITY);
        
        // 保存到数据库
        SystemConfig savedConfig = systemConfigRepository.save(systemConfig);
        
        // 缓存配置
        systemConfigCacheService.cacheConfig(savedConfig);
        
        // 发布配置创建事件
        publishConfigChangedEvent(savedConfig, null, savedConfig.getConfigValue(), 
                ConfigurationChangedEvent.ChangeType.CREATED, operator);
        
        return savedConfig;
    }
    
    /**
     * 更新系统配置
     * @param configId 配置ID
     * @param newValue 新配置值
     * @param operator 操作者
     * @return 更新后的系统配置
     */
    @Transactional
    public SystemConfig updateConfig(String configId, ConfigValue newValue, String operator) {
        
        // 查找现有配置
        Optional<SystemConfig> existingConfigOpt = systemConfigRepository.findById(configId);
        if (!existingConfigOpt.isPresent()) {
            throw new IllegalArgumentException("配置不存在: " + configId);
        }
        
        SystemConfig existingConfig = existingConfigOpt.get();
        String oldValue = existingConfig.getConfigValue();
        
        // 验证新配置值
        if (existingConfig.getConfigKeyObj() != null) {
            ValidationResult validation = configValidationService.validateConfiguration(
                existingConfig.getConfigKeyObj(), newValue, 
                existingConfig.getConfigTypeEnum(), existingConfig.getConfigStatusEnum());
            
            if (!validation.isValid()) {
                throw new IllegalArgumentException("配置数据验证失败: " + validation.getErrors());
            }
        }
        
        // 更新配置值
        existingConfig.updateValue(newValue);
        existingConfig.setUpdator(operator);
        existingConfig.setUpdateTime(LocalDateTime.now());
        
        // 保存更新
        SystemConfig updatedConfig = systemConfigRepository.save(existingConfig);
        
        // 智能缓存更新
        systemConfigCacheService.smartCacheUpdate(updatedConfig);
        
        // 发布配置变更事件
        publishConfigChangedEvent(updatedConfig, oldValue, updatedConfig.getConfigValue(), 
                ConfigurationChangedEvent.ChangeType.VALUE_CHANGED, operator);
        
        return updatedConfig;
    }
    
    /**
     * 更新配置状态
     * @param configId 配置ID
     * @param newStatus 新状态
     * @param operator 操作者
     * @return 更新后的系统配置
     */
    @Transactional
    public SystemConfig updateConfigStatus(String configId, ConfigStatus newStatus, String operator) {
        
        Optional<SystemConfig> existingConfigOpt = systemConfigRepository.findById(configId);
        if (!existingConfigOpt.isPresent()) {
            throw new IllegalArgumentException("配置不存在: " + configId);
        }
        
        SystemConfig existingConfig = existingConfigOpt.get();
        ConfigStatus oldStatus = existingConfig.getConfigStatusEnum();
        
        // 更新状态
        existingConfig.setConfigStatusEnum(newStatus);
        existingConfig.setUpdator(operator);
        existingConfig.setUpdateTime(LocalDateTime.now());
        
        // 保存更新
        SystemConfig updatedConfig = systemConfigRepository.save(existingConfig);
        
        // 智能缓存更新
        systemConfigCacheService.smartCacheUpdate(updatedConfig);
        
        // 发布状态变更事件
        publishConfigChangedEvent(updatedConfig, 
                oldStatus != null ? oldStatus.getCode() : null, 
                newStatus.getCode(), 
                ConfigurationChangedEvent.ChangeType.STATUS_CHANGED, operator);
        
        return updatedConfig;
    }
    
    /**
     * 获取配置值（带缓存）
     * @param configKey 配置键
     * @param tenantId 租户ID
     * @return 配置值，如果不存在返回空
     */
    public Optional<SystemConfig> getConfig(String configKey, String tenantId) {
        
        // 先从缓存获取
        SystemConfig cachedConfig = systemConfigCacheService.getConfigFromCache(configKey, tenantId);
        if (cachedConfig != null) {
            return Optional.of(cachedConfig);
        }
        
        // 缓存未命中，从数据库获取
        Optional<SystemConfig> configOpt = systemConfigRepository.findByConfigKey(configKey, tenantId);
        
        // 缓存结果（如果存在）
        configOpt.ifPresent(systemConfigCacheService::cacheConfig);
        
        return configOpt;
    }
    
    /**
     * 使用配置键值对象获取配置
     * @param configKeyObj 配置键值对象
     * @param tenantId 租户ID
     * @return 配置值，如果不存在返回空
     */
    public Optional<SystemConfig> getConfig(ConfigKey configKeyObj, String tenantId) {
        return getConfig(configKeyObj.getValue(), tenantId);
    }
    
    /**
     * 获取类型安全的配置值
     * @param configKey 配置键
     * @param tenantId 租户ID
     * @param valueType 值类型
     * @return 类型化的配置值
     */
    public <T> Optional<T> getTypedConfigValue(String configKey, String tenantId, Class<T> valueType) {
        return getConfig(configKey, tenantId)
                .map(config -> config.getTypedValue(valueType));
    }
    
    /**
     * 根据配置类型获取配置列表
     * @param configType 配置类型
     * @param tenantId 租户ID
     * @return 配置列表
     */
    public List<SystemConfig> getConfigsByType(ConfigType configType, String tenantId) {
        
        // 先从缓存获取
        List<SystemConfig> cachedConfigs = systemConfigCacheService.getConfigsByTypeFromCache(configType, tenantId);
        if (cachedConfigs != null && !cachedConfigs.isEmpty()) {
            return cachedConfigs;
        }
        
        // 缓存未命中，从数据库获取
        List<SystemConfig> configs = systemConfigRepository.findByConfigType(configType, tenantId);
        
        // 缓存结果
        if (!configs.isEmpty()) {
            systemConfigCacheService.cacheConfigsByType(configType, tenantId, configs);
        }
        
        return configs;
    }
    
    /**
     * 根据配置组获取配置列表
     * @param configGroup 配置组
     * @param tenantId 租户ID
     * @return 配置列表
     */
    public List<SystemConfig> getConfigsByGroup(String configGroup, String tenantId) {
        
        // 先从缓存获取
        List<SystemConfig> cachedConfigs = systemConfigCacheService.getConfigsByGroupFromCache(configGroup, tenantId);
        if (cachedConfigs != null && !cachedConfigs.isEmpty()) {
            return cachedConfigs;
        }
        
        // 缓存未命中，从数据库获取
        List<SystemConfig> configs = systemConfigRepository.findByConfigGroup(configGroup, tenantId);
        
        // 缓存结果
        if (!configs.isEmpty()) {
            systemConfigCacheService.cacheConfigsByGroup(configGroup, tenantId, configs);
        }
        
        return configs;
    }
    
    /**
     * 获取系统级配置
     * @param tenantId 租户ID
     * @return 系统级配置列表
     */
    public List<SystemConfig> getSystemLevelConfigs(String tenantId) {
        return systemConfigRepository.findSystemLevelConfigs(tenantId);
    }
    
    /**
     * 获取激活状态的配置
     * @param tenantId 租户ID
     * @return 激活配置列表
     */
    public List<SystemConfig> getActiveConfigs(String tenantId) {
        return systemConfigRepository.findActiveConfigs(tenantId);
    }
    
    /**
     * 批量创建配置
     * @param configs 配置数据列表
     * @param operator 操作者
     * @param tenantId 租户ID
     * @return 创建的配置列表
     */
    @Transactional
    public List<SystemConfig> batchCreateConfigs(List<ConfigCreationData> configs, 
            String operator, String tenantId) {
        
        // 批量验证
        List<ConfigValidationService.ConfigValidationItem> validationItems = configs.stream()
                .map(data -> new ConfigValidationService.ConfigValidationItem(
                        data.getConfigKey(), data.getConfigValue(), 
                        data.getConfigType(), ConfigStatus.ACTIVE))
                .collect(Collectors.toList());
        
        ValidationResult batchValidation = configValidationService.validateBatchConfigurations(validationItems);
        if (!batchValidation.isValid()) {
            throw new IllegalArgumentException("批量配置验证失败: " + batchValidation.getErrors());
        }
        
        // 创建配置实体
        List<SystemConfig> systemConfigs = configs.stream()
                .map(data -> {
                    SystemConfig config = new SystemConfig(data.getConfigKey(), 
                            data.getConfigValue(), data.getConfigName(), data.getConfigType());
                    config.setConfigGroup(data.getConfigGroup());
                    config.setTenantId(tenantId);
                    config.setCreator(operator);
                    config.setCreateTime(LocalDateTime.now());
                    config.setConfigId(UUID.randomUUID().toString());
                    config.setIsSystemLevel(data.getConfigType().isSystemLevel());
                    return config;
                })
                .collect(Collectors.toList());
        
        // 批量保存
        List<SystemConfig> savedConfigs = systemConfigRepository.batchSave(systemConfigs);
        
        // 批量缓存
        systemConfigCacheService.batchCacheConfigs(savedConfigs);
        
        // 批量发布事件
        savedConfigs.forEach(config -> publishConfigChangedEvent(config, null, 
                config.getConfigValue(), ConfigurationChangedEvent.ChangeType.CREATED, operator));
        
        return savedConfigs;
    }
    
    /**
     * 删除配置
     * @param configId 配置ID
     * @param operator 操作者
     */
    @Transactional
    public void deleteConfig(String configId, String operator) {
        
        Optional<SystemConfig> configOpt = systemConfigRepository.findById(configId);
        if (!configOpt.isPresent()) {
            throw new IllegalArgumentException("配置不存在: " + configId);
        }
        
        SystemConfig config = configOpt.get();
        
        // 检查是否为只读配置
        if (config.getIsReadonly() != null && config.getIsReadonly()) {
            throw new IllegalArgumentException("只读配置不允许删除: " + config.getConfigKey());
        }
        
        // 删除数据库记录
        systemConfigRepository.deleteById(configId);
        
        // 清除缓存
        systemConfigCacheService.evictConfig(config.getConfigKey(), config.getTenantId());
        
        // 发布删除事件
        publishConfigChangedEvent(config, config.getConfigValue(), null, 
                ConfigurationChangedEvent.ChangeType.DELETED, operator);
    }
    
    /**
     * 发布配置变更事件
     */
    private void publishConfigChangedEvent(SystemConfig config, String oldValue, String newValue, 
            ConfigurationChangedEvent.ChangeType changeType, String operator) {
        
        ConfigurationChangedEvent event = new ConfigurationChangedEvent(
                UUID.randomUUID().toString(),
                config.getConfigId(),
                config.getConfigKey(),
                oldValue,
                newValue,
                config.getConfigType(),
                changeType,
                operator,
                config.getTenantId()
        );
        
        eventPublisher.publishEvent(event);
    }
    
    /**
     * 热重载配置
     * @param configKey 配置键
     * @param tenantId 租户ID
     * @return 是否重载成功
     */
    public boolean hotReloadConfig(String configKey, String tenantId) {
        try {
            // 从数据库重新加载配置
            Optional<SystemConfig> configOpt = systemConfigRepository.findByConfigKey(configKey, tenantId);
            
            if (configOpt.isPresent()) {
                SystemConfig config = configOpt.get();
                
                // 重新缓存配置
                systemConfigCacheService.cacheConfig(config);
                
                // 发布配置重载事件
                publishConfigChangedEvent(config, null, config.getConfigValue(), 
                        ConfigurationChangedEvent.ChangeType.RELOADED, "SYSTEM");
                
                return true;
            }
            
            return false;
        } catch (Exception e) {
            throw new RuntimeException("热重载配置失败: " + e.getMessage(), e);
        }
    }
    
    /**
     * 清理租户缓存
     * @param tenantId 租户ID
     */
    public void clearCache(String tenantId) {
        try {
            systemConfigCacheService.evictAllConfigsForTenant(tenantId);
        } catch (Exception e) {
            throw new RuntimeException("清理缓存失败: " + e.getMessage(), e);
        }
    }
    
    /**
     * 批量热重载配置
     * @param configKeys 配置键列表
     * @param tenantId 租户ID
     * @return 重载成功的配置数量
     */
    @Async
    public int batchHotReloadConfigs(List<String> configKeys, String tenantId) {
        int successCount = 0;
        
        for (String configKey : configKeys) {
            try {
                if (hotReloadConfig(configKey, tenantId)) {
                    successCount++;
                }
            } catch (Exception e) {
                // 记录日志，但继续处理其他配置
                System.err.println("热重载配置失败: " + configKey + ", 错误: " + e.getMessage());
            }
        }
        
        return successCount;
    }
    
    /**
     * 智能缓存刷新
     * 根据配置访问频率和重要性进行缓存刷新
     * @param tenantId 租户ID
     */
    @Async
    public void smartCacheRefresh(String tenantId) {
        try {
            // 获取系统级配置进行预热
            List<SystemConfig> systemConfigs = getSystemLevelConfigs(tenantId);
            systemConfigs.forEach(config -> {
                systemConfigCacheService.cacheConfig(config);
            });
            
            // 获取激活配置进行预热
            List<SystemConfig> activeConfigs = getActiveConfigs(tenantId);
            systemConfigCacheService.warmupCache(tenantId, activeConfigs);
            
        } catch (Exception e) {
            throw new RuntimeException("智能缓存刷新失败: " + e.getMessage(), e);
        }
    }
    
    /**
     * 获取缓存健康状态
     * @param tenantId 租户ID
     * @return 缓存统计信息
     */
    public SystemConfigCacheService.CacheStats getCacheHealthStatus(String tenantId) {
        return systemConfigCacheService.getCacheStats(tenantId);
    }
    
    /**
     * 配置创建数据
     */
    public static class ConfigCreationData {
        private final ConfigKey configKey;
        private final ConfigValue configValue;
        private final String configName;
        private final ConfigType configType;
        private final String configGroup;
        
        public ConfigCreationData(ConfigKey configKey, ConfigValue configValue, 
                String configName, ConfigType configType, String configGroup) {
            this.configKey = configKey;
            this.configValue = configValue;
            this.configName = configName;
            this.configType = configType;
            this.configGroup = configGroup;
        }
        
        public ConfigKey getConfigKey() { return configKey; }
        public ConfigValue getConfigValue() { return configValue; }
        public String getConfigName() { return configName; }
        public ConfigType getConfigType() { return configType; }
        public String getConfigGroup() { return configGroup; }
    }
}