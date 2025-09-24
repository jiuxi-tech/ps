package com.jiuxi.admin.core.service;

import com.jiuxi.admin.core.bean.vo.TpDatabaseBackupLogVO;

/**
 * @ClassName: DatabaseBackupService
 * @Description: 数据库备份服务接口
 * @Author: system
 * @Date: 2025-09-24
 * @Copyright: www.tuxun.net Inc. All rights reserved.
 */
public interface DatabaseBackupService {

    /**
     * 执行数据库备份
     *
     * @param databaseName 数据库名称
     * @param backupType 备份类型 1:自动备份 2:手动备份
     * @param operator 操作人
     * @param tenantId 租户ID
     * @return 备份记录VO
     */

    TpDatabaseBackupLogVO executeBackup(Integer backupType, String operator, String tenantId);

    /**
     * 执行自动备份任务
     * 根据系统配置自动执行备份
     */
    void executeAutoBackup();

    /**
     * 检查备份配置是否有效
     *
     * @return true:配置有效 false:配置无效
     */
    boolean isBackupConfigValid();

    /**
     * 获取备份配置信息
     *
     * @return 配置信息描述
     */
    String getBackupConfigInfo();

    /**
     * 清理过期的备份文件
     * 根据配置的保留天数清理过期备份文件
     *
     * @return 清理的文件数量
     */
    int cleanExpiredBackupFiles();

    /**
     * 检查是否有正在进行的备份任务
     *
     * @param databaseName 数据库名称
     * @return true:有正在进行的备份 false:没有正在进行的备份
     */
    boolean hasRunningBackup(String databaseName);

    /**
     * 停止指定的备份任务
     *
     * @param backupId 备份记录ID
     * @param operator 操作人
     * @return true:停止成功 false:停止失败
     */
    boolean stopBackup(String backupId, String operator);

    /**
     * 测试mysqldump连接
     *
     * @return 测试结果信息
     */
    String testMysqldumpConnection();
}