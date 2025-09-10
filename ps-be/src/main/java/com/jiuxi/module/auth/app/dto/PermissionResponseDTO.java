package com.jiuxi.module.auth.app.dto;

import com.jiuxi.module.auth.domain.entity.PermissionType;
import com.jiuxi.module.auth.domain.entity.PermissionStatus;

import java.time.LocalDateTime;

/**
 * 权限响应DTO
 * 
 * @author DDD Refactor
 * @date 2025-09-06
 */
public class PermissionResponseDTO {
    
    /**
     * 权限ID
     */
    private String permissionId;
    
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
    
    /**
     * 权限状态
     */
    private PermissionStatus status;
    
    /**
     * 权限URI（针对API类型权限）
     */
    private String permissionUri;
    
    /**
     * 权限方法（针对API类型权限）
     */
    private String permissionMethod;
    
    /**
     * 组件标识（针对菜单/按钮类型权限）
     */
    private String componentId;
    
    /**
     * 数据范围（针对数据类型权限）
     */
    private String dataScope;
    
    /**
     * 排序序号
     */
    private Integer orderIndex;
    
    /**
     * 创建信息
     */
    private String creator;
    private LocalDateTime createTime;
    
    /**
     * 更新信息
     */
    private String updator;
    private LocalDateTime updateTime;
    
    /**
     * 租户ID
     */
    private String tenantId;
    
    // Getters and Setters
    public String getPermissionId() {
        return permissionId;
    }
    
    public void setPermissionId(String permissionId) {
        this.permissionId = permissionId;
    }
    
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
    
    public PermissionStatus getStatus() {
        return status;
    }
    
    public void setStatus(PermissionStatus status) {
        this.status = status;
    }
    
    public String getPermissionUri() {
        return permissionUri;
    }
    
    public void setPermissionUri(String permissionUri) {
        this.permissionUri = permissionUri;
    }
    
    public String getPermissionMethod() {
        return permissionMethod;
    }
    
    public void setPermissionMethod(String permissionMethod) {
        this.permissionMethod = permissionMethod;
    }
    
    public String getComponentId() {
        return componentId;
    }
    
    public void setComponentId(String componentId) {
        this.componentId = componentId;
    }
    
    public String getDataScope() {
        return dataScope;
    }
    
    public void setDataScope(String dataScope) {
        this.dataScope = dataScope;
    }
    
    public Integer getOrderIndex() {
        return orderIndex;
    }
    
    public void setOrderIndex(Integer orderIndex) {
        this.orderIndex = orderIndex;
    }
    
    public String getCreator() {
        return creator;
    }
    
    public void setCreator(String creator) {
        this.creator = creator;
    }
    
    public LocalDateTime getCreateTime() {
        return createTime;
    }
    
    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }
    
    public String getUpdator() {
        return updator;
    }
    
    public void setUpdator(String updator) {
        this.updator = updator;
    }
    
    public LocalDateTime getUpdateTime() {
        return updateTime;
    }
    
    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }
    
    public String getTenantId() {
        return tenantId;
    }
    
    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }
}