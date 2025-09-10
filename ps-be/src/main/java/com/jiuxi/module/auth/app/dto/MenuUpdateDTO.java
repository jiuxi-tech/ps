package com.jiuxi.module.auth.app.dto;

/**
 * 菜单更新DTO
 * 
 * @author DDD Refactor
 * @date 2025-09-06
 */
public class MenuUpdateDTO {
    
    /**
     * 菜单ID
     */
    private String menuId;
    
    /**
     * 菜单名称
     */
    private String menuName;
    
    /**
     * 菜单标题
     */
    private String menuTitle;
    
    // Getters and Setters
    public String getMenuId() {
        return menuId;
    }
    
    public void setMenuId(String menuId) {
        this.menuId = menuId;
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
}