package com.jiuxi.module.role.domain.model.vo;

import java.util.Objects;

/**
 * 角色名称值对象
 * 
 * @author DDD重构
 * @date 2025-09-15
 */
public class RoleName {
    
    private final String value;
    
    private RoleName(String value) {
        this.value = Objects.requireNonNull(value, "角色名称不能为空");
        if (value.trim().isEmpty()) {
            throw new IllegalArgumentException("角色名称不能为空字符串");
        }
        if (value.length() > 50) {
            throw new IllegalArgumentException("角色名称长度不能超过50个字符");
        }
    }
    
    public static RoleName of(String value) {
        return new RoleName(value);
    }
    
    public String getValue() {
        return value;
    }
    
    /**
     * 检查角色名称是否包含指定关键词
     */
    public boolean contains(String keyword) {
        return value.toLowerCase().contains(keyword.toLowerCase());
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RoleName roleName = (RoleName) o;
        return Objects.equals(value, roleName.value);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
    
    @Override
    public String toString() {
        return "RoleName{" + "value='" + value + '\'' + '}';
    }
}