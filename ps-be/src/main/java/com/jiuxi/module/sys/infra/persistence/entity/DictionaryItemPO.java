package com.jiuxi.module.sys.infra.persistence.entity;

import com.baomidou.mybatisplus.annotation.*;
import java.time.LocalDateTime;

/**
 * 字典项持久化对象
 * 对应数据库中的字典项表
 * 
 * @author System Management
 * @date 2025-09-06
 */
@TableName("sys_dictionary_item")
public class DictionaryItemPO {
    
    /**
     * 字典项ID
     */
    @TableId(value = "item_id", type = IdType.ASSIGN_ID)
    private String itemId;
    
    /**
     * 字典ID
     */
    @TableField("dict_id")
    private String dictId;
    
    /**
     * 项编码
     */
    @TableField("item_code")
    private String itemCode;
    
    /**
     * 项名称
     */
    @TableField("item_name")
    private String itemName;
    
    /**
     * 项值
     */
    @TableField("item_value")
    private String itemValue;
    
    /**
     * 项描述
     */
    @TableField("item_desc")
    private String itemDesc;
    
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