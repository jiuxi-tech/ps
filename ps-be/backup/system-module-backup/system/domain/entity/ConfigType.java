package com.jiuxi.module.system.domain.entity;

/**
 * @ClassName: ConfigType
 * @Description: 配置类型枚举
 * @Author: System
 * @Date: 2025/01/07
 * @Copyright: 2025 www.jiuxi.com Inc. All rights reserved.
 */
public enum ConfigType {
    
    /**
     * 系统配置
     */
    SYSTEM("SYSTEM", "系统配置"),
    
    /**
     * 业务配置
     */
    BUSINESS("BUSINESS", "业务配置"),
    
    /**
     * 安全配置
     */
    SECURITY("SECURITY", "安全配置"),
    
    /**
     * 集成配置
     */
    INTEGRATION("INTEGRATION", "集成配置"),
    
    /**
     * 界面配置
     */
    UI("UI", "界面配置"),
    
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
     * 根据代码获取枚举
     */
    public static ConfigType fromCode(String code) {
        for (ConfigType type : values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown config type code: " + code);
    }
    
    /**
     * 检查是否为系统配置
     */
    public boolean isSystem() {
        return this == SYSTEM;
    }
    
    /**
     * 检查是否为安全配置
     */
    public boolean isSecurity() {
        return this == SECURITY;
    }
}