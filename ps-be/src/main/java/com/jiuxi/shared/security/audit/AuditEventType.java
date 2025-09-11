package com.jiuxi.shared.security.audit;

/**
 * 审计事件类型枚举
 * 定义系统中所有可审计的事件类型
 * 
 * @author Security Refactoring
 * @since Phase 4.2.5
 */
public enum AuditEventType {

    // ================================ 认证事件 ================================
    LOGIN_SUCCESS("用户登录成功", "用户成功登录系统", AuditCategory.AUTHENTICATION, AuditLevel.INFO),
    LOGIN_FAILURE("用户登录失败", "用户登录失败", AuditCategory.AUTHENTICATION, AuditLevel.MEDIUM),
    LOGIN_LOCKED("用户账户锁定", "用户因多次登录失败被锁定", AuditCategory.AUTHENTICATION, AuditLevel.HIGH),
    LOGOUT_SUCCESS("用户注销成功", "用户成功注销系统", AuditCategory.AUTHENTICATION, AuditLevel.INFO),
    PASSWORD_CHANGE("密码修改", "用户修改密码", AuditCategory.AUTHENTICATION, AuditLevel.MEDIUM),
    PASSWORD_RESET("密码重置", "管理员重置用户密码", AuditCategory.AUTHENTICATION, AuditLevel.MEDIUM),
    ACCOUNT_UNLOCK("账户解锁", "管理员解锁用户账户", AuditCategory.AUTHENTICATION, AuditLevel.MEDIUM),
    MULTI_FACTOR_AUTH("多因子认证", "用户使用多因子认证", AuditCategory.AUTHENTICATION, AuditLevel.INFO),
    SESSION_TIMEOUT("会话超时", "用户会话超时自动注销", AuditCategory.AUTHENTICATION, AuditLevel.INFO),
    CONCURRENT_SESSION("并发会话", "检测到用户并发会话", AuditCategory.AUTHENTICATION, AuditLevel.MEDIUM),

    // ================================ 授权事件 ================================
    ACCESS_GRANTED("访问授权", "用户获得资源访问权限", AuditCategory.AUTHORIZATION, AuditLevel.INFO),
    ACCESS_DENIED("访问拒绝", "用户被拒绝访问资源", AuditCategory.AUTHORIZATION, AuditLevel.MEDIUM),
    PRIVILEGE_ESCALATION("权限提升", "检测到权限提升尝试", AuditCategory.AUTHORIZATION, AuditLevel.HIGH),
    ROLE_ASSIGNED("角色分配", "为用户分配角色", AuditCategory.AUTHORIZATION, AuditLevel.MEDIUM),
    ROLE_REMOVED("角色移除", "移除用户角色", AuditCategory.AUTHORIZATION, AuditLevel.MEDIUM),
    PERMISSION_GRANTED("权限授予", "为用户授予特定权限", AuditCategory.AUTHORIZATION, AuditLevel.MEDIUM),
    PERMISSION_REVOKED("权限撤销", "撤销用户特定权限", AuditCategory.AUTHORIZATION, AuditLevel.MEDIUM),
    ADMIN_ACCESS("管理员访问", "管理员访问系统管理功能", AuditCategory.AUTHORIZATION, AuditLevel.HIGH),

    // ================================ 数据操作事件 ================================
    DATA_CREATE("数据创建", "创建新的数据记录", AuditCategory.DATA_ACCESS, AuditLevel.INFO),
    DATA_READ("数据读取", "读取数据记录", AuditCategory.DATA_ACCESS, AuditLevel.LOW),
    DATA_UPDATE("数据更新", "更新数据记录", AuditCategory.DATA_ACCESS, AuditLevel.MEDIUM),
    DATA_DELETE("数据删除", "删除数据记录", AuditCategory.DATA_ACCESS, AuditLevel.HIGH),
    DATA_EXPORT("数据导出", "导出数据记录", AuditCategory.DATA_ACCESS, AuditLevel.MEDIUM),
    DATA_IMPORT("数据导入", "导入数据记录", AuditCategory.DATA_ACCESS, AuditLevel.MEDIUM),
    BULK_OPERATION("批量操作", "执行批量数据操作", AuditCategory.DATA_ACCESS, AuditLevel.MEDIUM),
    SENSITIVE_DATA_ACCESS("敏感数据访问", "访问敏感数据", AuditCategory.DATA_ACCESS, AuditLevel.HIGH),

    // ================================ 系统事件 ================================
    SYSTEM_START("系统启动", "系统启动", AuditCategory.SYSTEM, AuditLevel.INFO),
    SYSTEM_SHUTDOWN("系统关闭", "系统关闭", AuditCategory.SYSTEM, AuditLevel.INFO),
    CONFIG_CHANGE("配置修改", "系统配置被修改", AuditCategory.SYSTEM, AuditLevel.HIGH),
    BACKUP_CREATE("备份创建", "创建系统备份", AuditCategory.SYSTEM, AuditLevel.INFO),
    BACKUP_RESTORE("备份恢复", "从备份恢复系统", AuditCategory.SYSTEM, AuditLevel.HIGH),
    DATABASE_MIGRATION("数据库迁移", "执行数据库迁移", AuditCategory.SYSTEM, AuditLevel.HIGH),
    MAINTENANCE_MODE("维护模式", "系统进入维护模式", AuditCategory.SYSTEM, AuditLevel.MEDIUM),
    SERVICE_ERROR("服务错误", "系统服务发生错误", AuditCategory.SYSTEM, AuditLevel.HIGH),

