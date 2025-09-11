package com.jiuxi.module.sys.domain.service;

import com.jiuxi.module.sys.domain.entity.SystemConfig;
import com.jiuxi.module.sys.domain.entity.ConfigType;
import com.jiuxi.module.sys.domain.event.ConfigurationChangedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 系统配置热更新领域服务
 * 提供运行时配置热更新和刷新功能
 * 
 * @author DDD Refactor
 * @date 2025-09-11
 */
@Service
public class SystemConfigHotReloadService {
    
    /**
     * 热更新监听器接口
     */
    public interface ConfigChangeListener {
        /**
         * 配置变更回调
         * @param configKey 配置键
         * @param oldValue 旧值
         * @param newValue 新值
         * @param configType 配置类型
         */
        void onConfigChanged(String configKey, String oldValue, String newValue, ConfigType configType);
        
        /**
         * 获取监听的配置键模式
         * @return 配置键模式列表
         */
        List<String> getListeningKeyPatterns();
    }
    
    @Autowired
    private ConfigurableEnvironment environment;
    
    @Autowired
    private SystemConfigCacheService systemConfigCacheService;
    
    /**
     * 配置变更监听器注册表
     */
    private final Map<String, ConfigChangeListener> listeners = new ConcurrentHashMap<>();
    
    /**
     * 动态属性源名称
     */
    private static final String DYNAMIC_PROPERTY_SOURCE_NAME = "dynamicSystemConfigs";
    
    /**
     * 配置刷新任务调度器
     */
    private final ScheduledExecutorService refreshScheduler = Executors.newSingleThreadScheduledExecutor(
            r -> {
                Thread thread = new Thread(r, "ConfigHotReload-Scheduler");
                thread.setDaemon(true);
                return thread;
            }
    );
    
    /**
     * 初始化热更新服务
     */
    public void initializeHotReload() {
        // 创建动态属性源
        createDynamicPropertySource();
        
        // 启动定时刷新任务（每30秒检查一次）
        refreshScheduler.scheduleWithFixedDelay(this::refreshDynamicConfigs, 30, 30, TimeUnit.SECONDS);
    }
    
    /**
     * 注册配置变更监听器
     * @param listenerId 监听器ID
     * @param listener 监听器实例
     */
    public void registerConfigChangeListener(String listenerId, ConfigChangeListener listener) {
        listeners.put(listenerId, listener);
    }
    
    /**
     * 注销配置变更监听器
     * @param listenerId 监听器ID
     */
    public void unregisterConfigChangeListener(String listenerId) {
        listeners.remove(listenerId);
    }
    
    /**
     * 监听配置变更事件
     * @param event 配置变更事件
     */
    @EventListener
    public void handleConfigurationChangedEvent(ConfigurationChangedEvent event) {
        
        // 更新动态属性源
        updateDynamicPropertySource(event.getConfigKey(), event.getNewValue());
        
        // 通知注册的监听器
        notifyConfigChangeListeners(event);
        
        // 清理相关缓存
        cleanupRelatedCaches(event);
        
        // 根据配置类型执行特定的热更新逻辑
        executeTypeSpecificHotReload(event);
    }
    
    /**
     * 手动热更新指定配置
     * @param configKey 配置键
     * @param tenantId 租户ID
     * @return 是否更新成功
     */
    public boolean manualHotReload(String configKey, String tenantId) {
        try {
            // 从缓存或数据库获取最新配置
            SystemConfig latestConfig = systemConfigCacheService.getConfigFromCache(configKey, tenantId);
            
            if (latestConfig != null && latestConfig.isActive()) {
                // 更新动态属性源
                updateDynamicPropertySource(configKey, latestConfig.getConfigValue());
                
                // 创建模拟事件并处理
                ConfigurationChangedEvent mockEvent = new ConfigurationChangedEvent(
                        "manual-reload",
                        latestConfig.getConfigId(),
                        configKey,
                        null,
                        latestConfig.getConfigValue(),
                        latestConfig.getConfigType(),
                        ConfigurationChangedEvent.ChangeType.UPDATED,
                        "system",
                        tenantId
                );
                
                notifyConfigChangeListeners(mockEvent);
                executeTypeSpecificHotReload(mockEvent);
                
                return true;
            }
            
            return false;
        } catch (Exception e) {
            // 记录错误但不抛出异常，保证系统稳定性
            return false;
        }
    }
    
