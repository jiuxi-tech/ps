package com.jiuxi.admin.core.interceptor;

import com.jiuxi.admin.core.bean.entity.TpIpAccessLog;
import com.jiuxi.admin.core.service.TpIpAccessLogService;
import com.jiuxi.admin.core.util.IpAccessControlUtil;
import com.jiuxi.shared.config.cache.IpAccessConfigCache;
import com.jiuxi.common.bean.JsonResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * IP访问控制拦截器
 * 实现IP白名单和黑名单访问控制
 * 
 * @author system
 * @date 2025-01-20
 */
@Component
public class IpAccessControlInterceptor implements HandlerInterceptor {
    
    private static final Logger logger = LoggerFactory.getLogger(IpAccessControlInterceptor.class);
    
    @Autowired
    private IpAccessConfigCache ipAccessConfigCache;
    
    @Autowired
    private TpIpAccessLogService tpIpAccessLogService;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        
        // 检查IP访问控制是否启用
        if (!ipAccessConfigCache.isIpAccessControlEnabled()) {
            return true; // 未启用，直接放行
        }
        
        // 获取客户端真实IP
        String clientIp = IpAccessControlUtil.getClientRealIp(request);
        
        // 验证IP格式
        if (!IpAccessControlUtil.isValidIp(clientIp)) {
            logger.warn("无效的客户端IP地址: {}", clientIp);
            return handleAccessDenied(request, response, "无效的IP地址", clientIp);
        }
        
        // 检查访问权限
        IpAccessConfigCache.IpMatchResult matchResult = ipAccessConfigCache.getIpMatchResult(clientIp);
        AccessResult result = new AccessResult(matchResult.isAllowed(), matchResult.getReason());
        
        // 记录访问日志
        logAccess(request, clientIp, result, matchResult);
        
        // 处理访问结果
        if (!result.isAllowed()) {
            return handleAccessDenied(request, response, result.getReason(), clientIp);
        }
        
        return true;
    }
    

    
    /**
     * 处理访问被拒绝的情况
     * 
     * @param request HTTP请求
     * @param response HTTP响应
     * @param reason 拒绝原因
     * @param clientIp 客户端IP
     * @return false表示拦截请求
     */
    private boolean handleAccessDenied(HttpServletRequest request, HttpServletResponse response, 
                                      String reason, String clientIp) throws IOException {
        
        logger.warn("IP访问被拒绝: IP={}, URI={}, Reason={}", clientIp, request.getRequestURI(), reason);
        
        // 获取拒绝消息
        String denyMessage = ipAccessConfigCache.getIpDenyMessage();
        
        // 设置响应
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json;charset=UTF-8");
        
        // 构建响应数据
        JsonResponse jsonResponse = JsonResponse.buildFailure(denyMessage);
        jsonResponse.setCode(403); // HTTP 403 Forbidden
        
        // 写入响应
        response.getWriter().write(objectMapper.writeValueAsString(jsonResponse));
        response.getWriter().flush();
        
        return false;
    }
    
    /**
     * 记录访问日志
     * 
     * @param request HTTP请求
     * @param clientIp 客户端IP
     * @param result 访问结果
     * @param matchResult IP匹配结果
     */
    private void logAccess(HttpServletRequest request, String clientIp, AccessResult result, IpAccessConfigCache.IpMatchResult matchResult) {
        
        // 检查是否启用日志记录
        if (!ipAccessConfigCache.isIpAccessLogEnabled()) {
            return;
        }
        
        // 记录访问日志
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String uri = request.getRequestURI();
        String method = request.getMethod();
        String userAgent = request.getHeader("User-Agent");
        
        if (result.isAllowed()) {
            logger.info("IP访问允许: Time={}, IP={}, Method={}, URI={}, Reason={}, UserAgent={}", 
                       timestamp, clientIp, method, uri, result.getReason(), userAgent);
        } else {
            logger.warn("IP访问拒绝: Time={}, IP={}, Method={}, URI={}, Reason={}, UserAgent={}", 
                       timestamp, clientIp, method, uri, result.getReason(), userAgent);
        }
        
        // 记录到数据库
        try {
            String accessResult = result.isAllowed() ? TpIpAccessLog.ACCESS_RESULT_ALLOWED : TpIpAccessLog.ACCESS_RESULT_DENIED;
            String denyReason = result.isAllowed() ? null : getDenyReasonCode(result.getReason());
            String matchedRule = matchResult.getMatchedRule();
            String ruleType = matchResult.getRuleType();
            
            tpIpAccessLogService.logAccess(request, accessResult, denyReason, matchedRule, ruleType, null);
        } catch (Exception e) {
            logger.error("记录IP访问日志到数据库失败", e);
        }
    }
    
    /**
     * 获取拒绝原因代码
     */
    private String getDenyReasonCode(String reason) {
        if (reason != null) {
            if (reason.contains("黑名单")) {
                return TpIpAccessLog.DENY_REASON_BLACKLIST;
            } else if (reason.contains("白名单")) {
                return TpIpAccessLog.DENY_REASON_NOT_IN_WHITELIST;
            } else if (reason.contains("无效")) {
                return TpIpAccessLog.DENY_REASON_INVALID_IP;
            }
        }
        return TpIpAccessLog.DENY_REASON_OTHER;
    }

    /**
     * 访问结果内部类
     */
    private static class AccessResult {
        private final boolean allowed;
        private final String reason;
        
        public AccessResult(boolean allowed, String reason) {
            this.allowed = allowed;
            this.reason = reason;
        }
        
        public boolean isAllowed() {
            return allowed;
        }
        
        public String getReason() {
            return reason;
        }
    }
}