-- 移除标签表中的category字段
-- 执行时间: 2024-01-27
-- 描述: 移除tp_tag表中的category字段和相关索引

-- 删除category字段的索引（如果存在）
DROP INDEX IF EXISTS `idx_tag_category` ON `tp_tag`;

-- 删除category字段
ALTER TABLE `tp_tag` DROP COLUMN IF EXISTS `category`;

-- 验证字段是否已删除
-- DESCRIBE tp_tag;