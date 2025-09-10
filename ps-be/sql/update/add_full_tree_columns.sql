-- ========================================
-- 组织机构历史表结构更新脚本
-- 创建时间: 2024-01-27
-- 描述: 为组织机构变更历史表添加前后全节点树字段
-- ========================================

-- 检查表是否存在
SELECT COUNT(*) as table_exists 
FROM information_schema.tables 
WHERE table_schema = DATABASE() 
AND table_name = 'org_tree_change_history';

-- 添加前后全节点树字段
-- 如果字段不存在则添加
SET @sql = '';
SELECT COUNT(*) INTO @col_exists 
FROM information_schema.columns 
WHERE table_schema = DATABASE() 
AND table_name = 'org_tree_change_history' 
AND column_name = 'before_full_tree';

SET @sql = IF(@col_exists = 0, 
    'ALTER TABLE `org_tree_change_history` 
     ADD COLUMN `before_full_tree` LONGTEXT COMMENT "变更前的完整组织机构节点树JSON" AFTER `after_data`',
    'SELECT "before_full_tree column already exists" as message');
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SELECT COUNT(*) INTO @col_exists 
FROM information_schema.columns 
WHERE table_schema = DATABASE() 
AND table_name = 'org_tree_change_history' 
AND column_name = 'after_full_tree';

SET @sql = IF(@col_exists = 0, 
    'ALTER TABLE `org_tree_change_history` 
     ADD COLUMN `after_full_tree` LONGTEXT COMMENT "变更后的完整组织机构节点树JSON" AFTER `before_full_tree`',
    'SELECT "after_full_tree column already exists" as message');
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- 验证字段是否添加成功
SELECT 
    column_name,
    data_type,
    is_nullable,
    column_comment
FROM information_schema.columns 
WHERE table_schema = DATABASE() 
AND table_name = 'org_tree_change_history' 
AND column_name IN ('before_full_tree', 'after_full_tree')
ORDER BY ordinal_position;

-- 查看更新后的表结构
DESCRIBE org_tree_change_history;