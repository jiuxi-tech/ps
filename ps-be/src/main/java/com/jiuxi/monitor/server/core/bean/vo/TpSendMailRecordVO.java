package com.jiuxi.monitor.server.core.bean.vo;

import java.io.Serializable;

/**
 * 邮件发送记录表
 * 
 * @author yangzr
 * @email 373416873@qq.com
 * @date 2024-11-20 10:36:59
 */
public class TpSendMailRecordVO implements Serializable {

	private static final long serialVersionUID = 1L;


	/**
	 * 记录id
	 */
	private String recordId;

	/**
	 * 收件人姓名
	 */
	private String personName;

	/**
	 * 电子邮箱
	 */
	private String email;

	/**
	 * 发送时间
	 */
	private String sendTime;

	/**
	 * 发送内容
	 */
	private String message;

	/**
	 * 发送状态（1:成功，0：失败）
	 */
	private Integer status;

	/**
	 * 租户id
	 */
	private String tenantId;

	/**
	 * 是否有效
	 */
	private Integer actived;

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
 	 * 单条数据密钥
     */
	private String passKey;


	public String getRecordId() {
		return recordId;
	}

	public void setRecordId(String recordId) {
		this.recordId = recordId;
	}


	public String getPersonName() {
		return personName;
	}

	public void setPersonName(String personName) {
		this.personName = personName;
	}


	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}


	public String getSendTime() {
		return sendTime;
	}

	public void setSendTime(String sendTime) {
		this.sendTime = sendTime;
	}


	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}


	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}


	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}


	public Integer getActived() {
		return actived;
	}

	public void setActived(Integer actived) {
		this.actived = actived;
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

	public String getPassKey() {
		return passKey;
	}

	public void setPassKey(String passKey) {
		this.passKey = passKey;
	}
}
