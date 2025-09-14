package com.jiuxi.module.user.intf.web.dto;

import java.util.List;

/**
 * 分页结果包装类
 * 
 * @author DDD Refactor
 * @date 2025-09-11
 */
public class PageResult<T> {
    
    private List<T> data;
    private long total;
    private int current;
    private int size;
    private long pages;
    
    public PageResult() {
    }
    
    public PageResult(List<T> data, long total, int current, int size) {
        this.data = data;
        this.total = total;
        this.current = current;
        this.size = size;
        this.pages = (total + size - 1) / size;
    }
    
    public List<T> getData() {
        return data;
    }
    
    public void setData(List<T> data) {
        this.data = data;
    }
    
    public long getTotal() {
        return total;
    }
    
    public void setTotal(long total) {
        this.total = total;
    }
    
    public int getCurrent() {
        return current;
    }
    
    public void setCurrent(int current) {
        this.current = current;
    }
    
    public int getSize() {
        return size;
    }
    
    public void setSize(int size) {
        this.size = size;
    }
    
    public long getPages() {
        return pages;
    }
    
    public void setPages(long pages) {
        this.pages = pages;
    }
}