package com.jiuxi.module.role.app.command.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 更新角色命令
 * 
 * @author DDD重构
 * @date 2025-09-15
 */
public class UpdateRoleCommand {
    
    @NotBlank(message = "角色ID不能为空")
    private String roleId;
    
    @NotBlank(message = "角色名称不能为空")
    @Size(min = 2, max = 50, message = "角色名称长度应为2-50个字符")
    private String roleName;
    
    @Size(max = 200, message = "角色描述长度不能超过200个字符")
    private String roleDesc;
    
    @Size(max = 500, message = "备注长度不能超过500个字符")
    private String remark;
    
    @NotBlank(message = "操作人员ID不能为空")
    private String operatorId;
    
    public UpdateRoleCommand() {}
    
    public UpdateRoleCommand(String roleId, String roleName, String roleDesc, String remark, String operatorId) {
        this.roleId = roleId;
        this.roleName = roleName;
        this.roleDesc = roleDesc;
        this.remark = remark;
        this.operatorId = operatorId;
    }
    
    public String getRoleId() {
        return roleId;
    }
    
    public void setRoleId(String roleId) {
        this.roleId = roleId;
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
    
    public String getRemark() {
        return remark;
    }
    
    public void setRemark(String remark) {
        this.remark = remark;
    }
    
    public String getOperatorId() {
        return operatorId;
    }
    
    public void setOperatorId(String operatorId) {
        this.operatorId = operatorId;
    }
    
    @Override
    public String toString() {
        return "UpdateRoleCommand{" +
                "roleId='" + roleId + '\'' +
                ", roleName='" + roleName + '\'' +
                ", roleDesc='" + roleDesc + '\'' +
                ", remark='" + remark + '\'' +
                ", operatorId='" + operatorId + '\'' +
                '}';
    }
}