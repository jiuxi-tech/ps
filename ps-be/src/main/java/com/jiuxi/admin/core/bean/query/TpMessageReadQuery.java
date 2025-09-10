package com.jiuxi.admin.core.bean.query;

import java.io.Serializable;


/**
 * 消息、代办 已读表
 *
 * @author pand
 * @email 
 * @date 2021-05-28 15:04:39
 */
public class TpMessageReadQuery implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 人员id
     */
    private String personId;

    /**
     * 读取时间
     */
    private String readTime;
    
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
     * 当前页
     */
    private Integer current;

    /**
     * 每页记录数
     */
    private Integer size;



	public String getPersonId() {
	    return personId;
	}

	public void setPersonId(String personId) {
	    this.personId = personId;
	}


	public String getReadTime() {
	    return readTime;
	}

	public void setReadTime(String readTime) {
	    this.readTime = readTime;
	}


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

    public Integer getCurrent() {
        return current;
    }

    public void setCurrent(Integer current) {
        this.current = current;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }
}
