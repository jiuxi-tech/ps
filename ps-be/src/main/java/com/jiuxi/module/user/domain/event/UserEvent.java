package com.jiuxi.module.user.domain.event;

import java.time.LocalDateTime;

/**
 * 用户领域事件基类
 * 
 * @author DDD Refactor Phase 4.3.1.1
 * @date 2025-09-11
 */
public abstract class UserEvent {
    
    /**
     * 事件ID
     */
    private final String eventId;
    
    /**
     * 用户ID
     */
    private final String personId;
    
    /**
     * 事件时间
     */
    private final LocalDateTime occurredOn;
    
    /**
     * 事件版本
     */
    private final Integer version;
    
    protected UserEvent(String personId) {
        this.eventId = java.util.UUID.randomUUID().toString();
        this.personId = personId;
        this.occurredOn = LocalDateTime.now();
        this.version = 1;
    }
    
    protected UserEvent(String eventId, String personId, LocalDateTime occurredOn, Integer version) {
        this.eventId = eventId;
        this.personId = personId;
        this.occurredOn = occurredOn;
        this.version = version;
    }
    
    public String getEventId() {
        return eventId;
    }
    
    public String getPersonId() {
        return personId;
    }
    
    public LocalDateTime getOccurredOn() {
        return occurredOn;
    }
    
    public Integer getVersion() {
        return version;
    }
    
    /**
     * 获取事件类型
     */
    public abstract String getEventType();
    
    /**
     * 获取事件描述
     */
    public abstract String getEventDescription();
}