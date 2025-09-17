package com.jiuxi.module.org.app.query.dto;

/**
 * 部门查询DTO
 * 用于复杂查询条件的封装
 * 
 * @author DDD Refactor
 * @date 2025-09-11
 */
public class DepartmentQueryDTO {
    
    /**
     * 租户ID
     */
    private String tenantId;
    
    /**
     * 部门名称（模糊匹配）
     */
    private String deptName;
    
    /**
     * 部门状态
     */
    private String status;
    
    /**
     * 部门类型
     */
    private String type;
    
    /**
     * 父部门ID
     */
    private String parentDeptId;
    
    /**
     * 部门层级
     */
    private Integer level;
    
    /**
     * 最小层级
     */
    private Integer minLevel;
    
    /**
     * 最大层级
     */
    private Integer maxLevel;
    
    /**
     * 是否包含停用部门
     */
    private Boolean includeInactive;
    
    /**
     * 是否只查询叶子部门
     */
    private Boolean leafOnly;
    
    /**
     * 是否只查询根部门
     */
    private Boolean rootOnly;
    
    /**
     * 左值（用于嵌套集合查询）
     */
    private Integer leftValue;
    
    /**
     * 右值（用于嵌套集合查询）
     */
    private Integer rightValue;
    
    public String getTenantId() {
        return tenantId;
    }
    
    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }
    
    public String getDeptName() {
        return deptName;
    }
    
    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public String getParentDeptId() {
        return parentDeptId;
    }
    
    public void setParentDeptId(String parentDeptId) {
        this.parentDeptId = parentDeptId;
    }
    
    public Integer getLevel() {
        return level;
    }
    
    public void setLevel(Integer level) {
        this.level = level;
    }
    
    public Integer getMinLevel() {
        return minLevel;
    }
    
    public void setMinLevel(Integer minLevel) {
        this.minLevel = minLevel;
    }
    
    public Integer getMaxLevel() {
        return maxLevel;
    }
    
    public void setMaxLevel(Integer maxLevel) {
        this.maxLevel = maxLevel;
    }
    
    public Boolean getIncludeInactive() {
        return includeInactive;
    }
    
    public void setIncludeInactive(Boolean includeInactive) {
        this.includeInactive = includeInactive;
    }
    
    public Boolean getLeafOnly() {
        return leafOnly;
    }
    
    public void setLeafOnly(Boolean leafOnly) {
        this.leafOnly = leafOnly;
    }
    
    public Boolean getRootOnly() {
        return rootOnly;
    }
    
    public void setRootOnly(Boolean rootOnly) {
        this.rootOnly = rootOnly;
    }
    
    public Integer getLeftValue() {
        return leftValue;
    }
    
    public void setLeftValue(Integer leftValue) {
        this.leftValue = leftValue;
    }
    
    public Integer getRightValue() {
        return rightValue;
    }
    
    public void setRightValue(Integer rightValue) {
        this.rightValue = rightValue;
    }
}