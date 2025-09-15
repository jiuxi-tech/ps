package com.jiuxi.module.role.app.command.dto;

import javax.validation.constraints.NotBlank;

/**
 * 删除角色命令
 * 
 * @author DDD重构
 * @date 2025-09-15
 */
public class DeleteRoleCommand {
    
    @NotBlank(message = "角色ID不能为空")
    private String roleId;
    
    @NotBlank(message = "操作人员ID不能为空")
    private String operatorId;
    
    private String creator;
    
    public DeleteRoleCommand() {}
    
    public DeleteRoleCommand(String roleId, String operatorId, String creator) {
        this.roleId = roleId;
        this.operatorId = operatorId;
        this.creator = creator;
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
    
    public String getCreator() {
        return creator;
    }
    
    public void setCreator(String creator) {
        this.creator = creator;
    }
    
    @Override
    public String toString() {
        return "DeleteRoleCommand{" +
                "roleId='" + roleId + '\'' +
                ", operatorId='" + operatorId + '\'' +
                ", creator='" + creator + '\'' +
                '}';
    }
}