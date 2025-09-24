package com.jiuxi.admin.core.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.jiuxi.admin.core.bean.entity.TpDatabaseBackupLog;
import com.jiuxi.admin.core.bean.vo.TpDatabaseBackupLogVO;
import com.jiuxi.admin.core.mapper.TpDatabaseBackupLogMapper;
import com.jiuxi.admin.core.service.DatabaseBackupService;
import com.jiuxi.admin.core.service.TpSystemConfigService;
import com.jiuxi.shared.common.exception.TopinfoRuntimeException;
import com.jiuxi.common.util.CommonDateUtil;
import com.jiuxi.common.util.SnowflakeIdUtil;
import org.apache.commons.exec.*;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @ClassName: DatabaseBackupServiceImpl
 * @Description: 数据库备份服务实现类
 * @Author: system
 * @Date: 2025-09-24
 * @Copyright: www.tuxun.net Inc. All rights reserved.
 */
@Service("databaseBackupService")
public class DatabaseBackupServiceImpl implements DatabaseBackupService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseBackupServiceImpl.class);

    @Autowired
    private TpDatabaseBackupLogMapper tpDatabaseBackupLogMapper;

    @Autowired
    private TpSystemConfigService tpSystemConfigService;

    // 备份类型常量
    private static final int BACKUP_TYPE_AUTO = 1; // 自动备份
    private static final int BACKUP_TYPE_MANUAL = 2; // 手动备份

    // 备份状态常量
    private static final int BACKUP_STATUS_RUNNING = 1; // 进行中
    private static final int BACKUP_STATUS_SUCCESS = 2; // 成功
    private static final int BACKUP_STATUS_FAILED = 3; // 失败

    /**
     * 执行数据库备份
     */
    @Override
    public TpDatabaseBackupLogVO executeBackup(Integer backupType, String operator, String tenantId) {
        try {
            // 获取需要备份的数据库名称
            String databaseName = getConfigValue("database_backup_name", "ps-bmp");

            LOGGER.info("开始执行数据库备份，数据库名称: {}, 备份类型: {}, 操作人: {}", databaseName, backupType, operator);

            // 检查是否有正在进行的备份
            if (hasRunningBackup(databaseName)) {
                throw new TopinfoRuntimeException(-1, "该数据库正在备份中，请稍后再试");
            }
            
            // 获取备份配置
            String backupDir = getConfigValue("database_backup_dir", "/opt/backup/mysql");
            String dbHost = getConfigValue("database_backup_host", "localhost:3306");
            String dbUsername = getConfigValue("database_backup_username", "root");
            String dbPassword = getConfigValue("database_backup_password", "");

            // 解析主机和端口
            String[] hostParts = dbHost.split(":");
            String host = hostParts[0];
            String port = hostParts.length > 1 ? hostParts[1] : "3306";

            // 创建备份记录
            TpDatabaseBackupLog backupLog = createBackupLog(databaseName, backupType, operator, tenantId, backupDir);
            
            // 异步执行备份
            CompletableFuture.runAsync(() -> performBackup(backupLog, host, port, dbUsername, dbPassword));

            // 转换为VO并返回
            TpDatabaseBackupLogVO vo = new TpDatabaseBackupLogVO();
            BeanUtil.copyProperties(backupLog, vo);
            return vo;

        } catch (TopinfoRuntimeException e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("执行数据库备份失败，错误: {}", ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-1, "执行数据库备份失败: " + e.getMessage());
        }
    }

    /**
     * 执行自动备份任务
     */
    @Override
    public void executeAutoBackup() {
        try {
            // 检查自动备份开关
            String autoBackupEnabled = getConfigValue("database_auto_backup", "0");
            if (!"1".equals(autoBackupEnabled)) {
                LOGGER.info("自动备份功能已关闭");
                return;
            }

            // 获取需要备份的数据库名称
            String databaseName = getConfigValue("database_backup_name", "ps-bmp");
            
            LOGGER.info("开始执行自动备份任务，数据库名称: {}", databaseName);
            executeBackup(BACKUP_TYPE_AUTO, "system", "system");

        } catch (Exception e) {
            LOGGER.error("执行自动备份任务失败，错误: {}", ExceptionUtils.getStackTrace(e));
        }
    }

    /**
     * 检查备份配置是否有效
     */
    @Override
    public boolean isBackupConfigValid() {
        try {
            String backupDir = getConfigValue("database_backup_dir", "");
            String dbHost = getConfigValue("database_backup_host", "");
            String dbUsername = getConfigValue("database_backup_username", "");
            String databaseName = getConfigValue("database_backup_name", "");
            String mysqldumpPath = getConfigValue("database_backup_mysqldump", "mysqldump");

            return StrUtil.isNotBlank(backupDir) && 
                   StrUtil.isNotBlank(dbHost) && 
                   StrUtil.isNotBlank(dbUsername) && 
                   StrUtil.isNotBlank(databaseName) &&
                   StrUtil.isNotBlank(mysqldumpPath);
        } catch (Exception e) {
            LOGGER.error("检查备份配置失败，错误: {}", ExceptionUtils.getStackTrace(e));
            return false;
        }
    }

    /**
     * 获取备份配置信息
     */
    @Override
    public String getBackupConfigInfo() {
        try {
            StringBuilder info = new StringBuilder();
            info.append("备份目录: ").append(getConfigValue("database_backup_dir", "未配置")).append("\n");
            info.append("数据库地址: ").append(getConfigValue("database_backup_host", "未配置")).append("\n");
            info.append("数据库名称: ").append(getConfigValue("database_backup_name", "未配置")).append("\n");
            info.append("mysqldump路径: ").append(getConfigValue("database_backup_mysqldump", "mysqldump")).append("\n");
            info.append("自动备份: ").append("1".equals(getConfigValue("database_auto_backup", "0")) ? "已启用" : "已禁用").append("\n");
            info.append("备份时间: ").append(getConfigValue("database_auto_backup_cron", "未配置"));
            return info.toString();
        } catch (Exception e) {
            return "获取配置信息失败: " + e.getMessage();
        }
    }

    /**
     * 清理过期的备份文件
     */
    @Override
    public int cleanExpiredBackupFiles() {
        try {
            String retainDaysStr = getConfigValue("database_backup_retain_days", "30");
            int retainDays = Integer.parseInt(retainDaysStr);
            String backupDir = getConfigValue("database_backup_dir", "/opt/backup/mysql");

            Path backupPath = Paths.get(backupDir);
            if (!Files.exists(backupPath)) {
                return 0;
            }

            // 计算过期时间
            long expireTime = System.currentTimeMillis() - (retainDays * 24 * 60 * 60 * 1000L);
            
            // 清理过期文件
            int cleanedCount = 0;
            File[] files = backupPath.toFile().listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isFile() && file.getName().endsWith(".sql") && file.lastModified() < expireTime) {
                        if (file.delete()) {
                            cleanedCount++;
                            LOGGER.info("删除过期备份文件: {}", file.getName());
                        }
                    }
                }
            }

            LOGGER.info("清理过期备份文件完成，共清理 {} 个文件", cleanedCount);
            return cleanedCount;

        } catch (Exception e) {
            LOGGER.error("清理过期备份文件失败，错误: {}", ExceptionUtils.getStackTrace(e));
            return 0;
        }
    }

    /**
     * 检查是否有正在进行的备份任务
     */
    @Override
    public boolean hasRunningBackup(String databaseName) {
        try {
            List<TpDatabaseBackupLog> runningBackups = tpDatabaseBackupLogMapper.getRunningBackups(databaseName);
            return runningBackups != null && !runningBackups.isEmpty();
        } catch (Exception e) {
            LOGGER.error("检查正在进行的备份任务失败，错误: {}", ExceptionUtils.getStackTrace(e));
            return false;
        }
    }

    /**
     * 停止指定的备份任务
     */
    @Override
    public boolean stopBackup(String backupId, String operator) {
        try {
            LOGGER.info("开始停止备份任务，备份ID: {}, 操作人: {}", backupId, operator);
            
            if (backupId == null || backupId.trim().isEmpty()) {
                LOGGER.error("停止备份失败：备份ID为空");
                return false;
            }
            
            // 先查询备份记录是否存在
            TpDatabaseBackupLogVO existingRecord = tpDatabaseBackupLogMapper.view(backupId);
            if (existingRecord == null) {
                LOGGER.error("停止备份失败：找不到备份记录，backupId: {}", backupId);
                return false;
            }
            
            LOGGER.info("找到备份记录，当前状态: {}", existingRecord.getBackupStatus());
            
            TpDatabaseBackupLog backupLog = new TpDatabaseBackupLog();
            backupLog.setBackupId(backupId);
            backupLog.setBackupStatus(BACKUP_STATUS_FAILED);
            backupLog.setErrorMessage("用户手动停止备份");
            backupLog.setUpdator(operator);
            backupLog.setUpdateTime(CommonDateUtil.now());
            
            int result = tpDatabaseBackupLogMapper.update(backupLog);
            LOGGER.info("停止备份任务更新结果，影响行数: {}", result);
            
            boolean success = result > 0;
            if (success) {
                LOGGER.info("停止备份任务成功，backupId: {}", backupId);
            } else {
                LOGGER.error("停止备份任务失败，数据库更新失败，backupId: {}", backupId);
            }
            
            return success;
        } catch (Exception e) {
            LOGGER.error("停止备份任务失败，错误: {}", ExceptionUtils.getStackTrace(e));
            return false;
        }
    }

    /**
     * 创建备份记录
     */
    private TpDatabaseBackupLog createBackupLog(String databaseName, Integer backupType, String operator, String tenantId, String backupDir) {
        try {
            String backupId = SnowflakeIdUtil.nextIdStr();
            String now = CommonDateUtil.now();
            
            // 生成备份文件名
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
            String timestamp = sdf.format(new Date());
            String backupFileName = String.format("%s_backup_%s.sql", databaseName, timestamp);
            String backupFilePath = Paths.get(backupDir, backupFileName).toString();

            // 生成备份名称
            String backupName = String.format("%s数据库备份_%s", databaseName, timestamp);

            TpDatabaseBackupLog backupLog = new TpDatabaseBackupLog();
            backupLog.setBackupId(backupId);
            backupLog.setBackupName(backupName);
            backupLog.setBackupType(backupType);
            backupLog.setBackupStatus(BACKUP_STATUS_RUNNING);
            backupLog.setDatabaseName(databaseName);
            backupLog.setBackupFilePath(backupFilePath);
            backupLog.setBackupFileName(backupFileName);
            backupLog.setBackupStartTime(now);
            backupLog.setActived(1);
            backupLog.setTenantId(tenantId);
            backupLog.setCreator(operator);
            backupLog.setCreateTime(now);
            backupLog.setUpdator(operator);
            backupLog.setUpdateTime(now);

            // 保存备份记录
            tpDatabaseBackupLogMapper.save(backupLog);
            LOGGER.info("创建备份记录成功，备份ID: {}", backupId);

            return backupLog;
        } catch (Exception e) {
            LOGGER.error("创建备份记录失败，错误: {}", ExceptionUtils.getStackTrace(e));
            throw new TopinfoRuntimeException(-1, "创建备份记录失败");
        }
    }

    /**
     * 执行实际的备份操作
     */
    private void performBackup(TpDatabaseBackupLog backupLog, String host, String port, String username, String password) {
        long startTime = System.currentTimeMillis();
        
        try {
            // 确保备份目录存在
            File backupFile = new File(backupLog.getBackupFilePath());
            File backupDir = backupFile.getParentFile();
            if (!backupDir.exists()) {
                backupDir.mkdirs();
            }

            // 构建mysqldump命令
            String command = buildMysqldumpCommand(host, port, username, password, 
                                                 backupLog.getDatabaseName(), backupLog.getBackupFilePath());
            
            LOGGER.info("执行备份命令: {}", command.replaceAll(password, "****"));

            // 更新备份命令到记录中
            updateBackupCommand(backupLog.getBackupId(), command.replaceAll(password, "****"));
            
            // 执行备份命令
            LOGGER.info("开始解析命令行: {}", command.replaceAll(password, "****"));
            CommandLine cmdLine = CommandLine.parse(command);
            LOGGER.info("命令行解析完成，可执行文件: {}, 参数: {}", cmdLine.getExecutable(), java.util.Arrays.toString(cmdLine.getArguments()));
            
            DefaultExecutor executor = new DefaultExecutor();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ByteArrayOutputStream errorStream = new ByteArrayOutputStream();
            PumpStreamHandler streamHandler = new PumpStreamHandler(outputStream, errorStream);
            executor.setStreamHandler(streamHandler);

            // 设置超时时间（30分钟）
            ExecuteWatchdog watchdog = new ExecuteWatchdog(30 * 60 * 1000);
            executor.setWatchdog(watchdog);
            
            LOGGER.info("开始执行备份命令...");
            int exitCode = executor.execute(cmdLine);
            
            String standardOutput = outputStream.toString();
            String errorOutput = errorStream.toString();
            
            LOGGER.info("命令执行完成，退出码: {}", exitCode);
            if (StrUtil.isNotBlank(standardOutput)) {
                LOGGER.info("标准输出: {}", standardOutput);
            }
            if (StrUtil.isNotBlank(errorOutput)) {
                LOGGER.error("错误输出: {}", errorOutput);
            }

            long endTime = System.currentTimeMillis();
            int duration = (int) ((endTime - startTime) / 1000);

            if (exitCode == 0) {
                // 备份成功
                long fileSize = backupFile.exists() ? backupFile.length() : 0;
                updateBackupSuccess(backupLog.getBackupId(), fileSize, duration);
                LOGGER.info("数据库备份成功，备份ID: {}, 文件大小: {} 字节, 耗时: {} 秒", 
                           backupLog.getBackupId(), fileSize, duration);
            } else {
                // 备份失败
                String detailedError = String.format("备份命令执行失败，退出码: %d\n错误输出: %s\n标准输出: %s", 
                                                    exitCode, errorOutput, standardOutput);
                updateBackupFailure(backupLog.getBackupId(), detailedError, duration);
                LOGGER.error("数据库备份失败，备份ID: {}, 退出码: {}, 错误信息: {}", 
                            backupLog.getBackupId(), exitCode, detailedError);
            }

        } catch (Exception e) {
            long endTime = System.currentTimeMillis();
            int duration = (int) ((endTime - startTime) / 1000);
            
            String errorMessage = "备份执行异常: " + e.getMessage();
            updateBackupFailure(backupLog.getBackupId(), errorMessage, duration);
            LOGGER.error("数据库备份异常，备份ID: {}, 错误: {}", backupLog.getBackupId(), ExceptionUtils.getStackTrace(e));
        }
    }

    /**
     * 构建mysqldump命令
     */
    private String buildMysqldumpCommand(String host, String port, String username, String password, String databaseName, String outputFile) {
        StringBuilder command = new StringBuilder();
        
        // 获取mysqldump程序路径，默认使用mysqldump
        String mysqldumpPath = getConfigValue("database_backup_mysqldump", "mysqldump");
        
        // 基础命令（注意参数间要有空格）
        command.append(mysqldumpPath);
        command.append(" -h ").append(host);
        command.append(" -P ").append(port);
        command.append(" -u ").append(username);
        
        if (StrUtil.isNotBlank(password)) {
            command.append(" -p").append(password);
        }
        
        // 备份选项
        command.append(" --single-transaction");  // 保证事务一致性
        command.append(" --routines");            // 备份存储过程和函数
        command.append(" --triggers");            // 备份触发器
        command.append(" --lock-tables=false");   // 不锁定表
        command.append(" --add-drop-database");   // 添加删除数据库语句
        command.append(" --default-character-set=utf8");
        command.append(" --databases ").append(databaseName);
        
        // 使用--result-file参数代替重定向操作符
        command.append(" --result-file=\"").append(outputFile).append("\"");
        
        return command.toString();
    }

    /**
     * 更新备份命令
     */
    private void updateBackupCommand(String backupId, String command) {
        try {
            TpDatabaseBackupLog backupLog = new TpDatabaseBackupLog();
            backupLog.setBackupId(backupId);
            backupLog.setBackupCommand(command);
            tpDatabaseBackupLogMapper.update(backupLog);
        } catch (Exception e) {
            LOGGER.error("更新备份命令失败，备份ID: {}, 错误: {}", backupId, ExceptionUtils.getStackTrace(e));
        }
    }

    /**
     * 更新备份成功状态
     */
    private void updateBackupSuccess(String backupId, long fileSize, int duration) {
        try {
            String now = CommonDateUtil.now();
            TpDatabaseBackupLog backupLog = new TpDatabaseBackupLog();
            backupLog.setBackupId(backupId);
            backupLog.setBackupStatus(BACKUP_STATUS_SUCCESS);
            backupLog.setBackupFileSize(fileSize);
            backupLog.setBackupEndTime(now);
            backupLog.setBackupDuration(duration);
            backupLog.setUpdateTime(now);
            
            tpDatabaseBackupLogMapper.update(backupLog);
        } catch (Exception e) {
            LOGGER.error("更新备份成功状态失败，备份ID: {}, 错误: {}", backupId, ExceptionUtils.getStackTrace(e));
        }
    }

    /**
     * 更新备份失败状态
     */
    private void updateBackupFailure(String backupId, String errorMessage, int duration) {
        try {
            String now = CommonDateUtil.now();
            TpDatabaseBackupLog backupLog = new TpDatabaseBackupLog();
            backupLog.setBackupId(backupId);
            backupLog.setBackupStatus(BACKUP_STATUS_FAILED);
            backupLog.setErrorMessage(errorMessage);
            backupLog.setBackupEndTime(now);
            backupLog.setBackupDuration(duration);
            backupLog.setUpdateTime(now);
            
            tpDatabaseBackupLogMapper.update(backupLog);
        } catch (Exception e) {
            LOGGER.error("更新备份失败状态失败，备份ID: {}, 错误: {}", backupId, ExceptionUtils.getStackTrace(e));
        }
    }

    /**
     * 获取配置值
     */
    private String getConfigValue(String configKey, String defaultValue) {
        try {
            // 通过TpSystemConfigService获取系统配置值
            if (tpSystemConfigService != null) {
                String value = tpSystemConfigService.getConfigValue(configKey, defaultValue);
                return value;
            }
            return defaultValue;
        } catch (Exception e) {
            LOGGER.warn("获取配置值失败，使用默认值。configKey: {}, defaultValue: {}, 错误: {}", 
                       configKey, defaultValue, e.getMessage());
            return defaultValue;
        }
    }

    /**
     * 测试mysqldump连接
     */
    @Override
    public String testMysqldumpConnection() {
        try {
            StringBuilder result = new StringBuilder();
            
            // 获取配置
            String mysqldumpPath = getConfigValue("database_backup_mysqldump", "mysqldump");
            String dbHost = getConfigValue("database_backup_host", "localhost:3306");
            String dbUsername = getConfigValue("database_backup_username", "root");
            String dbPassword = getConfigValue("database_backup_password", "");
            String databaseName = getConfigValue("database_backup_name", "ps-bmp");
            
            // 解析主机和端口
            String[] hostParts = dbHost.split(":");
            String host = hostParts[0];
            String port = hostParts.length > 1 ? hostParts[1] : "3306";
            
            result.append("配置信息:\n");
            result.append("mysqldump路径: ").append(mysqldumpPath).append("\n");
            result.append("数据库地址: ").append(host).append(":").append(port).append("\n");
            result.append("用户名: ").append(dbUsername).append("\n");
            result.append("数据库名: ").append(databaseName).append("\n\n");
            
            // 构建简化的测试命令（只显示版本信息）
            String testCommand = mysqldumpPath + " --version";
            result.append("测试命令: ").append(testCommand).append("\n");
            
            try {
                CommandLine cmdLine = CommandLine.parse(testCommand);
                DefaultExecutor executor = new DefaultExecutor();
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                ByteArrayOutputStream errorStream = new ByteArrayOutputStream();
                PumpStreamHandler streamHandler = new PumpStreamHandler(outputStream, errorStream);
                executor.setStreamHandler(streamHandler);
                
                int exitCode = executor.execute(cmdLine);
                
                if (exitCode == 0) {
                    result.append("版本信息: ").append(outputStream.toString().trim()).append("\n");
                } else {
                    result.append("错误: mysqldump命令执行失败\n");
                    result.append("错误信息: ").append(errorStream.toString()).append("\n");
                }
            } catch (Exception e) {
                result.append("异常: ").append(e.getMessage()).append("\n");
            }
            
            // 测试数据库连接命令
            String connectionTestCommand = String.format("%s -h%s -P%s -u%s %s -e \"SELECT VERSION();\"",
                mysqldumpPath.replace("mysqldump", "mysql"), host, port, dbUsername,
                StrUtil.isNotBlank(dbPassword) ? "-p****" : "");
            result.append("\n数据库连接测试命令: ").append(connectionTestCommand).append("\n");
            
            return result.toString();
            
        } catch (Exception e) {
            LOGGER.error("测试mysqldump连接失败，错误: {}", ExceptionUtils.getStackTrace(e));
            return "测试失败: " + e.getMessage();
        }
    }
}