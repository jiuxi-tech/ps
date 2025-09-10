package com.jiuxi.module.auth.app.dto;

import com.jiuxi.module.auth.domain.entity.RoleType;

/**
 * 角色创建DTO
 * 
 * @author DDD Refactor
 * @date 2025-09-06
 */
public class RoleCreateDTO {
    
    /**
     * 角色编码
     */
    private String roleCode;
    
    /**
     * 角色名称
     */
    private String roleName;
    
    /**
     * 角色描述
     */
    private String roleDesc;
    
    /**
     * 角色类型
     */
    private RoleType roleType;
    
    // Getters and Setters
    public String getRoleCode() {
        return roleCode;
    }
    
    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
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
    
    public RoleType getRoleType() {
        return roleType;
    }
    
    public void setRoleType(RoleType roleType) {
        this.roleType = roleType;
    }
}