package com.jiuxi.module.org.domain.event;

import java.time.LocalDateTime;

/**
 * 部门删除事件
 * 
 * @author DDD Refactor
 * @date 2025-09-06
 */
public class DepartmentDeletedEvent {
    
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
     * 租户ID
     */
    private final String tenantId;
    
    /**
     * 删除者
     */
    private final String deletor;
    
    /**
     * 事件发生时间
     */
    private final LocalDateTime occurredOn;
    
    public DepartmentDeletedEvent(String deptId, String deptName, String parentDeptId, 
                                 String deptPath, String tenantId, String deletor) {
        this.deptId = deptId;
        this.deptName = deptName;
        this.parentDeptId = parentDeptId;
        this.deptPath = deptPath;
        this.tenantId = tenantId;
        this.deletor = deletor;
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
    
    public String getTenantId() {
        return tenantId;
    }
    
    public String getDeletor() {
        return deletor;
    }
    
    public LocalDateTime getOccurredOn() {
        return occurredOn;
    }
    
    @Override
    public String toString() {
        return "DepartmentDeletedEvent{" +
                "deptId='" + deptId + '\'' +
                ", deptName='" + deptName + '\'' +
                ", parentDeptId='" + parentDeptId + '\'' +
                ", deptPath='" + deptPath + '\'' +
                ", tenantId='" + tenantId + '\'' +
                ", deletor='" + deletor + '\'' +
                ", occurredOn=" + occurredOn +
                '}';
    }
}