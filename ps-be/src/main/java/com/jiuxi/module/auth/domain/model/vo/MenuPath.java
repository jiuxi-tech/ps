package com.jiuxi.module.auth.domain.model.vo;

import java.util.Objects;

/**
 * 菜单路径值对象
 * 封装菜单路径的业务规则和验证逻辑
 * 
 * @author DDD Refactor
 * @date 2025-09-18
 */
public class MenuPath {
    
    private final String value;
    
    public MenuPath(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("菜单路径不能为空");
        }
        
        String trimmedValue = value.trim();
        
        if (!trimmedValue.startsWith("/")) {
            throw new IllegalArgumentException("菜单路径必须以'/'开头");
        }
        
        if (trimmedValue.length() > 200) {
            throw new IllegalArgumentException("菜单路径长度不能超过200个字符");
        }
        
        this.value = trimmedValue;
    }
    
    public String getValue() {
        return value;
    }
    
    /**
     * 检查是否是根路径
     */
    public boolean isRootPath() {
        return "/".equals(value);
    }
    
    /**
     * 检查是否是外部链接
     */
    public boolean isExternalLink() {
        return value.startsWith("http://") || value.startsWith("https://");
    }
    
    /**
     * 获取父路径
     */
    public MenuPath getParentPath() {
        if (isRootPath()) {
            return null;
        }
        
        int lastSlashIndex = value.lastIndexOf("/");
        if (lastSlashIndex == 0) {
            return new MenuPath("/");
        }
        
        return new MenuPath(value.substring(0, lastSlashIndex));
    }
    
    /**
     * 检查是否是指定路径的子路径
     */
    public boolean isChildOf(MenuPath parentPath) {
        if (parentPath == null || parentPath.isRootPath()) {
            return !isRootPath();
        }
        
        return value.startsWith(parentPath.getValue() + "/");
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MenuPath menuPath = (MenuPath) o;
        return Objects.equals(value, menuPath.value);
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