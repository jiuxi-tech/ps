package com.jiuxi.platform.monitoring.app.service;

import com.jiuxi.platform.monitoring.domain.service.SystemMonitorDomainService;
import com.jiuxi.platform.monitoring.domain.entity.SystemMetrics;
import com.jiuxi.platform.monitoring.domain.valueobject.MetricValue;
import com.jiuxi.platform.monitoring.domain.valueobject.HealthStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 性能监控服务
 * 提供性能监控相关的应用逻辑
 * 
 * @author DDD Refactor
 * @date 2025-09-11
 */
@Service
public class PerformanceMonitorService {
    
    private final SystemMonitorDomainService systemMonitorDomainService;
    private final String instanceId;
    
    @Autowired
    public PerformanceMonitorService(SystemMonitorDomainService systemMonitorDomainService) {
        this.systemMonitorDomainService = systemMonitorDomainService;
        this.instanceId = generateInstanceId();
    }
    
    /**
     * 获取实时性能数据
     */
    public Map<String, Object> getRealTimePerformance() {
        SystemMetrics metrics = systemMonitorDomainService.collectSystemMetrics(instanceId);
        
        Map<String, Object> performance = new HashMap<>();
        Map<String, MetricValue> currentMetrics = metrics.getAllCurrentMetrics();
        
        // CPU指标
        addMetricToMap(performance, "cpu", currentMetrics, "cpu.usage");
        addMetricToMap(performance, "cpuLoad", currentMetrics, "cpu.load.percentage");
        
        // 内存指标
        addMetricToMap(performance, "memoryHeapUsage", currentMetrics, "memory.heap.usage.percentage");
        addMetricToMap(performance, "memoryPhysicalUsage", currentMetrics, "memory.physical.usage.percentage");
        
        // 磁盘指标
        addMetricToMap(performance, "diskUsage", currentMetrics, "disk.usage");
        
        // JVM指标
        addMetricToMap(performance, "jvmThreads", currentMetrics, "jvm.threads.count");
        addMetricToMap(performance, "jvmGcCount", currentMetrics, "jvm.gc.total.count");
        
        // 系统健康状态
        HealthStatus health = metrics.getOverallHealth();
        performance.put("healthStatus", Map.of(
                "status", health.getStatus().name(),
                "message", health.getMessage(),
                "level", health.getLevel()
        ));
        
        // 更新时间
        performance.put("timestamp", metrics.getLastUpdateTime().toString());
        performance.put("instanceId", instanceId);
        
        return performance;
    }
    
    /**
     * 获取性能历史数据
     */
    public Map<String, Object> getPerformanceHistory(String metricName, int count) {
        List<MetricValue> history = systemMonitorDomainService.getMetricHistory(instanceId, metricName, count);
        
        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> data = new ArrayList<>();
        
        for (MetricValue metric : history) {
            Map<String, Object> point = new HashMap<>();
            point.put("timestamp", metric.getTimestamp().toString());
            point.put("value", metric.getValue());
            point.put("unit", metric.getUnit());
            data.add(point);
        }
        
        result.put("metricName", metricName);
        result.put("data", data);
        result.put("count", data.size());
        
        return result;
    }
    
    /**
     * 获取CPU使用历史
     */
    public List<Map<String, Object>> getCpuUsageHistory(int minutes) {
        return getMetricHistoryData("cpu.usage", minutes);
    }
    
    /**
     * 获取内存使用历史
     */
    public List<Map<String, Object>> getMemoryUsageHistory(int minutes) {
        return getMetricHistoryData("memory.heap.usage.percentage", minutes);
    }
    
    /**
     * 获取磁盘使用历史
     */
    public List<Map<String, Object>> getDiskUsageHistory(int minutes) {
        return getMetricHistoryData("disk.usage", minutes);
    }
    
    /**
     * 获取JVM线程数历史
     */
    public List<Map<String, Object>> getJvmThreadsHistory(int minutes) {
        return getMetricHistoryData("jvm.threads.count", minutes);
    }
    
    /**
     * 获取性能概要信息
     */
    public Map<String, Object> getPerformanceSummary() {
        SystemMetrics metrics = systemMonitorDomainService.getSystemMetrics(instanceId);
        if (metrics == null) {
            metrics = systemMonitorDomainService.collectSystemMetrics(instanceId);
        }
        
        Map<String, Object> summary = new HashMap<>();
        Map<String, MetricValue> currentMetrics = metrics.getAllCurrentMetrics();
        
        // 计算平均性能指标
        double avgCpuUsage = getMetricValue(currentMetrics, "cpu.usage", 0.0);
        double avgMemoryUsage = getMetricValue(currentMetrics, "memory.heap.usage.percentage", 0.0);
        double diskUsage = getMetricValue(currentMetrics, "disk.usage", 0.0);
        
        summary.put("averageCpuUsage", avgCpuUsage);
        summary.put("averageMemoryUsage", avgMemoryUsage);
        summary.put("diskUsage", diskUsage);
        
        // 性能评级
        String performanceGrade = calculatePerformanceGrade(avgCpuUsage, avgMemoryUsage, diskUsage);
        summary.put("performanceGrade", performanceGrade);
        
        // 健康状态
        HealthStatus health = metrics.getOverallHealth();
        summary.put("healthStatus", health.getStatus().name());
        summary.put("healthMessage", health.getMessage());
        
        // 异常指标
        List<MetricValue> abnormalMetrics = metrics.getAbnormalMetrics();
        summary.put("abnormalMetricsCount", abnormalMetrics.size());
        
        List<Map<String, Object>> abnormalMetricsData = new ArrayList<>();
        for (MetricValue metric : abnormalMetrics) {
            Map<String, Object> metricData = new HashMap<>();
            metricData.put("name", metric.getName());
            metricData.put("value", metric.getValue());
            metricData.put("unit", metric.getUnit());
            abnormalMetricsData.add(metricData);
        }
        summary.put("abnormalMetrics", abnormalMetricsData);
        
        // 最后更新时间
        summary.put("lastUpdateTime", metrics.getLastUpdateTime().toString());
        summary.put("instanceId", instanceId);
        
        return summary;
    }
    
