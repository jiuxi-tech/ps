package com.jiuxi.admin.core.service.task;

import com.jiuxi.admin.core.service.DatabaseBackupService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @ClassName: DatabaseBackupTask
 * @Description: 数据库备份定时任务
 * @Author: system
 * @Date: 2025-09-24
 * @Copyright: www.tuxun.net Inc. All rights reserved.
 */
@Component
@ConditionalOnProperty(name = "app.database.backup.enabled", havingValue = "true", matchIfMissing = true)
public class DatabaseBackupTask {

    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseBackupTask.class);

    @Autowired
    private DatabaseBackupService databaseBackupService;

    /**
     * 自动备份任务
     * 默认每天凌晨2点执行
     * 可以通过系统配置表中的 database_auto_backup_cron 字段来动态配置
     */
    @Scheduled(cron = "${app.database.backup.cron:0 0 2 * * ?}")
    public void executeAutoBackup() {
        try {
            LOGGER.info("=== 开始执行数据库自动备份任务 ===");
            
            // 检查备份服务是否可用
            if (databaseBackupService == null) {
                LOGGER.error("数据库备份服务未初始化");
                return;
            }

            // 检查备份配置是否有效
            if (!databaseBackupService.isBackupConfigValid()) {
                LOGGER.warn("数据库备份配置无效，跳过自动备份任务");
                LOGGER.info("当前配置信息: \n{}", databaseBackupService.getBackupConfigInfo());
                return;
            }

            // 执行自动备份
            databaseBackupService.executeAutoBackup();
            
            LOGGER.info("=== 数据库自动备份任务执行完成 ===");

        } catch (Exception e) {
            LOGGER.error("执行数据库自动备份任务失败: {}", e.getMessage(), e);
        }
    }

    /**
     * 清理过期备份文件任务
     * 每周日凌晨3点执行
     */
    @Scheduled(cron = "0 0 3 ? * SUN")
    public void cleanExpiredBackupFiles() {
        try {
            LOGGER.info("=== 开始执行备份文件清理任务 ===");
            
            if (databaseBackupService == null) {
                LOGGER.error("数据库备份服务未初始化");
                return;
            }

            int cleanedCount = databaseBackupService.cleanExpiredBackupFiles();
            LOGGER.info("备份文件清理任务完成，共清理 {} 个过期文件", cleanedCount);
            
            LOGGER.info("=== 备份文件清理任务执行完成 ===");

        } catch (Exception e) {
            LOGGER.error("执行备份文件清理任务失败: {}", e.getMessage(), e);
        }
    }

    /**
     * 备份任务健康检查
     * 每小时执行一次，检查是否有长时间运行的备份任务
     */
    @Scheduled(cron = "0 0 * * * ?")
    public void healthCheck() {
        try {
            LOGGER.debug("执行数据库备份任务健康检查");
            
            // 这里可以添加健康检查逻辑
            // 比如检查是否有超时的备份任务，发送告警等
            
        } catch (Exception e) {
            LOGGER.error("备份任务健康检查失败: {}", e.getMessage(), e);
        }
    }
}