package com.jiuxi.module.user.domain.valueobject;

import java.util.Objects;
import java.util.regex.Pattern;

/**
 * 邮箱值对象
 * 
 * @author DDD Refactor Phase 4.3.1.1
 * @date 2025-09-11
 */
public class Email {
    
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );
    
    private final String value;
    
    public Email(String value) {
        if (value != null && !value.trim().isEmpty()) {
            if (!isValidEmail(value.trim())) {
                throw new IllegalArgumentException("邮箱格式不正确: " + value);
            }
        }
        this.value = value == null ? null : value.trim().toLowerCase();
    }
    
    /**
     * 从字符串创建邮箱
     */
    public static Email of(String value) {
        return new Email(value);
    }
    
    /**
     * 创建空邮箱
     */
    public static Email empty() {
        return new Email(null);
    }
    
    public String getValue() {
        return value;
    }
    
    /**
     * 检查邮箱是否为空
     */
    public boolean isEmpty() {
        return value == null || value.trim().isEmpty();
    }
    
    /**
     * 检查邮箱是否有效
     */
    public boolean isValid() {
        return !isEmpty() && isValidEmail(value);
    }
    
    /**
     * 邮箱格式验证
     */
    private static boolean isValidEmail(String email) {
        return EMAIL_PATTERN.matcher(email).matches();
    }
    
    /**
     * 获取邮箱域名
     */
    public String getDomain() {
        if (isEmpty()) {
            return null;
        }
        int atIndex = value.indexOf('@');
        return atIndex > 0 ? value.substring(atIndex + 1) : null;
    }
    
    /**
     * 获取邮箱本地部分
     */
    public String getLocalPart() {
        if (isEmpty()) {
            return null;
        }
        int atIndex = value.indexOf('@');
        return atIndex > 0 ? value.substring(0, atIndex) : null;
    }
    
    /**
     * 获取脱敏邮箱
     */
    public String getMaskedValue() {
        if (isEmpty()) {
            return "";
        }
        
        String localPart = getLocalPart();
        String domain = getDomain();
        
        if (localPart == null || domain == null) {
            return value;
        }
        
        if (localPart.length() <= 2) {
            return localPart + "***@" + domain;
        }
        
        return localPart.substring(0, 2) + "***@" + domain;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Email email = (Email) o;
        return Objects.equals(value, email.value);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
    
    @Override
    public String toString() {
        return value == null ? "" : value;
    }
}