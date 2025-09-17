package com.jiuxi.module.org.domain.query;

import com.jiuxi.module.org.domain.model.entity.OrganizationType;
import com.jiuxi.module.org.domain.model.entity.OrganizationStatus;

/**
 * 组织查询规范对象
 * 支持复杂查询条件的封装和组合
 * 
 * @author DDD Refactor
 * @date 2025-09-17
 */
public class OrganizationQuery {
    
    /**
     * 组织ID
     */
    private String organizationId;
    
    /**
     * 组织名称（支持模糊查询）
     */
    private String organizationName;
    
    /**
     * 组织简称（支持模糊查询）
     */
    private String organizationShortName;
    
    /**
     * 组织代码
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
     * 最小级别
     */
    private Integer minLevel;
    
    /**
     * 最大级别
     */
    private Integer maxLevel;
    
    /**
     * 组织路径
     */
    private String organizationPath;
    
    /**
     * 行政区划代码
     */
    private String cityCode;
    
    /**
     * 负责人姓名（支持模糊查询）
     */
    private String principalName;
    
    /**
     * 负责人电话
     */
    private String principalTel;
    
    /**
     * 联系电话
     */
    private String contactPhone;
    
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
     * 租户ID
     */
    private String tenantId;
    
    /**
     * 关键词搜索（用于在组织名称、负责人等字段中搜索）
     */
    private String keyword;
    
    /**
     * 是否包含子组织
     */
    private Boolean includeChildren;
    
    /**
     * 是否仅查询根组织
     */
    private Boolean rootOnly;
    
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
    public OrganizationQuery() {
    }
    
    public OrganizationQuery(String tenantId) {
        this.tenantId = tenantId;
    }
    
    // 链式设置方法
    public OrganizationQuery organizationId(String organizationId) {
        this.organizationId = organizationId;
        return this;
    }
    
    public OrganizationQuery organizationName(String organizationName) {
        this.organizationName = organizationName;
        return this;
    }
    
    public OrganizationQuery organizationShortName(String organizationShortName) {
        this.organizationShortName = organizationShortName;
        return this;
    }
    
    public OrganizationQuery organizationCode(String organizationCode) {
        this.organizationCode = organizationCode;
        return this;
    }
    
    public OrganizationQuery organizationType(OrganizationType organizationType) {
        this.organizationType = organizationType;
        return this;
    }
    
    public OrganizationQuery parentOrganizationId(String parentOrganizationId) {
        this.parentOrganizationId = parentOrganizationId;
        return this;
    }
    
    public OrganizationQuery organizationLevel(Integer organizationLevel) {
        this.organizationLevel = organizationLevel;
        return this;
    }
    
    public OrganizationQuery levelRange(Integer minLevel, Integer maxLevel) {
        this.minLevel = minLevel;
        this.maxLevel = maxLevel;
        return this;
    }
    
    public OrganizationQuery organizationPath(String organizationPath) {
        this.organizationPath = organizationPath;
        return this;
    }
    
    public OrganizationQuery cityCode(String cityCode) {
        this.cityCode = cityCode;
        return this;
    }
    
    public OrganizationQuery principalName(String principalName) {
        this.principalName = principalName;
        return this;
    }
    
    public OrganizationQuery principalTel(String principalTel) {
        this.principalTel = principalTel;
        return this;
    }
    
