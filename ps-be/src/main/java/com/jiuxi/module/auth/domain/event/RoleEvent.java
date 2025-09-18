package com.jiuxi.module.auth.domain.event;

import com.jiuxi.module.auth.domain.model.vo.RoleId;
import com.jiuxi.module.auth.domain.model.vo.TenantId;

import java.time.LocalDateTime;

/**
 * 角色领域事件基类
 * 
 * @author DDD Refactor
 * @date 2025-09-18
 */
public abstract class RoleEvent {
    
    private final RoleId roleId;
    private final TenantId tenantId;
    private final LocalDateTime occurredOn;
    
    protected RoleEvent(RoleId roleId, TenantId tenantId) {
        this.roleId = roleId;
        this.tenantId = tenantId;
        this.occurredOn = LocalDateTime.now();
    }
    
    public RoleId getRoleId() {
        return roleId;
    }
    
    public TenantId getTenantId() {
        return tenantId;
    }
    
    public LocalDateTime getOccurredOn() {
        return occurredOn;
    }
}