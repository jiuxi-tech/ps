package com.jiuxi.monitor.server.autoconfig;


import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.annotation.PostConstruct;

/**
 * @ClassName: MonitorClientAutoConfiguration
 * @Description: 监控客户端配置
 * @Author 杨占锐
 * @Date 2024/11/5 18:12
 * @Copyright: www.tuxun.net Inc. All rights reserved.
 */
@Configuration
@ComponentScan(basePackages = {"com.jiuxi.monitor.server.core"})
@MapperScan({"com.jiuxi.monitor.server.core.mapper"})
@EnableConfigurationProperties({MonitorServerAutoConfigurationProperties.class})
@EnableScheduling
public class MonitorServerAutoConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(MonitorServerAutoConfiguration.class);

    @PostConstruct
    public void init() {
        logger.info("[监控服务端] 监控服务端自动配置类初始化完成");
        logger.debug("[监控服务端] 组件扫描包: com.jiuxi.monitor.server.core");
        logger.debug("[监控服务端] Mapper扫描包: com.jiuxi.monitor.server.core.mapper");
        logger.debug("[监控服务端] 启用定时任务调度");
    }

}