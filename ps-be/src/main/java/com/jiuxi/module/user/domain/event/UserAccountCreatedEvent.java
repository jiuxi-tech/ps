package com.jiuxi.module.user.domain.event;

/**
 * 用户账户创建事件
 * 
 * @author DDD Refactor Phase 4.3.1.1
 * @date 2025-09-11
 */
public class UserAccountCreatedEvent extends UserEvent {
    
    /**
     * 账户ID
     */
    private final String accountId;
    
    /**
     * 用户名
     */
    private final String username;
    
    /**
     * 创建者
     */
    private final String creator;
    
    public UserAccountCreatedEvent(String personId, String accountId, String username, String creator) {
        super(personId);
        this.accountId = accountId;
        this.username = username;
        this.creator = creator;
    }
    
    public String getAccountId() {
        return accountId;
    }
    
    public String getUsername() {
        return username;
    }
    
    public String getCreator() {
        return creator;
    }
    
    @Override
    public String getEventType() {
        return "USER_ACCOUNT_CREATED";
    }
    
    @Override
    public String getEventDescription() {
        return String.format("用户 %s 的账户已创建，用户名：%s，创建者：%s", 
                           getPersonId(), username, creator);
    }
    
    @Override
    public String toString() {
        return "UserAccountCreatedEvent{" +
                "eventId='" + getEventId() + '\'' +
                ", personId='" + getPersonId() + '\'' +
                ", accountId='" + accountId + '\'' +
                ", username='" + username + '\'' +
                ", creator='" + creator + '\'' +
                ", occurredOn=" + getOccurredOn() +
                '}';
    }
}