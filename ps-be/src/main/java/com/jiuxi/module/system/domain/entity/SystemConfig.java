package com.jiuxi.module.system.domain.entity;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * @ClassName: SystemConfig
 * @Description: 系统配置聚合根 - 系统配置管理领域实体
 * @Author: System
 * @Date: 2025/01/07
 * @Copyright: 2025 www.jiuxi.com Inc. All rights reserved.
 */
public class SystemConfig {
    
    /**
     * 配置ID (聚合根标识)
     */
    private String configId;
    
    /**
     * 配置键 (业务唯一标识)
     */
    private String configKey;
    
    /**
     * 配置值
     */
    private String configValue;
    
    /**
     * 配置名称
     */
    private String configName;
    
    /**
     * 配置描述
     */
    private String description;
    
    /**
     * 配置分组
     */
    private String configGroup;
    
    /**
     * 配置类型
     */
    private ConfigType configType;
    
    /**
     * 数据类型 (STRING, INTEGER, BOOLEAN, JSON等)
     */
    private ConfigDataType dataType;
    
    /**
     * 是否系统内置 (系统内置配置不可删除)
     */
    private Boolean isSystem;
    
    /**
     * 是否加密存储
     */
    private Boolean isEncrypted;
    
    /**
     * 是否可编辑
     */
    private Boolean isEditable;
    
    /**
     * 配置状态
     */
    private ConfigStatus status;
    
    /**
     * 排序号
     */
    private Integer sortOrder;
    
    /**
     * 默认值
     */
    private String defaultValue;
    
    /**
     * 验证规则 (正则表达式或JSON Schema)
     */
    private String validationRule;
    
    /**
     * 创建信息
     */
    private String creator;
    private LocalDateTime createTime;
    
    /**
     * 更新信息
     */
    private String updator;
    private LocalDateTime updateTime;
    
    /**
     * 租户ID
     */
    private String tenantId;
    
    // 构造方法
    public SystemConfig() {
    }
    
    public SystemConfig(String configKey, String configValue, String configName, ConfigType configType) {
        this.configKey = configKey;
        this.configValue = configValue;
        this.configName = configName;
        this.configType = configType;
        this.dataType = ConfigDataType.STRING;
        this.isSystem = false;
        this.isEncrypted = false;
        this.isEditable = true;
        this.status = ConfigStatus.ENABLED;
        this.createTime = LocalDateTime.now();
    }
    
    // 领域方法
    
    /**
     * 更新配置值
     */
    public void updateValue(String newValue) {
        if (!isEditable) {
            throw new IllegalStateException("配置项不可编辑: " + configKey);
        }
        this.configValue = newValue;
        this.updateTime = LocalDateTime.now();
    }
    
    /**
     * 启用配置
     */
    public void enable() {
        this.status = ConfigStatus.ENABLED;
        this.updateTime = LocalDateTime.now();
    }
    
    /**
     * 禁用配置
     */
    public void disable() {
        if (isSystem) {
            throw new IllegalStateException("系统配置不能禁用: " + configKey);
        }
        this.status = ConfigStatus.DISABLED;
        this.updateTime = LocalDateTime.now();
    }
    
    /**
     * 设置为系统配置
     */
    public void markAsSystem() {
        this.isSystem = true;
        this.isEditable = false;
        this.updateTime = LocalDateTime.now();
    }
    
    /**
     * 设置加密存储
     */
    public void enableEncryption() {
        this.isEncrypted = true;
        this.updateTime = LocalDateTime.now();
    }
    
    /**
     * 验证配置值
     */
    public boolean validateValue(String value) {
        if (validationRule == null || validationRule.isEmpty()) {
            return true;
        }
        
        try {
            return value.matches(validationRule);
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * 获取类型化的配置值
     */
    public Object getTypedValue() {
        if (configValue == null) {
            return defaultValue;
        }
        
        switch (dataType) {
            case INTEGER:
                return Integer.valueOf(configValue);
            case BOOLEAN:
                return Boolean.valueOf(configValue);
            case DOUBLE:
                return Double.valueOf(configValue);
            case JSON:
            case STRING:
            default:
                return configValue;
        }
    }
    
    /**
     * 检查配置是否可用
     */
    public boolean isEnabled() {
        return ConfigStatus.ENABLED.equals(this.status);
    }
    
    /**
     * 检查是否可删除
     */
    public boolean isDeletable() {
        return !isSystem;
    }
    
    // Getters and Setters
    public String getConfigId() {
        return configId;
    }
    
    public void setConfigId(String configId) {
        this.configId = configId;
    }
    
    public String getConfigKey() {
        return configKey;
    }
    
    public void setConfigKey(String configKey) {
        this.configKey = configKey;
    }
    
    public String getConfigValue() {
        return configValue;
    }
    
    public void setConfigValue(String configValue) {
        this.configValue = configValue;
    }
    
    public String getConfigName() {
        return configName;
    }
    
    public void setConfigName(String configName) {
        this.configName = configName;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getConfigGroup() {
        return configGroup;
    }
    
    public void setConfigGroup(String configGroup) {
        this.configGroup = configGroup;
    }
    
    public ConfigType getConfigType() {
        return configType;
    }
    
    public void setConfigType(ConfigType configType) {
        this.configType = configType;
    }
    
    public ConfigDataType getDataType() {
        return dataType;
    }
    
    public void setDataType(ConfigDataType dataType) {
        this.dataType = dataType;
    }
    
    public Boolean getIsSystem() {
        return isSystem;
    }
    
    public void setIsSystem(Boolean isSystem) {
        this.isSystem = isSystem;
    }
    
    public Boolean getIsEncrypted() {
        return isEncrypted;
    }
    
    public void setIsEncrypted(Boolean isEncrypted) {
        this.isEncrypted = isEncrypted;
    }
    
    public Boolean getIsEditable() {
        return isEditable;
    }
    
    public void setIsEditable(Boolean isEditable) {
        this.isEditable = isEditable;
    }
    
    public ConfigStatus getStatus() {
        return status;
    }
    
    public void setStatus(ConfigStatus status) {
        this.status = status;
    }
    
    public Integer getSortOrder() {
        return sortOrder;
    }
    
    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }
    
    public String getDefaultValue() {
        return defaultValue;
    }
    
    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }
    
    public String getValidationRule() {
        return validationRule;
    }
    
    public void setValidationRule(String validationRule) {
        this.validationRule = validationRule;
    }
    
    public String getCreator() {
        return creator;
    }
    
    public void setCreator(String creator) {
        this.creator = creator;
    }
    
    public LocalDateTime getCreateTime() {
        return createTime;
    }
    
    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }
    
    public String getUpdator() {
        return updator;
    }
    
    public void setUpdator(String updator) {
        this.updator = updator;
    }
    
    public LocalDateTime getUpdateTime() {
        return updateTime;
    }
    
    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }
    
    public String getTenantId() {
        return tenantId;
    }
    
    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SystemConfig that = (SystemConfig) o;
        return Objects.equals(configId, that.configId);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(configId);
    }
    
    @Override
    public String toString() {
        return "SystemConfig{" +
                "configId='" + configId + '\'' +
                ", configKey='" + configKey + '\'' +
                ", configName='" + configName + '\'' +
                ", status=" + status +
                '}';
    }
}