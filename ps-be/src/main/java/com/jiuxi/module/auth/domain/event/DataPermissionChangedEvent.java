package com.jiuxi.module.auth.domain.event;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 数据权限变更事件
 * 
 * @author DDD Refactor
 * @date 2025-09-11
 */
public class DataPermissionChangedEvent {
    
    /**
     * 角色ID
     */
    private final String roleId;
    
    /**
     * 角色名称
     */
    private final String roleName;
    
    /**
     * 原数据权限范围
     */
    private final String oldDataScope;
    
    /**
     * 新数据权限范围
     */
    private final String newDataScope;
    
    /**
     * 原自定义部门列表
     */
    private final List<String> oldCustomDeptIds;
    
    /**
     * 新自定义部门列表
     */
    private final List<String> newCustomDeptIds;
    
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
    
    public DataPermissionChangedEvent(String roleId, String roleName, 
                                     String oldDataScope, String newDataScope,
                                     List<String> oldCustomDeptIds, List<String> newCustomDeptIds,
                                     String tenantId, String operator) {
        this.roleId = roleId;
        this.roleName = roleName;
        this.oldDataScope = oldDataScope;
        this.newDataScope = newDataScope;
        this.oldCustomDeptIds = oldCustomDeptIds;
        this.newCustomDeptIds = newCustomDeptIds;
        this.tenantId = tenantId;
        this.operator = operator;
        this.occurredOn = LocalDateTime.now();
    }
    
    public String getRoleId() {
        return roleId;
    }
    
    public String getRoleName() {
        return roleName;
    }
    
    public String getOldDataScope() {
        return oldDataScope;
    }
    
    public String getNewDataScope() {
        return newDataScope;
    }
    
    public List<String> getOldCustomDeptIds() {
        return oldCustomDeptIds;
    }
    
    public List<String> getNewCustomDeptIds() {
        return newCustomDeptIds;
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