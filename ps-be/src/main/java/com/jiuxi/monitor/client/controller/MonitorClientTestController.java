package com.jiuxi.monitor.client.controller;

import com.alibaba.fastjson.JSONObject;
import com.jiuxi.common.bean.JsonResponse;
import com.jiuxi.monitor.client.autoconfig.MonitorClientAutoConfigurationProperties;
import com.jiuxi.monitor.client.core.service.MonitorClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 监控客户端测试控制器
 */
@RestController
@RequestMapping("/test")
public class MonitorClientTestController {

    @Autowired
    private ApplicationContext applicationContext;
    
    @Autowired(required = false)
    private MonitorClientAutoConfigurationProperties properties;
    
    @Autowired(required = false)
    private MonitorClientService monitorClientService;

    /**
     * 检查监控客户端是否启动
     */
    @RequestMapping("/test_monitor_client_status")
    public JsonResponse testMonitorClientStatus() {
        JSONObject result = new JSONObject();
        
        try {
            // 检查配置属性是否存在
            if (properties != null) {
                result.put("configExists", true);
                result.put("serverUrl", properties.getServerUrl());
                result.put("clientId", properties.getClientId());
                result.put("systemDesc", properties.getSystemDesc());
            } else {
                result.put("configExists", false);
            }
            
            // 检查服务是否存在
            if (monitorClientService != null) {
                result.put("serviceExists", true);
            } else {
                result.put("serviceExists", false);
            }
            
            // 检查Spring容器中的监控相关Bean
            String[] beanNames = applicationContext.getBeanNamesForType(Object.class);
            int monitorBeanCount = 0;
            for (String beanName : beanNames) {
                if (beanName.toLowerCase().contains("monitor")) {
                    monitorBeanCount++;
                }
            }
            result.put("monitorBeanCount", monitorBeanCount);
            
            // 检查是否存在MonitorClientAutoConfigurationProperties Bean
            boolean hasMonitorConfig = applicationContext.containsBean("monitorClientAutoConfigurationProperties") ||
                                     applicationContext.getBeansOfType(MonitorClientAutoConfigurationProperties.class).size() > 0;
            result.put("hasMonitorConfigBean", hasMonitorConfig);
            
            result.put("status", "检测完成");
            
        } catch (Exception e) {
            result.put("error", e.getMessage());
            result.put("status", "检测异常");
        }
        
        return JsonResponse.buildSuccess(result);
    }
    
    /**
     * 测试触发心跳
     *
     * @return JsonResponse
     */
    @RequestMapping("/test_trigger_heartbeat")
    public JsonResponse testTriggerHeartbeat() {
        if (monitorClientService != null) {
            try {
                monitorClientService.heartbeat();
                return JsonResponse.buildSuccess("心跳发送成功");
            } catch (Exception e) {
                return JsonResponse.buildFailure("心跳发送失败: " + e.getMessage());
            }
        } else {
            return JsonResponse.buildFailure("监控客户端服务未启动");
        }
    }

    /**
     * 详细检查Bean创建情况
     *
     * @return JsonResponse
     */
    @RequestMapping("/test_bean_details")
    public JsonResponse testBeanDetails() {
        Map<String, Object> result = new HashMap<>();
        
        // 检查MonitorClientAutoConfigurationProperties Bean
        try {
            MonitorClientAutoConfigurationProperties props = applicationContext.getBean(MonitorClientAutoConfigurationProperties.class);
            result.put("monitorClientAutoConfigurationProperties", "存在");
            result.put("serverUrl", props.getServerUrl());
        } catch (Exception e) {
            result.put("monitorClientAutoConfigurationProperties", "不存在: " + e.getMessage());
        }
        
        // 检查MonitorClientService Bean
        try {
            MonitorClientService service = applicationContext.getBean(MonitorClientService.class);
            result.put("monitorClientService", "存在");
        } catch (Exception e) {
            result.put("monitorClientService", "不存在: " + e.getMessage());
        }
        
        // 检查MonitorHealthServiceComposite Bean
        try {
            Object healthService = applicationContext.getBean("monitorHealthServiceComposite");
            result.put("monitorHealthServiceComposite", "存在");
        } catch (Exception e) {
            result.put("monitorHealthServiceComposite", "不存在: " + e.getMessage());
        }
        
        // 检查所有监控相关的Bean
        String[] beanNames = applicationContext.getBeanNamesForType(Object.class);
        List<String> monitorBeans = new ArrayList<>();
        for (String beanName : beanNames) {
            if (beanName.toLowerCase().contains("monitor")) {
                monitorBeans.add(beanName);
            }
        }
        result.put("allMonitorBeans", monitorBeans);
        
        return JsonResponse.buildSuccess(result);
    }
}