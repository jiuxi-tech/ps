package com.jiuxi.admin.core.bean.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

/**
 * 标签表
 *
 * @author pand
 * @email 2521143047@qq.com
 * @date 2024-01-27
 */
@TableName("tp_tag")
public class TpTag implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 标签ID
     */
    @TableId
    private String tagId;
    
    /**
     * 标签名称
     */
    private String tagName;
    
    /**
     * 标签描述
     */
    private String tagDesc;
    
    /**
     * 标签颜色
     */
    private String tagColor;
    
    /**
     * 排序号
     */
    private Double orderIndex;
    

    
    /**
     * 租户ID
     */
    private String tenantId;
    
    /**
     * 所属机构ID
     */
    private String ascnId;
    

    

    
    /**
     * 创建人
     */
    private String creator;
    
    /**
     * 创建时间
     */
    private String createTime;
    
    /**
     * 修改人
     */
    private String updator;
    
    /**
     * 修改时间
     */
    private String updateTime;
    
    /**
     * 扩展字段01
     */
    private String extend01;
    
    /**
     * 扩展字段02
     */
    private String extend02;
    
    /**
     * 扩展字段03
     */
    private String extend03;

    public String getTagId() {
        return tagId;
    }

    public void setTagId(String tagId) {
        this.tagId = tagId;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public String getTagDesc() {
        return tagDesc;
    }

    public void setTagDesc(String tagDesc) {
        this.tagDesc = tagDesc;
    }

    public String getTagColor() {
        return tagColor;
    }

    public void setTagColor(String tagColor) {
        this.tagColor = tagColor;
    }

    public Double getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(Double orderIndex) {
        this.orderIndex = orderIndex;
    }



    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getAscnId() {
        return ascnId;
    }

    public void setAscnId(String ascnId) {
        this.ascnId = ascnId;
    }





    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdator() {
        return updator;
    }

    public void setUpdator(String updator) {
        this.updator = updator;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getExtend01() {
        return extend01;
    }

    public void setExtend01(String extend01) {
        this.extend01 = extend01;
    }

    public String getExtend02() {
        return extend02;
    }

    public void setExtend02(String extend02) {
        this.extend02 = extend02;
    }

    public String getExtend03() {
        return extend03;
    }

    public void setExtend03(String extend03) {
        this.extend03 = extend03;
    }
}