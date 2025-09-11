package com.jiuxi.platform.monitoring.domain.service;

import com.jiuxi.platform.monitoring.domain.entity.MetricsRecord;
import com.jiuxi.platform.monitoring.domain.repository.MetricsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 监控指标归档服务
 * 负责数据压缩、聚合和归档管理
 * 
 * @author DDD Refactor
 * @date 2025-09-11
 */
@Service
public class MetricsArchiveService {
    
    private final MetricsRepository metricsRepository;
    
    // 数据保留策略配置
    private final DataRetentionPolicy retentionPolicy = new DataRetentionPolicy();
    
    @Autowired
    public MetricsArchiveService(MetricsRepository metricsRepository) {
        this.metricsRepository = metricsRepository;
    }
    
    /**
     * 执行数据归档
     */
    @Scheduled(cron = "0 0 2 * * ?") // 每天凌晨2点执行
    @Async
    public void performDataArchiving() {
        try {
            System.out.println("开始执行监控数据归档任务...");
            
            LocalDateTime now = LocalDateTime.now();
            List<String> instanceIds = metricsRepository.getAllInstanceIds();
            
            for (String instanceId : instanceIds) {
                archiveInstanceData(instanceId, now);
            }
            
            // 清理过期数据
            cleanupExpiredData(now);
            
            System.out.println("监控数据归档任务完成");
            
        } catch (Exception e) {
            System.err.println("数据归档失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * 手动触发数据归档
     */
    public ArchiveResult manualArchive(String instanceId) {
        ArchiveResult result = new ArchiveResult();
        
        try {
            LocalDateTime now = LocalDateTime.now();
            
            if (instanceId != null && !instanceId.trim().isEmpty()) {
                // 归档指定实例
                result = archiveInstanceData(instanceId, now);
            } else {
                // 归档所有实例
                List<String> instanceIds = metricsRepository.getAllInstanceIds();
                for (String id : instanceIds) {
                    ArchiveResult instanceResult = archiveInstanceData(id, now);
                    result.merge(instanceResult);
                }
            }
            
            result.setSuccess(true);
            
        } catch (Exception e) {
            result.setSuccess(false);
            result.setErrorMessage("归档失败: " + e.getMessage());
        }
        
        return result;
    }
    
    /**
     * 归档指定实例的数据
     */
    private ArchiveResult archiveInstanceData(String instanceId, LocalDateTime now) {
        ArchiveResult result = new ArchiveResult();
        
        List<String> metricNames = metricsRepository.getMetricNames(instanceId);
        
        for (String metricName : metricNames) {
            try {
                // 小时级聚合 (保留最近7天的原始数据)
                if (shouldPerformHourlyAggregation(now)) {
                    int hourlyAggregated = performHourlyAggregation(instanceId, metricName, now);
                    result.addHourlyAggregated(hourlyAggregated);
                }
                
                // 日级聚合 (保留最近30天的小时数据)
                if (shouldPerformDailyAggregation(now)) {
                    int dailyAggregated = performDailyAggregation(instanceId, metricName, now);
                    result.addDailyAggregated(dailyAggregated);
                }
                
            } catch (Exception e) {
                result.addError("归档指标 " + metricName + " 失败: " + e.getMessage());
            }
        }
        
        return result;
    }
    
    /**
     * 执行小时级聚合
     */
    private int performHourlyAggregation(String instanceId, String metricName, LocalDateTime now) {
        LocalDateTime startTime = now.minusDays(retentionPolicy.getRawDataRetentionDays()).truncatedTo(ChronoUnit.HOURS);
        LocalDateTime endTime = now.truncatedTo(ChronoUnit.HOURS);
        
        int aggregatedCount = 0;
        
        // 按小时聚合数据
        for (LocalDateTime hourStart = startTime; hourStart.isBefore(endTime); hourStart = hourStart.plusHours(1)) {
            LocalDateTime hourEnd = hourStart.plusHours(1);
            
            List<MetricsRecord> rawRecords = metricsRepository.findRawRecords(
                    instanceId, metricName, hourStart, hourEnd, Integer.MAX_VALUE);
            
            if (!rawRecords.isEmpty()) {
                List<MetricsRecord> aggregatedRecords = createHourlyAggregatedRecords(
                        instanceId, metricName, rawRecords, hourStart, hourEnd);
                
                if (!aggregatedRecords.isEmpty()) {
                    metricsRepository.batchSave(aggregatedRecords);
                    aggregatedCount += aggregatedRecords.size();
                }
            }
        }
        
        return aggregatedCount;
    }
    
    /**
     * 执行日级聚合
     */
    private int performDailyAggregation(String instanceId, String metricName, LocalDateTime now) {
        LocalDateTime startTime = now.minusDays(retentionPolicy.getHourlyDataRetentionDays()).truncatedTo(ChronoUnit.DAYS);
        LocalDateTime endTime = now.truncatedTo(ChronoUnit.DAYS);
        
        int aggregatedCount = 0;
        
        // 按天聚合小时数据
        for (LocalDateTime dayStart = startTime; dayStart.isBefore(endTime); dayStart = dayStart.plusDays(1)) {
            LocalDateTime dayEnd = dayStart.plusDays(1);
            
            List<MetricsRecord> hourlyRecords = metricsRepository.findAggregatedRecords(
                    instanceId, metricName, "AVG", 60, dayStart, dayEnd);
            
            if (!hourlyRecords.isEmpty()) {
                List<MetricsRecord> dailyAggregatedRecords = createDailyAggregatedRecords(
                        instanceId, metricName, hourlyRecords, dayStart, dayEnd);
                
                if (!dailyAggregatedRecords.isEmpty()) {
                    metricsRepository.batchSave(dailyAggregatedRecords);
                    aggregatedCount += dailyAggregatedRecords.size();
                }
            }
        }
        
        return aggregatedCount;
    }
    
    /**
     * 创建小时级聚合记录
     */
    private List<MetricsRecord> createHourlyAggregatedRecords(String instanceId, String metricName,
            List<MetricsRecord> rawRecords, LocalDateTime periodStart, LocalDateTime periodEnd) {
        
        List<MetricsRecord> aggregatedRecords = new ArrayList<>();
        
        if (rawRecords.isEmpty()) {
            return aggregatedRecords;
        }
        
        List<Double> values = rawRecords.stream()
                .map(MetricsRecord::getMetricValue)
                .collect(Collectors.toList());
        
        String unit = rawRecords.get(0).getUnit();
        String source = rawRecords.get(0).getSource();
        
        // 创建不同类型的聚合记录
        aggregatedRecords.add(MetricsRecord.createAggregatedRecord(
                instanceId, metricName, "AVG", calculateAverage(values), 
                unit, source, periodStart, periodEnd, 60));
        
        aggregatedRecords.add(MetricsRecord.createAggregatedRecord(
                instanceId, metricName, "MAX", calculateMax(values), 
                unit, source, periodStart, periodEnd, 60));
        
        aggregatedRecords.add(MetricsRecord.createAggregatedRecord(
                instanceId, metricName, "MIN", calculateMin(values), 
                unit, source, periodStart, periodEnd, 60));
        
        return aggregatedRecords;
    }
    
    /**
     * 创建日级聚合记录
     */
    private List<MetricsRecord> createDailyAggregatedRecords(String instanceId, String metricName,
            List<MetricsRecord> hourlyRecords, LocalDateTime periodStart, LocalDateTime periodEnd) {
        
        List<MetricsRecord> aggregatedRecords = new ArrayList<>();
        
        if (hourlyRecords.isEmpty()) {
            return aggregatedRecords;
        }
        
        // 按聚合类型分组处理
        Map<String, List<MetricsRecord>> groupedByType = hourlyRecords.stream()
                .collect(Collectors.groupingBy(MetricsRecord::getAggregationType));
        
        String unit = hourlyRecords.get(0).getUnit();
        String source = hourlyRecords.get(0).getSource();
        
        for (Map.Entry<String, List<MetricsRecord>> entry : groupedByType.entrySet()) {
            String aggregationType = entry.getKey();
            List<Double> values = entry.getValue().stream()
                    .map(MetricsRecord::getMetricValue)
                    .collect(Collectors.toList());
            
            double aggregatedValue;
            switch (aggregationType) {
                case "AVG":
                    aggregatedValue = calculateAverage(values);
                    break;
                case "MAX":
                    aggregatedValue = calculateMax(values);
                    break;
                case "MIN":
                    aggregatedValue = calculateMin(values);
                    break;
                default:
                    continue; // 跳过未知类型
            }
            
            aggregatedRecords.add(MetricsRecord.createAggregatedRecord(
                    instanceId, metricName, aggregationType, aggregatedValue, 
                    unit, source, periodStart, periodEnd, 1440)); // 1440分钟 = 1天
        }
        
        return aggregatedRecords;
    }
    
    /**
     * 清理过期数据
     */
    private void cleanupExpiredData(LocalDateTime now) {
        try {
            // 清理过期的原始数据
            LocalDateTime rawDataExpireTime = now.minusDays(retentionPolicy.getRawDataRetentionDays());
            int deletedRawRecords = metricsRepository.deleteExpiredRecords(rawDataExpireTime);
            
            // 清理过期的聚合数据
            LocalDateTime aggregatedDataExpireTime = now.minusDays(retentionPolicy.getAggregatedDataRetentionDays());
            // 注意：这里需要更精细的删除逻辑，只删除聚合数据
            
            System.out.println("清理过期数据完成，删除原始记录: " + deletedRawRecords + " 条");
            
        } catch (Exception e) {
            System.err.println("清理过期数据失败: " + e.getMessage());
        }
    }
    
    /**
     * 判断是否应该执行小时级聚合
     */
    private boolean shouldPerformHourlyAggregation(LocalDateTime now) {
        return now.getMinute() == 0; // 每小时的第0分钟执行
    }
    
    /**
     * 判断是否应该执行日级聚合
     */
    private boolean shouldPerformDailyAggregation(LocalDateTime now) {
        return now.getHour() == 1 && now.getMinute() == 0; // 每天凌晨1点执行
    }
    
    /**
     * 获取数据保留策略
     */
    public DataRetentionPolicy getRetentionPolicy() {
        return retentionPolicy;
    }
    
    /**
     * 获取归档统计信息
     */
    public Map<String, Object> getArchiveStatistics() {
        Map<String, Object> stats = new HashMap<>();
        
        try {
            List<String> instanceIds = metricsRepository.getAllInstanceIds();
            stats.put("totalInstances", instanceIds.size());
            stats.put("totalRecords", 0L); // 需要统计总记录数
            stats.put("storageSize", metricsRepository.getStorageSize());
            
            // 统计各实例的记录数
            Map<String, Long> instanceStats = new HashMap<>();
            long totalRecords = 0;
            
            for (String instanceId : instanceIds) {
                LocalDateTime now = LocalDateTime.now();
                LocalDateTime oneWeekAgo = now.minusDays(7);
                long recordCount = metricsRepository.countRecords(instanceId, oneWeekAgo, now);
                instanceStats.put(instanceId, recordCount);
                totalRecords += recordCount;
            }
            
            stats.put("totalRecords", totalRecords);
            stats.put("instanceStats", instanceStats);
            stats.put("retentionPolicy", retentionPolicy.toMap());
            
        } catch (Exception e) {
            stats.put("error", "获取统计信息失败: " + e.getMessage());
        }
        
        return stats;
    }
    
    // 数学计算辅助方法
    private double calculateAverage(List<Double> values) {
        return values.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
    }
    
    private double calculateMax(List<Double> values) {
        return values.stream().mapToDouble(Double::doubleValue).max().orElse(0.0);
    }
    
    private double calculateMin(List<Double> values) {
        return values.stream().mapToDouble(Double::doubleValue).min().orElse(0.0);
    }
    
    /**
     * 数据保留策略配置
     */
    public static class DataRetentionPolicy {
        private int rawDataRetentionDays = 7;          // 原始数据保留7天
        private int hourlyDataRetentionDays = 30;      // 小时聚合数据保留30天
        private int aggregatedDataRetentionDays = 365; // 聚合数据保留365天
        
        public int getRawDataRetentionDays() {
            return rawDataRetentionDays;
        }
        
        public void setRawDataRetentionDays(int rawDataRetentionDays) {
            this.rawDataRetentionDays = rawDataRetentionDays;
        }
        
        public int getHourlyDataRetentionDays() {
            return hourlyDataRetentionDays;
        }
        
        public void setHourlyDataRetentionDays(int hourlyDataRetentionDays) {
            this.hourlyDataRetentionDays = hourlyDataRetentionDays;
        }
        
        public int getAggregatedDataRetentionDays() {
            return aggregatedDataRetentionDays;
        }
        
        public void setAggregatedDataRetentionDays(int aggregatedDataRetentionDays) {
            this.aggregatedDataRetentionDays = aggregatedDataRetentionDays;
        }
        
        public Map<String, Object> toMap() {
            Map<String, Object> map = new HashMap<>();
            map.put("rawDataRetentionDays", rawDataRetentionDays);
            map.put("hourlyDataRetentionDays", hourlyDataRetentionDays);
            map.put("aggregatedDataRetentionDays", aggregatedDataRetentionDays);
            return map;
        }
    }
    
    /**
     * 归档结果
     */
    public static class ArchiveResult {
        private boolean success = false;
        private int hourlyAggregated = 0;
        private int dailyAggregated = 0;
        private List<String> errors = new ArrayList<>();
        private String errorMessage;
        
        public void addHourlyAggregated(int count) {
            this.hourlyAggregated += count;
        }
        
        public void addDailyAggregated(int count) {
            this.dailyAggregated += count;
        }
        
        public void addError(String error) {
            this.errors.add(error);
        }
        
        public void merge(ArchiveResult other) {
            this.hourlyAggregated += other.hourlyAggregated;
            this.dailyAggregated += other.dailyAggregated;
            this.errors.addAll(other.errors);
        }
        
        // Getters and Setters
        public boolean isSuccess() { return success; }
        public void setSuccess(boolean success) { this.success = success; }
        
        public int getHourlyAggregated() { return hourlyAggregated; }
        public int getDailyAggregated() { return dailyAggregated; }
        public List<String> getErrors() { return errors; }
        
        public String getErrorMessage() { return errorMessage; }
        public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
        
        @Override
        public String toString() {
            return String.format("ArchiveResult{success=%s, hourly=%d, daily=%d, errors=%d}", 
                    success, hourlyAggregated, dailyAggregated, errors.size());
        }
    }
}