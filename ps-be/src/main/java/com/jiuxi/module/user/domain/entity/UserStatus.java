package com.jiuxi.module.user.domain.entity;

/**
 * 用户状态枚举
 * 
 * @author DDD Refactor
 * @date 2025-09-06
 */
public enum UserStatus {
    
    /**
     * 激活状态
     */
    ACTIVE(1, "激活"),
    
    /**
     * 非激活状态
     */
    INACTIVE(0, "非激活");
    
    private final Integer value;
    private final String description;
    
    UserStatus(Integer value, String description) {
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
     * 检查是否为激活状态
     */
    public boolean isActive() {
        return this == ACTIVE;
    }
    
    /**
     * 根据值获取枚举
     */
    public static UserStatus fromValue(Integer value) {
        for (UserStatus status : values()) {
            if (status.getValue().equals(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("未知的用户状态值: " + value);
    }
}