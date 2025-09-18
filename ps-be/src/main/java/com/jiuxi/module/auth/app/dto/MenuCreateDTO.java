package com.jiuxi.module.auth.app.dto;

import com.jiuxi.module.auth.domain.model.entity.MenuType;

/**
 * 菜单创建DTO
 * 
 * @author DDD Refactor
 * @date 2025-09-06
 */
public class MenuCreateDTO {
    
    /**
     * 菜单编码
     */
    private String menuCode;
    
    /**
     * 菜单名称
     */
    private String menuName;
    
    /**
     * 菜单标题
     */
    private String menuTitle;
    
    /**
     * 父菜单ID
     */
    private String parentMenuId;
    
    /**
     * 菜单类型
     */
    private MenuType menuType;
    
    // Getters and Setters
    public String getMenuCode() {
        return menuCode;
    }
    
    public void setMenuCode(String menuCode) {
        this.menuCode = menuCode;
    }
    
    public String getMenuName() {
        return menuName;
    }
    
    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }
    
    public String getMenuTitle() {
        return menuTitle;
    }
    
    public void setMenuTitle(String menuTitle) {
        this.menuTitle = menuTitle;
    }
    
    public String getParentMenuId() {
        return parentMenuId;
    }
    
    public void setParentMenuId(String parentMenuId) {
        this.parentMenuId = parentMenuId;
    }
    
    public MenuType getMenuType() {
        return menuType;
    }
    
    public void setMenuType(MenuType menuType) {
        this.menuType = menuType;
    }
}