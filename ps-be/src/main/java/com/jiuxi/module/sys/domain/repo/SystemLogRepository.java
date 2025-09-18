package com.jiuxi.module.sys.domain.repo;

import com.jiuxi.module.sys.domain.entity.SystemLog;
import com.jiuxi.module.sys.domain.entity.LogType;
import com.jiuxi.module.sys.domain.entity.LogLevel;
import com.jiuxi.module.sys.domain.entity.LogStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * @InterfaceName: SystemLogRepository
 * @Description: 系统日志仓储接口
 * 整合了原system模块的SystemLogRepository功能到sys模块
 * @Author: DDD重构 (Merged from system module)
 * @Date: 2025-09-12
 * @Copyright: 2025 www.jiuxi.com Inc. All rights reserved.
 */
public interface SystemLogRepository {
    
    /**
     * 保存系统日志
     */
    SystemLog save(SystemLog systemLog);
    
    /**
     * 批量保存系统日志
     */
    void saveAll(List<SystemLog> systemLogs);
    
    /**
     * 根据ID查找系统日志
     */
    Optional<SystemLog> findById(String logId);
    
    /**
     * 根据日志类型查找日志列表
     */
    List<SystemLog> findByLogType(LogType logType);
    
    /**
     * 根据日志级别查找日志列表
     */
    List<SystemLog> findByLogLevel(LogLevel logLevel);
    
    /**
     * 根据操作人ID查找日志列表
     */
    List<SystemLog> findByOperatorId(String operatorId);
    
    /**
     * 根据模块名称查找日志列表
     */
    List<SystemLog> findByModuleName(String moduleName);
    
    /**
     * 根据状态查找日志列表
     */
    List<SystemLog> findByStatus(LogStatus status);
    
    /**
     * 根据时间范围查找日志
     */
    List<SystemLog> findByCreateTimeBetween(LocalDateTime startTime, LocalDateTime endTime);
    
    /**
     * 根据客户端IP查找日志
     */
    List<SystemLog> findByClientIp(String clientIp);
    
    /**
     * 查找错误日志
     */
    List<SystemLog> findErrorLogs();
    
    /**
     * 查找失败的操作日志
     */
    List<SystemLog> findFailedOperations();
    
    /**
     * 根据执行时间范围查找慢操作日志
     */
    List<SystemLog> findSlowOperations(Long minExecutionTime);
    
    /**
     * 根据多个条件查找日志
     */
    List<SystemLog> findByConditions(LogType logType, LogLevel logLevel, String operatorId, 
                                   LocalDateTime startTime, LocalDateTime endTime);
    
    /**
     * 根据操作名称模糊查询
     */
    List<SystemLog> findByOperationNameContaining(String operationName);
    
    /**
     * 根据请求URL模糊查询
     */
    List<SystemLog> findByRequestUrlContaining(String requestUrl);
    
    /**
     * 根据ID删除系统日志
     */
    void deleteById(String logId);
    
    /**
     * 批量删除系统日志
     */
    void deleteByIds(List<String> logIds);
    
    /**
     * 根据时间范围删除日志
     */
    void deleteByCreateTimeBefore(LocalDateTime beforeTime);
    
    /**
     * 根据日志类型删除日志
     */
    void deleteByLogType(LogType logType);
    
    /**
     * 统计系统日志总数
     */
    long count();
    
    /**
     * 根据日志类型统计数量
     */
    long countByLogType(LogType logType);
    
    /**
     * 根据日志级别统计数量
     */
    long countByLogLevel(LogLevel logLevel);
    
    /**
     * 根据状态统计数量
     */
    long countByStatus(LogStatus status);
    
    /**
     * 根据时间范围统计数量
     */
    long countByCreateTimeBetween(LocalDateTime startTime, LocalDateTime endTime);
    
    /**
     * 统计操作人的日志数量
     */
    long countByOperatorId(String operatorId);
    
    /**
     * 分页查询系统日志
     */
    List<SystemLog> findWithPagination(int page, int size);
    
    /**
     * 根据条件分页查询系统日志
     */
    List<SystemLog> findByConditionsWithPagination(LogType logType, LogLevel logLevel, 
                                                  String operatorId, LocalDateTime startTime, 
                                                  LocalDateTime endTime, String keyword, 
                                                  int page, int size);
    
    /**
     * 获取日志统计信息
     */
    List<Object[]> getLogStatistics(LocalDateTime startTime, LocalDateTime endTime);
    
    /**
     * 获取操作人日志统计
     */
    List<Object[]> getOperatorLogStatistics(LocalDateTime startTime, LocalDateTime endTime);
    
    /**
     * 获取模块日志统计
     */
    List<Object[]> getModuleLogStatistics(LocalDateTime startTime, LocalDateTime endTime);
}