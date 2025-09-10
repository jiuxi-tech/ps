-- ========================================
-- 移除标签表的逻辑删除字段
-- 创建时间: 2025-01-28
-- 描述: 从tp_tag表移除log_delete字段，改为物理删除
-- ========================================

-- 删除索引（如果存在）
DROP INDEX IF EXISTS `idx_tag_log_delete` ON `tp_tag`;

-- 删除log_delete字段
ALTER TABLE `tp_tag` DROP COLUMN IF EXISTS `log_delete`;

-- 验证字段是否删除成功
SELECT COLUMN_NAME, DATA_TYPE, IS_NULLABLE, COLUMN_DEFAULT, COLUMN_COMMENT 
FROM INFORMATION_SCHEMA.COLUMNS 
WHERE TABLE_SCHEMA = DATABASE() 
AND TABLE_NAME = 'tp_tag' 
AND COLUMN_NAME = 'log_delete';

-- 如果上述查询返回空结果，说明字段删除成功