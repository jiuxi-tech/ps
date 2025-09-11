package com.jiuxi.platform.monitoring.infrastructure.storage;

import com.jiuxi.platform.monitoring.domain.entity.MetricsRecord;
import com.jiuxi.platform.monitoring.domain.repository.MetricsRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

/**
 * 内存时序数据存储实现
 * 提供高性能的时序数据存储和查询功能
 * 
 * @author DDD Refactor
 * @date 2025-09-11
 */
@Repository
public class InMemoryMetricsRepository implements MetricsRepository {
    
    // 主存储：instance -> metric -> 时间序列记录列表
    private final Map<String, Map<String, List<MetricsRecord>>> storage = new ConcurrentHashMap<>();
    
    // 聚合数据存储：instance -> metric -> aggregationType_interval -> 记录列表
    private final Map<String, Map<String, Map<String, List<MetricsRecord>>>> aggregatedStorage = new ConcurrentHashMap<>();
    
    // 索引：recordId -> record
    private final Map<String, MetricsRecord> recordIndex = new ConcurrentHashMap<>();
    
    // 统计信息
    private volatile long totalRecords = 0;
    private volatile long totalStorageSize = 0;
    private final LocalDateTime createTime = LocalDateTime.now();
    
    @Override
    public void save(MetricsRecord record) {
        if (record == null) {
            return;
        }
        
        synchronized (this) {
            // 保存到主存储或聚合存储
            if (record.isAggregatedRecord()) {
                saveAggregatedRecord(record);
            } else {
                saveRawRecord(record);
            }
            
            // 更新索引
            recordIndex.put(record.getRecordId(), record);
            
            // 更新统计信息
            totalRecords++;
            totalStorageSize += estimateRecordSize(record);
        }
    }
    
    @Override
    public void batchSave(List<MetricsRecord> records) {
        if (records == null || records.isEmpty()) {
            return;
        }
        
        synchronized (this) {
            for (MetricsRecord record : records) {
                if (record.isAggregatedRecord()) {
                    saveAggregatedRecord(record);
                } else {
                    saveRawRecord(record);
                }
                
                recordIndex.put(record.getRecordId(), record);
                totalRecords++;
                totalStorageSize += estimateRecordSize(record);
            }
        }
    }
    
    @Override
    public Optional<MetricsRecord> findById(String recordId) {
        return Optional.ofNullable(recordIndex.get(recordId));
    }
    
    @Override
    public List<MetricsRecord> findByInstanceId(String instanceId, LocalDateTime startTime, LocalDateTime endTime) {
        Map<String, List<MetricsRecord>> instanceData = storage.get(instanceId);
        if (instanceData == null) {
            return new ArrayList<>();
        }
        
        List<MetricsRecord> results = new ArrayList<>();
        for (List<MetricsRecord> records : instanceData.values()) {
            results.addAll(filterByTimeRange(records, startTime, endTime));
        }
        
        // 按时间排序
        results.sort(Comparator.comparing(MetricsRecord::getTimestamp));
        return results;
    }
    
    @Override
    public List<MetricsRecord> findByMetricName(String instanceId, String metricName, 
                                               LocalDateTime startTime, LocalDateTime endTime) {
        Map<String, List<MetricsRecord>> instanceData = storage.get(instanceId);
        if (instanceData == null) {
            return new ArrayList<>();
        }
        
        List<MetricsRecord> records = instanceData.get(metricName);
        if (records == null) {
            return new ArrayList<>();
        }
        
        List<MetricsRecord> filtered = filterByTimeRange(records, startTime, endTime);
        filtered.sort(Comparator.comparing(MetricsRecord::getTimestamp));
        return filtered;
    }
    
    @Override
    public List<MetricsRecord> findRawRecords(String instanceId, String metricName, 
                                             LocalDateTime startTime, LocalDateTime endTime, int limit) {
        List<MetricsRecord> allRecords = findByMetricName(instanceId, metricName, startTime, endTime);
        
        // 过滤原始记录
        List<MetricsRecord> rawRecords = allRecords.stream()
                .filter(MetricsRecord::isRawRecord)
                .sorted(Comparator.comparing(MetricsRecord::getTimestamp).reversed())
                .limit(limit)
                .collect(Collectors.toList());
        
        // 重新按时间正序排列
        rawRecords.sort(Comparator.comparing(MetricsRecord::getTimestamp));
        return rawRecords;
    }
    
