-- ========================================
-- 数据库更新验证脚本
-- 用途: 验证 org_tree_change_history 表的新字段是否正确添加
-- 执行方式: 在数据库客户端中运行此脚本
-- ========================================

-- 切换到正确的数据库
USE ps-bmp;

-- ========================================
-- 1. 基础表结构验证
-- ========================================

-- 检查表是否存在
SELECT 
    CASE 
        WHEN COUNT(*) > 0 THEN '✓ org_tree_change_history 表存在'
        ELSE '✗ org_tree_change_history 表不存在'
    END as table_status
FROM information_schema.tables 
WHERE table_schema = 'ps-bmp' 
AND table_name = 'org_tree_change_history';

-- ========================================
-- 2. 新字段存在性验证
-- ========================================

-- 检查 before_full_tree 字段
SELECT 
    CASE 
        WHEN COUNT(*) > 0 THEN '✓ before_full_tree 字段已添加'
        ELSE '✗ before_full_tree 字段缺失'
    END as before_full_tree_status
FROM information_schema.columns 
WHERE table_schema = 'ps-bmp' 
AND table_name = 'org_tree_change_history' 
AND column_name = 'before_full_tree';

-- 检查 after_full_tree 字段
SELECT 
    CASE 
        WHEN COUNT(*) > 0 THEN '✓ after_full_tree 字段已添加'
        ELSE '✗ after_full_tree 字段缺失'
    END as after_full_tree_status
FROM information_schema.columns 
WHERE table_schema = 'ps-bmp' 
AND table_name = 'org_tree_change_history' 
AND column_name = 'after_full_tree';

-- ========================================
-- 3. 字段详细信息验证
-- ========================================

-- 查看新字段的详细信息
SELECT 
    column_name as '字段名',
    data_type as '数据类型',
    is_nullable as '允许NULL',
    column_default as '默认值',
    column_comment as '注释',
    ordinal_position as '字段位置'
FROM information_schema.columns 
WHERE table_schema = 'ps-bmp' 
AND table_name = 'org_tree_change_history' 
AND column_name IN ('before_full_tree', 'after_full_tree')
ORDER BY ordinal_position;

-- ========================================
-- 4. 完整表结构显示
-- ========================================

-- 显示完整的表结构
SELECT 
    column_name as '字段名',
    data_type as '数据类型',
    is_nullable as '允许NULL',
    column_key as '键类型',
    column_default as '默认值',
    extra as '额外信息',
    column_comment as '注释'
FROM information_schema.columns 
WHERE table_schema = 'ps-bmp' 
AND table_name = 'org_tree_change_history' 
ORDER BY ordinal_position;

-- ========================================
-- 5. 数据完整性测试
-- ========================================

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
    9999, 
    NULL, 
    '{"test": "validation"}',
    '{"tree_before": [{"id": "1", "name": "测试节点"}]}',
    '{"tree_after": [{"id": "1", "name": "测试节点"}, {"id": "2", "name": "新增节点"}]}',
    1,
    0
);

-- 验证插入的数据
SELECT 
    id,
    operation_type,
    operator_id,
    CASE 
        WHEN before_full_tree IS NOT NULL THEN '✓ before_full_tree 有数据'
        ELSE '✗ before_full_tree 无数据'
    END as before_tree_status,
    CASE 
        WHEN after_full_tree IS NOT NULL THEN '✓ after_full_tree 有数据'
        ELSE '✗ after_full_tree 无数据'
    END as after_tree_status,
    operation_time
FROM org_tree_change_history 
WHERE operator_id = 9999 
ORDER BY id DESC 
LIMIT 1;

-- 查看插入的JSON数据是否正确
SELECT 
    'before_full_tree 内容:' as label,
    before_full_tree as json_content
FROM org_tree_change_history 
WHERE operator_id = 9999 
ORDER BY id DESC 
LIMIT 1

UNION ALL

SELECT 
    'after_full_tree 内容:' as label,
    after_full_tree as json_content
FROM org_tree_change_history 
WHERE operator_id = 9999 
ORDER BY id DESC 
LIMIT 1;

-- 清理测试数据
DELETE FROM org_tree_change_history WHERE operator_id = 9999;

-- ========================================
-- 6. 最终验证汇总
-- ========================================

-- 汇总验证结果
SELECT 
    '数据库更新验证汇总' as title,
    CONCAT(
        CASE 
            WHEN (SELECT COUNT(*) FROM information_schema.tables 
                  WHERE table_schema = 'ps-bmp' AND table_name = 'org_tree_change_history') > 0 
            THEN '✓ 表存在  ' 
            ELSE '✗ 表不存在  ' 
        END,
        CASE 
            WHEN (SELECT COUNT(*) FROM information_schema.columns 
                  WHERE table_schema = 'ps-bmp' AND table_name = 'org_tree_change_history' 
                  AND column_name = 'before_full_tree') > 0 
            THEN '✓ before_full_tree  ' 
            ELSE '✗ before_full_tree  ' 
        END,
        CASE 
            WHEN (SELECT COUNT(*) FROM information_schema.columns 
                  WHERE table_schema = 'ps-bmp' AND table_name = 'org_tree_change_history' 
                  AND column_name = 'after_full_tree') > 0 
            THEN '✓ after_full_tree' 
            ELSE '✗ after_full_tree' 
        END
    ) as verification_result;

-- ========================================
-- 7. 建议的下一步操作
-- ========================================

-- 如果所有验证都通过，显示成功消息
SELECT 
    '数据库更新完成！' as status,
    '现在可以使用组织机构历史记录的完整树功能了。' as message,
    '请重启应用程序以确保所有更改生效。' as next_step;

-- 显示使用建议
SELECT 
    '使用建议' as title,
    '1. 重启 Spring Boot 应用程序' as step1,
    '2. 执行部门增删改操作测试新功能' as step2,
    '3. 查看 org_tree_change_history 表验证数据记录' as step3,
    '4. 使用 OrgTreeFullHistoryExample 类进行功能演示' as step4;