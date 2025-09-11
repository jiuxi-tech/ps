package com.jiuxi.platform.monitoring.domain.valueobject;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 监控指标值对象
 * 
 * @author DDD Refactor
 * @date 2025-09-11
 */
public class MetricValue {
    
    private final String name;
    private final Double value;
    private final String unit;
    private final LocalDateTime timestamp;
    private final String source;
    
    public MetricValue(String name, Double value, String unit, LocalDateTime timestamp, String source) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("指标名称不能为空");
        }
        if (value == null) {
            throw new IllegalArgumentException("指标值不能为空");
        }
        
        this.name = name.trim();
        this.value = value;
        this.unit = unit != null ? unit.trim() : "";
        this.timestamp = timestamp != null ? timestamp : LocalDateTime.now();
        this.source = source != null ? source.trim() : "unknown";
    }
    
    /**
     * 创建CPU使用率指标
     */
    public static MetricValue createCpuUsage(double usage) {
        return new MetricValue("cpu.usage", usage, "%", LocalDateTime.now(), "system");
    }
    
    /**
     * 创建内存使用量指标
     */
    public static MetricValue createMemoryUsage(double usage) {
        return new MetricValue("memory.usage", usage, "MB", LocalDateTime.now(), "system");
    }
    
    /**
     * 创建磁盘使用量指标
     */
    public static MetricValue createDiskUsage(double usage) {
        return new MetricValue("disk.usage", usage, "%", LocalDateTime.now(), "system");
    }
    
    /**
     * 创建JVM堆内存指标
     */
    public static MetricValue createJvmHeapUsage(double usage) {
        return new MetricValue("jvm.heap.usage", usage, "MB", LocalDateTime.now(), "jvm");
    }
    
    /**
     * 判断是否为系统级指标
     */
    public boolean isSystemMetric() {
        return "system".equals(this.source);
    }
    
    /**
     * 判断是否为JVM指标
     */
    public boolean isJvmMetric() {
        return "jvm".equals(this.source);
    }
    
    /**
     * 判断是否为应用级指标
     */
    public boolean isApplicationMetric() {
        return "application".equals(this.source);
    }
    
    /**
     * 判断指标值是否超过阈值
     */
    public boolean exceedsThreshold(double threshold) {
        return this.value > threshold;
    }
    
    /**
     * 判断指标值是否低于阈值
     */
    public boolean belowThreshold(double threshold) {
        return this.value < threshold;
    }
    
    public String getName() {
        return name;
    }
    
    public Double getValue() {
        return value;
    }
    
    public String getUnit() {
        return unit;
    }
    
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    
    public String getSource() {
        return source;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MetricValue that = (MetricValue) o;
        return Objects.equals(name, that.name) && 
               Objects.equals(value, that.value) && 
               Objects.equals(unit, that.unit) && 
               Objects.equals(timestamp, that.timestamp) && 
               Objects.equals(source, that.source);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(name, value, unit, timestamp, source);
    }
    
    @Override
    public String toString() {
        return String.format("MetricValue{name='%s', value=%s%s, source='%s', timestamp=%s}", 
                name, value, unit, source, timestamp);
    }
}