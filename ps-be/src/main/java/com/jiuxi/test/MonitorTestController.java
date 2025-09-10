package com.jiuxi.test;

import com.jiuxi.common.bean.JsonResponse;
import com.jiuxi.monitor.client.autoconfig.MonitorClientAutoConfigurationProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 监控模块测试控制器
 */
@RestController
@RequestMapping("/test")
public class MonitorTestController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MonitorTestController.class);

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired(required = false)
    private MonitorClientAutoConfigurationProperties monitorProperties;

    /**
     * 检查监控客户端配置状态
     */
    @RequestMapping("/test_monitor_client_config")
    public JsonResponse testMonitorClientConfig() {
        try {
            Map<String, Object> result = new HashMap<>();
            
            // 检查监控配置属性
            if (monitorProperties != null) {
                result.put("monitorProperties", "存在");
                result.put("serverUrl", monitorProperties.getServerUrl());
                result.put("systemDesc", monitorProperties.getSystemDesc());
                result.put("clientId", monitorProperties.getClientId());
            } else {
                result.put("monitorProperties", "不存在");
            }
            
            // 检查Spring上下文中的Bean
            try {
                MonitorClientAutoConfigurationProperties props = applicationContext.getBean(MonitorClientAutoConfigurationProperties.class);
                result.put("monitorPropertiesBean", "存在");
            } catch (Exception e) {
                result.put("monitorPropertiesBean", "不存在: " + e.getMessage());
            }
            
            // 检查监控客户端相关Bean
            String[] beanNames = applicationContext.getBeanNamesForType(Object.class);
            int monitorBeanCount = 0;
            for (String beanName : beanNames) {
                if (beanName.toLowerCase().contains("monitor")) {
                    monitorBeanCount++;
                }
            }
            result.put("monitorRelatedBeanCount", monitorBeanCount);
            
            return JsonResponse.buildSuccess(result);
        } catch (Exception e) {
            LOGGER.error("检查监控客户端配置失败", e);
            return JsonResponse.buildFailure("检查失败: " + e.getMessage());
        }
    }
}