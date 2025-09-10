package com.jiuxi.admin.core.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jiuxi.admin.core.bean.query.TpIpAccessLogQuery;
import com.jiuxi.admin.core.bean.vo.TpIpAccessLogVO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: TpIpAccessLogService
 * @Description: IP访问控制日志表服务接口
 * @Author: System
 * @Date: 2024-01-27
 * @Copyright: www.jiuxi.com Inc. All rights reserved.
 */
public interface TpIpAccessLogService {

    /**
     * 分页查询IP访问日志
     * @param query 查询条件
     * @return 分页结果
     */
    IPage<TpIpAccessLogVO> queryPage(TpIpAccessLogQuery query);

    /**
     * 查询IP访问日志列表
     * @param query 查询条件
     * @return 日志列表
     */
    List<TpIpAccessLogVO> queryList(TpIpAccessLogQuery query);

    /**
     * 根据ID查询IP访问日志详情
     * @param logId 日志ID
     * @return 日志详情
     */
    TpIpAccessLogVO view(String logId);

    /**
     * 批量删除IP访问日志
     * @param logIds 日志ID列表
     * @return 影响行数
     */
    int deleteByLogIds(List<String> logIds);

    /**
     * 记录IP访问日志
     * @param request HTTP请求对象
     * @param accessResult 访问结果(ALLOWED:允许, DENIED:拒绝)
     * @param denyReason 拒绝原因(BLACKLIST:黑名单, NOT_IN_WHITELIST:不在白名单)
     * @param matchedRule 匹配的规则
     * @param ruleType 规则类型(WHITELIST:白名单, BLACKLIST:黑名单)
     * @param username 用户名(可选)
     */
    void logAccess(HttpServletRequest request, String accessResult, String denyReason, 
                   String matchedRule, String ruleType, String username);

    /**
     * 记录IP访问日志(详细参数)
     * @param clientIp 客户端IP
     * @param accessResult 访问结果
     * @param denyReason 拒绝原因
     * @param requestUri 请求URI
     * @param requestMethod 请求方法
     * @param userAgent 用户代理
     * @param username 用户名
     * @param matchedRule 匹配的规则
     * @param ruleType 规则类型
     * @param cityCode 城市代码
     */
    void logAccessDetail(String clientIp, String accessResult, String denyReason,
                        String requestUri, String requestMethod, String userAgent,
                        String username, String matchedRule, String ruleType, String cityCode);

    /**
     * 统计IP访问次数
     * @param query 查询条件
     * @return 统计结果
     */
    List<Map<String, Object>> statisticsIpAccess(TpIpAccessLogQuery query);

    /**
     * 统计访问结果分布
     * @param query 查询条件
     * @return 统计结果
     */
    List<Map<String, Object>> statisticsAccessResult(TpIpAccessLogQuery query);

    /**
     * 统计每日访问量
     * @param query 查询条件
     * @return 统计结果
     */
    List<Map<String, Object>> statisticsDailyAccess(TpIpAccessLogQuery query);

    /**
     * 清理过期日志
     * @param days 保留天数
     * @return 清理的记录数
     */
    int cleanExpiredLogs(int days);

    /**
     * 查询最近的访问记录
     * @param clientIp 客户端IP
     * @param limit 限制条数
     * @return 访问记录列表
     */
    List<TpIpAccessLogVO> getRecentAccessByIp(String clientIp, Integer limit);

    /**
     * 统计IP在指定时间段内的访问次数
     * @param clientIp 客户端IP
     * @param minutes 时间段(分钟)
     * @return 访问次数
     */
    Integer countRecentAccessByIp(String clientIp, Integer minutes);

    /**
     * 检查IP是否存在频繁访问
     * @param clientIp 客户端IP
     * @param maxAttempts 最大尝试次数
     * @param timeWindowMinutes 时间窗口(分钟)
     * @return 是否频繁访问
     */
    boolean isFrequentAccess(String clientIp, Integer maxAttempts, Integer timeWindowMinutes);
}