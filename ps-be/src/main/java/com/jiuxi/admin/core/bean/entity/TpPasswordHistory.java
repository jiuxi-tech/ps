package com.jiuxi.admin.core.bean.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

/**
 * 密码修改历史记录表
 *
 * @author system
 * @date 2025-12-01
 */
@TableName("tp_password_history")
public class TpPasswordHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 历史记录ID
     */
    @TableId
    private String historyId;

    /**
     * 账号ID
     */
    private String accountId;

    /**
     * 人员ID
     */
    private String personId;

    /**
     * 账号用户名
     */
    private String username;

    /**
     * 密码哈希值
     */
    private String passwordHash;

    /**
     * 修改类型：1-用户主动修改 2-管理员重置 3-密码过期强制修改 4-其他
     */
    private Integer changeType;

    /**
     * 修改原因说明
     */
    private String changeReason;

    /**
     * 修改操作人ID
     */
    private String changedBy;

    /**
     * 修改操作人姓名
     */
    private String changedByName;

    /**
     * 密码修改时间
     */
    private String changeTime;

    /**
     * 修改操作的IP地址
     */
    private String ipAddress;

    /**
     * 修改操作的用户代理信息
     */
    private String userAgent;

    /**
     * 租户ID
     */
    private String tenantId;

    /**
     * 备注信息
     */
    private String remark;

    /**
     * 记录创建时间
     */
    private String createdTime;

    public String getHistoryId() {
        return historyId;
    }

    public void setHistoryId(String historyId) {
        this.historyId = historyId;
    }

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

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public Integer getChangeType() {
        return changeType;
    }

    public void setChangeType(Integer changeType) {
        this.changeType = changeType;
    }

    public String getChangeReason() {
        return changeReason;
    }

    public void setChangeReason(String changeReason) {
        this.changeReason = changeReason;
    }

    public String getChangedBy() {
        return changedBy;
    }

    public void setChangedBy(String changedBy) {
        this.changedBy = changedBy;
    }

    public String getChangedByName() {
        return changedByName;
    }

    public void setChangedByName(String changedByName) {
        this.changedByName = changedByName;
    }

    public String getChangeTime() {
        return changeTime;
    }

    public void setChangeTime(String changeTime) {
        this.changeTime = changeTime;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }
}
