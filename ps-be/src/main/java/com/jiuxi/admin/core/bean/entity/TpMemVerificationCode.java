package com.jiuxi.admin.core.bean.entity;

import java.io.Serializable;

/**
 * 
 * 
 * @author pand
 * @email 
 * @date 2021-04-22 15:23:29
 */
public class TpMemVerificationCode implements Serializable {

	private static final long serialVersionUID = 1L;


	/**
	 * 主键
	 */
	private String codeId;

	/**
	 * 业务类型,同一个手机号可能针对不同的业务发送多个验证码
	 */
	private String busType;

	/**
	 * 验证码,最多支持20位
	 */
	private String verificationCode;

	/**
	 * 验证码发送的时间戳
	 */
	private String sendTimeStamp;

	/**
	 * 手机号
	 */
	private String phone;

	/**
	 * 创建时间
	 */
	private String createTime;


	public String getCodeId() {
		return codeId;
	}

	public void setCodeId(String codeId) {
		this.codeId = codeId;
	}


	public String getBusType() {
		return busType;
	}

	public void setBusType(String busType) {
		this.busType = busType;
	}


	public String getVerificationCode() {
		return verificationCode;
	}

	public void setVerificationCode(String verificationCode) {
		this.verificationCode = verificationCode;
	}


	public String getSendTimeStamp() {
		return sendTimeStamp;
	}

	public void setSendTimeStamp(String sendTimeStamp) {
		this.sendTimeStamp = sendTimeStamp;
	}


	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}


	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}



}
