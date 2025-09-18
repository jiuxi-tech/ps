package com.jiuxi.module.auth.domain.model.vo;

import java.util.Objects;
import java.util.regex.Pattern;

/**
 * 菜单编码值对象
 * 封装菜单编码的业务规则和验证逻辑
 * 
 * @author DDD Refactor
 * @date 2025-09-18
 */
public class MenuCode {
    
    private static final Pattern MENU_CODE_PATTERN = Pattern.compile("^[A-Z0-9_]{2,50}$");
    private final String value;
    
    public MenuCode(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("菜单编码不能为空");
        }
        
        String trimmedValue = value.trim().toUpperCase();
        
        if (!MENU_CODE_PATTERN.matcher(trimmedValue).matches()) {
            throw new IllegalArgumentException("菜单编码格式不正确，只能包含大写字母、数字和下划线，长度2-50位");
        }
        
        this.value = trimmedValue;
    }
    
    public String getValue() {
        return value;
    }
    
    /**
     * 检查是否是系统级菜单编码
     */
    public boolean isSystemMenu() {
        return value.startsWith("SYS_");
    }
    
    /**
     * 检查是否是业务级菜单编码
     */
    public boolean isBusinessMenu() {
        return value.startsWith("BIZ_");
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MenuCode menuCode = (MenuCode) o;
        return Objects.equals(value, menuCode.value);
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