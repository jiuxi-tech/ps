package com.jiuxi.module.org.app.query.dto;

import java.time.LocalDateTime;

/**
 * 企业响应DTO
 * 用于企业查询结果的数据传输
 * 
 * @author DDD Refactor
 * @date 2025-09-18
 */
public class EnterpriseResponseDTO {
    
    private String entId;
    private String entFullName;
    private String entSimpleName;
    private String entUnifiedCode;
    private String entType;
    private String entPhone;
    private String entEmail;
    private String entAddress;
    private String entLegalPerson;
    private String entManager;
    private String entDescription;
    private String entLongitude;
    private String entLatitude;
    private String entStatus;
    private String tenantId;
    private String creator;
    private LocalDateTime createTime;
    private String updator;
    private LocalDateTime updateTime;
    
    // Constructors
    public EnterpriseResponseDTO() {}
    
    public EnterpriseResponseDTO(String entId, String entFullName, String entUnifiedCode) {
        this.entId = entId;
        this.entFullName = entFullName;
        this.entUnifiedCode = entUnifiedCode;
    }
    
    // Getters and Setters
    public String getEntId() {
        return entId;
    }
    
    public void setEntId(String entId) {
        this.entId = entId;
    }
    
    public String getEntFullName() {
        return entFullName;
    }
    
    public void setEntFullName(String entFullName) {
        this.entFullName = entFullName;
    }
    
    public String getEntSimpleName() {
        return entSimpleName;
    }
    
    public void setEntSimpleName(String entSimpleName) {
        this.entSimpleName = entSimpleName;
    }
    
    public String getEntUnifiedCode() {
        return entUnifiedCode;
    }
    
    public void setEntUnifiedCode(String entUnifiedCode) {
        this.entUnifiedCode = entUnifiedCode;
    }
    
    public String getEntType() {
        return entType;
    }
    
    public void setEntType(String entType) {
        this.entType = entType;
    }
    
    public String getEntPhone() {
        return entPhone;
    }
    
    public void setEntPhone(String entPhone) {
        this.entPhone = entPhone;
    }
    
    public String getEntEmail() {
        return entEmail;
    }
    
    public void setEntEmail(String entEmail) {
        this.entEmail = entEmail;
    }
    
    public String getEntAddress() {
        return entAddress;
    }
    
    public void setEntAddress(String entAddress) {
        this.entAddress = entAddress;
    }
    
    public String getEntLegalPerson() {
        return entLegalPerson;
    }
    
    public void setEntLegalPerson(String entLegalPerson) {
        this.entLegalPerson = entLegalPerson;
    }
    
    public String getEntManager() {
        return entManager;
    }
    
    public void setEntManager(String entManager) {
        this.entManager = entManager;
    }
    
    public String getEntDescription() {
        return entDescription;
    }
    
    public void setEntDescription(String entDescription) {
        this.entDescription = entDescription;
    }
    
    public String getEntLongitude() {
        return entLongitude;
    }
    
    public void setEntLongitude(String entLongitude) {
        this.entLongitude = entLongitude;
    }
    
    public String getEntLatitude() {
        return entLatitude;
    }
    
    public void setEntLatitude(String entLatitude) {
        this.entLatitude = entLatitude;
    }
    
    public String getEntStatus() {
        return entStatus;
    }
    
    public void setEntStatus(String entStatus) {
        this.entStatus = entStatus;
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
        return "EnterpriseResponseDTO{" +
                "entId='" + entId + '\'' +
                ", entFullName='" + entFullName + '\'' +
                ", entSimpleName='" + entSimpleName + '\'' +
                ", entUnifiedCode='" + entUnifiedCode + '\'' +
                ", entType='" + entType + '\'' +
                ", entPhone='" + entPhone + '\'' +
                ", entEmail='" + entEmail + '\'' +
                ", entAddress='" + entAddress + '\'' +
                ", entLegalPerson='" + entLegalPerson + '\'' +
                ", entManager='" + entManager + '\'' +
                ", entDescription='" + entDescription + '\'' +
                ", entLongitude='" + entLongitude + '\'' +
                ", entLatitude='" + entLatitude + '\'' +
                ", entStatus='" + entStatus + '\'' +
                ", tenantId='" + tenantId + '\'' +
                ", creator='" + creator + '\'' +
                ", createTime=" + createTime +
                ", updator='" + updator + '\'' +
                ", updateTime=" + updateTime +
                '}';
    }
}