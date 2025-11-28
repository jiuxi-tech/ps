-- 执行清理操作
-- 步骤1: 验证备份
SELECT '步骤1: 验证备份' as 当前步骤;
SELECT COUNT(*) as 备份记录数 FROM tp_dept_basicinfo_deleted_backup;

-- 步骤2: 删除基础信息表中的逻辑删除记录
SELECT '步骤2: 开始删除基础信息表记录' as 当前步骤;
DELETE FROM tp_dept_basicinfo 
WHERE ACTIVED = 0
AND NOT EXISTS (
    SELECT 1 FROM tp_dept_basicinfo child
    WHERE child.PDEPT_ID = tp_dept_basicinfo.DEPT_ID
    AND child.ACTIVED = 1
)
AND NOT EXISTS (
    SELECT 1 FROM tp_person_dept pd
    WHERE pd.DEPT_ID = tp_dept_basicinfo.DEPT_ID
)
AND NOT EXISTS (
    SELECT 1 FROM tp_person_tag pt
    WHERE pt.DEPT_ID = tp_dept_basicinfo.DEPT_ID
);

-- 步骤3: 验证删除结果
SELECT '步骤3: 验证删除结果' as 当前步骤;
SELECT COUNT(*) as 剩余逻辑删除记录 FROM tp_dept_basicinfo WHERE ACTIVED = 0;

-- 步骤4: 清理扩展信息表中的孤立数据
SELECT '步骤4: 清理扩展信息表' as 当前步骤;
DELETE FROM tp_dept_exinfo
WHERE NOT EXISTS (
    SELECT 1 FROM tp_dept_basicinfo b
    WHERE b.DEPT_ID = tp_dept_exinfo.DEPT_ID
);

-- 步骤5: 最终统计
SELECT '步骤5: 最终统计' as 当前步骤;
SELECT 
    (SELECT COUNT(*) FROM tp_dept_basicinfo_deleted_backup) as 已备份数量,
    (SELECT COUNT(*) FROM tp_dept_basicinfo WHERE ACTIVED = 1) as 当前激活部门数,
    (SELECT COUNT(*) FROM tp_dept_basicinfo WHERE ACTIVED = 0) as 剩余逻辑删除数;

SELECT '清理完成！' as 状态;
