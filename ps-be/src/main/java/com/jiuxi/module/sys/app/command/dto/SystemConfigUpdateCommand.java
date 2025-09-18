package com.jiuxi.module.sys.app.command.dto;

import java.io.Serializable;

/**
 * 系统配置更新命令
 * 用于CQRS命令模式的系统配置更新请求
 * 
 * @author System Management
 * @date 2025-09-18
 */
public class SystemConfigUpdateCommand implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 配置ID
     */
    private String configId;
    
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
     * 状态（ACTIVE-启用, INACTIVE-禁用）
     */
    private String status;
    
    /**
     * 更新人
     */
    private String updator;
    
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
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getUpdator() {
        return updator;
    }
    
    public void setUpdator(String updator) {
        this.updator = updator;
    }
}