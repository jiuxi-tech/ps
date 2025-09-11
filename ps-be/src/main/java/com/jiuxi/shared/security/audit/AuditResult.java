package com.jiuxi.shared.security.audit;

/**
 * 审计事件结果枚举
 * 
 * @author Security Refactoring
 * @since Phase 4.2.5
 */
public enum AuditResult {
    
    /**
     * 成功
     */
    SUCCESS("成功", "操作成功完成"),
    
    /**
     * 失败
     */
    FAILURE("失败", "操作执行失败"),
    
    /**
     * 警告
     */
    WARNING("警告", "操作完成但有警告"),
    
    /**
     * 拒绝
     */
    DENIED("拒绝", "操作被拒绝执行"),
    
    /**
     * 超时
     */
    TIMEOUT("超时", "操作执行超时"),
    
    /**
     * 未知
     */
    UNKNOWN("未知", "操作结果未知");

    private final String displayName;
    private final String description;

    AuditResult(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }

    /**
     * 检查是否为成功结果
     */
    public boolean isSuccess() {
        return this == SUCCESS;
    }

    /**
     * 检查是否为失败结果
     */
    public boolean isFailure() {
        return this == FAILURE || this == DENIED || this == TIMEOUT;
    }

    /**
     * 检查是否需要告警
     */
    public boolean requiresAlert() {
        return this == FAILURE || this == DENIED;
    }

    /**
     * 根据名称获取结果
     */
    public static AuditResult fromName(String name) {
        for (AuditResult result : values()) {
            if (result.name().equalsIgnoreCase(name)) {
                return result;
            }
        }
        throw new IllegalArgumentException("Unknown audit result: " + name);
    }
}