-- ========================================
-- 一键数据库更新脚本
-- 功能: 完整执行数据库更新和验证流程
-- 使用方法: 在数据库客户端中运行此脚本
-- ========================================

-- 设置安全模式，防止意外操作
SET SQL_SAFE_UPDATES = 0;

-- 切换到正确的数据库
USE ps-bmp;

-- 显示开始信息
SELECT 
    '开始执行组织机构历史记录表更新...' as message,
    NOW() as start_time;

-- ========================================
-- 第一步: 检查当前状态
-- ========================================

SELECT '=== 第一步: 检查当前表状态 ===' as step;

-- 检查表是否存在
SELECT 
    CASE 
        WHEN COUNT(*) > 0 THEN '✓ org_tree_change_history 表存在，可以继续更新'
        ELSE '✗ org_tree_change_history 表不存在，请先创建表'
    END as table_check_result
FROM information_schema.tables 
WHERE table_schema = 'ps-bmp' 
AND table_name = 'org_tree_change_history';

-- 检查字段是否已存在
SELECT 
    (SELECT COUNT(*) FROM information_schema.columns 
     WHERE table_schema = 'ps-bmp' AND table_name = 'org_tree_change_history' 
     AND column_name = 'before_full_tree') as before_full_tree_exists,
    (SELECT COUNT(*) FROM information_schema.columns 
     WHERE table_schema = 'ps-bmp' AND table_name = 'org_tree_change_history' 
     AND column_name = 'after_full_tree') as after_full_tree_exists;

-- ========================================
-- 第二步: 执行表结构更新
-- ========================================

SELECT '=== 第二步: 执行表结构更新 ===' as step;

-- 添加 before_full_tree 字段
SET @sql = '';
SELECT COUNT(*) INTO @col_exists 
FROM information_schema.columns 
WHERE table_schema = 'ps-bmp' 
AND table_name = 'org_tree_change_history' 
AND column_name = 'before_full_tree';

SET @sql = IF(@col_exists = 0, 
    'ALTER TABLE `org_tree_change_history` 
     ADD COLUMN `before_full_tree` LONGTEXT COMMENT "变更前的完整组织机构节点树JSON" AFTER `after_data`',
    'SELECT "before_full_tree 字段已存在，跳过添加" as message');

PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- 添加 after_full_tree 字段
SELECT COUNT(*) INTO @col_exists 
FROM information_schema.columns 
WHERE table_schema = 'ps-bmp' 
AND table_name = 'org_tree_change_history' 
AND column_name = 'after_full_tree';

SET @sql = IF(@col_exists = 0, 
    'ALTER TABLE `org_tree_change_history` 
     ADD COLUMN `after_full_tree` LONGTEXT COMMENT "变更后的完整组织机构节点树JSON" AFTER `before_full_tree`',
    'SELECT "after_full_tree 字段已存在，跳过添加" as message');

PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- ========================================
-- 第三步: 验证更新结果
-- ========================================

SELECT '=== 第三步: 验证更新结果 ===' as step;

-- 验证新字段是否正确添加
SELECT 
    column_name as '字段名',
    data_type as '数据类型',
    is_nullable as '允许NULL',
    column_comment as '字段注释',
    ordinal_position as '字段位置'
FROM information_schema.columns 
WHERE table_schema = 'ps-bmp' 
AND table_name = 'org_tree_change_history' 
AND column_name IN ('before_full_tree', 'after_full_tree')
ORDER BY ordinal_position;

-- ========================================
-- 第四步: 功能测试
-- ========================================

SELECT '=== 第四步: 功能测试 ===' as step;

