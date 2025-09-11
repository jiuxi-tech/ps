package com.jiuxi.platform.monitoring.infrastructure.collector;

import com.jiuxi.platform.monitoring.domain.valueobject.MetricValue;

import java.util.List;

/**
 * 指标收集器接口
 * 
 * @author DDD Refactor
 * @date 2025-09-11
 */
public interface MetricsCollector {
    
    /**
     * 收集指标
     * @return 指标列表
     */
    List<MetricValue> collectMetrics();
    
    /**
     * 获取收集器名称
     * @return 收集器名称
     */
    String getCollectorName();
    
    /**
     * 判断收集器是否启用
     * @return 是否启用
     */
    boolean isEnabled();
    
    /**
     * 启用收集器
     */
    void enable();
    
    /**
     * 禁用收集器
     */
    void disable();
    
    /**
     * 获取收集间隔（秒）
     * @return 收集间隔
     */
    int getCollectionInterval();
}