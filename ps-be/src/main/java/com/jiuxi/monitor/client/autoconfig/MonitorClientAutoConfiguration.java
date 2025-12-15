package com.jiuxi.monitor.client.autoconfig;


import com.jiuxi.monitor.client.core.service.impl.health.DruidDataSourceHealthImpl;
import com.jiuxi.monitor.client.core.service.impl.health.DynamicDataSourceHealthImpl;
import com.jiuxi.monitor.client.core.service.impl.health.RedisHealthImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.annotation.PostConstruct;

/**
 * @ClassName: MonitorClientAutoConfiguration
 * @Description: 监控客户端配置
 * @Author jiuxx
 * @Date 2024/11/5 18:12
 * @Copyright: Hangzhou Jiuxi Inc. All rights reserved.
 */
@Configuration
@ComponentScan({"com.jiuxi.monitor.client.core", "com.jiuxi.monitor.client.controller"})
@EnableConfigurationProperties({MonitorClientAutoConfigurationProperties.class})
@ConditionalOnProperty(prefix = "jiuxi.platform.plugin.monitor", name = "server-url")
public class MonitorClientAutoConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(MonitorClientAutoConfiguration.class);

    @PostConstruct
    public void init() {
        logger.info("[监控客户端] 监控客户端自动配置类初始化完成");
        logger.info("[监控客户端] 组件扫描包: com.jiuxi.monitor.client.core, com.jiuxi.monitor.client.controller");
        logger.info("[监控客户端] MonitorClientAutoConfigurationProperties Bean 已创建");
        logger.info("[监控客户端] 定时任务调度将使用Application主类的@EnableScheduling配置");
    }


    @ConditionalOnProperty(prefix = "jiuxi.mybatis.datasourceConfig", name = "url")
    @Bean
    public DruidDataSourceHealthImpl druidDataSourceHealth(){
        logger.info("[监控客户端] 创建Druid数据源健康检查Bean");
        logger.debug("[监控客户端] 条件: jiuxi.mybatis.datasourceConfig.url 配置存在");
        DruidDataSourceHealthImpl health = new DruidDataSourceHealthImpl();
        logger.debug("[监控客户端] Druid数据源健康检查Bean创建成功");
        return health;
    }

    @ConditionalOnProperty(prefix = "jiuxi.mybatis.dynamic", name = "default-source")
    @Bean
    public DynamicDataSourceHealthImpl dynamicDataSourceHealth(){
        logger.info("[监控客户端] 创建动态数据源健康检查Bean");
        logger.debug("[监控客户端] 条件: jiuxi.mybatis.dynamic.default-source 配置存在");
        DynamicDataSourceHealthImpl health = new DynamicDataSourceHealthImpl();
        logger.debug("[监控客户端] 动态数据源健康检查Bean创建成功");
        return health;
    }

    @ConditionalOnProperty(prefix = "spring.redis.redisson", name = "config")
    @Bean
    public RedisHealthImpl redisHealth(){
        logger.info("[监控客户端] 创建Redis健康检查Bean");
        logger.debug("[监控客户端] 条件: spring.redis.redisson.config 配置存在");
        RedisHealthImpl health = new RedisHealthImpl();
        logger.debug("[监控客户端] Redis健康检查Bean创建成功");
        return health;
    }


}
