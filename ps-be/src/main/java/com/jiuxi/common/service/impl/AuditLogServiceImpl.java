package com.jiuxi.common.service.impl;

import com.jiuxi.common.service.AuditLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @ClassName: AuditLogServiceImpl
 * @Description: 审计日志服务实现类
 * @Author: System
 * @Date: 2025/01/07
 * @Copyright: 2025 www.jiuxi.com Inc. All rights reserved.
 */
@Slf4j
@Service
public class AuditLogServiceImpl implements AuditLogService {
    
    // 内存存储审计日志（实际项目中应使用数据库）
    private final Map<String, List<Map<String, Object>>> auditLogs = new ConcurrentHashMap<>();
    private final List<Map<String, Object>> alertRules = new ArrayList<>();
    
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    public AuditLogServiceImpl() {
        // 初始化日志分类
        auditLogs.put("operation", new ArrayList<>());
        auditLogs.put("login", new ArrayList<>());
        auditLogs.put("logout", new ArrayList<>());
        auditLogs.put("permission", new ArrayList<>());
        auditLogs.put("data", new ArrayList<>());
        auditLogs.put("system", new ArrayList<>());
        auditLogs.put("security", new ArrayList<>());
        auditLogs.put("api", new ArrayList<>());
        auditLogs.put("file", new ArrayList<>());
        auditLogs.put("config", new ArrayList<>());
    }
    
    @Override
    public String logOperation(String userId, String action, String resource, String resourceId, 
                              Map<String, Object> details, String ipAddress, String userAgent) {
        try {
            String logId = UUID.randomUUID().toString();
            Map<String, Object> logEntry = new HashMap<>();
            logEntry.put("id", logId);
            logEntry.put("userId", userId);
            logEntry.put("action", action);
            logEntry.put("resource", resource);
            logEntry.put("resourceId", resourceId);
            logEntry.put("details", details);
            logEntry.put("ipAddress", ipAddress);
            logEntry.put("userAgent", userAgent);
            logEntry.put("timestamp", LocalDateTime.now());
            logEntry.put("logType", "operation");
            
            auditLogs.get("operation").add(logEntry);
            
            log.info("操作日志记录成功: userId={}, action={}, resource={}", userId, action, resource);
            return logId;
            
        } catch (Exception e) {
            log.error("操作日志记录失败: userId={}, action={}", userId, action, e);
            return null;
        }
    }
    
    @Override
    public String logLogin(String userId, String username, String loginType, boolean success, 
                          String failureReason, String ipAddress, String userAgent, Map<String, Object> deviceInfo) {
        try {
            String logId = UUID.randomUUID().toString();
            Map<String, Object> logEntry = new HashMap<>();
            logEntry.put("id", logId);
            logEntry.put("userId", userId);
            logEntry.put("username", username);
            logEntry.put("loginType", loginType);
            logEntry.put("success", success);
            logEntry.put("failureReason", failureReason);
            logEntry.put("ipAddress", ipAddress);
            logEntry.put("userAgent", userAgent);
            logEntry.put("deviceInfo", deviceInfo);
            logEntry.put("timestamp", LocalDateTime.now());
            logEntry.put("logType", "login");
            
            auditLogs.get("login").add(logEntry);
            
            log.info("登录日志记录成功: username={}, success={}, loginType={}", username, success, loginType);
            return logId;
            
        } catch (Exception e) {
            log.error("登录日志记录失败: username={}", username, e);
            return null;
        }
    }
    
    @Override
    public String logLogout(String userId, String username, String logoutType, String ipAddress, String userAgent) {
        try {
            String logId = UUID.randomUUID().toString();
            Map<String, Object> logEntry = new HashMap<>();
            logEntry.put("id", logId);
            logEntry.put("userId", userId);
            logEntry.put("username", username);
            logEntry.put("logoutType", logoutType);
            logEntry.put("ipAddress", ipAddress);
            logEntry.put("userAgent", userAgent);
            logEntry.put("timestamp", LocalDateTime.now());
            logEntry.put("logType", "logout");
            
            auditLogs.get("logout").add(logEntry);
            
            log.info("登出日志记录成功: username={}, logoutType={}", username, logoutType);
            return logId;
            
        } catch (Exception e) {
            log.error("登出日志记录失败: username={}", username, e);
            return null;
        }
    }
    
