package com.jiuxi.admin.core.bean.vo;

import java.io.Serializable;

/**
 * 富文本内容
 * 
 * @author pand
 * @email 
 * @date 2021-04-27 14:29:12
 */
public class TpRichtextVO implements Serializable {

	private static final long serialVersionUID = 1L;


	/**
	 * 富文本内容id
	 */
	private String txtId;

	/**
	 * 类型
	 */
	private String txtType;

	/**
	 * 业务id
	 */
	private String referId;

	/**
	 * 富文本内容
	 */
	private String content;


	public String getTxtId() {
		return txtId;
	}

	public void setTxtId(String txtId) {
		this.txtId = txtId;
	}


	public String getTxtType() {
		return txtType;
	}

	public void setTxtType(String txtType) {
		this.txtType = txtType;
	}


	public String getReferId() {
		return referId;
	}

	public void setReferId(String referId) {
		this.referId = referId;
	}


	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
