package com.jiuxi.module.user.domain.event;

import java.time.LocalDateTime;

/**
 * 用户删除事件
 * 
 * @author DDD Refactor
 * @date 2025-09-06
 */
public class UserDeletedEvent {
    
    private final String personId;
    private final String personName;
    private final String operator;
    private final LocalDateTime occurredOn;
    
    public UserDeletedEvent(String personId, String personName, String operator) {
        this.personId = personId;
        this.personName = personName;
        this.operator = operator;
        this.occurredOn = LocalDateTime.now();
    }
    
    public String getPersonId() {
        return personId;
    }
    
    public String getPersonName() {
        return personName;
    }
    
    public String getOperator() {
        return operator;
    }
    
    public LocalDateTime getOccurredOn() {
        return occurredOn;
    }
    
    @Override
    public String toString() {
        return "UserDeletedEvent{" +
                "personId='" + personId + '\'' +
                ", personName='" + personName + '\'' +
                ", operator='" + operator + '\'' +
                ", occurredOn=" + occurredOn +
                '}';
    }
}