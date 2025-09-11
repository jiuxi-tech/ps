package com.jiuxi.module.auth.domain.event;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 权限分配事件
 * 
 * @author DDD Refactor
 * @date 2025-09-11
 */
public class PermissionAssignedEvent {
    
    /**
     * 角色ID
     */
    private final String roleId;
    
    /**
     * 角色名称
     */
    private final String roleName;
    
    /**
     * 分配的权限ID列表
     */
    private final List<String> permissionIds;
    
    /**
     * 分配的菜单ID列表
     */
    private final List<String> menuIds;
    
    /**
     * 数据权限范围
     */
    private final String dataScope;
    
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
    
    public PermissionAssignedEvent(String roleId, String roleName, 
                                  List<String> permissionIds, List<String> menuIds,
                                  String dataScope, String tenantId, String operator) {
        this.roleId = roleId;
        this.roleName = roleName;
        this.permissionIds = permissionIds;
        this.menuIds = menuIds;
        this.dataScope = dataScope;
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
    
    public List<String> getPermissionIds() {
        return permissionIds;
    }
    
    public List<String> getMenuIds() {
        return menuIds;
    }
    
    public String getDataScope() {
        return dataScope;
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