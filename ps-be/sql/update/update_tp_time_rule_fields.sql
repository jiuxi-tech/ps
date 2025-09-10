-- ========================================
-- 时间规则表字段更新脚本
-- 创建时间: 2025-01-31
-- 描述: 更新tp_time_rule表结构，重命名字段和删除不需要的字段
-- ========================================

-- 1. 重命名字段：rule_type -> allow_login
-- 注意：MySQL不支持直接重命名字段，需要使用CHANGE语句
ALTER TABLE `tp_time_rule` 
CHANGE COLUMN `rule_type` `allow_login` INTEGER DEFAULT 1 COMMENT '是否允许登录：1-允许，0-拒绝';

-- 2. 删除不需要的字段
ALTER TABLE `tp_time_rule` DROP COLUMN `tenant_id`;
ALTER TABLE `tp_time_rule` DROP COLUMN `ascn_id`;

-- 3. 删除相关的旧索引
DROP INDEX IF EXISTS `idx_rule_type` ON `tp_time_rule`;
DROP INDEX IF EXISTS `idx_tenant_id` ON `tp_time_rule`;
DROP INDEX IF EXISTS `idx_ascn_id` ON `tp_time_rule`;

-- 4. 创建新的索引
CREATE INDEX `idx_allow_login` ON `tp_time_rule`(`allow_login`);

-- 5. 更新表注释
ALTER TABLE `tp_time_rule` COMMENT = '登录时间段控制规则表：用于控制特定用户或角色在指定时间段的登录权限';

-- 验证更新结果
-- 可以运行以下查询来验证表结构是否正确更新：
-- DESCRIBE tp_time_rule;
-- SHOW INDEX FROM tp_time_rule;

-- 注意事项：
-- 1. 执行此脚本前请备份数据库
-- 2. 确保应用程序已停止或处于维护模式
-- 3. 执行后需要重启应用程序以使ORM映射生效
-- 4. 建议在测试环境先验证脚本的正确性