package com.jiuxi.module.auth.app.command.dto;

import javax.validation.constraints.NotBlank;

/**
 * 移动菜单命令
 * 封装移动菜单操作的所有参数
 * 
 * @author DDD Refactor
 * @date 2025-09-18
 */
public class MoveMenuCommand {
    
    @NotBlank(message = "菜单ID不能为空")
    private String menuId;
    
    private String newParentId;
    
    @NotBlank(message = "操作者不能为空")
    private String operator;
    
    // 构造器
    public MoveMenuCommand() {
    }
    
    public MoveMenuCommand(String menuId, String newParentId, String operator) {
        this.menuId = menuId;
        this.newParentId = newParentId;
        this.operator = operator;
    }
    
    // Getters and Setters
    public String getMenuId() {
        return menuId;
    }
    
    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }
    
    public String getNewParentId() {
        return newParentId;
    }
    
    public void setNewParentId(String newParentId) {
        this.newParentId = newParentId;
    }
    
    public String getOperator() {
        return operator;
    }
    
    public void setOperator(String operator) {
        this.operator = operator;
    }
}