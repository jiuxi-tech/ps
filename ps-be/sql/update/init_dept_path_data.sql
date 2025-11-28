-- ===================================================================
-- 用户导入导出功能重构:初始化部门路径数据(修复版v2)
-- 创建时间: 2025-11-27
-- 修复时间: 2025-11-27
-- 描述: 使用迭代方式按层级顺序计算部门路径,避免递归深度问题
-- ===================================================================

-- 1. 先清空现有的路径数据
UPDATE tp_dept_basicinfo 
SET 
    FULL_DEPT_NAME = NULL,
    FULL_DEPT_CODE = NULL
WHERE ACTIVED = 1;

-- 2. 更新根部门(层级为1或无父部门)
-- 注意：排除1111111111111111111虹拟节点，它不应该有FULL_DEPT_NAME
UPDATE tp_dept_basicinfo 
SET 
    FULL_DEPT_NAME = DEPT_FULL_NAME,
    FULL_DEPT_CODE = IFNULL(DEPT_NO, '')
WHERE ACTIVED = 1
  AND DEPT_ID != '1111111111111111111'
  AND (PDEPT_ID IS NULL OR PDEPT_ID = '' OR PDEPT_ID = '101' OR PDEPT_ID = '1111111111111111111'
       OR DEPT_LEVELCODE LIKE '___');

-- 3. 逐层更新子部门(层级2)
UPDATE tp_dept_basicinfo child
INNER JOIN tp_dept_basicinfo parent ON child.PDEPT_ID = parent.DEPT_ID
SET 
    child.FULL_DEPT_NAME = CONCAT(parent.FULL_DEPT_NAME, '>', child.DEPT_FULL_NAME),
    child.FULL_DEPT_CODE = CONCAT(
        IFNULL(parent.FULL_DEPT_CODE, ''),
        IF(child.DEPT_NO IS NOT NULL AND child.DEPT_NO != '', CONCAT('>', child.DEPT_NO), '')
    )
WHERE child.ACTIVED = 1
  AND parent.ACTIVED = 1
  AND child.FULL_DEPT_NAME IS NULL
  AND parent.FULL_DEPT_NAME IS NOT NULL
  AND child.DEPT_LEVELCODE LIKE '______';

-- 4. 逐层更新子部门(层级3)
UPDATE tp_dept_basicinfo child
INNER JOIN tp_dept_basicinfo parent ON child.PDEPT_ID = parent.DEPT_ID
SET 
    child.FULL_DEPT_NAME = CONCAT(parent.FULL_DEPT_NAME, '>', child.DEPT_FULL_NAME),
    child.FULL_DEPT_CODE = CONCAT(
        IFNULL(parent.FULL_DEPT_CODE, ''),
        IF(child.DEPT_NO IS NOT NULL AND child.DEPT_NO != '', CONCAT('>', child.DEPT_NO), '')
    )
WHERE child.ACTIVED = 1
  AND parent.ACTIVED = 1
  AND child.FULL_DEPT_NAME IS NULL
  AND parent.FULL_DEPT_NAME IS NOT NULL
  AND child.DEPT_LEVELCODE LIKE '_________';

-- 5. 逐层更新子部门(层级4)
UPDATE tp_dept_basicinfo child
INNER JOIN tp_dept_basicinfo parent ON child.PDEPT_ID = parent.DEPT_ID
SET 
    child.FULL_DEPT_NAME = CONCAT(parent.FULL_DEPT_NAME, '>', child.DEPT_FULL_NAME),
    child.FULL_DEPT_CODE = CONCAT(
        IFNULL(parent.FULL_DEPT_CODE, ''),
        IF(child.DEPT_NO IS NOT NULL AND child.DEPT_NO != '', CONCAT('>', child.DEPT_NO), '')
    )
WHERE child.ACTIVED = 1
  AND parent.ACTIVED = 1
  AND child.FULL_DEPT_NAME IS NULL
  AND parent.FULL_DEPT_NAME IS NOT NULL
  AND child.DEPT_LEVELCODE LIKE '____________';

-- 6. 逐层更新子部门(层级5)
UPDATE tp_dept_basicinfo child
INNER JOIN tp_dept_basicinfo parent ON child.PDEPT_ID = parent.DEPT_ID
SET 
    child.FULL_DEPT_NAME = CONCAT(parent.FULL_DEPT_NAME, '>', child.DEPT_FULL_NAME),
    child.FULL_DEPT_CODE = CONCAT(
        IFNULL(parent.FULL_DEPT_CODE, ''),
        IF(child.DEPT_NO IS NOT NULL AND child.DEPT_NO != '', CONCAT('>', child.DEPT_NO), '')
    )
