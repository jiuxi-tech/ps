package com.jiuxi.module.auth.app.dto;

/**
 * 权限更新DTO
 * 
 * @author DDD Refactor
 * @date 2025-09-06
 */
public class PermissionUpdateDTO {
    
    /**
     * 权限ID
     */
    private String permissionId;
    
    /**
     * 权限名称
     */
    private String permissionName;
    
    /**
     * 权限描述
     */
    private String permissionDesc;
    
    // Getters and Setters
    public String getPermissionId() {
        return permissionId;
    }
    
    public void setPermissionId(String permissionId) {
        this.permissionId = permissionId;
    }
    
    public String getPermissionName() {
        return permissionName;
    }
    
    public void setPermissionName(String permissionName) {
        this.permissionName = permissionName;
    }
    
    public String getPermissionDesc() {
        return permissionDesc;
    }
    
    public void setPermissionDesc(String permissionDesc) {
        this.permissionDesc = permissionDesc;
    }
}