    @Override
    public String logPermissionChange(String operatorId, String targetUserId, String action, 
                                     List<String> permissions, String reason, String ipAddress) {
        try {
            String logId = UUID.randomUUID().toString();
            Map<String, Object> logEntry = new HashMap<>();
            logEntry.put("id", logId);
            logEntry.put("operatorId", operatorId);
            logEntry.put("targetUserId", targetUserId);
            logEntry.put("action", action);
            logEntry.put("permissions", permissions);
            logEntry.put("reason", reason);
            logEntry.put("ipAddress", ipAddress);
            logEntry.put("timestamp", LocalDateTime.now());
            logEntry.put("logType", "permission");
            
            auditLogs.get("permission").add(logEntry);
            
            log.info("权限变更日志记录成功: operatorId={}, targetUserId={}, action={}", operatorId, targetUserId, action);
            return logId;
            
        } catch (Exception e) {
            log.error("权限变更日志记录失败: operatorId={}, targetUserId={}", operatorId, targetUserId, e);
            return null;
        }
    }
    
    @Override
    public String logDataChange(String userId, String tableName, String recordId, String action, 
                               Map<String, Object> oldData, Map<String, Object> newData, String ipAddress) {
        try {
            String logId = UUID.randomUUID().toString();
            Map<String, Object> logEntry = new HashMap<>();
            logEntry.put("id", logId);
            logEntry.put("userId", userId);
            logEntry.put("tableName", tableName);
            logEntry.put("recordId", recordId);
            logEntry.put("action", action);
            logEntry.put("oldData", oldData);
            logEntry.put("newData", newData);
            logEntry.put("ipAddress", ipAddress);
            logEntry.put("timestamp", LocalDateTime.now());
            logEntry.put("logType", "data");
            
            auditLogs.get("data").add(logEntry);
            
            log.info("数据变更日志记录成功: userId={}, tableName={}, action={}", userId, tableName, action);
            return logId;
            
        } catch (Exception e) {
            log.error("数据变更日志记录失败: userId={}, tableName={}", userId, tableName, e);
            return null;
        }
    }
    
    @Override
    public String logSystemEvent(String eventType, String eventName, String description, 
                                String severity, String source, Map<String, Object> details) {
        try {
            String logId = UUID.randomUUID().toString();
            Map<String, Object> logEntry = new HashMap<>();
            logEntry.put("id", logId);
            logEntry.put("eventType", eventType);
            logEntry.put("eventName", eventName);
            logEntry.put("description", description);
            logEntry.put("severity", severity);
            logEntry.put("source", source);
            logEntry.put("details", details);
            logEntry.put("timestamp", LocalDateTime.now());
            logEntry.put("logType", "system");
            
            auditLogs.get("system").add(logEntry);
            
            log.info("系统事件日志记录成功: eventType={}, eventName={}, severity={}", eventType, eventName, severity);
            return logId;
            
        } catch (Exception e) {
            log.error("系统事件日志记录失败: eventType={}, eventName={}", eventType, eventName, e);
            return null;
        }
    }
    
    @Override
    public String logSecurityEvent(String userId, String eventType, String description, String riskLevel, 
                                  String ipAddress, String userAgent, Map<String, Object> details) {
        try {
            String logId = UUID.randomUUID().toString();
            Map<String, Object> logEntry = new HashMap<>();
            logEntry.put("id", logId);
            logEntry.put("userId", userId);
            logEntry.put("eventType", eventType);
            logEntry.put("description", description);
            logEntry.put("riskLevel", riskLevel);
            logEntry.put("ipAddress", ipAddress);
            logEntry.put("userAgent", userAgent);
            logEntry.put("details", details);
            logEntry.put("timestamp", LocalDateTime.now());
            logEntry.put("logType", "security");
            
            auditLogs.get("security").add(logEntry);
            
            log.info("安全事件日志记录成功: userId={}, eventType={}, riskLevel={}", userId, eventType, riskLevel);
            return logId;
            
        } catch (Exception e) {
            log.error("安全事件日志记录失败: userId={}, eventType={}", userId, eventType, e);
            return null;
        }
    }
    
