-- 检查1111111111111111111节点信息
SELECT 
    DEPT_ID as 部门ID,
    DEPT_FULL_NAME as 部门名称,
    DEPT_LEVELCODE as 层级编码,
    PDEPT_ID as 父部门ID,
    ACTIVED as 是否激活
FROM tp_dept_basicinfo 
WHERE DEPT_ID = '1111111111111111111';

-- 检查以1111111111111111111为父节点的直接子机构
SELECT 
    DEPT_ID as 部门ID,
    DEPT_FULL_NAME as 部门名称,
    DEPT_TYPE as 部门类型,
    DEPT_LEVELCODE as 层级编码,
    PDEPT_ID as 父部门ID,
    ACTIVED as 是否激活,
    ORDER_INDEX as 排序
FROM tp_dept_basicinfo 
WHERE PDEPT_ID = '1111111111111111111' 
ORDER BY ORDER_INDEX;

-- 检查所有激活的顶级机构（层级编码最短的）
SELECT 
    DEPT_ID as 部门ID,
    DEPT_FULL_NAME as 部门名称,
    DEPT_TYPE as 部门类型,
    DEPT_LEVELCODE as 层级编码,
    PDEPT_ID as 父部门ID,
    LENGTH(DEPT_LEVELCODE) as 层级长度
FROM tp_dept_basicinfo 
WHERE ACTIVED = 1 
  AND CATEGORY = 0
  AND DEPT_ID != '1111111111111111111'
ORDER BY LENGTH(DEPT_LEVELCODE), ORDER_INDEX
LIMIT 20;
