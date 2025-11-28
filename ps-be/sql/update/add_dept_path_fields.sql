-- ===================================================================
-- 用户导入导出功能重构：部门表路径字段添加
-- 创建时间: 2025-11-27
-- 描述: 为部门表添加层级路径字段以支持导入功能的部门匹配
-- ===================================================================

-- 1. 添加部门名称层级全路径字段
ALTER TABLE tp_dept_basicinfo 
ADD COLUMN FULL_DEPT_NAME VARCHAR(500) DEFAULT '' COMMENT '部门名称层级全路径（如：AAA部>BBB部>CCC办公室）';

-- 2. 添加部门编码层级全路径字段
ALTER TABLE tp_dept_basicinfo 
ADD COLUMN FULL_DEPT_CODE VARCHAR(500) DEFAULT '' COMMENT '部门编码层级全路径（各级部门编号以>连接）';

-- 3. 创建索引以优化查询性能
CREATE INDEX idx_full_dept_name ON tp_dept_basicinfo(FULL_DEPT_NAME);
CREATE INDEX idx_full_dept_code ON tp_dept_basicinfo(FULL_DEPT_CODE);

-- 4. 为人员基本信息表的身份证字段创建索引（如果不存在）
-- 检查索引是否存在，如果不存在则创建
SELECT COUNT(1) INTO @index_exists 
FROM INFORMATION_SCHEMA.STATISTICS 
WHERE TABLE_SCHEMA = 'ps-bmp' 
  AND TABLE_NAME = 'tp_person_basicinfo' 
  AND INDEX_NAME = 'idx_person_idcard';

SET @sql_idcard_index = IF(@index_exists = 0, 
    'CREATE INDEX idx_person_idcard ON tp_person_basicinfo(IDCARD)',
    'SELECT "Index idx_person_idcard already exists"');

PREPARE stmt FROM @sql_idcard_index;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- 5. 为账号表的用户名创建唯一索引（如果不存在）
SELECT COUNT(1) INTO @index_exists_username 
FROM INFORMATION_SCHEMA.STATISTICS 
WHERE TABLE_SCHEMA = 'ps-bmp' 
  AND TABLE_NAME = 'tp_account' 
  AND INDEX_NAME = 'uk_username';

SET @sql_username_index = IF(@index_exists_username = 0, 
    'CREATE UNIQUE INDEX uk_username ON tp_account(USERNAME)',
    'SELECT "Index uk_username already exists"');

PREPARE stmt2 FROM @sql_username_index;
EXECUTE stmt2;
DEALLOCATE PREPARE stmt2;

-- 6. 为人员部门关系表创建复合索引（如果不存在）
SELECT COUNT(1) INTO @index_exists_person_dept 
FROM INFORMATION_SCHEMA.STATISTICS 
WHERE TABLE_SCHEMA = 'ps-bmp' 
  AND TABLE_NAME = 'tp_person_dept' 
  AND INDEX_NAME = 'idx_person_dept';

SET @sql_person_dept_index = IF(@index_exists_person_dept = 0, 
    'CREATE INDEX idx_person_dept ON tp_person_dept(PERSON_ID, DEPT_ID)',
    'SELECT "Index idx_person_dept already exists"');

PREPARE stmt3 FROM @sql_person_dept_index;
EXECUTE stmt3;
DEALLOCATE PREPARE stmt3;

-- 执行完成提示
SELECT 'Database schema update completed successfully!' AS result;
