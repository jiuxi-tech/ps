package com.jiuxi.module.sys.infra.persistence.entity;

import com.baomidou.mybatisplus.annotation.*;
import java.time.LocalDateTime;

/**
 * 系统配置持久化对象
 * 对应数据库中的系统配置表
 * 
 * @author System Management
 * @date 2025-09-06
 */
@TableName("sys_config")
public class SystemConfigPO {
    
    /**
     * 配置ID
     */
    @TableId(value = "config_id", type = IdType.ASSIGN_ID)
    private String configId;
    
    /**
     * 配置键
     */
    @TableField("config_key")
    private String configKey;
    
    /**
     * 配置值
     */
    @TableField("config_value")
    private String configValue;
    
    /**
     * 配置名称
     */
    @TableField("config_name")
    private String configName;
    
    /**
     * 配置描述
     */
    @TableField("config_desc")
    private String configDesc;
    
    /**
     * 配置类型
     */
    @TableField("config_type")
    private String configType;
    
    /**
     * 状态（ACTIVE-启用, INACTIVE-禁用）
     */
    @TableField("status")
    private String status;
    
    /**
     * 创建人
     */
    @TableField("creator")
    private String creator;
    
    /**
     * 创建时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;
    
    /**
     * 更新人
     */
    @TableField("updator")
    private String updator;
    
    /**
     * 更新时间
     */
    @TableField("update_time")
    private LocalDateTime updateTime;
    
    /**
     * 租户ID
     */
    @TableField("tenant_id")
    private String tenantId;
    
    /**
     * 逻辑删除标识(0-未删除, 1-已删除)
     */
    @TableLogic
    @TableField("deleted")
    private Integer deleted;
    
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
    
    public Integer getDeleted() {
        return deleted;
    }
    
    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }
}