package com.jiuxi.monitor.server.core.bean.vo;

import java.math.BigDecimal;
import java.io.Serializable;

/**
 * 监控配置
 * 
 * @author yangzr
 * @email 373416873@qq.com
 * @date 2024-11-20 10:36:59
 */
public class TpMonitorConfigVO implements Serializable {

	private static final long serialVersionUID = 1L;


	/**
	 * 主键
	 */
	private String configId;

	/**
	 * cpu报警阈值
	 */
	private BigDecimal cpuThreshold;

	/**
	 * 内存报警阈值
	 */
	private BigDecimal memoryThreshold;

	/**
	 * 磁盘报警阈值
	 */
	private BigDecimal diskThreshold;

	/**
	 * 是否发送邮件（1：是，0：否）
	 */
	private Integer sendMail;

	/**
	 * 系统负责人（多个逗号分隔）
	 */
	private String principal;

	/**
	 * 负责人手机号
	 */
	private String mobile;

	/**
	 * 负责人邮箱（多个逗号分隔）
	 */
	private String email;

	/**
	 * 离线报警阈值（分钟）
	 */
	private Integer offlineThreshold;

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


	public String getConfigId() {
		return configId;
	}

	public void setConfigId(String configId) {
		this.configId = configId;
	}


	public BigDecimal getCpuThreshold() {
		return cpuThreshold;
	}

	public void setCpuThreshold(BigDecimal cpuThreshold) {
		this.cpuThreshold = cpuThreshold;
	}


	public BigDecimal getMemoryThreshold() {
		return memoryThreshold;
	}

	public void setMemoryThreshold(BigDecimal memoryThreshold) {
		this.memoryThreshold = memoryThreshold;
	}


	public BigDecimal getDiskThreshold() {
		return diskThreshold;
	}

	public void setDiskThreshold(BigDecimal diskThreshold) {
		this.diskThreshold = diskThreshold;
	}


	public Integer getSendMail() {
		return sendMail;
	}

	public void setSendMail(Integer sendMail) {
		this.sendMail = sendMail;
	}


	public String getPrincipal() {
		return principal;
	}

	public void setPrincipal(String principal) {
		this.principal = principal;
	}


	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}


	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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

	public Integer getOfflineThreshold() {
		return offlineThreshold;
	}

	public void setOfflineThreshold(Integer offlineThreshold) {
		this.offlineThreshold = offlineThreshold;
	}

	/**
	 * 获取邮件接收人列表
	 * @return 邮件接收人
	 */
	public String getMailTo() {
		return email;
	}
}
