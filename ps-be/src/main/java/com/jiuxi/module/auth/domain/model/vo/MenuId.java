package com.jiuxi.module.auth.domain.model.vo;

import java.util.Objects;

/**
 * 菜单ID值对象
 * 封装菜单标识符的业务规则
 * 
 * @author DDD Refactor
 * @date 2025-09-18
 */
public class MenuId {
    
    private final String value;
    
    public MenuId(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("菜单ID不能为空");
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
        MenuId menuId = (MenuId) o;
        return Objects.equals(value, menuId.value);
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