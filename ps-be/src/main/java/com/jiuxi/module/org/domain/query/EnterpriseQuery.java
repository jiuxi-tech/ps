package com.jiuxi.module.org.domain.query;

import com.jiuxi.module.org.domain.model.entity.EnterpriseStatus;

/**
 * 企业查询规范对象
 * 支持复杂查询条件的封装和组合
 * 
 * @author DDD Refactor
 * @date 2025-09-17
 */
public class EnterpriseQuery {
    
    /**
     * 企业ID
     */
    private String entId;
    
    /**
     * 企业全称（支持模糊查询）
     */
    private String entFullName;
    
    /**
     * 企业简称（支持模糊查询）
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
     * 法定代表人（支持模糊查询）
     */
    private String legalRepr;
    
    /**
     * 联系人姓名（支持模糊查询）
     */
    private String linkPsnName;
    
    /**
     * 联系电话
     */
    private String linkPsnTel;
    
    /**
     * 注册地址代码
     */
    private String entAddrCode;
    
    /**
     * 生产地址代码
     */
    private String prodAddrCode;
    
    /**
     * 行业类别代码
     */
    private String industryTypeCode;
    
    /**
     * 条线代码
     */
    private String lineCode;
    
    /**
     * 企业规模
     */
    private String scaleType;
    
    /**
     * 企业状态
     */
    private EnterpriseStatus status;
    
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
     * 关键词搜索（用于在企业名称、法人、联系人等字段中搜索）
     */
    private String keyword;
    
    /**
     * 地理位置范围查询
     */
    private String minLongitude;
    private String maxLongitude;
    private String minLatitude;
    private String maxLatitude;
    
    /**
     * 注册资金范围
     */
    private String minRegFund;
    private String maxRegFund;
    
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
    public EnterpriseQuery() {
    }
    
    public EnterpriseQuery(String tenantId) {
        this.tenantId = tenantId;
    }
    
    // 链式设置方法
    public EnterpriseQuery entId(String entId) {
        this.entId = entId;
        return this;
    }
    
    public EnterpriseQuery entFullName(String entFullName) {
        this.entFullName = entFullName;
        return this;
    }
    
    public EnterpriseQuery entSimpleName(String entSimpleName) {
        this.entSimpleName = entSimpleName;
        return this;
    }
    
    public EnterpriseQuery entUnifiedCode(String entUnifiedCode) {
        this.entUnifiedCode = entUnifiedCode;
        return this;
    }
    
    public EnterpriseQuery entType(String entType) {
        this.entType = entType;
        return this;
    }
    
    public EnterpriseQuery legalRepr(String legalRepr) {
        this.legalRepr = legalRepr;
        return this;
    }
    
    public EnterpriseQuery linkPsnName(String linkPsnName) {
        this.linkPsnName = linkPsnName;
        return this;
    }
    
    public EnterpriseQuery linkPsnTel(String linkPsnTel) {
        this.linkPsnTel = linkPsnTel;
        return this;
    }
    
    public EnterpriseQuery entAddrCode(String entAddrCode) {
        this.entAddrCode = entAddrCode;
        return this;
    }
    
    public EnterpriseQuery prodAddrCode(String prodAddrCode) {
        this.prodAddrCode = prodAddrCode;
        return this;
    }
    
    public EnterpriseQuery industryTypeCode(String industryTypeCode) {
        this.industryTypeCode = industryTypeCode;
        return this;
    }
    
    public EnterpriseQuery lineCode(String lineCode) {
        this.lineCode = lineCode;
        return this;
    }
    
    public EnterpriseQuery scaleType(String scaleType) {
        this.scaleType = scaleType;
        return this;
    }
    
    public EnterpriseQuery status(EnterpriseStatus status) {
        this.status = status;
        return this;
    }
    
    public EnterpriseQuery enabled(Integer enabled) {
        this.enabled = enabled;
        return this;
    }
    
    public EnterpriseQuery actived(Integer actived) {
        this.actived = actived;
        return this;
    }
    
    public EnterpriseQuery tenantId(String tenantId) {
        this.tenantId = tenantId;
        return this;
    }
    
    public EnterpriseQuery keyword(String keyword) {
        this.keyword = keyword;
        return this;
    }
    
    public EnterpriseQuery locationRange(String minLongitude, String maxLongitude,
                                       String minLatitude, String maxLatitude) {
        this.minLongitude = minLongitude;
        this.maxLongitude = maxLongitude;
        this.minLatitude = minLatitude;
        this.maxLatitude = maxLatitude;
        return this;
    }
    
