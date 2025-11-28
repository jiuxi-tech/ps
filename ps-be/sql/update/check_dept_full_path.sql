-- 检查部门全路径是否包含"顶级部门节点"
SELECT 
    DEPT_ID as 部门ID,
    DEPT_FULL_NAME as 部门名称,
    FULL_DEPT_NAME as 部门全路径,
    DEPT_LEVELCODE as 层级编码
FROM tp_dept_basicinfo 
WHERE ACTIVED = 1 
  AND DEPT_ID != '1111111111111111111'
ORDER BY LENGTH(DEPT_LEVELCODE), ORDER_INDEX
LIMIT 15;

-- 检查是否有全路径包含"顶级部门节点"的记录
SELECT 
    COUNT(*) as 包含顶级节点的部门数
FROM tp_dept_basicinfo 
WHERE ACTIVED = 1 
  AND FULL_DEPT_NAME LIKE '%顶级部门节点%';

-- 示例：查看包含"顶级部门节点"的具体记录
SELECT 
    DEPT_ID,
    DEPT_FULL_NAME,
    FULL_DEPT_NAME as 部门全路径
FROM tp_dept_basicinfo 
WHERE ACTIVED = 1 
  AND FULL_DEPT_NAME LIKE '%顶级部门节点%'
LIMIT 10;
