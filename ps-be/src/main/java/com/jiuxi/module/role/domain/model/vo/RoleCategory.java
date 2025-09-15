package com.jiuxi.module.role.domain.model.vo;

import java.util.Objects;

/**
 * 角色类别值对象
 * 
 * @author DDD重构
 * @date 2025-09-15
 */
public class RoleCategory {
    
    public static final RoleCategory GOVERNMENT = new RoleCategory(0, "政府角色");
    public static final RoleCategory GENERAL = new RoleCategory(1, "普通角色");
    
    private final Integer code;
    private final String description;
    
    private RoleCategory(Integer code, String description) {
        this.code = Objects.requireNonNull(code, "角色类别码不能为空");
        this.description = Objects.requireNonNull(description, "角色类别描述不能为空");
    }
    
    public static RoleCategory of(Integer code) {
        if (code == null) {
            throw new IllegalArgumentException("角色类别码不能为空");
        }
        switch (code) {
            case 0:
                return GOVERNMENT;
            case 1:
                return GENERAL;
            default:
                throw new IllegalArgumentException("无效的角色类别码: " + code);
        }
    }
    
    public Integer getCode() {
        return code;
    }
    
    public String getDescription() {
        return description;
    }
    
    /**
     * 获取值（兼容性方法）
     */
    public Integer getValue() {
        return code;
    }
    
    /**
     * 检查是否为政府角色
     */
    public boolean isGovernment() {
        return this.equals(GOVERNMENT);
    }
    
    /**
     * 检查是否为普通角色
     */
    public boolean isGeneral() {
        return this.equals(GENERAL);
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RoleCategory that = (RoleCategory) o;
        return Objects.equals(code, that.code);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(code);
    }
    
    @Override
    public String toString() {
        return "RoleCategory{" + "code=" + code + ", description='" + description + '\'' + '}';
    }
}