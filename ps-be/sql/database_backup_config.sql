-- ===================================================================
-- 数据库备份相关系统配置项
-- Database Backup System Configuration
-- ===================================================================

-- 插入数据库备份相关配置项到 tp_system_config 表
-- 注意：请根据实际的 tp_system_config 表结构调整字段名称

-- 1. 数据库自动备份开关配置
INSERT INTO tp_system_config (
 
    config_key, 
    config_value, 
    description, 
  
    create_time, 
    update_time
) VALUES ( 
    'database_auto_backup',
    '1',
    '数据库自动备份开关 1:开启 0:关闭',
 
 
    NOW(),

    NOW()
);

-- 2. 数据库自动备份Cron表达式配置
INSERT INTO tp_system_config (
 
    config_key, 
    config_value, 
    description, 
 
 
 
  
    create_time, 
    update_time
) VALUES ( 
    'database_auto_backup_cron',
    '0 0 2 * * ?',
    '数据库自动备份时间表达式，默认每天凌晨2点执行',

 
    NOW(),

    NOW()
);

-- 3. 数据库备份文件存放目录配置
INSERT INTO tp_system_config (
   config_key, 
    config_value, 
    description, 
 
 
    create_time, 
    update_time
) VALUES ( 
    'database_backup_dir',
    '/opt/backup/mysql',
    '数据库备份文件存放目录路径',
 NOW(),

    NOW()
);

-- 4. 需要备份的数据库名称配置
INSERT INTO tp_system_config (
  config_key, 
    config_value, 
    description, 
 
 
    create_time, 
    update_time
) VALUES ( 
    'database_backup_name',
    'ps-bmp',
    '需要备份的数据库名称',
   NOW(),

    NOW()
);

-- 5. 数据库备份保留天数配置（可选）
INSERT INTO tp_system_config (
    config_key, 
    config_value, 
    description, 
 
 
    create_time, 
    update_time
) VALUES ( 
    'database_backup_retain_days',
    '30',
    '数据库备份文件保留天数，超过该天数的备份文件将被清理',
 NOW(),

    NOW()
);

-- 6. 备份时的MySQL用户名配置
INSERT INTO tp_system_config (
     config_key, 
    config_value, 
    description, 
 
 
    create_time, 
    update_time
) VALUES ( 
    'database_backup_username',
    'root',
    '数据库备份时使用的MySQL用户名',
 NOW(),

    NOW()
);

-- 7. 备份时的MySQL密码配置（注意：实际使用时建议加密存储）
INSERT INTO tp_system_config (
   config_key, 
    config_value, 
    description, 
 
 
    create_time, 
    update_time
) VALUES ( 
    'database_backup_password',
    'W4HV=QxtHz',
    '数据库备份时使用的MySQL密码（建议加密存储）',
 NOW(),

    NOW()
);

-- 8. 数据库服务器地址和端口配置
INSERT INTO tp_system_config (
    config_key, 
    config_value, 
    description, 
 
 
    create_time, 
    update_time
) VALUES ( 
    'database_backup_host',
    'alilaoba.cn:13307',
    '数据库服务器地址和端口',
  NOW(),

    NOW()
);