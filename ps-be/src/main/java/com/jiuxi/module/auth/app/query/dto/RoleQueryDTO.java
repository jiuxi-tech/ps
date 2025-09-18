package com.jiuxi.module.auth.app.query.dto;

import java.util.List;

/**
 * 角色查询DTO
 * 封装角色查询的响应数据
 * 
 * @author DDD Refactor
 * @date 2025-09-18
 */
public class RoleQueryDTO {
    
    private String roleId;
    private String roleCode;
    private String roleName;
    private String roleDesc;
    private String roleType;
    private String status;
    private Boolean builtIn;
    private String deptId;
    private String parentRoleId;
    private Integer roleLevel;
    private String rolePath;
    private Boolean inheritParentPermissions;
    private String dataScope;
    private Integer orderIndex;
    private String creator;
    private String createTime;
    private String updator;
    private String updateTime;
    private String tenantId;
    
    // 关联数据
    private List<PermissionQueryDTO> permissions;
    private List<MenuQueryDTO> menus;
    private List<RoleQueryDTO> childRoles;
    
    // 构造器
    public RoleQueryDTO() {
    }
    
    // Getters and Setters
    public String getRoleId() {
        return roleId;
    }
    
    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }
    
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
    
    public String getRoleType() {
        return roleType;
    }
    
    public void setRoleType(String roleType) {
        this.roleType = roleType;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public Boolean getBuiltIn() {
        return builtIn;
    }
    
    public void setBuiltIn(Boolean builtIn) {
        this.builtIn = builtIn;
    }
    
    public String getDeptId() {
        return deptId;
    }
    
    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }
    
    public String getParentRoleId() {
        return parentRoleId;
    }
    
    public void setParentRoleId(String parentRoleId) {
        this.parentRoleId = parentRoleId;
    }
    
    public Integer getRoleLevel() {
        return roleLevel;
    }
    
    public void setRoleLevel(Integer roleLevel) {
        this.roleLevel = roleLevel;
    }
    
    public String getRolePath() {
        return rolePath;
    }
    
    public void setRolePath(String rolePath) {
        this.rolePath = rolePath;
    }
    
    public Boolean getInheritParentPermissions() {
        return inheritParentPermissions;
    }
    
    public void setInheritParentPermissions(Boolean inheritParentPermissions) {
        this.inheritParentPermissions = inheritParentPermissions;
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
    
    public String getCreateTime() {
        return createTime;
    }
    
    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
    
    public String getUpdator() {
        return updator;
    }
    
    public void setUpdator(String updator) {
        this.updator = updator;
    }
    
    public String getUpdateTime() {
        return updateTime;
    }
    
    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
    
    public String getTenantId() {
        return tenantId;
    }
    
    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }
    
    public List<PermissionQueryDTO> getPermissions() {
        return permissions;
    }
    
    public void setPermissions(List<PermissionQueryDTO> permissions) {
        this.permissions = permissions;
    }
    
    public List<MenuQueryDTO> getMenus() {
        return menus;
    }
    
    public void setMenus(List<MenuQueryDTO> menus) {
        this.menus = menus;
    }
    
    public List<RoleQueryDTO> getChildRoles() {
        return childRoles;
    }
    
    public void setChildRoles(List<RoleQueryDTO> childRoles) {
        this.childRoles = childRoles;
    }
}