    public OrganizationQuery contactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
        return this;
    }
    
    public OrganizationQuery status(OrganizationStatus status) {
        this.status = status;
        return this;
    }
    
    public OrganizationQuery enabled(Integer enabled) {
        this.enabled = enabled;
        return this;
    }
    
    public OrganizationQuery actived(Integer actived) {
        this.actived = actived;
        return this;
    }
    
    public OrganizationQuery tenantId(String tenantId) {
        this.tenantId = tenantId;
        return this;
    }
    
    public OrganizationQuery keyword(String keyword) {
        this.keyword = keyword;
        return this;
    }
    
    public OrganizationQuery includeChildren(Boolean includeChildren) {
        this.includeChildren = includeChildren;
        return this;
    }
    
    public OrganizationQuery rootOnly(Boolean rootOnly) {
        this.rootOnly = rootOnly;
        return this;
    }
    
    public OrganizationQuery page(Integer pageNum, Integer pageSize) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        return this;
    }
    
    public OrganizationQuery orderBy(String orderBy, String orderDirection) {
        this.orderBy = orderBy;
        this.orderDirection = orderDirection;
        return this;
    }
    
    // Getter and Setter methods
    public String getOrganizationId() { return organizationId; }
    public void setOrganizationId(String organizationId) { this.organizationId = organizationId; }
    
    public String getOrganizationName() { return organizationName; }
    public void setOrganizationName(String organizationName) { this.organizationName = organizationName; }
    
    public String getOrganizationShortName() { return organizationShortName; }
    public void setOrganizationShortName(String organizationShortName) { this.organizationShortName = organizationShortName; }
    
    public String getOrganizationCode() { return organizationCode; }
    public void setOrganizationCode(String organizationCode) { this.organizationCode = organizationCode; }
    
    public OrganizationType getOrganizationType() { return organizationType; }
    public void setOrganizationType(OrganizationType organizationType) { this.organizationType = organizationType; }
    
    public String getParentOrganizationId() { return parentOrganizationId; }
    public void setParentOrganizationId(String parentOrganizationId) { this.parentOrganizationId = parentOrganizationId; }
    
    public Integer getOrganizationLevel() { return organizationLevel; }
    public void setOrganizationLevel(Integer organizationLevel) { this.organizationLevel = organizationLevel; }
    
    public Integer getMinLevel() { return minLevel; }
    public void setMinLevel(Integer minLevel) { this.minLevel = minLevel; }
    
    public Integer getMaxLevel() { return maxLevel; }
    public void setMaxLevel(Integer maxLevel) { this.maxLevel = maxLevel; }
    
    public String getOrganizationPath() { return organizationPath; }
    public void setOrganizationPath(String organizationPath) { this.organizationPath = organizationPath; }
    
    public String getCityCode() { return cityCode; }
    public void setCityCode(String cityCode) { this.cityCode = cityCode; }
    
    public String getPrincipalName() { return principalName; }
    public void setPrincipalName(String principalName) { this.principalName = principalName; }
    
    public String getPrincipalTel() { return principalTel; }
    public void setPrincipalTel(String principalTel) { this.principalTel = principalTel; }
    
    public String getContactPhone() { return contactPhone; }
    public void setContactPhone(String contactPhone) { this.contactPhone = contactPhone; }
    
    public OrganizationStatus getStatus() { return status; }
    public void setStatus(OrganizationStatus status) { this.status = status; }
    
    public Integer getEnabled() { return enabled; }
    public void setEnabled(Integer enabled) { this.enabled = enabled; }
    
    public Integer getActived() { return actived; }
    public void setActived(Integer actived) { this.actived = actived; }
    
    public String getTenantId() { return tenantId; }
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }
    
    public String getKeyword() { return keyword; }
    public void setKeyword(String keyword) { this.keyword = keyword; }
    
    public Boolean getIncludeChildren() { return includeChildren; }
    public void setIncludeChildren(Boolean includeChildren) { this.includeChildren = includeChildren; }
    
    public Boolean getRootOnly() { return rootOnly; }
    public void setRootOnly(Boolean rootOnly) { this.rootOnly = rootOnly; }
    
    public Integer getPageNum() { return pageNum; }
    public void setPageNum(Integer pageNum) { this.pageNum = pageNum; }
    
    public Integer getPageSize() { return pageSize; }
    public void setPageSize(Integer pageSize) { this.pageSize = pageSize; }
    
    public String getOrderBy() { return orderBy; }
    public void setOrderBy(String orderBy) { this.orderBy = orderBy; }
    
    public String getOrderDirection() { return orderDirection; }
    public void setOrderDirection(String orderDirection) { this.orderDirection = orderDirection; }
}