package com.jiuxi.module.org.domain.model.entity;

/**
 * 部门类型枚举
 * 
 * @author DDD Refactor
 * @date 2025-09-06
 */
public enum DepartmentType {
    
    /**
     * 公司
     */
    COMPANY("COMPANY", "公司"),
    
    /**
     * 部门
     */
    DEPARTMENT("DEPT", "部门"),
    
    /**
     * 小组
     */
    GROUP("GROUP", "小组"),
    
    /**
     * 虚拟部门
     */
    VIRTUAL("VIRTUAL", "虚拟部门");
    
    private final String code;
    private final String description;
    
    DepartmentType(String code, String description) {
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
    public static DepartmentType fromCode(String code) {
        for (DepartmentType type : values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        throw new IllegalArgumentException("未知的部门类型代码: " + code);
    }
}