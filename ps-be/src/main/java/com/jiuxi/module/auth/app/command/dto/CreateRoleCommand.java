package com.jiuxi.module.auth.app.command.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * 创建角色命令
 * 封装创建角色操作的所有参数
 * 
 * @author DDD Refactor
 * @date 2025-09-18
 */
public class CreateRoleCommand {
    
    @NotBlank(message = "角色编码不能为空")
    @Size(max = 50, message = "角色编码长度不能超过50个字符")
    private String roleCode;
    
    @NotBlank(message = "角色名称不能为空")
    @Size(max = 50, message = "角色名称长度不能超过50个字符")
    private String roleName;
    
    @Size(max = 200, message = "角色描述长度不能超过200个字符")
    private String roleDesc;
    
    private String roleType;
    
    private String parentRoleId;
    
    private Boolean inheritParentPermissions = true;
    
    private String deptId;
    
    private String dataScope;
    
    private List<String> permissionIds;
    
    private List<String> menuIds;
    
    private Integer orderIndex;
    
    @NotBlank(message = "操作者不能为空")
    private String operator;
    
    @NotBlank(message = "租户ID不能为空")
    private String tenantId;
    
    // 构造器
    public CreateRoleCommand() {
    }
    
    public CreateRoleCommand(String roleCode, String roleName, String roleDesc, 
                           String operator, String tenantId) {
        this.roleCode = roleCode;
        this.roleName = roleName;
        this.roleDesc = roleDesc;
        this.operator = operator;
        this.tenantId = tenantId;
    }
    
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
    
    public String getRoleType() {
        return roleType;
    }
    
    public void setRoleType(String roleType) {
        this.roleType = roleType;
    }
    
    public String getParentRoleId() {
        return parentRoleId;
    }
    
    public void setParentRoleId(String parentRoleId) {
        this.parentRoleId = parentRoleId;
    }
    
    public Boolean getInheritParentPermissions() {
        return inheritParentPermissions;
    }
    
    public void setInheritParentPermissions(Boolean inheritParentPermissions) {
        this.inheritParentPermissions = inheritParentPermissions;
    }
    
    public String getDeptId() {
        return deptId;
    }
    
    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }
    
    public String getDataScope() {
        return dataScope;
    }
    
    public void setDataScope(String dataScope) {
        this.dataScope = dataScope;
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
    
    public Integer getOrderIndex() {
        return orderIndex;
    }
    
    public void setOrderIndex(Integer orderIndex) {
        this.orderIndex = orderIndex;
    }
    
    public String getOperator() {
        return operator;
    }
    
    public void setOperator(String operator) {
        this.operator = operator;
    }
    
    public String getTenantId() {
        return tenantId;
    }
    
    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }
}