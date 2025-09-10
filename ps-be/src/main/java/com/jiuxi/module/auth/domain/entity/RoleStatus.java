package com.jiuxi.module.auth.domain.entity;

/**
 * 角色状态枚举
 * 
 * @author DDD Refactor
 * @date 2025-09-06
 */
public enum RoleStatus {
    
    /**
     * 激活
     */
    ACTIVE("ACTIVE", "激活"),
    
    /**
     * 停用
     */
    INACTIVE("INACTIVE", "停用");
    
    private final String code;
    private final String description;
    
    RoleStatus(String code, String description) {
        this.code = code;
        this.description = description;
    }
    
    public String getCode() {
        return code;
    }
    
    public String getDescription() {
        return description;
    }
    
    public static RoleStatus fromCode(String code) {
        for (RoleStatus status : values()) {
            if (status.code.equals(code)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown role status code: " + code);
    }
}