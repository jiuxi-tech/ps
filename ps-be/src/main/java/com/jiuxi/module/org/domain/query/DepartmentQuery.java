package com.jiuxi.module.org.domain.query;

import com.jiuxi.module.org.domain.model.entity.DepartmentStatus;
import com.jiuxi.module.org.domain.model.entity.DepartmentType;

/**
 * 部门查询规范对象
 * 支持复杂查询条件的封装和组合
 * 
 * @author DDD Refactor
 * @date 2025-09-17
 */
public class DepartmentQuery {
    
    /**
     * 部门ID
     */
    private String deptId;
    
    /**
     * 部门名称（支持模糊查询）
     */
    private String deptName;
    
    /**
     * 部门编号
     */
    private String deptNo;
    
    /**
     * 父部门ID
     */
    private String parentDeptId;
    
    /**
     * 部门状态
     */
    private DepartmentStatus status;
    
    /**
     * 部门类型
     */
    private DepartmentType type;
    
    /**
     * 部门层级
     */
    private Integer deptLevel;
    
    /**
     * 最小层级
     */
    private Integer minLevel;
    
    /**
     * 最大层级
     */
    private Integer maxLevel;
    
    /**
     * 负责人ID
     */
    private String managerId;
    
    /**
     * 负责人姓名（支持模糊查询）
     */
    private String principalName;
    
    /**
     * 部门类别（0政府 1企业）
     */
    private Integer category;
    
    /**
     * 行政区划代码
     */
    private String cityCode;
    
    /**
     * 所属机构ID
     */
    private String ascnId;
    
    /**
     * 是否启用
     */
    private Integer enabled;
    
    /**
     * 是否叶子节点
     */
    private Integer leaf;
    
    /**
     * 租户ID
     */
    private String tenantId;
    
    /**
     * 关键词搜索（用于在部门名称、负责人等字段中搜索）
     */
    private String keyword;
    
    /**
     * 是否包含子部门
     */
    private Boolean includeChildren;
    
    /**
     * 是否仅查询根部门
     */
    private Boolean rootOnly;
    
    /**
     * 左值范围查询（嵌套集合模型）
     */
    private Integer leftValue;
    private Integer rightValue;
    
    /**
     * 分页参数
     */
    private Integer pageNum;
    private Integer pageSize;
    
    /**
     * 排序字段
     */
    private String orderBy;
    
    /**
     * 排序方向（ASC/DESC）
     */
    private String orderDirection;
    
    // 构造器
    public DepartmentQuery() {
    }
    
    public DepartmentQuery(String tenantId) {
        this.tenantId = tenantId;
    }
    
    // 链式设置方法
    public DepartmentQuery deptId(String deptId) {
        this.deptId = deptId;
        return this;
    }
    
    public DepartmentQuery deptName(String deptName) {
        this.deptName = deptName;
        return this;
    }
    
    public DepartmentQuery deptNo(String deptNo) {
        this.deptNo = deptNo;
        return this;
    }
    
    public DepartmentQuery parentDeptId(String parentDeptId) {
        this.parentDeptId = parentDeptId;
        return this;
    }
    
    public DepartmentQuery status(DepartmentStatus status) {
        this.status = status;
        return this;
    }
    
    public DepartmentQuery type(DepartmentType type) {
        this.type = type;
        return this;
    }
    
    public DepartmentQuery level(Integer deptLevel) {
        this.deptLevel = deptLevel;
        return this;
    }
    
    public DepartmentQuery levelRange(Integer minLevel, Integer maxLevel) {
        this.minLevel = minLevel;
        this.maxLevel = maxLevel;
        return this;
    }
    
    public DepartmentQuery managerId(String managerId) {
        this.managerId = managerId;
        return this;
    }
    
    public DepartmentQuery principalName(String principalName) {
        this.principalName = principalName;
        return this;
    }
    
    public DepartmentQuery category(Integer category) {
        this.category = category;
        return this;
    }
    
    public DepartmentQuery cityCode(String cityCode) {
        this.cityCode = cityCode;
        return this;
    }
    
    public DepartmentQuery ascnId(String ascnId) {
        this.ascnId = ascnId;
        return this;
    }
    
    public DepartmentQuery enabled(Integer enabled) {
        this.enabled = enabled;
        return this;
    }
    
    public DepartmentQuery leaf(Integer leaf) {
        this.leaf = leaf;
        return this;
    }
    