    @Override
    public List<MetricsRecord> findAggregatedRecords(String instanceId, String metricName, 
                                                    String aggregationType, Integer interval,
                                                    LocalDateTime startTime, LocalDateTime endTime) {
        Map<String, Map<String, List<MetricsRecord>>> instanceAggregated = aggregatedStorage.get(instanceId);
        if (instanceAggregated == null) {
            return new ArrayList<>();
        }
        
        Map<String, List<MetricsRecord>> metricAggregated = instanceAggregated.get(metricName);
        if (metricAggregated == null) {
            return new ArrayList<>();
        }
        
        String key = aggregationType + "_" + interval;
        List<MetricsRecord> records = metricAggregated.get(key);
        if (records == null) {
            return new ArrayList<>();
        }
        
        List<MetricsRecord> filtered = filterByTimeRange(records, startTime, endTime);
        filtered.sort(Comparator.comparing(MetricsRecord::getTimestamp));
        return filtered;
    }
    
    @Override
    public Optional<MetricsRecord> findLatestRecord(String instanceId, String metricName) {
        Map<String, List<MetricsRecord>> instanceData = storage.get(instanceId);
        if (instanceData == null) {
            return Optional.empty();
        }
        
        List<MetricsRecord> records = instanceData.get(metricName);
        if (records == null || records.isEmpty()) {
            return Optional.empty();
        }
        
        // 找到最新的原始记录
        return records.stream()
                .filter(MetricsRecord::isRawRecord)
                .max(Comparator.comparing(MetricsRecord::getTimestamp));
    }
    
    @Override
    public Map<String, Object> getMetricStatistics(String instanceId, String metricName, 
                                                  LocalDateTime startTime, LocalDateTime endTime) {
        List<MetricsRecord> records = findByMetricName(instanceId, metricName, startTime, endTime);
        
        if (records.isEmpty()) {
            return Collections.emptyMap();
        }
        
        // 只统计原始记录
        List<Double> values = records.stream()
                .filter(MetricsRecord::isRawRecord)
                .map(MetricsRecord::getMetricValue)
                .collect(Collectors.toList());
        
        if (values.isEmpty()) {
            return Collections.emptyMap();
        }
        
        Map<String, Object> statistics = new HashMap<>();
        statistics.put("count", values.size());
        statistics.put("min", values.stream().min(Double::compare).orElse(0.0));
        statistics.put("max", values.stream().max(Double::compare).orElse(0.0));
        statistics.put("avg", values.stream().mapToDouble(Double::doubleValue).average().orElse(0.0));
        statistics.put("sum", values.stream().mapToDouble(Double::doubleValue).sum());
        
        // 计算标准差
        double avg = (Double) statistics.get("avg");
        double variance = values.stream()
                .mapToDouble(v -> Math.pow(v - avg, 2))
                .average()
                .orElse(0.0);
        statistics.put("stdDev", Math.sqrt(variance));
        
        // 时间范围信息
        statistics.put("startTime", startTime);
        statistics.put("endTime", endTime);
        statistics.put("duration", ChronoUnit.SECONDS.between(startTime, endTime));
        
        return statistics;
    }
    
    @Override
    public int deleteExpiredRecords(LocalDateTime expireTime) {
        int deletedCount = 0;
        
        synchronized (this) {
            // 删除原始记录
            for (Map<String, List<MetricsRecord>> instanceData : storage.values()) {
                for (List<MetricsRecord> records : instanceData.values()) {
                    int sizeBefore = records.size();
                    records.removeIf(record -> record.isExpired(expireTime));
                    deletedCount += sizeBefore - records.size();
                }
            }
            
            // 删除聚合记录
            for (Map<String, Map<String, List<MetricsRecord>>> instanceAggregated : aggregatedStorage.values()) {
                for (Map<String, List<MetricsRecord>> metricAggregated : instanceAggregated.values()) {
                    for (List<MetricsRecord> records : metricAggregated.values()) {
                        int sizeBefore = records.size();
                        records.removeIf(record -> record.isExpired(expireTime));
                        deletedCount += sizeBefore - records.size();
                    }
                }
            }
            
            // 清理索引
            recordIndex.entrySet().removeIf(entry -> entry.getValue().isExpired(expireTime));
            
            // 更新统计信息
            totalRecords -= deletedCount;
            // 重新计算存储大小（简化处理）
            recalculateStorageSize();
        }
        
        return deletedCount;
    }
    
    @Override
    public int deleteByInstanceId(String instanceId) {
        int deletedCount = 0;
        
        synchronized (this) {
            // 删除原始数据
            Map<String, List<MetricsRecord>> instanceData = storage.remove(instanceId);
            if (instanceData != null) {
                for (List<MetricsRecord> records : instanceData.values()) {
                    deletedCount += records.size();
                }
            }
            
            // 删除聚合数据
            Map<String, Map<String, List<MetricsRecord>>> instanceAggregated = aggregatedStorage.remove(instanceId);
            if (instanceAggregated != null) {
                for (Map<String, List<MetricsRecord>> metricAggregated : instanceAggregated.values()) {
                    for (List<MetricsRecord> records : metricAggregated.values()) {
                        deletedCount += records.size();
                    }
                }
            }
            
            // 清理索引
            recordIndex.entrySet().removeIf(entry -> instanceId.equals(entry.getValue().getInstanceId()));
            
            // 更新统计信息
            totalRecords -= deletedCount;
            recalculateStorageSize();
        }
        
        return deletedCount;
    }
    
