package com.jiuxi.module.auth.infra.persistence.entity;

import com.baomidou.mybatisplus.annotation.*;
import java.time.LocalDateTime;

/**
 * 权限持久化对象
 * 对应数据库中的权限表
 * 
 * @author DDD Refactor
 * @date 2025-09-06
 */
@TableName("sys_permission")
public class PermissionPO {
    
    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;
    
    /**
     * 权限编码
     */
    @TableField("permission_code")
    private String permissionCode;
    
    /**
     * 权限名称
     */
    @TableField("permission_name")
    private String permissionName;
    
    /**
     * 权限描述
     */
    @TableField("permission_desc")
    private String permissionDesc;
    
    /**
     * 权限类型
     */
    @TableField("permission_type")
    private String permissionType;
    
    /**
     * 权限状态
     */
    @TableField("status")
    private String status;
    
    /**
     * 权限URI（针对API类型权限）
     */
    @TableField("permission_uri")
    private String permissionUri;
    
    /**
     * 权限方法（针对API类型权限）
     */
    @TableField("permission_method")
    private String permissionMethod;
    
    /**
     * 组件标识（针对菜单/按钮类型权限）
     */
    @TableField("component_id")
    private String componentId;
    
    /**
     * 数据范围（针对数据类型权限）
     */
    @TableField("data_scope")
    private String dataScope;
    
    /**
     * 排序序号
     */
    @TableField("order_index")
    private Integer orderIndex;
    
    /**
     * 创建信息
     */
    @TableField("creator")
    private String creator;
    
    @TableField("create_time")
    private LocalDateTime createTime;
    
    /**
     * 更新信息
     */
    @TableField("updator")
    private String updator;
    
    @TableField("update_time")
    private LocalDateTime updateTime;
    
    /**
     * 租户ID
     */
    @TableField("tenant_id")
    private String tenantId;
    
    /**
     * 逻辑删除标识
     */
    @TableLogic
    @TableField("deleted")
    private Integer deleted;
    
    // Getters and Setters
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
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
    
    public String getPermissionType() {
        return permissionType;
    }
    
    public void setPermissionType(String permissionType) {
        this.permissionType = permissionType;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
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
    
    public Integer getDeleted() {
        return deleted;
    }
    
    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }
}