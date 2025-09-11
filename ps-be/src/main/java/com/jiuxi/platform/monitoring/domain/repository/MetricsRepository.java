package com.jiuxi.platform.monitoring.domain.repository;

import com.jiuxi.platform.monitoring.domain.entity.MetricsRecord;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 监控指标存储库接口
 * 定义监控数据的存储和查询契约
 * 
 * @author DDD Refactor
 * @date 2025-09-11
 */
public interface MetricsRepository {
    
    /**
     * 保存单条指标记录
     */
    void save(MetricsRecord record);
    
    /**
     * 批量保存指标记录
     */
    void batchSave(List<MetricsRecord> records);
    
    /**
     * 根据ID查找指标记录
     */
    Optional<MetricsRecord> findById(String recordId);
    
    /**
     * 查询指定实例的指标记录
     */
    List<MetricsRecord> findByInstanceId(String instanceId, LocalDateTime startTime, LocalDateTime endTime);
    
    /**
     * 查询指定指标的记录
     */
    List<MetricsRecord> findByMetricName(String instanceId, String metricName, 
                                        LocalDateTime startTime, LocalDateTime endTime);
    
    /**
     * 查询指定时间范围的原始记录
     */
    List<MetricsRecord> findRawRecords(String instanceId, String metricName, 
                                      LocalDateTime startTime, LocalDateTime endTime, int limit);
    
    /**
     * 查询指定时间范围的聚合记录
     */
    List<MetricsRecord> findAggregatedRecords(String instanceId, String metricName, 
                                             String aggregationType, Integer interval,
                                             LocalDateTime startTime, LocalDateTime endTime);
    
    /**
     * 查询最新的指标记录
     */
    Optional<MetricsRecord> findLatestRecord(String instanceId, String metricName);
    
    /**
     * 查询指标统计信息
     */
    Map<String, Object> getMetricStatistics(String instanceId, String metricName, 
                                           LocalDateTime startTime, LocalDateTime endTime);
    
    /**
     * 删除过期记录
     */
    int deleteExpiredRecords(LocalDateTime expireTime);
    
    /**
     * 删除指定实例的记录
     */
    int deleteByInstanceId(String instanceId);
    
    /**
     * 删除指定时间范围的记录
     */
    int deleteByTimeRange(LocalDateTime startTime, LocalDateTime endTime);
    
    /**
     * 统计记录数量
     */
    long countRecords(String instanceId, LocalDateTime startTime, LocalDateTime endTime);
    
    /**
     * 统计存储大小（如果支持）
     */
    long getStorageSize();
    
    /**
     * 获取所有实例ID列表
     */
    List<String> getAllInstanceIds();
    
    /**
     * 获取指定实例的所有指标名称
     */
    List<String> getMetricNames(String instanceId);
    
    /**
     * 检查存储库连接状态
     */
    boolean isHealthy();
    
    /**
     * 获取存储库信息
     */
    Map<String, Object> getRepositoryInfo();
}