package com.jiuxi.module.system.domain.entity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * @ClassName: Dictionary
 * @Description: 字典聚合根 - 系统字典管理领域实体
 * @Author: System
 * @Date: 2025/01/07
 * @Copyright: 2025 www.jiuxi.com Inc. All rights reserved.
 */
public class Dictionary {
    
    /**
     * 字典ID (聚合根标识)
     */
    private String dictId;
    
    /**
     * 字典编码 (业务唯一标识)
     */
    private String dictCode;
    
    /**
     * 字典名称
     */
    private String dictName;
    
    /**
     * 字典描述
     */
    private String description;
    
    /**
     * 字典类型 (SYSTEM-系统字典, BUSINESS-业务字典)
     */
    private DictionaryType dictType;
    
    /**
     * 字典状态
     */
    private DictionaryStatus status;
    
    /**
     * 排序号
     */
    private Integer sortOrder;
    
    /**
     * 字典项列表
     */
    private List<DictionaryItem> items;
    
    /**
     * 创建信息
     */
    private String creator;
    private LocalDateTime createTime;
    
    /**
     * 更新信息
     */
    private String updator;
    private LocalDateTime updateTime;
    
    /**
     * 租户ID
     */
    private String tenantId;
    
    // 构造方法
    public Dictionary() {
    }
    
    public Dictionary(String dictCode, String dictName, DictionaryType dictType) {
        this.dictCode = dictCode;
        this.dictName = dictName;
        this.dictType = dictType;
        this.status = DictionaryStatus.ENABLED;
        this.createTime = LocalDateTime.now();
    }
    
    // 领域方法
    
    /**
     * 启用字典
     */
    public void enable() {
        this.status = DictionaryStatus.ENABLED;
        this.updateTime = LocalDateTime.now();
    }
    
    /**
     * 禁用字典
     */
    public void disable() {
        this.status = DictionaryStatus.DISABLED;
        this.updateTime = LocalDateTime.now();
    }
    
    /**
     * 添加字典项
     */
    public void addItem(DictionaryItem item) {
        if (items != null) {
            item.setDictId(this.dictId);
            items.add(item);
        }
    }
    
    /**
     * 移除字典项
     */
    public void removeItem(String itemId) {
        if (items != null) {
            items.removeIf(item -> Objects.equals(item.getItemId(), itemId));
        }
    }
    
    /**
     * 检查字典是否可用
     */
    public boolean isEnabled() {
        return DictionaryStatus.ENABLED.equals(this.status);
    }
    
    /**
     * 验证字典编码唯一性
     */
    public boolean isCodeUnique(String code) {
        return !Objects.equals(this.dictCode, code);
    }
    
    // Getters and Setters
    public String getDictId() {
        return dictId;
    }
    
    public void setDictId(String dictId) {
        this.dictId = dictId;
    }
    
    public String getDictCode() {
        return dictCode;
    }
    
    public void setDictCode(String dictCode) {
        this.dictCode = dictCode;
    }
    
    public String getDictName() {
        return dictName;
    }
    
    public void setDictName(String dictName) {
        this.dictName = dictName;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public DictionaryType getDictType() {
        return dictType;
    }
    
    public void setDictType(DictionaryType dictType) {
        this.dictType = dictType;
    }
    
    public DictionaryStatus getStatus() {
        return status;
    }
    
    public void setStatus(DictionaryStatus status) {
        this.status = status;
    }
    
    public Integer getSortOrder() {
        return sortOrder;
    }
    
    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }
    
    public List<DictionaryItem> getItems() {
        return items;
    }
    
    public void setItems(List<DictionaryItem> items) {
        this.items = items;
    }
    
    public String getCreator() {
        return creator;
    }
    
    public void setCreator(String creator) {
        this.creator = creator;
    }
    
    public LocalDateTime getCreateTime() {
        return createTime;
    }
    
    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }
    
    public String getUpdator() {
        return updator;
    }
    
    public void setUpdator(String updator) {
        this.updator = updator;
    }
    
    public LocalDateTime getUpdateTime() {
        return updateTime;
    }
    
    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }
    
    public String getTenantId() {
        return tenantId;
    }
    
    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Dictionary that = (Dictionary) o;
        return Objects.equals(dictId, that.dictId);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(dictId);
    }
    
    @Override
    public String toString() {
        return "Dictionary{" +
                "dictId='" + dictId + '\'' +
                ", dictCode='" + dictCode + '\'' +
                ", dictName='" + dictName + '\'' +
                ", status=" + status +
                '}';
    }
}