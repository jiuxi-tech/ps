package com.jiuxi.module.role.domain.model.vo;

import java.util.Objects;

/**
 * 权限ID值对象
 * 
 * @author DDD重构
 * @date 2025-09-15
 */
public class PermissionId {
    
    private final String value;
    
    private PermissionId(String value) {
        this.value = Objects.requireNonNull(value, "权限ID不能为空");
        if (value.trim().isEmpty()) {
            throw new IllegalArgumentException("权限ID不能为空字符串");
        }
    }
    
    public static PermissionId of(String value) {
        return new PermissionId(value);
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
        return "PermissionId{" + "value='" + value + '\'' + '}';
    }
}