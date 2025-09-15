package com.jiuxi.module.role.infra.persistence.entity;

import com.baomidou.mybatisplus.annotation.*;
import java.time.LocalDateTime;

/**
 * 角色持久化对象（PO）
 * 对应数据库表结构
 * 
 * @author DDD重构
 * @date 2025-09-15
 */
@TableName("tp_role")
public class RolePO {
    
    @TableId(value = "role_id", type = IdType.ASSIGN_ID)
    private String roleId;
    
    @TableField("role_name")
    private String roleName;
    
    @TableField("role_desc")
    private String roleDesc;
    
    @TableField("category")
    private Integer category;
    
    @TableField("creator")
    private String creator;
    
    @TableField("remark")
    private String remark;
    
    @TableField("is_enabled")
    private Integer isEnabled;
    
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    
    @TableField("application_id")
    private String applicationId;
    
    @TableLogic
    @TableField("is_deleted")
    private Integer isDeleted;
    
    public RolePO() {}
    
    public RolePO(String roleId, String roleName, String roleDesc, Integer category, String creator, String remark) {
        this.roleId = roleId;
        this.roleName = roleName;
        this.roleDesc = roleDesc;
        this.category = category;
        this.creator = creator;
        this.remark = remark;
        this.isEnabled = 1;
        this.isDeleted = 0;
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
    
    public String getRemark() {
        return remark;
    }
    
    public void setRemark(String remark) {
        this.remark = remark;
    }
    
    public Integer getIsEnabled() {
        return isEnabled;
    }
    
    public void setIsEnabled(Integer isEnabled) {
        this.isEnabled = isEnabled;
    }
    
    public LocalDateTime getCreateTime() {
        return createTime;
    }
    
    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }
    
    public LocalDateTime getUpdateTime() {
        return updateTime;
    }
    
    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }
    
    public String getApplicationId() {
        return applicationId;
    }
    
    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }
    
    public Integer getIsDeleted() {
        return isDeleted;
    }
    
    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }
    
    @Override
    public String toString() {
        return "RolePO{" +
                "roleId='" + roleId + '\'' +
                ", roleName='" + roleName + '\'' +
                ", roleDesc='" + roleDesc + '\'' +
                ", category=" + category +
                ", creator='" + creator + '\'' +
                ", remark='" + remark + '\'' +
                ", isEnabled=" + isEnabled +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", applicationId='" + applicationId + '\'' +
                ", isDeleted=" + isDeleted +
                '}';
    }
}