package com.jiuxi.module.sys.domain.entity;

import com.jiuxi.module.sys.domain.valueobject.ConfigKey;
import com.jiuxi.module.sys.domain.valueobject.ConfigValue;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 系统配置实体
 * 表示系统中的配置参数，使用DDD模式设计
 * 
 * @author System Management (Enhanced by DDD Refactor)
 * @date 2025-09-06
 * @version 2.0 - Enhanced with DDD value objects and domain logic
 */
public class SystemConfig {
    
    /**
     * 配置ID
     */
    private String configId;
    
    /**
     * 配置键值对象
     */
    private ConfigKey configKeyObj;
    
    /**
     * 配置值对象
     */
    private ConfigValue configValueObj;
    
    /**
     * 配置键（兼容性字段）
     */
    private String configKey;
    
    /**
     * 配置值（兼容性字段）
     */
    private String configValue;
    
    /**
     * 配置名称
     */
    private String configName;
    
    /**
     * 配置描述
     */
    private String configDesc;
    
    /**
     * 配置类型枚举
     */
    private ConfigType configTypeEnum;
    
    /**
     * 配置状态枚举
     */
    private ConfigStatus configStatusEnum;
    
    /**
     * 配置类型（兼容性字段）
     */
    private String configType;
    
    /**
     * 状态（兼容性字段）
     */
    private String status;
    
    /**
     * 数据类型（STRING, NUMBER, BOOLEAN, JSON）
     */
    private String dataType;
    
    /**
     * 配置组
     */
    private String configGroup;
    
    /**
     * 排序序号
     */
    private Integer orderIndex;
    
    /**
     * 是否系统级配置
     */
    private Boolean isSystemLevel;
    
    /**
     * 是否只读配置
     */
    private Boolean isReadonly;
    
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
    
    /**
     * 是否加密存储
     */
    private Boolean isEncrypted;
    
    /**
     * 是否可编辑
     */
    private Boolean isEditable;
    
    /**
     * 默认值
     */
    private String defaultValue;
    
    /**
     * 验证规则 (正则表达式或JSON Schema)
     */
    private String validationRule;
    
    /**
     * 排序号
     */
    private Integer sortOrder;
    
    // 构造器
    public SystemConfig() {
        this.status = "ACTIVE";
        this.configStatusEnum = ConfigStatus.ACTIVE;
        this.configTypeEnum = ConfigType.SYSTEM;
        this.dataType = "STRING";
        this.isSystemLevel = false;
        this.isReadonly = false;
        this.orderIndex = 0;
        this.isEncrypted = false;
        this.isEditable = true;
        this.sortOrder = 0;
    }
    
    public SystemConfig(String configKey, String configValue, String configName) {
        this();
        this.configKey = configKey;
        this.configValue = configValue;
        this.configName = configName;
        // 创建值对象
        if (configKey != null) {
            this.configKeyObj = new ConfigKey(configKey);
        }
        if (configValue != null) {
            this.configValueObj = ConfigValue.ofString(configValue);
        }
    }
    
    /**
     * 使用值对象构造
     * @param configKey 配置键对象
     * @param configValue 配置值对象
     * @param configName 配置名称
     * @param configType 配置类型
     */
    public SystemConfig(ConfigKey configKey, ConfigValue configValue, String configName, ConfigType configType) {
        this();
        this.configKeyObj = configKey;
        this.configValueObj = configValue;
        this.configName = configName;
        this.configTypeEnum = configType;
        // 同步兼容性字段
        if (configKey != null) {
            this.configKey = configKey.getValue();
        }
        if (configValue != null) {
            this.configValue = configValue.getValue();
            this.dataType = configValue.getDataType();
        }
        if (configType != null) {
            this.configType = configType.getCode();
            this.isSystemLevel = configType.isSystemLevel();
        }
    }
    
    // 业务方法
    
    /**
     * 启用配置
     */
    public void enable() {
        this.configStatusEnum = ConfigStatus.ACTIVE;
        this.status = "ACTIVE";
    }
    
    /**
     * 停用配置
     */
    public void disable() {
        this.configStatusEnum = ConfigStatus.INACTIVE;
        this.status = "INACTIVE";
    }
    
