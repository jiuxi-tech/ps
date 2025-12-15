package com.jiuxi.admin.core.interceptor;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.jiuxi.admin.core.bean.entity.TpApiDefinition;
import com.jiuxi.admin.core.bean.entity.TpAppApiPermission;
import com.jiuxi.admin.core.bean.vo.TpThirdPartyAppVO;
import com.jiuxi.admin.core.mapper.TpAppApiPermissionMapper;
import com.jiuxi.admin.core.service.TpApiCallLogService;
import com.jiuxi.admin.core.service.TpApiDefinitionService;
import com.jiuxi.admin.core.service.TpThirdPartyAppService;
import com.jiuxi.shared.common.exception.TopinfoRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @ClassName: ApiKeyInterceptor
 * @Description: API Key验证拦截器
 * @Author system
 * @Date 2025-01-28
 * @Copyright: Hangzhou Jiuxi Inc. All rights reserved.
 */
@Component
public class ApiKeyInterceptor implements HandlerInterceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiKeyInterceptor.class);

    private static final String API_KEY_HEADER = "Authorization";
    private static final String APP_ID_ATTRIBUTE = "appId";
    private static final String APP_NAME_ATTRIBUTE = "appName";
    private static final String APP_INFO_ATTRIBUTE = "appInfo";
    private static final String REQUEST_START_TIME = "requestStartTime";
    private static final String BUSINESS_STATUS_ATTRIBUTE = "businessStatus";
    private static final String BUSINESS_ERROR_ATTRIBUTE = "businessErrorMessage";

    @Autowired
    private TpThirdPartyAppService tpThirdPartyAppService;

    @Autowired
    private TpApiDefinitionService tpApiDefinitionService;

    @Autowired
    private TpAppApiPermissionMapper tpAppApiPermissionMapper;

    @Autowired
    private TpApiCallLogService tpApiCallLogService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 记录请求开始时间
        request.setAttribute(REQUEST_START_TIME, System.currentTimeMillis());

        try {
            // 1. 提取API Key
            String apiKey = request.getHeader(API_KEY_HEADER);
            if (StrUtil.isBlank(apiKey)) {
                sendErrorResponse(response, 401, "缺少API Key，请在请求头中添加 Authorization");
                return false;
            }

            // 2. 验证API Key
            TpThirdPartyAppVO app = null;
            try {
                app = tpThirdPartyAppService.validateApiKey(apiKey);
            } catch (TopinfoRuntimeException e) {
                // validateApiKey 内部验证失败
                sendErrorResponse(response, e.getErrcode(), e.getMessage());
                
                // 尝试查询应用信息，如果能查到说明是应用存在但状态不正常（禁用/过期）
                TpThirdPartyAppVO appInfo = tpThirdPartyAppService.getByApiKey(apiKey);
                if (appInfo != null) {
                    // 应用存在，记录真实的应用信息
                    logApiCall(appInfo.getAppId(), appInfo.getAppName(), request, e.getErrcode(), e.getMessage());
                } else {
                    // 应用不存在，记录为未知应用
                    logApiCallWithoutApp(apiKey, request, e.getErrcode(), e.getMessage());
                }
                return false;
            }
            
            if (app == null) {
                sendErrorResponse(response, 401, "无效的API Key");
                // 记录失败日志（使用apiKey作为appId）
                logApiCallWithoutApp(apiKey, request, 401, "无效的API Key");
                return false;
            }

            // 3. 检查IP白名单
            if (StrUtil.isNotBlank(app.getIpWhitelist())) {
                String clientIp = getClientIp(request);
                if (!checkIpWhitelist(clientIp, app.getIpWhitelist())) {
                    LOGGER.warn("IP不在白名单中，应用：{}，客户端IP：{}", app.getAppName(), clientIp);
                    sendErrorResponse(response, 403, "IP地址不在白名单中");
                    logApiCall(app.getAppId(), app.getAppName(), request, 403, "IP地址不在白名单中");
                    return false;
                }
            }

            // 4. 检查API权限
            String apiPath = request.getRequestURI();
            String httpMethod = request.getMethod();
            
            TpApiDefinition apiDef = tpApiDefinitionService.getByPathAndMethod(apiPath, httpMethod);
            if (apiDef != null) {
                TpAppApiPermission permission = tpAppApiPermissionMapper.checkPermission(app.getAppId(), apiDef.getApiId());
                if (permission == null) {
                    LOGGER.warn("应用无权访问此API，应用：{}，API路径：{}", app.getAppName(), apiPath);
                    sendErrorResponse(response, 403, "无权访问此API");
                    logApiCall(app.getAppId(), app.getAppName(), request, 403, "无权访问此API");
                    return false;
                }
            }

            // 5. 将应用信息存入request，供后续使用
            request.setAttribute(APP_ID_ATTRIBUTE, app.getAppId());
            request.setAttribute(APP_NAME_ATTRIBUTE, app.getAppName());
            request.setAttribute(APP_INFO_ATTRIBUTE, app);

            // 6. 更新应用最后调用时间（异步）
            tpThirdPartyAppService.updateLastCallTime(app.getAppId());

            return true;

        } catch (TopinfoRuntimeException e) {
            // 其他业务异常（不应该走到这里，因为validateApiKey已经单独处理）
            LOGGER.error("API验证出现未预期的业务异常：{}", e.getMessage());
            sendErrorResponse(response, e.getErrcode(), e.getMessage());
            return false;
        } catch (Exception e) {
            LOGGER.error("API Key验证失败", e);
            sendErrorResponse(response, 500, "服务器内部错误");
            return false;
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 记录API调用日志
        try {
            String appId = (String) request.getAttribute(APP_ID_ATTRIBUTE);
            String appName = (String) request.getAttribute(APP_NAME_ATTRIBUTE);
            
            LOGGER.info("afterCompletion 被调用，appId={}, appName={}, URI={}", appId, appName, request.getRequestURI());
            
            if (StrUtil.isNotBlank(appId)) {
                Long startTime = (Long) request.getAttribute(REQUEST_START_TIME);
                Integer responseTime = startTime != null ? (int) (System.currentTimeMillis() - startTime) : null;
                
                // 获取业务状态码（从ResponseBodyAdvice设置）
                Integer businessStatus = (Integer) request.getAttribute(BUSINESS_STATUS_ATTRIBUTE);
                
                // 优先使用业务错误信息，其次使用异常信息
                String errorMessage = (String) request.getAttribute(BUSINESS_ERROR_ATTRIBUTE);
                if (StrUtil.isBlank(errorMessage) && ex != null) {
                    errorMessage = ex.getMessage();
                }
                
                LOGGER.info("准备记录日志：businessStatus={}, errorMessage={}", businessStatus, errorMessage);
                
                // 调用日志记录方法
                tpApiCallLogService.logApiCall(appId, appName, request, businessStatus, responseTime, errorMessage);
            } else {
                LOGGER.warn("afterCompletion 中 appId 为空，无法记录日志，URI={}", request.getRequestURI());
            }
        } catch (Exception e) {
            LOGGER.error("记录API调用日志失败", e);
        }
    }

    /**
     * 发送错误响应
     */
    private void sendErrorResponse(HttpServletResponse response, int status, String message) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json;charset=UTF-8");
        
        JSONObject result = new JSONObject();
        result.put("success", false);
        result.put("code", status);
        result.put("message", message);
        
        response.getWriter().write(result.toJSONString());
    }

    /**
     * 获取客户端IP地址
     */
    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (StrUtil.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (StrUtil.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (StrUtil.isNotBlank(ip) && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        return ip;
    }

    /**
     * 检查IP是否在白名单中
     */
    private boolean checkIpWhitelist(String clientIp, String whitelist) {
        if (StrUtil.isBlank(whitelist)) {
            return true;
        }
        
        String[] ips = whitelist.split(",");
        for (String allowedIp : ips) {
            if (allowedIp.trim().equals(clientIp)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 记录API调用日志
     */
    private void logApiCall(String appId, String appName, HttpServletRequest request, Integer businessStatus, 
                           String errorMessage) {
        try {
            Long startTime = (Long) request.getAttribute(REQUEST_START_TIME);
            Integer responseTime = startTime != null ? (int) (System.currentTimeMillis() - startTime) : null;
            
            tpApiCallLogService.logApiCall(appId, appName, request, businessStatus, responseTime, errorMessage);
        } catch (Exception e) {
            LOGGER.error("记录API调用日志失败", e);
        }
    }
    
    /**
     * 记录API调用日志（无应用信息）
     */
    private void logApiCallWithoutApp(String apiKey, HttpServletRequest request, Integer businessStatus, 
                                      String errorMessage) {
        try {
            LOGGER.info("开始记录无应用信息API调用日志，apiKey={}, URI={}, businessStatus={}, errorMessage={}",
                apiKey, request.getRequestURI(), businessStatus, errorMessage);
            
            Long startTime = (Long) request.getAttribute(REQUEST_START_TIME);
            Integer responseTime = startTime != null ? (int) (System.currentTimeMillis() - startTime) : null;
            
            // 使用固定ID "UNKNOWN" 作为appId，并在appName中包含apiKey信息
            String appName = "未知应用(" + apiKey + ")";
            tpApiCallLogService.logApiCall("UNKNOWN", appName, request, businessStatus, responseTime, errorMessage);
            
            LOGGER.info("无应用信息API调用日志记录完成");
        } catch (Exception e) {
            LOGGER.error("记录API调用日志失败，apiKey={}, error={}", apiKey, e.getMessage(), e);
        }
    }
}
