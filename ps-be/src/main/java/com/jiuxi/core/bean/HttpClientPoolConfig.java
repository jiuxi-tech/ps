package com.jiuxi.core.bean;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description: httpClient 连接配置项
 * @ClassName: HttpClientPoolConfig
 * @Author: pand
 * @Date: 2021-06-09 10:15
 * @Copyright: 2021 www.tuxun.net Inc. All rights reserved.
 */
public class HttpClientPoolConfig {

    private String charset = "utf-8";

    /**
     * 最大连接数
     */
    private Integer maxTotalConnect = 1600;


    private Integer maxConnectPerRoute = 10;

    /**
     * 连接超时, 默认10秒
     */
    private Integer connectTimeout = 5000;

    /**
     * 数据读取超时时间，即SocketTimeout, 默认10秒
     */
    private Integer readTimeout = 10000;

    /**
     * 从连接池获取请求连接的超时时间，不宜过长，必须设置，比如连接不够用时，时间过长将是灾难性的，默认3秒
     */
    private Integer connectionRequestTimout = 3000;

    /**
     * 重试次数，默认 5 次
     */
    private Integer retryTimes = 5;

    /**
     * 最大空闲时间 默认5秒
     */
    private Integer maxIdleTime = 5000;

    /**
     * 单独配置目标地址长连接保持时间
     * 如：https:www.baidu.com 60000
     * keepAliveTargetHost.put("https:www.baidu.com", 60);
     */
    private Map<String, Integer> keepAliveTargetHost = new HashMap();


    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public Integer getMaxTotalConnect() {
        return maxTotalConnect;
    }

    public void setMaxTotalConnect(Integer maxTotalConnect) {
        this.maxTotalConnect = maxTotalConnect;
    }

    public Integer getMaxConnectPerRoute() {
        return maxConnectPerRoute;
    }

    public void setMaxConnectPerRoute(Integer maxConnectPerRoute) {
        this.maxConnectPerRoute = maxConnectPerRoute;
    }

    public Integer getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(Integer connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public Integer getReadTimeout() {
        return readTimeout;
    }

    public void setReadTimeout(Integer readTimeout) {
        this.readTimeout = readTimeout;
    }

    public Integer getConnectionRequestTimout() {
        return connectionRequestTimout;
    }

    public void setConnectionRequestTimout(Integer connectionRequestTimout) {
        this.connectionRequestTimout = connectionRequestTimout;
    }

    public Integer getRetryTimes() {
        return retryTimes;
    }

    public void setRetryTimes(Integer retryTimes) {
        this.retryTimes = retryTimes;
    }

    public Integer getMaxIdleTime() {
        return maxIdleTime;
    }

    public void setMaxIdleTime(Integer maxIdleTime) {
        this.maxIdleTime = maxIdleTime;
    }

    public Map getKeepAliveTargetHost() {
        return keepAliveTargetHost;
    }

    public void setKeepAliveTargetHost(Map keepAliveTargetHost) {
        this.keepAliveTargetHost = keepAliveTargetHost;
    }
}
