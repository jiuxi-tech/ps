package com.jiuxi.module.user.domain.model.vo;

/**
 * 用户类别枚举
 * 
 * @author DDD Refactor
 * @date 2025-09-06
 */
public enum UserCategory {
    
    /**
     * 个人用户
     */
    PERSONAL("PERSONAL", "个人"),
    
    /**
     * 组织用户
     */
    ORGANIZATION("ORG", "组织"),
    
    /**
     * 企业用户
     */
    ENTERPRISE("ENT", "企业");
    
    private final String code;
    private final String description;
    
    UserCategory(String code, String description) {
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
    public static UserCategory fromCode(String code) {
        for (UserCategory category : values()) {
            if (category.getCode().equals(code)) {
                return category;
            }
        }
        throw new IllegalArgumentException("未知的用户类别代码: " + code);
    }
}