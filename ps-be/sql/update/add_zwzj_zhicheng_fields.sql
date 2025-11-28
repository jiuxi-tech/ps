-- ===================================================================
-- 人员表新增职务职级和职称字段
-- 创建时间: 2025-11-28
-- 描述: 添加明确的职务职级(ZWZJ)和职称(ZHICHENG)字段，替代原有混乱的字段
-- ===================================================================

START TRANSACTION;

-- 1. 为人员扩展信息表添加职务职级和职称字段
ALTER TABLE tp_person_exinfo 
ADD COLUMN ZWZJ VARCHAR(100) DEFAULT NULL COMMENT '职务职级（如：四级调研员、科长等）',
ADD COLUMN ZHICHENG VARCHAR(100) DEFAULT NULL COMMENT '职称（如：教授、副教授、工程师等）';

-- 2. 数据迁移：将原有数据迁移到新字段
-- 迁移职务职级数据：优先使用RANK，其次使用POSITION
UPDATE tp_person_exinfo 
SET ZWZJ = COALESCE(RANK, POSITION)
WHERE ZWZJ IS NULL AND (RANK IS NOT NULL OR POSITION IS NOT NULL);

-- 迁移职称数据：使用TITLE_CODE
UPDATE tp_person_exinfo 
SET ZHICHENG = TITLE_CODE
WHERE ZHICHENG IS NULL AND TITLE_CODE IS NOT NULL;

-- 3. 字段说明
-- 新字段（推荐使用）：
--   - ZWZJ: 职务职级，如"四级调研员"、"科长"、"处长"等
--   - ZHICHENG: 职称，如"教授"、"副教授"、"高级工程师"等
--
-- 旧字段（已弃用，保留仅为兼容）：
--   - OFFICE (tp_person_basicinfo.OFFICE): 职位（已废弃）
--   - POSITION (tp_person_exinfo.POSITION): 职务（已废弃）
--   - RANK (tp_person_exinfo.RANK): 职务职级（已废弃）
--   - TITLE_CODE (tp_person_exinfo.TITLE_CODE): 职称编码/文本（已废弃）

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
  AND COLUMN_NAME IN ('ZWZJ', 'ZHICHENG', 'POSITION', 'RANK', 'TITLE_CODE')
ORDER BY ORDINAL_POSITION;

-- 5. 验证数据迁移结果
SELECT 
    COUNT(*) as 总记录数,
    COUNT(ZWZJ) as 职务职级有值数,
    COUNT(ZHICHENG) as 职称有值数,
    COUNT(RANK) as 原RANK有值数,
    COUNT(POSITION) as 原POSITION有值数,
    COUNT(TITLE_CODE) as 原TITLE_CODE有值数
FROM tp_person_exinfo;

COMMIT;

-- ===================================================================
-- 执行说明：
-- 1. 此脚本添加了ZWZJ和ZHICHENG两个新字段
-- 2. 自动将历史数据从旧字段迁移到新字段
-- 3. 旧字段保留但不再使用，仅为数据兼容
-- 4. 未来所有开发应使用ZWZJ和ZHICHENG字段
-- ===================================================================
