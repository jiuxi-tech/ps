package com.jiuxi.module.org.domain.event;

import java.time.LocalDateTime;

/**
 * 部门创建事件
 * 
 * @author DDD Refactor
 * @date 2025-09-06
 */
public class DepartmentCreatedEvent {
    
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
     * 租户ID
     */
    private final String tenantId;
    
    /**
     * 创建者
     */
    private final String creator;
    
    /**
     * 事件发生时间
     */
    private final LocalDateTime occurredOn;
    
    public DepartmentCreatedEvent(String deptId, String deptName, String parentDeptId, 
                                 String deptPath, Integer deptLevel, String tenantId, String creator) {
        this.deptId = deptId;
        this.deptName = deptName;
        this.parentDeptId = parentDeptId;
        this.deptPath = deptPath;
        this.deptLevel = deptLevel;
        this.tenantId = tenantId;
        this.creator = creator;
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
    
    public String getTenantId() {
        return tenantId;
    }
    
    public String getCreator() {
        return creator;
    }
    
    public LocalDateTime getOccurredOn() {
        return occurredOn;
    }
    
    @Override
    public String toString() {
        return "DepartmentCreatedEvent{" +
                "deptId='" + deptId + '\'' +
                ", deptName='" + deptName + '\'' +
                ", parentDeptId='" + parentDeptId + '\'' +
                ", deptPath='" + deptPath + '\'' +
                ", deptLevel=" + deptLevel +
                ", tenantId='" + tenantId + '\'' +
                ", creator='" + creator + '\'' +
                ", occurredOn=" + occurredOn +
                '}';
    }
}