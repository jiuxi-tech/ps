-- 快速分析逻辑删除的部门数据

-- 1. 统计逻辑删除的部门总数
SELECT '1. 逻辑删除部门总数' as 分析项, COUNT(*) as 数量
FROM tp_dept_basicinfo 
WHERE ACTIVED = 0;

-- 2. 检查有子部门引用的逻辑删除部门
SELECT '2. 有子部门引用的部门' as 分析项, COUNT(DISTINCT parent.DEPT_ID) as 数量
FROM tp_dept_basicinfo parent
WHERE parent.ACTIVED = 0
AND EXISTS (
    SELECT 1 FROM tp_dept_basicinfo child
    WHERE child.PDEPT_ID = parent.DEPT_ID
    AND child.ACTIVED = 1
);

-- 3. 检查被人员部门表引用的逻辑删除部门
SELECT '3. 被人员关系引用的部门' as 分析项, COUNT(DISTINCT dept.DEPT_ID) as 数量
FROM tp_dept_basicinfo dept
WHERE dept.ACTIVED = 0
AND EXISTS (
    SELECT 1 FROM tp_person_dept pd
    WHERE pd.DEPT_ID = dept.DEPT_ID
);

-- 4. 检查被人员标签表引用的逻辑删除部门
SELECT '4. 被标签关系引用的部门' as 分析项, COUNT(DISTINCT dept.DEPT_ID) as 数量
FROM tp_dept_basicinfo dept
WHERE dept.ACTIVED = 0
AND EXISTS (
    SELECT 1 FROM tp_person_tag pt
    WHERE pt.DEPT_ID = dept.DEPT_ID
);

-- 5. 统计可以安全删除的部门数量
SELECT '5. 可以安全删除的部门' as 分析项, COUNT(*) as 数量
FROM tp_dept_basicinfo dept
WHERE dept.ACTIVED = 0
AND NOT EXISTS (
    SELECT 1 FROM tp_dept_basicinfo child
    WHERE child.PDEPT_ID = dept.DEPT_ID AND child.ACTIVED = 1
)
AND NOT EXISTS (
    SELECT 1 FROM tp_person_dept pd WHERE pd.DEPT_ID = dept.DEPT_ID
)
AND NOT EXISTS (
    SELECT 1 FROM tp_person_tag pt WHERE pt.DEPT_ID = dept.DEPT_ID
);

-- 6. 列出可以安全删除的部门详情（前10条）
SELECT 
    dept.DEPT_ID as 部门ID,
    dept.DEPT_FULL_NAME as 部门名称,
    dept.DEPT_LEVELCODE as 层级编码,
    dept.UPDATE_TIME as 删除时间
FROM tp_dept_basicinfo dept
WHERE dept.ACTIVED = 0
AND NOT EXISTS (
    SELECT 1 FROM tp_dept_basicinfo child
    WHERE child.PDEPT_ID = dept.DEPT_ID AND child.ACTIVED = 1
)
AND NOT EXISTS (
    SELECT 1 FROM tp_person_dept pd WHERE pd.DEPT_ID = dept.DEPT_ID
)
AND NOT EXISTS (
    SELECT 1 FROM tp_person_tag pt WHERE pt.DEPT_ID = dept.DEPT_ID
)
ORDER BY dept.UPDATE_TIME DESC
LIMIT 10;
