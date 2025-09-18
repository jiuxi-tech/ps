package com.jiuxi.module.auth.domain.model.vo;

import java.util.Objects;

/**
 * 租户ID值对象
 * 封装多租户架构中的租户标识符
 * 
 * @author DDD Refactor
 * @date 2025-09-18
 */
public class TenantId {
    
    private final String value;
    
    public TenantId(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("租户ID不能为空");
        }
        this.value = value.trim();
    }
    
    public String getValue() {
        return value;
    }
    
    /**
     * 检查是否是默认租户
     */
    public boolean isDefaultTenant() {
        return "DEFAULT".equals(value) || "SYSTEM".equals(value);
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TenantId tenantId = (TenantId) o;
        return Objects.equals(value, tenantId.value);
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