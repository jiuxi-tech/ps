package com.jiuxi.module.sys.domain.event;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 配置变更领域事件
 * 当系统配置发生变更时触发
 * 
 * @author DDD Refactor
 * @date 2025-09-11
 */
public class ConfigurationChangedEvent {
    
    /**
     * 事件ID
     */
    private final String eventId;
    
    /**
     * 配置ID
     */
    private final String configId;
    
    /**
     * 配置键
     */
    private final String configKey;
    
    /**
     * 旧配置值
     */
    private final String oldValue;
    
    /**
     * 新配置值
     */
    private final String newValue;
    
    /**
     * 配置类型
     */
    private final String configType;
    
    /**
     * 变更类型
     */
    private final ChangeType changeType;
    
    /**
     * 变更操作者
     */
    private final String operator;
    
    /**
     * 事件发生时间
     */
    private final LocalDateTime occurredOn;
    
    /**
     * 租户ID
     */
    private final String tenantId;
    
    public ConfigurationChangedEvent(String eventId, String configId, String configKey, 
            String oldValue, String newValue, String configType, ChangeType changeType,
            String operator, String tenantId) {
        this.eventId = eventId;
        this.configId = configId;
        this.configKey = configKey;
        this.oldValue = oldValue;
        this.newValue = newValue;
        this.configType = configType;
        this.changeType = changeType;
        this.operator = operator;
        this.tenantId = tenantId;
        this.occurredOn = LocalDateTime.now();
    }
    
    /**
     * 变更类型枚举
     */
    public enum ChangeType {
        CREATED("CREATED", "创建"),
        UPDATED("UPDATED", "更新"),
        DELETED("DELETED", "删除"),
        STATUS_CHANGED("STATUS_CHANGED", "状态变更"),
        VALUE_CHANGED("VALUE_CHANGED", "值变更");
        
        private final String code;
        private final String description;
        
        ChangeType(String code, String description) {
            this.code = code;
            this.description = description;
        }
        
        public String getCode() {
            return code;
        }
        
        public String getDescription() {
            return description;
        }
    }
    
    public String getEventId() {
        return eventId;
    }
    
    public String getConfigId() {
        return configId;
    }
    
    public String getConfigKey() {
        return configKey;
    }
    
    public String getOldValue() {
        return oldValue;
    }
    
    public String getNewValue() {
        return newValue;
    }
    
    public String getConfigType() {
        return configType;
    }
    
    public ChangeType getChangeType() {
        return changeType;
    }
    
    public String getOperator() {
        return operator;
    }
    
    public LocalDateTime getOccurredOn() {
        return occurredOn;
    }
    
    public String getTenantId() {
        return tenantId;
    }
    
    /**
     * 检查是否为敏感配置变更
     */
    public boolean isSensitiveChange() {
        return configKey != null && (
            configKey.toLowerCase().contains("password") ||
            configKey.toLowerCase().contains("secret") ||
            configKey.toLowerCase().contains("key") ||
            configKey.toLowerCase().contains("token")
        );
    }
    
    /**
     * 检查是否为系统级配置变更
     */
    public boolean isSystemLevelChange() {
        return configKey != null && (
            configKey.startsWith("system.") ||
            configKey.startsWith("app.") ||
            configKey.startsWith("server.")
        );
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConfigurationChangedEvent that = (ConfigurationChangedEvent) o;
        return Objects.equals(eventId, that.eventId);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(eventId);
    }
    
    @Override
    public String toString() {
        return "ConfigurationChangedEvent{" +
                "eventId='" + eventId + '\'' +
                ", configKey='" + configKey + '\'' +
                ", changeType=" + changeType +
                ", occurredOn=" + occurredOn +
                '}';
    }
}