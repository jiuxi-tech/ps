package com.jiuxi.admin.core.bean.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @ClassName: TpIpAccessLogVO
 * @Description: IP访问控制日志表VO
 * @Author: Qdd
 * @Date: 2024-01-27
 * @Copyright: www.jiuxi.com Inc. All rights reserved.
 */
@Data
@Accessors(chain = true)
public class TpIpAccessLogVO {

    /**
     * 日志ID
     */
    private String logId;

    /**
     * 客户端IP地址
     */
    private String clientIp;

    /**
     * 访问时间
     */
    private Date accessTime;

    /**
     * 访问结果(ALLOWED:允许, DENIED:拒绝)
     */
    private String accessResult;

    /**
     * 拒绝原因(BLACKLIST:黑名单, NOT_IN_WHITELIST:不在白名单)
     */
    private String denyReason;

    /**
     * 请求URI
     */
    private String requestUri;

    /**
     * 请求方法(GET,POST等)
     */
    private String requestMethod;

    /**
     * 用户代理信息
     */
    private String userAgent;

    /**
     * 用户名(如果已登录)
     */
    private String username;

    /**
     * 匹配的规则(具体的IP规则)
     */
    private String matchedRule;

    /**
     * 规则类型(WHITELIST:白名单, BLACKLIST:黑名单)
     */
    private String ruleType;

    /**
     * 城市代码
     */
    private String cityCode;

    /**
     * 是否激活(1:激活 0:未激活)
     */
    private String actived;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 扩展字段1
     */
    private String extend01;

    /**
     * 扩展字段2
     */
    private String extend02;

    /**
     * 扩展字段3
     */
    private String extend03;

    // 访问结果显示文本
    public String getAccessResultText() {
        if ("ALLOWED".equals(accessResult)) {
            return "允许";
        } else if ("DENIED".equals(accessResult)) {
            return "拒绝";
        }
        return accessResult;
    }

    // 拒绝原因显示文本
    public String getDenyReasonText() {
        if ("BLACKLIST".equals(denyReason)) {
            return "黑名单";
        } else if ("NOT_IN_WHITELIST".equals(denyReason)) {
            return "不在白名单";
        }
        return denyReason;
    }

    // 规则类型显示文本
    public String getRuleTypeText() {
        if ("WHITELIST".equals(ruleType)) {
            return "白名单";
        } else if ("BLACKLIST".equals(ruleType)) {
            return "黑名单";
        }
        return ruleType;
    }
}