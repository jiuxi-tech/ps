package com.jiuxi.platform.monitoring.infrastructure.collector;

import com.jiuxi.platform.monitoring.domain.valueobject.MetricValue;
import org.springframework.stereotype.Component;

import java.lang.management.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * JVM指标收集器
 * 
 * @author DDD Refactor
 * @date 2025-09-11
 */
@Component
public class JvmMetricsCollector implements MetricsCollector {
    
    private boolean enabled = true;
    private int collectionInterval = 30; // 30秒
    
    private final RuntimeMXBean runtimeBean;
    private final ThreadMXBean threadBean;
    private final ClassLoadingMXBean classLoadingBean;
    private final CompilationMXBean compilationBean;
    private final List<GarbageCollectorMXBean> gcBeans;
    private final List<MemoryPoolMXBean> memoryPoolBeans;
    
    public JvmMetricsCollector() {
        this.runtimeBean = ManagementFactory.getRuntimeMXBean();
        this.threadBean = ManagementFactory.getThreadMXBean();
        this.classLoadingBean = ManagementFactory.getClassLoadingMXBean();
        this.compilationBean = ManagementFactory.getCompilationMXBean();
        this.gcBeans = ManagementFactory.getGarbageCollectorMXBeans();
        this.memoryPoolBeans = ManagementFactory.getMemoryPoolMXBeans();
    }
    
    @Override
    public List<MetricValue> collectMetrics() {
        List<MetricValue> metrics = new ArrayList<>();
        
        if (!enabled) {
            return metrics;
        }
        
        try {
            LocalDateTime timestamp = LocalDateTime.now();
            
            // 收集运行时指标
            collectRuntimeMetrics(metrics, timestamp);
            
            // 收集线程指标
            collectThreadMetrics(metrics, timestamp);
            
            // 收集类加载指标
            collectClassLoadingMetrics(metrics, timestamp);
            
            // 收集编译指标
            collectCompilationMetrics(metrics, timestamp);
            
            // 收集垃圾回收指标
            collectGarbageCollectionMetrics(metrics, timestamp);
            
            // 收集内存池指标
            collectMemoryPoolMetrics(metrics, timestamp);
            
        } catch (Exception e) {
            System.err.println("JVM指标收集失败: " + e.getMessage());
        }
        
        return metrics;
    }
    
    /**
     * 收集运行时指标
     */
    private void collectRuntimeMetrics(List<MetricValue> metrics, LocalDateTime timestamp) {
        try {
            // JVM运行时间 (秒)
            long uptimeMs = runtimeBean.getUptime();
            double uptimeSeconds = uptimeMs / 1000.0;
            metrics.add(new MetricValue("jvm.runtime.uptime", uptimeSeconds, "秒", timestamp, "jvm"));
            
            // JVM启动时间
            long startTime = runtimeBean.getStartTime();
            metrics.add(new MetricValue("jvm.runtime.start.time", (double) startTime, "timestamp", 
                    timestamp, "jvm"));
            
            // 系统属性
            String javaVersion = System.getProperty("java.version");
            String javaVendor = System.getProperty("java.vendor");
            String osName = System.getProperty("os.name");
            String osVersion = System.getProperty("os.version");
            
            // 注意：这里将字符串转换为数值，实际使用时可能需要不同的处理方式
            metrics.add(new MetricValue("jvm.runtime.java.version.hash", (double) javaVersion.hashCode(), 
                    "hash", timestamp, "jvm"));
            
        } catch (Exception e) {
            System.err.println("收集运行时指标失败: " + e.getMessage());
        }
    }
    
    /**
     * 收集线程指标
     */
    private void collectThreadMetrics(List<MetricValue> metrics, LocalDateTime timestamp) {
        try {
            // 当前线程数
            int threadCount = threadBean.getThreadCount();
            metrics.add(new MetricValue("jvm.threads.count", (double) threadCount, "个", timestamp, "jvm"));
            
            // 守护线程数
            int daemonThreadCount = threadBean.getDaemonThreadCount();
            metrics.add(new MetricValue("jvm.threads.daemon.count", (double) daemonThreadCount, "个", 
                    timestamp, "jvm"));
            
            // 峰值线程数
            int peakThreadCount = threadBean.getPeakThreadCount();
            metrics.add(new MetricValue("jvm.threads.peak.count", (double) peakThreadCount, "个", 
                    timestamp, "jvm"));
            
            // 总启动线程数
            long totalStartedThreadCount = threadBean.getTotalStartedThreadCount();
            metrics.add(new MetricValue("jvm.threads.total.started.count", (double) totalStartedThreadCount, 
                    "个", timestamp, "jvm"));
            
            // 死锁线程检查
            long[] deadlockedThreads = threadBean.findDeadlockedThreads();
            int deadlockedThreadCount = deadlockedThreads != null ? deadlockedThreads.length : 0;
            metrics.add(new MetricValue("jvm.threads.deadlocked.count", (double) deadlockedThreadCount, 
                    "个", timestamp, "jvm"));
            
        } catch (Exception e) {
            System.err.println("收集线程指标失败: " + e.getMessage());
        }
    }
    
