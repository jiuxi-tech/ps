package com.jiuxi.admin.core.bean.query;

import java.io.Serializable;

/**
 * 密码修改历史查询对象
 *
 * @author system
 * @date 2025-12-01
 */
public class TpPasswordHistoryQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 账号ID
     */
    private String accountId;

    /**
     * 人员ID
     */
    private String personId;

    /**
     * 用户名（模糊查询）
     */
    private String username;

    /**
     * 人员姓名（模糊查询）
     */
    private String personName;

    /**
     * 修改类型
     */
    private Integer changeType;

    /**
     * 操作人姓名（模糊查询）
     */
    private String changedByName;

    /**
     * 开始时间
     */
    private String startTime;

    /**
     * 结束时间
     */
    private String endTime;

    /**
     * 租户ID
     */
    private String tenantId;

    /**
     * 当前页码
     */
    private Integer current;

    /**
     * 每页条数
     */
    private Integer size;

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public Integer getChangeType() {
        return changeType;
    }

    public void setChangeType(Integer changeType) {
        this.changeType = changeType;
    }

    public String getChangedByName() {
        return changedByName;
    }

    public void setChangedByName(String changedByName) {
        this.changedByName = changedByName;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
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
