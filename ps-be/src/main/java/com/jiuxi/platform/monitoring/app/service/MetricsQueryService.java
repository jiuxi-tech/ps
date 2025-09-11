package com.jiuxi.platform.monitoring.app.service;

import com.jiuxi.platform.monitoring.domain.entity.MetricsRecord;
import com.jiuxi.platform.monitoring.domain.repository.MetricsRepository;
import com.jiuxi.platform.monitoring.domain.service.MetricsArchiveService;
import com.jiuxi.platform.monitoring.domain.valueobject.MetricValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 监控数据查询服务
 * 提供监控数据的查询、聚合和统计功能
 * 
 * @author DDD Refactor
 * @date 2025-09-11
 */
@Service
public class MetricsQueryService {
    
    private final MetricsRepository metricsRepository;
    private final MetricsArchiveService archiveService;
    
    @Autowired
    public MetricsQueryService(MetricsRepository metricsRepository, MetricsArchiveService archiveService) {
        this.metricsRepository = metricsRepository;
        this.archiveService = archiveService;
    }
    
    /**
     * 查询实时指标数据
     */
    public Map<String, Object> queryRealTimeMetrics(String instanceId, List<String> metricNames) {
        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> metrics = new ArrayList<>();
        
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime fiveMinutesAgo = now.minusMinutes(5);
        
        for (String metricName : metricNames) {
            try {
                Optional<MetricsRecord> latestRecord = metricsRepository.findLatestRecord(instanceId, metricName);
                
                Map<String, Object> metricData = new HashMap<>();
                metricData.put("metricName", metricName);
                
                if (latestRecord.isPresent()) {
                    MetricsRecord record = latestRecord.get();
                    metricData.put("value", record.getMetricValue());
                    metricData.put("unit", record.getUnit());
                    metricData.put("timestamp", record.getTimestamp());
                    metricData.put("source", record.getSource());
                    
                    // 检查数据是否新鲜
                    boolean isFresh = record.getTimestamp().isAfter(fiveMinutesAgo);
                    metricData.put("fresh", isFresh);
                } else {
                    metricData.put("value", null);
                    metricData.put("fresh", false);
                    metricData.put("message", "无数据");
                }
                
                metrics.add(metricData);
                
            } catch (Exception e) {
                Map<String, Object> errorData = new HashMap<>();
                errorData.put("metricName", metricName);
                errorData.put("error", e.getMessage());
                metrics.add(errorData);
            }
        }
        
        result.put("instanceId", instanceId);
        result.put("metrics", metrics);
        result.put("queryTime", now);
        result.put("totalMetrics", metrics.size());
        
        return result;
    }
    
    /**
     * 查询历史指标数据
     */
    public Map<String, Object> queryHistoricalMetrics(String instanceId, String metricName, 
                                                     String startTime, String endTime, String resolution) {
        
        Map<String, Object> result = new HashMap<>();
        
        try {
            LocalDateTime start = parseTimeString(startTime);
            LocalDateTime end = parseTimeString(endTime);
            
            // 根据分辨率选择合适的查询策略
            List<MetricsRecord> records = selectOptimalDataSource(instanceId, metricName, start, end, resolution);
            
            // 转换为时间序列格式
            List<Map<String, Object>> timeSeries = convertToTimeSeries(records);
            
            result.put("instanceId", instanceId);
            result.put("metricName", metricName);
            result.put("startTime", start);
            result.put("endTime", end);
            result.put("resolution", resolution);
            result.put("dataPoints", timeSeries);
            result.put("totalDataPoints", timeSeries.size());
            
            // 添加统计信息
            if (!records.isEmpty()) {
                Map<String, Object> statistics = calculateStatistics(records);
                result.put("statistics", statistics);
            }
            
        } catch (Exception e) {
            result.put("error", "查询失败: " + e.getMessage());
        }
        
        return result;
    }
    
    /**
     * 查询多指标历史数据
     */
    public Map<String, Object> queryMultipleHistoricalMetrics(String instanceId, List<String> metricNames,
                                                              String startTime, String endTime, String resolution) {
        
        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> metricsData = new ArrayList<>();
        
        try {
            LocalDateTime start = parseTimeString(startTime);
            LocalDateTime end = parseTimeString(endTime);
            
            for (String metricName : metricNames) {
                Map<String, Object> metricResult = queryHistoricalMetrics(instanceId, metricName, startTime, endTime, resolution);
                metricsData.add(metricResult);
            }
            
            result.put("instanceId", instanceId);
            result.put("startTime", start);
            result.put("endTime", end);
            result.put("resolution", resolution);
            result.put("metrics", metricsData);
            result.put("totalMetrics", metricsData.size());
            
        } catch (Exception e) {
            result.put("error", "多指标查询失败: " + e.getMessage());
        }
        
        return result;
    }
    
