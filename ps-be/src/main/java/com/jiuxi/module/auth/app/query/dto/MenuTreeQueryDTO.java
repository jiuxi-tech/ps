package com.jiuxi.module.auth.app.query.dto;

import java.util.List;

/**
 * 菜单树查询DTO
 * 封装菜单树查询的响应数据
 * 
 * @author DDD Refactor
 * @date 2025-09-18
 */
public class MenuTreeQueryDTO extends MenuQueryDTO {
    
    private List<MenuTreeQueryDTO> children;
    
    // 构造器
    public MenuTreeQueryDTO() {
        super();
    }
    
    // Getters and Setters
    public List<MenuTreeQueryDTO> getChildren() {
        return children;
    }
    
    public void setChildren(List<MenuTreeQueryDTO> children) {
        this.children = children;
    }
}