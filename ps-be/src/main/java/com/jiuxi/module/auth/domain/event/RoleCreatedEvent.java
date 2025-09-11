package com.jiuxi.module.auth.domain.event;

import java.time.LocalDateTime;

/**
 * 角色创建事件
 * 
 * @author DDD Refactor
 * @date 2025-09-11
 */
public class RoleCreatedEvent {
    
    /**
     * 角色ID
     */
    private final String roleId;
    
    /**
     * 角色编码
     */
    private final String roleCode;
    
    /**
     * 角色名称
     */
    private final String roleName;
    
    /**
     * 角色类型
     */
    private final String roleType;
    
    /**
     * 部门ID（如果是部门角色）
     */
    private final String deptId;
    
    /**
     * 租户ID
     */
    private final String tenantId;
    
    /**
     * 操作者
     */
    private final String operator;
    
    /**
     * 事件发生时间
     */
    private final LocalDateTime occurredOn;
    
    public RoleCreatedEvent(String roleId, String roleCode, String roleName, 
                           String roleType, String deptId, String tenantId, String operator) {
        this.roleId = roleId;
        this.roleCode = roleCode;
        this.roleName = roleName;
        this.roleType = roleType;
        this.deptId = deptId;
        this.tenantId = tenantId;
        this.operator = operator;
        this.occurredOn = LocalDateTime.now();
    }
    
    public String getRoleId() {
        return roleId;
    }
    
    public String getRoleCode() {
        return roleCode;
    }
    
    public String getRoleName() {
        return roleName;
    }
    
    public String getRoleType() {
        return roleType;
    }
    
    public String getDeptId() {
        return deptId;
    }
    
    public String getTenantId() {
        return tenantId;
    }
    
    public String getOperator() {
        return operator;
    }
    
    public LocalDateTime getOccurredOn() {
        return occurredOn;
    }
}