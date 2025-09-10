package com.jiuxi.monitor.server.core.util;

import cn.hutool.core.util.StrUtil;

import javax.servlet.http.HttpServletRequest;

/**
 * @ClassName: MonitorIpUtil
 * @Description:
 * @Author 杨占锐
 * @Date 2024/11/18 9:42
 * @Copyright: www.tuxun.net Inc. All rights reserved.
 */
public class MonitorIpUtil {

    /**
     * 获取IP链路
     *
     * @param request
     * @return java.lang.String
     * @author 杨占锐
     * @date 2023/8/7 9:14
     */
    public static String getIpAddr(HttpServletRequest request){
        // 保存访问客户端ip
        String ip = request.getHeader("X-Forwarded-For");
        if(StrUtil.isBlank(ip)){
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}
