package com.jiuxi.module.role.app.query.dto;

import javax.validation.constraints.NotBlank;

/**
 * 角色详情查询
 * 
 * @author DDD重构
 * @date 2025-09-15
 */
public class RoleDetailQuery {
    
    @NotBlank(message = "角色ID不能为空")
    private String roleId;
    
    private String operatorId;
    private String operatorRoleIds;
    
    public RoleDetailQuery() {}
    
    public RoleDetailQuery(String roleId, String operatorId, String operatorRoleIds) {
        this.roleId = roleId;
        this.operatorId = operatorId;
        this.operatorRoleIds = operatorRoleIds;
    }
    
    public String getRoleId() {
        return roleId;
    }
    
    public void setRoleId(String roleId) {
        this.roleId = roleId;
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
        return "RoleDetailQuery{" +
                "roleId='" + roleId + '\'' +
                ", operatorId='" + operatorId + '\'' +
                ", operatorRoleIds='" + operatorRoleIds + '\'' +
                '}';
    }
}