package com.jiuxi.module.sys.domain.entity;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 系统配置实体
 * 表示系统中的配置参数
 * 
 * @author System Management
 * @date 2025-09-06
 */
public class SystemConfig {
    
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
    
    // 构造器
    public SystemConfig() {
        this.status = "ACTIVE";
    }
    
    public SystemConfig(String configKey, String configValue, String configName) {
        this();
        this.configKey = configKey;
        this.configValue = configValue;
        this.configName = configName;
    }
    
    // 业务方法
    
    /**
     * 启用配置
     */
    public void enable() {
        this.status = "ACTIVE";
    }
    
    /**
     * 停用配置
     */
    public void disable() {
        this.status = "INACTIVE";
    }
    
    /**
     * 检查是否激活
     */
    public boolean isActive() {
        return "ACTIVE".equals(this.status);
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
}