package com.jiuxi.module.auth.domain.model.entity;

/**
 * 菜单状态枚举
 * 
 * @author DDD Refactor
 * @date 2025-09-06
 */
public enum MenuStatus {
    
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
    
    MenuStatus(String code, String description) {
        this.code = code;
        this.description = description;
    }
    
    public String getCode() {
        return code;
    }
    
    public String getDescription() {
        return description;
    }
    
    public static MenuStatus fromCode(String code) {
        for (MenuStatus status : values()) {
            if (status.code.equals(code)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown menu status code: " + code);
    }
}