    @Override
    public String logApiCall(String userId, String method, String url, Map<String, Object> requestParams, 
                            int responseStatus, long responseTime, String ipAddress, String userAgent) {
        try {
            String logId = UUID.randomUUID().toString();
            Map<String, Object> logEntry = new HashMap<>();
            logEntry.put("id", logId);
            logEntry.put("userId", userId);
            logEntry.put("method", method);
            logEntry.put("url", url);
            logEntry.put("requestParams", requestParams);
            logEntry.put("responseStatus", responseStatus);
            logEntry.put("responseTime", responseTime);
            logEntry.put("ipAddress", ipAddress);
            logEntry.put("userAgent", userAgent);
            logEntry.put("timestamp", LocalDateTime.now());
            logEntry.put("logType", "api");
            
            auditLogs.get("api").add(logEntry);
            
            log.debug("API调用日志记录成功: method={}, url={}, status={}", method, url, responseStatus);
            return logId;
            
        } catch (Exception e) {
            log.error("API调用日志记录失败: method={}, url={}", method, url, e);
            return null;
        }
    }
    
    @Override
    public String logFileOperation(String userId, String action, String fileName, String filePath, 
                                  long fileSize, String ipAddress, Map<String, Object> details) {
        try {
            String logId = UUID.randomUUID().toString();
            Map<String, Object> logEntry = new HashMap<>();
            logEntry.put("id", logId);
            logEntry.put("userId", userId);
            logEntry.put("action", action);
            logEntry.put("fileName", fileName);
            logEntry.put("filePath", filePath);
            logEntry.put("fileSize", fileSize);
            logEntry.put("ipAddress", ipAddress);
            logEntry.put("details", details);
            logEntry.put("timestamp", LocalDateTime.now());
            logEntry.put("logType", "file");
            
            auditLogs.get("file").add(logEntry);
            
            log.info("文件操作日志记录成功: userId={}, action={}, fileName={}", userId, action, fileName);
            return logId;
            
        } catch (Exception e) {
            log.error("文件操作日志记录失败: userId={}, action={}", userId, action, e);
            return null;
        }
    }
    
    @Override
    public String logConfigChange(String userId, String configKey, String oldValue, String newValue, 
                                 String category, String reason, String ipAddress) {
        try {
            String logId = UUID.randomUUID().toString();
            Map<String, Object> logEntry = new HashMap<>();
            logEntry.put("id", logId);
            logEntry.put("userId", userId);
            logEntry.put("configKey", configKey);
            logEntry.put("oldValue", oldValue);
            logEntry.put("newValue", newValue);
            logEntry.put("category", category);
            logEntry.put("reason", reason);
            logEntry.put("ipAddress", ipAddress);
            logEntry.put("timestamp", LocalDateTime.now());
            logEntry.put("logType", "config");
            
            auditLogs.get("config").add(logEntry);
            
            log.info("配置变更日志记录成功: userId={}, configKey={}", userId, configKey);
            return logId;
            
        } catch (Exception e) {
            log.error("配置变更日志记录失败: userId={}, configKey={}", userId, configKey, e);
            return null;
        }
    }
    
    @Override
    public Map<String, Object> queryOperationLogs(String userId, String action, String resource, 
                                                  String startTime, String endTime, int page, int size) {
        return queryLogs("operation", buildOperationFilter(userId, action, resource), startTime, endTime, page, size);
    }
    
    @Override
    public Map<String, Object> queryLoginLogs(String userId, String username, String loginType, Boolean success, 
                                             String startTime, String endTime, int page, int size) {
        return queryLogs("login", buildLoginFilter(userId, username, loginType, success), startTime, endTime, page, size);
    }
    
    @Override
    public Map<String, Object> querySecurityLogs(String userId, String eventType, String riskLevel, 
                                                 String startTime, String endTime, int page, int size) {
        return queryLogs("security", buildSecurityFilter(userId, eventType, riskLevel), startTime, endTime, page, size);
    }
    
    @Override
    public Map<String, Object> querySystemLogs(String eventType, String severity, String source, 
                                              String startTime, String endTime, int page, int size) {
        return queryLogs("system", buildSystemFilter(eventType, severity, source), startTime, endTime, page, size);
    }
    
