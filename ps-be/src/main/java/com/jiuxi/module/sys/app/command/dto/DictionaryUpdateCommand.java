package com.jiuxi.module.sys.app.command.dto;

import java.io.Serializable;

/**
 * 字典更新命令
 * 用于CQRS命令模式的字典更新请求
 * 
 * @author System Management
 * @date 2025-09-18
 */
public class DictionaryUpdateCommand implements Serializable {
    
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
     * 字典组
     */
    private String dictGroup;
    
    /**
     * 上级字典ID
     */
    private String parentDictId;
    
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
    
    public String getDictGroup() {
        return dictGroup;
    }
    
    public void setDictGroup(String dictGroup) {
        this.dictGroup = dictGroup;
    }
    
    public String getParentDictId() {
        return parentDictId;
    }
    
    public void setParentDictId(String parentDictId) {
        this.parentDictId = parentDictId;
    }
}