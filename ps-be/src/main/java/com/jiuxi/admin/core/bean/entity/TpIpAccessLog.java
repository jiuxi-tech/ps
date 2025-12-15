package com.jiuxi.admin.core.bean.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName: TpIpAccessLog
 * @Description: IP访问控制日志表实体类
 * @Author: Qdd
 * @Date: 2024-01-27
 * @Copyright: www.jiuxi.com Inc. All rights reserved.
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("tp_ip_access_log")
public class TpIpAccessLog implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 日志ID
     */
    @TableId(value = "LOG_ID", type = IdType.ASSIGN_ID)
    private String logId;

    /**
     * 客户端IP地址
     */
    @TableField("CLIENT_IP")
    private String clientIp;

    /**
     * 访问时间
     */
    @TableField("ACCESS_TIME")
    private Date accessTime;

    /**
     * 访问结果(ALLOWED:允许, DENIED:拒绝)
     */
    @TableField("ACCESS_RESULT")
    private String accessResult;

    /**
     * 拒绝原因(BLACKLIST:黑名单, NOT_IN_WHITELIST:不在白名单)
     */
    @TableField("DENY_REASON")
    private String denyReason;

    /**
     * 请求URI
     */
    @TableField("REQUEST_URI")
    private String requestUri;

    /**
     * 请求方法(GET,POST等)
     */
    @TableField("REQUEST_METHOD")
    private String requestMethod;

    /**
     * 用户代理信息
     */
    @TableField("USER_AGENT")
    private String userAgent;

    /**
     * 用户名(如果已登录)
     */
    @TableField("USERNAME")
    private String username;

    /**
     * 匹配的规则(具体的IP规则)
     */
    @TableField("MATCHED_RULE")
    private String matchedRule;

    /**
     * 规则类型(WHITELIST:白名单, BLACKLIST:黑名单)
     */
    @TableField("RULE_TYPE")
    private String ruleType;

    /**
     * 城市代码
     */
    @TableField("CITY_CODE")
    private String cityCode;

    /**
     * 是否激活(1:激活 0:未激活)
     */
    @TableField("ACTIVED")
    private String actived;

    /**
     * 创建时间
     */
    @TableField("CREATE_TIME")
    private Date createTime;

    /**
     * 扩展字段1
     */
    @TableField("EXTEND01")
    private String extend01;

    /**
     * 扩展字段2
     */
    @TableField("EXTEND02")
    private String extend02;

    /**
     * 扩展字段3
     */
    @TableField("EXTEND03")
    private String extend03;

    // 访问结果常量
    public static final String ACCESS_RESULT_ALLOWED = "ALLOWED";
    public static final String ACCESS_RESULT_DENIED = "DENIED";

    // 拒绝原因常量
    public static final String DENY_REASON_BLACKLIST = "BLACKLIST";
    public static final String DENY_REASON_NOT_IN_WHITELIST = "NOT_IN_WHITELIST";
    public static final String DENY_REASON_INVALID_IP = "INVALID_IP";
    public static final String DENY_REASON_OTHER = "OTHER";

    // 规则类型常量
    public static final String RULE_TYPE_WHITELIST = "WHITELIST";
    public static final String RULE_TYPE_BLACKLIST = "BLACKLIST";

    // 激活状态常量
    public static final String ACTIVED_YES = "1";
    public static final String ACTIVED_NO = "0";
}