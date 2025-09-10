package com.jiuxi.admin.core.bean.vo;

import java.io.Serializable;

/**
 * 人员标签关系表
 *
 * @author pand
 * @email 2521143047@qq.com
 * @date 2024-01-27
 */
public class TpPersonTagVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 人员ID
     */
    private String personId;
    
    /**
     * 标签ID
     */
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
     * 部门ID
     */
    private String deptId;
    
    /**
     * 创建人
     */
    private String creator;
    
    /**
     * 创建时间
     */
    private String createTime;

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

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

    public String getTagColor() {
        return tagColor;
    }

    public void setTagColor(String tagColor) {
        this.tagColor = tagColor;
    }

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
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

    public String getTagDesc() {
        return tagDesc;
    }

    public void setTagDesc(String tagDesc) {
        this.tagDesc = tagDesc;
    }

    public Double getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(Double orderIndex) {
        this.orderIndex = orderIndex;
    }
}