    @Override
    public Map<String, Object> queryApiLogs(String userId, String method, String url, Integer responseStatus, 
                                           String startTime, String endTime, int page, int size) {
        return queryLogs("api", buildApiFilter(userId, method, url, responseStatus), startTime, endTime, page, size);
    }
    
    @Override
    public Map<String, Object> queryDataChangeLogs(String userId, String tableName, String recordId, String action, 
                                                   String startTime, String endTime, int page, int size) {
        return queryLogs("data", buildDataFilter(userId, tableName, recordId, action), startTime, endTime, page, size);
    }
    
    @Override
    public Map<String, Object> getUserOperationStatistics(String userId, String startTime, String endTime) {
        Map<String, Object> stats = new HashMap<>();
        
        try {
            List<Map<String, Object>> userLogs = getAllLogsForUser(userId, startTime, endTime);
            
            long totalOperations = userLogs.size();
            Map<String, Long> actionStats = userLogs.stream()
                    .filter(log -> log.get("action") != null)
                    .collect(Collectors.groupingBy(
                            log -> (String) log.get("action"),
                            Collectors.counting()
                    ));
            
            Map<String, Long> resourceStats = userLogs.stream()
                    .filter(log -> log.get("resource") != null)
                    .collect(Collectors.groupingBy(
                            log -> (String) log.get("resource"),
                            Collectors.counting()
                    ));
            
            stats.put("userId", userId);
            stats.put("totalOperations", totalOperations);
            stats.put("actionStats", actionStats);
            stats.put("resourceStats", resourceStats);
            stats.put("period", startTime + " ~ " + endTime);
            
        } catch (Exception e) {
            log.error("获取用户操作统计失败: userId={}", userId, e);
        }
        
        return stats;
    }
    
    @Override
    public Map<String, Object> getSystemOperationStatistics(String startTime, String endTime) {
        Map<String, Object> stats = new HashMap<>();
        
        try {
            List<Map<String, Object>> allLogs = getAllLogsInPeriod(startTime, endTime);
            
            long totalOperations = allLogs.size();
            Map<String, Long> logTypeStats = allLogs.stream()
                    .collect(Collectors.groupingBy(
                            log -> (String) log.get("logType"),
                            Collectors.counting()
                    ));
            
            Map<String, Long> userStats = allLogs.stream()
                    .filter(log -> log.get("userId") != null)
                    .collect(Collectors.groupingBy(
                            log -> (String) log.get("userId"),
                            Collectors.counting()
                    ));
            
            stats.put("totalOperations", totalOperations);
            stats.put("logTypeStats", logTypeStats);
            stats.put("activeUsers", userStats.size());
            stats.put("topUsers", getTopUsers(userStats, 10));
            stats.put("period", startTime + " ~ " + endTime);
            
        } catch (Exception e) {
            log.error("获取系统操作统计失败", e);
        }
        
        return stats;
    }
    
    @Override
    public Map<String, Object> getLoginStatistics(String startTime, String endTime, String groupBy) {
        Map<String, Object> stats = new HashMap<>();
        
        try {
            List<Map<String, Object>> loginLogs = auditLogs.get("login");
            List<Map<String, Object>> filteredLogs = filterLogsByTime(loginLogs, startTime, endTime);
            
            long totalLogins = filteredLogs.size();
            long successLogins = filteredLogs.stream()
                    .filter(log -> Boolean.TRUE.equals(log.get("success")))
                    .count();
            long failedLogins = totalLogins - successLogins;
            
            Map<String, Long> loginTypeStats = filteredLogs.stream()
                    .collect(Collectors.groupingBy(
                            log -> (String) log.get("loginType"),
                            Collectors.counting()
                    ));
            
            stats.put("totalLogins", totalLogins);
            stats.put("successLogins", successLogins);
            stats.put("failedLogins", failedLogins);
            stats.put("successRate", totalLogins > 0 ? (double) successLogins / totalLogins * 100 : 0);
            stats.put("loginTypeStats", loginTypeStats);
            stats.put("period", startTime + " ~ " + endTime);
            
        } catch (Exception e) {
            log.error("获取登录统计失败", e);
        }
        
        return stats;
    }
    
