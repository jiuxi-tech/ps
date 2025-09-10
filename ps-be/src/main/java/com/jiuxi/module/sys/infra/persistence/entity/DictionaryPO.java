package com.jiuxi.module.sys.infra.persistence.entity;

import com.baomidou.mybatisplus.annotation.*;
import java.time.LocalDateTime;

/**
 * 字典持久化对象
 * 对应数据库中的字典表
 * 
 * @author System Management
 * @date 2025-09-06
 */
@TableName("sys_dictionary")
public class DictionaryPO {
    
    /**
     * 字典ID
     */
    @TableId(value = "dict_id", type = IdType.ASSIGN_ID)
    private String dictId;
    
    /**
     * 字典编码
     */
    @TableField("dict_code")
    private String dictCode;
    
    /**
     * 字典名称
     */
    @TableField("dict_name")
    private String dictName;
    
    /**
     * 字典描述
     */
    @TableField("dict_desc")
    private String dictDesc;
    
    /**
     * 字典类型
     */
    @TableField("dict_type")
    private String dictType;
    
    /**
     * 状态（ACTIVE-启用, INACTIVE-禁用）
     */
    @TableField("status")
    private String status;
    
    /**
     * 排序序号
     */
    @TableField("order_index")
    private Integer orderIndex;
    
    /**
     * 创建人
     */
    @TableField("creator")
    private String creator;
    
    /**
     * 创建时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;
    
    /**
     * 更新人
     */
    @TableField("updator")
    private String updator;
    
    /**
     * 更新时间
     */
    @TableField("update_time")
    private LocalDateTime updateTime;
    
    /**
     * 租户ID
     */
    @TableField("tenant_id")
    private String tenantId;
    
    /**
     * 逻辑删除标识(0-未删除, 1-已删除)
     */
    @TableLogic
    @TableField("deleted")
    private Integer deleted;
    
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
    
    public Integer getDeleted() {
        return deleted;
    }
    
    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }
}