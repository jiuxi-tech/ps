package com.jiuxi.platform.monitoring.infrastructure.collector;

import com.jiuxi.platform.monitoring.domain.valueobject.MetricValue;
import org.springframework.stereotype.Component;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 内存指标收集器
 * 
 * @author DDD Refactor
 * @date 2025-09-11
 */
@Component
public class MemoryMetricsCollector implements MetricsCollector {
    
    private boolean enabled = true;
    private int collectionInterval = 30; // 30秒
    private final MemoryMXBean memoryBean;
    
    public MemoryMetricsCollector() {
        this.memoryBean = ManagementFactory.getMemoryMXBean();
    }
    
    @Override
    public List<MetricValue> collectMetrics() {
        List<MetricValue> metrics = new ArrayList<>();
        
        if (!enabled) {
            return metrics;
        }
        
        try {
            LocalDateTime timestamp = LocalDateTime.now();
            
            // 堆内存使用情况
            MemoryUsage heapUsage = memoryBean.getHeapMemoryUsage();
            if (heapUsage != null) {
                // 已使用的堆内存 (MB)
                double heapUsedMB = heapUsage.getUsed() / (1024.0 * 1024.0);
                metrics.add(new MetricValue("memory.heap.used", heapUsedMB, "MB", timestamp, "jvm"));
                
                // 堆内存最大值 (MB)
                if (heapUsage.getMax() > 0) {
                    double heapMaxMB = heapUsage.getMax() / (1024.0 * 1024.0);
                    metrics.add(new MetricValue("memory.heap.max", heapMaxMB, "MB", timestamp, "jvm"));
                    
                    // 堆内存使用率 (%)
                    double heapUsagePercentage = (heapUsedMB / heapMaxMB) * 100;
                    metrics.add(new MetricValue("memory.heap.usage.percentage", heapUsagePercentage, "%", 
                            timestamp, "jvm"));
                }
                
                // 堆内存已提交 (MB)
                double heapCommittedMB = heapUsage.getCommitted() / (1024.0 * 1024.0);
                metrics.add(new MetricValue("memory.heap.committed", heapCommittedMB, "MB", timestamp, "jvm"));
            }
            
            // 非堆内存使用情况
            MemoryUsage nonHeapUsage = memoryBean.getNonHeapMemoryUsage();
            if (nonHeapUsage != null) {
                // 已使用的非堆内存 (MB)
                double nonHeapUsedMB = nonHeapUsage.getUsed() / (1024.0 * 1024.0);
                metrics.add(new MetricValue("memory.nonheap.used", nonHeapUsedMB, "MB", timestamp, "jvm"));
                
                // 非堆内存最大值 (MB)
                if (nonHeapUsage.getMax() > 0) {
                    double nonHeapMaxMB = nonHeapUsage.getMax() / (1024.0 * 1024.0);
                    metrics.add(new MetricValue("memory.nonheap.max", nonHeapMaxMB, "MB", timestamp, "jvm"));
                    
                    // 非堆内存使用率 (%)
                    double nonHeapUsagePercentage = (nonHeapUsedMB / nonHeapMaxMB) * 100;
                    metrics.add(new MetricValue("memory.nonheap.usage.percentage", nonHeapUsagePercentage, "%", 
                            timestamp, "jvm"));
                }
                
                // 非堆内存已提交 (MB)
                double nonHeapCommittedMB = nonHeapUsage.getCommitted() / (1024.0 * 1024.0);
                metrics.add(new MetricValue("memory.nonheap.committed", nonHeapCommittedMB, "MB", timestamp, "jvm"));
            }
            
            // 系统物理内存信息
            collectSystemMemoryMetrics(metrics, timestamp);
            
        } catch (Exception e) {
            System.err.println("内存指标收集失败: " + e.getMessage());
        }
        
        return metrics;
    }
    
    /**
     * 收集系统级内存指标
     */
    private void collectSystemMemoryMetrics(List<MetricValue> metrics, LocalDateTime timestamp) {
        try {
            // 尝试获取操作系统内存信息
            java.lang.management.OperatingSystemMXBean osBean = 
                    ManagementFactory.getOperatingSystemMXBean();
            
            if (osBean instanceof com.sun.management.OperatingSystemMXBean) {
                com.sun.management.OperatingSystemMXBean sunOsBean = 
                        (com.sun.management.OperatingSystemMXBean) osBean;
                
                // 总物理内存
                long totalPhysicalMemory = sunOsBean.getTotalPhysicalMemorySize();
                double totalPhysicalMemoryMB = 0;
                if (totalPhysicalMemory > 0) {
                    totalPhysicalMemoryMB = totalPhysicalMemory / (1024.0 * 1024.0);
                    metrics.add(new MetricValue("memory.physical.total", totalPhysicalMemoryMB, "MB", 
                            timestamp, "system"));
                }
                
                // 可用物理内存
                long freePhysicalMemory = sunOsBean.getFreePhysicalMemorySize();
                if (freePhysicalMemory >= 0) {
                    double freePhysicalMemoryMB = freePhysicalMemory / (1024.0 * 1024.0);
                    metrics.add(new MetricValue("memory.physical.free", freePhysicalMemoryMB, "MB", 
                            timestamp, "system"));
                    
                    // 物理内存使用率
                    if (totalPhysicalMemory > 0 && totalPhysicalMemoryMB > 0) {
                        double usedPhysicalMemoryMB = totalPhysicalMemoryMB - freePhysicalMemoryMB;
                        double physicalMemoryUsagePercentage = (usedPhysicalMemoryMB / totalPhysicalMemoryMB) * 100;
                        metrics.add(new MetricValue("memory.physical.used", usedPhysicalMemoryMB, "MB", 
                                timestamp, "system"));
                        metrics.add(new MetricValue("memory.physical.usage.percentage", physicalMemoryUsagePercentage, "%", 
                                timestamp, "system"));
                    }
                }
                
                // 虚拟内存信息
                long totalSwap = sunOsBean.getTotalSwapSpaceSize();
                double totalSwapMB = 0;
                if (totalSwap > 0) {
                    totalSwapMB = totalSwap / (1024.0 * 1024.0);
                    metrics.add(new MetricValue("memory.swap.total", totalSwapMB, "MB", timestamp, "system"));
                }
                
                long freeSwap = sunOsBean.getFreeSwapSpaceSize();
                if (freeSwap >= 0) {
                    double freeSwapMB = freeSwap / (1024.0 * 1024.0);
                    metrics.add(new MetricValue("memory.swap.free", freeSwapMB, "MB", timestamp, "system"));
                    
                    // Swap使用率
                    if (totalSwap > 0 && totalSwapMB > 0) {
                        double usedSwapMB = totalSwapMB - freeSwapMB;
                        double swapUsagePercentage = (usedSwapMB / totalSwapMB) * 100;
                        metrics.add(new MetricValue("memory.swap.used", usedSwapMB, "MB", timestamp, "system"));
                        metrics.add(new MetricValue("memory.swap.usage.percentage", swapUsagePercentage, "%", 
                                timestamp, "system"));
                    }
                }
            }
            
        } catch (Exception e) {
            System.err.println("系统内存指标收集失败: " + e.getMessage());
        }
    }
    
    @Override
    public String getCollectorName() {
        return "MemoryMetricsCollector";
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