    /**
     * 查询指标聚合数据
     */
    public Map<String, Object> queryAggregatedMetrics(String instanceId, String metricName,
                                                     String aggregationType, String timeWindow,
                                                     String startTime, String endTime) {
        
        Map<String, Object> result = new HashMap<>();
        
        try {
            LocalDateTime start = parseTimeString(startTime);
            LocalDateTime end = parseTimeString(endTime);
            
            // 解析时间窗口（如"1h", "1d"）
            Integer intervalMinutes = parseTimeWindow(timeWindow);
            
            List<MetricsRecord> aggregatedRecords;
            if (intervalMinutes != null) {
                // 查询预聚合数据
                aggregatedRecords = metricsRepository.findAggregatedRecords(
                        instanceId, metricName, aggregationType, intervalMinutes, start, end);
            } else {
                // 实时聚合原始数据
                aggregatedRecords = performRealTimeAggregation(instanceId, metricName, aggregationType, timeWindow, start, end);
            }
            
            List<Map<String, Object>> timeSeries = convertToTimeSeries(aggregatedRecords);
            
            result.put("instanceId", instanceId);
            result.put("metricName", metricName);
            result.put("aggregationType", aggregationType);
            result.put("timeWindow", timeWindow);
            result.put("startTime", start);
            result.put("endTime", end);
            result.put("dataPoints", timeSeries);
            result.put("totalDataPoints", timeSeries.size());
            
        } catch (Exception e) {
            result.put("error", "聚合查询失败: " + e.getMessage());
        }
        
        return result;
    }
    
    /**
     * 查询指标统计信息
     */
    public Map<String, Object> queryMetricStatistics(String instanceId, String metricName, String startTime, String endTime) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            LocalDateTime start = parseTimeString(startTime);
            LocalDateTime end = parseTimeString(endTime);
            
            Map<String, Object> statistics = metricsRepository.getMetricStatistics(instanceId, metricName, start, end);
            
            result.put("instanceId", instanceId);
            result.put("metricName", metricName);
            result.put("startTime", start);
            result.put("endTime", end);
            result.put("statistics", statistics);
            
