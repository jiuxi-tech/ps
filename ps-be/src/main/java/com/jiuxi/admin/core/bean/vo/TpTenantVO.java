package com.jiuxi.admin.core.bean.vo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

/**
 * 租户表
 * 
 * @author pand
 * @email 2521143047@qq.com
 * @date 2020-12-01 14:26:53
 */
@TableName("tp_tenant")
public class TpTenantVO implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 租户id
	 */
	@TableId
	private String tenantId;
	/**
	 * 租户名称
	 */
	private String tenantName;
	/**
	 * 是否启用
	 */
	private Integer enabled;
	/**
	 * 是否有效
	 */
	private Integer actived;
	/**
	 * 创建时间
	 */
	private String createTime;
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

}
