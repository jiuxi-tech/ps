package com.jiuxi.common.util;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.ServletUtil;
import cn.hutool.http.useragent.Browser;
import cn.hutool.http.useragent.UserAgent;
import cn.hutool.http.useragent.UserAgentUtil;
import org.springframework.util.StreamUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletInputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName: RequestUtil
 * @Description: 请求工具类
 * @Author: Ypp
 * @Date: 2023/10/11 16:11
 * @Copyright: 2023 Hangzhou Jiuxi Inc. All rights reserved.
 */
public class CommonRequestUtil {

    /**
     * 获取当前的 HttpServletRequest，注意：必须有一个HTTP请求正在处理，如果没有HTTP请求，那么这个方法将无法工作。
     *
     * @param
     * @return javax.servlet.http.HttpServletRequest
     * @author Ypp
     * @date 2023/10/11 16:18
     */
    public static HttpServletRequest getHttpServletRequest() {
        // 获取当前的RequestAttributes
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        // 将RequestAttributes强制转换为HttpServletRequest
        HttpServletRequest httpServletRequest = ((ServletRequestAttributes) requestAttributes).getRequest();
        return httpServletRequest;
    }

    /**
     * 获取当前的 HttpServletResponse，注意：必须有一个HTTP请求正在处理，如果没有HTTP请求，那么这个方法将无法工作。
     *
     * @param
     * @return javax.servlet.http.HttpServletResponse
     * @author Ypp
     * @date 2023/10/11 16:23
     */
    public static HttpServletResponse getHttpServletResponse() {
        // 获取当前的RequestAttributes
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        // 将RequestAttributes强制转换为HttpServletRequest
        HttpServletResponse httpServletResponse = ((ServletRequestAttributes) requestAttributes).getResponse();
        return httpServletResponse;
    }


    /**
     * 从请求中获取请求的域名以及项目名，如：http://192.168.90.77:8080/topinfo-admin，注意：必须有一个HTTP请求正在处理，如果没有HTTP请求，那么这个方法将无法工作。
     *
     * @param
     * @return java.lang.String
     * @author Ypp
     * @date 2023/10/11 16:20
     */
    public static String getBasicPath() {
        HttpServletRequest httpServletRequest = getHttpServletRequest();
        return getBasicPath(httpServletRequest);
    }


    /**
     * 从请求中获取请求的域名以及项目名，如：http://192.168.90.77:8080/topinfo-admin
     *
     * @param request
     * @return java.lang.String
     * @author Ypp
     * @date 2023/10/11 16:11
     */
    public static String getBasicPath(HttpServletRequest request) {
        // 请求协议
        String scheme = request.getScheme();
        // 获取服务器名，localhost；
        String serverName = request.getServerName();
        // 获取服务器端口号，8080；
        int serverPort = request.getServerPort();
        // 获取项目名，/Example；
        String contextPath = request.getContextPath();

        StringBuffer sb = new StringBuffer();
        sb.append(scheme).append("://").append(serverName).append(":").append(serverPort).append(contextPath);

        return sb.toString();
    }

    /**
     * 获取请求类型为：application/x-www-form-urlencoded 的HttpServletRequest 中的所有参数，
     * @author Ypp
     * @date 2023/11/1 11:09
     * @param request
     * @return Map<String,String>
     */
    public static Map<String, String> getParameterMapAll(HttpServletRequest request) {

        Enumeration<String> parameters = request.getParameterNames();

        Map<String, String> params = new HashMap<>();
        while (parameters.hasMoreElements()) {
            String parameter = parameters.nextElement();
            String value = request.getParameter(parameter);
            params.put(parameter, value);
        }

        return params;
    }

    /**
     * 获取请求类型为：application/json 的HttpServletRequest 中的json字符串
     * @author Ypp
     * @date 2023/11/1 11:11
     * @param request
     * @return java.lang.String
     */
    public static String getBodyString(HttpServletRequest request) throws IOException {
        ServletInputStream inputStream = request.getInputStream();
        String jsonStr = StreamUtils.copyToString(inputStream, Charset.forName("UTF-8"));
        return jsonStr;
    }

    /**
     * 获取客户端浏览器
     *
     * @param request
     * @return java.lang.String
     * @author Ypp
     * @date 2023/10/13 15:02
     */
    public static String getBrowser(HttpServletRequest request) {
        UserAgent userAgent = getUserAgent(request);
        if (ObjectUtil.isEmpty(userAgent)) {
            return StrUtil.DASHED;
        } else {
            String browser = userAgent.getBrowser().toString();
            return "Unknown".equals(browser) ? StrUtil.DASHED : browser;
        }
    }

    /**
     * 获取请求代理头
     *
     * @param request
     * @return cn.hutool.http.useragent.UserAgent
     * @author Ypp
     * @date 2023/10/13 15:02
     */
    private static UserAgent getUserAgent(HttpServletRequest request) {
        String userAgentStr = ServletUtil.getHeaderIgnoreCase(request, "User-Agent");
        UserAgent userAgent = UserAgentUtil.parse(userAgentStr);
        if (ObjectUtil.isNotEmpty(userAgentStr)) {
            if ("Unknown".equals(userAgent.getBrowser().getName())) {
                userAgent.setBrowser(new Browser(userAgentStr, null, ""));
            }
        }
        return userAgent;
    }

    /**
     * 获取客户端操作系统
     *
     * @param request
     * @return java.lang.String
     * @author Ypp
     * @date 2023/10/13 15:02
     */
    public static String getOs(HttpServletRequest request) {
        UserAgent userAgent = getUserAgent(request);
        if (ObjectUtil.isEmpty(userAgent)) {
            return StrUtil.DASHED;
        } else {
            String os = userAgent.getOs().toString();
            return "Unknown".equals(os) ? StrUtil.DASHED : os;
        }
    }

    /**
     * 从cookie里读取 参数，注意：必须有一个HTTP请求正在处理，如果没有HTTP请求，那么这个方法将无法工作。
     *
     * @param paramName 参数名
     * @return java.lang.String
     * @author Ypp
     * @date 2023/10/13 15:05
     */
    public static String getCookieParam(String paramName) {
        HttpServletRequest request = getHttpServletRequest();
        return getCookieParam(request, paramName);
    }

    /**
     * 从cookie里读取 参数
     *
     * @param request
     * @param paramName 参数名
     * @return java.lang.String
     * @author Ypp
     * @date 2023/10/13 15:06
     */
    public static String getCookieParam(HttpServletRequest request, String paramName) {
        Cookie[] cookies = request.getCookies();
        if (ObjectUtil.isNotEmpty(cookies)) {
            for (Cookie cookie : cookies) {
                String cookieName = cookie.getName();
                if (cookieName.equals(paramName)) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    /**
     * 从header里读取，注意：必须有一个HTTP请求正在处理，如果没有HTTP请求，那么这个方法将无法工作。
     * @author Ypp
     * @date 2023/10/13 15:08
     * @param paramName 参数名
     * @return java.lang.String
     */
    public static String getHeaderParam(String paramName) {
        HttpServletRequest request = getHttpServletRequest();
        return request.getHeader(paramName);
    }

    /**
     * 从header里读取Token，注意：必须有一个HTTP请求正在处理，如果没有HTTP请求，那么这个方法将无法工作。
     * @author Ypp
     * @date 2023/10/13 15:08
     * @return java.lang.String
     */
    public static String getHeaderToken() {
        HttpServletRequest request = getHttpServletRequest();
        return request.getHeader("token");
    }

}
