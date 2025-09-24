-- ===================================================================
-- 数据库备份记录表
-- Database Backup Log Table
-- ===================================================================

-- 删除已存在的表（谨慎操作）
-- DROP TABLE IF EXISTS tp_database_backup_log;

-- 创建数据库备份记录表
CREATE TABLE tp_database_backup_log (
    backup_id VARCHAR(32) NOT NULL COMMENT '备份记录ID',
    backup_name VARCHAR(100) NOT NULL COMMENT '备份名称',
    backup_type TINYINT(1) NOT NULL DEFAULT 1 COMMENT '备份类型 1:自动备份 2:手动备份',
    backup_status TINYINT(1) NOT NULL DEFAULT 1 COMMENT '备份状态 1:进行中 2:成功 3:失败',
    database_name VARCHAR(100) NOT NULL COMMENT '备份的数据库名称',
    backup_file_path VARCHAR(500) NULL COMMENT '备份文件存储路径',
    backup_file_name VARCHAR(200) NULL COMMENT '备份文件名称',
    backup_file_size BIGINT NULL COMMENT '备份文件大小(字节)',
    backup_start_time DATETIME NOT NULL COMMENT '备份开始时间',
    backup_end_time DATETIME NULL COMMENT '备份结束时间',
    backup_duration INT NULL COMMENT '备份耗时(秒)',
    backup_command TEXT NULL COMMENT '执行的备份命令',
    error_message TEXT NULL COMMENT '错误信息',
    actived TINYINT(1) NOT NULL DEFAULT 1 COMMENT '是否有效 1:有效 0:无效',
    tenant_id VARCHAR(32) NULL COMMENT '租户ID',
    creator VARCHAR(32) NULL COMMENT '创建人',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updator VARCHAR(32) NULL COMMENT '修改人',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    extend01 VARCHAR(200) NULL COMMENT '扩展字段01',
    extend02 VARCHAR(200) NULL COMMENT '扩展字段02',
    extend03 VARCHAR(200) NULL COMMENT '扩展字段03',
    PRIMARY KEY (backup_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='数据库备份记录表';

-- 创建索引
CREATE INDEX idx_backup_status ON tp_database_backup_log(backup_status);
CREATE INDEX idx_backup_type ON tp_database_backup_log(backup_type);
CREATE INDEX idx_backup_start_time ON tp_database_backup_log(backup_start_time);
CREATE INDEX idx_database_name ON tp_database_backup_log(database_name);
CREATE INDEX idx_tenant_id ON tp_database_backup_log(tenant_id);
CREATE INDEX idx_actived ON tp_database_backup_log(actived);