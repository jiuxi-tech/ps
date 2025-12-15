package com.jiuxi.common.util;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description: 请求工具类
 * @ClassName: RequestUtils
 * @Author: pdd
 * @Date: 2020-09-09 18:16
 * @Copyright: 2020 Hangzhou Jiuxi Inc. All rights reserved.
 */
public class RequestUtils {

    /**
     * 排除路径
     * 判断servletPath请求路径是否在匹配路径excludePath集合中，存在返回true，不存在返回false
     *
     * @param servletPath: 请求路径
     * @param excludePath: 过滤路径集合
     * @return boolean true：不需要鉴权   false：需要鉴权
     * @author pand
     * @date 2020-08-24 10:44
     */
    public static boolean checkPath(String servletPath, String[] excludePath) {
        assert StringUtils.isBlank(servletPath) : "servletPath 路径异常";

        boolean excludeFlag = false;
        if (excludePath.length == 0) {
            return excludeFlag;
        }
        for (String url : excludePath) {
            String formatUrl = url.replaceAll("\\*", "");
            if (servletPath.indexOf(formatUrl) >= 0) {
                // 不需要鉴权
                excludeFlag = true;
                break;
            }
        }
        return excludeFlag;
    }


    /**
     * 从请求中获取请求的域名以及项目名，如：http://192.168.90.77:8080/topinfo-admin
     *
     * @param request:
     * @return java.lang.String
     * @author pand
     * @date 2021-07-16 11:15
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

}
