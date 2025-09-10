package com.jiuxi.module.auth.domain.entity;

/**
 * 权限类型枚举
 * 
 * @author DDD Refactor
 * @date 2025-09-06
 */
public enum PermissionType {
    
    /**
     * API权限
     */
    API("API", "API权限"),
    
    /**
     * 菜单权限
     */
    MENU("MENU", "菜单权限"),
    
    /**
     * 按钮权限
     */
    BUTTON("BUTTON", "按钮权限"),
    
    /**
     * 数据权限
     */
    DATA("DATA", "数据权限");
    
    private final String code;
    private final String description;
    
    PermissionType(String code, String description) {
        this.code = code;
        this.description = description;
    }
    
    public String getCode() {
        return code;
    }
    
    public String getDescription() {
        return description;
    }
    
    public static PermissionType fromCode(String code) {
        for (PermissionType type : values()) {
            if (type.code.equals(code)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown permission type code: " + code);
    }
}