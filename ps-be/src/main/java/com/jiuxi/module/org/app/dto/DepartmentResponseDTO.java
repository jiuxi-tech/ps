package com.jiuxi.module.org.app.dto;

import com.jiuxi.module.org.domain.entity.DepartmentStatus;
import com.jiuxi.module.org.domain.entity.DepartmentType;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 部门响应DTO
 * 
 * @author DDD Refactor
 * @date 2025-09-06
 */
public class DepartmentResponseDTO {
    
    /**
     * 部门ID
     */
    private String deptId;
    
    /**
     * 部门名称
     */
    private String deptName;
    
    /**
     * 部门简称
     */
    private String deptSimpleName;
    
    /**
     * 部门全称
     */
    private String deptFullName;
    
    /**
     * 父部门ID
     */
    private String parentDeptId;
    
    /**
     * 父部门名称
     */
    private String parentDeptName;
    
    /**
     * 部门路径
     */
    private String deptPath;
    
    /**
     * 部门层级
     */
    private Integer deptLevel;
    
    /**
     * 排序序号
     */
    private Integer orderIndex;
    
    /**
     * 部门状态
     */
    private DepartmentStatus status;
    
    /**
     * 部门类型
     */
    private DepartmentType type;
    
    /**
     * 部门负责人ID
     */
    private String managerId;
    
    /**
     * 部门负责人姓名
     */
    private String managerName;
    
    /**
     * 联系电话
     */
    private String contactPhone;
    
    /**
     * 联系地址
     */
    private String address;
    
    /**
     * 部门描述
     */
    private String description;
    
    /**
     * 子部门列表
     */
    private List<DepartmentResponseDTO> children;
    
    /**
     * 用户数量
     */
    private Long userCount;
    
    /**
     * 创建者
     */
    private String creator;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    
    /**
     * 更新者
     */
    private String updator;
    
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
    
    /**
     * 租户ID
     */
    private String tenantId;
    
    // Getters and Setters
    public String getDeptId() {
        return deptId;
    }
    
    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }
    
    public String getDeptName() {
        return deptName;
    }
    
    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }
    
    public String getDeptSimpleName() {
        return deptSimpleName;
    }
    
    public void setDeptSimpleName(String deptSimpleName) {
        this.deptSimpleName = deptSimpleName;
    }
    
    public String getDeptFullName() {
        return deptFullName;
    }
    
    public void setDeptFullName(String deptFullName) {
        this.deptFullName = deptFullName;
    }
    
    public String getParentDeptId() {
        return parentDeptId;
    }
    
    public void setParentDeptId(String parentDeptId) {
        this.parentDeptId = parentDeptId;
    }
    
    public String getParentDeptName() {
        return parentDeptName;
    }
    
    public void setParentDeptName(String parentDeptName) {
        this.parentDeptName = parentDeptName;
    }
    
    public String getDeptPath() {
        return deptPath;
    }
    
    public void setDeptPath(String deptPath) {
        this.deptPath = deptPath;
    }
    
    public Integer getDeptLevel() {
        return deptLevel;
    }
    
    public void setDeptLevel(Integer deptLevel) {
        this.deptLevel = deptLevel;
    }
    
    public Integer getOrderIndex() {
        return orderIndex;
    }
    
    public void setOrderIndex(Integer orderIndex) {
        this.orderIndex = orderIndex;
    }
    
    public DepartmentStatus getStatus() {
        return status;
    }
    
    public void setStatus(DepartmentStatus status) {
        this.status = status;
    }
    
    public DepartmentType getType() {
        return type;
    }
    
    public void setType(DepartmentType type) {
        this.type = type;
    }
    
    public String getManagerId() {
        return managerId;
    }
    
    public void setManagerId(String managerId) {
        this.managerId = managerId;
    }
    
    public String getManagerName() {
        return managerName;
    }
    
    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }
    
    public String getContactPhone() {
        return contactPhone;
    }
    
    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }
    
    public String getAddress() {
        return address;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public List<DepartmentResponseDTO> getChildren() {
        return children;
    }
    
    public void setChildren(List<DepartmentResponseDTO> children) {
        this.children = children;
    }
    
    public Long getUserCount() {
        return userCount;
    }
    
    public void setUserCount(Long userCount) {
        this.userCount = userCount;
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
}