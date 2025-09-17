package com.jiuxi.module.org.domain.model.entity;

/**
 * 企业状态枚举
 * 
 * @author DDD Refactor
 * @date 2025-09-11
 */
public enum EnterpriseStatus {
    
    /**
     * 激活状态
     */
    ACTIVE(1, "激活"),
    
    /**
     * 停用状态
     */
    INACTIVE(0, "停用"),
    
    /**
     * 已注销
     */
    CANCELLED(-1, "已注销");
    
    private final Integer value;
    private final String description;
    
    EnterpriseStatus(Integer value, String description) {
        this.value = value;
        this.description = description;
    }
    
    public Integer getValue() {
        return value;
    }
    
    public String getDescription() {
        return description;
    }
    
    /**
     * 根据值获取枚举
     */
    public static EnterpriseStatus fromValue(Integer value) {
        for (EnterpriseStatus status : values()) {
            if (status.getValue().equals(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("未知的企业状态值: " + value);
    }
    
    /**
     * 根据字符串获取枚举
     */
    public static EnterpriseStatus fromString(String name) {
        try {
            return EnterpriseStatus.valueOf(name.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("未知的企业状态: " + name);
        }
    }
}