    /**
     * 设置为草稿状态
     */
    public void setDraft() {
        this.configStatusEnum = ConfigStatus.DRAFT;
        this.status = "DRAFT";
    }
    
    /**
     * 设置为待审核状态
     */
    public void setPending() {
        this.configStatusEnum = ConfigStatus.PENDING;
        this.status = "PENDING";
    }
    
    /**
     * 检查是否激活
     */
    public boolean isActive() {
        return this.configStatusEnum != null ? 
            this.configStatusEnum.isActive() : "ACTIVE".equals(this.status);
    }
    
    /**
     * 检查是否可编辑
     */
    public boolean isEditable() {
        if (this.isReadonly != null && this.isReadonly) {
            return false;
        }
        return this.configStatusEnum != null ? this.configStatusEnum.isEditable() : true;
    }
    
    /**
     * 更新配置值
     * @param newValue 新配置值
     * @param dataType 数据类型
     */
    public void updateValue(String newValue, String dataType) {
        if (!isEditable()) {
            throw new IllegalStateException("配置不可编辑");
        }
        
        this.configValue = newValue;
        this.dataType = dataType;
        
        // 更新值对象
        if (newValue != null) {
            this.configValueObj = new ConfigValue(newValue, dataType);
        }
    }
    
    /**
     * 更新配置值（使用值对象）
     * @param newValue 新配置值对象
     */
    public void updateValue(ConfigValue newValue) {
        if (!isEditable()) {
            throw new IllegalStateException("配置不可编辑");
        }
        
        this.configValueObj = newValue;
        if (newValue != null) {
            this.configValue = newValue.getValue();
            this.dataType = newValue.getDataType();
        }
    }
    
    /**
     * 获取配置的根级别
     */
    public String getRootLevel() {
        return this.configKeyObj != null ? 
            this.configKeyObj.getRootLevel() : 
            (this.configKey != null ? this.configKey.split("\\.")[0] : null);
    }
    
    /**
     * 检查是否是系统级配置
     */
    public boolean isSystemLevel() {
        if (this.isSystemLevel != null) {
            return this.isSystemLevel;
        }
        if (this.configTypeEnum != null) {
            return this.configTypeEnum.isSystemLevel();
        }
        if (this.configKeyObj != null) {
            return this.configKeyObj.isSystemLevel();
        }
        return false;
    }
    
    /**
     * 获取类型安全的配置值
     */
    public <T> T getTypedValue(Class<T> type) {
        if (this.configValueObj == null) {
            return null;
        }
        
        if (type == String.class) {
            return type.cast(this.configValueObj.asString());
        } else if (type == Integer.class) {
            return type.cast(this.configValueObj.asInteger());
        } else if (type == Long.class) {
            return type.cast(this.configValueObj.asLong());
        } else if (type == Double.class) {
            return type.cast(this.configValueObj.asDouble());
        } else if (type == Boolean.class) {
            return type.cast(this.configValueObj.asBoolean());
        }
        
        return null;
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
        // 同步更新值对象
        if (configKey != null) {
            try {
                this.configKeyObj = new ConfigKey(configKey);
            } catch (Exception e) {
                // 保持向后兼容，如果键格式不正确则不创建值对象
                this.configKeyObj = null;
            }
        } else {
            this.configKeyObj = null;
        }
    }
    
    public String getConfigValue() {
        return configValue;
    }
    
    public void setConfigValue(String configValue) {
        this.configValue = configValue;
        // 同步更新值对象
        if (configValue != null && this.dataType != null) {
            try {
                this.configValueObj = new ConfigValue(configValue, this.dataType);
            } catch (Exception e) {
                // 保持向后兼容，如果值格式不正确则使用字符串类型
                this.configValueObj = ConfigValue.ofString(configValue);
            }
        } else if (configValue != null) {
            this.configValueObj = ConfigValue.ofString(configValue);
        } else {
            this.configValueObj = null;
        }
    }
    
    public String getConfigName() {
        return configName;
    }
    
    public void setConfigName(String configName) {
        this.configName = configName;
    }
    
