package com.jiuxi.module.org.domain.model.aggregate;

import java.time.LocalDateTime;
import java.util.Objects;

import com.jiuxi.module.org.domain.model.entity.EnterpriseStatus;

/**
 * 企业实体
 * 对应tp_ent_basicinfo表
 * 
 * @author DDD Refactor  
 * @date 2025-09-11
 */
public class Enterprise {
    
    /**
     * 企业ID
     */
    private String entId;
    
    /**
     * 企业名称（全称）
     */
    private String entFullName;
    
    /**
     * 企业简称
     */
    private String entSimpleName;
    
    /**
     * 统一社会信用代码
     */
    private String entUnifiedCode;
    
    /**
     * 企业类型
     */
    private String entType;
    
    /**
     * 企业简介
     */
    private String entDesc;
    
    /**
     * 法定代表人
     */
    private String legalRepr;
    
    /**
     * 法人联系方式
     */
    private String legalReprTel;
    
    /**
     * 联系人姓名
     */
    private String linkPsnName;
    
    /**
     * 联系电话
     */
    private String linkPsnTel;
    
    /**
     * 注册资金
     */
    private String regFund;
    
    /**
     * 注册地址code
     */
    private String entAddrCode;
    
    /**
     * 注册地址
     */
    private String entAddr;
    
    /**
     * 企业坐标_经度
     */
    private String longitude;
    
    /**
     * 企业坐标_纬度
     */
    private String latitude;
    
    /**
     * 经纬度所计算的geohash码
     */
    private String geoCode;
    
    /**
     * 生产地行政区划CODE
     */
    private String prodAddrCode;
    
    /**
     * 生产经营详细地址
     */
    private String prodAddr;
    
    /**
     * 行业类别代码
     */
    private String industryTypeCode;
    
    /**
     * 条线code
     */
    private String lineCode;
    
    /**
     * 企业规模
     */
    private String scaleType;
    
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
     * 企业状态
     */
    private EnterpriseStatus status;
    
    // 构造器
    public Enterprise() {
        this.status = EnterpriseStatus.ACTIVE;
        this.createTime = LocalDateTime.now();
        this.enabled = 1;
        this.actived = 1;
    }
    
    public Enterprise(String entFullName, String entUnifiedCode) {
        this();
        this.entFullName = entFullName;
        this.entUnifiedCode = entUnifiedCode;
    }
    
    /**
     * 激活企业
     */
    public void activate() {
        this.status = EnterpriseStatus.ACTIVE;
        this.enabled = 1;
        this.actived = 1;
        this.updateTime = LocalDateTime.now();
    }
    
    /**
     * 停用企业
     */
    public void deactivate() {
        this.status = EnterpriseStatus.INACTIVE;
        this.enabled = 0;
        this.updateTime = LocalDateTime.now();
    }
    
    /**
     * 注销企业
     */
    public void cancel() {
        this.status = EnterpriseStatus.CANCELLED;
        this.actived = 0;
        this.updateTime = LocalDateTime.now();
    }
    
    /**
     * 检查企业是否有效
     */
    public boolean isValid() {
        return this.actived != null && this.actived == 1;
    }
    
    /**
     * 检查企业是否启用
     */
    public boolean isEnabled() {
        return this.enabled != null && this.enabled == 1;
    }
    
    /**
     * 检查企业是否激活
     */
    public boolean isActive() {
        return EnterpriseStatus.ACTIVE.equals(this.status);
    }
    
    /**
     * 更新法人信息
     */
    public void updateLegalRepresentative(String legalRepr, String legalReprTel) {
        this.legalRepr = legalRepr;
        this.legalReprTel = legalReprTel;
        this.updateTime = LocalDateTime.now();
    }
    
    /**
     * 更新联系人信息
     */
    public void updateContactInfo(String linkPsnName, String linkPsnTel) {
        this.linkPsnName = linkPsnName;
        this.linkPsnTel = linkPsnTel;
        this.updateTime = LocalDateTime.now();
    }
    
    /**
     * 更新地理位置信息
     */
    public void updateGeolocation(String longitude, String latitude, String geoCode) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.geoCode = geoCode;
        this.updateTime = LocalDateTime.now();
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
    
    public String getEntDesc() {
        return entDesc;
    }
    
    public void setEntDesc(String entDesc) {
        this.entDesc = entDesc;
    }
    
    public String getLegalRepr() {
        return legalRepr;
    }
    
    public void setLegalRepr(String legalRepr) {
        this.legalRepr = legalRepr;
    }
    
    public String getLegalReprTel() {
        return legalReprTel;
    }
    
    public void setLegalReprTel(String legalReprTel) {
        this.legalReprTel = legalReprTel;
    }
    
    public String getLinkPsnName() {
        return linkPsnName;
    }
    
    public void setLinkPsnName(String linkPsnName) {
        this.linkPsnName = linkPsnName;
    }
    
    public String getLinkPsnTel() {
        return linkPsnTel;
    }
    
    public void setLinkPsnTel(String linkPsnTel) {
        this.linkPsnTel = linkPsnTel;
    }
    
    public String getRegFund() {
        return regFund;
    }
    
    public void setRegFund(String regFund) {
        this.regFund = regFund;
    }
    
    public String getEntAddrCode() {
        return entAddrCode;
    }
    
    public void setEntAddrCode(String entAddrCode) {
        this.entAddrCode = entAddrCode;
    }
    
    public String getEntAddr() {
        return entAddr;
    }
    
    public void setEntAddr(String entAddr) {
        this.entAddr = entAddr;
    }
    
    public String getLongitude() {
        return longitude;
    }
    
    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
    
    public String getLatitude() {
        return latitude;
    }
    
    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }
    
    public String getGeoCode() {
        return geoCode;
    }
    
    public void setGeoCode(String geoCode) {
        this.geoCode = geoCode;
    }
    
    public String getProdAddrCode() {
        return prodAddrCode;
    }
    
    public void setProdAddrCode(String prodAddrCode) {
        this.prodAddrCode = prodAddrCode;
    }
    
    public String getProdAddr() {
        return prodAddr;
    }
    
    public void setProdAddr(String prodAddr) {
        this.prodAddr = prodAddr;
    }
    
    public String getIndustryTypeCode() {
        return industryTypeCode;
    }
    
    public void setIndustryTypeCode(String industryTypeCode) {
        this.industryTypeCode = industryTypeCode;
    }
    
    public String getLineCode() {
        return lineCode;
    }
    
    public void setLineCode(String lineCode) {
        this.lineCode = lineCode;
    }
    
    public String getScaleType() {
        return scaleType;
    }
    
    public void setScaleType(String scaleType) {
        this.scaleType = scaleType;
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
    
    public EnterpriseStatus getStatus() {
        return status;
    }
    
    public void setStatus(EnterpriseStatus status) {
        this.status = status;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Enterprise enterprise = (Enterprise) o;
        return Objects.equals(entId, enterprise.entId) && 
               Objects.equals(entUnifiedCode, enterprise.entUnifiedCode);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(entId, entUnifiedCode);
    }
    
    @Override
    public String toString() {
        return "Enterprise{" +
                "entId='" + entId + '\'' +
                ", entFullName='" + entFullName + '\'' +
                ", entUnifiedCode='" + entUnifiedCode + '\'' +
                ", status=" + status +
                '}';
    }
}