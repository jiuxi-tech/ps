package com.jiuxi.admin.core.bean.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

/**
 * 参数配置表
 *
 * @author pand
 * @email 2521143047@qq.com
 * @date 2020-11-18 11:05:18
 */
@TableName("tp_parameter_config")
public class TpParameterConfig implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 参数配置
	 */
	@TableId
	private String pmId;
	/**
	 * 参数key
	 */
	private String pmKey;
	/**
	 * 参数名称
	 */
	private String pmName;
	/**
	 * 参数值
	 */
	private String pmVal;
	/**
	 * 参数配置描述
	 */
	private String pmDesc;
	/**
	 * 字典排序
	 */
	private Double orderIndex;
	/**
	 * 是否启用
	 */
	private Integer enabled;
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

	public String getPmId() {
		return pmId;
	}

	public void setPmId(String pmId) {
		this.pmId = pmId;
	}

	public String getPmKey() {
		return pmKey;
	}

	public void setPmKey(String pmKey) {
		this.pmKey = pmKey;
	}

	public String getPmName() {
		return pmName;
	}

	public void setPmName(String pmName) {
		this.pmName = pmName;
	}

	public String getPmVal() {
		return pmVal;
	}

	public void setPmVal(String pmVal) {
		this.pmVal = pmVal;
	}

	public String getPmDesc() {
		return pmDesc;
	}

	public void setPmDesc(String pmDesc) {
		this.pmDesc = pmDesc;
	}

	public Double getOrderIndex() {
		return orderIndex;
	}

	public void setOrderIndex(Double orderIndex) {
		this.orderIndex = orderIndex;
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
