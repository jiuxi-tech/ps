package com.jiuxi.monitor.client.core.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.jiuxi.common.exception.ExceptionUtils;
import com.jiuxi.monitor.client.autoconfig.MonitorClientAutoConfigurationProperties;
import com.jiuxi.monitor.client.constant.MonitorClientConstant;
import com.jiuxi.monitor.client.core.bean.ServerHealth;
import com.jiuxi.monitor.client.core.service.MonitorHealthService;
import com.jiuxi.monitor.client.core.util.MonitorClientThreadPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.*;

/**
 * @ClassName: MonitorHealthServiceComposite
 * @Description:
 * @Author jiuxx
 * @Date 2024/11/21 9:16
 * @Copyright: Hangzhou Jiuxi Inc. All rights reserved.
 */
@Service
@ConditionalOnProperty(prefix = "jiuxi.platform.plugin.monitor", name = "server-url")
public class MonitorHealthServiceComposite implements ApplicationContextAware {

    private static final Logger LOGGER = LoggerFactory.getLogger(MonitorHealthServiceComposite.class);

    @Autowired(required = false)
    private MonitorClientAutoConfigurationProperties properties;

    /**
     * 保存不同的服务实现
     */
    private List<MonitorHealthService> serviceList = new ArrayList<>();

    /**
     * 获取不健康的服务
     *
     * @return java.util.List<java.lang.String>
     * @author jiuxx
     * @date 2024/11/21 9:26
     */
    public List<String> getNotHealthServer(){

        List<String> list = new ArrayList<>();
        if (CollectionUtil.isEmpty(serviceList)) {
            return list;
        }

        // 新建数组，用于存放异步任务
        CompletableFuture<ServerHealth> [] arrFutures = new CompletableFuture[serviceList.size()];
        // 线程池
        ThreadPoolExecutor threadPoolExecutor = MonitorClientThreadPool.getPoolExecutor();

        // 循环，异步去执行算法
        for (int i = 0; i < serviceList.size(); i++) {

            MonitorHealthService healthService = serviceList.get(i);
            // 创建异步执行任务:
            CompletableFuture<ServerHealth> completableFuture = CompletableFuture.supplyAsync(()->{
                // 执行具体的算法
                boolean bool = healthService.isHealth();
                return new ServerHealth(bool, healthService.getServerName());
            }, threadPoolExecutor);

            // 记录已经创建的异步任务
            arrFutures[i] = completableFuture;
        }

        // 存法执行结果
        Map<String, ServerHealth> map = new HashMap<>();
        for (CompletableFuture<ServerHealth> future: arrFutures) {
            ServerHealth serverHealth = null;
            try {
                // 每个服务最多等待 5 秒
                serverHealth = future.get(MonitorClientConstant.WAIT_SECONDS, TimeUnit.SECONDS);
                map.put(serverHealth.getServerName(), serverHealth);
            } catch (Exception e) {
                LOGGER.info("获取健康情况，线程等待超时，错误：{}", ExceptionUtils.getStackTrace(e));
            }
        }

        for (MonitorHealthService service: serviceList) {
            ServerHealth serverHealth = map.get(service.getServerName());
            if (null != serverHealth && serverHealth.isHealth()) {
                continue;
            }
            list.add(service.getServerName());
        }

        return list;
    }

    /**
     * 系统启动时，由spring调用，获取到所有DtLocalDbDataService接口的服务
     *
     * @param applicationContext
     * @return void
     * @author jiuxx
     * @date 2024/7/3 18:01
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        // 获取所有服务
        Map<String, MonitorHealthService> beanMap = applicationContext.getBeansOfType(MonitorHealthService.class);
        Collection<MonitorHealthService> values = beanMap.values();
        List<MonitorHealthService> list = new ArrayList<>(values);

        // 删除排除的服务
        this.removeExcludeSource(list);
        this.serviceList = list;
    }

    /**
     * 删除排除的服务
     *
     * @param list 系统自动识别到的服务集合
     * @return void
     * @author jiuxx
     * @date 2024/11/25 10:11
     */
    private void removeExcludeSource(List<MonitorHealthService> list) {
        try {

            if (CollectionUtil.isEmpty(list)) {
                return;
            }
            List<String> excludeSource = properties.getExcludeSource();
            if (CollectionUtil.isEmpty(excludeSource)) {
                return;
            }
            Map<String, String> temp = new HashMap<>();
            for (String str: excludeSource) {
                temp.put(str, str);
            }
            list.removeIf(service -> StrUtil.isNotBlank(temp.get(service.getServerName())));
        } catch (Exception e) {
            LOGGER.error("监控服务客户端启动后，删除排除的服务失败，请检查配置文件【jiuxi.platform.plugin.monitor.exclude-source】");
        }
    }
}
