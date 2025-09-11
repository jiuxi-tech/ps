package com.jiuxi.module.sys.domain.entity;

/**
 * 配置类型枚举
 * 定义系统支持的配置类型
 * 
 * @author DDD Refactor
 * @date 2025-09-11
 */
public enum ConfigType {
    
    /**
     * 系统配置
     */
    SYSTEM("SYSTEM", "系统配置"),
    
    /**
     * 应用配置
     */
    APPLICATION("APPLICATION", "应用配置"),
    
    /**
     * 业务配置
     */
    BUSINESS("BUSINESS", "业务配置"),
    
    /**
     * 安全配置
     */
    SECURITY("SECURITY", "安全配置"),
    
    /**
     * 缓存配置
     */
    CACHE("CACHE", "缓存配置"),
    
    /**
     * 数据库配置
     */
    DATABASE("DATABASE", "数据库配置"),
    
    /**
     * 监控配置
     */
    MONITOR("MONITOR", "监控配置"),
    
    /**
     * 通知配置
     */
    NOTIFICATION("NOTIFICATION", "通知配置");
    
    private final String code;
    private final String description;
    
    ConfigType(String code, String description) {
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
     * 根据代码获取配置类型
     * @param code 代码
     * @return 配置类型
     */
    public static ConfigType fromCode(String code) {
        for (ConfigType type : values()) {
            if (type.code.equals(code)) {
                return type;
            }
        }
        throw new IllegalArgumentException("未知的配置类型代码: " + code);
    }
    
    /**
     * 检查是否是系统级配置
     * @return 是否是系统级配置
     */
    public boolean isSystemLevel() {
        return this == SYSTEM || this == SECURITY || this == DATABASE || this == MONITOR;
    }
    
    /**
     * 检查是否是应用级配置
     * @return 是否是应用级配置
     */
    public boolean isApplicationLevel() {
        return this == APPLICATION || this == BUSINESS || this == CACHE || this == NOTIFICATION;
    }
}