    public DepartmentQuery tenantId(String tenantId) {
        this.tenantId = tenantId;
        return this;
    }
    
    public DepartmentQuery keyword(String keyword) {
        this.keyword = keyword;
        return this;
    }
    
    public DepartmentQuery includeChildren(Boolean includeChildren) {
        this.includeChildren = includeChildren;
        return this;
    }
    
    public DepartmentQuery rootOnly(Boolean rootOnly) {
        this.rootOnly = rootOnly;
        return this;
    }
    
    public DepartmentQuery leftRightRange(Integer leftValue, Integer rightValue) {
        this.leftValue = leftValue;
        this.rightValue = rightValue;
        return this;
    }
    
    public DepartmentQuery page(Integer pageNum, Integer pageSize) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        return this;
    }
    
    public DepartmentQuery orderBy(String orderBy, String orderDirection) {
        this.orderBy = orderBy;
        this.orderDirection = orderDirection;
        return this;
    }
    
    // Getter and Setter methods
    public String getDeptId() { return deptId; }
    public void setDeptId(String deptId) { this.deptId = deptId; }
    
    public String getDeptName() { return deptName; }
    public void setDeptName(String deptName) { this.deptName = deptName; }
    
    public String getDeptNo() { return deptNo; }
    public void setDeptNo(String deptNo) { this.deptNo = deptNo; }
    
    public String getParentDeptId() { return parentDeptId; }
    public void setParentDeptId(String parentDeptId) { this.parentDeptId = parentDeptId; }
    
    public DepartmentStatus getStatus() { return status; }
    public void setStatus(DepartmentStatus status) { this.status = status; }
    
    public DepartmentType getType() { return type; }
    public void setType(DepartmentType type) { this.type = type; }
    
    public Integer getDeptLevel() { return deptLevel; }
    public void setDeptLevel(Integer deptLevel) { this.deptLevel = deptLevel; }
    
    public Integer getMinLevel() { return minLevel; }
    public void setMinLevel(Integer minLevel) { this.minLevel = minLevel; }
    
    public Integer getMaxLevel() { return maxLevel; }
    public void setMaxLevel(Integer maxLevel) { this.maxLevel = maxLevel; }
    
    public String getManagerId() { return managerId; }
    public void setManagerId(String managerId) { this.managerId = managerId; }
    
    public String getPrincipalName() { return principalName; }
    public void setPrincipalName(String principalName) { this.principalName = principalName; }
    
    public Integer getCategory() { return category; }
    public void setCategory(Integer category) { this.category = category; }
    
    public String getCityCode() { return cityCode; }
    public void setCityCode(String cityCode) { this.cityCode = cityCode; }
    
    public String getAscnId() { return ascnId; }
    public void setAscnId(String ascnId) { this.ascnId = ascnId; }
    
    public Integer getEnabled() { return enabled; }
    public void setEnabled(Integer enabled) { this.enabled = enabled; }
    
    public Integer getLeaf() { return leaf; }
    public void setLeaf(Integer leaf) { this.leaf = leaf; }
    
    public String getTenantId() { return tenantId; }
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }
    
    public String getKeyword() { return keyword; }
    public void setKeyword(String keyword) { this.keyword = keyword; }
    
    public Boolean getIncludeChildren() { return includeChildren; }
    public void setIncludeChildren(Boolean includeChildren) { this.includeChildren = includeChildren; }
    
    public Boolean getRootOnly() { return rootOnly; }
    public void setRootOnly(Boolean rootOnly) { this.rootOnly = rootOnly; }
    
    public Integer getLeftValue() { return leftValue; }
    public void setLeftValue(Integer leftValue) { this.leftValue = leftValue; }
    
    public Integer getRightValue() { return rightValue; }
    public void setRightValue(Integer rightValue) { this.rightValue = rightValue; }
    
    public Integer getPageNum() { return pageNum; }
    public void setPageNum(Integer pageNum) { this.pageNum = pageNum; }
    
    public Integer getPageSize() { return pageSize; }
    public void setPageSize(Integer pageSize) { this.pageSize = pageSize; }
    
    public String getOrderBy() { return orderBy; }
    public void setOrderBy(String orderBy) { this.orderBy = orderBy; }
    
    public String getOrderDirection() { return orderDirection; }
    public void setOrderDirection(String orderDirection) { this.orderDirection = orderDirection; }
}