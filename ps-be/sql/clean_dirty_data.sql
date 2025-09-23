-- =====================================================
-- 清理脏数据SQL脚本
-- 用于清除与已不存在的tp_person相关的脏数据
-- 创建时间: 2025-01-24
-- =====================================================

-- 开始事务
START TRANSACTION;

-- 1. 清理 tp_person_dept 表中的脏数据
-- 删除 PERSON_ID 在 tp_person_basicinfo 中不存在的记录
DELETE FROM tp_person_dept 
WHERE PERSON_ID IS NOT NULL 
  AND PERSON_ID NOT IN (
    SELECT PERSON_ID 
    FROM tp_person_basicinfo 
    WHERE PERSON_ID IS NOT NULL
  );

-- 2. 清理 tp_person_role 表中的脏数据
-- 删除 PERSON_ID 在 tp_person_basicinfo 中不存在的记录
DELETE FROM tp_person_role 
WHERE PERSON_ID IS NOT NULL 
  AND PERSON_ID NOT IN (
    SELECT PERSON_ID 
    FROM tp_person_basicinfo 
    WHERE PERSON_ID IS NOT NULL
  );

-- 3. 清理 tp_person_exinfo 表中的脏数据
-- 删除 PERSON_ID 在 tp_person_basicinfo 中不存在的记录
DELETE FROM tp_person_exinfo 
WHERE PERSON_ID IS NOT NULL 
  AND PERSON_ID NOT IN (
    SELECT PERSON_ID 
    FROM tp_person_basicinfo 
    WHERE PERSON_ID IS NOT NULL
  );

-- 4. 清理 tp_person_tag 表中的脏数据
-- 删除 person_id 在 tp_person_basicinfo 中不存在的记录
DELETE FROM tp_person_tag 
WHERE person_id IS NOT NULL 
  AND person_id NOT IN (
    SELECT PERSON_ID 
    FROM tp_person_basicinfo 
    WHERE PERSON_ID IS NOT NULL
  );

-- 5. 清理 tp_account 表中的脏数据
-- 删除 PERSON_ID 在 tp_person_basicinfo 中不存在的记录
DELETE FROM tp_account 
WHERE PERSON_ID IS NOT NULL 
  AND PERSON_ID NOT IN (
    SELECT PERSON_ID 
    FROM tp_person_basicinfo 
    WHERE PERSON_ID IS NOT NULL
  );

-- 6. 清理 tp_account_exinfo 表中的脏数据
-- 删除 ACCOUNT_ID 在 tp_account 中不存在的记录
DELETE FROM tp_account_exinfo 
WHERE ACCOUNT_ID IS NOT NULL 
  AND ACCOUNT_ID NOT IN (
    SELECT ACCOUNT_ID 
    FROM tp_account 
    WHERE ACCOUNT_ID IS NOT NULL
  );

-- 提交事务
COMMIT;

-- =====================================================
-- 验证清理结果的查询语句
-- =====================================================

-- 验证 tp_person_dept 脏数据是否已清理
SELECT 'tp_person_dept' as table_name, COUNT(*) as remaining_dirty_count 
FROM tp_person_dept 
WHERE PERSON_ID IS NOT NULL 
  AND PERSON_ID NOT IN (
    SELECT PERSON_ID 
    FROM tp_person_basicinfo 
    WHERE PERSON_ID IS NOT NULL
  )

UNION ALL

-- 验证 tp_person_role 脏数据是否已清理
SELECT 'tp_person_role' as table_name, COUNT(*) as remaining_dirty_count 
FROM tp_person_role 
WHERE PERSON_ID IS NOT NULL 
  AND PERSON_ID NOT IN (
    SELECT PERSON_ID 
    FROM tp_person_basicinfo 
    WHERE PERSON_ID IS NOT NULL
  )

UNION ALL

-- 验证 tp_person_exinfo 脏数据是否已清理
SELECT 'tp_person_exinfo' as table_name, COUNT(*) as remaining_dirty_count 
FROM tp_person_exinfo 
WHERE PERSON_ID IS NOT NULL 
  AND PERSON_ID NOT IN (
    SELECT PERSON_ID 
    FROM tp_person_basicinfo 
    WHERE PERSON_ID IS NOT NULL
  )

UNION ALL

-- 验证 tp_person_tag 脏数据是否已清理
SELECT 'tp_person_tag' as table_name, COUNT(*) as remaining_dirty_count 
FROM tp_person_tag 
WHERE person_id IS NOT NULL 
  AND person_id NOT IN (
    SELECT PERSON_ID 
    FROM tp_person_basicinfo 
    WHERE PERSON_ID IS NOT NULL
  )

UNION ALL

-- 验证 tp_account 脏数据是否已清理
SELECT 'tp_account' as table_name, COUNT(*) as remaining_dirty_count 
FROM tp_account 
WHERE PERSON_ID IS NOT NULL 
  AND PERSON_ID NOT IN (
    SELECT PERSON_ID 
    FROM tp_person_basicinfo 
    WHERE PERSON_ID IS NOT NULL
  )

UNION ALL

-- 验证 tp_account_exinfo 脏数据是否已清理
SELECT 'tp_account_exinfo' as table_name, COUNT(*) as remaining_dirty_count 
FROM tp_account_exinfo 
WHERE ACCOUNT_ID IS NOT NULL 
  AND ACCOUNT_ID NOT IN (
    SELECT ACCOUNT_ID 
    FROM tp_account 
    WHERE ACCOUNT_ID IS NOT NULL
  );

-- =====================================================
-- 使用说明:
-- 1. 执行前请先备份相关数据表
-- 2. 建议在测试环境先执行验证
-- 3. 脚本使用事务确保数据一致性
-- 4. 执行后可运行验证查询检查清理结果
-- =====================================================