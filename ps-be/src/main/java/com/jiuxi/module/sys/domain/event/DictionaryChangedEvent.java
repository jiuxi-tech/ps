package com.jiuxi.module.sys.domain.event;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 字典变更领域事件
 * 当数据字典发生变更时触发
 * 
 * @author DDD Refactor
 * @date 2025-09-11
 */
public class DictionaryChangedEvent {
    
    /**
     * 事件ID
     */
    private final String eventId;
    
    /**
     * 字典ID
     */
    private final String dictId;
    
    /**
     * 字典编码
     */
    private final String dictCode;
    
    /**
     * 字典名称
     */
    private final String dictName;
    
    /**
     * 字典类型
     */
    private final String dictType;
    
    /**
     * 变更类型
     */
    private final ChangeType changeType;
    
    /**
     * 旧状态
     */
    private final String oldStatus;
    
    /**
     * 新状态
     */
    private final String newStatus;
    
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
    
    public DictionaryChangedEvent(String eventId, String dictId, String dictCode, 
            String dictName, String dictType, ChangeType changeType, 
            String oldStatus, String newStatus, String operator, String tenantId) {
        this.eventId = eventId;
        this.dictId = dictId;
        this.dictCode = dictCode;
        this.dictName = dictName;
        this.dictType = dictType;
        this.changeType = changeType;
        this.oldStatus = oldStatus;
        this.newStatus = newStatus;
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
        STRUCTURE_CHANGED("STRUCTURE_CHANGED", "结构变更");
        
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
    
    public String getDictId() {
        return dictId;
    }
    
    public String getDictCode() {
        return dictCode;
    }
    
    public String getDictName() {
        return dictName;
    }
    
    public String getDictType() {
        return dictType;
    }
    
    public ChangeType getChangeType() {
        return changeType;
    }
    
    public String getOldStatus() {
        return oldStatus;
    }
    
    public String getNewStatus() {
        return newStatus;
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
     * 检查是否为系统预置字典变更
     */
    public boolean isSystemPresetChange() {
        return dictCode != null && (
            dictCode.startsWith("sys.") ||
            dictCode.startsWith("system.") ||
            dictCode.startsWith("config.")
        );
    }
    
    /**
     * 检查是否为状态变更事件
     */
    public boolean isStatusChange() {
        return changeType == ChangeType.STATUS_CHANGED;
    }
    
    /**
     * 检查是否为结构变更事件
     */
    public boolean isStructureChange() {
        return changeType == ChangeType.STRUCTURE_CHANGED;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DictionaryChangedEvent that = (DictionaryChangedEvent) o;
        return Objects.equals(eventId, that.eventId);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(eventId);
    }
    
    @Override
    public String toString() {
        return "DictionaryChangedEvent{" +
                "eventId='" + eventId + '\'' +
                ", dictCode='" + dictCode + '\'' +
                ", changeType=" + changeType +
                ", occurredOn=" + occurredOn +
                '}';
    }
}