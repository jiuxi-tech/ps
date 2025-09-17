package com.jiuxi.module.org.domain.event;

import com.jiuxi.module.org.domain.model.entity.OrganizationType;

import java.time.LocalDateTime;

/**
 * 组织创建事件
 * 
 * @author DDD Refactor
 * @date 2025-09-11
 */
public class OrganizationCreatedEvent {
    
    /**
     * 组织ID
     */
    private final String organizationId;
    
    /**
     * 组织名称
     */
    private final String organizationName;
    
    /**
     * 组织简称
     */
    private final String organizationShortName;
    
    /**
     * 组织代码
     */
    private final String organizationCode;
    
    /**
     * 组织类型
     */
    private final OrganizationType organizationType;
    
    /**
     * 上级组织ID
     */
    private final String parentOrganizationId;
    
    /**
     * 组织级别
     */
    private final Integer organizationLevel;
    
    /**
     * 组织路径
     */
    private final String organizationPath;
    
    /**
     * 行政区划代码
     */
    private final String cityCode;
    
    /**
     * 租户ID
     */
    private final String tenantId;
    
    /**
     * 创建者
     */
    private final String creator;
    
    /**
     * 事件发生时间
     */
    private final LocalDateTime occurredOn;
    
    public OrganizationCreatedEvent(String organizationId, String organizationName, String organizationShortName,
                                   String organizationCode, OrganizationType organizationType, 
                                   String parentOrganizationId, Integer organizationLevel, String organizationPath,
                                   String cityCode, String tenantId, String creator) {
        this.organizationId = organizationId;
        this.organizationName = organizationName;
        this.organizationShortName = organizationShortName;
        this.organizationCode = organizationCode;
        this.organizationType = organizationType;
        this.parentOrganizationId = parentOrganizationId;
        this.organizationLevel = organizationLevel;
        this.organizationPath = organizationPath;
        this.cityCode = cityCode;
        this.tenantId = tenantId;
        this.creator = creator;
        this.occurredOn = LocalDateTime.now();
    }
    
    public String getOrganizationId() {
        return organizationId;
    }
    
    public String getOrganizationName() {
        return organizationName;
    }
    
    public String getOrganizationShortName() {
        return organizationShortName;
    }
    
    public String getOrganizationCode() {
        return organizationCode;
    }
    
    public OrganizationType getOrganizationType() {
        return organizationType;
    }
    
    public String getParentOrganizationId() {
        return parentOrganizationId;
    }
    
    public Integer getOrganizationLevel() {
        return organizationLevel;
    }
    
    public String getOrganizationPath() {
        return organizationPath;
    }
    
    public String getCityCode() {
        return cityCode;
    }
    
    public String getTenantId() {
        return tenantId;
    }
    
    public String getCreator() {
        return creator;
    }
    
    public LocalDateTime getOccurredOn() {
        return occurredOn;
    }
    
    @Override
    public String toString() {
        return "OrganizationCreatedEvent{" +
                "organizationId='" + organizationId + '\'' +
                ", organizationName='" + organizationName + '\'' +
                ", organizationShortName='" + organizationShortName + '\'' +
                ", organizationCode='" + organizationCode + '\'' +
                ", organizationType=" + organizationType +
                ", parentOrganizationId='" + parentOrganizationId + '\'' +
                ", organizationLevel=" + organizationLevel +
                ", organizationPath='" + organizationPath + '\'' +
                ", cityCode='" + cityCode + '\'' +
                ", tenantId='" + tenantId + '\'' +
                ", creator='" + creator + '\'' +
                ", occurredOn=" + occurredOn +
                '}';
    }
}