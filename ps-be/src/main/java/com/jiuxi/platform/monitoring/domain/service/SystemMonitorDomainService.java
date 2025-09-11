package com.jiuxi.platform.monitoring.domain.service;

import com.jiuxi.platform.monitoring.domain.entity.SystemMetrics;
import com.jiuxi.platform.monitoring.domain.valueobject.MetricValue;
import com.jiuxi.platform.monitoring.domain.valueobject.HealthStatus;
import com.jiuxi.platform.monitoring.infrastructure.collector.MetricsCollector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 系统监控领域服务
 * 
 * @author DDD Refactor
 * @date 2025-09-11
 */
@Service
public class SystemMonitorDomainService {
    
    private final Map<String, SystemMetrics> systemMetricsCache;
    private final List<MetricsCollector> metricsCollectors;
    
    @Autowired
    public SystemMonitorDomainService(List<MetricsCollector> metricsCollectors) {
        this.systemMetricsCache = new ConcurrentHashMap<>();
        this.metricsCollectors = metricsCollectors;
    }
    
    /**
     * 收集系统指标
     */
    public SystemMetrics collectSystemMetrics(String instanceId) {
        SystemMetrics systemMetrics = systemMetricsCache.computeIfAbsent(instanceId, 
                id -> new SystemMetrics(id));
        
        // 从所有收集器收集指标
        for (MetricsCollector collector : metricsCollectors) {
            try {
                if (collector.isEnabled()) {
                    List<MetricValue> metrics = collector.collectMetrics();
                    systemMetrics.updateMetrics(metrics);
                }
            } catch (Exception e) {
                System.err.println("收集器 " + collector.getCollectorName() + " 执行失败: " + e.getMessage());
            }
        }
        
        return systemMetrics;
    }
    
    /**
     * 获取系统指标
     */
    public SystemMetrics getSystemMetrics(String instanceId) {
        return systemMetricsCache.get(instanceId);
    }
    
    /**
     * 获取所有实例的系统指标
     */
    public Map<String, SystemMetrics> getAllSystemMetrics() {
        return new ConcurrentHashMap<>(systemMetricsCache);
    }
    
    /**
     * 检查系统健康状态
     */
    public HealthStatus checkSystemHealth(String instanceId) {
        SystemMetrics metrics = systemMetricsCache.get(instanceId);
        
        if (metrics == null) {
            return HealthStatus.unknown("system", "未找到系统指标数据");
        }
        
        return metrics.getOverallHealth();
    }
    
    /**
     * 获取异常实例列表
     */
    public Map<String, HealthStatus> getUnhealthyInstances() {
        Map<String, HealthStatus> unhealthyInstances = new ConcurrentHashMap<>();
        
        for (Map.Entry<String, SystemMetrics> entry : systemMetricsCache.entrySet()) {
            HealthStatus health = entry.getValue().getOverallHealth();
            if (health.isUnhealthy()) {
                unhealthyInstances.put(entry.getKey(), health);
            }
        }
        
        return unhealthyInstances;
    }
    
    /**
     * 获取需要警告的实例列表
     */
    public Map<String, HealthStatus> getWarningInstances() {
        Map<String, HealthStatus> warningInstances = new ConcurrentHashMap<>();
        
        for (Map.Entry<String, SystemMetrics> entry : systemMetricsCache.entrySet()) {
            HealthStatus health = entry.getValue().getOverallHealth();
            if (health.needsWarning()) {
                warningInstances.put(entry.getKey(), health);
            }
        }
        
        return warningInstances;
    }
    
    /**
     * 清理过期的指标数据
     */
    public void cleanupExpiredMetrics(LocalDateTime before) {
        for (SystemMetrics metrics : systemMetricsCache.values()) {
            metrics.cleanOldHistory(before);
        }
    }
    
    /**
     * 移除实例指标
     */
    public void removeInstanceMetrics(String instanceId) {
        systemMetricsCache.remove(instanceId);
    }
    
