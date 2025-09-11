package com.jiuxi.module.org.domain.entity;

/**
 * 组织状态枚举
 * 
 * @author DDD Refactor
 * @date 2025-09-11
 */
public enum OrganizationStatus {
    
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
    
    OrganizationStatus(Integer value, String description) {
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
    public static OrganizationStatus fromValue(Integer value) {
        for (OrganizationStatus status : values()) {
            if (status.getValue().equals(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("未知的组织状态值: " + value);
    }
}