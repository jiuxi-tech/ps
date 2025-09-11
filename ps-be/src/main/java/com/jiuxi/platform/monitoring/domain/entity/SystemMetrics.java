package com.jiuxi.platform.monitoring.domain.entity;

import com.jiuxi.platform.monitoring.domain.valueobject.MetricValue;
import com.jiuxi.platform.monitoring.domain.valueobject.HealthStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 系统指标实体
 * 
 * @author DDD Refactor
 * @date 2025-09-11
 */
public class SystemMetrics {
    
    private final String instanceId;
    private final Map<String, MetricValue> currentMetrics;
    private final List<MetricValue> metricHistory;
    private HealthStatus overallHealth;
    private LocalDateTime lastUpdateTime;
    
    public SystemMetrics(String instanceId) {
        if (instanceId == null || instanceId.trim().isEmpty()) {
            throw new IllegalArgumentException("实例ID不能为空");
        }
        
        this.instanceId = instanceId.trim();
        this.currentMetrics = new ConcurrentHashMap<>();
        this.metricHistory = new ArrayList<>();
        this.overallHealth = HealthStatus.unknown("system", "系统初始化中");
        this.lastUpdateTime = LocalDateTime.now();
    }
    
    /**
     * 更新指标值
     */
    public void updateMetric(MetricValue metricValue) {
        if (metricValue == null) {
            return;
        }
        
        // 保存当前值
        currentMetrics.put(metricValue.getName(), metricValue);
        
        // 添加到历史记录
        metricHistory.add(metricValue);
        
        // 更新时间
        this.lastUpdateTime = LocalDateTime.now();
        
        // 重新评估整体健康状态
        evaluateOverallHealth();
    }
    
    /**
     * 批量更新指标
     */
    public void updateMetrics(List<MetricValue> metrics) {
        if (metrics == null || metrics.isEmpty()) {
            return;
        }
        
        for (MetricValue metric : metrics) {
            currentMetrics.put(metric.getName(), metric);
            metricHistory.add(metric);
        }
        
        this.lastUpdateTime = LocalDateTime.now();
        evaluateOverallHealth();
    }
    
    /**
     * 获取当前指标值
     */
    public MetricValue getCurrentMetric(String metricName) {
        return currentMetrics.get(metricName);
    }
    
    /**
     * 获取所有当前指标
     */
    public Map<String, MetricValue> getAllCurrentMetrics() {
        return new ConcurrentHashMap<>(currentMetrics);
    }
    
    /**
     * 获取指标历史记录
     */
    public List<MetricValue> getMetricHistory(String metricName) {
        List<MetricValue> history = new ArrayList<>();
        for (MetricValue metric : metricHistory) {
            if (metricName.equals(metric.getName())) {
                history.add(metric);
            }
        }
        return history;
    }
    
    /**
     * 获取最近的指标历史
     */
    public List<MetricValue> getRecentMetricHistory(String metricName, int count) {
        List<MetricValue> history = getMetricHistory(metricName);
        if (history.size() <= count) {
            return history;
        }
        return history.subList(history.size() - count, history.size());
    }
    
    /**
     * 清理旧的历史数据
     */
    public void cleanOldHistory(LocalDateTime before) {
        metricHistory.removeIf(metric -> metric.getTimestamp().isBefore(before));
    }
    
    /**
     * 评估整体健康状态
     */
    private void evaluateOverallHealth() {
        // 检查CPU使用率
        MetricValue cpuUsage = currentMetrics.get("cpu.usage");
        if (cpuUsage != null && cpuUsage.getValue() > 90) {
            this.overallHealth = HealthStatus.critical("system", "CPU使用率过高", 
                    String.format("当前CPU使用率: %.2f%%", cpuUsage.getValue()));
            return;
        }
        
        // 检查内存使用率
        MetricValue memoryUsage = currentMetrics.get("memory.usage");
        if (memoryUsage != null && memoryUsage.getValue() > 85) {
            this.overallHealth = HealthStatus.unhealthy("system", "内存使用率过高", 
                    String.format("当前内存使用率: %.2fMB", memoryUsage.getValue()));
            return;
        }
        
        // 检查磁盘使用率
        MetricValue diskUsage = currentMetrics.get("disk.usage");
        if (diskUsage != null && diskUsage.getValue() > 95) {
            this.overallHealth = HealthStatus.critical("system", "磁盘空间不足", 
                    String.format("当前磁盘使用率: %.2f%%", diskUsage.getValue()));
            return;
        }
        
        // 如果有告警级别的指标，设置为警告状态
        boolean hasWarningMetrics = currentMetrics.values().stream()
                .anyMatch(metric -> {
                    if ("cpu.usage".equals(metric.getName()) && metric.getValue() > 80) return true;
                    if ("memory.usage".equals(metric.getName()) && metric.getValue() > 75) return true;
                    if ("disk.usage".equals(metric.getName()) && metric.getValue() > 85) return true;
                    return false;
                });
        
        if (hasWarningMetrics) {
            this.overallHealth = HealthStatus.warning("system", "系统性能存在警告", "部分指标接近阈值");
        } else {
            this.overallHealth = HealthStatus.healthy("system", "系统运行正常");
        }
    }
    
    /**
     * 检查是否有异常指标
     */
    public boolean hasAbnormalMetrics() {
        return !overallHealth.isHealthy();
    }
    
    /**
     * 获取异常指标列表
     */
    public List<MetricValue> getAbnormalMetrics() {
        List<MetricValue> abnormalMetrics = new ArrayList<>();
        
        for (MetricValue metric : currentMetrics.values()) {
            if ("cpu.usage".equals(metric.getName()) && metric.getValue() > 80) {
                abnormalMetrics.add(metric);
            } else if ("memory.usage".equals(metric.getName()) && metric.getValue() > 75) {
                abnormalMetrics.add(metric);
            } else if ("disk.usage".equals(metric.getName()) && metric.getValue() > 85) {
                abnormalMetrics.add(metric);
            }
        }
        
        return abnormalMetrics;
    }
    
    public String getInstanceId() {
        return instanceId;
    }
    
    public HealthStatus getOverallHealth() {
        return overallHealth;
    }
    
    public LocalDateTime getLastUpdateTime() {
        return lastUpdateTime;
    }
    
    public int getMetricHistorySize() {
        return metricHistory.size();
    }
    
    @Override
    public String toString() {
        return String.format("SystemMetrics{instanceId='%s', health=%s, lastUpdate=%s, metricsCount=%d}", 
                instanceId, overallHealth.getStatus(), lastUpdateTime, currentMetrics.size());
    }
}