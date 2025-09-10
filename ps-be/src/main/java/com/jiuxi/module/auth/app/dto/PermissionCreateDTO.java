package com.jiuxi.module.auth.app.dto;

import com.jiuxi.module.auth.domain.entity.PermissionType;

/**
 * 权限创建DTO
 * 
 * @author DDD Refactor
 * @date 2025-09-06
 */
public class PermissionCreateDTO {
    
    /**
     * 权限编码
     */
    private String permissionCode;
    
    /**
     * 权限名称
     */
    private String permissionName;
    
    /**
     * 权限描述
     */
    private String permissionDesc;
    
    /**
     * 权限类型
     */
    private PermissionType permissionType;
    
    // Getters and Setters
    public String getPermissionCode() {
        return permissionCode;
    }
    
    public void setPermissionCode(String permissionCode) {
        this.permissionCode = permissionCode;
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
    
    public PermissionType getPermissionType() {
        return permissionType;
    }
    
    public void setPermissionType(PermissionType permissionType) {
        this.permissionType = permissionType;
    }
}