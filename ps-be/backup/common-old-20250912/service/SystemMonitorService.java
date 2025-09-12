package com.jiuxi.common.service;

import java.util.List;
import java.util.Map;

/**
 * @ClassName: SystemMonitorService
 * @Description: 系统监控服务接口
 * @Author: System
 * @Date: 2025/01/07
 * @Copyright: 2025 www.jiuxi.com Inc. All rights reserved.
 */
public interface SystemMonitorService {
    
    // ==================== 系统信息监控 ====================
    
    /**
     * 获取系统基本信息
     * @return 系统信息
     */
    Map<String, Object> getSystemInfo();
    
    /**
     * 获取JVM信息
     * @return JVM信息
     */
    Map<String, Object> getJvmInfo();
    
    /**
     * 获取服务器信息
     * @return 服务器信息
     */
    Map<String, Object> getServerInfo();
    
    /**
     * 获取操作系统信息
     * @return 操作系统信息
     */
    Map<String, Object> getOsInfo();
    
    /**
     * 获取网络信息
     * @return 网络信息
     */
    Map<String, Object> getNetworkInfo();
    
    /**
     * 获取磁盘信息
     * @return 磁盘信息
     */
    Map<String, Object> getDiskInfo();
    
    // ==================== 性能监控 ====================
    
    /**
     * 获取CPU使用率
     * @return CPU使用率信息
     */
    Map<String, Object> getCpuUsage();
    
    /**
     * 获取内存使用情况
     * @return 内存使用情况
     */
    Map<String, Object> getMemoryUsage();
    
    /**
     * 获取磁盘使用情况
     * @return 磁盘使用情况
     */
    Map<String, Object> getDiskUsage();
    
    /**
     * 获取网络流量
     * @return 网络流量信息
     */
    Map<String, Object> getNetworkTraffic();
    
    /**
     * 获取系统负载
     * @return 系统负载信息
     */
    Map<String, Object> getSystemLoad();
    
    /**
     * 获取线程信息
     * @return 线程信息
     */
    Map<String, Object> getThreadInfo();
    
    /**
     * 获取垃圾回收信息
     * @return 垃圾回收信息
     */
    Map<String, Object> getGcInfo();
    
    // ==================== 应用监控 ====================
    
    /**
     * 获取应用状态
     * @return 应用状态
     */
    Map<String, Object> getApplicationStatus();
    
    /**
     * 获取数据库连接池状态
     * @return 数据库连接池状态
     */
    Map<String, Object> getDatabasePoolStatus();
    
    /**
     * 获取Redis连接状态
     * @return Redis连接状态
     */
    Map<String, Object> getRedisStatus();
    
    /**
     * 获取缓存统计
     * @return 缓存统计信息
     */
    Map<String, Object> getCacheStats();
    
    /**
     * 获取会话统计
     * @return 会话统计信息
     */
    Map<String, Object> getSessionStats();
    
    /**
     * 获取API调用统计
     * @return API调用统计
     */
    Map<String, Object> getApiStats();
    
    /**
     * 获取错误统计
     * @return 错误统计信息
     */
    Map<String, Object> getErrorStats();
    
    // ==================== 实时监控 ====================
    
    /**
     * 获取实时性能数据
     * @param duration 时间范围（分钟）
     * @return 实时性能数据
     */
    Map<String, Object> getRealTimePerformance(int duration);
    
    /**
     * 获取实时CPU数据
     * @param duration 时间范围（分钟）
     * @return 实时CPU数据
     */
    List<Map<String, Object>> getRealTimeCpuData(int duration);
    
    /**
     * 获取实时内存数据
     * @param duration 时间范围（分钟）
     * @return 实时内存数据
     */
    List<Map<String, Object>> getRealTimeMemoryData(int duration);
    
    /**
     * 获取实时网络数据
     * @param duration 时间范围（分钟）
     * @return 实时网络数据
     */
    List<Map<String, Object>> getRealTimeNetworkData(int duration);
    
    /**
     * 获取实时磁盘IO数据
     * @param duration 时间范围（分钟）
     * @return 实时磁盘IO数据
     */
    List<Map<String, Object>> getRealTimeDiskIOData(int duration);
    
    // ==================== 历史数据 ====================
    
    /**
     * 获取历史性能数据
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param interval 时间间隔（分钟）
     * @return 历史性能数据
     */
    Map<String, Object> getHistoryPerformance(String startTime, String endTime, int interval);
    
    /**
     * 获取历史CPU数据
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param interval 时间间隔（分钟）
     * @return 历史CPU数据
     */
    List<Map<String, Object>> getHistoryCpuData(String startTime, String endTime, int interval);
    
    /**
     * 获取历史内存数据
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param interval 时间间隔（分钟）
     * @return 历史内存数据
     */
    List<Map<String, Object>> getHistoryMemoryData(String startTime, String endTime, int interval);
    
    /**
     * 获取历史网络数据
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param interval 时间间隔（分钟）
     * @return 历史网络数据
     */
    List<Map<String, Object>> getHistoryNetworkData(String startTime, String endTime, int interval);
    
    /**
     * 获取历史磁盘数据
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param interval 时间间隔（分钟）
     * @return 历史磁盘数据
     */
    List<Map<String, Object>> getHistoryDiskData(String startTime, String endTime, int interval);
    
    // ==================== 告警监控 ====================
    
