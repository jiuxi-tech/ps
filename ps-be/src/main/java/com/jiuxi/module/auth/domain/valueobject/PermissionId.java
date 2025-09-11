package com.jiuxi.module.auth.domain.valueobject;

import java.util.Objects;

/**
 * 权限ID值对象
 * 
 * @author DDD Refactor
 * @date 2025-09-11
 */
public class PermissionId {
    
    private final String value;
    
    public PermissionId(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("权限ID不能为空");
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
        PermissionId that = (PermissionId) o;
        return Objects.equals(value, that.value);
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