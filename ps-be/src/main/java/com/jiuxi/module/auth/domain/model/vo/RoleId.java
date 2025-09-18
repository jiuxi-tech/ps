package com.jiuxi.module.auth.domain.model.vo;

import java.util.Objects;

/**
 * 角色ID值对象
 * 
 * @author DDD Refactor
 * @date 2025-09-11
 */
public class RoleId {
    
    private final String value;
    
    public RoleId(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("角色ID不能为空");
        }
        this.value = value.trim();
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
        return value;
    }
}