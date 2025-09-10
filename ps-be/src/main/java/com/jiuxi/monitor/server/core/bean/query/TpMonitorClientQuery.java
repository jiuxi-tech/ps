package com.jiuxi.monitor.server.core.bean.query;


import java.io.Serializable;


/**
 * 客户端基本信息
 *
 * @author yangzr
 * @email 373416873@qq.com
 * @date 2024-11-18 16:30:42
 */
public class TpMonitorClientQuery implements Serializable {
    private static final long serialVersionUID = 1L;

    
    /**
     * 系统名称
     */
    private String applicationName;
    
    /**
     * mac地址
     */
    private String macAddr;

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
     * 状态（1：在线  0：离线）
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

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
}
