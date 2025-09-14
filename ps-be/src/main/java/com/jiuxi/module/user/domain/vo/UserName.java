package com.jiuxi.module.user.domain.vo;

import java.util.Objects;

/**
 * 用户名值对象
 * 
 * @author DDD Refactor Phase 4.3.1.1
 * @date 2025-09-11
 */
public class UserName {
    
    private final String value;
    
    public UserName(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("用户名不能为空");
        }
        if (value.trim().length() < 2) {
            throw new IllegalArgumentException("用户名长度不能少于2个字符");
        }
        if (value.trim().length() > 50) {
            throw new IllegalArgumentException("用户名长度不能超过50个字符");
        }
        this.value = value.trim();
    }
    
    /**
     * 从字符串创建用户名
     */
    public static UserName of(String value) {
        return new UserName(value);
    }
    
    public String getValue() {
        return value;
    }
    
    /**
     * 检查用户名是否有效
     */
    public boolean isValid() {
        return value != null && 
               value.length() >= 2 && 
               value.length() <= 50 &&
               !containsInvalidChars();
    }
    
    /**
     * 检查是否包含无效字符
     */
    private boolean containsInvalidChars() {
        // 可以根据业务需要定义无效字符规则
        return value.contains("<") || value.contains(">") || 
               value.contains("&") || value.contains("\"") || 
               value.contains("'");
    }
    
    /**
     * 获取脱敏显示
     */
    public String getMaskedValue() {
        if (value == null || value.length() <= 2) {
            return value;
        }
        if (value.length() <= 4) {
            return value.charAt(0) + "*" + value.charAt(value.length() - 1);
        }
        return value.substring(0, 2) + "****" + value.substring(value.length() - 2);
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserName userName = (UserName) o;
        return Objects.equals(value, userName.value);
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