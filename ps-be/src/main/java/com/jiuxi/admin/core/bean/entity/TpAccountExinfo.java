package com.jiuxi.admin.core.bean.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

/**
 * 账号拓展表
 * 
 * @author pand
 * @email 2521143047@qq.com
 * @date 2020-11-18 11:05:18
 */
@TableName("tp_account_exinfo")
public class TpAccountExinfo implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 账号id
	 */
	@TableId
	private String accountId;
	/**
	 * 密码错误次数记录
	 */
	private Integer errCount;
	/**
	 * 最后一次密码错误时间
	 */
	private String lastErrTime;
	/**
	 * 最后一次登陆时间
	 */
	private String lastLoginTime;
	/**
	 * 租户id
	 */
	private String tenantId;
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

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public Integer getErrCount() {
		return errCount;
	}

	public void setErrCount(Integer errCount) {
		this.errCount = errCount;
	}

	public String getLastErrTime() {
		return lastErrTime;
	}

	public void setLastErrTime(String lastErrTime) {
		this.lastErrTime = lastErrTime;
	}

	public String getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(String lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
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
