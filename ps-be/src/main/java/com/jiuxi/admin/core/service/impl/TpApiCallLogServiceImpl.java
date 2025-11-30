package com.jiuxi.admin.core.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jiuxi.admin.core.bean.entity.TpApiCallLog;
import com.jiuxi.admin.core.bean.query.TpApiCallLogQuery;
import com.jiuxi.admin.core.bean.vo.TpApiCallLogVO;
import com.jiuxi.admin.core.mapper.TpApiCallLogMapper;
import com.jiuxi.admin.core.service.TpApiCallLogService;
import com.jiuxi.shared.common.exception.TopinfoRuntimeException;
import com.jiuxi.common.util.CommonDateUtil;
import com.jiuxi.common.util.SnowflakeIdUtil;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

/**
 * API调用日志Service实现
 *
 * @author system
 * @date 2025-11-30
 */
@Service("tpApiCallLogService")
public class TpApiCallLogServiceImpl implements TpApiCallLogService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TpApiCallLogServiceImpl.class);

    @Autowired
    private TpApiCallLogMapper tpApiCallLogMapper;

    /**
     * 分页查询API调用日志列表
     *
     * @param query 查询条件
     * @param tenantId 租户ID（忽略）
     * @return 分页结果
     */
    @Override
    public IPage<TpApiCallLogVO> queryPage(TpApiCallLogQuery query, String tenantId) {
        try {
            // 忽略租户ID，不设置过滤条件
            // query.setTenantId(tenantId);
            
            Integer pageNum = Optional.ofNullable(query.getCurrent()).orElse(1);
            Integer pageSize = Optional.ofNullable(query.getSize()).orElse(10);
            
            Page<TpApiCallLogVO> page = new Page<>(pageNum, pageSize);
            IPage<TpApiCallLogVO> iPage = tpApiCallLogMapper.getPage(page, query);
            return iPage;
        } catch (Exception e) {
            LOGGER.error("API调用日志列表查询失败！query:{}, 错误: {}", JSONObject.toJSONString(query), ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-1, "API调用日志列表查询失败！");
        }
    }

    /**
     * 查看API调用日志详情
     *
     * @param logId 日志ID
     * @return 日志详情
     */
    @Override
    public TpApiCallLogVO view(String logId) {
        try {
            TpApiCallLogVO vo = tpApiCallLogMapper.view(logId);
            return vo;
        } catch (Exception e) {
            LOGGER.error("查看API调用日志详情失败！logId:{}, 错误: {}", logId, ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-1, "查看API调用日志详情失败！");
        }
    }

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
    @Override
    public void logApiCall(String appId, String appName, javax.servlet.http.HttpServletRequest request,
                          Integer businessStatus, Integer responseTime, String errorMessage) {
        try {
            LOGGER.info("开始记录API调用日志，appId={}, appName={}, URI={}, method={}, businessStatus={}", 
                appId, appName, request.getRequestURI(), request.getMethod(), businessStatus);
            
            TpApiCallLog log = new TpApiCallLog();
            log.setLogId(SnowflakeIdUtil.nextIdStr());
            log.setAppId(appId);
            log.setAppName(appName);
            
            log.setApiPath(request.getRequestURI());
            log.setHttpMethod(request.getMethod());
            log.setRequestIp(getClientIp(request));
            
            // 请求参数（支持GET和POST）
            String requestParams = getRequestParams(request);
            if (requestParams != null && requestParams.length() > 500) {
                requestParams = requestParams.substring(0, 500) + "...";
            }
            log.setRequestParams(requestParams);
            
            log.setResponseStatus(businessStatus);
            log.setResponseTime(responseTime);
            log.setErrorMessage(errorMessage);
            log.setCallTime(CommonDateUtil.now());
            
            // 异步插入日志（避免影响主请求）
            // 这里简化处理，直接同步插入
            // 生产环境建议使用@Async异步处理
            LOGGER.info("准备插入日志记录，logId={}", log.getLogId());
            int rows = tpApiCallLogMapper.insert(log);
            LOGGER.info("日志记录插入完成，影响行数={}", rows);
            
        } catch (Exception e) {
            // 日志记录失败不影响主流程
            LOGGER.error("记录API调用日志失败！appId:{}, 错误: {}", appId, ExceptionUtils.getStackTrace(e));
        }
    }
    
    /**
     * 获取客户端IP地址
     */
    private String getClientIp(javax.servlet.http.HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        return ip;
    }
    
    /**
     * 获取请求参数（支持GET和POST）
     */
    private String getRequestParams(javax.servlet.http.HttpServletRequest request) {
        StringBuilder params = new StringBuilder();
        
        // 1. 获取URL参数（GET参数）
        String queryString = request.getQueryString();
        if (queryString != null && !queryString.isEmpty()) {
            params.append("?").append(queryString);
        }
        
        // 2. 获取POST请求体参数
        if ("POST".equalsIgnoreCase(request.getMethod()) || 
            "PUT".equalsIgnoreCase(request.getMethod()) ||
            "PATCH".equalsIgnoreCase(request.getMethod())) {
            try {
                // 尝试获取请求体（注意：请求体只能读取一次）
                Object bodyParam = request.getAttribute("requestBody");
                if (bodyParam != null) {
                    if (params.length() > 0) {
                        params.append(" & ");
                    }
                    params.append("Body: ").append(bodyParam.toString());
                } else {
                    // 如果没有预先存储，尝试从 parameterMap 获取
                    java.util.Map<String, String[]> paramMap = request.getParameterMap();
                    if (!paramMap.isEmpty()) {
                        if (params.length() > 0) {
                            params.append(" & ");
                        }
                        params.append("Params: ");
                        for (java.util.Map.Entry<String, String[]> entry : paramMap.entrySet()) {
                            params.append(entry.getKey()).append("=")
                                  .append(String.join(",", entry.getValue()))
                                  .append("&");
                        }
                        // 移除最后一个&
                        if (params.charAt(params.length() - 1) == '&') {
                            params.deleteCharAt(params.length() - 1);
                        }
                    }
                }
            } catch (Exception e) {
                LOGGER.warn("获取请求体参数失败", e);
            }
        }
        
        return params.length() > 0 ? params.toString() : null;
    }
}
