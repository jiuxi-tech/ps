-- ========================================
-- 时间规则表字段更新脚本 (DM数据库版本)
-- 创建时间: 2025-01-31
-- 描述: 更新tp_time_rule表结构，重命名字段和删除不需要的字段
-- ========================================

-- 1. 添加新字段 allow_login
ALTER TABLE tp_time_rule ADD COLUMN allow_login INTEGER DEFAULT 1;

-- 2. 复制数据从 rule_type 到 allow_login
UPDATE tp_time_rule SET allow_login = rule_type;

-- 3. 删除旧字段 rule_type
ALTER TABLE tp_time_rule DROP COLUMN rule_type;

-- 4. 删除不需要的字段
ALTER TABLE tp_time_rule DROP COLUMN tenant_id;
ALTER TABLE tp_time_rule DROP COLUMN ascn_id;

-- 5. 添加字段注释
COMMENT ON COLUMN tp_time_rule.allow_login IS '是否允许登录：1-允许，0-拒绝';

-- 6. 删除相关的旧索引
DROP INDEX idx_rule_type;
DROP INDEX idx_tenant_id;
DROP INDEX idx_ascn_id;

-- 7. 创建新的索引
CREATE INDEX idx_allow_login ON tp_time_rule(allow_login);

-- 8. 更新表注释
COMMENT ON TABLE tp_time_rule IS '登录时间段控制规则表：用于控制特定用户或角色在指定时间段的登录权限';

-- 验证更新结果
-- 可以运行以下查询来验证表结构是否正确更新：
-- SELECT * FROM USER_TAB_COLUMNS WHERE TABLE_NAME = 'TP_TIME_RULE';
-- SELECT * FROM USER_INDEXES WHERE TABLE_NAME = 'TP_TIME_RULE';

-- 注意事项：
-- 1. 执行此脚本前请备份数据库
-- 2. 确保应用程序已停止或处于维护模式
-- 3. 执行后需要重启应用程序以使ORM映射生效
-- 4. 建议在测试环境先验证脚本的正确性
-- 5. DM数据库不支持直接重命名字段，采用添加-复制-删除的方式