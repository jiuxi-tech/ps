package com.jiuxi.module.role.app.query.dto;

/**
 * 角色分页查询
 * 
 * @author DDD重构
 * @date 2025-09-15
 */
public class RolePageQuery {
    
    private String roleName;
    private String roleDesc;
    private Integer category;
    private String creator;
    private String applicationId;
    
    private int current = 1;
    private int size = 10;
    
    private String operatorId;
    private String operatorRoleIds;
    
    public RolePageQuery() {}
    
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
    
    public String getCreator() {
        return creator;
    }
    
    public void setCreator(String creator) {
        this.creator = creator;
    }
    
    public String getApplicationId() {
        return applicationId;
    }
    
    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }
    
    public int getCurrent() {
        return current;
    }
    
    public void setCurrent(int current) {
        this.current = current;
    }
    
    public int getSize() {
        return size;
    }
    
    public void setSize(int size) {
        this.size = size;
    }
    
    public String getOperatorId() {
        return operatorId;
    }
    
    public void setOperatorId(String operatorId) {
        this.operatorId = operatorId;
    }
    
    public String getOperatorRoleIds() {
        return operatorRoleIds;
    }
    
    public void setOperatorRoleIds(String operatorRoleIds) {
        this.operatorRoleIds = operatorRoleIds;
    }
    
    @Override
    public String toString() {
        return "RolePageQuery{" +
                "roleName='" + roleName + '\'' +
                ", roleDesc='" + roleDesc + '\'' +
                ", category=" + category +
                ", creator='" + creator + '\'' +
                ", applicationId='" + applicationId + '\'' +
                ", current=" + current +
                ", size=" + size +
                ", operatorId='" + operatorId + '\'' +
                ", operatorRoleIds='" + operatorRoleIds + '\'' +
                '}';
    }
}