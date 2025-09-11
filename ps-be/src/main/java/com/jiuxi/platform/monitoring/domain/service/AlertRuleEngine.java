package com.jiuxi.platform.monitoring.domain.service;

import com.jiuxi.platform.monitoring.domain.entity.Alert;
import com.jiuxi.platform.monitoring.domain.entity.AlertRule;
import com.jiuxi.platform.monitoring.domain.valueobject.MetricValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 告警规则引擎
 * 负责告警规则的执行、升级和管理
 * 
 * @author DDD Refactor
 * @date 2025-09-11
 */
@Service
public class AlertRuleEngine {
    
    private final AlertService alertService;
    
    // 规则状态跟踪：规则ID_实例ID -> 状态信息
    private final Map<String, RuleState> ruleStates = new ConcurrentHashMap<>();
    
    // 升级配置
    private final Map<String, List<EscalationLevel>> escalationRules = new ConcurrentHashMap<>();
    
    @Autowired
    public AlertRuleEngine(AlertService alertService) {
        this.alertService = alertService;
        initializeDefaultEscalations();
    }
    
    /**
     * 处理指标数据并评估告警规则
     */
    public List<Alert> processMetrics(String instanceId, List<MetricValue> metrics) {
        List<Alert> newAlerts = new ArrayList<>();
        
        for (MetricValue metric : metrics) {
            List<Alert> metricAlerts = processMetric(instanceId, metric);
            newAlerts.addAll(metricAlerts);
        }
        
        return newAlerts;
    }
    
    /**
     * 处理单个指标
     */
    private List<Alert> processMetric(String instanceId, MetricValue metric) {
        List<Alert> alerts = new ArrayList<>();
        
        // 获取匹配的告警规则
        List<AlertRule> matchingRules = alertService.getAllAlertRules().stream()
                .filter(rule -> rule.getEnabled() && rule.getMetricName().equals(metric.getName()))
                .collect(Collectors.toList());
        
        for (AlertRule rule : matchingRules) {
            String stateKey = rule.getRuleId() + "_" + instanceId;
            RuleState state = ruleStates.computeIfAbsent(stateKey, k -> new RuleState(rule.getRuleId(), instanceId));
            
            boolean conditionMet = rule.evaluate(metric.getValue());
            state.updateCondition(conditionMet, metric.getValue());
            
            // 检查是否需要触发告警
            if (shouldTriggerAlert(rule, state)) {
                Alert alert = createAndProcessAlert(rule, instanceId, metric, state);
                alerts.add(alert);
                state.setAlertTriggered(true);
                state.setLastAlertTime(LocalDateTime.now());
            } else if (!conditionMet && state.isAlertTriggered()) {
                // 条件不再满足，解决告警
                resolveAlert(rule, instanceId, state);
                state.setAlertTriggered(false);
                state.reset();
            }
        }
        
        return alerts;
    }
    
    /**
     * 判断是否应该触发告警
     */
    private boolean shouldTriggerAlert(AlertRule rule, RuleState state) {
        // 条件必须满足
        if (!state.isCurrentConditionMet()) {
            return false;
        }
        
        // 检查持续时间
        if (state.getConditionStartTime() == null) {
            return false;
        }
        
        long durationSeconds = java.time.Duration.between(state.getConditionStartTime(), LocalDateTime.now()).getSeconds();
        return durationSeconds >= rule.getDuration() && !state.isAlertTriggered();
    }
    
    /**
     * 创建并处理告警
     */
    private Alert createAndProcessAlert(AlertRule rule, String instanceId, MetricValue metric, RuleState state) {
        String message = generateAlertMessage(rule, metric, state);
        Alert alert = Alert.create(rule, instanceId, metric.getValue(), message);
        
        // 处理告警升级
        processEscalation(alert, rule);
        
        return alert;
    }
    
