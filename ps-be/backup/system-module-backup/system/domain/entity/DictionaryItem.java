package com.jiuxi.module.system.domain.entity;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * @ClassName: DictionaryItem
 * @Description: 字典项实体 - 字典的子项
 * @Author: System
 * @Date: 2025/01/07
 * @Copyright: 2025 www.jiuxi.com Inc. All rights reserved.
 */
public class DictionaryItem {
    
    /**
     * 字典项ID
     */
    private String itemId;
    
    /**
     * 所属字典ID
     */
    private String dictId;
    
    /**
     * 字典项键值
     */
    private String itemKey;
    
    /**
     * 字典项显示值
     */
    private String itemValue;
    
    /**
     * 字典项标签
     */
    private String itemLabel;
    
    /**
     * 字典项描述
     */
    private String description;
    
    /**
     * 排序号
     */
    private Integer sortOrder;
    
    /**
     * 状态 (1-启用, 0-禁用)
     */
    private Integer status;
    
    /**
     * 扩展属性 (JSON格式)
     */
    private String extendProps;
    
    /**
     * 父级字典项ID (支持层级结构)
     */
    private String parentId;
    
    /**
     * 层级路径
     */
    private String treePath;
    
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
    
    // 构造方法
    public DictionaryItem() {
    }
    
    public DictionaryItem(String dictId, String itemKey, String itemValue, String itemLabel) {
        this.dictId = dictId;
        this.itemKey = itemKey;
        this.itemValue = itemValue;
        this.itemLabel = itemLabel;
        this.status = 1; // 默认启用
        this.createTime = LocalDateTime.now();
    }
    
    // 领域方法
    
    /**
     * 启用字典项
     */
    public void enable() {
        this.status = 1;
        this.updateTime = LocalDateTime.now();
    }
    
    /**
     * 禁用字典项
     */
    public void disable() {
        this.status = 0;
        this.updateTime = LocalDateTime.now();
    }
    
    /**
     * 检查是否启用
     */
    public boolean isEnabled() {
        return Integer.valueOf(1).equals(this.status);
    }
    
    /**
     * 设置父级关系
     */
    public void setParent(String parentId, String parentTreePath) {
        this.parentId = parentId;
        this.treePath = parentTreePath + "/" + this.itemId;
    }
    
    /**
     * 检查是否为根节点
     */
    public boolean isRoot() {
        return parentId == null || parentId.isEmpty();
    }
    
    // Getters and Setters
    public String getItemId() {
        return itemId;
    }
    
    public void setItemId(String itemId) {
        this.itemId = itemId;
    }
    
    public String getDictId() {
        return dictId;
    }
    
    public void setDictId(String dictId) {
        this.dictId = dictId;
    }
    
    public String getItemKey() {
        return itemKey;
    }
    
    public void setItemKey(String itemKey) {
        this.itemKey = itemKey;
    }
    
    public String getItemValue() {
        return itemValue;
    }
    
    public void setItemValue(String itemValue) {
        this.itemValue = itemValue;
    }
    
    public String getItemLabel() {
        return itemLabel;
    }
    
    public void setItemLabel(String itemLabel) {
        this.itemLabel = itemLabel;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public Integer getSortOrder() {
        return sortOrder;
    }
    
    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }
    
    public Integer getStatus() {
        return status;
    }
    
    public void setStatus(Integer status) {
        this.status = status;
    }
    
    public String getExtendProps() {
        return extendProps;
    }
    
    public void setExtendProps(String extendProps) {
        this.extendProps = extendProps;
    }
    
    public String getParentId() {
        return parentId;
    }
    
    public void setParentId(String parentId) {
        this.parentId = parentId;
    }
    
    public String getTreePath() {
        return treePath;
    }
    
    public void setTreePath(String treePath) {
        this.treePath = treePath;
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
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DictionaryItem that = (DictionaryItem) o;
        return Objects.equals(itemId, that.itemId);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(itemId);
    }
    
    @Override
    public String toString() {
        return "DictionaryItem{" +
                "itemId='" + itemId + '\'' +
                ", dictId='" + dictId + '\'' +
                ", itemKey='" + itemKey + '\'' +
                ", itemValue='" + itemValue + '\'' +
                ", itemLabel='" + itemLabel + '\'' +
                ", status=" + status +
                '}';
    }
}