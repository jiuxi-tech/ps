package com.jiuxi.core.core.controller;

import com.jiuxi.common.bean.JsonResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName: StationlineController
 * @Description: 站点在线状态管理
 * @Author System
 * @Date 2025-01-20
 * @Copyright: www.jiuxi.com Inc. All rights reserved.
 */
@RestController
@RequestMapping("/platform/stationline")
public class StationlineController {

    /**
     * 心跳检测接口
     * 用于前端定期发送心跳请求，维持会话状态
     *
     * @param jt token参数
     * @param request HTTP请求对象
     * @return 心跳响应
     */
    @RequestMapping("/heartbeat")
    public JsonResponse heartbeat(@RequestParam(value = "jt", required = false) String jt, 
                                 HttpServletRequest request) {
        try {
            // 记录心跳时间
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            
            // 获取客户端IP
            String clientIp = getClientIpAddress(request);
            
            // 构建心跳响应数据
            Map<String, Object> data = new HashMap<>();
            data.put("timestamp", timestamp);
            data.put("clientIp", clientIp);
            data.put("status", "online");
            data.put("message", "心跳正常");
            
            return JsonResponse.buildSuccess(data);
        } catch (Exception e) {
            return JsonResponse.buildFailure("心跳检测失败: " + e.getMessage());
        }
    }

    /**
     * 获取客户端真实IP地址
     *
     * @param request HTTP请求对象
     * @return 客户端IP地址
     */
    private String getClientIpAddress(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty() && !"unknown".equalsIgnoreCase(xForwardedFor)) {
            return xForwardedFor.split(",")[0].trim();
        }
        
        String xRealIp = request.getHeader("X-Real-IP");
        if (xRealIp != null && !xRealIp.isEmpty() && !"unknown".equalsIgnoreCase(xRealIp)) {
            return xRealIp;
        }
        
        String proxyClientIp = request.getHeader("Proxy-Client-IP");
        if (proxyClientIp != null && !proxyClientIp.isEmpty() && !"unknown".equalsIgnoreCase(proxyClientIp)) {
            return proxyClientIp;
        }
        
        String wlProxyClientIp = request.getHeader("WL-Proxy-Client-IP");
        if (wlProxyClientIp != null && !wlProxyClientIp.isEmpty() && !"unknown".equalsIgnoreCase(wlProxyClientIp)) {
            return wlProxyClientIp;
        }
        
        return request.getRemoteAddr();
    }
}