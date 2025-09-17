package com.jiuxi.module.org.infra.persistence.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDateTime;

/**
 * 企业持久化对象
 * 对应数据库表 tp_ent_basicinfo
 * 
 * @author DDD Refactor
 * @date 2025-09-18
 */
@TableName("tp_ent_basicinfo")
public class EnterprisePO {
    
    /**
     * 企业ID
     */
    @TableId
    private String entId;
    
    /**
     * 企业全称
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
}