    public String getConfigDesc() {
        return configDesc;
    }
    
    public void setConfigDesc(String configDesc) {
        this.configDesc = configDesc;
    }
    
    public String getConfigType() {
        return configType;
    }
    
    public void setConfigType(String configType) {
        this.configType = configType;
        // 同步更新枚举
        if (configType != null) {
            try {
                this.configTypeEnum = ConfigType.fromCode(configType);
                this.isSystemLevel = this.configTypeEnum.isSystemLevel();
            } catch (Exception e) {
                // 保持向后兼容，如果类型代码不正确则设为默认值
                this.configTypeEnum = ConfigType.SYSTEM;
                this.isSystemLevel = false;
            }
        } else {
            this.configTypeEnum = ConfigType.SYSTEM;
        }
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
        // 同步更新枚举
        if (status != null) {
            try {
                this.configStatusEnum = ConfigStatus.fromCode(status);
            } catch (Exception e) {
                // 保持向后兼容，如果状态代码不正确则设为默认值
                this.configStatusEnum = ConfigStatus.ACTIVE;
            }
        } else {
            this.configStatusEnum = ConfigStatus.ACTIVE;
        }
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
    
    // 新增的值对象和枚举getter/setter
    
    public ConfigKey getConfigKeyObj() {
        return configKeyObj;
    }
    
    public void setConfigKeyObj(ConfigKey configKeyObj) {
        this.configKeyObj = configKeyObj;
        if (configKeyObj != null) {
            this.configKey = configKeyObj.getValue();
        }
    }
    
    public ConfigValue getConfigValueObj() {
        return configValueObj;
    }
    
    public void setConfigValueObj(ConfigValue configValueObj) {
        this.configValueObj = configValueObj;
        if (configValueObj != null) {
            this.configValue = configValueObj.getValue();
            this.dataType = configValueObj.getDataType();
        }
    }
    
    public ConfigType getConfigTypeEnum() {
        return configTypeEnum;
    }
    
    public void setConfigTypeEnum(ConfigType configTypeEnum) {
        this.configTypeEnum = configTypeEnum;
        if (configTypeEnum != null) {
            this.configType = configTypeEnum.getCode();
            this.isSystemLevel = configTypeEnum.isSystemLevel();
        }
    }
    
    public ConfigStatus getConfigStatusEnum() {
        return configStatusEnum;
    }
    
    public void setConfigStatusEnum(ConfigStatus configStatusEnum) {
        this.configStatusEnum = configStatusEnum;
        if (configStatusEnum != null) {
            this.status = configStatusEnum.getCode();
        }
    }
    
    public String getDataType() {
        return dataType;
    }
    
    public void setDataType(String dataType) {
        this.dataType = dataType;
        // 如果已有配置值，则重新创建值对象以应用新的数据类型
        if (this.configValue != null) {
            try {
                this.configValueObj = new ConfigValue(this.configValue, dataType);
            } catch (Exception e) {
                this.configValueObj = ConfigValue.ofString(this.configValue);
            }
        }
    }
    
    public String getConfigGroup() {
        return configGroup;
    }
    
    public void setConfigGroup(String configGroup) {
        this.configGroup = configGroup;
    }
    
    public Integer getOrderIndex() {
        return orderIndex;
    }
    
    public void setOrderIndex(Integer orderIndex) {
        this.orderIndex = orderIndex;
    }
    
    public Boolean getIsSystemLevel() {
        return isSystemLevel;
    }
    
    public void setIsSystemLevel(Boolean isSystemLevel) {
        this.isSystemLevel = isSystemLevel;
    }
    
    public Boolean getIsReadonly() {
        return isReadonly;
    }
    
    public void setIsReadonly(Boolean isReadonly) {
        this.isReadonly = isReadonly;
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
    
    public Integer getSortOrder() {
        return sortOrder;
    }
    
    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }
    
    /**
     * 设置为系统配置
     */
    public void markAsSystem() {
        this.isSystemLevel = true;
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
     * 检查是否可删除
     */
    public boolean isDeletable() {
        return !isSystemLevel;
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
}