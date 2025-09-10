package com.jiuxi.module.sys.app.dto;

import java.io.Serializable;

/**
 * 字典项创建DTO
 * 用于创建字典项的数据传输对象
 * 
 * @author System Management
 * @date 2025-09-06
 */
public class DictionaryItemCreateDTO implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
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
     * 排序序号
     */
    private Integer orderIndex;
    
    /**
     * 租户ID
     */
    private String tenantId;
    
    // Getters and Setters
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
    
    public Integer getOrderIndex() {
        return orderIndex;
    }
    
    public void setOrderIndex(Integer orderIndex) {
        this.orderIndex = orderIndex;
    }
    
    public String getTenantId() {
        return tenantId;
    }
    
    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }
}