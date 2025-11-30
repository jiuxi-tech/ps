package com.jiuxi.admin.core.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jiuxi.admin.core.bean.query.TpApiCallLogQuery;
import com.jiuxi.admin.core.bean.vo.TpApiCallLogVO;

/**
 * API调用日志Service接口
 *
 * @author system
 * @date 2025-11-30
 */
public interface TpApiCallLogService {

    /**
     * 分页查询API调用日志列表
     *
     * @param query 查询条件
     * @param tenantId 租户ID
     * @return 分页结果
     */
    IPage<TpApiCallLogVO> queryPage(TpApiCallLogQuery query, String tenantId);

    /**
     * 查看API调用日志详情
     *
     * @param logId 日志ID
     * @return 日志详情
     */
    TpApiCallLogVO view(String logId);

    /**
     * 记录API调用日志
     *
     * @param appId 应用ID（如果apiKey不存在，传"UNKNOWN"）
     * @param appName 应用名称（如果apiKey不存在，传"未知应用"）
     * @param request HTTP请求
     * @param businessStatus 业务状态码（1:成功, -1:失败, 401/403/500等）
     * @param responseTime 响应时间（毫秒）
     * @param errorMessage 错误信息
     */
    void logApiCall(String appId, String appName, javax.servlet.http.HttpServletRequest request, 
                    Integer businessStatus, Integer responseTime, String errorMessage);
}
