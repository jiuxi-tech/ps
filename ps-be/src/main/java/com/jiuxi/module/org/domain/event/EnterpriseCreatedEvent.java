package com.jiuxi.module.org.domain.event;

import java.time.LocalDateTime;

/**
 * 企业创建事件
 * 
 * @author DDD Refactor
 * @date 2025-09-11
 */
public class EnterpriseCreatedEvent {
    
    /**
     * 企业ID
     */
    private final String entId;
    
    /**
     * 企业全称
     */
    private final String entFullName;
    
    /**
     * 企业简称
     */
    private final String entSimpleName;
    
    /**
     * 统一社会信用代码
     */
    private final String entUnifiedCode;
    
    /**
     * 企业类型
     */
    private final String entType;
    
    /**
     * 法人代表
     */
    private final String legalRepr;
    
    /**
     * 行业类别代码
     */
    private final String industryTypeCode;
    
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
    
    public EnterpriseCreatedEvent(String entId, String entFullName, String entSimpleName, 
                                 String entUnifiedCode, String entType, String legalRepr,
                                 String industryTypeCode, String tenantId, String creator) {
        this.entId = entId;
        this.entFullName = entFullName;
        this.entSimpleName = entSimpleName;
        this.entUnifiedCode = entUnifiedCode;
        this.entType = entType;
        this.legalRepr = legalRepr;
        this.industryTypeCode = industryTypeCode;
        this.tenantId = tenantId;
        this.creator = creator;
        this.occurredOn = LocalDateTime.now();
    }
    
    public String getEntId() {
        return entId;
    }
    
    public String getEntFullName() {
        return entFullName;
    }
    
    public String getEntSimpleName() {
        return entSimpleName;
    }
    
    public String getEntUnifiedCode() {
        return entUnifiedCode;
    }
    
    public String getEntType() {
        return entType;
    }
    
    public String getLegalRepr() {
        return legalRepr;
    }
    
    public String getIndustryTypeCode() {
        return industryTypeCode;
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
        return "EnterpriseCreatedEvent{" +
                "entId='" + entId + '\'' +
                ", entFullName='" + entFullName + '\'' +
                ", entSimpleName='" + entSimpleName + '\'' +
                ", entUnifiedCode='" + entUnifiedCode + '\'' +
                ", entType='" + entType + '\'' +
                ", legalRepr='" + legalRepr + '\'' +
                ", industryTypeCode='" + industryTypeCode + '\'' +
                ", tenantId='" + tenantId + '\'' +
                ", creator='" + creator + '\'' +
                ", occurredOn=" + occurredOn +
                '}';
    }
}