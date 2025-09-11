package com.jiuxi.platform.monitoring.infrastructure.collector;

import com.jiuxi.platform.monitoring.domain.valueobject.MetricValue;
import org.springframework.stereotype.Component;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * CPU指标收集器
 * 
 * @author DDD Refactor
 * @date 2025-09-11
 */
@Component
public class CpuMetricsCollector implements MetricsCollector {
    
    private boolean enabled = true;
    private int collectionInterval = 30; // 30秒
    private final OperatingSystemMXBean osBean;
    
    public CpuMetricsCollector() {
        this.osBean = ManagementFactory.getOperatingSystemMXBean();
    }
    
    @Override
    public List<MetricValue> collectMetrics() {
        List<MetricValue> metrics = new ArrayList<>();
        
        if (!enabled) {
            return metrics;
        }
        
        try {
            // 获取CPU使用率
            double cpuUsage = getCpuUsage();
            if (cpuUsage >= 0) {
                metrics.add(new MetricValue("cpu.usage", cpuUsage, "%", LocalDateTime.now(), "system"));
            }
            
            // 获取系统负载平均值
            double loadAverage = osBean.getSystemLoadAverage();
            if (loadAverage >= 0) {
                metrics.add(new MetricValue("cpu.load.average", loadAverage, "", LocalDateTime.now(), "system"));
            }
            
            // 获取可用处理器数量
            int availableProcessors = osBean.getAvailableProcessors();
            metrics.add(new MetricValue("cpu.processors.available", (double) availableProcessors, "个", 
                    LocalDateTime.now(), "system"));
            
            // 计算负载使用率百分比
            if (loadAverage >= 0 && availableProcessors > 0) {
                double loadPercentage = (loadAverage / availableProcessors) * 100;
                metrics.add(new MetricValue("cpu.load.percentage", loadPercentage, "%", 
                        LocalDateTime.now(), "system"));
            }
            
        } catch (Exception e) {
            // 记录错误但不抛出异常，保证其他收集器能正常工作
            System.err.println("CPU指标收集失败: " + e.getMessage());
        }
        
        return metrics;
    }
    
    /**
     * 获取CPU使用率
     * 使用反射调用操作系统相关的方法
     */
    private double getCpuUsage() {
        try {
            // 尝试获取系统CPU使用率
            if (osBean instanceof com.sun.management.OperatingSystemMXBean) {
                com.sun.management.OperatingSystemMXBean sunOsBean = 
                        (com.sun.management.OperatingSystemMXBean) osBean;
                
                // 获取系统CPU使用率
                double systemCpuLoad = sunOsBean.getSystemCpuLoad();
                if (systemCpuLoad >= 0) {
                    return systemCpuLoad * 100; // 转换为百分比
                }
                
                // 如果系统CPU负载不可用，尝试获取进程CPU使用率
                double processCpuLoad = sunOsBean.getProcessCpuLoad();
                if (processCpuLoad >= 0) {
                    return processCpuLoad * 100; // 转换为百分比
                }
            }
            
            // 如果上述方法都不可用，使用负载平均值估算
            double loadAverage = osBean.getSystemLoadAverage();
            int processors = osBean.getAvailableProcessors();
            if (loadAverage >= 0 && processors > 0) {
                return Math.min((loadAverage / processors) * 100, 100.0);
            }
            
        } catch (Exception e) {
            System.err.println("获取CPU使用率失败: " + e.getMessage());
        }
        
        return -1; // 表示无法获取
    }
    
    @Override
    public String getCollectorName() {
        return "CpuMetricsCollector";
    }
    
    @Override
    public boolean isEnabled() {
        return enabled;
    }
    
    @Override
    public void enable() {
        this.enabled = true;
    }
    
    @Override
    public void disable() {
        this.enabled = false;
    }
    
    @Override
    public int getCollectionInterval() {
        return collectionInterval;
    }
    
    /**
     * 设置收集间隔
     */
    public void setCollectionInterval(int intervalSeconds) {
        if (intervalSeconds > 0) {
            this.collectionInterval = intervalSeconds;
        }
    }
}