    /**
     * 生成告警消息
     */
    private String generateAlertMessage(AlertRule rule, MetricValue metric, RuleState state) {
        StringBuilder message = new StringBuilder();
        message.append(String.format("【%s】告警规则 '%s' 被触发", rule.getSeverity(), rule.getRuleName()));
        message.append(String.format(" - 指标: %s", rule.getMetricName()));
        message.append(String.format(" - 当前值: %s", metric.getValue()));
        message.append(String.format(" - 阈值: %s %s", rule.getCondition(), rule.getThreshold()));
        message.append(String.format(" - 持续时间: %d秒", java.time.Duration.between(state.getConditionStartTime(), LocalDateTime.now()).getSeconds()));
        
        if (rule.getDescription() != null) {
            message.append(String.format(" - 描述: %s", rule.getDescription()));
        }
        
        return message.toString();
    }
    
    /**
     * 解决告警
     */
    private void resolveAlert(AlertRule rule, String instanceId, RuleState state) {
        // 这里可以添加告警解决的逻辑
        // 例如发送解决通知等
    }
    
    /**
     * 处理告警升级
     */
    private void processEscalation(Alert alert, AlertRule rule) {
        List<EscalationLevel> escalations = escalationRules.get(rule.getSeverity());
        if (escalations != null && !escalations.isEmpty()) {
            // 设置初始升级等级
            EscalationLevel initialLevel = escalations.get(0);
            alert.getAnnotations().put("escalation_level", 0);
            alert.getAnnotations().put("next_escalation_time", LocalDateTime.now().plusMinutes(initialLevel.getDelayMinutes()));
        }
    }
    
    /**
     * 定期检查告警升级
     */
    @Scheduled(fixedRate = 60000) // 每分钟执行一次
    public void checkEscalations() {
        List<Alert> activeAlerts = alertService.getActiveAlerts();
        LocalDateTime now = LocalDateTime.now();
        
        for (Alert alert : activeAlerts) {
            if (alert.getAnnotations() != null && alert.getAnnotations().containsKey("next_escalation_time")) {
                LocalDateTime nextEscalation = (LocalDateTime) alert.getAnnotations().get("next_escalation_time");
                
                if (now.isAfter(nextEscalation)) {
                    escalateAlert(alert);
                }
            }
        }
    }
    
    /**
     * 升级告警
     */
    private void escalateAlert(Alert alert) {
        Integer currentLevel = (Integer) alert.getAnnotations().getOrDefault("escalation_level", 0);
        List<EscalationLevel> escalations = escalationRules.get(alert.getSeverity());
        
        if (escalations != null && currentLevel < escalations.size() - 1) {
            int nextLevel = currentLevel + 1;
            EscalationLevel escalation = escalations.get(nextLevel);
            
            // 更新告警严重性
            if (escalation.getNewSeverity() != null) {
                alert.setSeverity(escalation.getNewSeverity());
            }
            
            // 更新升级信息
            alert.getAnnotations().put("escalation_level", nextLevel);
            alert.getAnnotations().put("escalation_time", LocalDateTime.now());
            
            // 设置下次升级时间
            if (nextLevel < escalations.size() - 1) {
                EscalationLevel nextEscalation = escalations.get(nextLevel + 1);
                alert.getAnnotations().put("next_escalation_time", 
                    LocalDateTime.now().plusMinutes(nextEscalation.getDelayMinutes()));
            } else {
                alert.getAnnotations().remove("next_escalation_time");
            }
            
            // 重置通知计数，确保升级后的告警会被通知
            alert.setNotifyCount(0);
            
            // 记录升级日志
            String escalationMessage = String.format("告警已升级到等级 %d，新严重性: %s", nextLevel, alert.getSeverity());
            alert.setMessage(alert.getMessage() + " | " + escalationMessage);
        }
    }
    
    /**
     * 定期清理规则状态
     */
    @Scheduled(fixedRate = 300000) // 每5分钟执行一次
    public void cleanupRuleStates() {
        LocalDateTime expireTime = LocalDateTime.now().minusHours(1);
        
        ruleStates.entrySet().removeIf(entry -> {
            RuleState state = entry.getValue();
            return state.getLastUpdateTime().isBefore(expireTime) && !state.isCurrentConditionMet();
        });
    }
    
