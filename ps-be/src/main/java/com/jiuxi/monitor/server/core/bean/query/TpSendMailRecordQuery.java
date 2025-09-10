package com.jiuxi.monitor.server.core.bean.query;


import java.io.Serializable;


/**
 * 邮件发送记录表
 *
 * @author yangzr
 * @email 373416873@qq.com
 * @date 2024-11-20 10:36:59
 */
public class TpSendMailRecordQuery implements Serializable {

    private static final long serialVersionUID = 1L;

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
    private String sendTimeStart;

	/**
	 * 发送时间
	 */
    private String sendTimeEnd;

    /**
     * 发送内容
     */
    private String message;
    
    /**
     * 发送状态（1:成功，0：失败）
     */
    private Integer status;

    /**
     * 当前页
     */
    private Integer current;

    /**
     * 每页记录数
     */
    private Integer size;

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

	public String getSendTimeStart() {
		return sendTimeStart;
	}

	public void setSendTimeStart(String sendTimeStart) {
		this.sendTimeStart = sendTimeStart;
	}

	public String getSendTimeEnd() {
		return sendTimeEnd;
	}

	public void setSendTimeEnd(String sendTimeEnd) {
		this.sendTimeEnd = sendTimeEnd;
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
