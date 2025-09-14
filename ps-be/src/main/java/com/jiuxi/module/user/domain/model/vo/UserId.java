package com.jiuxi.module.user.domain.model.vo;

import java.util.Objects;
import java.util.UUID;

/**
 * 用户ID值对象
 * 
 * @author DDD Refactor Phase 4.3.1.1
 * @date 2025-09-11
 */
public class UserId {
    
    private final String value;
    
    public UserId(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("用户ID不能为空");
        }
        this.value = value.trim();
    }
    
    /**
     * 生成新的用户ID
     */
    public static UserId generate() {
        return new UserId(UUID.randomUUID().toString().replace("-", ""));
    }
    
    /**
     * 从字符串创建用户ID
     */
    public static UserId of(String value) {
        return new UserId(value);
    }
    
    public String getValue() {
        return value;
    }
    
    /**
     * 检查是否有效
     */
    public boolean isValid() {
        return value != null && !value.trim().isEmpty();
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserId userId = (UserId) o;
        return Objects.equals(value, userId.value);
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