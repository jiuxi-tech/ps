package com.jiuxi.module.auth.domain.model.entity;

/**
 * 数据权限范围枚举
 * 
 * @author DDD Refactor
 * @date 2025-09-06
 */
public enum DataScope {
    
    /**
     * 全部数据权限
     */
    ALL("ALL", "全部数据权限"),
    
    /**
     * 部门数据权限
     */
    DEPT("DEPT", "部门数据权限"),
    
    /**
     * 部门及以下数据权限
     */
    DEPT_AND_CHILD("DEPT_AND_CHILD", "部门及以下数据权限"),
    
    /**
     * 仅本人数据权限
     */
    SELF("SELF", "仅本人数据权限"),
    
    /**
     * 自定义数据权限
     */
    CUSTOM("CUSTOM", "自定义数据权限");
    
    private final String code;
    private final String description;
    
    DataScope(String code, String description) {
        this.code = code;
        this.description = description;
    }
    
    public String getCode() {
        return code;
    }
    
    public String getDescription() {
        return description;
    }
    
    public static DataScope fromCode(String code) {
        for (DataScope scope : values()) {
            if (scope.code.equals(code)) {
                return scope;
            }
        }
        throw new IllegalArgumentException("Unknown data scope code: " + code);
    }
}