    /**
     * 批量热更新配置
     * @param configKeys 配置键列表
     * @param tenantId 租户ID
     * @return 更新成功的配置数量
     */
    public int batchHotReload(List<String> configKeys, String tenantId) {
        int successCount = 0;
        
        for (String configKey : configKeys) {
            if (manualHotReload(configKey, tenantId)) {
                successCount++;
            }
        }
        
        return successCount;
    }
    
    /**
     * 获取当前动态配置快照
     * @return 动态配置Map
     */
    public Map<String, Object> getDynamicConfigSnapshot() {
        MapPropertySource dynamicSource = (MapPropertySource) environment
                .getPropertySources().get(DYNAMIC_PROPERTY_SOURCE_NAME);
        
        return dynamicSource != null ? 
                new ConcurrentHashMap<>(dynamicSource.getSource()) : 
                new ConcurrentHashMap<>();
    }
    
    /**
     * 重置动态配置
     * 清空所有动态配置并重新加载
     * @param tenantId 租户ID
     */
    public void resetDynamicConfigs(String tenantId) {
        // 清空动态属性源
        MapPropertySource dynamicSource = (MapPropertySource) environment
                .getPropertySources().get(DYNAMIC_PROPERTY_SOURCE_NAME);
        
        if (dynamicSource != null) {
            dynamicSource.getSource().clear();
        }
        
        // 清除所有配置缓存
        systemConfigCacheService.evictAllConfigsForTenant(tenantId);
        
        // 触发配置重新加载
        refreshDynamicConfigs();
    }
    
    /**
     * 创建动态属性源
     */
    private void createDynamicPropertySource() {
        MapPropertySource dynamicSource = new MapPropertySource(
                DYNAMIC_PROPERTY_SOURCE_NAME, new ConcurrentHashMap<>());
        
        // 将动态属性源添加到环境配置的最高优先级
        environment.getPropertySources().addFirst(dynamicSource);
    }
    
    /**
     * 更新动态属性源
     * @param configKey 配置键
     * @param configValue 配置值
     */
    private void updateDynamicPropertySource(String configKey, String configValue) {
        MapPropertySource dynamicSource = (MapPropertySource) environment
                .getPropertySources().get(DYNAMIC_PROPERTY_SOURCE_NAME);
        
        if (dynamicSource != null) {
            if (configValue != null) {
                dynamicSource.getSource().put(configKey, configValue);
            } else {
                dynamicSource.getSource().remove(configKey);
            }
        }
    }
    
    /**
     * 通知配置变更监听器
     * @param event 配置变更事件
     */
    private void notifyConfigChangeListeners(ConfigurationChangedEvent event) {
        listeners.values().forEach(listener -> {
            try {
                // 检查监听器是否关心这个配置键
                if (isListenerInterestedInKey(listener, event.getConfigKey())) {
                    ConfigType configType = event.getConfigType() != null ? 
                            ConfigType.fromCode(event.getConfigType()) : null;
                    
                    listener.onConfigChanged(
                            event.getConfigKey(),
                            event.getOldValue(),
                            event.getNewValue(),
                            configType
                    );
                }
            } catch (Exception e) {
                // 监听器异常不应该影响其他监听器和主流程
                // 实际应用中应该记录日志
            }
        });
    }
    
