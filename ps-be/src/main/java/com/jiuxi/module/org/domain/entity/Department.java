package com.jiuxi.module.org.domain.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 部门聚合根
 * 
 * @author DDD Refactor
 * @date 2025-09-06
 */
public class Department {
    
    /**
     * 部门ID
     */
    private String deptId;
    
    /**
     * 部门编号
     */
    private String deptNo;
    
    /**
     * 部门名称（对应deptFullName）
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
     * 部门层级编码（对应deptLevelcode）
     */
    private String deptLevelCode;
    
    /**
     * 部门层级路径
     */
    private String deptPath;
    
    /**
     * 部门层级
     */
    private Integer deptLevel;
    
    /**
     * 排序序号（对应orderIndex）
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
     * 部门负责人（对应principalName）
     */
    private String principalName;
    
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
     * 负责人电话（对应principalTel）
     */
    private String principalTel;
    
    /**
     * 部门类别 政府or企业（对应category）
     */
    private Integer category;
    
    /**
     * 行政区划code（对应cityCode）
     */
    private String cityCode;
    
    /**
     * 所属机构（单位）id（对应ascnId）
     */
    private String ascnId;
    
    /**
     * 是否启用（对应enabled）
     */
    private Integer enabled;
    
    /**
     * 是否叶子节点（对应leaf）
     */
    private Integer leaf;
    
    /**
     * 部门描述（对应deptDesc）
     */
    private String deptDesc;
    
    /**
     * 子部门列表
     */
    private List<Department> children;
    
    /**
     * 创建信息
     */
    private String creator;
    private LocalDateTime createTime;
    
    /**
     * 更新信息
     */
    private String updator;
    private LocalDateTime updateTime;
    
    /**
     * 租户ID
     */
    private String tenantId;
    
    // 构造器
    public Department() {
        this.children = new ArrayList<>();
        this.status = DepartmentStatus.ACTIVE;
        this.createTime = LocalDateTime.now();
    }
    
    public Department(String deptName, String parentDeptId) {
        this();
        this.deptName = deptName;
        this.parentDeptId = parentDeptId;
    }
    
    /**
     * 添加子部门
     */
    public void addChild(Department child) {
        if (child == null) {
            throw new IllegalArgumentException("子部门不能为空");
        }
        
        if (this.children == null) {
            this.children = new ArrayList<>();
        }
        
        child.parentDeptId = this.deptId;
        child.deptLevel = this.deptLevel != null ? this.deptLevel + 1 : 1;
        child.updateDeptPath(this.deptPath);
        
        this.children.add(child);
    }
    
    /**
     * 移除子部门
     */
    public void removeChild(String childDeptId) {
        if (this.children != null) {
            this.children.removeIf(child -> Objects.equals(child.getDeptId(), childDeptId));
        }
    }
    
    /**
     * 更新部门路径
     */
    public void updateDeptPath(String parentPath) {
        if (parentPath == null || parentPath.trim().isEmpty()) {
            this.deptPath = this.deptId;
        } else {
            this.deptPath = parentPath + "/" + this.deptId;
        }
        
        // 更新所有子部门的路径
        if (this.children != null) {
            for (Department child : this.children) {
                child.updateDeptPath(this.deptPath);
            }
        }
    }
    
    /**
     * 激活部门
     */
    public void activate() {
        this.status = DepartmentStatus.ACTIVE;
        this.updateTime = LocalDateTime.now();
    }
    
    /**
     * 停用部门
     */
    public void deactivate() {
        this.status = DepartmentStatus.INACTIVE;
        this.updateTime = LocalDateTime.now();
    }
    
    /**
     * 检查是否为根部门
     */
    public boolean isRoot() {
        return this.parentDeptId == null || this.parentDeptId.trim().isEmpty() || "-1".equals(this.parentDeptId);
    }
    
    /**
     * 检查是否为叶子部门
     */
    public boolean isLeaf() {
        return this.children == null || this.children.isEmpty();
    }
    
    /**
     * 检查是否有子部门
     */
    public boolean hasChildren() {
        return !isLeaf();
    }
    
    /**
     * 检查部门是否激活
     */
    public boolean isActive() {
        return DepartmentStatus.ACTIVE.equals(this.status);
    }
    
    /**
     * 设置部门负责人
     */
    public void setManager(String managerId) {
        this.managerId = managerId;
        this.updateTime = LocalDateTime.now();
    }
    
    /**
     * 更新联系信息
     */
    public void updateContactInfo(String contactPhone, String address) {
        this.contactPhone = contactPhone;
        this.address = address;
        this.updateTime = LocalDateTime.now();
    }
    
    // Getters and Setters
    public String getDeptId() {
        return deptId;
    }
    
    public String getDeptNo() {
        return deptNo;
    }
    
    public void setDeptNo(String deptNo) {
        this.deptNo = deptNo;
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
    
    public String getDeptLevelCode() {
        return deptLevelCode;
    }
    
    public void setDeptLevelCode(String deptLevelCode) {
        this.deptLevelCode = deptLevelCode;
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
    
    public String getPrincipalName() {
        return principalName;
    }
    
    public void setPrincipalName(String principalName) {
        this.principalName = principalName;
    }
    
    public String getPrincipalTel() {
        return principalTel;
    }
    
    public void setPrincipalTel(String principalTel) {
        this.principalTel = principalTel;
    }
    
    public Integer getCategory() {
        return category;
    }
    
    public void setCategory(Integer category) {
        this.category = category;
    }
    
    public String getCityCode() {
        return cityCode;
    }
    
    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }
    
    public String getAscnId() {
        return ascnId;
    }
    
    public void setAscnId(String ascnId) {
        this.ascnId = ascnId;
    }
    
    public Integer getEnabled() {
        return enabled;
    }
    
    public void setEnabled(Integer enabled) {
        this.enabled = enabled;
    }
    
    public Integer getLeaf() {
        return leaf;
    }
    
    public void setLeaf(Integer leaf) {
        this.leaf = leaf;
    }
    
    public String getDeptDesc() {
        return deptDesc;
    }
    
    public void setDeptDesc(String deptDesc) {
        this.deptDesc = deptDesc;
    }
    
    public List<Department> getChildren() {
        return children;
    }
    
    public void setChildren(List<Department> children) {
        this.children = children;
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
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Department that = (Department) o;
        return Objects.equals(deptId, that.deptId);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(deptId);
    }
    
    @Override
    public String toString() {
        return "Department{" +
                "deptId='" + deptId + '\'' +
                ", deptName='" + deptName + '\'' +
                ", parentDeptId='" + parentDeptId + '\'' +
                ", status=" + status +
                ", type=" + type +
                '}';
    }
}