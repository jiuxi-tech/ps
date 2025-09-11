package com.jiuxi.shared.security.audit;

/**
 * 审计事件分类枚举
 * 
 * @author Security Refactoring
 * @since Phase 4.2.5
 */
public enum AuditCategory {
    
    /**
     * 认证相关事件
     */
    AUTHENTICATION("认证", "用户身份验证相关事件"),
    
    /**
     * 授权相关事件
     */
    AUTHORIZATION("授权", "用户权限和授权相关事件"),
    
    /**
     * 数据访问事件
     */
    DATA_ACCESS("数据访问", "数据读取、修改、删除等操作事件"),
    
    /**
     * 系统事件
     */
    SYSTEM("系统", "系统启动、配置变更等系统级事件"),
    
    /**
     * 安全事件
     */
    SECURITY("安全", "安全威胁、攻击检测等安全事件"),
    
    /**
     * 业务事件
     */
    BUSINESS("业务", "业务流程相关事件"),
    
    /**
     * 合规事件
     */
    COMPLIANCE("合规", "法规遵循、策略执行相关事件");

    private final String displayName;
    private final String description;

    AuditCategory(String displayName, String description) {
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
     * 根据名称获取分类
     */
    public static AuditCategory fromName(String name) {
        for (AuditCategory category : values()) {
            if (category.name().equalsIgnoreCase(name)) {
                return category;
            }
        }
        throw new IllegalArgumentException("Unknown audit category: " + name);
    }
}