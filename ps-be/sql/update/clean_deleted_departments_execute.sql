-- ========================================
-- 快速清理逻辑删除部门数据 - 执行脚本
-- 创建时间: 2025-01-27
-- 警告: 此脚本会直接执行删除操作，仅供有经验的DBA使用
-- ========================================

-- 使用前请确认：
-- 1. ✅ 已完整备份数据库
-- 2. ✅ 已在测试环境验证过
-- 3. ✅ 已获得业务部门审批
-- 4. ✅ 当前处于业务低峰期

-- ========================================
-- 第一步：创建备份表
-- ========================================

-- 备份即将删除的数据
DROP TABLE IF EXISTS tp_dept_basicinfo_deleted_backup;

CREATE TABLE tp_dept_basicinfo_deleted_backup AS
SELECT * FROM tp_dept_basicinfo
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

-- 验证备份
SELECT 
    '备份记录数' as 统计项,
    COUNT(*) as 数量,
    NOW() as 备份时间
FROM tp_dept_basicinfo_deleted_backup;

-- ========================================
-- 第二步：执行物理删除
-- ========================================

-- 开始事务（建议手动控制提交）
START TRANSACTION;

-- 执行删除
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

-- 查看删除影响行数
SELECT ROW_COUNT() as 删除记录数;

-- ⚠️ 验证删除结果后再提交
-- 如果结果正确，执行: COMMIT;
-- 如果需要回滚，执行: ROLLBACK;

-- 暂不自动提交，等待人工确认
-- COMMIT;

-- ========================================
-- 第三步：验证删除结果
-- ========================================

-- 验证剩余的逻辑删除部门
SELECT 
    '剩余逻辑删除部门' as 统计项,
    COUNT(*) as 数量,
    '这些部门有引用关系' as 说明
FROM tp_dept_basicinfo 
WHERE ACTIVED = 0;

-- 验证删除的部门在备份表中
SELECT 
    '备份表记录数' as 统计项,
    COUNT(*) as 数量
FROM tp_dept_basicinfo_deleted_backup;

-- ========================================
-- 第四步：清理孤立的部门扩展信息（可选）
-- ========================================

-- 查找孤立的扩展信息
SELECT 
    '孤立的部门扩展信息' as 统计项,
    COUNT(*) as 数量
FROM tp_dept_exinfo ex
WHERE NOT EXISTS (
    SELECT 1 FROM tp_dept_basicinfo dept
    WHERE dept.DEPT_ID = ex.DEPT_ID
);

-- 删除孤立的扩展信息（取消注释后执行）
-- DELETE FROM tp_dept_exinfo
-- WHERE NOT EXISTS (
--     SELECT 1 FROM tp_dept_basicinfo dept
--     WHERE dept.DEPT_ID = tp_dept_exinfo.DEPT_ID
-- );

-- ========================================
-- 第五步：优化表（可选）
-- ========================================

-- 重建索引和统计信息
-- ANALYZE TABLE tp_dept_basicinfo;

-- ========================================
-- 回滚脚本（如果需要恢复数据）
-- ========================================

-- 从备份表恢复数据
-- INSERT INTO tp_dept_basicinfo 
-- SELECT * FROM tp_dept_basicinfo_deleted_backup;

-- 验证恢复
-- SELECT COUNT(*) FROM tp_dept_basicinfo WHERE ACTIVED = 0;

-- ========================================
-- 执行记录
-- ========================================

/*
执行时间：__________
执行人：__________
环境：生产/测试

删除前统计：
- 逻辑删除部门总数：__________
- 可安全删除数量：__________

执行结果：
- 实际删除数量：__________
- 备份记录数量：__________
- 剩余逻辑删除：__________

验证结果：
□ 部门树加载正常
□ 人员管理功能正常
□ 数据一致性检查通过

备注：
__________
*/
