package com.jiuxi.platform.monitoring.domain.service;

import com.jiuxi.platform.monitoring.domain.entity.Alert;
import com.jiuxi.platform.monitoring.domain.entity.AlertRule;
import com.jiuxi.platform.monitoring.domain.valueobject.MetricValue;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 告警服务
 * 负责告警规则管理、告警评估和告警生命周期管理
 * 
 * @author DDD Refactor
 * @date 2025-09-11
 */
@Service
public class AlertService {
    
    // 告警规则存储
    private final Map<String, AlertRule> alertRules = new ConcurrentHashMap<>();
    
    // 活跃告警存储：实例ID -> 告警列表
    private final Map<String, List<Alert>> activeAlerts = new ConcurrentHashMap<>();
    
    // 告警历史记录：告警ID -> 告警
    private final Map<String, Alert> alertHistory = new ConcurrentHashMap<>();
    
    // 抑制规则：规则ID -> 抑制时间
    private final Map<String, LocalDateTime> suppressedRules = new ConcurrentHashMap<>();
    
    // 去重缓存：规则ID_实例ID -> 最后触发时间
    private final Map<String, LocalDateTime> deduplicationCache = new ConcurrentHashMap<>();
    
    // 配置参数
    private static final int DEFAULT_MAX_NOTIFY_COUNT = 10;
    private static final int DEFAULT_NOTIFY_INTERVAL = 15; // 15分钟
    private static final int DEFAULT_DEDUPLICATION_WINDOW = 5; // 5分钟
    
    /**
     * 添加告警规则
     */
    public void addAlertRule(AlertRule rule) {
        if (rule != null && rule.isValid()) {
            alertRules.put(rule.getRuleId(), rule);
        } else {
            throw new IllegalArgumentException("无效的告警规则");
        }
    }
    
    /**
     * 更新告警规则
     */
    public void updateAlertRule(AlertRule rule) {
        if (rule != null && rule.isValid() && alertRules.containsKey(rule.getRuleId())) {
            alertRules.put(rule.getRuleId(), rule);
        } else {
            throw new IllegalArgumentException("告警规则不存在或无效");
        }
    }
    
    /**
     * 删除告警规则
     */
    public void removeAlertRule(String ruleId) {
        alertRules.remove(ruleId);
        // 清理相关的活跃告警
        for (List<Alert> alerts : activeAlerts.values()) {
            alerts.removeIf(alert -> ruleId.equals(alert.getRuleId()));
        }
    }
    
    /**
     * 获取告警规则
     */
    public AlertRule getAlertRule(String ruleId) {
        return alertRules.get(ruleId);
    }
    
    /**
     * 获取所有告警规则
     */
    public List<AlertRule> getAllAlertRules() {
        return new ArrayList<>(alertRules.values());
    }
    
    /**
     * 启用告警规则
     */
    public void enableAlertRule(String ruleId) {
        AlertRule rule = alertRules.get(ruleId);
        if (rule != null) {
            rule.enable();
        }
    }
    
    /**
     * 禁用告警规则
     */
    public void disableAlertRule(String ruleId) {
        AlertRule rule = alertRules.get(ruleId);
        if (rule != null) {
            rule.disable();
        }
    }
    
    /**
     * 评估指标并生成告警
     */
    public List<Alert> evaluateMetrics(String instanceId, List<MetricValue> metrics) {
        List<Alert> newAlerts = new ArrayList<>();
        
        for (MetricValue metric : metrics) {
            List<Alert> metricAlerts = evaluateMetric(instanceId, metric);
            newAlerts.addAll(metricAlerts);
        }
        
        return newAlerts;
    }
    
    /**
     * 评估单个指标
     */
    public List<Alert> evaluateMetric(String instanceId, MetricValue metric) {
        List<Alert> newAlerts = new ArrayList<>();
        
        // 查找匹配的告警规则
        List<AlertRule> matchingRules = alertRules.values().stream()
                .filter(rule -> rule.getEnabled() && 
                               rule.getMetricName().equals(metric.getName()))
                .collect(Collectors.toList());
        
        for (AlertRule rule : matchingRules) {
            if (isRuleSuppressed(rule.getRuleId())) {
                continue;
            }
            
            boolean shouldAlert = rule.evaluate(metric.getValue());
            String deduplicationKey = rule.getRuleId() + "_" + instanceId;
            
            if (shouldAlert) {
                // 检查去重
                if (shouldCreateAlert(deduplicationKey)) {
                    Alert alert = createAlert(rule, instanceId, metric);
                    newAlerts.add(alert);
                    addActiveAlert(instanceId, alert);
                    deduplicationCache.put(deduplicationKey, LocalDateTime.now());
                }
            } else {
                // 检查是否需要解决现有告警
                resolveAlertsForRule(instanceId, rule.getRuleId());
            }
        }
        
        return newAlerts;
    }
    
