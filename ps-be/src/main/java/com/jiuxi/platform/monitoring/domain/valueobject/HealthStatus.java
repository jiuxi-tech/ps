package com.jiuxi.platform.monitoring.domain.valueobject;

import java.util.Objects;

/**
 * 健康状态值对象
 * 
 * @author DDD Refactor
 * @date 2025-09-11
 */
public class HealthStatus {
    
    /**
     * 健康状态枚举
     */
    public enum Status {
        HEALTHY("健康", 1),
        WARNING("警告", 2), 
        UNHEALTHY("不健康", 3),
        CRITICAL("严重", 4),
        UNKNOWN("未知", 0);
        
        private final String description;
        private final int level;
        
        Status(String description, int level) {
            this.description = description;
            this.level = level;
        }
        
        public String getDescription() {
            return description;
        }
        
        public int getLevel() {
            return level;
        }
    }
    
    private final Status status;
    private final String component;
    private final String message;
    private final String details;
    
    public HealthStatus(Status status, String component, String message, String details) {
        this.status = status != null ? status : Status.UNKNOWN;
        this.component = component != null ? component.trim() : "unknown";
        this.message = message != null ? message.trim() : "";
        this.details = details != null ? details.trim() : "";
    }
    
    /**
     * 创建健康状态
     */
    public static HealthStatus healthy(String component, String message) {
        return new HealthStatus(Status.HEALTHY, component, message, null);
    }
    
    /**
     * 创建警告状态
     */
    public static HealthStatus warning(String component, String message, String details) {
        return new HealthStatus(Status.WARNING, component, message, details);
    }
    
    /**
     * 创建不健康状态
     */
    public static HealthStatus unhealthy(String component, String message, String details) {
        return new HealthStatus(Status.UNHEALTHY, component, message, details);
    }
    
    /**
     * 创建严重状态
     */
    public static HealthStatus critical(String component, String message, String details) {
        return new HealthStatus(Status.CRITICAL, component, message, details);
    }
    
    /**
     * 创建未知状态
     */
    public static HealthStatus unknown(String component, String message) {
        return new HealthStatus(Status.UNKNOWN, component, message, null);
    }
    
    /**
     * 判断是否健康
     */
    public boolean isHealthy() {
        return status == Status.HEALTHY;
    }
    
    /**
     * 判断是否需要警告
     */
    public boolean needsWarning() {
        return status == Status.WARNING;
    }
    
    /**
     * 判断是否不健康
     */
    public boolean isUnhealthy() {
        return status == Status.UNHEALTHY || status == Status.CRITICAL;
    }
    
    /**
     * 判断是否为严重状态
     */
    public boolean isCritical() {
        return status == Status.CRITICAL;
    }
    
    /**
     * 获取状态级别
     */
    public int getLevel() {
        return status.getLevel();
    }
    
    /**
     * 比较两个健康状态的严重程度
     * @param other 另一个健康状态
     * @return 负数表示当前状态更好，正数表示更差，0表示相同
     */
    public int compareLevel(HealthStatus other) {
        return Integer.compare(this.getLevel(), other.getLevel());
    }
    
    public Status getStatus() {
        return status;
    }
    
    public String getComponent() {
        return component;
    }
    
    public String getMessage() {
        return message;
    }
    
    public String getDetails() {
        return details;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HealthStatus that = (HealthStatus) o;
        return status == that.status && 
               Objects.equals(component, that.component) && 
               Objects.equals(message, that.message) && 
               Objects.equals(details, that.details);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(status, component, message, details);
    }
    
    @Override
    public String toString() {
        return String.format("HealthStatus{status=%s, component='%s', message='%s'}", 
                status, component, message);
    }
}