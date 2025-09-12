package com.jiuxi.shared.config.events;

import com.jiuxi.shared.config.cache.IpAccessConfigCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * 配置变更监听器
 * 监听系统配置变更事件，自动刷新IP访问控制配置缓存
 * 
 * @author ps-bmp
 * @since 2024-01-20
 */
@Slf4j
@Component
public class ConfigChangeListener implements ApplicationListener<ConfigChangeEvent> {

    @Autowired
    private IpAccessConfigCache ipAccessConfigCache;
    
    // IP访问控制相关配置键前缀
    private static final String IP_CONFIG_PREFIX = "security.ip";
    
    @Override
    public void onApplicationEvent(ConfigChangeEvent event) {
        String configKey = event.getConfigKey();
        String operation = event.getOperation();
        
        log.debug("收到配置变更事件: key={}, operation={}", configKey, operation);
        
        // 只处理IP访问控制相关的配置变更
        if (configKey != null && configKey.startsWith(IP_CONFIG_PREFIX)) {
            try {
                // 刷新对应的配置项
                ipAccessConfigCache.refreshConfig(configKey);
                log.info("IP访问控制配置已更新: {}", configKey);
            } catch (Exception e) {
                log.error("刷新IP访问控制配置失败: {}", configKey, e);
            }
        }
    }
}