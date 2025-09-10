package com.jiuxi.admin.core.bean.entity;

import java.io.Serializable;

/**
 * 中介表
 * 
 * @author pand
 * @email 
 * @date 2021-05-25 17:32:41
 */
public class TpMediorg implements Serializable {

	private static final long serialVersionUID = 1L;


	/**
	 * 主键 主键id
	 */
	private String mediorgId;

	/**
	 * 中介名称（全称）
	 */
	private String mediorgFullName;

	/**
	 * 中介简称
	 */
	private String mediorgSimpleName;

	/**
	 * 机构类型
	 */
	private String mediorgType;

	/**
	 * 统一社会信用代码
	 */
	private String mediorgUnifiedCode;

	/**
	 * 所属行政区划
	 */
	private String cityCode;

	/**
	 * 注册地址
	 */
	private String regAddr;

	/**
	 * 办公地址
	 */
	private String orgAddr;

	/**
	 * 成立时间
	 */
	private String establishTime;

	/**
	 * 中介简介
	 */
	private String mediorgDesc;

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
	 * 中介坐标_经度
	 */
	private String longitude;

	/**
	 * 中介坐标_纬度
	 */
	private String latitude;

	/**
	 * 经纬度所计算的geohash码
	 */
	private String geoCode;

	/**
	 * 服务区域	
	 */
	private String serviceArea;

	/**
	 * 服务类型
	 */
	private String serviceType;

	/**
	 * 服务行业
	 */
	private String serviceIndustry;

	/**
	 * 信息公开网址
	 */
	private String informationPubWeb;

	/**
	 * 中介规模 字典编码：C36
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


	public String getMediorgId() {
		return mediorgId;
	}

	public void setMediorgId(String mediorgId) {
		this.mediorgId = mediorgId;
	}


	public String getMediorgFullName() {
		return mediorgFullName;
	}

	public void setMediorgFullName(String mediorgFullName) {
		this.mediorgFullName = mediorgFullName;
	}


	public String getMediorgSimpleName() {
		return mediorgSimpleName;
	}

	public void setMediorgSimpleName(String mediorgSimpleName) {
		this.mediorgSimpleName = mediorgSimpleName;
	}


	public String getMediorgType() {
		return mediorgType;
	}

	public void setMediorgType(String mediorgType) {
		this.mediorgType = mediorgType;
	}


	public String getMediorgUnifiedCode() {
		return mediorgUnifiedCode;
	}

	public void setMediorgUnifiedCode(String mediorgUnifiedCode) {
		this.mediorgUnifiedCode = mediorgUnifiedCode;
	}


	public String getCityCode() {
		return cityCode;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}


	public String getRegAddr() {
		return regAddr;
	}

	public void setRegAddr(String regAddr) {
		this.regAddr = regAddr;
	}


	public String getOrgAddr() {
		return orgAddr;
	}

	public void setOrgAddr(String orgAddr) {
		this.orgAddr = orgAddr;
	}


	public String getEstablishTime() {
		return establishTime;
	}

	public void setEstablishTime(String establishTime) {
		this.establishTime = establishTime;
	}


	public String getMediorgDesc() {
		return mediorgDesc;
	}

	public void setMediorgDesc(String mediorgDesc) {
		this.mediorgDesc = mediorgDesc;
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


	public String getServiceArea() {
		return serviceArea;
	}

	public void setServiceArea(String serviceArea) {
		this.serviceArea = serviceArea;
	}


	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}


	public String getServiceIndustry() {
		return serviceIndustry;
	}

	public void setServiceIndustry(String serviceIndustry) {
		this.serviceIndustry = serviceIndustry;
	}


	public String getInformationPubWeb() {
		return informationPubWeb;
	}

	public void setInformationPubWeb(String informationPubWeb) {
		this.informationPubWeb = informationPubWeb;
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



}
