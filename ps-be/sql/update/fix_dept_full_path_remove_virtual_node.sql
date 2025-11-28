-- ===================================================================
-- 修复部门全路径：移除"顶级部门节点"前缀
-- 创建时间: 2025-01-27
-- 描述: 清除FULL_DEPT_NAME中包含虚拟节点"顶级部门节点>"的前缀
-- ===================================================================

-- 步骤1: 查看当前受影响的记录
SELECT '步骤1: 当前受影响的部门' as 说明;
SELECT 
    DEPT_ID,
    DEPT_FULL_NAME,
    FULL_DEPT_NAME as 修复前全路径,
    PDEPT_ID
FROM tp_dept_basicinfo 
WHERE ACTIVED = 1 
  AND FULL_DEPT_NAME LIKE '顶级部门节点>%';

-- 步骤2: 移除虚拟顶级节点本身的FULL_DEPT_NAME
SELECT '步骤2: 清除虚拟节点自身的全路径' as 说明;
UPDATE tp_dept_basicinfo 
SET FULL_DEPT_NAME = NULL
WHERE DEPT_ID = '1111111111111111111';

-- 步骤3: 修复直接子节点（父节点为1111111111111111111的真实顶级机构）
SELECT '步骤3: 修复真实顶级机构' as 说明;
UPDATE tp_dept_basicinfo 
SET FULL_DEPT_NAME = DEPT_FULL_NAME
WHERE PDEPT_ID = '1111111111111111111' 
  AND ACTIVED = 1
  AND DEPT_ID != '1111111111111111111';

-- 步骤4: 递归更新所有子部门的全路径（层级2）
SELECT '步骤4: 更新层级2部门' as 说明;
UPDATE tp_dept_basicinfo child
INNER JOIN tp_dept_basicinfo parent ON child.PDEPT_ID = parent.DEPT_ID
SET 
    child.FULL_DEPT_NAME = CONCAT(parent.FULL_DEPT_NAME, '>', child.DEPT_FULL_NAME)
WHERE child.ACTIVED = 1
  AND parent.ACTIVED = 1
  AND parent.PDEPT_ID = '1111111111111111111'
  AND parent.DEPT_ID != '1111111111111111111'
  AND child.DEPT_LEVELCODE LIKE '______';

-- 步骤5: 更新层级3部门
SELECT '步骤5: 更新层级3部门' as 说明;
UPDATE tp_dept_basicinfo child
INNER JOIN tp_dept_basicinfo parent ON child.PDEPT_ID = parent.DEPT_ID
SET 
    child.FULL_DEPT_NAME = CONCAT(parent.FULL_DEPT_NAME, '>', child.DEPT_FULL_NAME)
WHERE child.ACTIVED = 1
  AND parent.ACTIVED = 1
  AND child.FULL_DEPT_NAME LIKE '顶级部门节点>%'
  AND child.DEPT_LEVELCODE LIKE '_________';

-- 步骤6: 更新层级4及以上（兜底处理）
SELECT '步骤6: 更新剩余层级部门（兜底）' as 说明;
UPDATE tp_dept_basicinfo child
INNER JOIN tp_dept_basicinfo parent ON child.PDEPT_ID = parent.DEPT_ID
SET 
    child.FULL_DEPT_NAME = CONCAT(parent.FULL_DEPT_NAME, '>', child.DEPT_FULL_NAME)
WHERE child.ACTIVED = 1
  AND parent.ACTIVED = 1
  AND child.FULL_DEPT_NAME LIKE '顶级部门节点>%'
  AND parent.FULL_DEPT_NAME NOT LIKE '顶级部门节点>%';

-- 重复执行以确保深层级都更新
UPDATE tp_dept_basicinfo child
INNER JOIN tp_dept_basicinfo parent ON child.PDEPT_ID = parent.DEPT_ID
SET 
    child.FULL_DEPT_NAME = CONCAT(parent.FULL_DEPT_NAME, '>', child.DEPT_FULL_NAME)
WHERE child.ACTIVED = 1
  AND parent.ACTIVED = 1
  AND child.FULL_DEPT_NAME LIKE '顶级部门节点>%'
  AND parent.FULL_DEPT_NAME NOT LIKE '顶级部门节点>%';

-- 最后一次兜底
UPDATE tp_dept_basicinfo child
INNER JOIN tp_dept_basicinfo parent ON child.PDEPT_ID = parent.DEPT_ID
SET 
    child.FULL_DEPT_NAME = CONCAT(parent.FULL_DEPT_NAME, '>', child.DEPT_FULL_NAME)
WHERE child.ACTIVED = 1
  AND parent.ACTIVED = 1
  AND child.FULL_DEPT_NAME LIKE '顶级部门节点>%'
  AND parent.FULL_DEPT_NAME NOT LIKE '顶级部门节点>%';

-- 步骤7: 验证修复结果
SELECT '步骤7: 验证修复结果' as 说明;

-- 检查是否还有包含"顶级部门节点"的记录
SELECT 
    COUNT(*) as 剩余问题记录数
FROM tp_dept_basicinfo 
WHERE ACTIVED = 1 
  AND DEPT_ID != '1111111111111111111'
  AND FULL_DEPT_NAME LIKE '%顶级部门节点%';

-- 显示修复后的顶级机构
SELECT 
    DEPT_ID,
    DEPT_FULL_NAME,
    FULL_DEPT_NAME as 修复后全路径,
    PDEPT_ID
FROM tp_dept_basicinfo 
WHERE ACTIVED = 1 
  AND PDEPT_ID = '1111111111111111111'
ORDER BY ORDER_INDEX;

-- 显示前15条记录
SELECT 
    DEPT_ID,
    DEPT_FULL_NAME,
    FULL_DEPT_NAME as 修复后全路径,
    LENGTH(DEPT_LEVELCODE) / 3 as 层级
FROM tp_dept_basicinfo 
WHERE ACTIVED = 1 
  AND DEPT_ID != '1111111111111111111'
ORDER BY DEPT_LEVELCODE
LIMIT 15;

SELECT '✅ 修复完成！' as 结果;
