package com.jiuxi.admin.core.bean.query;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;

/**
 * @ClassName: TpDatabaseBackupLogQuery
 * @Description: 数据库备份记录查询条件类
 * @Author: system
 * @Date: 2025-09-24
 * @Copyright: www.tuxun.net Inc. All rights reserved.
 */
public class TpDatabaseBackupLogQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 备份名称
     */
    private String backupName;

    /**
     * 备份类型 1:自动备份 2:手动备份
     */
    private Integer backupType;

    /**
     * 备份状态 1:进行中 2:成功 3:失败
     */
    private Integer backupStatus;

    /**
     * 数据库名称
     */
    private String databaseName;

    /**
     * 备份开始时间-起始
     */
    private String backupStartTimeBegin;

    /**
     * 备份开始时间-结束
     */
    private String backupStartTimeEnd;

    /**
     * 创建人
     */
    private String creator;

    /**
     * 租户ID
     */
    @JsonIgnore
    private String tenantId;

    /**
     * 机构ID
     */
    @JsonIgnore
    private String orgId;

    /**
     * 是否有效 1:有效 0:无效
     */
    private Integer actived;

    /**
     * 当前页
     */
    private Integer current;

    /**
     * 每页记录数
     */
    private Integer size;

    // Getter and Setter methods

    public String getBackupName() {
        return backupName;
    }

    public void setBackupName(String backupName) {
        this.backupName = backupName;
    }

    public Integer getBackupType() {
        return backupType;
    }

    public void setBackupType(Integer backupType) {
        this.backupType = backupType;
    }

    public Integer getBackupStatus() {
        return backupStatus;
    }

    public void setBackupStatus(Integer backupStatus) {
        this.backupStatus = backupStatus;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public String getBackupStartTimeBegin() {
        return backupStartTimeBegin;
    }

    public void setBackupStartTimeBegin(String backupStartTimeBegin) {
        this.backupStartTimeBegin = backupStartTimeBegin;
    }

    public String getBackupStartTimeEnd() {
        return backupStartTimeEnd;
    }

    public void setBackupStartTimeEnd(String backupStartTimeEnd) {
        this.backupStartTimeEnd = backupStartTimeEnd;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
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

    public Integer getActived() {
        return actived;
    }

    public void setActived(Integer actived) {
        this.actived = actived;
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