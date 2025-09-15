package com.jiuxi.module.user.app.query.dto;

/**
 * 用户分页查询条件
 */
public class UserPageQuery extends UserQuery {
    
    private Integer current = 1;
    private Integer size = 10;
    
    // Getters and Setters
    public Integer getCurrent() {
        return current;
    }
    
    public void setCurrent(Integer current) {
        this.current = current;
    }
    
    public Integer getSize() {
        return size;
    }
    
    public void setSize(Integer size) {
        this.size = size;
    }
}