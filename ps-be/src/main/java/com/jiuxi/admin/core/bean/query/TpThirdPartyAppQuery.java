package com.jiuxi.admin.core.bean.query;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;

/**
 * 第三方应用查询条件
 *
 * @author system
 * @date 2024-01-18 11:05:17
 */
public class TpThirdPartyAppQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 应用名称
     */
    private String appName;

    /**
     * 状态（1:启用 0:禁用）
     */
    private Integer status;

    /**
     * 联系人
     */
    private String contactPerson;

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

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
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
