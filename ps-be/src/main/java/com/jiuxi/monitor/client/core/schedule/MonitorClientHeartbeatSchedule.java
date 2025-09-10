package com.jiuxi.monitor.client.core.schedule;

import com.jiuxi.common.exception.ExceptionUtils;
import com.jiuxi.monitor.client.autoconfig.MonitorClientAutoConfigurationProperties;
import com.jiuxi.monitor.client.core.service.MonitorClientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @ClassName: MonitorClientHeartbeatSchedule
 * @Description:
 * @Author 杨占锐
 * @Date 2024/11/14 17:38
 * @Copyright: www.tuxun.net Inc. All rights reserved.
 */
@Component
@ConditionalOnBean(MonitorClientAutoConfigurationProperties.class)
public class MonitorClientHeartbeatSchedule {

    private static final Logger LOGGER = LoggerFactory.getLogger(MonitorClientHeartbeatSchedule.class);

    @Autowired
    private MonitorClientService monitorClientService;

    /**
     * 监控客户端定时上报心跳信息
     * <pre>
     *     1. 60秒执行一次
     * </pre>
     * @return void
     * @author 杨占锐
     * @date 2024/11/14 17:39
     */
    @Scheduled(initialDelay = 60 * 1000, fixedRate  = 60 * 1000)
    public void heartbeat() {

        try {
            LOGGER.info("监控客户端开始定时上报心跳信息，频率60秒一次");

            monitorClientService.heartbeat();

            LOGGER.info("监控客户端定时上报心跳信息，完成");
        } catch (Exception e) {
            LOGGER.error("监控客户端定时上报心跳信息失败，错误：{}", ExceptionUtils.getStackTrace(e));
        }
    }
}
