package com.jiuxi.module.system.domain.entity;

/**
 * @ClassName: LogType
 * @Description: 日志类型枚举
 * @Author: System
 * @Date: 2025/01/07
 * @Copyright: 2025 www.jiuxi.com Inc. All rights reserved.
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
     * 异常日志
     */
    EXCEPTION("EXCEPTION", "异常日志"),
    
    /**
     * 安全日志
     */
    SECURITY("SECURITY", "安全日志"),
    
    /**
     * 审计日志
     */
    AUDIT("AUDIT", "审计日志"),
    
    /**
     * 性能日志
     */
    PERFORMANCE("PERFORMANCE", "性能日志"),
    
    /**
     * 接口调用日志
     */
    API("API", "接口调用日志");
    
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
     * 根据代码获取枚举
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
     * 检查是否为操作日志
     */
    public boolean isOperation() {
        return this == OPERATION;
    }
    
    /**
     * 检查是否为安全相关日志
     */
    public boolean isSecurity() {
        return this == SECURITY || this == LOGIN;
    }
    
    /**
     * 检查是否为系统级日志
     */
    public boolean isSystemLevel() {
        return this == SYSTEM || this == EXCEPTION || this == PERFORMANCE;
    }
}