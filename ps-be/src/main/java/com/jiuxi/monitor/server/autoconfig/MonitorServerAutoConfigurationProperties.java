package com.jiuxi.monitor.server.autoconfig;

import com.jiuxi.monitor.server.bean.MailConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @ClassName: MonitorClientAutoConfigurationProperties
 * @Description: 监控客户端配置
 * @Author 杨占锐
 * @Date 2024/11/5 18:13
 * @Copyright: www.tuxun.net Inc. All rights reserved.
 */
@ConfigurationProperties(prefix = "jiuxi.platform.plugin.monitor")
public class MonitorServerAutoConfigurationProperties {

    /**
     * 邮件配置
     */
    private MailConfig mailConfig;

    public MailConfig getMailConfig() {
        return mailConfig;
    }

    public void setMailConfig(MailConfig mailConfig) {
        this.mailConfig = mailConfig;
    }
}