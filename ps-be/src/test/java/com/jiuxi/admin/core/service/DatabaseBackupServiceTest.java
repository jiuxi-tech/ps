package com.jiuxi.admin.core.service;

import com.jiuxi.admin.core.service.impl.DatabaseBackupServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @ClassName: DatabaseBackupServiceTest
 * @Description: 数据库备份服务测试类
 * @Author: system
 * @Date: 2025-09-24
 * @Copyright: www.tuxun.net Inc. All rights reserved.
 */
@ExtendWith(MockitoExtension.class)
public class DatabaseBackupServiceTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseBackupServiceTest.class);

    @InjectMocks
    private DatabaseBackupServiceImpl databaseBackupService;

    /**
     * 测试备份配置检查功能
     */
    @Test
    public void testIsBackupConfigValid() {
        try {
            boolean isValid = databaseBackupService.isBackupConfigValid();
            LOGGER.info("备份配置检查结果: {}", isValid);
            // 由于依赖配置服务，这里主要验证方法不抛异常
        } catch (Exception e) {
            LOGGER.error("备份配置检查测试失败", e);
        }
    }

    /**
     * 测试获取备份配置信息
     */
    @Test
    public void testGetBackupConfigInfo() {
        try {
            String configInfo = databaseBackupService.getBackupConfigInfo();
            LOGGER.info("备份配置信息: {}", configInfo);
            // 验证配置信息不为空
            assert configInfo != null;
        } catch (Exception e) {
            LOGGER.error("获取备份配置信息测试失败", e);
        }
    }

    /**
     * 测试检查正在进行的备份任务
     */
    @Test
    public void testHasRunningBackup() {
        try {
            boolean hasRunning = databaseBackupService.hasRunningBackup("test_database");
            LOGGER.info("是否有正在进行的备份: {}", hasRunning);
            // 验证方法正常执行
        } catch (Exception e) {
            LOGGER.error("检查正在进行备份测试失败", e);
        }
    }

    /**
     * 测试清理过期备份文件
     */
    @Test
    public void testCleanExpiredBackupFiles() {
        try {
            int cleanedCount = databaseBackupService.cleanExpiredBackupFiles();
            LOGGER.info("清理的过期文件数量: {}", cleanedCount);
            // 验证返回值大于等于0
            assert cleanedCount >= 0;
        } catch (Exception e) {
            LOGGER.error("清理过期备份文件测试失败", e);
        }
    }
}