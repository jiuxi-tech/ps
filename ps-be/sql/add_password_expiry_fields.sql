-- ========================================
-- 密码有效期功能相关字段添加
-- 创建时间: 2025-01-29
-- 描述: 为密码有效期功能添加必要的数据库字段
-- ========================================

-- 1. 在账号表添加上次密码修改时间字段
ALTER TABLE `tp_account` 
ADD COLUMN `last_password_change_time` DATETIME DEFAULT NULL COMMENT '上次密码修改时间' 
AFTER `update_time`;

-- 2. 为现有用户设置默认的密码修改时间（设为当前时间）
UPDATE `tp_account` 
SET `last_password_change_time` = NOW() 
WHERE `last_password_change_time` IS NULL;

-- 3. 在系统配置表中添加密码有效期配置
INSERT INTO `tp_system_config` (`config_key`, `config_value`, `description`) VALUES
('password.validity.months', '3', '密码有效期（月）'),
('password.expiry.reminder.days', '30', '密码到期提醒天数')
ON DUPLICATE KEY UPDATE 
    config_value = VALUES(config_value),
    description = VALUES(description),
    update_time = CURRENT_TIMESTAMP;

-- 4. 创建索引以提高查询性能
CREATE INDEX `idx_last_password_change_time` ON `tp_account`(`last_password_change_time`);

-- ========================================
-- 验证脚本
-- ========================================

-- 查看字段是否添加成功
-- DESCRIBE tp_account;

-- 查看配置是否添加成功
-- SELECT * FROM tp_system_config WHERE config_key LIKE 'password%';

-- 查看索引是否创建成功
-- SHOW INDEX FROM tp_account WHERE Key_name = 'idx_last_password_change_time';