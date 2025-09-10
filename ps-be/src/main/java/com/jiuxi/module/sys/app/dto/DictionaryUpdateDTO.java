package com.jiuxi.module.sys.app.dto;

import java.io.Serializable;

/**
 * 字典更新DTO
 * 用于更新字典的数据传输对象
 * 
 * @author System Management
 * @date 2025-09-06
 */
public class DictionaryUpdateDTO implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 字典ID
     */
    private String dictId;
    
    /**
     * 字典编码
     */
    private String dictCode;
    
    /**
     * 字典名称
     */
    private String dictName;
    
    /**
     * 字典描述
     */
    private String dictDesc;
    
    /**
     * 字典类型
     */
    private String dictType;
    
    /**
     * 状态（ACTIVE-启用, INACTIVE-禁用）
     */
    private String status;
    
    /**
     * 排序序号
     */
    private Integer orderIndex;
    
    /**
     * 更新人
     */
    private String updator;
    
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
    
    public String getDictDesc() {
        return dictDesc;
    }
    
    public void setDictDesc(String dictDesc) {
        this.dictDesc = dictDesc;
    }
    
    public String getDictType() {
        return dictType;
    }
    
    public void setDictType(String dictType) {
        this.dictType = dictType;
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
}