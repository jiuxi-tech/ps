package com.jiuxi.module.org.app.query.dto;

import com.jiuxi.module.org.domain.model.entity.OrganizationType;
import com.jiuxi.module.org.domain.model.entity.OrganizationStatus;
import java.time.LocalDateTime;

/**
 * 组织响应DTO
 * 用于组织查询结果的数据传输
 * 
 * @author DDD Refactor
 * @date 2025-09-18
 */
public class OrganizationResponseDTO {
    
    private String organizationId;
    private String organizationName;
    private String organizationCode;
    private OrganizationType organizationType;
    private String parentOrganizationId;
    private String organizationPath;
    private Integer level;
    private String description;
    private Integer orderIndex;
    private OrganizationStatus status;
    private String tenantId;
    private String creator;
    private LocalDateTime createTime;
    private String updator;
    private LocalDateTime updateTime;
    
    // Constructors
    public OrganizationResponseDTO() {}
    
    public OrganizationResponseDTO(String organizationId, String organizationName, OrganizationType organizationType) {
        this.organizationId = organizationId;
        this.organizationName = organizationName;
        this.organizationType = organizationType;
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
    
    public String getOrganizationPath() {
        return organizationPath;
    }
    
    public void setOrganizationPath(String organizationPath) {
        this.organizationPath = organizationPath;
    }
    
    public Integer getLevel() {
        return level;
    }
    
    public void setLevel(Integer level) {
        this.level = level;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public Integer getOrderIndex() {
        return orderIndex;
    }
    
    public void setOrderIndex(Integer orderIndex) {
        this.orderIndex = orderIndex;
    }
    
    public OrganizationStatus getStatus() {
        return status;
    }
    
    public void setStatus(OrganizationStatus status) {
        this.status = status;
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
    
    @Override
    public String toString() {
        return "OrganizationResponseDTO{" +
                "organizationId='" + organizationId + '\'' +
                ", organizationName='" + organizationName + '\'' +
                ", organizationCode='" + organizationCode + '\'' +
                ", organizationType=" + organizationType +
                ", parentOrganizationId='" + parentOrganizationId + '\'' +
                ", organizationPath='" + organizationPath + '\'' +
                ", level=" + level +
                ", description='" + description + '\'' +
                ", orderIndex=" + orderIndex +
                ", status=" + status +
                ", tenantId='" + tenantId + '\'' +
                ", creator='" + creator + '\'' +
                ", createTime=" + createTime +
                ", updator='" + updator + '\'' +
                ", updateTime=" + updateTime +
                '}';
    }
}