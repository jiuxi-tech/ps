package com.jiuxi.module.sys.app.service;

import com.jiuxi.module.sys.domain.entity.ConfigType;
import com.jiuxi.module.sys.domain.entity.ConfigStatus;
import com.jiuxi.module.sys.domain.valueobject.ConfigKey;
import com.jiuxi.module.sys.domain.valueobject.ConfigValue;
import com.jiuxi.module.sys.domain.service.SystemConfigDomainService;
import com.jiuxi.module.sys.domain.service.SystemConfigCacheService;
import com.jiuxi.module.sys.domain.service.ConfigValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 参数配置应用服务
 * 专门处理系统参数配置的业务逻辑，提供便捷的参数操作接口
 * 
 * @author DDD Refactor
 * @date 2025-09-11
 */
@Service
public class ParameterConfigApplicationService {
    
    @Autowired
    private SystemConfigDomainService systemConfigDomainService;
    
    @Autowired
    private SystemConfigCacheService systemConfigCacheService;
    
    @Autowired
    private ConfigValidationService configValidationService;
    
    /**
     * 获取字符串类型参数
     * @param paramKey 参数键
     * @param tenantId 租户ID
     * @param defaultValue 默认值
     * @return 参数值
     */
    public String getStringParameter(String paramKey, String tenantId, String defaultValue) {
        String value = systemConfigDomainService.getTypedConfigValue(paramKey, tenantId, String.class)
                .orElse(defaultValue);
        return value != null ? value : defaultValue;
    }
    
    /**
     * 获取整数类型参数
     * @param paramKey 参数键
     * @param tenantId 租户ID
     * @param defaultValue 默认值
     * @return 参数值
     */
    public Integer getIntegerParameter(String paramKey, String tenantId, Integer defaultValue) {
        return systemConfigDomainService.getTypedConfigValue(paramKey, tenantId, Integer.class)
                .orElse(defaultValue);
    }
    
    /**
     * 获取长整数类型参数
     * @param paramKey 参数键
     * @param tenantId 租户ID
     * @param defaultValue 默认值
     * @return 参数值
     */
    public Long getLongParameter(String paramKey, String tenantId, Long defaultValue) {
        return systemConfigDomainService.getTypedConfigValue(paramKey, tenantId, Long.class)
                .orElse(defaultValue);
    }
    
    /**
     * 获取浮点数类型参数
     * @param paramKey 参数键
     * @param tenantId 租户ID
     * @param defaultValue 默认值
     * @return 参数值
     */
    public Double getDoubleParameter(String paramKey, String tenantId, Double defaultValue) {
        return systemConfigDomainService.getTypedConfigValue(paramKey, tenantId, Double.class)
                .orElse(defaultValue);
    }
    
    /**
     * 获取布尔类型参数
     * @param paramKey 参数键
     * @param tenantId 租户ID
     * @param defaultValue 默认值
     * @return 参数值
     */
    public Boolean getBooleanParameter(String paramKey, String tenantId, Boolean defaultValue) {
        return systemConfigDomainService.getTypedConfigValue(paramKey, tenantId, Boolean.class)
                .orElse(defaultValue);
    }
    
