package com.jiuxi.admin.core.bean.entity;

import java.io.Serializable;

/**
 * 
 * 
 * @author yangp
 * @email 
 * @date 2021-03-24 16:04:29
 */
public class TpMessage implements Serializable {

	private static final long serialVersionUID = 1L;


	/**
	 * 消息id
	 */
	private String msgId;

	/**
	 * 消息标题
	 */
	private String msgTitle;

	/**
	 * 消息类型，o2o/o2e/e2e
	 */
	private String msgType;

	/**
	 * 消息范围，即单发还是群发（all/行业/本级/个人/本级及下级单位）
	 */
	private String msgScope;

	/**
	 * 行业或条线
	 */
	private String msgGroup;

	/**
	 * 发件人
	 */
	private String msgFrom;

	/**
	 * 发件人名称
	 */
	private String msgFromName;

	/**
	 * 消息收件人
	 */
	private String msgTo;

	/**
	 * 消息收件人名称
	 */
	private String msgToName;

	/**
	 * 层级code
	 */
	private String levelcode;

	/**
	 * 消息内容
	 */
	private String msgContext;

	/**
	 * 是否有效
	 */
	private Integer actived;

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
	 * 创建时间
	 */
	private String createTime;

	/**
	 * 创建人
	 */
	private String creator;

	/**
	 * 修改时间
	 */
	private String updateTime;

	/**
	 * 修改人
	 */
	private String updator;


	public String getMsgId() {
		return msgId;
	}

	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}


	public String getMsgTitle() {
		return msgTitle;
	}

	public void setMsgTitle(String msgTitle) {
		this.msgTitle = msgTitle;
	}


	public String getMsgType() {
		return msgType;
	}

	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}


	public String getMsgScope() {
		return msgScope;
	}

	public void setMsgScope(String msgScope) {
		this.msgScope = msgScope;
	}


	public String getMsgGroup() {
		return msgGroup;
	}

	public void setMsgGroup(String msgGroup) {
		this.msgGroup = msgGroup;
	}


	public String getMsgFrom() {
		return msgFrom;
	}

	public void setMsgFrom(String msgFrom) {
		this.msgFrom = msgFrom;
	}


	public String getMsgFromName() {
		return msgFromName;
	}

	public void setMsgFromName(String msgFromName) {
		this.msgFromName = msgFromName;
	}


	public String getMsgTo() {
		return msgTo;
	}

	public void setMsgTo(String msgTo) {
		this.msgTo = msgTo;
	}


	public String getMsgToName() {
		return msgToName;
	}

	public void setMsgToName(String msgToName) {
		this.msgToName = msgToName;
	}


	public String getLevelcode() {
		return levelcode;
	}

	public void setLevelcode(String levelcode) {
		this.levelcode = levelcode;
	}


	public String getMsgContext() {
		return msgContext;
	}

	public void setMsgContext(String msgContext) {
		this.msgContext = msgContext;
	}

	public Integer getActived() {
		return actived;
	}

	public void setActived(Integer actived) {
		this.actived = actived;
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


	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}


	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}


	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}


	public String getUpdator() {
		return updator;
	}

	public void setUpdator(String updator) {
		this.updator = updator;
	}



}
