package com.jiuxi.admin.core.bean.query;

import java.io.Serializable;


/**
 * 
 *
 * @author yangp
 * @email 
 * @date 2021-03-24 16:04:29
 */
public class TpMessageQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 消息标题
     */
    private String msgTitle;
    
    /**
     * 消息类型，o2o/o2e/e2e
     */
    private String msgType;

	/**
	 * 创建人
	 */
	private String creator;
    
    /**
     * 当前页
     */
    private Integer current;

    /**
     * 每页记录数
     */
    private Integer size;


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

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
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
