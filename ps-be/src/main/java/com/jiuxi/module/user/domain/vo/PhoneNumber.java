package com.jiuxi.module.user.domain.vo;

import java.util.Objects;
import java.util.regex.Pattern;

/**
 * 手机号值对象
 * 
 * @author DDD Refactor Phase 4.3.1.1
 * @date 2025-09-11
 */
public class PhoneNumber {
    
    private static final Pattern MOBILE_PATTERN = Pattern.compile("^1[3-9]\\d{9}$");
    private static final Pattern LANDLINE_PATTERN = Pattern.compile("^0\\d{2,3}-?\\d{7,8}$");
    
    private final String value;
    private final PhoneType type;
    
    public enum PhoneType {
        MOBILE("手机"),
        LANDLINE("固话"),
        UNKNOWN("未知");
        
        private final String description;
        
        PhoneType(String description) {
            this.description = description;
        }
        
        public String getDescription() {
            return description;
        }
    }
    
    public PhoneNumber(String value) {
        if (value != null && !value.trim().isEmpty()) {
            String cleanValue = value.trim().replaceAll("\\s+", "");
            if (!isValidPhone(cleanValue)) {
                throw new IllegalArgumentException("手机号格式不正确: " + value);
            }
            this.value = cleanValue;
            this.type = determinePhoneType(cleanValue);
        } else {
            this.value = null;
            this.type = PhoneType.UNKNOWN;
        }
    }
    
    /**
     * 从字符串创建手机号
     */
    public static PhoneNumber of(String value) {
        return new PhoneNumber(value);
    }
    
    /**
     * 创建空手机号
     */
    public static PhoneNumber empty() {
        return new PhoneNumber(null);
    }
    
    public String getValue() {
        return value;
    }
    
    public PhoneType getType() {
        return type;
    }
    
    /**
     * 检查手机号是否为空
     */
    public boolean isEmpty() {
        return value == null || value.trim().isEmpty();
    }
    
    /**
     * 检查是否为手机号
     */
    public boolean isMobile() {
        return type == PhoneType.MOBILE;
    }
    
    /**
     * 检查是否为固话
     */
    public boolean isLandline() {
        return type == PhoneType.LANDLINE;
    }
    
    /**
     * 检查手机号是否有效
     */
    public boolean isValid() {
        return !isEmpty() && isValidPhone(value);
    }
    
    /**
     * 手机号格式验证
     */
    private static boolean isValidPhone(String phone) {
        return MOBILE_PATTERN.matcher(phone).matches() || 
               LANDLINE_PATTERN.matcher(phone).matches();
    }
    
    /**
     * 判断手机号类型
     */
    private static PhoneType determinePhoneType(String phone) {
        if (MOBILE_PATTERN.matcher(phone).matches()) {
            return PhoneType.MOBILE;
        } else if (LANDLINE_PATTERN.matcher(phone).matches()) {
            return PhoneType.LANDLINE;
        } else {
            return PhoneType.UNKNOWN;
        }
    }
    
    /**
     * 获取脱敏手机号
     */
    public String getMaskedValue() {
        if (isEmpty()) {
            return "";
        }
        
        if (isMobile() && value.length() == 11) {
            return value.substring(0, 3) + "****" + value.substring(7);
        } else if (isLandline()) {
            int dashIndex = value.indexOf('-');
            if (dashIndex > 0) {
                String prefix = value.substring(0, dashIndex + 1);
                String suffix = value.substring(dashIndex + 1);
                if (suffix.length() > 4) {
                    return prefix + "****" + suffix.substring(suffix.length() - 2);
                }
            }
        }
        
        // 默认脱敏策略
        if (value.length() > 4) {
            return value.substring(0, 2) + "****" + value.substring(value.length() - 2);
        }
        
        return value;
    }
    
    /**
     * 获取格式化显示
     */
    public String getFormattedValue() {
        if (isEmpty()) {
            return "";
        }
        
        if (isMobile() && value.length() == 11) {
            return value.substring(0, 3) + " " + value.substring(3, 7) + " " + value.substring(7);
        }
        
        return value;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PhoneNumber that = (PhoneNumber) o;
        return Objects.equals(value, that.value);
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