-- ========================================
-- 为标签表添加逻辑删除字段
-- 创建时间: 2025-01-28
-- 描述: 为tp_tag表添加log_delete字段用于逻辑删除
-- ========================================

-- 为tp_tag表添加逻辑删除字段
ALTER TABLE `tp_tag` ADD COLUMN `log_delete` INTEGER DEFAULT 0 COMMENT '逻辑删除（0：未删除，1：已删除）' AFTER `actived`;

-- 创建索引以提高查询性能
CREATE INDEX `idx_tag_log_delete` ON `tp_tag`(`log_delete`);

-- 验证字段是否添加成功
SELECT COLUMN_NAME, DATA_TYPE, IS_NULLABLE, COLUMN_DEFAULT, COLUMN_COMMENT 
FROM INFORMATION_SCHEMA.COLUMNS 
WHERE TABLE_SCHEMA = DATABASE() 
AND TABLE_NAME = 'tp_tag' 
AND COLUMN_NAME = 'log_delete';