package com.jiuxi.platform.monitoring.domain.entity;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

/**
 * 告警实体
 * 
 * @author DDD Refactor
 * @date 2025-09-11
 */
public class Alert {
    
    private String alertId;
    private String ruleId;
    private String ruleName;
    private String instanceId;
    private String metricName;
    private Double metricValue;
    private Double threshold;
    private String condition;
    private String severity;
    private String status; // FIRING, RESOLVED, SUPPRESSED
    private String message;
    private LocalDateTime fireTime;
    private LocalDateTime resolveTime;
    private LocalDateTime lastNotifyTime;
    private Integer notifyCount;
    private Map<String, Object> labels;
    private Map<String, Object> annotations;
    
    // 构造函数
    public Alert() {
        this.alertId = UUID.randomUUID().toString();
        this.fireTime = LocalDateTime.now();
        this.status = "FIRING";
        this.notifyCount = 0;
    }
    
    public Alert(String ruleId, String ruleName, String instanceId, String metricName, 
                Double metricValue, Double threshold, String condition, String severity, String message) {
        this();
        this.ruleId = ruleId;
        this.ruleName = ruleName;
        this.instanceId = instanceId;
        this.metricName = metricName;
        this.metricValue = metricValue;
        this.threshold = threshold;
        this.condition = condition;
        this.severity = severity;
        this.message = message;
    }
    
    /**
     * 创建告警
     */
    public static Alert create(AlertRule rule, String instanceId, Double metricValue, String message) {
        return new Alert(
            rule.getRuleId(),
            rule.getRuleName(),
            instanceId,
            rule.getMetricName(),
            metricValue,
            rule.getThreshold(),
            rule.getCondition(),
            rule.getSeverity(),
            message
        );
    }
    
    /**
     * 解决告警
     */
    public void resolve() {
        this.status = "RESOLVED";
        this.resolveTime = LocalDateTime.now();
    }
    
    /**
     * 抑制告警
     */
    public void suppress() {
        this.status = "SUPPRESSED";
    }
    
    /**
     * 重新激活告警
     */
    public void refire() {
        this.status = "FIRING";
        this.resolveTime = null;
    }
    
    /**
     * 记录通知
     */
    public void recordNotification() {
        this.lastNotifyTime = LocalDateTime.now();
        this.notifyCount = this.notifyCount == null ? 1 : this.notifyCount + 1;
    }
    
    /**
     * 检查是否需要通知
     */
    public boolean shouldNotify(int maxNotifyCount, int notifyInterval) {
        if (!"FIRING".equals(status)) {
            return false;
        }
        
        if (notifyCount >= maxNotifyCount) {
            return false;
        }
        
        if (lastNotifyTime == null) {
            return true;
        }
        
        return lastNotifyTime.plusMinutes(notifyInterval).isBefore(LocalDateTime.now());
    }
    
    /**
     * 获取告警持续时间（分钟）
     */
    public long getDurationMinutes() {
        LocalDateTime endTime = resolveTime != null ? resolveTime : LocalDateTime.now();
        return java.time.Duration.between(fireTime, endTime).toMinutes();
    }
    
    /**
     * 获取告警严重性等级
     */
    public int getSeverityLevel() {
        switch (severity.toUpperCase()) {
            case "CRITICAL":
                return 4;
            case "HIGH":
                return 3;
            case "MEDIUM":
                return 2;
            case "LOW":
                return 1;
            default:
                return 0;
        }
    }
    
    /**
     * 检查告警是否激活
     */
    public boolean isFiring() {
        return "FIRING".equals(status);
    }
    
    /**
     * 检查告警是否已解决
     */
    public boolean isResolved() {
        return "RESOLVED".equals(status);
    }
    
    /**
     * 检查告警是否被抑制
     */
    public boolean isSuppressed() {
        return "SUPPRESSED".equals(status);
    }
    
    /**
     * 生成告警摘要
     */
    public String getSummary() {
        return String.format("[%s] %s: %s %s %s (当前值: %s)", 
                severity, ruleName, metricName, condition, threshold, metricValue);
    }
    
    // Getters and Setters
    public String getAlertId() { return alertId; }
    public void setAlertId(String alertId) { this.alertId = alertId; }
    
    public String getRuleId() { return ruleId; }
    public void setRuleId(String ruleId) { this.ruleId = ruleId; }
    
    public String getRuleName() { return ruleName; }
    public void setRuleName(String ruleName) { this.ruleName = ruleName; }
    
    public String getInstanceId() { return instanceId; }
    public void setInstanceId(String instanceId) { this.instanceId = instanceId; }
    
    public String getMetricName() { return metricName; }
    public void setMetricName(String metricName) { this.metricName = metricName; }
    
    public Double getMetricValue() { return metricValue; }
    public void setMetricValue(Double metricValue) { this.metricValue = metricValue; }
    
    public Double getThreshold() { return threshold; }
    public void setThreshold(Double threshold) { this.threshold = threshold; }
    
    public String getCondition() { return condition; }
    public void setCondition(String condition) { this.condition = condition; }
    
    public String getSeverity() { return severity; }
    public void setSeverity(String severity) { this.severity = severity; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    
    public LocalDateTime getFireTime() { return fireTime; }
    public void setFireTime(LocalDateTime fireTime) { this.fireTime = fireTime; }
    
    public LocalDateTime getResolveTime() { return resolveTime; }
    public void setResolveTime(LocalDateTime resolveTime) { this.resolveTime = resolveTime; }
    
    public LocalDateTime getLastNotifyTime() { return lastNotifyTime; }
    public void setLastNotifyTime(LocalDateTime lastNotifyTime) { this.lastNotifyTime = lastNotifyTime; }
    
    public Integer getNotifyCount() { return notifyCount; }
    public void setNotifyCount(Integer notifyCount) { this.notifyCount = notifyCount; }
    
    public Map<String, Object> getLabels() { return labels; }
    public void setLabels(Map<String, Object> labels) { this.labels = labels; }
    
    public Map<String, Object> getAnnotations() { return annotations; }
    public void setAnnotations(Map<String, Object> annotations) { this.annotations = annotations; }
    
    @Override
    public String toString() {
        return String.format("Alert{alertId='%s', ruleName='%s', instanceId='%s', metricName='%s', severity='%s', status='%s', fireTime=%s}", 
                alertId, ruleName, instanceId, metricName, severity, status, fireTime);
    }
}