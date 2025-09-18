package com.jiuxi.module.sys.app.command.dto;

import java.io.Serializable;

/**
 * 系统配置创建命令
 * 用于CQRS命令模式的系统配置创建请求
 * 
 * @author System Management
 * @date 2025-09-18
 */
public class SystemConfigCreateCommand implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 配置键
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
    private String configDesc;
    
    /**
     * 配置类型
     */
    private String configType;
    
    /**
     * 配置组
     */
    private String configGroup;
    
    /**
     * 租户ID
     */
    private String tenantId;
    
    // Getters and Setters
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
    }
    
    public String getTenantId() {
        return tenantId;
    }
    
    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }
    
    public String getConfigGroup() {
        return configGroup;
    }
    
    public void setConfigGroup(String configGroup) {
        this.configGroup = configGroup;
    }
}