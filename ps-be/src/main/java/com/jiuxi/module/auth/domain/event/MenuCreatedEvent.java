package com.jiuxi.module.auth.domain.event;

import com.jiuxi.module.auth.domain.model.vo.MenuCode;
import com.jiuxi.module.auth.domain.model.vo.MenuId;
import com.jiuxi.module.auth.domain.model.vo.TenantId;
import com.jiuxi.module.auth.domain.model.entity.MenuType;

/**
 * 菜单创建领域事件
 * 当新菜单被创建时触发
 * 
 * @author DDD Refactor
 * @date 2025-09-18
 */
public class MenuCreatedEvent extends MenuEvent {
    
    private final MenuCode menuCode;
    private final String menuName;
    private final MenuType menuType;
    private final String creator;
    
    public MenuCreatedEvent(MenuId menuId, TenantId tenantId, MenuCode menuCode, 
                           String menuName, MenuType menuType, String creator) {
        super(menuId, tenantId);
        this.menuCode = menuCode;
        this.menuName = menuName;
        this.menuType = menuType;
        this.creator = creator;
    }
    
    public MenuCode getMenuCode() {
        return menuCode;
    }
    
    public String getMenuName() {
        return menuName;
    }
    
    public MenuType getMenuType() {
        return menuType;
    }
    
    public String getCreator() {
        return creator;
    }
}