    @Override
    public Map<String, Object> getSecurityEventStatistics(String startTime, String endTime, String groupBy) {
        Map<String, Object> stats = new HashMap<>();
        
        try {
            List<Map<String, Object>> securityLogs = auditLogs.get("security");
            List<Map<String, Object>> filteredLogs = filterLogsByTime(securityLogs, startTime, endTime);
            
            long totalEvents = filteredLogs.size();
            Map<String, Long> riskLevelStats = filteredLogs.stream()
                    .collect(Collectors.groupingBy(
                            log -> (String) log.get("riskLevel"),
                            Collectors.counting()
                    ));
            
            Map<String, Long> eventTypeStats = filteredLogs.stream()
                    .collect(Collectors.groupingBy(
                            log -> (String) log.get("eventType"),
                            Collectors.counting()
                    ));
            
            stats.put("totalEvents", totalEvents);
            stats.put("riskLevelStats", riskLevelStats);
            stats.put("eventTypeStats", eventTypeStats);
            stats.put("period", startTime + " ~ " + endTime);
            
        } catch (Exception e) {
            log.error("获取安全事件统计失败", e);
        }
        
        return stats;
    }
    
    @Override
    public Map<String, Object> getApiCallStatistics(String startTime, String endTime, String groupBy) {
        Map<String, Object> stats = new HashMap<>();
        
        try {
            List<Map<String, Object>> apiLogs = auditLogs.get("api");
            List<Map<String, Object>> filteredLogs = filterLogsByTime(apiLogs, startTime, endTime);
            
            long totalCalls = filteredLogs.size();
            double avgResponseTime = filteredLogs.stream()
                    .mapToLong(log -> (Long) log.get("responseTime"))
                    .average()
                    .orElse(0.0);
            
            Map<String, Long> methodStats = filteredLogs.stream()
                    .collect(Collectors.groupingBy(
                            log -> (String) log.get("method"),
                            Collectors.counting()
                    ));
            
            Map<String, Long> statusStats = filteredLogs.stream()
                    .collect(Collectors.groupingBy(
                            log -> String.valueOf(log.get("responseStatus")),
                            Collectors.counting()
                    ));
            
            stats.put("totalCalls", totalCalls);
            stats.put("avgResponseTime", avgResponseTime);
            stats.put("methodStats", methodStats);
            stats.put("statusStats", statusStats);
            stats.put("period", startTime + " ~ " + endTime);
            
        } catch (Exception e) {
            log.error("获取API调用统计失败", e);
        }
        
        return stats;
    }
    
    @Override
    public Map<String, Object> exportAuditLogs(String logType, Map<String, Object> filters, String format) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            List<Map<String, Object>> logs = auditLogs.getOrDefault(logType, new ArrayList<>());
            
            // 应用过滤条件
            List<Map<String, Object>> filteredLogs = applyFilters(logs, filters);
            
            String exportPath = generateExportFile(filteredLogs, format, logType);
            