            // 添加额外的统计信息
            List<MetricsRecord> records = metricsRepository.findByMetricName(instanceId, metricName, start, end);
            if (!records.isEmpty()) {
                result.put("dataQuality", calculateDataQuality(records, start, end));
            }
            
        } catch (Exception e) {
            result.put("error", "统计查询失败: " + e.getMessage());
        }
        
        return result;
    }
    
    /**
     * 查询实例概览
     */
    public Map<String, Object> queryInstanceOverview(String instanceId, String timeRange) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            LocalDateTime end = LocalDateTime.now();
            LocalDateTime start = calculateStartTimeFromRange(end, timeRange);
            
            List<String> metricNames = metricsRepository.getMetricNames(instanceId);
            List<Map<String, Object>> metricsSummary = new ArrayList<>();
            
            for (String metricName : metricNames) {
                Map<String, Object> summary = new HashMap<>();
                summary.put("metricName", metricName);
                
                // 获取最新值
                Optional<MetricsRecord> latest = metricsRepository.findLatestRecord(instanceId, metricName);
                if (latest.isPresent()) {
                    MetricsRecord record = latest.get();
                    summary.put("currentValue", record.getMetricValue());
                    summary.put("unit", record.getUnit());
                    summary.put("lastUpdated", record.getTimestamp());
                }
                
                // 获取统计信息
                Map<String, Object> stats = metricsRepository.getMetricStatistics(instanceId, metricName, start, end);
                summary.put("statistics", stats);
                
                metricsSummary.add(summary);
            }
            
            result.put("instanceId", instanceId);
            result.put("timeRange", timeRange);
            result.put("startTime", start);
            result.put("endTime", end);
            result.put("totalMetrics", metricNames.size());
            result.put("metrics", metricsSummary);
            
            // 添加存储信息
            result.put("storage", metricsRepository.getRepositoryInfo());
            
        } catch (Exception e) {
            result.put("error", "实例概览查询失败: " + e.getMessage());
        }
        
        return result;
    }
    
    /**
     * 查询所有实例列表
     */
    public Map<String, Object> queryAllInstances() {
        Map<String, Object> result = new HashMap<>();
        
        try {
            List<String> instanceIds = metricsRepository.getAllInstanceIds();
            List<Map<String, Object>> instances = new ArrayList<>();
            
            for (String instanceId : instanceIds) {
                Map<String, Object> instance = new HashMap<>();
                instance.put("instanceId", instanceId);
                
                // 获取指标数量
                List<String> metricNames = metricsRepository.getMetricNames(instanceId);
                instance.put("totalMetrics", metricNames.size());
                
                // 获取最近的活动时间
                LocalDateTime now = LocalDateTime.now();
                LocalDateTime oneHourAgo = now.minusHours(1);
                long recentRecords = metricsRepository.countRecords(instanceId, oneHourAgo, now);
                instance.put("recentActivity", recentRecords);
                instance.put("active", recentRecords > 0);
                
                instances.add(instance);
            }
            
            result.put("totalInstances", instanceIds.size());
            result.put("instances", instances);
            result.put("queryTime", LocalDateTime.now());
            
            // 添加全局统计
            Map<String, Object> globalStats = metricsRepository.getRepositoryInfo();
            result.put("globalStatistics", globalStats);
            
        } catch (Exception e) {
            result.put("error", "实例列表查询失败: " + e.getMessage());
        }
        
        return result;
    }
    
    /**
     * 根据数据量和时间范围选择最优数据源
     */
    private List<MetricsRecord> selectOptimalDataSource(String instanceId, String metricName, 
                                                       LocalDateTime start, LocalDateTime end, String resolution) {
        
        long durationHours = ChronoUnit.HOURS.between(start, end);
        
        // 根据时间范围和分辨率选择数据源
        if ("raw".equals(resolution) || durationHours <= 24) {
            // 短期查询使用原始数据
            return metricsRepository.findRawRecords(instanceId, metricName, start, end, 10000);
        } else if (durationHours <= 168) { // 7天内
            // 中期查询使用小时聚合数据
            return metricsRepository.findAggregatedRecords(instanceId, metricName, "AVG", 60, start, end);
        } else {
            // 长期查询使用日聚合数据
            return metricsRepository.findAggregatedRecords(instanceId, metricName, "AVG", 1440, start, end);
        }
    }
    
    /**
     * 执行实时聚合
     */
    private List<MetricsRecord> performRealTimeAggregation(String instanceId, String metricName,
                                                          String aggregationType, String timeWindow,
                                                          LocalDateTime start, LocalDateTime end) {
        
        List<MetricsRecord> rawRecords = metricsRepository.findRawRecords(instanceId, metricName, start, end, Integer.MAX_VALUE);
        
        if (rawRecords.isEmpty()) {
            return new ArrayList<>();
        }
        
        // 根据时间窗口分组聚合
        Integer windowMinutesObj = parseTimeWindow(timeWindow);
        int windowMinutes = windowMinutesObj != null ? windowMinutesObj : 60; // 默认1小时
        
        Map<LocalDateTime, List<MetricsRecord>> groupedRecords = new HashMap<>();
        
        for (MetricsRecord record : rawRecords) {
            LocalDateTime windowStart = truncateToWindow(record.getTimestamp(), windowMinutes);
            groupedRecords.computeIfAbsent(windowStart, k -> new ArrayList<>()).add(record);
        }
        
        List<MetricsRecord> aggregatedRecords = new ArrayList<>();
        
        for (Map.Entry<LocalDateTime, List<MetricsRecord>> entry : groupedRecords.entrySet()) {
            LocalDateTime windowStart = entry.getKey();
            List<MetricsRecord> windowRecords = entry.getValue();
            
            if (!windowRecords.isEmpty()) {
                List<Double> values = windowRecords.stream()
                        .map(MetricsRecord::getMetricValue)
                        .collect(Collectors.toList());
                
                double aggregatedValue = performAggregation(values, aggregationType);
                
                MetricsRecord aggregatedRecord = MetricsRecord.createAggregatedRecord(
                        instanceId, metricName, aggregationType, aggregatedValue,
                        windowRecords.get(0).getUnit(), windowRecords.get(0).getSource(),
                        windowStart, windowStart.plusMinutes(windowMinutes), windowMinutes);
                
                aggregatedRecords.add(aggregatedRecord);
            }
        }
        
        aggregatedRecords.sort(Comparator.comparing(MetricsRecord::getTimestamp));
        return aggregatedRecords;
    }
    
    /**
     * 转换为时间序列格式
     */
    private List<Map<String, Object>> convertToTimeSeries(List<MetricsRecord> records) {
        return records.stream().map(record -> {
            Map<String, Object> dataPoint = new HashMap<>();
            dataPoint.put("timestamp", record.getTimestamp());
            dataPoint.put("value", record.getMetricValue());
            dataPoint.put("unit", record.getUnit());
            
            if (record.isAggregatedRecord()) {
                dataPoint.put("aggregationType", record.getAggregationType());
                dataPoint.put("interval", record.getAggregationInterval());
            }
            
            return dataPoint;
        }).collect(Collectors.toList());
    }
    
    /**
     * 计算统计信息
     */
    private Map<String, Object> calculateStatistics(List<MetricsRecord> records) {
        List<Double> values = records.stream()
                .map(MetricsRecord::getMetricValue)
                .collect(Collectors.toList());
        
        Map<String, Object> stats = new HashMap<>();
        if (!values.isEmpty()) {
            stats.put("count", values.size());
            stats.put("min", values.stream().min(Double::compare).orElse(0.0));
            stats.put("max", values.stream().max(Double::compare).orElse(0.0));
            stats.put("avg", values.stream().mapToDouble(Double::doubleValue).average().orElse(0.0));
            stats.put("sum", values.stream().mapToDouble(Double::doubleValue).sum());
        }
        return stats;
    }
    
    /**
     * 计算数据质量指标
     */
    private Map<String, Object> calculateDataQuality(List<MetricsRecord> records, LocalDateTime start, LocalDateTime end) {
        Map<String, Object> quality = new HashMap<>();
        
        if (records.isEmpty()) {
            quality.put("completeness", 0.0);
            quality.put("freshness", 0.0);
            return quality;
        }
        
        // 数据完整性（期望数据点数 vs 实际数据点数）
        long expectedPoints = ChronoUnit.MINUTES.between(start, end) / 5; // 假设5分钟一个数据点
        double completeness = Math.min(1.0, (double) records.size() / expectedPoints);
        quality.put("completeness", completeness);
        
        // 数据新鲜度（最新数据点的时间）
        Optional<MetricsRecord> latest = records.stream()
                .max(Comparator.comparing(MetricsRecord::getTimestamp));
        
        if (latest.isPresent()) {
            long minutesSinceLatest = ChronoUnit.MINUTES.between(latest.get().getTimestamp(), LocalDateTime.now());
            double freshness = Math.max(0.0, 1.0 - minutesSinceLatest / 60.0); // 1小时内为新鲜
            quality.put("freshness", freshness);
        }
        
        return quality;
    }
    
    // 辅助方法
    private LocalDateTime parseTimeString(String timeString) {
        if (timeString == null || timeString.trim().isEmpty()) {
            return LocalDateTime.now();
        }
        
        // 支持多种时间格式
        List<DateTimeFormatter> formatters = Arrays.asList(
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"),
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"),
                DateTimeFormatter.ofPattern("yyyy-MM-dd"),
                DateTimeFormatter.ISO_LOCAL_DATE_TIME
        );
        
        for (DateTimeFormatter formatter : formatters) {
            try {
                return LocalDateTime.parse(timeString, formatter);
            } catch (Exception ignored) {
            }
        }
        
        throw new IllegalArgumentException("无法解析时间格式: " + timeString);
    }
    
    private Integer parseTimeWindow(String timeWindow) {
        if (timeWindow == null) return null;
        
        timeWindow = timeWindow.toLowerCase().trim();
        
        if (timeWindow.endsWith("m")) {
            return Integer.parseInt(timeWindow.substring(0, timeWindow.length() - 1));
        } else if (timeWindow.endsWith("h")) {
            return Integer.parseInt(timeWindow.substring(0, timeWindow.length() - 1)) * 60;
        } else if (timeWindow.endsWith("d")) {
            return Integer.parseInt(timeWindow.substring(0, timeWindow.length() - 1)) * 1440;
        }
        
        // 默认返回null，表示需要实时聚合
        return null;
    }
    
    private LocalDateTime calculateStartTimeFromRange(LocalDateTime end, String timeRange) {
        switch (timeRange.toLowerCase()) {
            case "1h": return end.minusHours(1);
            case "6h": return end.minusHours(6);
            case "12h": return end.minusHours(12);
            case "24h":
            case "1d": return end.minusDays(1);
            case "7d": return end.minusDays(7);
            case "30d": return end.minusDays(30);
            default: return end.minusHours(1);
        }
    }
    
    private LocalDateTime truncateToWindow(LocalDateTime dateTime, int windowMinutes) {
        int totalMinutes = dateTime.getHour() * 60 + dateTime.getMinute();
        int windowStart = (totalMinutes / windowMinutes) * windowMinutes;
        
        return dateTime.truncatedTo(ChronoUnit.DAYS)
                .plusMinutes(windowStart);
    }
    
    private double performAggregation(List<Double> values, String aggregationType) {
        switch (aggregationType.toUpperCase()) {
            case "AVG":
                return values.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
            case "MAX":
                return values.stream().mapToDouble(Double::doubleValue).max().orElse(0.0);
            case "MIN":
                return values.stream().mapToDouble(Double::doubleValue).min().orElse(0.0);
            case "SUM":
                return values.stream().mapToDouble(Double::doubleValue).sum();
            case "COUNT":
                return values.size();
            default:
                return values.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
        }
    }
}