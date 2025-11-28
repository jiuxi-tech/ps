-- 检查超级管理员部门节点
SELECT 
    DEPT_ID as 部门ID,
    DEPT_FULL_NAME as 部门名称,
    DEPT_LEVELCODE as 层级编码,
    PDEPT_ID as 父部门ID,
    ACTIVED as 是否激活,
    CATEGORY as 类别
FROM tp_dept_basicinfo 
WHERE DEPT_ID = '1111111111111111111';

-- 检查是否有子部门
SELECT 
    COUNT(*) as 子部门数量
FROM tp_dept_basicinfo 
WHERE PDEPT_ID = '1111111111111111111' AND ACTIVED = 1;

-- 列出子部门
SELECT 
    DEPT_ID as 部门ID,
    DEPT_FULL_NAME as 部门名称,
    PDEPT_ID as 父部门ID
FROM tp_dept_basicinfo 
WHERE PDEPT_ID = '1111111111111111111' AND ACTIVED = 1
LIMIT 10;
