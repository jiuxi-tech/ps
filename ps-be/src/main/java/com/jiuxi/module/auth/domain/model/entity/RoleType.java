package com.jiuxi.module.auth.domain.model.entity;

/**
 * 角色类型枚举
 * 
 * @author DDD Refactor
 * @date 2025-09-06
 */
public enum RoleType {
    
    /**
     * 系统角色
     */
    SYSTEM("SYSTEM", "系统角色"),
    
    /**
     * 业务角色
     */
    BUSINESS("BUSINESS", "业务角色"),
    
    /**
     * 部门角色
     */
    DEPARTMENT("DEPARTMENT", "部门角色"),
    
    /**
     * 临时角色
     */
    TEMPORARY("TEMPORARY", "临时角色");
    
    private final String code;
    private final String description;
    
    RoleType(String code, String description) {
        this.code = code;
        this.description = description;
    }
    
    public String getCode() {
        return code;
    }
    
    public String getDescription() {
        return description;
    }
    
    public static RoleType fromCode(String code) {
        for (RoleType type : values()) {
            if (type.code.equals(code)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown role type code: " + code);
    }
}