package com.jiuxi.module.auth.infra.persistence.entity;

import com.baomidou.mybatisplus.annotation.*;
import java.time.LocalDateTime;

/**
 * 角色持久化对象
 * 对应数据库中的角色表
 * 
 * @author DDD Refactor
 * @date 2025-09-06
 */
@TableName("sys_role")
public class RolePO {
    
    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;
    
    /**
     * 角色编码
     */
    @TableField("role_code")
    private String roleCode;
    
    /**
     * 角色名称
     */
    @TableField("role_name")
    private String roleName;
    
    /**
     * 角色描述
     */
    @TableField("role_desc")
    private String roleDesc;
    
    /**
     * 角色类型
     */
    @TableField("role_type")
    private String roleType;
    
    /**
     * 角色状态
     */
    @TableField("status")
    private String status;
    
    /**
     * 是否内置角色
     */
    @TableField("built_in")
    private Boolean builtIn;
    
    /**
     * 部门ID（如果是部门角色）
     */
    @TableField("dept_id")
    private String deptId;
    
    /**
     * 数据权限范围
     */
    @TableField("data_scope")
    private String dataScope;
    
    /**
     * 排序序号
     */
    @TableField("order_index")
    private Integer orderIndex;
    
    /**
     * 创建信息
     */
    @TableField("creator")
    private String creator;
    
    @TableField("create_time")
    private LocalDateTime createTime;
    
    /**
     * 更新信息
     */
    @TableField("updator")
    private String updator;
    
    @TableField("update_time")
    private LocalDateTime updateTime;
    
    /**
     * 租户ID
     */
    @TableField("tenant_id")
    private String tenantId;
    
    /**
     * 逻辑删除标识
     */
    @TableLogic
    @TableField("deleted")
    private Integer deleted;
    
    // Getters and Setters
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
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
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public Boolean getBuiltIn() {
        return builtIn;
    }
    
    public void setBuiltIn(Boolean builtIn) {
        this.builtIn = builtIn;
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
    
    public Integer getOrderIndex() {
        return orderIndex;
    }
    
    public void setOrderIndex(Integer orderIndex) {
        this.orderIndex = orderIndex;
    }
    
    public String getCreator() {
        return creator;
    }
    
    public void setCreator(String creator) {
        this.creator = creator;
    }
    
    public LocalDateTime getCreateTime() {
        return createTime;
    }
    
    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }
    
    public String getUpdator() {
        return updator;
    }
    
    public void setUpdator(String updator) {
        this.updator = updator;
    }
    
    public LocalDateTime getUpdateTime() {
        return updateTime;
    }
    
    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }
    
    public String getTenantId() {
        return tenantId;
    }
    
    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }
    
    public Integer getDeleted() {
        return deleted;
    }
    
    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }
}