    public EnterpriseQuery regFundRange(String minRegFund, String maxRegFund) {
        this.minRegFund = minRegFund;
        this.maxRegFund = maxRegFund;
        return this;
    }
    
    public EnterpriseQuery page(Integer pageNum, Integer pageSize) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        return this;
    }
    
    public EnterpriseQuery orderBy(String orderBy, String orderDirection) {
        this.orderBy = orderBy;
        this.orderDirection = orderDirection;
        return this;
    }
    
    // Getter and Setter methods
    public String getEntId() { return entId; }
    public void setEntId(String entId) { this.entId = entId; }
    
    public String getEntFullName() { return entFullName; }
    public void setEntFullName(String entFullName) { this.entFullName = entFullName; }
    
    public String getEntSimpleName() { return entSimpleName; }
    public void setEntSimpleName(String entSimpleName) { this.entSimpleName = entSimpleName; }
    
    public String getEntUnifiedCode() { return entUnifiedCode; }
    public void setEntUnifiedCode(String entUnifiedCode) { this.entUnifiedCode = entUnifiedCode; }
    
    public String getEntType() { return entType; }
    public void setEntType(String entType) { this.entType = entType; }
    
    public String getLegalRepr() { return legalRepr; }
    public void setLegalRepr(String legalRepr) { this.legalRepr = legalRepr; }
    
    public String getLinkPsnName() { return linkPsnName; }
    public void setLinkPsnName(String linkPsnName) { this.linkPsnName = linkPsnName; }
    
    public String getLinkPsnTel() { return linkPsnTel; }
    public void setLinkPsnTel(String linkPsnTel) { this.linkPsnTel = linkPsnTel; }
    
    public String getEntAddrCode() { return entAddrCode; }
    public void setEntAddrCode(String entAddrCode) { this.entAddrCode = entAddrCode; }
    
    public String getProdAddrCode() { return prodAddrCode; }
    public void setProdAddrCode(String prodAddrCode) { this.prodAddrCode = prodAddrCode; }
    
    public String getIndustryTypeCode() { return industryTypeCode; }
    public void setIndustryTypeCode(String industryTypeCode) { this.industryTypeCode = industryTypeCode; }
    
    public String getLineCode() { return lineCode; }
    public void setLineCode(String lineCode) { this.lineCode = lineCode; }
    
    public String getScaleType() { return scaleType; }
    public void setScaleType(String scaleType) { this.scaleType = scaleType; }
    
    public EnterpriseStatus getStatus() { return status; }
    public void setStatus(EnterpriseStatus status) { this.status = status; }
    
    public Integer getEnabled() { return enabled; }
    public void setEnabled(Integer enabled) { this.enabled = enabled; }
    
    public Integer getActived() { return actived; }
    public void setActived(Integer actived) { this.actived = actived; }
    
    public String getTenantId() { return tenantId; }
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }
    
    public String getKeyword() { return keyword; }
    public void setKeyword(String keyword) { this.keyword = keyword; }
    
    public String getMinLongitude() { return minLongitude; }
    public void setMinLongitude(String minLongitude) { this.minLongitude = minLongitude; }
    
    public String getMaxLongitude() { return maxLongitude; }
    public void setMaxLongitude(String maxLongitude) { this.maxLongitude = maxLongitude; }
    
    public String getMinLatitude() { return minLatitude; }
    public void setMinLatitude(String minLatitude) { this.minLatitude = minLatitude; }
    
    public String getMaxLatitude() { return maxLatitude; }
    public void setMaxLatitude(String maxLatitude) { this.maxLatitude = maxLatitude; }
    
    public String getMinRegFund() { return minRegFund; }
    public void setMinRegFund(String minRegFund) { this.minRegFund = minRegFund; }
    
    public String getMaxRegFund() { return maxRegFund; }
    public void setMaxRegFund(String maxRegFund) { this.maxRegFund = maxRegFund; }
    
    public Integer getPageNum() { return pageNum; }
    public void setPageNum(Integer pageNum) { this.pageNum = pageNum; }
    
    public Integer getPageSize() { return pageSize; }
    public void setPageSize(Integer pageSize) { this.pageSize = pageSize; }
    
    public String getOrderBy() { return orderBy; }
    public void setOrderBy(String orderBy) { this.orderBy = orderBy; }
    
    public String getOrderDirection() { return orderDirection; }
    public void setOrderDirection(String orderDirection) { this.orderDirection = orderDirection; }
}