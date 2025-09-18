package com.jiuxi.module.auth.domain.event;

import com.jiuxi.module.auth.domain.model.entity.MenuStatus;
import com.jiuxi.module.auth.domain.model.vo.MenuId;
import com.jiuxi.module.auth.domain.model.vo.TenantId;

/**
 * 菜单状态变更领域事件
 * 当菜单状态发生变化时触发
 * 
 * @author DDD Refactor
 * @date 2025-09-18
 */
public class MenuStatusChangedEvent extends MenuEvent {
    
    private final MenuStatus oldStatus;
    private final MenuStatus newStatus;
    private final String operator;
    
    public MenuStatusChangedEvent(MenuId menuId, TenantId tenantId, 
                                 MenuStatus oldStatus, MenuStatus newStatus, String operator) {
        super(menuId, tenantId);
        this.oldStatus = oldStatus;
        this.newStatus = newStatus;
        this.operator = operator;
    }
    
    public MenuStatus getOldStatus() {
        return oldStatus;
    }
    
    public MenuStatus getNewStatus() {
        return newStatus;
    }
    
    public String getOperator() {
        return operator;
    }
}