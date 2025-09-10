package com.jiuxi.module.auth.app.dto;

/**
 * 角色更新DTO
 * 
 * @author DDD Refactor
 * @date 2025-09-06
 */
public class RoleUpdateDTO {
    
    /**
     * 角色ID
     */
    private String roleId;
    
    /**
     * 角色名称
     */
    private String roleName;
    
    /**
     * 角色描述
     */
    private String roleDesc;
    
    // Getters and Setters
    public String getRoleId() {
        return roleId;
    }
    
    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }
    
    public String getRoleName() {
        return roleName;
    }
    
    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
    
    public String getRoleDesc() {
        return roleDesc;
    }
    
    public void setRoleDesc(String roleDesc) {
        this.roleDesc = roleDesc;
    }
}