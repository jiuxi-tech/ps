package com.jiuxi.module.auth.domain.model.entity;

/**
 * 菜单类型枚举
 * 
 * @author DDD Refactor
 * @date 2025-09-06
 */
public enum MenuType {
    
    /**
     * 目录
     */
    DIRECTORY("DIRECTORY", "目录"),
    
    /**
     * 菜单
     */
    MENU("MENU", "菜单"),
    
    /**
     * 按钮
     */
    BUTTON("BUTTON", "按钮");
    
    private final String code;
    private final String description;
    
    MenuType(String code, String description) {
        this.code = code;
        this.description = description;
    }
    
    public String getCode() {
        return code;
    }
    
    public String getDescription() {
        return description;
    }
    
    public static MenuType fromCode(String code) {
        for (MenuType type : values()) {
            if (type.code.equals(code)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown menu type code: " + code);
    }
}