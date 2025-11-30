package com.jiuxi.common.util;

import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * HTTP请求工具类
 * 用于获取请求相关信息
 *
 * @author system
 * @date 2025-12-01
 */
public class HttpRequestUtils {

    /**
     * 获取当前HTTP请求
     *
     * @return HttpServletRequest对象，如果不在Web请求上下文中则返回null
     */
    public static HttpServletRequest getCurrentRequest() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return attributes != null ? attributes.getRequest() : null;
    }

    /**
     * 获取客户端IP地址
     * 支持代理服务器情况下获取真实IP
     *
     * @param request HTTP请求对象
     * @return 客户端IP地址
     */
    public static String getClientIp(HttpServletRequest request) {
        if (request == null) {
            return null;
        }

        // 尝试从X-Forwarded-For获取（经过代理）
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (StringUtils.hasText(xForwardedFor)) {
            // X-Forwarded-For可能包含多个IP，第一个是真实客户端IP
            return xForwardedFor.split(",")[0].trim();
        }

        // 尝试从X-Real-IP获取
        String xRealIp = request.getHeader("X-Real-IP");
        if (StringUtils.hasText(xRealIp)) {
            return xRealIp;
        }

        // 尝试从Proxy-Client-IP获取
        String proxyClientIp = request.getHeader("Proxy-Client-IP");
        if (StringUtils.hasText(proxyClientIp)) {
            return proxyClientIp;
        }

        // 尝试从WL-Proxy-Client-IP获取（WebLogic）
        String wlProxyClientIp = request.getHeader("WL-Proxy-Client-IP");
        if (StringUtils.hasText(wlProxyClientIp)) {
            return wlProxyClientIp;
        }

        // 直接从RemoteAddr获取
        return request.getRemoteAddr();
    }

    /**
     * 获取当前请求的客户端IP地址
     *
     * @return 客户端IP地址，如果不在Web请求上下文中则返回null
     */
    public static String getCurrentClientIp() {
        HttpServletRequest request = getCurrentRequest();
        return getClientIp(request);
    }

    /**
     * 获取User-Agent
     *
     * @param request HTTP请求对象
     * @return User-Agent字符串
     */
    public static String getUserAgent(HttpServletRequest request) {
        if (request == null) {
            return null;
        }
        return request.getHeader("User-Agent");
    }

    /**
     * 获取当前请求的User-Agent
     *
     * @return User-Agent字符串，如果不在Web请求上下文中则返回null
     */
    public static String getCurrentUserAgent() {
        HttpServletRequest request = getCurrentRequest();
        return getUserAgent(request);
    }

    /**
     * 获取请求URI
     *
     * @param request HTTP请求对象
     * @return 请求URI
     */
    public static String getRequestUri(HttpServletRequest request) {
        if (request == null) {
            return null;
        }
        return request.getRequestURI();
    }

    /**
     * 获取当前请求的URI
     *
     * @return 请求URI，如果不在Web请求上下文中则返回null
     */
    public static String getCurrentRequestUri() {
        HttpServletRequest request = getCurrentRequest();
        return getRequestUri(request);
    }
}