    /**
     * 创建告警
     */
    private Alert createAlert(AlertRule rule, String instanceId, MetricValue metric) {
        String message = String.format("指标 %s 当前值 %s %s 阈值 %s", 
                metric.getName(), metric.getValue(), rule.getCondition(), rule.getThreshold());
        
        Alert alert = Alert.create(rule, instanceId, metric.getValue(), message);
        alertHistory.put(alert.getAlertId(), alert);
        
        return alert;
    }
    
    /**
     * 添加活跃告警
     */
    private void addActiveAlert(String instanceId, Alert alert) {
        activeAlerts.computeIfAbsent(instanceId, k -> new ArrayList<>()).add(alert);
    }
    
    /**
     * 解决告警
     */
    public void resolveAlert(String alertId) {
        Alert alert = alertHistory.get(alertId);
        if (alert != null && alert.isFiring()) {
            alert.resolve();
            removeFromActiveAlerts(alert);
        }
    }
    
    /**
     * 为规则解决告警
     */
    private void resolveAlertsForRule(String instanceId, String ruleId) {
        List<Alert> instanceAlerts = activeAlerts.get(instanceId);
        if (instanceAlerts != null) {
            List<Alert> toResolve = instanceAlerts.stream()
                    .filter(alert -> ruleId.equals(alert.getRuleId()) && alert.isFiring())
                    .collect(Collectors.toList());
            
            for (Alert alert : toResolve) {
                alert.resolve();
                instanceAlerts.remove(alert);
            }
        }
    }
    
    /**
     * 从活跃告警中移除
     */
    private void removeFromActiveAlerts(Alert alert) {
        List<Alert> instanceAlerts = activeAlerts.get(alert.getInstanceId());
        if (instanceAlerts != null) {
            instanceAlerts.remove(alert);
            if (instanceAlerts.isEmpty()) {
                activeAlerts.remove(alert.getInstanceId());
            }
        }
    }
    
    /**
     * 抑制告警规则
     */
    public void suppressAlertRule(String ruleId, int minutes) {
        suppressedRules.put(ruleId, LocalDateTime.now().plusMinutes(minutes));
    }
    
    /**
     * 取消抑制告警规则
     */
    public void unsuppressAlertRule(String ruleId) {
        suppressedRules.remove(ruleId);
    }
    
    /**
     * 检查规则是否被抑制
     */
    private boolean isRuleSuppressed(String ruleId) {
        LocalDateTime suppressUntil = suppressedRules.get(ruleId);
        if (suppressUntil == null) {
            return false;
        }
        
        if (LocalDateTime.now().isAfter(suppressUntil)) {
            suppressedRules.remove(ruleId);
            return false;
        }
        
        return true;
    }
    
    /**
     * 检查是否应该创建告警（去重逻辑）
     */
    private boolean shouldCreateAlert(String deduplicationKey) {
        LocalDateTime lastAlert = deduplicationCache.get(deduplicationKey);
        if (lastAlert == null) {
            return true;
        }
        
        return LocalDateTime.now().isAfter(lastAlert.plusMinutes(DEFAULT_DEDUPLICATION_WINDOW));
    }
    
    /**
     * 获取活跃告警
     */
    public List<Alert> getActiveAlerts() {
        return activeAlerts.values().stream()
                .flatMap(List::stream)
                .filter(Alert::isFiring)
                .sorted((a, b) -> {
                    int severityCompare = Integer.compare(b.getSeverityLevel(), a.getSeverityLevel());
                    return severityCompare != 0 ? severityCompare : a.getFireTime().compareTo(b.getFireTime());
                })
                .collect(Collectors.toList());
    }
    
    /**
     * 获取实例的活跃告警
     */
    public List<Alert> getActiveAlertsForInstance(String instanceId) {
        List<Alert> instanceAlerts = activeAlerts.get(instanceId);
        return instanceAlerts != null ? 
                instanceAlerts.stream().filter(Alert::isFiring).collect(Collectors.toList()) : 
                new ArrayList<>();
    }
    