    /**
     * 获取指定指标的当前值
     */
    public MetricValue getCurrentMetricValue(String instanceId, String metricName) {
        SystemMetrics metrics = systemMetricsCache.get(instanceId);
        if (metrics != null) {
            return metrics.getCurrentMetric(metricName);
        }
        return null;
    }
    
    /**
     * 获取指定指标的历史数据
     */
    public List<MetricValue> getMetricHistory(String instanceId, String metricName, int count) {
        SystemMetrics metrics = systemMetricsCache.get(instanceId);
        if (metrics != null) {
            return metrics.getRecentMetricHistory(metricName, count);
        }
        return List.of();
    }
    
    /**
     * 检查指标是否超过阈值
     */
    public boolean isMetricExceedingThreshold(String instanceId, String metricName, double threshold) {
        MetricValue metric = getCurrentMetricValue(instanceId, metricName);
        return metric != null && metric.exceedsThreshold(threshold);
    }
    
    /**
     * 获取启用的收集器列表
     */
    public List<MetricsCollector> getEnabledCollectors() {
        return metricsCollectors.stream()
                .filter(MetricsCollector::isEnabled)
                .toList();
    }
    
    /**
     * 启用收集器
     */
    public void enableCollector(String collectorName) {
        metricsCollectors.stream()
                .filter(collector -> collector.getCollectorName().equals(collectorName))
                .findFirst()
                .ifPresent(MetricsCollector::enable);
    }
    
    /**
     * 禁用收集器
     */
    public void disableCollector(String collectorName) {
        metricsCollectors.stream()
                .filter(collector -> collector.getCollectorName().equals(collectorName))
                .findFirst()
                .ifPresent(MetricsCollector::disable);
    }
    
    /**
     * 获取系统监控概要
     */
    public MonitoringSummary getMonitoringSummary() {
        int totalInstances = systemMetricsCache.size();
        int healthyInstances = 0;
        int warningInstances = 0;
        int unhealthyInstances = 0;
        int unknownInstances = 0;
        
        for (SystemMetrics metrics : systemMetricsCache.values()) {
            HealthStatus health = metrics.getOverallHealth();
            switch (health.getStatus()) {
                case HEALTHY:
                    healthyInstances++;
                    break;
                case WARNING:
                    warningInstances++;
                    break;
                case UNHEALTHY:
                case CRITICAL:
                    unhealthyInstances++;
                    break;
                case UNKNOWN:
                    unknownInstances++;
                    break;
            }
        }
        
        return new MonitoringSummary(totalInstances, healthyInstances, warningInstances, 
                unhealthyInstances, unknownInstances, metricsCollectors.size());
    }
    
    /**
     * 监控概要信息
     */
    public static class MonitoringSummary {
        private final int totalInstances;
        private final int healthyInstances;
        private final int warningInstances;
        private final int unhealthyInstances;
        private final int unknownInstances;
        private final int collectorsCount;
        
        public MonitoringSummary(int totalInstances, int healthyInstances, int warningInstances,
                int unhealthyInstances, int unknownInstances, int collectorsCount) {
            this.totalInstances = totalInstances;
            this.healthyInstances = healthyInstances;
            this.warningInstances = warningInstances;
            this.unhealthyInstances = unhealthyInstances;
            this.unknownInstances = unknownInstances;
            this.collectorsCount = collectorsCount;
        }
        
        // Getters
        public int getTotalInstances() { return totalInstances; }
        public int getHealthyInstances() { return healthyInstances; }
        public int getWarningInstances() { return warningInstances; }
        public int getUnhealthyInstances() { return unhealthyInstances; }
        public int getUnknownInstances() { return unknownInstances; }
        public int getCollectorsCount() { return collectorsCount; }
        
        public double getHealthPercentage() {
            return totalInstances > 0 ? (double) healthyInstances / totalInstances * 100 : 0;
        }
        
        @Override
        public String toString() {
            return String.format("MonitoringSummary{total=%d, healthy=%d, warning=%d, unhealthy=%d, unknown=%d, collectors=%d}", 
                    totalInstances, healthyInstances, warningInstances, unhealthyInstances, unknownInstances, collectorsCount);
        }
    }
}