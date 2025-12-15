package com.jiuxi.monitor.client.autoconfig;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * @ClassName: MonitorClientAutoConfigurationProperties
 * @Description: 监控客户端配置
 * @Author jiuxx
 * @Date 2024/11/5 18:13
 * @Copyright: Hangzhou Jiuxi Inc. All rights reserved.
 */
@ConfigurationProperties(prefix = "jiuxi.platform.plugin.monitor")
public class MonitorClientAutoConfigurationProperties {

    /**
     * 服务端地址
     */
    private String serverUrl;

    /**
     * 系统描述
     */
    private String systemDesc;

    /**
     * 本系统唯一id
     */
    private String clientId;

    /**
     * http连接超时时间
     */
    private Integer connectionTimeout = 3000;

    /**
     * http读取超时时间
     */
    private Integer readTimeout = 5000;

    /**
     * 排除检查检查的服务
     */
    private List<String> excludeSource;

    public String getServerUrl() {
        return serverUrl;
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public String getSystemDesc() {
        return systemDesc;
    }

    public void setSystemDesc(String systemDesc) {
        this.systemDesc = systemDesc;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public Integer getConnectionTimeout() {
        return connectionTimeout;
    }

    public void setConnectionTimeout(Integer connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public Integer getReadTimeout() {
        return readTimeout;
    }

    public void setReadTimeout(Integer readTimeout) {
        this.readTimeout = readTimeout;
    }

    public List<String> getExcludeSource() {
        return excludeSource;
    }

    public void setExcludeSource(List<String> excludeSource) {
        this.excludeSource = excludeSource;
    }
}
