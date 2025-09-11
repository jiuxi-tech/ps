package com.jiuxi.module.user.domain.event;

/**
 * 用户资料更新事件
 * 
 * @author DDD Refactor Phase 4.3.1.1
 * @date 2025-09-11
 */
public class UserProfileUpdatedEvent extends UserEvent {
    
    /**
     * 更新者
     */
    private final String updator;
    
    /**
     * 更新的字段列表
     */
    private final String[] updatedFields;
    
    /**
     * 更新原因
     */
    private final String reason;
    
    public UserProfileUpdatedEvent(String personId, String updator, String[] updatedFields, String reason) {
        super(personId);
        this.updator = updator;
        this.updatedFields = updatedFields != null ? updatedFields.clone() : new String[0];
        this.reason = reason;
    }
    
    public String getUpdator() {
        return updator;
    }
    
    public String[] getUpdatedFields() {
        return updatedFields != null ? updatedFields.clone() : new String[0];
    }
    
    public String getReason() {
        return reason;
    }
    
    @Override
    public String getEventType() {
        return "USER_PROFILE_UPDATED";
    }
    
    @Override
    public String getEventDescription() {
        return String.format("用户 %s 的资料已更新，更新者：%s，更新字段：%s", 
                           getPersonId(), 
                           updator, 
                           String.join(", ", updatedFields));
    }
    
    @Override
    public String toString() {
        return "UserProfileUpdatedEvent{" +
                "eventId='" + getEventId() + '\'' +
                ", personId='" + getPersonId() + '\'' +
                ", updator='" + updator + '\'' +
                ", updatedFields=" + java.util.Arrays.toString(updatedFields) +
                ", reason='" + reason + '\'' +
                ", occurredOn=" + getOccurredOn() +
                '}';
    }
}