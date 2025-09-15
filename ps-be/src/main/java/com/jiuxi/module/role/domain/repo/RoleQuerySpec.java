package com.jiuxi.module.role.domain.repo;

import com.jiuxi.module.role.domain.model.vo.RoleCategory;
import com.jiuxi.module.role.domain.model.vo.RoleStatus;
import java.time.LocalDateTime;

/**
 * 角色查询规格
 * 
 * @author DDD重构
 * @date 2025-09-15
 */
public class RoleQuerySpec {
    
    private String roleName;
    private RoleStatus status;
    private RoleCategory category;
    private String creator;
    private LocalDateTime createTimeStart;
    private LocalDateTime createTimeEnd;
    
    // 分页参数
    private int page = 1;
    private int size = 10;
    
    // 排序参数
    private String sortBy = "createTime";
    private SortDirection sortDirection = SortDirection.DESC;
    
    public RoleQuerySpec() {
    }
    
    public static Builder builder() {
        return new Builder();
    }
    
    // Getters and Setters
    public String getRoleName() {
        return roleName;
    }
    
    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
    
    public RoleStatus getStatus() {
        return status;
    }
    
    public void setStatus(RoleStatus status) {
        this.status = status;
    }
    
    public RoleCategory getCategory() {
        return category;
    }
    
    public void setCategory(RoleCategory category) {
        this.category = category;
    }
    
    public String getCreator() {
        return creator;
    }
    
    public void setCreator(String creator) {
        this.creator = creator;
    }
    
    public LocalDateTime getCreateTimeStart() {
        return createTimeStart;
    }
    
    public void setCreateTimeStart(LocalDateTime createTimeStart) {
        this.createTimeStart = createTimeStart;
    }
    
    public LocalDateTime getCreateTimeEnd() {
        return createTimeEnd;
    }
    
    public void setCreateTimeEnd(LocalDateTime createTimeEnd) {
        this.createTimeEnd = createTimeEnd;
    }
    
    public int getPage() {
        return page;
    }
    
    public void setPage(int page) {
        this.page = Math.max(1, page);
    }
    
    public int getSize() {
        return size;
    }
    
    public void setSize(int size) {
        this.size = Math.max(1, Math.min(100, size)); // 限制最大页大小为100
    }
    
    public String getSortBy() {
        return sortBy;
    }
    
    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }
    
    public SortDirection getSortDirection() {
        return sortDirection;
    }
    
    public void setSortDirection(SortDirection sortDirection) {
        this.sortDirection = sortDirection;
    }
    
    public int getOffset() {
        return (page - 1) * size;
    }
    
    public enum SortDirection {
        ASC, DESC
    }
    
    /**
     * 建造者模式
     */
    public static class Builder {
        private final RoleQuerySpec spec = new RoleQuerySpec();
        
        public Builder roleName(String roleName) {
            spec.setRoleName(roleName);
            return this;
        }
        
        public Builder status(RoleStatus status) {
            spec.setStatus(status);
            return this;
        }
        
        public Builder category(RoleCategory category) {
            spec.setCategory(category);
            return this;
        }
        
        public Builder creator(String creator) {
            spec.setCreator(creator);
            return this;
        }
        
        public Builder createTimeRange(LocalDateTime start, LocalDateTime end) {
            spec.setCreateTimeStart(start);
            spec.setCreateTimeEnd(end);
            return this;
        }
        
        public Builder page(int page, int size) {
            spec.setPage(page);
            spec.setSize(size);
            return this;
        }
        
        public Builder sortBy(String sortBy, SortDirection direction) {
            spec.setSortBy(sortBy);
            spec.setSortDirection(direction);
            return this;
        }
        
        public RoleQuerySpec build() {
            return spec;
        }
    }
}