WHERE child.ACTIVED = 1
  AND parent.ACTIVED = 1
  AND child.FULL_DEPT_NAME IS NULL
  AND parent.FULL_DEPT_NAME IS NOT NULL
  AND child.DEPT_LEVELCODE LIKE '_______________';

-- 7. 逐层更新子部门(层级6及以上 - 兜底处理)
UPDATE tp_dept_basicinfo child
INNER JOIN tp_dept_basicinfo parent ON child.PDEPT_ID = parent.DEPT_ID
SET 
    child.FULL_DEPT_NAME = CONCAT(parent.FULL_DEPT_NAME, '>', child.DEPT_FULL_NAME),
    child.FULL_DEPT_CODE = CONCAT(
        IFNULL(parent.FULL_DEPT_CODE, ''),
        IF(child.DEPT_NO IS NOT NULL AND child.DEPT_NO != '', CONCAT('>', child.DEPT_NO), '')
    )
WHERE child.ACTIVED = 1
  AND parent.ACTIVED = 1
  AND child.FULL_DEPT_NAME IS NULL
  AND parent.FULL_DEPT_NAME IS NOT NULL;

-- 8. 再次执行兜底处理,确保深层级部门也能更新(最多7层)
UPDATE tp_dept_basicinfo child
INNER JOIN tp_dept_basicinfo parent ON child.PDEPT_ID = parent.DEPT_ID
SET 
    child.FULL_DEPT_NAME = CONCAT(parent.FULL_DEPT_NAME, '>', child.DEPT_FULL_NAME),
    child.FULL_DEPT_CODE = CONCAT(
        IFNULL(parent.FULL_DEPT_CODE, ''),
        IF(child.DEPT_NO IS NOT NULL AND child.DEPT_NO != '', CONCAT('>', child.DEPT_NO), '')
    )
WHERE child.ACTIVED = 1
  AND parent.ACTIVED = 1
  AND child.FULL_DEPT_NAME IS NULL
  AND parent.FULL_DEPT_NAME IS NOT NULL;

-- 9. 显示更新完成消息
SELECT '部门路径初始化完成!' AS result;

-- 10. 验证更新结果
SELECT 
    DEPT_ID,
    DEPT_FULL_NAME,
    DEPT_NO,
    FULL_DEPT_NAME,
    FULL_DEPT_CODE,
    PDEPT_ID,
    LENGTH(DEPT_LEVELCODE) / 3 AS dept_level
FROM tp_dept_basicinfo 
WHERE ACTIVED = 1
ORDER BY DEPT_LEVELCODE
LIMIT 20;

-- 11. 统计更新情况
SELECT 
    '总部门数' AS item,
    COUNT(*) AS count
FROM tp_dept_basicinfo 
WHERE ACTIVED = 1

UNION ALL

SELECT 
    '已更新路径的部门数' AS item,
    COUNT(*) AS count
FROM tp_dept_basicinfo 
WHERE ACTIVED = 1 
  AND FULL_DEPT_NAME IS NOT NULL 
  AND FULL_DEPT_NAME != ''

UNION ALL

SELECT 
    '未更新路径的部门数(需检查)' AS item,
    COUNT(*) AS count
FROM tp_dept_basicinfo 
WHERE ACTIVED = 1 
  AND (FULL_DEPT_NAME IS NULL OR FULL_DEPT_NAME = '')

UNION ALL

SELECT 
    '根部门数(无父部门)' AS item,
    COUNT(*) AS count
FROM tp_dept_basicinfo 
WHERE ACTIVED = 1 
  AND (PDEPT_ID IS NULL OR PDEPT_ID = '' OR PDEPT_ID = '101');

-- 12. 显示未更新的部门(如果存在)
SELECT 
    DEPT_ID,
    DEPT_FULL_NAME,
    PDEPT_ID,
    DEPT_LEVELCODE,
    '请检查父部门ID是否有效' AS note
FROM tp_dept_basicinfo 
WHERE ACTIVED = 1 
  AND (FULL_DEPT_NAME IS NULL OR FULL_DEPT_NAME = '')
LIMIT 10;
