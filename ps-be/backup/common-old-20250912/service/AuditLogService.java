package com.jiuxi.common.service;

import java.util.List;
import java.util.Map;

/**
 * @ClassName: AuditLogService
 * @Description: 审计日志服务接口
 * @Author: System
 * @Date: 2025/01/07
 * @Copyright: 2025 www.jiuxi.com Inc. All rights reserved.
 */
public interface AuditLogService {
    
    /**
     * 记录操作日志
     * 
     * @param userId 用户ID
     * @param action 操作动作
     * @param resource 操作资源
     * @param resourceId 资源ID
     * @param details 操作详情
     * @param ipAddress IP地址
     * @param userAgent 用户代理
     * @return 日志ID
     */
    String logOperation(String userId, String action, String resource, String resourceId, 
                       Map<String, Object> details, String ipAddress, String userAgent);
    
    /**
     * 记录登录日志
     * 
     * @param userId 用户ID
     * @param username 用户名
     * @param loginType 登录类型（password/sso/oauth等）
     * @param success 是否成功
     * @param failureReason 失败原因
     * @param ipAddress IP地址
     * @param userAgent 用户代理
     * @param deviceInfo 设备信息
     * @return 日志ID
     */
    String logLogin(String userId, String username, String loginType, boolean success, 
                   String failureReason, String ipAddress, String userAgent, Map<String, Object> deviceInfo);
    
    /**
     * 记录登出日志
     * 
     * @param userId 用户ID
     * @param username 用户名
     * @param logoutType 登出类型（manual/timeout/force等）
     * @param ipAddress IP地址
     * @param userAgent 用户代理
     * @return 日志ID
     */
    String logLogout(String userId, String username, String logoutType, String ipAddress, String userAgent);
    
    /**
     * 记录权限变更日志
     * 
     * @param operatorId 操作者ID
     * @param targetUserId 目标用户ID
     * @param action 权限动作（grant/revoke/modify）
     * @param permissions 权限列表
     * @param reason 变更原因
     * @param ipAddress IP地址
     * @return 日志ID
     */
    String logPermissionChange(String operatorId, String targetUserId, String action, 
                              List<String> permissions, String reason, String ipAddress);
    
    /**
     * 记录数据变更日志
     * 
     * @param userId 用户ID
     * @param tableName 表名
     * @param recordId 记录ID
     * @param action 操作类型（INSERT/UPDATE/DELETE）
     * @param oldData 旧数据
     * @param newData 新数据
     * @param ipAddress IP地址
     * @return 日志ID
     */
    String logDataChange(String userId, String tableName, String recordId, String action, 
                        Map<String, Object> oldData, Map<String, Object> newData, String ipAddress);
    
    /**
     * 记录系统事件日志
     * 
     * @param eventType 事件类型
     * @param eventName 事件名称
     * @param description 事件描述
     * @param severity 严重程度（INFO/WARN/ERROR/CRITICAL）
     * @param source 事件源
     * @param details 事件详情
     * @return 日志ID
     */
    String logSystemEvent(String eventType, String eventName, String description, 
                         String severity, String source, Map<String, Object> details);
    
    /**
     * 记录安全事件日志
     * 
     * @param userId 用户ID
     * @param eventType 安全事件类型
     * @param description 事件描述
     * @param riskLevel 风险等级（LOW/MEDIUM/HIGH/CRITICAL）
     * @param ipAddress IP地址
     * @param userAgent 用户代理
     * @param details 事件详情
     * @return 日志ID
     */
    String logSecurityEvent(String userId, String eventType, String description, String riskLevel, 
                           String ipAddress, String userAgent, Map<String, Object> details);
    
    /**
     * 记录API调用日志
     * 
     * @param userId 用户ID
     * @param method HTTP方法
     * @param url 请求URL
     * @param requestParams 请求参数
     * @param responseStatus 响应状态码
     * @param responseTime 响应时间（毫秒）
     * @param ipAddress IP地址
     * @param userAgent 用户代理
     * @return 日志ID
     */
    String logApiCall(String userId, String method, String url, Map<String, Object> requestParams, 
                     int responseStatus, long responseTime, String ipAddress, String userAgent);
    
    /**
     * 记录文件操作日志
     * 
     * @param userId 用户ID
     * @param action 文件操作（upload/download/delete/view）
     * @param fileName 文件名
     * @param filePath 文件路径
     * @param fileSize 文件大小
     * @param ipAddress IP地址
     * @param details 操作详情
     * @return 日志ID
     */
    String logFileOperation(String userId, String action, String fileName, String filePath, 
                           long fileSize, String ipAddress, Map<String, Object> details);
    
    /**
     * 记录配置变更日志
     * 
     * @param userId 用户ID
     * @param configKey 配置键
     * @param oldValue 旧值
     * @param newValue 新值
     * @param category 配置分类
     * @param reason 变更原因
     * @param ipAddress IP地址
     * @return 日志ID
     */
    String logConfigChange(String userId, String configKey, String oldValue, String newValue, 
                          String category, String reason, String ipAddress);
    
    /**
     * 查询操作日志
     * 
     * @param userId 用户ID（可选）
     * @param action 操作动作（可选）
     * @param resource 操作资源（可选）
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param page 页码
     * @param size 页大小
     * @return 分页查询结果
     */
    Map<String, Object> queryOperationLogs(String userId, String action, String resource, 
                                           String startTime, String endTime, int page, int size);
    
