package com.jiuxi.admin.core.bean.vo;

import java.io.Serializable;

/**
 * 修改痕迹表
 * 
 * @author yangp
 * @email 
 * @date 2021-02-26 15:48:55
 */
public class TpTraceVO implements Serializable {

	private static final long serialVersionUID = 1L;


	/**
	 * 主键
	 */
	private String id;

	/**
	 * 模块类型
	 */
	private String moduleType;

	/**
	 * 修改的记录id
	 */
	private String recordId;

	/**
	 * 修改前
	 */
	private String updateBefore;

	/**
	 * 修改后
	 */
	private String updateAfter;

	/**
	 * 是否有效
	 */
	private Integer actived;

	/**
	 * 创建人ID
	 */
	private String creator;

	/**
	 * 创建人姓名
	 */
	private String creatorName;

	/**
	 * 创建时间
	 */
	private String createTime;


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}


	public String getModuleType() {
		return moduleType;
	}

	public void setModuleType(String moduleType) {
		this.moduleType = moduleType;
	}


	public String getRecordId() {
		return recordId;
	}

	public void setRecordId(String recordId) {
		this.recordId = recordId;
	}


	public String getUpdateBefore() {
		return updateBefore;
	}

	public void setUpdateBefore(String updateBefore) {
		this.updateBefore = updateBefore;
	}


	public String getUpdateAfter() {
		return updateAfter;
	}

	public void setUpdateAfter(String updateAfter) {
		this.updateAfter = updateAfter;
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


	public String getCreatorName() {
		return creatorName;
	}

	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}


	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

}
