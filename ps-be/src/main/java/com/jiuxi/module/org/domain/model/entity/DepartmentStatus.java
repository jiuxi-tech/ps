package com.jiuxi.module.org.domain.model.entity;

/**
 * 部门状态枚举
 * 
 * @author DDD Refactor
 * @date 2025-09-06
 */
public enum DepartmentStatus {
    
    /**
     * 激活状态
     */
    ACTIVE(1, "激活"),
    
    /**
     * 停用状态
     */
    INACTIVE(0, "停用");
    
    private final Integer value;
    private final String description;
    
    DepartmentStatus(Integer value, String description) {
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
    public static DepartmentStatus fromValue(Integer value) {
        for (DepartmentStatus status : values()) {
            if (status.getValue().equals(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("未知的部门状态值: " + value);
    }
}