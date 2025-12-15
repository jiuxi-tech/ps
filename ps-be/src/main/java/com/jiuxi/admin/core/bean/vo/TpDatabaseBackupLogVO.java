package com.jiuxi.admin.core.bean.vo;

import com.jiuxi.shared.common.validation.group.AddGroup;
import com.jiuxi.shared.common.validation.group.UpdateGroup;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @ClassName: TpDatabaseBackupLogVO
 * @Description: 数据库备份记录表视图对象
 * @Author: Qdd
 * @Date: 2025-09-24
 * @Copyright: Hangzhou Jiuxi Inc. All rights reserved.
 */
public class TpDatabaseBackupLogVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 备份记录ID
     */
    @NotBlank(message = "备份记录ID不能为空", groups = {UpdateGroup.class})
    private String backupId;

    /**
     * 备份名称
     */
    @NotBlank(message = "备份名称不能为空", groups = {AddGroup.class, UpdateGroup.class})
    private String backupName;

    /**
     * 备份类型 1:自动备份 2:手动备份
     */
    @NotNull(message = "备份类型不能为空", groups = {AddGroup.class})
    private Integer backupType;

    /**
     * 备份状态 1:进行中 2:成功 3:失败
     */
    private Integer backupStatus;

    /**
     * 备份的数据库名称
     */
    @NotBlank(message = "数据库名称不能为空", groups = {AddGroup.class})
    private String databaseName;

    /**
     * 备份文件存储路径
     */
    private String backupFilePath;

    /**
     * 备份文件名称
     */
    private String backupFileName;

    /**
     * 备份文件大小(字节)
     */
    private Long backupFileSize;

    /**
     * 备份文件大小显示值(如：10.5MB)
     */
    private String backupFileSizeDisplay;

    /**
     * 备份开始时间
     */
    private String backupStartTime;

    /**
     * 备份结束时间
     */
    private String backupEndTime;

    /**
     * 备份耗时(秒)
     */
    private Integer backupDuration;

    /**
     * 备份耗时显示值(如：1分30秒)
     */
    private String backupDurationDisplay;

    /**
     * 执行的备份命令
     */
    private String backupCommand;

    /**
     * 错误信息
     */
    private String errorMessage;

    /**
     * 是否有效 1:有效 0:无效
     */
    private Integer actived;

    /**
     * 租户ID
     */
    private String tenantId;

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
     * 备份类型显示名称
     */
    private String backupTypeName;

    /**
     * 备份状态显示名称
     */
    private String backupStatusName;

    /**
     * 创建人姓名
     */
    private String creatorName;

    /**
     * 修改人姓名
     */
    private String updatorName;

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

    // Getter and Setter methods

    public String getBackupId() {
        return backupId;
    }

    public void setBackupId(String backupId) {
        this.backupId = backupId;
    }

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

    public String getBackupFilePath() {
        return backupFilePath;
    }

    public void setBackupFilePath(String backupFilePath) {
        this.backupFilePath = backupFilePath;
    }

    public String getBackupFileName() {
        return backupFileName;
    }

    public void setBackupFileName(String backupFileName) {
        this.backupFileName = backupFileName;
    }

    public Long getBackupFileSize() {
        return backupFileSize;
    }

    public void setBackupFileSize(Long backupFileSize) {
        this.backupFileSize = backupFileSize;
    }

    public String getBackupFileSizeDisplay() {
        return backupFileSizeDisplay;
    }

    public void setBackupFileSizeDisplay(String backupFileSizeDisplay) {
        this.backupFileSizeDisplay = backupFileSizeDisplay;
    }

    public String getBackupStartTime() {
        return backupStartTime;
    }

    public void setBackupStartTime(String backupStartTime) {
        this.backupStartTime = backupStartTime;
    }

    public String getBackupEndTime() {
        return backupEndTime;
    }

    public void setBackupEndTime(String backupEndTime) {
        this.backupEndTime = backupEndTime;
    }

    public Integer getBackupDuration() {
        return backupDuration;
    }

    public void setBackupDuration(Integer backupDuration) {
        this.backupDuration = backupDuration;
    }

    public String getBackupDurationDisplay() {
        return backupDurationDisplay;
    }

    public void setBackupDurationDisplay(String backupDurationDisplay) {
        this.backupDurationDisplay = backupDurationDisplay;
    }

    public String getBackupCommand() {
        return backupCommand;
    }

    public void setBackupCommand(String backupCommand) {
        this.backupCommand = backupCommand;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Integer getActived() {
        return actived;
    }

    public void setActived(Integer actived) {
        this.actived = actived;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
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

    public String getBackupTypeName() {
        return backupTypeName;
    }

    public void setBackupTypeName(String backupTypeName) {
        this.backupTypeName = backupTypeName;
    }

    public String getBackupStatusName() {
        return backupStatusName;
    }

    public void setBackupStatusName(String backupStatusName) {
        this.backupStatusName = backupStatusName;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public String getUpdatorName() {
        return updatorName;
    }

    public void setUpdatorName(String updatorName) {
        this.updatorName = updatorName;
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