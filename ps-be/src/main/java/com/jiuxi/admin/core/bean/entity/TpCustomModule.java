package com.jiuxi.admin.core.bean.entity;

import java.io.Serializable;

/**
 * 自定义模块信息表 存储模块的信息，按钮信息、路由信息
 * 
 * @author pand
 * @email 
 * @date 2021-05-11 11:22:40
 */
public class TpCustomModule implements Serializable {

	private static final long serialVersionUID = 1L;


	/**
	 * 模块id
	 */
	private String mid;

	/**
	 * 模块名称
	 */
	private String mname;

	/**
	 * 模块编码 表名,自动添加前缀: tp_json_
	 */
	private String mcode;

	/**
	 * 模块简介
	 */
	private String mdesc;

	/**
	 * 有效标志
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
	 * 更新人
	 */
	private String updator;

	/**
	 * 更新时间
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


	public String getMid() {
		return mid;
	}

	public void setMid(String mid) {
		this.mid = mid;
	}


	public String getMname() {
		return mname;
	}

	public void setMname(String mname) {
		this.mname = mname;
	}


	public String getMcode() {
		return mcode;
	}

	public void setMcode(String mcode) {
		this.mcode = mcode;
	}


	public String getMdesc() {
		return mdesc;
	}

	public void setMdesc(String mdesc) {
		this.mdesc = mdesc;
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



}