    /**
     * 设置性能告警阈值
     * @param metric 指标名称
     * @param threshold 阈值
     * @param operator 操作符（>、<、>=、<=、==）
     * @param level 告警级别（1-低，2-中，3-高，4-紧急）
     * @return 是否设置成功
     */
    boolean setPerformanceAlert(String metric, double threshold, String operator, int level);
    
    /**
     * 获取性能告警规则
     * @return 告警规则列表
     */
    List<Map<String, Object>> getPerformanceAlerts();
    
    /**
     * 删除性能告警规则
     * @param alertId 告警规则ID
     * @return 是否删除成功
     */
    boolean deletePerformanceAlert(String alertId);
    
    /**
     * 检查性能告警
     * @return 触发的告警列表
     */
    List<Map<String, Object>> checkPerformanceAlerts();
    
    /**
     * 获取告警历史
     * @param page 页码
     * @param size 每页大小
     * @param level 告警级别
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 告警历史分页数据
     */
    Map<String, Object> getAlertHistory(int page, int size, Integer level, String startTime, String endTime);
    
    /**
     * 确认告警
     * @param alertId 告警ID
     * @param userId 确认用户ID
     * @param remark 确认备注
     * @return 是否确认成功
     */
    boolean acknowledgeAlert(String alertId, String userId, String remark);
    
    // ==================== 健康检查 ====================
    
    /**
     * 执行系统健康检查
     * @return 健康检查结果
     */
    Map<String, Object> performHealthCheck();
    
    /**
     * 检查数据库连接
     * @return 数据库连接状态
     */
    Map<String, Object> checkDatabaseHealth();
    
    /**
     * 检查Redis连接
     * @return Redis连接状态
     */
    Map<String, Object> checkRedisHealth();
    
    /**
     * 检查外部服务
     * @return 外部服务状态
     */
    Map<String, Object> checkExternalServices();
    
    /**
     * 检查磁盘空间
     * @return 磁盘空间状态
     */
    Map<String, Object> checkDiskSpace();
    
    /**
     * 检查内存使用
     * @return 内存使用状态
     */
    Map<String, Object> checkMemoryUsage();
    
    /**
     * 获取健康检查历史
     * @param page 页码
     * @param size 每页大小
     * @param status 状态
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 健康检查历史
     */
    Map<String, Object> getHealthCheckHistory(int page, int size, String status, String startTime, String endTime);
    
    // ==================== 进程监控 ====================
    
    /**
     * 获取进程列表
     * @return 进程列表
     */
    List<Map<String, Object>> getProcessList();
    
    /**
     * 获取进程详情
     * @param pid 进程ID
     * @return 进程详情
     */
    Map<String, Object> getProcessDetail(long pid);
    
    /**
     * 杀死进程
     * @param pid 进程ID
     * @return 是否成功
     */
    boolean killProcess(long pid);
    
    /**
     * 获取端口占用情况
     * @return 端口占用列表
     */
    List<Map<String, Object>> getPortUsage();
    
    /**
     * 检查端口是否被占用
     * @param port 端口号
     * @return 是否被占用
     */
    boolean isPortInUse(int port);
    
    // ==================== 日志监控 ====================
    
    /**
     * 获取应用日志统计
     * @return 日志统计信息
     */
    Map<String, Object> getLogStats();
    
    /**
     * 获取错误日志
     * @param page 页码
     * @param size 每页大小
     * @param level 日志级别
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 错误日志分页数据
     */
    Map<String, Object> getErrorLogs(int page, int size, String level, String startTime, String endTime);
    
    /**
     * 搜索日志
     * @param keyword 关键词
     * @param page 页码
     * @param size 每页大小
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 日志搜索结果
     */
    Map<String, Object> searchLogs(String keyword, int page, int size, String startTime, String endTime);
    
    /**
     * 获取日志文件列表
     * @return 日志文件列表
     */
    List<Map<String, Object>> getLogFiles();
    
    /**
     * 下载日志文件
     * @param fileName 文件名
     * @return 文件路径
     */
    String downloadLogFile(String fileName);
    
    // ==================== 性能分析 ====================
    
    /**
     * 生成性能报告
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 性能报告
     */
    Map<String, Object> generatePerformanceReport(String startTime, String endTime);
    
    /**
     * 获取慢查询统计
     * @return 慢查询统计
     */
    Map<String, Object> getSlowQueryStats();
    
    /**
     * 获取接口性能统计
     * @return 接口性能统计
     */
    Map<String, Object> getApiPerformanceStats();
    
    /**
     * 获取热点数据
     * @return 热点数据统计
     */
    Map<String, Object> getHotspotData();
    
    /**
     * 分析系统瓶颈
     * @return 瓶颈分析结果
     */
    Map<String, Object> analyzeBottlenecks();
    
    // ==================== 监控配置 ====================
    
    /**
     * 设置监控配置
     * @param key 配置键
     * @param value 配置值
     * @return 是否设置成功
     */
    boolean setMonitorConfig(String key, Object value);
    
    /**
     * 获取监控配置
     * @param key 配置键
     * @return 配置值
     */
    Object getMonitorConfig(String key);
    
    /**
     * 获取所有监控配置
     * @return 所有配置
     */
    Map<String, Object> getAllMonitorConfigs();
    
    /**
     * 重置监控配置
     * @return 是否重置成功
     */
    boolean resetMonitorConfig();
    
    /**
     * 启用/禁用监控
     * @param enabled 是否启用
     * @return 是否设置成功
     */
    boolean setMonitorEnabled(boolean enabled);
    
    /**
     * 获取监控状态
     * @return 监控状态
     */
    Map<String, Object> getMonitorStatus();
}