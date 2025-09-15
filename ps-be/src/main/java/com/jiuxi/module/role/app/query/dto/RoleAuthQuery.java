package com.jiuxi.module.role.app.query.dto;

/**
 * 角色授权查询
 * 
 * @author DDD重构
 * @date 2025-09-15
 */
public class RoleAuthQuery {
    
    private String roleName;
    private String roleDesc;
    private Integer category;
    private String applicationId;
    
    private String operatorId;
    private String deptId;
    private String operatorRoleIds;
    
    public RoleAuthQuery() {}
    
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
    
    public Integer getCategory() {
        return category;
    }
    
    public void setCategory(Integer category) {
        this.category = category;
    }
    
    public String getApplicationId() {
        return applicationId;
    }
    
    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }
    
    public String getOperatorId() {
        return operatorId;
    }
    
    public void setOperatorId(String operatorId) {
        this.operatorId = operatorId;
    }
    
    public String getDeptId() {
        return deptId;
    }
    
    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }
    
    public String getOperatorRoleIds() {
        return operatorRoleIds;
    }
    
    public void setOperatorRoleIds(String operatorRoleIds) {
        this.operatorRoleIds = operatorRoleIds;
    }
    
    @Override
    public String toString() {
        return "RoleAuthQuery{" +
                "roleName='" + roleName + '\'' +
                ", roleDesc='" + roleDesc + '\'' +
                ", category=" + category +
                ", applicationId='" + applicationId + '\'' +
                ", operatorId='" + operatorId + '\'' +
                ", deptId='" + deptId + '\'' +
                ", operatorRoleIds='" + operatorRoleIds + '\'' +
                '}';
    }
}