-- 测试插入包含新字段的数据
INSERT INTO org_tree_change_history (
    operation_type, 
    operation_time,
    operator_id, 
    before_data, 
    after_data,
    before_full_tree,
    after_full_tree,
    version,
    dept_id
) VALUES (
    'CREATE', 
    NOW(),
    8888, 
    NULL, 
    '{"test": "update_validation", "action": "create_dept"}',
    NULL,
    JSON_OBJECT(
        'root', JSON_ARRAY(
            JSON_OBJECT('id', '1', 'name', '根节点', 'children', JSON_ARRAY(
                JSON_OBJECT('id', '2', 'name', '新增部门', 'children', JSON_ARRAY())
            ))
        )
    ),
    1,
    0
);

-- 验证插入结果
SELECT 
    id,
    operation_type,
    operator_id,
    CHAR_LENGTH(before_full_tree) as before_tree_length,
    CHAR_LENGTH(after_full_tree) as after_tree_length,
    CASE 
        WHEN before_full_tree IS NULL AND after_full_tree IS NOT NULL THEN '✓ 新增操作数据正确'
        ELSE '✗ 数据异常'
    END as test_result,
    operation_time
FROM org_tree_change_history 
WHERE operator_id = 8888 
ORDER BY id DESC 
LIMIT 1;

-- 清理测试数据
DELETE FROM org_tree_change_history WHERE operator_id = 8888;

-- ========================================
-- 第五步: 显示完整表结构
-- ========================================

SELECT '=== 第五步: 完整表结构确认 ===' as step;

-- 显示所有字段信息
SELECT 
    ordinal_position as '位置',
    column_name as '字段名',
    data_type as '数据类型',
    CASE 
        WHEN is_nullable = 'YES' THEN '是' 
        ELSE '否' 
    END as '允许NULL',
    COALESCE(column_default, 'NULL') as '默认值',
    CASE 
        WHEN column_key = 'PRI' THEN '主键'
        WHEN column_key = 'UNI' THEN '唯一'
        WHEN column_key = 'MUL' THEN '索引'
        ELSE ''
    END as '键类型',
    COALESCE(extra, '') as '额外信息',
    COALESCE(column_comment, '') as '注释'
FROM information_schema.columns 
WHERE table_schema = 'ps-bmp' 
AND table_name = 'org_tree_change_history' 
ORDER BY ordinal_position;

-- ========================================
-- 第六步: 最终状态检查
-- ========================================

SELECT '=== 第六步: 最终状态检查 ===' as step;

-- 最终验证结果
SELECT 
    '数据库更新状态检查' as check_type,
    CASE 
        WHEN (
            SELECT COUNT(*) FROM information_schema.columns 
            WHERE table_schema = 'ps-bmp' 
            AND table_name = 'org_tree_change_history' 
            AND column_name IN ('before_full_tree', 'after_full_tree')
        ) = 2 THEN '✓ 所有字段更新成功'
        ELSE '✗ 字段更新失败'
    END as status,
    NOW() as check_time;

-- 显示字段数量统计
SELECT 
    '表字段统计' as info_type,
    COUNT(*) as total_columns,
    SUM(CASE WHEN column_name IN ('before_full_tree', 'after_full_tree') THEN 1 ELSE 0 END) as new_columns
FROM information_schema.columns 
WHERE table_schema = 'ps-bmp' 
AND table_name = 'org_tree_change_history';

-- ========================================
-- 完成信息
-- ========================================

SELECT 
    '🎉 数据库更新完成！' as result,
    '现在可以使用组织机构历史记录的完整树功能了。' as message,
    CONCAT(
        '新增字段: before_full_tree, after_full_tree | ',
        '完成时间: ', NOW()
    ) as details;

-- 使用建议
SELECT 
    '后续操作建议' as title,
    CONCAT(
        '1. 重启 Spring Boot 应用程序 | ',
        '2. 测试部门增删改操作 | ',
        '3. 查看历史记录表验证功能 | ',
        '4. 运行 OrgTreeFullHistoryExample 进行演示'
    ) as recommendations;

-- 恢复安全模式
SET SQL_SAFE_UPDATES = 1;

SELECT '脚本执行完成，请检查上述验证结果。' as final_message;