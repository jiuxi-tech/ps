package com.jiuxi.module.auth.domain.event;

import com.jiuxi.module.auth.domain.model.vo.PermissionId;
import com.jiuxi.module.auth.domain.model.vo.RoleId;
import com.jiuxi.module.auth.domain.model.vo.TenantId;

/**
 * 角色权限授予领域事件
 * 当角色被授予新权限时触发
 * 
 * @author DDD Refactor
 * @date 2025-09-18
 */
public class RolePermissionGrantedEvent extends RoleEvent {
    
    private final PermissionId permissionId;
    private final String operator;
    
    public RolePermissionGrantedEvent(RoleId roleId, TenantId tenantId, 
                                    PermissionId permissionId, String operator) {
        super(roleId, tenantId);
        this.permissionId = permissionId;
        this.operator = operator;
    }
    
    public PermissionId getPermissionId() {
        return permissionId;
    }
    
    public String getOperator() {
        return operator;
    }
}