    /**
     * 初始化默认升级规则
     */
    private void initializeDefaultEscalations() {
        // MEDIUM级别升级规则
        List<EscalationLevel> mediumEscalations = Arrays.asList(
            new EscalationLevel(0, 15, null), // 15分钟后升级
            new EscalationLevel(1, 30, "HIGH"), // 再30分钟后升级到HIGH
            new EscalationLevel(2, 60, "CRITICAL") // 再60分钟后升级到CRITICAL
        );
        escalationRules.put("MEDIUM", mediumEscalations);
        
        // HIGH级别升级规则
        List<EscalationLevel> highEscalations = Arrays.asList(
            new EscalationLevel(0, 10, null), // 10分钟后升级
            new EscalationLevel(1, 30, "CRITICAL") // 再30分钟后升级到CRITICAL
        );
        escalationRules.put("HIGH", highEscalations);
        
        // CRITICAL级别无升级（已是最高级别）
        List<EscalationLevel> criticalEscalations = Arrays.asList(
            new EscalationLevel(0, 5, null) // 5分钟后重新通知
        );
        escalationRules.put("CRITICAL", criticalEscalations);
    }
    
    /**
     * 获取规则引擎统计信息
     */
    public Map<String, Object> getEngineStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("activeRuleStates", ruleStates.size());
        stats.put("escalationRules", escalationRules.size());
        
        // 统计各状态的规则数量
        long firingRules = ruleStates.values().stream()
                .mapToLong(state -> state.isCurrentConditionMet() ? 1 : 0).sum();
        stats.put("firingRules", firingRules);
        
        // 统计各严重性等级的升级规则
        Map<String, Integer> escalationStats = new HashMap<>();
        for (Map.Entry<String, List<EscalationLevel>> entry : escalationRules.entrySet()) {
            escalationStats.put(entry.getKey(), entry.getValue().size());
        }
        stats.put("escalationLevels", escalationStats);
        
        return stats;
    }
    
    /**
     * 规则状态类
     */
    private static class RuleState {
        private final String ruleId;
        private final String instanceId;
        private boolean currentConditionMet;
        private LocalDateTime conditionStartTime;
        private LocalDateTime lastUpdateTime;
        private Double lastMetricValue;
        private boolean alertTriggered;
        private LocalDateTime lastAlertTime;
        
        public RuleState(String ruleId, String instanceId) {
            this.ruleId = ruleId;
            this.instanceId = instanceId;
            this.currentConditionMet = false;
            this.alertTriggered = false;
            this.lastUpdateTime = LocalDateTime.now();
        }
        
        public void updateCondition(boolean conditionMet, Double metricValue) {
            if (conditionMet != this.currentConditionMet) {
                this.currentConditionMet = conditionMet;
                if (conditionMet) {
                    this.conditionStartTime = LocalDateTime.now();
                } else {
                    this.conditionStartTime = null;
                }
            }
            this.lastMetricValue = metricValue;
            this.lastUpdateTime = LocalDateTime.now();
        }
        
        public void reset() {
            this.conditionStartTime = null;
            this.alertTriggered = false;
            this.lastAlertTime = null;
        }
        
        // Getters and Setters
        public String getRuleId() { return ruleId; }
        public String getInstanceId() { return instanceId; }
        public boolean isCurrentConditionMet() { return currentConditionMet; }
        public LocalDateTime getConditionStartTime() { return conditionStartTime; }
        public LocalDateTime getLastUpdateTime() { return lastUpdateTime; }
        public Double getLastMetricValue() { return lastMetricValue; }
        public boolean isAlertTriggered() { return alertTriggered; }
        public void setAlertTriggered(boolean alertTriggered) { this.alertTriggered = alertTriggered; }
        public LocalDateTime getLastAlertTime() { return lastAlertTime; }
        public void setLastAlertTime(LocalDateTime lastAlertTime) { this.lastAlertTime = lastAlertTime; }
    }
    
    /**
     * 升级等级类
     */
    private static class EscalationLevel {
        private final int level;
        private final int delayMinutes;
        private final String newSeverity;
        
        public EscalationLevel(int level, int delayMinutes, String newSeverity) {
            this.level = level;
            this.delayMinutes = delayMinutes;
            this.newSeverity = newSeverity;
        }
        
        public int getLevel() { return level; }
        public int getDelayMinutes() { return delayMinutes; }
        public String getNewSeverity() { return newSeverity; }
    }
}