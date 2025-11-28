-- ===================================================================
-- 人员表职务职级字段添加
-- 创建时间: 2025-11-28
-- 描述: 为人员扩展信息表添加职务职级字段，区分职位(POSITION)和职务职级(RANK)
-- ===================================================================

START TRANSACTION;

-- 1. 为人员扩展信息表添加职务职级字段
ALTER TABLE tp_person_exinfo 
ADD COLUMN RANK VARCHAR(50) DEFAULT NULL COMMENT '职务职级（如：四级调研员、二级主任科员等）';

-- 2. 数据迁移说明
-- OFFICE字段(tp_person_basicinfo) - 原先存储的是职位/职务，将继续保留
-- POSITION字段(tp_person_exinfo) - 原先存储的是职务，将继续保留
-- RANK字段(tp_person_exinfo) - 新增，专门存储职务职级

-- 3. 字段用途说明
-- - OFFICE (tp_person_basicinfo.OFFICE): 职位（保留原有数据和用途）
-- - POSITION (tp_person_exinfo.POSITION): 职务（保留原有数据和用途）
-- - RANK (tp_person_exinfo.RANK): 职务职级（新字段，如"四级调研员"）
-- - TITLE_CODE (tp_person_exinfo.TITLE_CODE): 职称编码（对应字典SYS12，如"SYS1206"代表"副教授"）

-- 4. 验证字段添加结果
SELECT 
    COLUMN_NAME as 字段名,
    COLUMN_TYPE as 类型,
    IS_NULLABLE as 可为空,
    COLUMN_DEFAULT as 默认值,
    COLUMN_COMMENT as 注释
FROM INFORMATION_SCHEMA.COLUMNS
WHERE TABLE_SCHEMA = 'ps-bmp'
  AND TABLE_NAME = 'tp_person_exinfo'
  AND COLUMN_NAME IN ('POSITION', 'RANK', 'TITLE_CODE')
ORDER BY ORDINAL_POSITION;

COMMIT;

-- ===================================================================
-- 执行说明：
-- 1. 此脚本添加了新的RANK字段用于存储职务职级
-- 2. 原有OFFICE、POSITION字段保持不变
-- 3. 导入导出功能将更新以使用RANK字段存储职务职级
-- 4. 职称TITLE_CODE将在导出时转换为字典中的名称
-- ===================================================================
