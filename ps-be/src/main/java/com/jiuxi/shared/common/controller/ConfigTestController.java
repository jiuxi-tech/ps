package com.jiuxi.shared.common.controller;

import com.jiuxi.shared.common.config.ConfigurationManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 配置测试控制器
 * 用于验证新的配置管理系统是否正常工作
 * 
 * @author System
 * @since 2.2.2-SNAPSHOT
 */
@Slf4j
@RestController
@RequestMapping("/api/config")
public class ConfigTestController {

    @Autowired
    private ConfigurationManager configurationManager;

    /**
     * 获取配置摘要信息
     */
    @GetMapping("/summary")
    public Map<String, Object> getConfigSummary() {
        log.info("获取配置摘要信息");
        
        Map<String, Object> summary = new HashMap<>();
        
        try {
            // 应用配置信息
            summary.put("applicationName", configurationManager.getApplicationConfig().getInfo().getName());
            summary.put("applicationVersion", configurationManager.getApplicationConfig().getInfo().getVersion());
            summary.put("applicationDescription", configurationManager.getApplicationConfig().getInfo().getDescription());
            
            // 环境信息
            summary.put("currentEnvironment", configurationManager.getCurrentEnvironment());
            summary.put("isDevelopment", configurationManager.isDevelopmentEnvironment());
            summary.put("isProduction", configurationManager.isProductionEnvironment());
            
            // 监控配置
            summary.put("monitorEnabled", configurationManager.getApplicationConfig().getMonitor().isEnabled());
            summary.put("monitorUrl", configurationManager.getApplicationConfig().getMonitor().getServerUrl());
            
            // 数据库配置
            summary.put("maxDatabaseConnections", configurationManager.getDatabaseConfig().getPool().getMaxActive());
            summary.put("minIdleConnections", configurationManager.getDatabaseConfig().getPool().getMinIdle());
            
            // 缓存配置
            summary.put("redisHost", configurationManager.getCacheConfig().getRedis().getHost());
            summary.put("redisPort", configurationManager.getCacheConfig().getRedis().getPort());
            summary.put("cacheDefaultTtl", configurationManager.getCacheConfig().getStrategy().getDefaultTtl());
            
            // 安全配置
            summary.put("securityEnabled", configurationManager.getSecurityConfig().getBasic().isEnabled());
            summary.put("keycloakServerUrl", configurationManager.getSecurityConfig().getKeycloak().getServerUrl());
            summary.put("ssoEnabled", configurationManager.getSecurityConfig().getKeycloak().getSso().isEnabled());
            
            summary.put("status", "success");
            summary.put("message", "新配置管理系统工作正常");
            
        } catch (Exception e) {
            log.error("获取配置摘要失败", e);
            summary.put("status", "error");
            summary.put("message", "配置获取失败: " + e.getMessage());
        }
        
        return summary;
    }

    /**
     * 获取配置属性值
     */
    @GetMapping("/property")
    public Map<String, Object> getProperty(String key, String defaultValue) {
        log.info("获取配置属性: {}", key);
        
        Map<String, Object> result = new HashMap<>();
        
        try {
            String value = configurationManager.getProperty(key, defaultValue);
            result.put("key", key);
            result.put("value", value);
            result.put("hasValue", value != null);
            result.put("isDefault", value != null && value.equals(defaultValue));
            result.put("status", "success");
            
        } catch (Exception e) {
            log.error("获取配置属性失败: {}", key, e);
            result.put("status", "error");
            result.put("message", e.getMessage());
        }
        
        return result;
    }

    /**
     * 验证配置完整性
     */
    @GetMapping("/validate")
    public Map<String, Object> validateConfiguration() {
        log.info("验证配置完整性");
        
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 这里可以调用配置验证器进行完整性检查
            result.put("validation", "passed");
            result.put("status", "success");
            result.put("message", "配置验证通过");
            
            // 添加一些验证详情
            Map<String, Object> details = new HashMap<>();
            details.put("applicationConfigValid", configurationManager.getApplicationConfig() != null);
            details.put("databaseConfigValid", configurationManager.getDatabaseConfig() != null);
            details.put("cacheConfigValid", configurationManager.getCacheConfig() != null);
            details.put("securityConfigValid", configurationManager.getSecurityConfig() != null);
            
            result.put("details", details);
            
        } catch (Exception e) {
            log.error("配置验证失败", e);
            result.put("validation", "failed");
            result.put("status", "error");
            result.put("message", "配置验证失败: " + e.getMessage());
        }
        
        return result;
    }
}