    /**
     * 设置字符串类型参数
     * @param paramKey 参数键
     * @param value 参数值
     * @param tenantId 租户ID
     * @param operator 操作者
     * @return 是否设置成功
     */
    public boolean setStringParameter(String paramKey, String value, String tenantId, String operator) {
        try {
            ConfigValue configValue = ConfigValue.ofString(value);
            
            // 检查参数是否已存在
            Optional<com.jiuxi.module.sys.domain.entity.SystemConfig> existingConfig = 
                    systemConfigDomainService.getConfig(paramKey, tenantId);
            
            if (existingConfig.isPresent()) {
                // 更新现有参数
                systemConfigDomainService.updateConfig(existingConfig.get().getConfigId(), configValue, operator);
            } else {
                // 创建新参数
                ConfigKey configKey = new ConfigKey(paramKey);
                systemConfigDomainService.createConfig(configKey, configValue, 
                        "系统参数: " + paramKey, ConfigType.APPLICATION, "params", operator, tenantId);
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * 设置整数类型参数
     * @param paramKey 参数键
     * @param value 参数值
     * @param tenantId 租户ID
     * @param operator 操作者
     * @return 是否设置成功
     */
    public boolean setIntegerParameter(String paramKey, Integer value, String tenantId, String operator) {
        try {
            ConfigValue configValue = ConfigValue.ofNumber(value);
            return updateOrCreateParameter(paramKey, configValue, tenantId, operator);
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * 设置布尔类型参数
     * @param paramKey 参数键
     * @param value 参数值
     * @param tenantId 租户ID
     * @param operator 操作者
     * @return 是否设置成功
     */
    public boolean setBooleanParameter(String paramKey, Boolean value, String tenantId, String operator) {
        try {
            ConfigValue configValue = ConfigValue.ofBoolean(value);
            return updateOrCreateParameter(paramKey, configValue, tenantId, operator);
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * 批量获取参数
     * @param paramKeys 参数键列表
     * @param tenantId 租户ID
     * @return 参数键值对Map
     */
    public Map<String, String> getBatchParameters(List<String> paramKeys, String tenantId) {
        Map<String, String> parameters = new HashMap<>();
        
        for (String paramKey : paramKeys) {
            String value = getStringParameter(paramKey, tenantId, null);
            if (value != null) {
                parameters.put(paramKey, value);
            }
        }
        
        return parameters;
    }
    
    /**
     * 批量设置参数
     * @param parameters 参数键值对Map
     * @param tenantId 租户ID
     * @param operator 操作者
     * @return 设置成功的参数数量
     */
    public int setBatchParameters(Map<String, String> parameters, String tenantId, String operator) {
        int successCount = 0;
        
        for (Map.Entry<String, String> entry : parameters.entrySet()) {
            if (setStringParameter(entry.getKey(), entry.getValue(), tenantId, operator)) {
                successCount++;
            }
        }
        
        return successCount;
    }
    
    /**
     * 获取应用级参数配置
     * @param tenantId 租户ID
     * @return 应用级参数Map
     */
    public Map<String, String> getApplicationParameters(String tenantId) {
        Map<String, String> appParams = new HashMap<>();
        
        systemConfigDomainService.getConfigsByType(ConfigType.APPLICATION, tenantId)
                .forEach(config -> {
                    if (config.isActive() && config.getConfigKey() != null && config.getConfigValue() != null) {
                        appParams.put(config.getConfigKey(), config.getConfigValue());
                    }
                });
        
        return appParams;
    }
    
    /**
     * 获取业务级参数配置
     * @param tenantId 租户ID
     * @return 业务级参数Map
     */
    public Map<String, String> getBusinessParameters(String tenantId) {
        Map<String, String> businessParams = new HashMap<>();
        
        systemConfigDomainService.getConfigsByType(ConfigType.BUSINESS, tenantId)
                .forEach(config -> {
                    if (config.isActive() && config.getConfigKey() != null && config.getConfigValue() != null) {
                        businessParams.put(config.getConfigKey(), config.getConfigValue());
                    }
                });
        
        return businessParams;
    }
    
    /**
     * 重置参数为默认值
     * @param paramKey 参数键
     * @param defaultValue 默认值
     * @param tenantId 租户ID
     * @param operator 操作者
     * @return 是否重置成功
     */
    public boolean resetParameterToDefault(String paramKey, String defaultValue, String tenantId, String operator) {
        return setStringParameter(paramKey, defaultValue, tenantId, operator);
    }
    
    /**
     * 删除参数
     * @param paramKey 参数键
     * @param tenantId 租户ID
     * @param operator 操作者
     * @return 是否删除成功
     */
    public boolean deleteParameter(String paramKey, String tenantId, String operator) {
        try {
            Optional<com.jiuxi.module.sys.domain.entity.SystemConfig> existingConfig = 
                    systemConfigDomainService.getConfig(paramKey, tenantId);
            
            if (existingConfig.isPresent()) {
                systemConfigDomainService.deleteConfig(existingConfig.get().getConfigId(), operator);
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * 检查参数是否存在
     * @param paramKey 参数键
     * @param tenantId 租户ID
     * @return 是否存在
     */
    public boolean parameterExists(String paramKey, String tenantId) {
        return systemConfigDomainService.getConfig(paramKey, tenantId).isPresent();
    }
    
    /**
     * 获取参数的配置信息（包含元数据）
     * @param paramKey 参数键
     * @param tenantId 租户ID
     * @return 参数配置信息
     */
    public ParameterInfo getParameterInfo(String paramKey, String tenantId) {
        Optional<com.jiuxi.module.sys.domain.entity.SystemConfig> configOpt = 
                systemConfigDomainService.getConfig(paramKey, tenantId);
        
        if (!configOpt.isPresent()) {
            return null;
        }
        
        com.jiuxi.module.sys.domain.entity.SystemConfig config = configOpt.get();
        return new ParameterInfo(
                config.getConfigKey(),
                config.getConfigValue(),
                config.getDataType(),
                config.getConfigName(),
                config.getConfigDesc(),
                config.isActive(),
                config.getUpdateTime(),
                config.getUpdator()
        );
    }
    
    /**
     * 验证参数值
     * @param paramKey 参数键
     * @param paramValue 参数值
     * @param dataType 数据类型
     * @return 验证结果
     */
    public ConfigValidationService.ValidationResult validateParameter(String paramKey, String paramValue, String dataType) {
        try {
            ConfigKey configKey = new ConfigKey(paramKey);
            ConfigValue configValue = new ConfigValue(paramValue, dataType);
            return configValidationService.validateConfiguration(configKey, configValue, ConfigType.APPLICATION, ConfigStatus.ACTIVE);
        } catch (Exception e) {
            return new ConfigValidationService.ValidationResult(
                    List.of("参数验证失败: " + e.getMessage()), 
                    List.of()
            );
        }
    }
    
    /**
     * 更新或创建参数的通用方法
     */
    private boolean updateOrCreateParameter(String paramKey, ConfigValue configValue, String tenantId, String operator) {
        try {
            Optional<com.jiuxi.module.sys.domain.entity.SystemConfig> existingConfig = 
                    systemConfigDomainService.getConfig(paramKey, tenantId);
            
            if (existingConfig.isPresent()) {
                systemConfigDomainService.updateConfig(existingConfig.get().getConfigId(), configValue, operator);
            } else {
                ConfigKey configKey = new ConfigKey(paramKey);
                systemConfigDomainService.createConfig(configKey, configValue, 
                        "系统参数: " + paramKey, ConfigType.APPLICATION, "params", operator, tenantId);
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * 参数信息封装类
     */
    public static class ParameterInfo {
        private final String key;
        private final String value;
        private final String dataType;
        private final String name;
        private final String description;
        private final boolean active;
        private final java.time.LocalDateTime lastModified;
        private final String lastModifier;
        
        public ParameterInfo(String key, String value, String dataType, String name, 
                String description, boolean active, java.time.LocalDateTime lastModified, String lastModifier) {
            this.key = key;
            this.value = value;
            this.dataType = dataType;
            this.name = name;
            this.description = description;
            this.active = active;
            this.lastModified = lastModified;
            this.lastModifier = lastModifier;
        }
        
        // Getters
        public String getKey() { return key; }
        public String getValue() { return value; }
        public String getDataType() { return dataType; }
        public String getName() { return name; }
        public String getDescription() { return description; }
        public boolean isActive() { return active; }
        public java.time.LocalDateTime getLastModified() { return lastModified; }
        public String getLastModifier() { return lastModifier; }
        
        @Override
        public String toString() {
            return String.format("ParameterInfo{key='%s', value='%s', dataType='%s', active=%s}", 
                    key, value, dataType, active);
        }
    }
}