    /**
     * 检查监听器是否对配置键感兴趣
     * @param listener 监听器
     * @param configKey 配置键
     * @return 是否感兴趣
     */
    private boolean isListenerInterestedInKey(ConfigChangeListener listener, String configKey) {
        List<String> patterns = listener.getListeningKeyPatterns();
        if (patterns == null || patterns.isEmpty()) {
            return false;
        }
        
        return patterns.stream().anyMatch(pattern -> {
            // 支持简单的通配符匹配
            if (pattern.endsWith("*")) {
                String prefix = pattern.substring(0, pattern.length() - 1);
                return configKey.startsWith(prefix);
            }
            return pattern.equals(configKey);
        });
    }
    
    /**
     * 清理相关缓存
     * @param event 配置变更事件
     */
    private void cleanupRelatedCaches(ConfigurationChangedEvent event) {
        // 清理配置本身的缓存
        if (event.getTenantId() != null) {
            systemConfigCacheService.evictConfig(event.getConfigKey(), event.getTenantId());
        }
        
        // 根据配置类型清理类型缓存
        if (event.getConfigType() != null) {
            try {
                ConfigType configType = ConfigType.fromCode(event.getConfigType());
                systemConfigCacheService.evictConfigsByType(configType, event.getTenantId());
            } catch (Exception e) {
                // 忽略类型解析错误
            }
        }
    }
    
    /**
     * 根据配置类型执行特定的热更新逻辑
     * @param event 配置变更事件
     */
    private void executeTypeSpecificHotReload(ConfigurationChangedEvent event) {
        try {
            ConfigType configType = event.getConfigType() != null ? 
                    ConfigType.fromCode(event.getConfigType()) : null;
            
            if (configType != null) {
                switch (configType) {
                    case CACHE:
                        handleCacheConfigChange(event);
                        break;
                    case DATABASE:
                        handleDatabaseConfigChange(event);
                        break;
                    case SECURITY:
                        handleSecurityConfigChange(event);
                        break;
                    case MONITOR:
                        handleMonitorConfigChange(event);
                        break;
                    default:
                        // 其他类型的通用处理
                        handleGenericConfigChange(event);
                        break;
                }
            }
        } catch (Exception e) {
            // 特定类型的热更新失败不应该影响主流程
            // 实际应用中应该记录详细错误日志
        }
    }
    
    /**
     * 处理缓存配置变更
     */
    private void handleCacheConfigChange(ConfigurationChangedEvent event) {
        // 缓存相关配置变更时，可能需要重新初始化缓存连接
        // 这里只是示例，实际实现需要根据具体的缓存框架
    }
    
    /**
     * 处理数据库配置变更
     */
    private void handleDatabaseConfigChange(ConfigurationChangedEvent event) {
        // 数据库配置变更时，可能需要重新初始化数据源
        // 实际实现需要谨慎处理，避免影响正在进行的事务
    }
    
    /**
     * 处理安全配置变更
     */
    private void handleSecurityConfigChange(ConfigurationChangedEvent event) {
        // 安全配置变更时，可能需要刷新安全策略
        // 例如：JWT密钥变更、权限规则更新等
    }
    
    /**
     * 处理监控配置变更
     */
    private void handleMonitorConfigChange(ConfigurationChangedEvent event) {
        // 监控配置变更时，可能需要调整监控参数
        // 例如：日志级别、监控采样率等
    }
    
    /**
     * 处理通用配置变更
     */
    private void handleGenericConfigChange(ConfigurationChangedEvent event) {
        // 通用配置变更的默认处理逻辑
        // 通常只需要更新属性源即可
    }
    
    /**
     * 刷新动态配置（定时任务）
     */
    private void refreshDynamicConfigs() {
        // 定时刷新任务，可以从数据库重新加载关键配置
        // 这里是一个占位实现，实际需要根据业务需求实现
    }
    
    /**
     * 销毁服务时清理资源
     */
    public void destroy() {
        if (refreshScheduler != null && !refreshScheduler.isShutdown()) {
            refreshScheduler.shutdown();
            try {
                if (!refreshScheduler.awaitTermination(5, TimeUnit.SECONDS)) {
                    refreshScheduler.shutdownNow();
                }
            } catch (InterruptedException e) {
                refreshScheduler.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }
    }
}