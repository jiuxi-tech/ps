package com.jiuxi.module.role.domain.model.vo;

import java.util.Objects;

/**
 * 角色ID值对象
 * 
 * @author DDD重构
 * @date 2025-09-15
 */
public class RoleId {
    
    private final String value;
    
    private RoleId(String value) {
        this.value = Objects.requireNonNull(value, "角色ID不能为空");
        if (value.trim().isEmpty()) {
            throw new IllegalArgumentException("角色ID不能为空字符串");
        }
    }
    
    public static RoleId of(String value) {
        return new RoleId(value);
    }
    
    public String getValue() {
        return value;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RoleId roleId = (RoleId) o;
        return Objects.equals(value, roleId.value);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
    
    @Override
    public String toString() {
        return "RoleId{" + "value='" + value + '\'' + '}';
    }
}