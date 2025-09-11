package com.jiuxi.module.org.domain.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 组织实体
 * 可以代表政府机构或企业集团等高层级组织
 * 
 * @author DDD Refactor
 * @date 2025-09-11
 */
public class Organization {
    
    /**
     * 组织ID
     */
    private String organizationId;
    
    /**
     * 组织名称
     */
    private String organizationName;
    
    /**
     * 组织简称
     */
    private String organizationShortName;
    
    /**
     * 组织代码（统一社会信用代码或组织机构代码）
     */
    private String organizationCode;
    
    /**
     * 组织类型
     */
    private OrganizationType organizationType;
    
    /**
     * 上级组织ID
     */
    private String parentOrganizationId;
    
    /**
     * 组织级别
     */
    private Integer organizationLevel;
    
    /**
     * 组织路径
     */
    private String organizationPath;
    
    /**
     * 行政区划代码
     */
    private String cityCode;
    
    /**
     * 组织地址
     */
    private String address;
    
    /**
     * 联系电话
     */
    private String contactPhone;
    
    /**
     * 负责人姓名
     */
    private String principalName;
    
    /**
     * 负责人电话
     */
    private String principalTel;
    
    /**
     * 组织状态
     */
    private OrganizationStatus status;
    
    /**
     * 是否启用
     */
    private Integer enabled;
    
    /**
     * 是否有效
     */
    private Integer actived;
    
    /**
     * 所属部门列表
     */
    private List<Department> departments;
    
    /**
     * 所属企业列表（如果是政府机构管理的企业）
     */
    private List<Enterprise> enterprises;
    
    /**
     * 子组织列表
     */
    private List<Organization> subOrganizations;
    
    /**
     * 组织描述
     */
    private String description;
    
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
    public Organization() {
        this.status = OrganizationStatus.ACTIVE;
        this.createTime = LocalDateTime.now();
        this.enabled = 1;
        this.actived = 1;
        this.departments = new ArrayList<>();
        this.enterprises = new ArrayList<>();
        this.subOrganizations = new ArrayList<>();
    }
    
    public Organization(String organizationName, OrganizationType organizationType) {
        this();
        this.organizationName = organizationName;
        this.organizationType = organizationType;
    }
    
    /**
     * 添加部门
     */
    public void addDepartment(Department department) {
        if (department == null) {
            throw new IllegalArgumentException("部门不能为空");
        }
        
        if (this.departments == null) {
            this.departments = new ArrayList<>();
        }
        
        this.departments.add(department);
    }
    
    /**
     * 移除部门
     */
    public void removeDepartment(String deptId) {
        if (this.departments != null) {
            this.departments.removeIf(dept -> Objects.equals(dept.getDeptId(), deptId));
        }
    }
    
    /**
     * 添加企业
     */
    public void addEnterprise(Enterprise enterprise) {
        if (enterprise == null) {
            throw new IllegalArgumentException("企业不能为空");
        }
        
        if (this.enterprises == null) {
            this.enterprises = new ArrayList<>();
        }
        
        this.enterprises.add(enterprise);
    }
    
    /**
     * 移除企业
     */
    public void removeEnterprise(String entId) {
        if (this.enterprises != null) {
            this.enterprises.removeIf(ent -> Objects.equals(ent.getEntId(), entId));
        }
    }
    
    /**
     * 添加子组织
     */
    public void addSubOrganization(Organization subOrganization) {
        if (subOrganization == null) {
            throw new IllegalArgumentException("子组织不能为空");
        }
        
        if (this.subOrganizations == null) {
            this.subOrganizations = new ArrayList<>();
        }
        
        subOrganization.parentOrganizationId = this.organizationId;
        subOrganization.organizationLevel = this.organizationLevel != null ? this.organizationLevel + 1 : 1;
        subOrganization.updateOrganizationPath(this.organizationPath);
        
        this.subOrganizations.add(subOrganization);
    }
    
    /**
     * 更新组织路径
     */
    public void updateOrganizationPath(String parentPath) {
        if (parentPath == null || parentPath.trim().isEmpty()) {
            this.organizationPath = this.organizationId;
        } else {
            this.organizationPath = parentPath + "/" + this.organizationId;
        }
        
        // 更新所有子组织的路径
        if (this.subOrganizations != null) {
            for (Organization subOrg : this.subOrganizations) {
                subOrg.updateOrganizationPath(this.organizationPath);
            }
        }
    }
    
