package com.jiuxi.module.role.app.command.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 创建角色命令
 * 
 * @author DDD重构
 * @date 2025-09-15
 */
public class CreateRoleCommand {
    
    @NotBlank(message = "角色名称不能为空")
    @Size(min = 2, max = 50, message = "角色名称长度应为2-50个字符")
    private String roleName;
    
    @Size(max = 200, message = "角色描述长度不能超过200个字符")
    private String roleDesc;
    
    @NotNull(message = "角色类别不能为空")
    private Integer category;
    
    @NotBlank(message = "创建者ID不能为空")
    private String creatorId;
    
    @Size(max = 500, message = "备注长度不能超过500个字符")
    private String remark;
    
    private String applicationId;
    
    public CreateRoleCommand() {}
    
    public CreateRoleCommand(String roleName, String roleDesc, Integer category, String creatorId, String remark) {
        this.roleName = roleName;
        this.roleDesc = roleDesc;
        this.category = category;
        this.creatorId = creatorId;
        this.remark = remark;
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
    
    public Integer getCategory() {
        return category;
    }
    
    public void setCategory(Integer category) {
        this.category = category;
    }
    
    public String getCreatorId() {
        return creatorId;
    }
    
    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }
    
    public String getRemark() {
        return remark;
    }
    
    public void setRemark(String remark) {
        this.remark = remark;
    }
    
    public String getApplicationId() {
        return applicationId;
    }
    
    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }
    
    @Override
    public String toString() {
        return "CreateRoleCommand{" +
                "roleName='" + roleName + '\'' +
                ", roleDesc='" + roleDesc + '\'' +
                ", category=" + category +
                ", creatorId='" + creatorId + '\'' +
                ", remark='" + remark + '\'' +
                ", applicationId='" + applicationId + '\'' +
                '}';
    }
}