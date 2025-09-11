package com.jiuxi.platform.monitoring.domain.entity;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * 告警规则实体
 * 
 * @author DDD Refactor
 * @date 2025-09-11
 */
public class AlertRule {
    
    private String ruleId;
    private String ruleName;
    private String description;
    private String metricName;
    private String condition; // >, <, >=, <=, ==, !=
    private Double threshold;
    private String severity; // CRITICAL, HIGH, MEDIUM, LOW
    private Boolean enabled;
    private Integer duration; // 持续时间（秒）
    private Map<String, Object> labels;
    private Map<String, Object> annotations;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    
    // 构造函数
    public AlertRule() {
        this.createTime = LocalDateTime.now();
        this.updateTime = LocalDateTime.now();
    }
    
    public AlertRule(String ruleId, String ruleName, String metricName, 
                    String condition, Double threshold, String severity) {
        this();
        this.ruleId = ruleId;
        this.ruleName = ruleName;
        this.metricName = metricName;
        this.condition = condition;
        this.threshold = threshold;
        this.severity = severity;
        this.enabled = true;
        this.duration = 60; // 默认1分钟
    }
    
    /**
     * 评估指标值是否触发告警
     */
    public boolean evaluate(Double metricValue) {
        if (!enabled || metricValue == null || threshold == null) {
            return false;
        }
        
        switch (condition) {
            case ">":
                return metricValue > threshold;
            case "<":
                return metricValue < threshold;
            case ">=":
                return metricValue >= threshold;
            case "<=":
                return metricValue <= threshold;
            case "==":
                return metricValue.equals(threshold);
            case "!=":
                return !metricValue.equals(threshold);
            default:
                return false;
        }
    }
    
    /**
     * 获取告警严重性等级（数值）
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
     * 检查规则是否有效
     */
    public boolean isValid() {
        return ruleId != null && !ruleId.trim().isEmpty() &&
               ruleName != null && !ruleName.trim().isEmpty() &&
               metricName != null && !metricName.trim().isEmpty() &&
               condition != null && !condition.trim().isEmpty() &&
               threshold != null && severity != null;
    }
    
    /**
     * 更新规则
     */
    public void update(String ruleName, String description, String condition, 
                      Double threshold, String severity, Integer duration) {
        if (ruleName != null) this.ruleName = ruleName;
        if (description != null) this.description = description;
        if (condition != null) this.condition = condition;
        if (threshold != null) this.threshold = threshold;
        if (severity != null) this.severity = severity;
        if (duration != null) this.duration = duration;
        this.updateTime = LocalDateTime.now();
    }
    
    /**
     * 启用规则
     */
    public void enable() {
        this.enabled = true;
        this.updateTime = LocalDateTime.now();
    }
    
    /**
     * 禁用规则
     */
    public void disable() {
        this.enabled = false;
        this.updateTime = LocalDateTime.now();
    }
    
    // Getters and Setters
    public String getRuleId() { return ruleId; }
    public void setRuleId(String ruleId) { this.ruleId = ruleId; }
    
    public String getRuleName() { return ruleName; }
    public void setRuleName(String ruleName) { this.ruleName = ruleName; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getMetricName() { return metricName; }
    public void setMetricName(String metricName) { this.metricName = metricName; }
    
    public String getCondition() { return condition; }
    public void setCondition(String condition) { this.condition = condition; }
    
    public Double getThreshold() { return threshold; }
    public void setThreshold(Double threshold) { this.threshold = threshold; }
    
    public String getSeverity() { return severity; }
    public void setSeverity(String severity) { this.severity = severity; }
    
    public Boolean getEnabled() { return enabled; }
    public void setEnabled(Boolean enabled) { this.enabled = enabled; }
    
    public Integer getDuration() { return duration; }
    public void setDuration(Integer duration) { this.duration = duration; }
    
    public Map<String, Object> getLabels() { return labels; }
    public void setLabels(Map<String, Object> labels) { this.labels = labels; }
    
    public Map<String, Object> getAnnotations() { return annotations; }
    public void setAnnotations(Map<String, Object> annotations) { this.annotations = annotations; }
    
    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
    
    public LocalDateTime getUpdateTime() { return updateTime; }
    public void setUpdateTime(LocalDateTime updateTime) { this.updateTime = updateTime; }
    
    @Override
    public String toString() {
        return String.format("AlertRule{ruleId='%s', ruleName='%s', metricName='%s', condition='%s', threshold=%s, severity='%s', enabled=%s}", 
                ruleId, ruleName, metricName, condition, threshold, severity, enabled);
    }
}