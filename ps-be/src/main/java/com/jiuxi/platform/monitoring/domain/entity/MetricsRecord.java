package com.jiuxi.platform.monitoring.domain.entity;

import com.jiuxi.platform.monitoring.domain.valueobject.MetricValue;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 监控指标记录实体
 * 用于持久化存储的指标数据记录
 * 
 * @author DDD Refactor
 * @date 2025-09-11
 */
public class MetricsRecord {
    
    private String recordId;
    private String instanceId;
    private String metricName;
    private Double metricValue;
    private String unit;
    private String source;
    private LocalDateTime timestamp;
    private LocalDateTime createTime;
    
    // 用于数据聚合的字段
    private String aggregationType; // AVG, MAX, MIN, SUM, COUNT
    private Integer aggregationInterval; // 聚合间隔（分钟）
    private LocalDateTime periodStart; // 聚合周期开始时间
    private LocalDateTime periodEnd;   // 聚合周期结束时间
    
    public MetricsRecord() {
        this.createTime = LocalDateTime.now();
    }
    
    public MetricsRecord(String instanceId, MetricValue metricValue) {
        this();
        this.recordId = generateRecordId(instanceId, metricValue);
        this.instanceId = instanceId;
        this.metricName = metricValue.getName();
        this.metricValue = metricValue.getValue();
        this.unit = metricValue.getUnit();
        this.source = metricValue.getSource();
        this.timestamp = metricValue.getTimestamp();
    }
    
    /**
     * 创建聚合记录
     */
    public static MetricsRecord createAggregatedRecord(String instanceId, String metricName, 
            String aggregationType, Double aggregatedValue, String unit, String source,
            LocalDateTime periodStart, LocalDateTime periodEnd, Integer interval) {
        
        MetricsRecord record = new MetricsRecord();
        record.recordId = generateAggregatedRecordId(instanceId, metricName, aggregationType, periodStart);
        record.instanceId = instanceId;
        record.metricName = metricName;
        record.metricValue = aggregatedValue;
        record.unit = unit;
        record.source = source;
        record.timestamp = periodEnd;
        record.aggregationType = aggregationType;
        record.aggregationInterval = interval;
        record.periodStart = periodStart;
        record.periodEnd = periodEnd;
        
        return record;
    }
    
    /**
     * 判断是否为聚合记录
     */
    public boolean isAggregatedRecord() {
        return aggregationType != null && aggregationInterval != null;
    }
    
    /**
     * 判断是否为原始记录
     */
    public boolean isRawRecord() {
        return !isAggregatedRecord();
    }
    
    /**
     * 获取时间戳的小时部分
     */
    public int getTimestampHour() {
        return timestamp.getHour();
    }
    
    /**
     * 获取时间戳的分钟部分
     */
    public int getTimestampMinute() {
        return timestamp.getMinute();
    }
    
    /**
     * 判断记录是否过期
     */
    public boolean isExpired(LocalDateTime expireTime) {
        return timestamp.isBefore(expireTime);
    }
    
    /**
     * 转换为MetricValue
     */
    public MetricValue toMetricValue() {
        return new MetricValue(metricName, metricValue, unit, timestamp, source);
    }
    
    /**
     * 生成记录ID
     */
    private String generateRecordId(String instanceId, MetricValue metricValue) {
        return String.format("%s_%s_%s", 
                instanceId, 
                metricValue.getName().replaceAll("\\.", "_"),
                metricValue.getTimestamp().toString().replaceAll("[:-]", ""));
    }
    
    /**
     * 生成聚合记录ID
     */
    private static String generateAggregatedRecordId(String instanceId, String metricName, 
            String aggregationType, LocalDateTime periodStart) {
        return String.format("%s_%s_%s_%s", 
                instanceId, 
                metricName.replaceAll("\\.", "_"),
                aggregationType.toLowerCase(),
                periodStart.toString().replaceAll("[:-]", ""));
    }
    
    // Getters and Setters
    public String getRecordId() {
        return recordId;
    }
    
    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }
    
    public String getInstanceId() {
        return instanceId;
    }
    
    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }
    
    public String getMetricName() {
        return metricName;
    }
    
    public void setMetricName(String metricName) {
        this.metricName = metricName;
    }
    
    public Double getMetricValue() {
        return metricValue;
    }
    
    public void setMetricValue(Double metricValue) {
        this.metricValue = metricValue;
    }
    
    public String getUnit() {
        return unit;
    }
    
    public void setUnit(String unit) {
        this.unit = unit;
    }
    
    public String getSource() {
        return source;
    }
    
    public void setSource(String source) {
        this.source = source;
    }
    
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
    
    public LocalDateTime getCreateTime() {
        return createTime;
    }
    
    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }
    
    public String getAggregationType() {
        return aggregationType;
    }
    
    public void setAggregationType(String aggregationType) {
        this.aggregationType = aggregationType;
    }
    
    public Integer getAggregationInterval() {
        return aggregationInterval;
    }
    
    public void setAggregationInterval(Integer aggregationInterval) {
        this.aggregationInterval = aggregationInterval;
    }
    
    public LocalDateTime getPeriodStart() {
        return periodStart;
    }
    
    public void setPeriodStart(LocalDateTime periodStart) {
        this.periodStart = periodStart;
    }
    
    public LocalDateTime getPeriodEnd() {
        return periodEnd;
    }
    
    public void setPeriodEnd(LocalDateTime periodEnd) {
        this.periodEnd = periodEnd;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MetricsRecord that = (MetricsRecord) o;
        return Objects.equals(recordId, that.recordId);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(recordId);
    }
    
    @Override
    public String toString() {
        return String.format("MetricsRecord{id='%s', instance='%s', metric='%s', value=%s%s, time=%s, aggregated=%s}", 
                recordId, instanceId, metricName, metricValue, unit, timestamp, isAggregatedRecord());
    }
}