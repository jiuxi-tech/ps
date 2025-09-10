package com.jiuxi.module.user.domain.entity;

/**
 * 账户状态枚举
 * 
 * @author DDD Refactor
 * @date 2025-09-06
 */
public enum AccountStatus {
    
    /**
     * 激活状态
     */
    ACTIVE(1, "激活"),
    
    /**
     * 非激活状态
     */
    INACTIVE(0, "非激活"),
    
    /**
     * 已删除状态
     */
    DELETED(-1, "已删除");
    
    private final Integer value;
    private final String description;
    
    AccountStatus(Integer value, String description) {
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
    public static AccountStatus fromValue(Integer value) {
        for (AccountStatus status : values()) {
            if (status.getValue().equals(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("未知的账户状态值: " + value);
    }
}