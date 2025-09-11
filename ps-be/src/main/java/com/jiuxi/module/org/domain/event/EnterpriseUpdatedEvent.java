package com.jiuxi.module.org.domain.event;

import java.time.LocalDateTime;

/**
 * 企业更新事件
 * 
 * @author DDD Refactor
 * @date 2025-09-11
 */
public class EnterpriseUpdatedEvent {
    
    /**
     * 企业ID
     */
    private final String entId;
    
    /**
     * 企业全称
     */
    private final String entFullName;
    
    /**
     * 更新字段
     */
    private final String updatedFields;
    
    /**
     * 租户ID
     */
    private final String tenantId;
    
    /**
     * 更新者
     */
    private final String updator;
    
    /**
     * 事件发生时间
     */
    private final LocalDateTime occurredOn;
    
    public EnterpriseUpdatedEvent(String entId, String entFullName, String updatedFields, 
                                 String tenantId, String updator) {
        this.entId = entId;
        this.entFullName = entFullName;
        this.updatedFields = updatedFields;
        this.tenantId = tenantId;
        this.updator = updator;
        this.occurredOn = LocalDateTime.now();
    }
    
    public String getEntId() {
        return entId;
    }
    
    public String getEntFullName() {
        return entFullName;
    }
    
    public String getUpdatedFields() {
        return updatedFields;
    }
    
    public String getTenantId() {
        return tenantId;
    }
    
    public String getUpdator() {
        return updator;
    }
    
    public LocalDateTime getOccurredOn() {
        return occurredOn;
    }
    
    @Override
    public String toString() {
        return "EnterpriseUpdatedEvent{" +
                "entId='" + entId + '\'' +
                ", entFullName='" + entFullName + '\'' +
                ", updatedFields='" + updatedFields + '\'' +
                ", tenantId='" + tenantId + '\'' +
                ", updator='" + updator + '\'' +
                ", occurredOn=" + occurredOn +
                '}';
    }
}