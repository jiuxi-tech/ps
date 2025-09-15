package com.jiuxi.module.role.domain.repo;

import java.util.Collections;
import java.util.List;

/**
 * 分页结果
 * 
 * @author DDD重构
 * @date 2025-09-15
 */
public class PageResult<T> {
    
    private final List<T> content;
    private final int page;
    private final int size;
    private final long total;
    private final int totalPages;
    
    public PageResult(List<T> content, int page, int size, long total) {
        this.content = content != null ? content : Collections.emptyList();
        this.page = page;
        this.size = size;
        this.total = total;
        this.totalPages = size > 0 ? (int) Math.ceil((double) total / size) : 0;
    }
    
    public static <T> PageResult<T> empty(int page, int size) {
        return new PageResult<>(Collections.emptyList(), page, size, 0);
    }
    
    public static <T> PageResult<T> of(List<T> content, int page, int size, long total) {
        return new PageResult<>(content, page, size, total);
    }
    
    public List<T> getContent() {
        return Collections.unmodifiableList(content);
    }
    
    public int getPage() {
        return page;
    }
    
    public int getSize() {
        return size;
    }
    
    public long getTotal() {
        return total;
    }
    
    public int getTotalPages() {
        return totalPages;
    }
    
    public boolean hasContent() {
        return !content.isEmpty();
    }
    
    public boolean hasNext() {
        return page < totalPages;
    }
    
    public boolean hasPrevious() {
        return page > 1;
    }
    
    public boolean isFirst() {
        return page == 1;
    }
    
    public boolean isLast() {
        return page >= totalPages;
    }
    
    public int getNumberOfElements() {
        return content.size();
    }
    
    @Override
    public String toString() {
        return "PageResult{" +
                "content=" + content.size() + " items" +
                ", page=" + page +
                ", size=" + size +
                ", total=" + total +
                ", totalPages=" + totalPages +
                '}';
    }
}