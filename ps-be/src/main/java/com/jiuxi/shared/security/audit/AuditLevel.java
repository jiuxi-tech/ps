package com.jiuxi.shared.security.audit;

/**
 * 审计事件级别枚举
 * 
 * @author Security Refactoring
 * @since Phase 4.2.5
 */
public enum AuditLevel {
    
    /**
     * 低级别 - 一般信息记录
     */
    LOW("低", "一般信息记录，如数据查询", 1),
    
    /**
     * 信息级别 - 正常操作记录
     */
    INFO("信息", "正常操作记录，如登录成功", 2),
    
    /**
     * 中等级别 - 需要关注的操作
     */
    MEDIUM("中等", "需要关注的操作，如权限变更", 3),
    
    /**
     * 高级别 - 重要操作或潜在风险
     */
    HIGH("高", "重要操作或潜在风险，如敏感数据访问", 4),
    
    /**
     * 严重级别 - 安全威胁或严重错误
     */
    CRITICAL("严重", "安全威胁或严重错误，如攻击检测", 5);

    private final String displayName;
    private final String description;
    private final int priority;

    AuditLevel(String displayName, String description, int priority) {
        this.displayName = displayName;
        this.description = description;
        this.priority = priority;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }

    public int getPriority() {
        return priority;
    }

    /**
     * 检查是否为高优先级
     */
    public boolean isHighPriority() {
        return priority >= HIGH.priority;
    }

    /**
     * 检查是否需要实时处理
     */
    public boolean requiresRealTimeProcessing() {
        return priority >= HIGH.priority;
    }

    /**
     * 根据优先级获取级别
     */
    public static AuditLevel fromPriority(int priority) {
        for (AuditLevel level : values()) {
            if (level.priority == priority) {
                return level;
            }
        }
        throw new IllegalArgumentException("Unknown audit level priority: " + priority);
    }

    /**
     * 根据名称获取级别
     */
    public static AuditLevel fromName(String name) {
        for (AuditLevel level : values()) {
            if (level.name().equalsIgnoreCase(name)) {
                return level;
            }
        }
        throw new IllegalArgumentException("Unknown audit level: " + name);
    }
}