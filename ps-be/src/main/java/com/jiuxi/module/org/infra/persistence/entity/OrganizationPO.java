package com.jiuxi.module.org.infra.persistence.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDateTime;

/**
 * 组织持久化对象
 * 对应数据库表 tp_org_basicinfo
 * 
 * @author DDD Refactor
 * @date 2025-09-18
 */
@TableName("tp_org_basicinfo")
public class OrganizationPO {
    
    /**
     * 组织ID
     */
    @TableId
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
    private String organizationType;
    
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
    private String status;
    
    /**
     * 是否启用
     */
    private Integer enabled;
    
    /**
     * 是否有效
     */
    private Integer actived;
    
    /**
     * 组织描述
     */
    private String description;
    
    /**
     * 租户ID
     */
    private String tenantId;
    
    /**
     * 创建人
     */
    private String creator;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    
    /**
     * 更新人
     */
    private String updator;
    
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
    
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
    
    public String getOrganizationType() {
        return organizationType;
    }
    
    public void setOrganizationType(String organizationType) {
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
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
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
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getTenantId() {
        return tenantId;
    }
    
    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
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
}