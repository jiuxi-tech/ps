package com.jiuxi.module.sys.domain.entity;

/**
 * 日志类型枚举
 * 定义系统支持的日志类型
 * 
 * @author DDD Refactor
 * @date 2025-09-11
 */
public enum LogType {
    
    /**
     * 操作日志
     */
    OPERATION("OPERATION", "操作日志"),
    
    /**
     * 登录日志
     */
    LOGIN("LOGIN", "登录日志"),
    
    /**
     * 系统日志
     */
    SYSTEM("SYSTEM", "系统日志"),
    
    /**
     * 错误日志
     */
    ERROR("ERROR", "错误日志"),
    
    /**
     * 安全日志
     */
    SECURITY("SECURITY", "安全日志"),
    
    /**
     * 审计日志
     */
    AUDIT("AUDIT", "审计日志");
    
    private final String code;
    private final String description;
    
    LogType(String code, String description) {
        this.code = code;
        this.description = description;
    }
    
    public String getCode() {
        return code;
    }
    
    public String getDescription() {
        return description;
    }
    
    /**
     * 根据代码获取日志类型
     * @param code 代码
     * @return 日志类型
     */
    public static LogType fromCode(String code) {
        for (LogType type : values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown log type code: " + code);
    }
    
    /**
     * 判断是否为系统级日志
     * @return 是否为系统级
     */
    public boolean isSystemLevel() {
        return this == SYSTEM || this == ERROR || this == SECURITY;
    }
    
    /**
     * 判断是否为用户操作日志
     * @return 是否为用户操作
     */
    public boolean isUserOperation() {
        return this == OPERATION || this == LOGIN;
    }
}