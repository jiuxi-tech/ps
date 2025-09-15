package com.jiuxi.module.sys.domain.entity;

/**
 * 日志级别枚举
 * 定义系统支持的日志级别
 * 
 * @author DDD Refactor
 * @date 2025-09-11
 */
public enum LogLevel {
    
    /**
     * 调试级别
     */
    DEBUG("DEBUG", "调试", 1),
    
    /**
     * 信息级别
     */
    INFO("INFO", "信息", 2),
    
    /**
     * 警告级别
     */
    WARN("WARN", "警告", 3),
    
    /**
     * 错误级别
     */
    ERROR("ERROR", "错误", 4),
    
    /**
     * 致命级别
     */
    FATAL("FATAL", "致命", 5);
    
    private final String code;
    private final String description;
    private final Integer level;
    
    LogLevel(String code, String description, Integer level) {
        this.code = code;
        this.description = description;
        this.level = level;
    }
    
    public String getCode() {
        return code;
    }
    
    public String getDescription() {
        return description;
    }
    
    public Integer getLevel() {
        return level;
    }
    
    /**
     * 根据代码获取日志级别
     * @param code 代码
     * @return 日志级别
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
     * 判断是否为严重级别
     * @return 是否严重
     */
    public boolean isSevere() {
        return this.level >= ERROR.level;
    }
    
    /**
     * 比较级别高低
     * @param other 其他级别
     * @return 比较结果
     */
    public int compareLevel(LogLevel other) {
        return this.level.compareTo(other.level);
    }
}