package com.jiuxi.monitor.server.core.bean.vo;

import java.io.Serializable;
import java.util.List;

/**
 * 客户端基本信息
 * 
 * @author yangzr
 * @email 373416873@qq.com
 * @date 2024-11-18 16:30:42
 */
public class TpMonitorClientVO implements Serializable {

	private static final long serialVersionUID = 1L;


	/**
	 * 客户端id
	 */
	private String clientId;

	/**
	 * 系统名称
	 */
	private String applicationName;

	/**
	 * mac地址
	 */
	private String macAddr;

	/**
	 * 系统部署的绝对路径
	 */
	private String absolutePath;

	/**
	 * 系统描述
	 */
	private String systemDesc;

	/**
	 * ip链路
	 */
	private String ip;

	/**
	 * 备注
	 */
	private String remark;

	/**
	 * 系统访问地址
	 */
	private String systemUrl;

	/**
	 * 租户id
	 */
	private String tenantId;

	/**
	 * 报警信息
	 */
	private String alarmMsg;

	/**
	 * 不健康的服务
	 */
	private List<String> notHealthServer;

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
	 * 状态（1：在线  0：离线）
	 */
	private Integer status;

	/**
 	 * 单条数据密钥
     */
	private String passKey;


	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}


	public String getApplicationName() {
		return applicationName;
	}

	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}


	public String getMacAddr() {
		return macAddr;
	}

	public void setMacAddr(String macAddr) {
		this.macAddr = macAddr;
	}


	public String getAbsolutePath() {
		return absolutePath;
	}

	public void setAbsolutePath(String absolutePath) {
		this.absolutePath = absolutePath;
	}


	public String getSystemDesc() {
		return systemDesc;
	}

	public void setSystemDesc(String systemDesc) {
		this.systemDesc = systemDesc;
	}


	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}


	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}


	public String getSystemUrl() {
		return systemUrl;
	}

	public void setSystemUrl(String systemUrl) {
		this.systemUrl = systemUrl;
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

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getAlarmMsg() {
		return alarmMsg;
	}

	public void setAlarmMsg(String alarmMsg) {
		this.alarmMsg = alarmMsg;
	}

	public List<String> getNotHealthServer() {
		return notHealthServer;
	}

	public void setNotHealthServer(List<String> notHealthServer) {
		this.notHealthServer = notHealthServer;
	}
}
