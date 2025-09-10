package com.jiuxi.module.sys.domain.entity;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 字典实体
 * 表示系统中的数据字典项
 * 
 * @author System Management
 * @date 2025-09-06
 */
public class Dictionary {
    
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
    
    // 构造器
    public Dictionary() {
        this.status = "ACTIVE";
    }
    
    public Dictionary(String dictCode, String dictName) {
        this();
        this.dictCode = dictCode;
        this.dictName = dictName;
    }
    
    // 业务方法
    
    /**
     * 启用字典
     */
    public void enable() {
        this.status = "ACTIVE";
    }
    
    /**
     * 停用字典
     */
    public void disable() {
        this.status = "INACTIVE";
    }
    
    /**
     * 检查是否激活
     */
    public boolean isActive() {
        return "ACTIVE".equals(this.status);
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
}