package com.jiuxi.module.auth.domain.event;

import com.jiuxi.module.auth.domain.model.vo.MenuId;
import com.jiuxi.module.auth.domain.model.vo.TenantId;

import java.time.LocalDateTime;

/**
 * 菜单领域事件基类
 * 
 * @author DDD Refactor
 * @date 2025-09-18
 */
public abstract class MenuEvent {
    
    private final MenuId menuId;
    private final TenantId tenantId;
    private final LocalDateTime occurredOn;
    
    protected MenuEvent(MenuId menuId, TenantId tenantId) {
        this.menuId = menuId;
        this.tenantId = tenantId;
        this.occurredOn = LocalDateTime.now();
    }
    
    public MenuId getMenuId() {
        return menuId;
    }
    
    public TenantId getTenantId() {
        return tenantId;
    }
    
    public LocalDateTime getOccurredOn() {
        return occurredOn;
    }
}