    /**
     * 获取需要通知的告警
     */
    public List<Alert> getAlertsNeedingNotification() {
        return getActiveAlerts().stream()
                .filter(alert -> alert.shouldNotify(DEFAULT_MAX_NOTIFY_COUNT, DEFAULT_NOTIFY_INTERVAL))
                .collect(Collectors.toList());
    }
    
    /**
     * 记录告警通知
     */
    public void recordAlertNotification(String alertId) {
        Alert alert = alertHistory.get(alertId);
        if (alert != null) {
            alert.recordNotification();
        }
    }
    
    /**
     * 获取告警统计
     */
    public Map<String, Object> getAlertStatistics() {
        Map<String, Object> stats = new HashMap<>();
        
        List<Alert> activeAlertsList = getActiveAlerts();
        stats.put("totalActiveAlerts", activeAlertsList.size());
        
        // 按严重性统计
        Map<String, Long> severityStats = activeAlertsList.stream()
                .collect(Collectors.groupingBy(Alert::getSeverity, Collectors.counting()));
        stats.put("alertsBySeverity", severityStats);
        
        // 按实例统计
        Map<String, Long> instanceStats = activeAlertsList.stream()
                .collect(Collectors.groupingBy(Alert::getInstanceId, Collectors.counting()));
        stats.put("alertsByInstance", instanceStats);
        
        stats.put("totalRules", alertRules.size());
        stats.put("enabledRules", alertRules.values().stream().mapToLong(rule -> rule.getEnabled() ? 1 : 0).sum());
        stats.put("suppressedRules", suppressedRules.size());
        
        return stats;
    }
    
    /**
     * 清理过期数据
     */
    public void cleanup() {
        // 清理过期的抑制规则
        LocalDateTime now = LocalDateTime.now();
        suppressedRules.entrySet().removeIf(entry -> now.isAfter(entry.getValue()));
        
        // 清理过期的去重缓存
        LocalDateTime expireTime = now.minusMinutes(DEFAULT_DEDUPLICATION_WINDOW);
        deduplicationCache.entrySet().removeIf(entry -> entry.getValue().isBefore(expireTime));
        
        // 清理已解决的告警（保留24小时）
        LocalDateTime historyExpire = now.minusHours(24);
        alertHistory.entrySet().removeIf(entry -> {
            Alert alert = entry.getValue();
            return alert.isResolved() && alert.getResolveTime().isBefore(historyExpire);
        });
    }
    
    /**
     * 初始化默认告警规则
     */
    public void initializeDefaultRules() {
        // CPU使用率告警
        AlertRule cpuHighRule = new AlertRule("cpu_high", "CPU使用率过高", "cpu.usage", ">", 80.0, "HIGH");
        cpuHighRule.setDescription("CPU使用率超过80%");
        cpuHighRule.setDuration(300); // 5分钟
        addAlertRule(cpuHighRule);
        
        AlertRule cpuCriticalRule = new AlertRule("cpu_critical", "CPU使用率严重过高", "cpu.usage", ">", 95.0, "CRITICAL");
        cpuCriticalRule.setDescription("CPU使用率超过95%");
        cpuCriticalRule.setDuration(60); // 1分钟
        addAlertRule(cpuCriticalRule);
        
        // 内存使用率告警
        AlertRule memoryHighRule = new AlertRule("memory_high", "内存使用率过高", "memory.heap.usage.percentage", ">", 85.0, "HIGH");
        memoryHighRule.setDescription("堆内存使用率超过85%");
        memoryHighRule.setDuration(300);
        addAlertRule(memoryHighRule);
        
        AlertRule memoryCriticalRule = new AlertRule("memory_critical", "内存使用率严重过高", "memory.heap.usage.percentage", ">", 95.0, "CRITICAL");
        memoryCriticalRule.setDescription("堆内存使用率超过95%");
        memoryCriticalRule.setDuration(60);
        addAlertRule(memoryCriticalRule);
        
        // 磁盘使用率告警
        AlertRule diskHighRule = new AlertRule("disk_high", "磁盘使用率过高", "disk.usage", ">", 90.0, "MEDIUM");
        diskHighRule.setDescription("磁盘使用率超过90%");
        diskHighRule.setDuration(600); // 10分钟
        addAlertRule(diskHighRule);
        
        // JVM线程数告警
        AlertRule threadHighRule = new AlertRule("thread_high", "线程数过多", "jvm.threads.count", ">", 1000.0, "MEDIUM");
        threadHighRule.setDescription("JVM线程数超过1000");
        threadHighRule.setDuration(300);
        addAlertRule(threadHighRule);
    }
}