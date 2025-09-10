package com.jiuxi.admin.core.bean.query;

import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;

/**
 * 租户表
 *
 * @author pand
 * @email 2521143047@qq.com
 * @date 2020-12-01 14:26:53
 */
public class TpTenantQuery implements Serializable {

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

    /**
     * 当前页
     */
    private Integer current;
    /**
     * 每页记录数
     */
    private Integer size;

}