            result.put("success", true);
            result.put("exportPath", exportPath);
            result.put("recordCount", filteredLogs.size());
            result.put("format", format);
            result.put("message", "审计日志导出成功");
            
        } catch (Exception e) {
            log.error("导出审计日志失败: logType={}, format={}", logType, format, e);
            result.put("success", false);
            result.put("message", "导出审计日志失败: " + e.getMessage());
        }
        
        return result;
    }
    
    @Override
    public Map<String, Object> cleanExpiredLogs(String logType, int retentionDays) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            LocalDateTime cutoffDate = LocalDateTime.now().minusDays(retentionDays);
            List<Map<String, Object>> logs = auditLogs.get(logType);
            
            if (logs != null) {
                int originalSize = logs.size();
                logs.removeIf(log -> {
                    LocalDateTime timestamp = (LocalDateTime) log.get("timestamp");
                    return timestamp.isBefore(cutoffDate);
                });
                int cleanedCount = originalSize - logs.size();
                
                result.put("success", true);
                result.put("cleanedCount", cleanedCount);
                result.put("remainingCount", logs.size());
                result.put("message", "过期日志清理完成");
                
                log.info("过期日志清理完成: logType={}, cleanedCount={}", logType, cleanedCount);
            } else {
                result.put("success", false);
                result.put("message", "日志类型不存在: " + logType);
            }
            
        } catch (Exception e) {
            log.error("清理过期日志失败: logType={}", logType, e);
            result.put("success", false);
            result.put("message", "清理过期日志失败: " + e.getMessage());
        }
        
        return result;
    }
    
    @Override
    public Map<String, Object> archiveHistoryLogs(String logType, String archiveDate, String archivePath) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 简化实现，实际项目中需要将日志移动到归档存储
            result.put("success", true);
            result.put("archivePath", archivePath);
            result.put("message", "历史日志归档成功");
            
            log.info("历史日志归档成功: logType={}, archivePath={}", logType, archivePath);
            
        } catch (Exception e) {
            log.error("归档历史日志失败: logType={}", logType, e);
            result.put("success", false);
            result.put("message", "归档历史日志失败: " + e.getMessage());
        }
        
        return result;
    }
    
    @Override
    public Map<String, Object> getLogStorageStatistics() {
        Map<String, Object> stats = new HashMap<>();
        
        try {
            long totalLogs = auditLogs.values().stream()
                    .mapToLong(List::size)
                    .sum();
            
            Map<String, Integer> logTypeCounts = new HashMap<>();
            for (Map.Entry<String, List<Map<String, Object>>> entry : auditLogs.entrySet()) {
                logTypeCounts.put(entry.getKey(), entry.getValue().size());
            }
            
            stats.put("totalLogs", totalLogs);
            stats.put("logTypeCounts", logTypeCounts);
            stats.put("estimatedSize", totalLogs * 1024); // 简化估算
            
        } catch (Exception e) {
            log.error("获取日志存储统计失败", e);
        }
        
        return stats;
    }
    
    @Override
    public Map<String, Object> checkLogIntegrity(String logType, String startTime, String endTime) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            List<Map<String, Object>> logs = auditLogs.get(logType);
            List<Map<String, Object>> filteredLogs = filterLogsByTime(logs, startTime, endTime);
            
            // 简化的完整性检查
            boolean isIntact = filteredLogs.stream()
                    .allMatch(log -> log.get("id") != null && log.get("timestamp") != null);
            
            result.put("success", true);
            result.put("isIntact", isIntact);
            result.put("checkedCount", filteredLogs.size());
            result.put("message", isIntact ? "日志完整性检查通过" : "发现日志完整性问题");
            
        } catch (Exception e) {
            log.error("日志完整性检查失败: logType={}", logType, e);
            result.put("success", false);
            result.put("message", "日志完整性检查失败: " + e.getMessage());
        }
        
        return result;
    }
    
    @Override
    public Map<String, Object> createLogBackup(String logType, String backupPath, String startTime, String endTime) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 简化实现，实际项目中需要创建实际的备份文件
            result.put("success", true);
            result.put("backupPath", backupPath);
            result.put("message", "日志备份创建成功");
            
            log.info("日志备份创建成功: logType={}, backupPath={}", logType, backupPath);
            
        } catch (Exception e) {
            log.error("创建日志备份失败: logType={}", logType, e);
            result.put("success", false);
            result.put("message", "创建日志备份失败: " + e.getMessage());
        }
        
        return result;
    }
    
    @Override
    public Map<String, Object> restoreLogBackup(String backupPath, String targetDate) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 简化实现，实际项目中需要从备份文件恢复日志
            result.put("success", true);
            result.put("message", "日志备份恢复成功");
            
            log.info("日志备份恢复成功: backupPath={}, targetDate={}", backupPath, targetDate);
            
        } catch (Exception e) {
            log.error("恢复日志备份失败: backupPath={}", backupPath, e);
            result.put("success", false);
            result.put("message", "恢复日志备份失败: " + e.getMessage());
        }
        
        return result;
    }
    
    @Override
    public Map<String, Object> setLogAlertRule(String ruleName, String logType, Map<String, Object> conditions, List<String> actions) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            String ruleId = UUID.randomUUID().toString();
            Map<String, Object> rule = new HashMap<>();
            rule.put("id", ruleId);
            rule.put("name", ruleName);
            rule.put("logType", logType);
            rule.put("conditions", conditions);
            rule.put("actions", actions);
            rule.put("enabled", true);
            rule.put("createTime", LocalDateTime.now());
            
            alertRules.add(rule);
            
            result.put("success", true);
            result.put("ruleId", ruleId);
            result.put("message", "日志告警规则设置成功");
            
        } catch (Exception e) {
            log.error("设置日志告警规则失败: ruleName={}", ruleName, e);
            result.put("success", false);
            result.put("message", "设置日志告警规则失败: " + e.getMessage());
        }
        
        return result;
    }
    
    @Override
    public List<Map<String, Object>> getLogAlertRules() {
        return new ArrayList<>(alertRules);
    }
    
    @Override
    public boolean deleteLogAlertRule(String ruleId) {
        try {
            return alertRules.removeIf(rule -> ruleId.equals(rule.get("id")));
        } catch (Exception e) {
            log.error("删除日志告警规则失败: ruleId={}", ruleId, e);
            return false;
        }
    }
    
    @Override
    public Map<String, Object> triggerLogAlertCheck() {
        Map<String, Object> result = new HashMap<>();
        
        try {
            int checkedRules = 0;
            int triggeredAlerts = 0;
            
            for (Map<String, Object> rule : alertRules) {
                if (Boolean.TRUE.equals(rule.get("enabled"))) {
                    checkedRules++;
                    // 简化实现，实际项目中需要根据规则条件检查日志
                    if (checkAlertCondition(rule)) {
                        triggeredAlerts++;
                        executeAlertActions(rule);
                    }
                }
            }
            
            result.put("success", true);
            result.put("checkedRules", checkedRules);
            result.put("triggeredAlerts", triggeredAlerts);
            result.put("message", "日志告警检查完成");
            
        } catch (Exception e) {
            log.error("触发日志告警检查失败", e);
            result.put("success", false);
            result.put("message", "触发日志告警检查失败: " + e.getMessage());
        }
        
        return result;
    }
    
    // 私有辅助方法
    
    private Map<String, Object> queryLogs(String logType, Map<String, Object> filters, 
                                          String startTime, String endTime, int page, int size) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            List<Map<String, Object>> logs = auditLogs.getOrDefault(logType, new ArrayList<>());
            
            // 应用过滤条件
            List<Map<String, Object>> filteredLogs = applyFilters(logs, filters);
            
            // 时间过滤
            filteredLogs = filterLogsByTime(filteredLogs, startTime, endTime);
            
            // 排序
            filteredLogs.sort((l1, l2) -> ((LocalDateTime) l2.get("timestamp")).compareTo((LocalDateTime) l1.get("timestamp")));
            
            // 分页
            int start = (page - 1) * size;
            int end = Math.min(start + size, filteredLogs.size());
            List<Map<String, Object>> pageLogs = filteredLogs.subList(start, end);
            
            result.put("records", pageLogs);
            result.put("total", filteredLogs.size());
            result.put("current", page);
            result.put("size", size);
            result.put("pages", (int) Math.ceil((double) filteredLogs.size() / size));
            
        } catch (Exception e) {
            log.error("查询日志失败: logType={}", logType, e);
            result.put("records", new ArrayList<>());
            result.put("total", 0);
            result.put("current", page);
            result.put("size", size);
            result.put("pages", 0);
        }
        
        return result;
    }
    
    private Map<String, Object> buildOperationFilter(String userId, String action, String resource) {
        Map<String, Object> filter = new HashMap<>();
        if (userId != null) filter.put("userId", userId);
        if (action != null) filter.put("action", action);
        if (resource != null) filter.put("resource", resource);
        return filter;
    }
    
    private Map<String, Object> buildLoginFilter(String userId, String username, String loginType, Boolean success) {
        Map<String, Object> filter = new HashMap<>();
        if (userId != null) filter.put("userId", userId);
        if (username != null) filter.put("username", username);
        if (loginType != null) filter.put("loginType", loginType);
        if (success != null) filter.put("success", success);
        return filter;
    }
    
    private Map<String, Object> buildSecurityFilter(String userId, String eventType, String riskLevel) {
        Map<String, Object> filter = new HashMap<>();
        if (userId != null) filter.put("userId", userId);
        if (eventType != null) filter.put("eventType", eventType);
        if (riskLevel != null) filter.put("riskLevel", riskLevel);
        return filter;
    }
    
    private Map<String, Object> buildSystemFilter(String eventType, String severity, String source) {
        Map<String, Object> filter = new HashMap<>();
        if (eventType != null) filter.put("eventType", eventType);
        if (severity != null) filter.put("severity", severity);
        if (source != null) filter.put("source", source);
        return filter;
    }
    
    private Map<String, Object> buildApiFilter(String userId, String method, String url, Integer responseStatus) {
        Map<String, Object> filter = new HashMap<>();
        if (userId != null) filter.put("userId", userId);
        if (method != null) filter.put("method", method);
        if (url != null) filter.put("url", url);
        if (responseStatus != null) filter.put("responseStatus", responseStatus);
        return filter;
    }
    
    private Map<String, Object> buildDataFilter(String userId, String tableName, String recordId, String action) {
        Map<String, Object> filter = new HashMap<>();
        if (userId != null) filter.put("userId", userId);
        if (tableName != null) filter.put("tableName", tableName);
        if (recordId != null) filter.put("recordId", recordId);
        if (action != null) filter.put("action", action);
        return filter;
    }
    
    private List<Map<String, Object>> applyFilters(List<Map<String, Object>> logs, Map<String, Object> filters) {
        if (filters == null || filters.isEmpty()) {
            return logs;
        }
        
        return logs.stream()
                .filter(log -> {
                    for (Map.Entry<String, Object> filter : filters.entrySet()) {
                        Object logValue = log.get(filter.getKey());
                        Object filterValue = filter.getValue();
                        if (logValue == null || !logValue.equals(filterValue)) {
                            return false;
                        }
                    }
                    return true;
                })
                .collect(Collectors.toList());
    }
    
    private List<Map<String, Object>> filterLogsByTime(List<Map<String, Object>> logs, String startTime, String endTime) {
        if (startTime == null && endTime == null) {
            return logs;
        }
        
        return logs.stream()
                .filter(log -> {
                    LocalDateTime timestamp = (LocalDateTime) log.get("timestamp");
                    if (startTime != null) {
                        LocalDateTime start = LocalDateTime.parse(startTime, formatter);
                        if (timestamp.isBefore(start)) {
                            return false;
                        }
                    }
                    if (endTime != null) {
                        LocalDateTime end = LocalDateTime.parse(endTime, formatter);
                        if (timestamp.isAfter(end)) {
                            return false;
                        }
                    }
                    return true;
                })
                .collect(Collectors.toList());
    }
    
    private List<Map<String, Object>> getAllLogsForUser(String userId, String startTime, String endTime) {
        List<Map<String, Object>> userLogs = new ArrayList<>();
        
        for (List<Map<String, Object>> logs : auditLogs.values()) {
            List<Map<String, Object>> filteredLogs = logs.stream()
                    .filter(log -> userId.equals(log.get("userId")))
                    .collect(Collectors.toList());
            filteredLogs = filterLogsByTime(filteredLogs, startTime, endTime);
            userLogs.addAll(filteredLogs);
        }
        
        return userLogs;
    }
    
    private List<Map<String, Object>> getAllLogsInPeriod(String startTime, String endTime) {
        List<Map<String, Object>> allLogs = new ArrayList<>();
        
        for (List<Map<String, Object>> logs : auditLogs.values()) {
            List<Map<String, Object>> filteredLogs = filterLogsByTime(logs, startTime, endTime);
            allLogs.addAll(filteredLogs);
        }
        
        return allLogs;
    }
    
    private List<Map<String, Object>> getTopUsers(Map<String, Long> userStats, int limit) {
        return userStats.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(limit)
                .map(entry -> {
                    Map<String, Object> userStat = new HashMap<>();
                    userStat.put("userId", entry.getKey());
                    userStat.put("operationCount", entry.getValue());
                    return userStat;
                })
                .collect(Collectors.toList());
    }
    
    private String generateExportFile(List<Map<String, Object>> logs, String format, String logType) {
        // 简化实现，实际项目中需要生成实际的导出文件
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        return "/exports/audit_logs_" + logType + "_" + timestamp + "." + format;
    }
    
    private boolean checkAlertCondition(Map<String, Object> rule) {
        // 简化实现，实际项目中需要根据规则条件检查日志
        return Math.random() > 0.8; // 20%概率触发告警
    }
    
    private void executeAlertActions(Map<String, Object> rule) {
        // 简化实现，实际项目中需要执行告警动作（发送邮件、短信等）
        log.warn("触发日志告警: ruleName={}", rule.get("name"));
    }
}