package com.jiuxi.platform.monitoring.infrastructure.collector;

import com.jiuxi.platform.monitoring.domain.valueobject.MetricValue;
import org.springframework.stereotype.Component;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 磁盘指标收集器
 * 
 * @author DDD Refactor
 * @date 2025-09-11
 */
@Component
public class DiskMetricsCollector implements MetricsCollector {
    
    private boolean enabled = true;
    private int collectionInterval = 60; // 60秒，磁盘信息变化较慢
    
    @Override
    public List<MetricValue> collectMetrics() {
        List<MetricValue> metrics = new ArrayList<>();
        
        if (!enabled) {
            return metrics;
        }
        
        try {
            LocalDateTime timestamp = LocalDateTime.now();
            
            // 获取所有文件系统根目录
            File[] roots = File.listRoots();
            
            double totalDiskSpace = 0;
            double totalUsedSpace = 0;
            double totalFreeSpace = 0;
            
            for (File root : roots) {
                try {
                    String rootPath = root.getAbsolutePath();
                    
                    // 总空间 (GB)
                    long totalSpace = root.getTotalSpace();
                    double totalSpaceGB = totalSpace / (1024.0 * 1024.0 * 1024.0);
                    
                    // 可用空间 (GB)
                    long freeSpace = root.getFreeSpace();
                    double freeSpaceGB = freeSpace / (1024.0 * 1024.0 * 1024.0);
                    
                    // 已用空间 (GB)
                    long usedSpace = totalSpace - freeSpace;
                    double usedSpaceGB = usedSpace / (1024.0 * 1024.0 * 1024.0);
                    
                    // 使用率 (%)
                    double usagePercentage = totalSpace > 0 ? (double) usedSpace / totalSpace * 100 : 0;
                    
                    // 为每个磁盘分区添加指标
                    String diskPrefix = "disk." + sanitizePath(rootPath);
                    
                    metrics.add(new MetricValue(diskPrefix + ".total", totalSpaceGB, "GB", timestamp, "system"));
                    metrics.add(new MetricValue(diskPrefix + ".used", usedSpaceGB, "GB", timestamp, "system"));
                    metrics.add(new MetricValue(diskPrefix + ".free", freeSpaceGB, "GB", timestamp, "system"));
                    metrics.add(new MetricValue(diskPrefix + ".usage.percentage", usagePercentage, "%", 
                            timestamp, "system"));
                    
                    // 累加到总计
                    totalDiskSpace += totalSpaceGB;
                    totalUsedSpace += usedSpaceGB;
                    totalFreeSpace += freeSpaceGB;
                    
                } catch (Exception e) {
                    System.err.println("收集磁盘 " + root.getAbsolutePath() + " 指标失败: " + e.getMessage());
                }
            }
            
            // 添加总体磁盘指标
            if (totalDiskSpace > 0) {
                metrics.add(new MetricValue("disk.total", totalDiskSpace, "GB", timestamp, "system"));
                metrics.add(new MetricValue("disk.used", totalUsedSpace, "GB", timestamp, "system"));
                metrics.add(new MetricValue("disk.free", totalFreeSpace, "GB", timestamp, "system"));
                
                double totalUsagePercentage = (totalUsedSpace / totalDiskSpace) * 100;
                metrics.add(new MetricValue("disk.usage", totalUsagePercentage, "%", timestamp, "system"));
            }
            
            // 收集磁盘IO指标 (如果支持)
            collectDiskIOMetrics(metrics, timestamp);
            
        } catch (Exception e) {
            System.err.println("磁盘指标收集失败: " + e.getMessage());
        }
        
        return metrics;
    }
    
    /**
     * 收集磁盘IO指标
     * 注意：Java标准库不直接支持IO统计，这里提供接口框架
     */
    private void collectDiskIOMetrics(List<MetricValue> metrics, LocalDateTime timestamp) {
        try {
            // 在实际生产环境中，可以通过以下方式获取磁盘IO统计：
            // 1. 解析 /proc/diskstats (Linux)
            // 2. 使用 wmi 查询 (Windows)
            // 3. 集成第三方库如 oshi
            
            // 这里提供一个示例框架，实际实现需要根据操作系统特定实现
            
            // Linux示例: 可以读取 /proc/diskstats
            if (System.getProperty("os.name").toLowerCase().contains("linux")) {
                collectLinuxDiskIOMetrics(metrics, timestamp);
            }
            
            // Windows示例: 可以使用Performance Counters
            else if (System.getProperty("os.name").toLowerCase().contains("windows")) {
                collectWindowsDiskIOMetrics(metrics, timestamp);
            }
            
        } catch (Exception e) {
            System.err.println("磁盘IO指标收集失败: " + e.getMessage());
        }
    }
    
    /**
     * 收集Linux磁盘IO指标
     */
    private void collectLinuxDiskIOMetrics(List<MetricValue> metrics, LocalDateTime timestamp) {
        try {
            // 实现读取 /proc/diskstats 的逻辑
            // 这里只是提供框架，实际实现需要解析文件内容
            
            // 示例指标
            metrics.add(new MetricValue("disk.io.read.bytes", 0.0, "KB/s", timestamp, "system"));
            metrics.add(new MetricValue("disk.io.write.bytes", 0.0, "KB/s", timestamp, "system"));
            metrics.add(new MetricValue("disk.io.read.ops", 0.0, "ops/s", timestamp, "system"));
            metrics.add(new MetricValue("disk.io.write.ops", 0.0, "ops/s", timestamp, "system"));
            
        } catch (Exception e) {
            System.err.println("Linux磁盘IO指标收集失败: " + e.getMessage());
        }
    }
    
    /**
     * 收集Windows磁盘IO指标
     */
    private void collectWindowsDiskIOMetrics(List<MetricValue> metrics, LocalDateTime timestamp) {
        try {
            // 实现Windows Performance Counters查询的逻辑
            // 这里只是提供框架，实际实现需要使用JNI或第三方库
            
            // 示例指标
            metrics.add(new MetricValue("disk.io.read.bytes", 0.0, "KB/s", timestamp, "system"));
            metrics.add(new MetricValue("disk.io.write.bytes", 0.0, "KB/s", timestamp, "system"));
            metrics.add(new MetricValue("disk.io.read.ops", 0.0, "ops/s", timestamp, "system"));
            metrics.add(new MetricValue("disk.io.write.ops", 0.0, "ops/s", timestamp, "system"));
            
        } catch (Exception e) {
            System.err.println("Windows磁盘IO指标收集失败: " + e.getMessage());
        }
    }
    
    /**
     * 清理路径名称用于指标名称
     */
    private String sanitizePath(String path) {
        if (path == null) {
            return "unknown";
        }
        
        // 移除特殊字符，只保留字母、数字和短横线
        return path.replaceAll("[^a-zA-Z0-9]", "").toLowerCase();
    }
    
    /**
     * 检查磁盘空间是否不足
     */
    public boolean isDiskSpaceInSufficient(double thresholdPercentage) {
        try {
            File[] roots = File.listRoots();
            for (File root : roots) {
                long totalSpace = root.getTotalSpace();
                long freeSpace = root.getFreeSpace();
                
                if (totalSpace > 0) {
                    double usagePercentage = ((double) (totalSpace - freeSpace) / totalSpace) * 100;
                    if (usagePercentage > thresholdPercentage) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("检查磁盘空间失败: " + e.getMessage());
        }
        
        return false;
    }
    
    @Override
    public String getCollectorName() {
        return "DiskMetricsCollector";
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