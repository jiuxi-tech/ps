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

import javax.annotation.PostConstruct;

/**
 * @ClassName: MonitorClientHeartbeatSchedule
 * @Description:
 * @Author jiuxx
 * @Date 2024/11/14 17:38
 * @Copyright: Hangzhou Jiuxi Inc. All rights reserved.
 */
@Component
@ConditionalOnBean(MonitorClientAutoConfigurationProperties.class)
public class MonitorClientHeartbeatSchedule {

    private static final Logger LOGGER = LoggerFactory.getLogger(MonitorClientHeartbeatSchedule.class);

    @Autowired
    private MonitorClientService monitorClientService;

    @PostConstruct
    public void init() {
        LOGGER.info("[监控客户端] MonitorClientHeartbeatSchedule 定时任务调度器初始化完成");
        LOGGER.info("[监控客户端] 心跳定时任务将在10秒后开始执行，执行频率: 每10秒一次");
    }

    /**
     * 监控客户端定时上报心跳信息
     * <pre>
     *     1. 10秒执行一次
     * </pre>
     * @return void
     * @author jiuxx
     * @date 2024/11/14 17:39
     */
    @Scheduled(initialDelay = 10 * 1000, fixedRate  = 10 * 1000)
    public void heartbeat() {

        try {
            LOGGER.info("监控客户端开始定时上报心跳信息，频率10秒一次");

            monitorClientService.heartbeat();

            LOGGER.info("监控客户端定时上报心跳信息，完成");
        } catch (Exception e) {
            LOGGER.error("监控客户端定时上报心跳信息失败，错误：{}", ExceptionUtils.getStackTrace(e));
        }
    }
}
