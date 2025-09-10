package com.jiuxi.module.system.domain.service;

import com.jiuxi.module.system.domain.entity.SystemLog;
import com.jiuxi.module.system.domain.entity.LogType;
import com.jiuxi.module.system.domain.entity.LogLevel;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * @InterfaceName: SystemLogDomainService
 * @Description: 系统日志领域服务接口
 * @Author: System
 * @Date: 2025/01/07
 * @Copyright: 2025 www.jiuxi.com Inc. All rights reserved.
 */
public interface SystemLogDomainService {
    
    /**
     * 创建操作日志
     */
    SystemLog createOperationLog(String moduleName, String operationName, String operationDesc, 
                               String operatorId, String operatorName);
    
    /**
     * 创建登录日志
     */
    SystemLog createLoginLog(String operatorId, String operatorName, String clientIp, 
                           String userAgent, boolean success, String errorMessage);
    
    /**
     * 创建异常日志
     */
    SystemLog createExceptionLog(String moduleName, String operationName, Exception exception, 
                               String operatorId, String requestUrl);
    
    /**
     * 创建安全日志
     */
    SystemLog createSecurityLog(String operationName, String operationDesc, String operatorId, 
                              String clientIp, String details);
    
    /**
     * 创建性能日志
     */
    SystemLog createPerformanceLog(String moduleName, String operationName, long executionTime, 
                                 String requestUrl, String operatorId);
    
    /**
     * 记录API调用日志
     */
    SystemLog recordApiLog(String requestMethod, String requestUrl, String requestParams, 
                         String responseResult, long executionTime, String operatorId);
    
    /**
     * 批量记录日志
     */
    void batchRecordLogs(List<SystemLog> logs);
    
    /**
     * 异步记录日志
     */
    void asyncRecordLog(SystemLog log);
    
    /**
     * 验证日志数据完整性
     */
    void validateLogData(SystemLog log);
    
    /**
     * 过滤敏感信息
     */
    String filterSensitiveInfo(String content);
    
    /**
     * 压缩日志内容
     */
    String compressLogContent(String content);
    
    /**
     * 解压日志内容
     */
    String decompressLogContent(String compressedContent);
    
    /**
     * 归档历史日志
     */
    void archiveHistoryLogs(LocalDateTime beforeTime);
    
    /**
     * 清理过期日志
     */
    void cleanExpiredLogs(int retentionDays);
    
    /**
     * 按条件清理日志
     */
    void cleanLogsByConditions(LogType logType, LogLevel logLevel, LocalDateTime beforeTime);
    
    /**
     * 生成日志统计报告
     */
    Map<String, Object> generateLogStatisticsReport(LocalDateTime startTime, LocalDateTime endTime);
    
    /**
     * 生成操作人日志报告
     */
    Map<String, Object> generateOperatorLogReport(String operatorId, LocalDateTime startTime, LocalDateTime endTime);
    
    /**
     * 生成模块日志报告
     */
    Map<String, Object> generateModuleLogReport(String moduleName, LocalDateTime startTime, LocalDateTime endTime);
    
    /**
     * 检测异常日志模式
     */
    List<Map<String, Object>> detectAbnormalLogPatterns(LocalDateTime startTime, LocalDateTime endTime);
    
    /**
     * 分析日志趋势
     */
    Map<String, Object> analyzeLogTrends(LogType logType, LocalDateTime startTime, LocalDateTime endTime);
    
    /**
     * 导出日志数据
     */
    String exportLogData(List<String> logIds, String format);
    
    /**
     * 备份日志数据
     */
    String backupLogData(LocalDateTime startTime, LocalDateTime endTime);
    
    /**
     * 验证日志完整性
     */
    boolean validateLogIntegrity(String logId);
    
    /**
     * 计算日志摘要
     */
    String calculateLogDigest(SystemLog log);
    
    /**
     * 监控日志存储空间
     */
    Map<String, Object> monitorLogStorage();
    
    /**
     * 优化日志存储
     */
    void optimizeLogStorage();
    
    /**
     * 获取日志配置
     */
    Map<String, Object> getLogConfiguration();
    
    /**
     * 更新日志配置
     */
    void updateLogConfiguration(Map<String, Object> config);
}