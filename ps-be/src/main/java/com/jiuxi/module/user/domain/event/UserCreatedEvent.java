package com.jiuxi.module.user.domain.event;

import com.jiuxi.module.user.domain.entity.UserCategory;

/**
 * 用户创建事件
 * 
 * @author DDD Refactor Phase 4.3.1.1
 * @date 2025-09-11
 */
public class UserCreatedEvent extends UserEvent {
    
    /**
     * 用户名称
     */
    private final String personName;
    
    /**
     * 用户类别
     */
    private final UserCategory category;
    
    /**
     * 创建者
     */
    private final String creator;
    
    /**
     * 租户ID
     */
    private final String tenantId;
    
    public UserCreatedEvent(String personId, String personName, UserCategory category, String creator, String tenantId) {
        super(personId);
        this.personName = personName;
        this.category = category;
        this.creator = creator;
        this.tenantId = tenantId;
    }
    
    public String getPersonName() {
        return personName;
    }
    
    public UserCategory getCategory() {
        return category;
    }
    
    public String getCreator() {
        return creator;
    }
    
    public String getTenantId() {
        return tenantId;
    }
    
    @Override
    public String getEventType() {
        return "USER_CREATED";
    }
    
    @Override
    public String getEventDescription() {
        return String.format("用户 %s 已创建，类别：%s，创建者：%s", 
                           personName, 
                           category != null ? category.getDescription() : "未知", 
                           creator);
    }
    
    @Override
    public String toString() {
        return "UserCreatedEvent{" +
                "eventId='" + getEventId() + '\'' +
                ", personId='" + getPersonId() + '\'' +
                ", personName='" + personName + '\'' +
                ", category=" + category +
                ", creator='" + creator + '\'' +
                ", tenantId='" + tenantId + '\'' +
                ", occurredOn=" + getOccurredOn() +
                '}';
    }
}