-- ========================================
-- 清理逻辑删除的部门数据
-- 创建时间: 2025-01-27
-- 说明: 安全地物理删除已逻辑删除且无任何引用的部门记录
-- ========================================

-- 步骤1: 分析逻辑删除的部门数据
-- ========================================

-- 1.1 查看所有逻辑删除的部门
SELECT 
    '逻辑删除的部门总数' as 描述,
    COUNT(*) as 数量
FROM tp_dept_basicinfo 
WHERE ACTIVED = 0;

-- 1.2 查看逻辑删除部门的详细信息
SELECT 
    DEPT_ID as 部门ID,
    DEPT_FULL_NAME as 部门全称,
    DEPT_LEVELCODE as 层级编码,
    PDEPT_ID as 父部门ID,
    CATEGORY as 类别,
    UPDATE_TIME as 删除时间
FROM tp_dept_basicinfo 
WHERE ACTIVED = 0
ORDER BY UPDATE_TIME DESC;

-- ========================================
-- 步骤2: 检查逻辑删除部门的引用情况
-- ========================================

-- 2.1 检查是否有子部门引用（作为父部门）
SELECT 
    '有子部门引用的逻辑删除部门' as 检查项,
    COUNT(DISTINCT parent.DEPT_ID) as 数量
FROM tp_dept_basicinfo parent
WHERE parent.ACTIVED = 0
AND EXISTS (
    SELECT 1 FROM tp_dept_basicinfo child
    WHERE child.PDEPT_ID = parent.DEPT_ID
    AND child.ACTIVED = 1
);

-- 2.2 列出有子部门引用的逻辑删除部门详情
SELECT 
    parent.DEPT_ID as 父部门ID,
    parent.DEPT_FULL_NAME as 父部门名称,
    COUNT(child.DEPT_ID) as 子部门数量,
    GROUP_CONCAT(child.DEPT_FULL_NAME SEPARATOR ', ') as 子部门列表
FROM tp_dept_basicinfo parent
INNER JOIN tp_dept_basicinfo child ON child.PDEPT_ID = parent.DEPT_ID
WHERE parent.ACTIVED = 0
AND child.ACTIVED = 1
GROUP BY parent.DEPT_ID, parent.DEPT_FULL_NAME;

-- 2.3 检查是否被人员部门关系表引用
SELECT 
    '被人员部门表引用的逻辑删除部门' as 检查项,
    COUNT(DISTINCT dept.DEPT_ID) as 数量
FROM tp_dept_basicinfo dept
WHERE dept.ACTIVED = 0
AND EXISTS (
    SELECT 1 FROM tp_person_dept pd
    WHERE pd.DEPT_ID = dept.DEPT_ID
);

-- 2.4 列出被人员部门关系表引用的部门详情
SELECT 
    dept.DEPT_ID as 部门ID,
    dept.DEPT_FULL_NAME as 部门名称,
    COUNT(pd.PERSON_ID) as 关联人员数
FROM tp_dept_basicinfo dept
INNER JOIN tp_person_dept pd ON pd.DEPT_ID = dept.DEPT_ID
WHERE dept.ACTIVED = 0
GROUP BY dept.DEPT_ID, dept.DEPT_FULL_NAME;

-- 2.5 检查是否被人员标签表引用
SELECT 
    '被人员标签表引用的逻辑删除部门' as 检查项,
    COUNT(DISTINCT dept.DEPT_ID) as 数量
FROM tp_dept_basicinfo dept
WHERE dept.ACTIVED = 0
AND EXISTS (
    SELECT 1 FROM tp_person_tag pt
    WHERE pt.DEPT_ID = dept.DEPT_ID
);

-- 2.6 列出被人员标签表引用的部门详情
SELECT 
    dept.DEPT_ID as 部门ID,
    dept.DEPT_FULL_NAME as 部门名称,
    COUNT(pt.PERSON_ID) as 关联标签数
FROM tp_dept_basicinfo dept
INNER JOIN tp_person_tag pt ON pt.DEPT_ID = dept.DEPT_ID
WHERE dept.ACTIVED = 0
GROUP BY dept.DEPT_ID, dept.DEPT_FULL_NAME;

-- ========================================
-- 步骤3: 找出可以安全删除的部门
-- ========================================

-- 3.1 列出所有可以安全物理删除的部门
-- 条件: 逻辑删除 AND 没有子部门 AND 没有人员引用 AND 没有标签引用
SELECT 
    dept.DEPT_ID as 部门ID,
    dept.DEPT_FULL_NAME as 部门名称,
    dept.DEPT_LEVELCODE as 层级编码,
    dept.CATEGORY as 类别,
    dept.UPDATE_TIME as 删除时间
