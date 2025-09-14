package com.jiuxi.module.user.domain.event;

import com.jiuxi.module.user.domain.model.aggregate.User;
import java.time.LocalDateTime;

/**
 * 用户更新事件
 * 
 * @author DDD Refactor
 * @date 2025-09-06
 */
public class UserUpdatedEvent {
    
    private final User user;
    private final String operator;
    private final LocalDateTime occurredOn;
    
    public UserUpdatedEvent(User user, String operator) {
        this.user = user;
        this.operator = operator;
        this.occurredOn = LocalDateTime.now();
    }
    
    public User getUser() {
        return user;
    }
    
    public String getOperator() {
        return operator;
    }
    
    public LocalDateTime getOccurredOn() {
        return occurredOn;
    }
    
    @Override
    public String toString() {
        return "UserUpdatedEvent{" +
                "userId='" + user.getPersonId() + '\'' +
                ", userName='" + user.getProfile().getPersonName() + '\'' +
                ", operator='" + operator + '\'' +
                ", occurredOn=" + occurredOn +
                '}';
    }
}