package com.jiuxi.admin.core.bean.query;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;

/**
 * 标签表查询条件
 *
 * @author system
 * @date 2024-01-18 11:05:17
 */
public class TpTagQuery implements Serializable {

    private static final long serialVersionUID = 1L;

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
     * 机构ID
     */
    private String ascnId;

    /**
     * 租户ID
     */
    @JsonIgnore
    private String tenantId;

    /**
     * 所属机构ID
     */
    @JsonIgnore
    private String orgId;

    /**
     * 创建人
     */
    @JsonIgnore
    private String creator;

    /**
     * 当前页
     */
    private Integer current;

    /**
     * 每页记录数
     */
    private Integer size;

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





    public String getAscnId() {
        return ascnId;
    }

    public void setAscnId(String ascnId) {
        this.ascnId = ascnId;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Integer getCurrent() {
        return current;
    }

    public void setCurrent(Integer current) {
        this.current = current;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }
}