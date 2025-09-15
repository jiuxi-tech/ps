package com.jiuxi.module.role.app.command.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 分配权限命令
 * 
 * @author DDD重构
 * @date 2025-09-15
 */
public class AssignPermissionsCommand {
    
    @NotBlank(message = "角色ID不能为空")
    private String roleId;
    
    @NotNull(message = "菜单ID列表不能为空")
    private List<String> menuIds;
    
    @NotBlank(message = "操作人员ID不能为空")
    private String operatorId;
    
    public AssignPermissionsCommand() {}
    
    public AssignPermissionsCommand(String roleId, List<String> menuIds, String operatorId) {
        this.roleId = roleId;
        this.menuIds = menuIds;
        this.operatorId = operatorId;
    }
    
    public String getRoleId() {
        return roleId;
    }
    
    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }
    
    public List<String> getMenuIds() {
        return menuIds;
    }
    
    public void setMenuIds(List<String> menuIds) {
        this.menuIds = menuIds;
    }
    
    public String getOperatorId() {
        return operatorId;
    }
    
    public void setOperatorId(String operatorId) {
        this.operatorId = operatorId;
    }
    
    @Override
    public String toString() {
        return "AssignPermissionsCommand{" +
                "roleId='" + roleId + '\'' +
                ", menuIds=" + menuIds +
                ", operatorId='" + operatorId + '\'' +
                '}';
    }
}