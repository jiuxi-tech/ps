package com.jiuxi.admin.core.bean.query;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @ClassName: TpIpAccessLogQuery
 * @Description: IP访问控制日志表查询对象
 * @Author: System
 * @Date: 2024-01-27
 * @Copyright: www.jiuxi.com Inc. All rights reserved.
 */
@Data
@Accessors(chain = true)
public class TpIpAccessLogQuery {

    /**
     * 日志ID
     */
    private String logId;

    /**
     * 客户端IP地址
     */
    private String clientIp;

    /**
     * 访问时间开始
     */
    @DateTimeFormat(pattern = "yyyyMMddHHmmss")
    private Date accessTimeStart;

    /**
     * 访问时间结束
     */
    @DateTimeFormat(pattern = "yyyyMMddHHmmss")
    private Date accessTimeEnd;

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
     * 用户名(如果已登录)
     */
    private String username;

    /**
     * 规则类型(WHITELIST:白名单, BLACKLIST:黑名单)
     */
    private String ruleType;

    /**
     * 城市代码
     */
    private String cityCode;

    /**
     * 是否激活
     */
    private String actived;

    /**
     * 当前页
     */
    private Integer current = 1;

    /**
     * 每页大小
     */
    private Integer size = 10;

    /**
     * 排序字段
     */
    private String orderBy = "ACCESS_TIME";

    /**
     * 排序方向(ASC:升序, DESC:降序)
     */
    private String orderDirection = "DESC";

    /**
     * 创建时间开始
     */
    @DateTimeFormat(pattern = "yyyyMMddHHmmss")
    private Date createTimeStart;

    /**
     * 创建时间结束
     */
    @DateTimeFormat(pattern = "yyyyMMddHHmmss")
    private Date createTimeEnd;

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
}