    // ================================ 安全事件 ================================
    INTRUSION_ATTEMPT("入侵尝试", "检测到系统入侵尝试", AuditCategory.SECURITY, AuditLevel.CRITICAL),
    SUSPICIOUS_ACTIVITY("可疑活动", "检测到可疑用户活动", AuditCategory.SECURITY, AuditLevel.HIGH),
    BRUTE_FORCE_ATTACK("暴力破解", "检测到暴力破解攻击", AuditCategory.SECURITY, AuditLevel.CRITICAL),
    SQL_INJECTION("SQL注入", "检测到SQL注入攻击", AuditCategory.SECURITY, AuditLevel.CRITICAL),
    XSS_ATTACK("跨站脚本", "检测到XSS攻击", AuditCategory.SECURITY, AuditLevel.HIGH),
    CSRF_ATTACK("跨站请求伪造", "检测到CSRF攻击", AuditCategory.SECURITY, AuditLevel.HIGH),
    IP_BLOCKED("IP地址封禁", "IP地址被加入黑名单", AuditCategory.SECURITY, AuditLevel.MEDIUM),
    SECURITY_SCAN("安全扫描", "执行安全漏洞扫描", AuditCategory.SECURITY, AuditLevel.INFO),
    VIRUS_DETECTED("病毒检测", "检测到恶意文件", AuditCategory.SECURITY, AuditLevel.CRITICAL),

    // ================================ 业务事件 ================================
    ORDER_CREATE("订单创建", "创建新订单", AuditCategory.BUSINESS, AuditLevel.INFO),
    ORDER_CANCEL("订单取消", "取消订单", AuditCategory.BUSINESS, AuditLevel.MEDIUM),
    PAYMENT_SUCCESS("支付成功", "支付处理成功", AuditCategory.BUSINESS, AuditLevel.INFO),
    PAYMENT_FAILURE("支付失败", "支付处理失败", AuditCategory.BUSINESS, AuditLevel.MEDIUM),
    REFUND_PROCESS("退款处理", "处理退款请求", AuditCategory.BUSINESS, AuditLevel.MEDIUM),
    CONTRACT_SIGN("合同签署", "签署合同", AuditCategory.BUSINESS, AuditLevel.MEDIUM),
    WORKFLOW_START("工作流启动", "启动业务工作流", AuditCategory.BUSINESS, AuditLevel.INFO),
    APPROVAL_GRANTED("审批通过", "审批请求通过", AuditCategory.BUSINESS, AuditLevel.INFO),
    APPROVAL_REJECTED("审批拒绝", "审批请求被拒绝", AuditCategory.BUSINESS, AuditLevel.MEDIUM),

    // ================================ 合规事件 ================================
    COMPLIANCE_CHECK("合规检查", "执行合规性检查", AuditCategory.COMPLIANCE, AuditLevel.INFO),
    POLICY_VIOLATION("策略违反", "检测到策略违反", AuditCategory.COMPLIANCE, AuditLevel.HIGH),
    AUDIT_LOG_ACCESS("审计日志访问", "访问审计日志", AuditCategory.COMPLIANCE, AuditLevel.HIGH),
    GDPR_REQUEST("GDPR请求", "处理GDPR数据请求", AuditCategory.COMPLIANCE, AuditLevel.HIGH),
    DATA_RETENTION("数据保留", "执行数据保留策略", AuditCategory.COMPLIANCE, AuditLevel.MEDIUM),
    ENCRYPTION_FAILURE("加密失败", "数据加密过程失败", AuditCategory.COMPLIANCE, AuditLevel.HIGH);

    private final String displayName;
    private final String description;
    private final AuditCategory category;
    private final AuditLevel defaultLevel;

    AuditEventType(String displayName, String description, AuditCategory category, AuditLevel defaultLevel) {
        this.displayName = displayName;
        this.description = description;
        this.category = category;
        this.defaultLevel = defaultLevel;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }

    public AuditCategory getCategory() {
        return category;
    }

    public AuditLevel getDefaultLevel() {
        return defaultLevel;
    }

    /**
     * 根据事件代码查找事件类型
     */
    public static AuditEventType fromCode(String code) {
        try {
            return AuditEventType.valueOf(code.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Unknown audit event type: " + code);
        }
    }

    /**
     * 获取事件代码
     */
    public String getCode() {
        return this.name();
    }

    /**
     * 检查是否为认证相关事件
     */
    public boolean isAuthenticationEvent() {
        return this.category == AuditCategory.AUTHENTICATION;
    }

    /**
     * 检查是否为授权相关事件
     */
    public boolean isAuthorizationEvent() {
        return this.category == AuditCategory.AUTHORIZATION;
    }

    /**
     * 检查是否为安全事件
     */
    public boolean isSecurityEvent() {
        return this.category == AuditCategory.SECURITY;
    }

    /**
     * 检查是否为高风险事件
     */
    public boolean isHighRiskEvent() {
        return this.defaultLevel == AuditLevel.HIGH || this.defaultLevel == AuditLevel.CRITICAL;
    }

    /**
     * 检查是否需要实时告警
     */
    public boolean requiresRealTimeAlert() {
        return this.defaultLevel == AuditLevel.CRITICAL || 
               (this.defaultLevel == AuditLevel.HIGH && this.isSecurityEvent());
    }
}