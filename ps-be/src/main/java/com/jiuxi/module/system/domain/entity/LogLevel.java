package com.jiuxi.module.system.domain.entity;

/**
 * @ClassName: LogLevel
 * @Description: 日志级别枚举
 * @Author: System
 * @Date: 2025/01/07
 * @Copyright: 2025 www.jiuxi.com Inc. All rights reserved.
 */
public enum LogLevel {
    
    /**
     * 调试级别
     */
    DEBUG(1, "DEBUG", "调试"),
    
    /**
     * 信息级别
     */
    INFO(2, "INFO", "信息"),
    
    /**
     * 警告级别
     */
    WARN(3, "WARN", "警告"),
    
    /**
     * 错误级别
     */
    ERROR(4, "ERROR", "错误"),
    
    /**
     * 致命错误级别
     */
    FATAL(5, "FATAL", "致命错误");
    
    private final Integer level;
    private final String code;
    private final String description;
    
    LogLevel(Integer level, String code, String description) {
        this.level = level;
        this.code = code;
        this.description = description;
    }
    
    public Integer getLevel() {
        return level;
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
    public static LogLevel fromCode(String code) {
        for (LogLevel level : values()) {
            if (level.getCode().equals(code)) {
                return level;
            }
        }
        throw new IllegalArgumentException("Unknown log level code: " + code);
    }
    
    /**
     * 根据级别获取枚举
     */
    public static LogLevel fromLevel(Integer level) {
        for (LogLevel logLevel : values()) {
            if (logLevel.getLevel().equals(level)) {
                return logLevel;
            }
        }
        throw new IllegalArgumentException("Unknown log level: " + level);
    }
    
    /**
     * 检查是否为错误级别
     */
    public boolean isError() {
        return this == ERROR || this == FATAL;
    }
    
    /**
     * 检查是否为警告级别或以上
     */
    public boolean isWarnOrAbove() {
        return this.level >= WARN.level;
    }
    
    /**
     * 比较级别高低
     */
    public boolean isHigherThan(LogLevel other) {
        return this.level > other.level;
    }
    
    /**
     * 比较级别高低
     */
    public boolean isLowerThan(LogLevel other) {
        return this.level < other.level;
    }
}