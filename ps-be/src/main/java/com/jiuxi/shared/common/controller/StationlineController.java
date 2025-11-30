package com.jiuxi.shared.common.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.jiuxi.common.bean.JsonResponse;
import com.jiuxi.common.util.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
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

    private static final Logger logger = LoggerFactory.getLogger(StationlineController.class);

    /**
     * TOKEN刷新阈值：剩余有效期小于总有效期的20%时触发刷新
     */
    private static final double TOKEN_REFRESH_THRESHOLD = 0.2;

    /**
     * 心跳检测接口
     * 用于前端定期发送心跳请求，维持会话状态
     * 支持TOKEN自动刷新功能：当TOKEN即将过期时，自动生成新TOKEN并返回
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
            
            // TOKEN自动刷新逻辑
            if (jt != null && !jt.isEmpty()) {
                String newToken = checkAndRefreshToken(jt);
                if (newToken != null) {
                    data.put("newToken", newToken);
                    logger.info("TOKEN已自动刷新，客户端IP: {}", clientIp);
                }
            }
            
            return JsonResponse.buildSuccess(data);
        } catch (Exception e) {
            logger.error("心跳检测失败", e);
            return JsonResponse.buildFailure("心跳检测失败: " + e.getMessage());
        }
    }

    /**
     * 检查TOKEN是否需要刷新，如果需要则生成新TOKEN
     *
     * @param token 当前TOKEN
     * @return 新TOKEN，如果不需要刷新则返回null
     */
    private String checkAndRefreshToken(String token) {
        try {
            // 1. 验证TOKEN是否合法
            boolean isValid = JwtUtil.checkToken(token);
            if (!isValid) {
                logger.warn("TOKEN验证失败，不进行刷新");
                return null;
            }

            // 2. 解析TOKEN获取过期时间
            DecodedJWT decodedJWT = JWT.decode(token);
            Date expiresAt = decodedJWT.getExpiresAt();
            Date issuedAt = decodedJWT.getIssuedAt();
            
            if (expiresAt == null || issuedAt == null) {
                logger.warn("TOKEN中缺少过期时间或签发时间信息");
                return null;
            }

            // 3. 计算TOKEN剩余有效期和总有效期
            long currentTime = System.currentTimeMillis();
            long expiresAtTime = expiresAt.getTime();
            long issuedAtTime = issuedAt.getTime();
            
            long remainingTime = expiresAtTime - currentTime;
            long totalValidTime = expiresAtTime - issuedAtTime;

            // 4. 判断是否需要刷新（剩余有效期 < 总有效期的20%）
            if (remainingTime > 0 && remainingTime < totalValidTime * TOKEN_REFRESH_THRESHOLD) {
                logger.info("TOKEN即将过期，剩余时间: {}秒，开始刷新", remainingTime / 1000);
                
                // 5. 获取原TOKEN的载荷信息
                String tokenPayload = JwtUtil.getToken(token);
                if (tokenPayload == null) {
                    logger.error("无法获取TOKEN载荷信息");
                    return null;
                }

                // 6. 生成新TOKEN（保持相同的有效期）
                int timeoutMinutes = (int) (totalValidTime / (1000 * 60));
                String newToken = JwtUtil.createToken(tokenPayload, timeoutMinutes);
                
                logger.info("TOKEN刷新成功，新TOKEN有效期: {}分钟", timeoutMinutes);
                return newToken;
            } else {
                // TOKEN尚未到刷新阈值
                logger.debug("TOKEN尚未到刷新阈值，剩余时间: {}秒", remainingTime / 1000);
                return null;
            }

        } catch (Exception e) {
            logger.error("检查并刷新TOKEN时发生异常", e);
            return null;
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