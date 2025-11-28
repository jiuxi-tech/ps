-- 清理孤立的人员部门关系数据
-- 这些是人员基础信息已被删除，但关联表中仍有残留的记录

-- 步骤1: 统计孤立数据
SELECT '步骤1: 统计孤立的人员部门关系' as 当前步骤;
SELECT COUNT(*) as 孤立记录数
FROM tp_person_dept pd
WHERE NOT EXISTS (
    SELECT 1 FROM tp_person_basicinfo p
    WHERE p.PERSON_ID = pd.PERSON_ID
);

-- 步骤2: 查看孤立数据详情（前10条）
SELECT '步骤2: 孤立数据详情' as 当前步骤;
SELECT 
    pd.PERSON_ID,
    pd.DEPT_ID,
    d.DEPT_FULL_NAME as 部门名称,
    pd.DEFAULT_DEPT as 是否默认部门
FROM tp_person_dept pd
LEFT JOIN tp_dept_basicinfo d ON pd.DEPT_ID = d.DEPT_ID
WHERE NOT EXISTS (
    SELECT 1 FROM tp_person_basicinfo p
    WHERE p.PERSON_ID = pd.PERSON_ID
)
LIMIT 10;

-- 步骤3: 删除孤立的人员部门关系
SELECT '步骤3: 删除孤立的人员部门关系' as 当前步骤;
DELETE FROM tp_person_dept
WHERE NOT EXISTS (
    SELECT 1 FROM tp_person_basicinfo p
    WHERE p.PERSON_ID = tp_person_dept.PERSON_ID
);

-- 步骤4: 统计孤立的人员标签数据
SELECT '步骤4: 统计孤立的人员标签关系' as 当前步骤;
SELECT COUNT(*) as 孤立标签记录数
FROM tp_person_tag pt
WHERE NOT EXISTS (
    SELECT 1 FROM tp_person_basicinfo p
    WHERE p.PERSON_ID = pt.PERSON_ID
);

-- 步骤5: 删除孤立的人员标签关系
SELECT '步骤5: 删除孤立的人员标签关系' as 当前步骤;
DELETE FROM tp_person_tag
WHERE NOT EXISTS (
    SELECT 1 FROM tp_person_basicinfo p
    WHERE p.PERSON_ID = tp_person_tag.PERSON_ID
);

-- 步骤6: 验证清理结果
SELECT '步骤6: 验证清理结果' as 当前步骤;
SELECT 
    (SELECT COUNT(*) FROM tp_person_dept pd WHERE NOT EXISTS (SELECT 1 FROM tp_person_basicinfo p WHERE p.PERSON_ID = pd.PERSON_ID)) as 剩余孤立部门关系,
    (SELECT COUNT(*) FROM tp_person_tag pt WHERE NOT EXISTS (SELECT 1 FROM tp_person_basicinfo p WHERE p.PERSON_ID = pt.PERSON_ID)) as 剩余孤立标签关系;

SELECT '清理完成！' as 状态;