FROM tp_dept_basicinfo dept
WHERE dept.ACTIVED = 0
-- 没有激活的子部门
AND NOT EXISTS (
    SELECT 1 FROM tp_dept_basicinfo child
    WHERE child.PDEPT_ID = dept.DEPT_ID
    AND child.ACTIVED = 1
)
-- 没有人员部门关系
AND NOT EXISTS (
    SELECT 1 FROM tp_person_dept pd
    WHERE pd.DEPT_ID = dept.DEPT_ID
)
-- 没有人员标签关系
AND NOT EXISTS (
    SELECT 1 FROM tp_person_tag pt
    WHERE pt.DEPT_ID = dept.DEPT_ID
)
ORDER BY dept.UPDATE_TIME DESC;

-- 3.2 统计可以安全删除的部门数量
SELECT 
    '可以安全物理删除的部门数量' as 统计项,
    COUNT(*) as 数量
FROM tp_dept_basicinfo dept
WHERE dept.ACTIVED = 0
AND NOT EXISTS (
    SELECT 1 FROM tp_dept_basicinfo child
    WHERE child.PDEPT_ID = dept.DEPT_ID
    AND child.ACTIVED = 1
)
AND NOT EXISTS (
    SELECT 1 FROM tp_person_dept pd
    WHERE pd.DEPT_ID = dept.DEPT_ID
)
AND NOT EXISTS (
    SELECT 1 FROM tp_person_tag pt
    WHERE pt.DEPT_ID = dept.DEPT_ID
);

-- ========================================
-- 步骤4: 执行物理删除（谨慎操作）
-- ========================================

-- 注意: 以下删除操作不可逆，请先备份数据库！

-- 4.1 备份要删除的数据（可选，建议执行）
-- CREATE TABLE tp_dept_basicinfo_deleted_backup AS
-- SELECT * FROM tp_dept_basicinfo
-- WHERE ACTIVED = 0
-- AND NOT EXISTS (
--     SELECT 1 FROM tp_dept_basicinfo child
--     WHERE child.PDEPT_ID = tp_dept_basicinfo.DEPT_ID
--     AND child.ACTIVED = 1
-- )
-- AND NOT EXISTS (
--     SELECT 1 FROM tp_person_dept pd
--     WHERE pd.DEPT_ID = tp_dept_basicinfo.DEPT_ID
-- )
-- AND NOT EXISTS (
--     SELECT 1 FROM tp_person_tag pt
--     WHERE pt.DEPT_ID = tp_dept_basicinfo.DEPT_ID
-- );

-- 4.2 执行物理删除（取消注释前请确认）
-- DELETE FROM tp_dept_basicinfo
-- WHERE ACTIVED = 0
-- AND NOT EXISTS (
--     SELECT 1 FROM tp_dept_basicinfo child
--     WHERE child.PDEPT_ID = tp_dept_basicinfo.DEPT_ID
--     AND child.ACTIVED = 1
-- )
-- AND NOT EXISTS (
--     SELECT 1 FROM tp_person_dept pd
--     WHERE pd.DEPT_ID = tp_dept_basicinfo.DEPT_ID
-- )
-- AND NOT EXISTS (
--     SELECT 1 FROM tp_person_tag pt
--     WHERE pt.DEPT_ID = tp_dept_basicinfo.DEPT_ID
-- );

-- 4.3 验证删除结果
-- SELECT 
--     '物理删除后剩余的逻辑删除部门' as 描述,
--     COUNT(*) as 数量,
--     '这些部门有引用关系，不能删除' as 备注
-- FROM tp_dept_basicinfo 
-- WHERE ACTIVED = 0;

-- ========================================
-- 步骤5: 清理孤立的部门扩展信息（可选）
-- ========================================

-- 5.1 查找孤立的部门扩展信息（部门已被删除）
-- SELECT 
--     '孤立的部门扩展信息' as 描述,
--     COUNT(*) as 数量
-- FROM tp_dept_exinfo ex
-- WHERE NOT EXISTS (
--     SELECT 1 FROM tp_dept_basicinfo dept
--     WHERE dept.DEPT_ID = ex.DEPT_ID
-- );

-- 5.2 删除孤立的部门扩展信息（取消注释前请确认）
-- DELETE FROM tp_dept_exinfo
-- WHERE NOT EXISTS (
--     SELECT 1 FROM tp_dept_basicinfo dept
--     WHERE dept.DEPT_ID = tp_dept_exinfo.DEPT_ID
-- );

-- ========================================
-- 使用说明
-- ========================================
-- 1. 执行步骤1-3的查询语句，分析数据情况
-- 2. 根据分析结果，决定是否需要清理数据
-- 3. 如需清理，先执行步骤4.1进行备份
-- 4. 取消步骤4.2的注释，执行物理删除
-- 5. 执行步骤4.3验证删除结果
-- 6. 可选：执行步骤5清理孤立的扩展信息

-- ========================================
-- 注意事项
-- ========================================
-- 1. 执行删除操作前务必备份数据库
-- 2. 建议在测试环境先验证脚本
-- 3. 生产环境执行时建议在业务低峰期进行
-- 4. 删除操作不可逆，请谨慎操作
-- 5. 如有疑问，请联系数据库管理员
