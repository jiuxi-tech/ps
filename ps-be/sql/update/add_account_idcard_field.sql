-- ============================================================================
-- 账号多因子登录扩展 - 数据库变更脚本
-- 功能: 添加账号表IDCARD字段,支持身份证号登录
-- 作者: Qoder AI
-- 日期: 2024-12-15
-- ============================================================================

-- ============================================================================
-- 第一步: DDL变更 - 添加IDCARD字段
-- ============================================================================

-- 为tp_account表添加IDCARD字段
ALTER TABLE `tp_account` ADD COLUMN `IDCARD` VARCHAR(18) NULL COMMENT '身份证号,用于登录凭据' AFTER `PHONE`;

-- ============================================================================
-- 第二步: 数据迁移 - 从人员表同步数据到账号表
-- ============================================================================

-- 数据迁移: 从tp_person_basicinfo同步手机号和身份证号到tp_account
-- 注意: 手机号已加密存储,直接复制加密后的值
UPDATE tp_account a
INNER JOIN tp_person_basicinfo p ON a.PERSON_ID = p.PERSON_ID
SET 
    a.PHONE = p.PHONE,
    a.IDCARD = p.IDCARD,
    a.UPDATE_TIME = DATE_FORMAT(NOW(), '%Y%m%d%H%i%s')
WHERE a.ACTIVED = 1 AND p.ACTIVED = 1;

-- ============================================================================
-- 第三步: 数据清理 - 处理重复数据
-- ============================================================================

-- 3.1 检查并记录重复的手机号(仅在非空时检查)
SELECT 
    TENANT_ID,
    PHONE,
    COUNT(*) as duplicate_count,
    GROUP_CONCAT(ACCOUNT_ID ORDER BY UPDATE_TIME DESC) as account_ids
FROM tp_account
WHERE ACTIVED = 1 
  AND PHONE IS NOT NULL 
  AND PHONE != ''
GROUP BY TENANT_ID, PHONE
HAVING COUNT(*) > 1;

-- 3.2 检查并记录重复的身份证号(仅在非空时检查)
SELECT 
    TENANT_ID,
    IDCARD,
    COUNT(*) as duplicate_count,
    GROUP_CONCAT(ACCOUNT_ID ORDER BY UPDATE_TIME DESC) as account_ids
FROM tp_account
WHERE ACTIVED = 1 
  AND IDCARD IS NOT NULL 
  AND IDCARD != ''
GROUP BY TENANT_ID, IDCARD
HAVING COUNT(*) > 1;

-- ============================================================================
-- 第四步: 数据验证
-- ============================================================================

-- 4.1 验证数据迁移完整性
SELECT 
    '账号表总数' as check_item,
    COUNT(*) as count
FROM tp_account
WHERE ACTIVED = 1

UNION ALL

SELECT 
    '有人员ID的账号数' as check_item,
    COUNT(*) as count
FROM tp_account
WHERE ACTIVED = 1 AND PERSON_ID IS NOT NULL

UNION ALL

SELECT 
    '同步了手机号的账号数' as check_item,
    COUNT(*) as count
FROM tp_account
WHERE ACTIVED = 1 
  AND PERSON_ID IS NOT NULL 
  AND PHONE IS NOT NULL 
  AND PHONE != ''

UNION ALL

SELECT 
    '同步了身份证号的账号数' as check_item,
    COUNT(*) as count
FROM tp_account
WHERE ACTIVED = 1 
  AND PERSON_ID IS NOT NULL 
  AND IDCARD IS NOT NULL 
  AND IDCARD != '';

-- 4.2 验证租户内唯一性(应该没有重复记录)
SELECT 
    'tp_account手机号重复检查' as check_item,
    COUNT(*) as duplicate_groups
FROM (
    SELECT TENANT_ID, PHONE
    FROM tp_account
    WHERE ACTIVED = 1 
      AND PHONE IS NOT NULL 
      AND PHONE != ''
    GROUP BY TENANT_ID, PHONE
    HAVING COUNT(*) > 1
) as duplicates

UNION ALL

SELECT 
    'tp_account身份证号重复检查' as check_item,
    COUNT(*) as duplicate_groups
FROM (
    SELECT TENANT_ID, IDCARD
    FROM tp_account
    WHERE ACTIVED = 1 
      AND IDCARD IS NOT NULL 
      AND IDCARD != ''
    GROUP BY TENANT_ID, IDCARD
    HAVING COUNT(*) > 1
) as duplicates;

-- ============================================================================
-- 第五步: 添加索引(可选,根据实际性能需求决定)
-- ============================================================================

-- 为IDCARD字段添加索引以提升查询性能
-- 注意: 由于IDCARD可为NULL且可重复(不同租户可以相同),不能创建唯一索引
CREATE INDEX idx_account_idcard ON tp_account(IDCARD);

-- 为PHONE字段添加索引(如果尚未创建)
-- CREATE INDEX idx_account_phone ON tp_account(PHONE);

-- ============================================================================
-- 执行说明:
-- 1. 执行前请先备份tp_account和tp_person_basicinfo表
-- 2. 建议在测试环境先执行并验证
-- 3. 第三步会显示重复数据,需要人工处理后再继续
-- 4. 第四步验证数据迁移结果,确保数据完整性
-- 5. 第五步索引创建可以在业务低峰期执行
-- ============================================================================