    @Override
    public int deleteByTimeRange(LocalDateTime startTime, LocalDateTime endTime) {
        return deleteExpiredRecords(endTime); // 删除结束时间之前的所有记录
    }
    
    @Override
    public long countRecords(String instanceId, LocalDateTime startTime, LocalDateTime endTime) {
        return findByInstanceId(instanceId, startTime, endTime).size();
    }
    
    @Override
    public long getStorageSize() {
        return totalStorageSize;
    }
    
    @Override
    public List<String> getAllInstanceIds() {
        return new ArrayList<>(storage.keySet());
    }
    
    @Override
    public List<String> getMetricNames(String instanceId) {
        Map<String, List<MetricsRecord>> instanceData = storage.get(instanceId);
        return instanceData != null ? new ArrayList<>(instanceData.keySet()) : new ArrayList<>();
    }
    
    @Override
    public boolean isHealthy() {
        return true; // 内存存储总是健康的
    }
    
    @Override
    public Map<String, Object> getRepositoryInfo() {
        Map<String, Object> info = new HashMap<>();
        info.put("type", "InMemory");
        info.put("totalRecords", totalRecords);
        info.put("storageSize", totalStorageSize);
        info.put("instanceCount", storage.size());
        info.put("createTime", createTime);
        info.put("uptime", ChronoUnit.SECONDS.between(createTime, LocalDateTime.now()));
        info.put("healthy", isHealthy());
        
        // 统计各实例的记录数
        Map<String, Long> instanceStats = new HashMap<>();
        for (Map.Entry<String, Map<String, List<MetricsRecord>>> entry : storage.entrySet()) {
            long count = entry.getValue().values().stream()
                    .mapToLong(List::size)
                    .sum();
            instanceStats.put(entry.getKey(), count);
        }
        info.put("instanceStats", instanceStats);
        
        return info;
    }
    
    /**
     * 保存原始记录
     */
    private void saveRawRecord(MetricsRecord record) {
        storage.computeIfAbsent(record.getInstanceId(), k -> new ConcurrentHashMap<>())
                .computeIfAbsent(record.getMetricName(), k -> new CopyOnWriteArrayList<>())
                .add(record);
    }
    
    /**
     * 保存聚合记录
     */
    private void saveAggregatedRecord(MetricsRecord record) {
        String key = record.getAggregationType() + "_" + record.getAggregationInterval();
        
        aggregatedStorage.computeIfAbsent(record.getInstanceId(), k -> new ConcurrentHashMap<>())
                .computeIfAbsent(record.getMetricName(), k -> new ConcurrentHashMap<>())
                .computeIfAbsent(key, k -> new CopyOnWriteArrayList<>())
                .add(record);
    }
    
    /**
     * 按时间范围过滤记录
     */
    private List<MetricsRecord> filterByTimeRange(List<MetricsRecord> records, 
                                                 LocalDateTime startTime, LocalDateTime endTime) {
        if (startTime == null && endTime == null) {
            return new ArrayList<>(records);
        }
        
        return records.stream()
                .filter(record -> {
                    LocalDateTime timestamp = record.getTimestamp();
                    boolean afterStart = startTime == null || !timestamp.isBefore(startTime);
                    boolean beforeEnd = endTime == null || !timestamp.isAfter(endTime);
                    return afterStart && beforeEnd;
                })
                .collect(Collectors.toList());
    }
    
    /**
     * 估算记录大小
     */
    private long estimateRecordSize(MetricsRecord record) {
        // 简单估算：基础对象大小 + 字符串长度
        long size = 200; // 基础对象大小
        if (record.getRecordId() != null) size += record.getRecordId().length() * 2;
        if (record.getInstanceId() != null) size += record.getInstanceId().length() * 2;
        if (record.getMetricName() != null) size += record.getMetricName().length() * 2;
        if (record.getUnit() != null) size += record.getUnit().length() * 2;
        if (record.getSource() != null) size += record.getSource().length() * 2;
        if (record.getAggregationType() != null) size += record.getAggregationType().length() * 2;
        return size;
    }
    
    /**
     * 重新计算存储大小
     */
    private void recalculateStorageSize() {
        totalStorageSize = recordIndex.values().stream()
                .mapToLong(this::estimateRecordSize)
                .sum();
    }
}