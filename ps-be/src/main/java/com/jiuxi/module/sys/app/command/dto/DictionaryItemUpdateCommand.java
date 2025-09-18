package com.jiuxi.module.sys.app.command.dto;

import java.io.Serializable;

/**
 * 字典项更新命令
 * 用于CQRS命令模式的字典项更新请求
 * 
 * @author System Management
 * @date 2025-09-18
 */
public class DictionaryItemUpdateCommand implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 字典项ID
     */
    private String itemId;
    
    /**
     * 字典ID
     */
    private String dictId;
    
    /**
     * 项编码
     */
    private String itemCode;
    
    /**
     * 项名称
     */
    private String itemName;
    
    /**
     * 项值
     */
    private String itemValue;
    
    /**
     * 项描述
     */
    private String itemDesc;
    
    /**
     * 状态（ACTIVE-启用, INACTIVE-禁用）
     */
    private String status;
    
    /**
     * 排序序号
     */
    private Integer orderIndex;
    
    /**
     * 是否默认项
     */
    private Boolean isDefault;
    
    /**
     * 更新人
     */
    private String updator;
    
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
    
    public String getItemCode() {
        return itemCode;
    }
    
    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }
    
    public String getItemName() {
        return itemName;
    }
    
    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
    
    public String getItemValue() {
        return itemValue;
    }
    
    public void setItemValue(String itemValue) {
        this.itemValue = itemValue;
    }
    
    public String getItemDesc() {
        return itemDesc;
    }
    
    public void setItemDesc(String itemDesc) {
        this.itemDesc = itemDesc;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public Integer getOrderIndex() {
        return orderIndex;
    }
    
    public void setOrderIndex(Integer orderIndex) {
        this.orderIndex = orderIndex;
    }
    
    public String getUpdator() {
        return updator;
    }
    
    public void setUpdator(String updator) {
        this.updator = updator;
    }
    
    public Boolean getIsDefault() {
        return isDefault;
    }
    
    public void setIsDefault(Boolean isDefault) {
        this.isDefault = isDefault;
    }
}