package com.jiuxi.module.auth.app.command.dto;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * 分配权限命令
 * 封装角色权限分配操作的所有参数
 * 
 * @author DDD Refactor
 * @date 2025-09-18
 */
public class AssignPermissionsCommand {
    
    @NotBlank(message = "角色ID不能为空")
    private String roleId;
    
    private List<String> permissionIds;
    
    private List<String> menuIds;
    
    @NotBlank(message = "操作者不能为空")
    private String operator;
    
    // 构造器
    public AssignPermissionsCommand() {
    }
    
    public AssignPermissionsCommand(String roleId, List<String> permissionIds, 
                                   List<String> menuIds, String operator) {
        this.roleId = roleId;
        this.permissionIds = permissionIds;
        this.menuIds = menuIds;
        this.operator = operator;
    }
    
    // Getters and Setters
    public String getRoleId() {
        return roleId;
    }
    
    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }
    
    public List<String> getPermissionIds() {
        return permissionIds;
    }
    
    public void setPermissionIds(List<String> permissionIds) {
        this.permissionIds = permissionIds;
    }
    
    public List<String> getMenuIds() {
        return menuIds;
    }
    
    public void setMenuIds(List<String> menuIds) {
        this.menuIds = menuIds;
    }
    
    public String getOperator() {
        return operator;
    }
    
    public void setOperator(String operator) {
        this.operator = operator;
    }
}