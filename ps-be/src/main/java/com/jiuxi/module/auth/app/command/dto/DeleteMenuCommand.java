package com.jiuxi.module.auth.app.command.dto;

import javax.validation.constraints.NotBlank;

/**
 * 删除菜单命令
 * 封装删除菜单操作的所有参数
 * 
 * @author DDD Refactor
 * @date 2025-09-18
 */
public class DeleteMenuCommand {
    
    @NotBlank(message = "菜单ID不能为空")
    private String menuId;
    
    private String operator;
    
    // 构造器
    public DeleteMenuCommand() {
    }
    
    public DeleteMenuCommand(String menuId, String operator) {
        this.menuId = menuId;
        this.operator = operator;
    }
    
    // Getters and Setters
    public String getMenuId() {
        return menuId;
    }
    
    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }
    
    public String getOperator() {
        return operator;
    }
    
    public void setOperator(String operator) {
        this.operator = operator;
    }
}