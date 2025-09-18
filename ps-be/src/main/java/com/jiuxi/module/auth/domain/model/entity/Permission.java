package com.jiuxi.module.auth.domain.model.entity;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 权限实体
 * 表示系统中的一个具体权限
 * 
 * @author DDD Refactor
 * @date 2025-09-06
 */
public class Permission {
    
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
     * 资源路径（如API路径）
     */
    private String resourcePath;
    
    /**
     * HTTP方法（GET、POST、PUT、DELETE等）
     */
    private String httpMethod;
    
    /**
     * 权限状态
     */
    private PermissionStatus status;
    
    /**
     * 是否内置权限
     */
    private Boolean builtIn;
    
    /**
     * 权限分组
     */
    private String permissionGroup;
    
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
    
    // 构造器
    public Permission() {
        this.status = PermissionStatus.ACTIVE;
        this.builtIn = false;
    }
    
    public Permission(String permissionCode, String permissionName, PermissionType permissionType) {
        this();
        this.permissionCode = permissionCode;
        this.permissionName = permissionName;
        this.permissionType = permissionType;
    }
    
    public Permission(String permissionCode, String permissionName, String resourcePath, String httpMethod) {
        this(permissionCode, permissionName, PermissionType.API);
        this.resourcePath = resourcePath;
        this.httpMethod = httpMethod;
    }
    
    // 业务方法
    
    /**
     * 启用权限
     */
    public void enable() {
        this.status = PermissionStatus.ACTIVE;
    }
    
    /**
     * 停用权限
     */
    public void disable() {
        this.status = PermissionStatus.INACTIVE;
    }
    
    /**
     * 检查是否激活
     */
    public boolean isActive() {
        return PermissionStatus.ACTIVE.equals(this.status);
    }
    
    /**
     * 检查是否是内置权限
     */
    public boolean isBuiltIn() {
        return Boolean.TRUE.equals(this.builtIn);
    }
    
    /**
     * 设置为内置权限
     */
    public void setAsBuiltIn() {
        this.builtIn = true;
    }
    
    /**
     * 检查是否是API权限
     */
    public boolean isApiPermission() {
        return PermissionType.API.equals(this.permissionType);
    }
    
    /**
     * 检查是否是菜单权限
     */
    public boolean isMenuPermission() {
        return PermissionType.MENU.equals(this.permissionType);
    }
    
    /**
     * 检查是否是按钮权限
     */
    public boolean isButtonPermission() {
        return PermissionType.BUTTON.equals(this.permissionType);
    }
    
    /**
     * 检查是否是数据权限
     */
    public boolean isDataPermission() {
        return PermissionType.DATA.equals(this.permissionType);
    }
    
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
    
    public String getResourcePath() {
        return resourcePath;
    }
    
    public void setResourcePath(String resourcePath) {
        this.resourcePath = resourcePath;
    }
    
    public String getHttpMethod() {
        return httpMethod;
    }
    
    public void setHttpMethod(String httpMethod) {
        this.httpMethod = httpMethod;
    }
    
    public PermissionStatus getStatus() {
        return status;
    }
    
    public void setStatus(PermissionStatus status) {
        this.status = status;
    }
    
    public Boolean getBuiltIn() {
        return builtIn;
    }
    
    public void setBuiltIn(Boolean builtIn) {
        this.builtIn = builtIn;
    }
    
    public String getPermissionGroup() {
        return permissionGroup;
    }
    
    public void setPermissionGroup(String permissionGroup) {
        this.permissionGroup = permissionGroup;
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
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Permission that = (Permission) o;
        return Objects.equals(permissionId, that.permissionId);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(permissionId);
    }
}