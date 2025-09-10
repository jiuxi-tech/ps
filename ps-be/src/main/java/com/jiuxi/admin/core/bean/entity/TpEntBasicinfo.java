package com.jiuxi.admin.core.bean.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

/**
 * 企业基本信息表
 * 
 * @author pand
 * @email 2521143047@qq.com
 * @date 2020-11-18 11:05:18
 */
@TableName("tp_ent_basicinfo")
public class TpEntBasicinfo implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 企业id
	 */
	@TableId
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
	 * 行业类别代码 字典码：SYS16字典值，各个行业条线，即部门类别，如司法条线
	 */
	private String industryTypeCode;
	/**
	 * 条线code
	 */
	private String lineCode;
	/**
	 * 企业规模 字典编码：C36
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
	 * 租户id
	 */
	private String tenantId;

	/**
	 * 创建人
	 */
	private String creator;
	/**
	 * 创建时间
	 */
	private String createTime;
	/**
	 * 修改人
	 */
	private String updator;
	/**
	 * 修改时间
	 */
	private String updateTime;

	/**
	 * 扩展字段01
	 */
	private String extend01;
	/**
	 * 扩展字段02
	 */
	private String extend02;
	/**
	 * 扩展字段03
	 */
	private String extend03;

	/**
	 * 扩展字段04
	 */
	private String extend04;

	/**
	 * 扩展字段05
	 */
	private String extend05;

	/**
	 * 代码证类型
	 */
	private String certCodeType;

	/**
	 * 安全管理负责人姓名
	 */
	private String safetyDirectorName;
	/**
	 * 安全管理负责人联系方式
	 */
	private String safetyDirectorTel;


	/**
	 * 企业领域（业务字段，字典编码：HUHY）
	 */
	private String entDomain;

	/**
	 * 企业标签（业务字段，字典编码：JP7）
	 */
	private String entLabel;

	/**
	 * 营业状态（业务字段，字典编码：E08）
	 */
	private String stateCode;

	/**
	 * 生产经营方式（业务字段，字典编码：E30）
	 */
	private String productionMode;

	/**
	 * 行政隶属关系（业务字段，字典编码：E06）
	 */
	private String administrativeSubOrdination;

	/**
	 * 国民经济类型（业务字段，字典编码：HYZ）
	 */
	private String registrationType;

	/**
	 * 主营业务（业务字段）
	 */
	private String opScope;

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

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getUpdator() {
		return updator;
	}

	public void setUpdator(String updator) {
		this.updator = updator;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public String getExtend01() {
		return extend01;
	}

	public void setExtend01(String extend01) {
		this.extend01 = extend01;
	}

	public String getExtend02() {
		return extend02;
	}

	public void setExtend02(String extend02) {
		this.extend02 = extend02;
	}

	public String getExtend03() {
		return extend03;
	}

	public void setExtend03(String extend03) {
		this.extend03 = extend03;
	}

	public String getCertCodeType() {
		return certCodeType;
	}

	public void setCertCodeType(String certCodeType) {
		this.certCodeType = certCodeType;
	}

	public String getSafetyDirectorName() {
		return safetyDirectorName;
	}

	public void setSafetyDirectorName(String safetyDirectorName) {
		this.safetyDirectorName = safetyDirectorName;
	}

	public String getSafetyDirectorTel() {
		return safetyDirectorTel;
	}

	public void setSafetyDirectorTel(String safetyDirectorTel) {
		this.safetyDirectorTel = safetyDirectorTel;
	}

	public String getExtend04() {
		return extend04;
	}

	public void setExtend04(String extend04) {
		this.extend04 = extend04;
	}

	public String getExtend05() {
		return extend05;
	}

	public void setExtend05(String extend05) {
		this.extend05 = extend05;
	}

	public String getEntDomain() {
		return entDomain;
	}

	public void setEntDomain(String entDomain) {
		this.entDomain = entDomain;
	}

	public String getEntLabel() {
		return entLabel;
	}

	public void setEntLabel(String entLabel) {
		this.entLabel = entLabel;
	}

	public String getStateCode() {
		return stateCode;
	}

	public void setStateCode(String stateCode) {
		this.stateCode = stateCode;
	}

	public String getProductionMode() {
		return productionMode;
	}

	public void setProductionMode(String productionMode) {
		this.productionMode = productionMode;
	}

	public String getAdministrativeSubOrdination() {
		return administrativeSubOrdination;
	}

	public void setAdministrativeSubOrdination(String administrativeSubOrdination) {
		this.administrativeSubOrdination = administrativeSubOrdination;
	}

	public String getRegistrationType() {
		return registrationType;
	}

	public void setRegistrationType(String registrationType) {
		this.registrationType = registrationType;
	}

	public String getOpScope() {
		return opScope;
	}

	public void setOpScope(String opScope) {
		this.opScope = opScope;
	}
}