    /**
     * 查询登录日志
     * 
     * @param userId 用户ID（可选）
     * @param username 用户名（可选）
     * @param loginType 登录类型（可选）
     * @param success 是否成功（可选）
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param page 页码
     * @param size 页大小
     * @return 分页查询结果
     */
    Map<String, Object> queryLoginLogs(String userId, String username, String loginType, Boolean success, 
                                       String startTime, String endTime, int page, int size);
    
    /**
     * 查询安全事件日志
     * 
     * @param userId 用户ID（可选）
     * @param eventType 事件类型（可选）
     * @param riskLevel 风险等级（可选）
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param page 页码
     * @param size 页大小
     * @return 分页查询结果
     */
    Map<String, Object> querySecurityLogs(String userId, String eventType, String riskLevel, 
                                          String startTime, String endTime, int page, int size);
    
    /**
     * 查询系统事件日志
     * 
     * @param eventType 事件类型（可选）
     * @param severity 严重程度（可选）
     * @param source 事件源（可选）
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param page 页码
     * @param size 页大小
     * @return 分页查询结果
     */
    Map<String, Object> querySystemLogs(String eventType, String severity, String source, 
                                        String startTime, String endTime, int page, int size);
    
    /**
     * 查询API调用日志
     * 
     * @param userId 用户ID（可选）
     * @param method HTTP方法（可选）
     * @param url 请求URL（可选）
     * @param responseStatus 响应状态码（可选）
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param page 页码
     * @param size 页大小
     * @return 分页查询结果
     */
    Map<String, Object> queryApiLogs(String userId, String method, String url, Integer responseStatus, 
                                     String startTime, String endTime, int page, int size);
    
    /**
     * 查询数据变更日志
     * 
     * @param userId 用户ID（可选）
     * @param tableName 表名（可选）
     * @param recordId 记录ID（可选）
     * @param action 操作类型（可选）
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param page 页码
     * @param size 页大小
     * @return 分页查询结果
     */
    Map<String, Object> queryDataChangeLogs(String userId, String tableName, String recordId, String action, 
                                            String startTime, String endTime, int page, int size);
    
    /**
     * 获取用户操作统计
     * 
     * @param userId 用户ID
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 统计结果
     */
    Map<String, Object> getUserOperationStatistics(String userId, String startTime, String endTime);
    
    /**
     * 获取系统操作统计
     * 
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 统计结果
     */
    Map<String, Object> getSystemOperationStatistics(String startTime, String endTime);
    
    /**
     * 获取登录统计
     * 
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param groupBy 分组方式（hour/day/week/month）
     * @return 统计结果
     */
    Map<String, Object> getLoginStatistics(String startTime, String endTime, String groupBy);
    
    /**
     * 获取安全事件统计
     * 
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param groupBy 分组方式（hour/day/week/month）
     * @return 统计结果
     */
    Map<String, Object> getSecurityEventStatistics(String startTime, String endTime, String groupBy);
    
    /**
     * 获取API调用统计
     * 
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param groupBy 分组方式（hour/day/week/month）
     * @return 统计结果
     */
    Map<String, Object> getApiCallStatistics(String startTime, String endTime, String groupBy);
    
    /**
     * 导出审计日志
     * 
     * @param logType 日志类型（operation/login/security/system/api/data）
     * @param filters 过滤条件
     * @param format 导出格式（csv/excel/json）
     * @return 导出结果
     */
    Map<String, Object> exportAuditLogs(String logType, Map<String, Object> filters, String format);
    
    /**
     * 清理过期日志
     * 
     * @param logType 日志类型
     * @param retentionDays 保留天数
     * @return 清理结果
     */
    Map<String, Object> cleanExpiredLogs(String logType, int retentionDays);
    
    /**
     * 归档历史日志
     * 
     * @param logType 日志类型
     * @param archiveDate 归档日期
     * @param archivePath 归档路径
     * @return 归档结果
     */
    Map<String, Object> archiveHistoryLogs(String logType, String archiveDate, String archivePath);
    
    /**
     * 获取日志存储统计
     * 
     * @return 存储统计信息
     */
    Map<String, Object> getLogStorageStatistics();
    
    /**
     * 检查日志完整性
     * 
     * @param logType 日志类型
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 完整性检查结果
     */
    Map<String, Object> checkLogIntegrity(String logType, String startTime, String endTime);
    
    /**
     * 创建日志备份
     * 
     * @param logType 日志类型
     * @param backupPath 备份路径
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 备份结果
     */
    Map<String, Object> createLogBackup(String logType, String backupPath, String startTime, String endTime);
    
    /**
     * 恢复日志备份
     * 
     * @param backupPath 备份路径
     * @param targetDate 目标日期
     * @return 恢复结果
     */
    Map<String, Object> restoreLogBackup(String backupPath, String targetDate);
    
    /**
     * 设置日志告警规则
     * 
     * @param ruleName 规则名称
     * @param logType 日志类型
     * @param conditions 告警条件
     * @param actions 告警动作
     * @return 设置结果
     */
    Map<String, Object> setLogAlertRule(String ruleName, String logType, Map<String, Object> conditions, List<String> actions);
    
    /**
     * 获取日志告警规则列表
     * 
     * @return 告警规则列表
     */
    List<Map<String, Object>> getLogAlertRules();
    
    /**
     * 删除日志告警规则
     * 
     * @param ruleId 规则ID
     * @return 删除结果
     */
    boolean deleteLogAlertRule(String ruleId);
    
    /**
     * 触发日志告警检查
     * 
     * @return 检查结果
     */
    Map<String, Object> triggerLogAlertCheck();
}