    /**
     * 收集类加载指标
     */
    private void collectClassLoadingMetrics(List<MetricValue> metrics, LocalDateTime timestamp) {
        try {
            // 当前加载的类数量
            int loadedClassCount = classLoadingBean.getLoadedClassCount();
            metrics.add(new MetricValue("jvm.classes.loaded.count", (double) loadedClassCount, "个", 
                    timestamp, "jvm"));
            
            // 总加载的类数量
            long totalLoadedClassCount = classLoadingBean.getTotalLoadedClassCount();
            metrics.add(new MetricValue("jvm.classes.total.loaded.count", (double) totalLoadedClassCount, 
                    "个", timestamp, "jvm"));
            
            // 卸载的类数量
            long unloadedClassCount = classLoadingBean.getUnloadedClassCount();
            metrics.add(new MetricValue("jvm.classes.unloaded.count", (double) unloadedClassCount, 
                    "个", timestamp, "jvm"));
            
        } catch (Exception e) {
            System.err.println("收集类加载指标失败: " + e.getMessage());
        }
    }
    
    /**
     * 收集编译指标
     */
    private void collectCompilationMetrics(List<MetricValue> metrics, LocalDateTime timestamp) {
        try {
            if (compilationBean != null) {
                // 编译器名称
                String compilerName = compilationBean.getName();
                
                // 编译总时间
                if (compilationBean.isCompilationTimeMonitoringSupported()) {
                    long totalCompilationTime = compilationBean.getTotalCompilationTime();
                    metrics.add(new MetricValue("jvm.compilation.total.time", (double) totalCompilationTime, 
                            "毫秒", timestamp, "jvm"));
                }
            }
            
        } catch (Exception e) {
            System.err.println("收集编译指标失败: " + e.getMessage());
        }
    }
    
    /**
     * 收集垃圾回收指标
     */
    private void collectGarbageCollectionMetrics(List<MetricValue> metrics, LocalDateTime timestamp) {
        try {
            long totalGcCollections = 0;
            long totalGcTime = 0;
            
            for (GarbageCollectorMXBean gcBean : gcBeans) {
                String gcName = gcBean.getName().toLowerCase().replaceAll("\\s+", ".");
                
                // GC收集次数
                long collectionCount = gcBean.getCollectionCount();
                if (collectionCount >= 0) {
                    metrics.add(new MetricValue("jvm.gc." + gcName + ".count", (double) collectionCount, 
                            "次", timestamp, "jvm"));
                    totalGcCollections += collectionCount;
                }
                
                // GC收集时间
                long collectionTime = gcBean.getCollectionTime();
                if (collectionTime >= 0) {
                    metrics.add(new MetricValue("jvm.gc." + gcName + ".time", (double) collectionTime, 
                            "毫秒", timestamp, "jvm"));
                    totalGcTime += collectionTime;
                }
            }
            
            // 总GC统计
            metrics.add(new MetricValue("jvm.gc.total.count", (double) totalGcCollections, "次", 
                    timestamp, "jvm"));
            metrics.add(new MetricValue("jvm.gc.total.time", (double) totalGcTime, "毫秒", 
                    timestamp, "jvm"));
            
        } catch (Exception e) {
            System.err.println("收集垃圾回收指标失败: " + e.getMessage());
        }
    }
    
    /**
     * 收集内存池指标
     */
    private void collectMemoryPoolMetrics(List<MetricValue> metrics, LocalDateTime timestamp) {
        try {
            for (MemoryPoolMXBean poolBean : memoryPoolBeans) {
                String poolName = poolBean.getName().toLowerCase()
                        .replaceAll("\\s+", ".")
                        .replaceAll("[^a-zA-Z0-9.]", "");
                
                MemoryUsage usage = poolBean.getUsage();
                if (usage != null) {
                    // 内存池使用量 (MB)
                    double usedMB = usage.getUsed() / (1024.0 * 1024.0);
                    metrics.add(new MetricValue("jvm.memory.pool." + poolName + ".used", usedMB, 
                            "MB", timestamp, "jvm"));
                    
                    // 内存池最大值 (MB)
                    if (usage.getMax() > 0) {
                        double maxMB = usage.getMax() / (1024.0 * 1024.0);
                        metrics.add(new MetricValue("jvm.memory.pool." + poolName + ".max", maxMB, 
                                "MB", timestamp, "jvm"));
                        
                        // 内存池使用率 (%)
                        double usagePercentage = (usedMB / maxMB) * 100;
                        metrics.add(new MetricValue("jvm.memory.pool." + poolName + ".usage.percentage", 
                                usagePercentage, "%", timestamp, "jvm"));
                    }
                    
                    // 内存池已提交 (MB)
                    double committedMB = usage.getCommitted() / (1024.0 * 1024.0);
                    metrics.add(new MetricValue("jvm.memory.pool." + poolName + ".committed", committedMB, 
                            "MB", timestamp, "jvm"));
                }
            }
            
        } catch (Exception e) {
            System.err.println("收集内存池指标失败: " + e.getMessage());
        }
    }
    
    @Override
    public String getCollectorName() {
        return "JvmMetricsCollector";
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