    /**
     * 检查性能阈值
     */
    public Map<String, Object> checkPerformanceThresholds() {
        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> alerts = new ArrayList<>();
        
        // CPU阈值检查
        if (systemMonitorDomainService.isMetricExceedingThreshold(instanceId, "cpu.usage", 85.0)) {
            alerts.add(createAlert("CPU使用率过高", "cpu.usage", 85.0, "warning"));
        }
        
        // 内存阈值检查
        if (systemMonitorDomainService.isMetricExceedingThreshold(instanceId, "memory.heap.usage.percentage", 80.0)) {
            alerts.add(createAlert("JVM堆内存使用率过高", "memory.heap.usage.percentage", 80.0, "warning"));
        }
        
        // 磁盘阈值检查
        if (systemMonitorDomainService.isMetricExceedingThreshold(instanceId, "disk.usage", 90.0)) {
            alerts.add(createAlert("磁盘使用率过高", "disk.usage", 90.0, "critical"));
        }
        
        result.put("alerts", alerts);
        result.put("alertCount", alerts.size());
        result.put("hasAlerts", !alerts.isEmpty());
        result.put("checkTime", LocalDateTime.now().toString());
        
        return result;
    }
    
    /**
     * 定时收集性能指标
     */
    @Scheduled(fixedRate = 30000) // 每30秒执行一次
    @Async
    public void schedulePerformanceCollection() {
        try {
            systemMonitorDomainService.collectSystemMetrics(instanceId);
        } catch (Exception e) {
            System.err.println("定时性能收集失败: " + e.getMessage());
        }
    }
    
    /**
     * 定时清理过期数据
     */
    @Scheduled(fixedRate = 3600000) // 每小时执行一次
    @Async
    public void scheduleDataCleanup() {
        try {
            // 清理1小时前的数据
            LocalDateTime oneHourAgo = LocalDateTime.now().minusHours(1);
            systemMonitorDomainService.cleanupExpiredMetrics(oneHourAgo);
        } catch (Exception e) {
            System.err.println("定时数据清理失败: " + e.getMessage());
        }
    }
    
    /**
     * 辅助方法：添加指标到Map
     */
    private void addMetricToMap(Map<String, Object> map, String key, 
            Map<String, MetricValue> metrics, String metricName) {
        MetricValue metric = metrics.get(metricName);
        if (metric != null) {
            map.put(key, Map.of(
                    "value", metric.getValue(),
                    "unit", metric.getUnit(),
                    "timestamp", metric.getTimestamp().toString()
            ));
        }
    }
    
    /**
     * 辅助方法：获取指标历史数据
     */
    private List<Map<String, Object>> getMetricHistoryData(String metricName, int minutes) {
        List<MetricValue> history = systemMonitorDomainService.getMetricHistory(instanceId, 
                metricName, minutes * 2); // 假设每30秒一个数据点
        
        List<Map<String, Object>> data = new ArrayList<>();
        for (MetricValue metric : history) {
            Map<String, Object> point = new HashMap<>();
            point.put("timestamp", metric.getTimestamp().toString());
            point.put("value", metric.getValue());
            point.put("unit", metric.getUnit());
            data.add(point);
        }
        
        return data;
    }
    
    /**
     * 辅助方法：获取指标值
     */
    private double getMetricValue(Map<String, MetricValue> metrics, String metricName, double defaultValue) {
        MetricValue metric = metrics.get(metricName);
        return metric != null ? metric.getValue() : defaultValue;
    }
    
    /**
     * 计算性能评级
     */
    private String calculatePerformanceGrade(double cpuUsage, double memoryUsage, double diskUsage) {
        double averageUsage = (cpuUsage + memoryUsage + diskUsage) / 3;
        
        if (averageUsage < 40) return "Excellent";
        if (averageUsage < 60) return "Good";
        if (averageUsage < 80) return "Fair";
        return "Poor";
    }
    
    /**
     * 创建告警信息
     */
    private Map<String, Object> createAlert(String message, String metricName, double threshold, String level) {
        Map<String, Object> alert = new HashMap<>();
        alert.put("message", message);
        alert.put("metricName", metricName);
        alert.put("threshold", threshold);
        alert.put("level", level);
        alert.put("timestamp", LocalDateTime.now().toString());
        
        MetricValue currentValue = systemMonitorDomainService.getCurrentMetricValue(instanceId, metricName);
        if (currentValue != null) {
            alert.put("currentValue", currentValue.getValue());
            alert.put("unit", currentValue.getUnit());
        }
        
        return alert;
    }
    
    /**
     * 生成实例ID
     */
    private String generateInstanceId() {
        return "instance-" + System.currentTimeMillis() + "-" + 
               (int)(Math.random() * 1000);
    }
    
    public String getInstanceId() {
        return instanceId;
    }
}