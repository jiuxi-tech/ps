package com.jiuxi.module.org.domain.event;

import java.time.LocalDateTime;

/**
 * 部门更新事件
 * 
 * @author DDD Refactor
 * @date 2025-09-06
 */
public class DepartmentUpdatedEvent {
    
    /**
     * 部门ID
     */
    private final String deptId;
    
    /**
     * 部门名称
     */
    private final String deptName;
    
    /**
     * 父部门ID
     */
    private final String parentDeptId;
    
    /**
     * 部门路径
     */
    private final String deptPath;
    
    /**
     * 部门层级
     */
    private final Integer deptLevel;
    
    /**
     * 原部门名称
     */
    private final String oldDeptName;
    
    /**
     * 原父部门ID
     */
    private final String oldParentDeptId;
    
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
    
    public DepartmentUpdatedEvent(String deptId, String deptName, String parentDeptId, 
                                 String deptPath, Integer deptLevel, String oldDeptName, 
                                 String oldParentDeptId, String tenantId, String updator) {
        this.deptId = deptId;
        this.deptName = deptName;
        this.parentDeptId = parentDeptId;
        this.deptPath = deptPath;
        this.deptLevel = deptLevel;
        this.oldDeptName = oldDeptName;
        this.oldParentDeptId = oldParentDeptId;
        this.tenantId = tenantId;
        this.updator = updator;
        this.occurredOn = LocalDateTime.now();
    }
    
    public String getDeptId() {
        return deptId;
    }
    
    public String getDeptName() {
        return deptName;
    }
    
    public String getParentDeptId() {
        return parentDeptId;
    }
    
    public String getDeptPath() {
        return deptPath;
    }
    
    public Integer getDeptLevel() {
        return deptLevel;
    }
    
    public String getOldDeptName() {
        return oldDeptName;
    }
    
    public String getOldParentDeptId() {
        return oldParentDeptId;
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
        return "DepartmentUpdatedEvent{" +
                "deptId='" + deptId + '\'' +
                ", deptName='" + deptName + '\'' +
                ", parentDeptId='" + parentDeptId + '\'' +
                ", deptPath='" + deptPath + '\'' +
                ", deptLevel=" + deptLevel +
                ", oldDeptName='" + oldDeptName + '\'' +
                ", oldParentDeptId='" + oldParentDeptId + '\'' +
                ", tenantId='" + tenantId + '\'' +
                ", updator='" + updator + '\'' +
                ", occurredOn=" + occurredOn +
                '}';
    }
}