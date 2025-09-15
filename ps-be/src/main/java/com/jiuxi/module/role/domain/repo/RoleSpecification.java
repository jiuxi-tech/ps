package com.jiuxi.module.role.domain.repo;

import java.util.List;

/**
 * 角色查询规格
 * 用于构建复杂查询条件
 * 
 * @author DDD重构
 * @date 2025-09-15
 */
public class RoleSpecification {
    
    private String roleName;
    private String roleDesc;
    private Integer category;
    private String creator;
    private String applicationId;
    private List<String> roleIds;
    private Integer isEnabled;
    
    // 分页参数
    private int current = 1;
    private int size = 10;
    
    public RoleSpecification() {}
    
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
    
    public List<String> getRoleIds() {
        return roleIds;
    }
    
    public void setRoleIds(List<String> roleIds) {
        this.roleIds = roleIds;
    }
    
    public Integer getIsEnabled() {
        return isEnabled;
    }
    
    public void setIsEnabled(Integer isEnabled) {
        this.isEnabled = isEnabled;
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
    
    @Override
    public String toString() {
        return "RoleSpecification{" +
                "roleName='" + roleName + '\'' +
                ", roleDesc='" + roleDesc + '\'' +
                ", category=" + category +
                ", creator='" + creator + '\'' +
                ", applicationId='" + applicationId + '\'' +
                ", roleIds=" + roleIds +
                ", isEnabled=" + isEnabled +
                ", current=" + current +
                ", size=" + size +
                '}';
    }
}