    /**
     * 激活组织
     */
    public void activate() {
        this.status = OrganizationStatus.ACTIVE;
        this.enabled = 1;
        this.actived = 1;
        this.updateTime = LocalDateTime.now();
    }
    
    /**
     * 停用组织
     */
    public void deactivate() {
        this.status = OrganizationStatus.INACTIVE;
        this.enabled = 0;
        this.updateTime = LocalDateTime.now();
    }
    
    /**
     * 检查组织是否有效
     */
    public boolean isValid() {
        return this.actived != null && this.actived == 1;
    }
    
    /**
     * 检查组织是否启用
     */
    public boolean isEnabled() {
        return this.enabled != null && this.enabled == 1;
    }
    
    /**
     * 检查组织是否激活
     */
    public boolean isActive() {
        return OrganizationStatus.ACTIVE.equals(this.status);
    }
    
    /**
     * 检查是否为根组织
     */
    public boolean isRoot() {
        return this.parentOrganizationId == null || this.parentOrganizationId.trim().isEmpty();
    }
    
    // Getters and Setters
    public String getOrganizationId() {
        return organizationId;
    }
    
    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }
    
    public String getOrganizationName() {
        return organizationName;
    }
    
    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }
    
    public String getOrganizationShortName() {
        return organizationShortName;
    }
    
    public void setOrganizationShortName(String organizationShortName) {
        this.organizationShortName = organizationShortName;
    }
    
    public String getOrganizationCode() {
        return organizationCode;
    }
    
    public void setOrganizationCode(String organizationCode) {
        this.organizationCode = organizationCode;
    }
    
    public OrganizationType getOrganizationType() {
        return organizationType;
    }
    
    public void setOrganizationType(OrganizationType organizationType) {
        this.organizationType = organizationType;
    }
    
    public String getParentOrganizationId() {
        return parentOrganizationId;
    }
    
    public void setParentOrganizationId(String parentOrganizationId) {
        this.parentOrganizationId = parentOrganizationId;
    }
    
    public Integer getOrganizationLevel() {
        return organizationLevel;
    }
    
    public void setOrganizationLevel(Integer organizationLevel) {
        this.organizationLevel = organizationLevel;
    }
    
    public String getOrganizationPath() {
        return organizationPath;
    }
    
    public void setOrganizationPath(String organizationPath) {
        this.organizationPath = organizationPath;
    }
    
    public String getCityCode() {
        return cityCode;
    }
    
    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }
    
    public String getAddress() {
        return address;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }
    
    public String getContactPhone() {
        return contactPhone;
    }
    
    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
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
    
    public OrganizationStatus getStatus() {
        return status;
    }
    
    public void setStatus(OrganizationStatus status) {
        this.status = status;
    }
    
    public Integer getEnabled() {
        return enabled;
    }
    
    public void setEnabled(Integer enabled) {
        this.enabled = enabled;
    }
    
    public Integer getActived() {
        return actived;
    }
    
    public void setActived(Integer actived) {
        this.actived = actived;
    }
    
    public List<Department> getDepartments() {
        return departments;
    }
    
    public void setDepartments(List<Department> departments) {
        this.departments = departments;
    }
    
    public List<Enterprise> getEnterprises() {
        return enterprises;
    }
    
    public void setEnterprises(List<Enterprise> enterprises) {
        this.enterprises = enterprises;
    }
    
    public List<Organization> getSubOrganizations() {
        return subOrganizations;
    }
    
    public void setSubOrganizations(List<Organization> subOrganizations) {
        this.subOrganizations = subOrganizations;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
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
        Organization that = (Organization) o;
        return Objects.equals(organizationId, that.organizationId);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(organizationId);
    }
    
    @Override
    public String toString() {
        return "Organization{" +
                "organizationId='" + organizationId + '\'' +
                ", organizationName='" + organizationName + '\'' +
                ", organizationType=" + organizationType +
                ", status=" + status +
                '}';
    }
}