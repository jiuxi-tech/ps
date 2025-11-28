-- ===================================================================
-- 批量插入新部门到虚拟顶级节点下
-- 创建时间: 2025-01-27
-- 描述: 在1111111111111111111虚拟节点下批量添加35个新部门
-- ===================================================================

-- 查看当前最大ORDER_INDEX
SELECT 
    MAX(ORDER_INDEX) as 当前最大排序,
    COUNT(*) as 当前子部门数
FROM tp_dept_basicinfo 
WHERE PDEPT_ID = '1111111111111111111' 
  AND ACTIVED = 1;

-- 查看虚拟节点信息
SELECT 
    DEPT_ID,
    DEPT_FULL_NAME,
    DEPT_LEVELCODE,
    CATEGORY
FROM tp_dept_basicinfo 
WHERE DEPT_ID = '1111111111111111111';

-- 开始批量插入
-- 注意：DEPT_ID使用雪花算法生成的唯一ID，这里使用时间戳+序号模拟
-- DEPT_LEVELCODE格式：101 + 6位数字（从101000001开始）
-- 实际执行时需要替换为真实的雪花ID

SET @base_time = UNIX_TIMESTAMP() * 1000;
SET @order_start = 10; -- 从10开始排序

-- 1. 办公室
INSERT INTO tp_dept_basicinfo (
    DEPT_ID, PDEPT_ID, DEPT_LEVELCODE, DEPT_FULL_NAME, DEPT_SIMPLE_NAME,
    DEPT_TYPE, CATEGORY, ASCN_ID, ENABLED, ACTIVED, 
    ORDER_INDEX, LEAF, CREATOR, CREATE_TIME, UPDATOR, UPDATE_TIME,
    FULL_DEPT_NAME
) VALUES (
    CONCAT(@base_time, '001'), '1111111111111111111', '101000001', 
    '办公室', '办公室', 'SYS0502', 0, CONCAT(@base_time, '001'), 
    1, 1, @order_start + 1, 1, 'admin', NOW(), 'admin', NOW(),
    '办公室'
);

-- 2. 教务处
INSERT INTO tp_dept_basicinfo (
    DEPT_ID, PDEPT_ID, DEPT_LEVELCODE, DEPT_FULL_NAME, DEPT_SIMPLE_NAME,
    DEPT_TYPE, CATEGORY, ASCN_ID, ENABLED, ACTIVED, 
    ORDER_INDEX, LEAF, CREATOR, CREATE_TIME, UPDATOR, UPDATE_TIME,
    FULL_DEPT_NAME
) VALUES (
    CONCAT(@base_time, '002'), '1111111111111111111', '101000002', 
    '教务处', '教务处', 'SYS0502', 0, CONCAT(@base_time, '002'), 
    1, 1, @order_start + 2, 1, 'admin', NOW(), 'admin', NOW(),
    '教务处'
);

-- 3. 科研处
INSERT INTO tp_dept_basicinfo (
    DEPT_ID, PDEPT_ID, DEPT_LEVELCODE, DEPT_FULL_NAME, DEPT_SIMPLE_NAME,
    DEPT_TYPE, CATEGORY, ASCN_ID, ENABLED, ACTIVED, 
    ORDER_INDEX, LEAF, CREATOR, CREATE_TIME, UPDATOR, UPDATE_TIME,
    FULL_DEPT_NAME
) VALUES (
    CONCAT(@base_time, '003'), '1111111111111111111', '101000003', 
    '科研处', '科研处', 'SYS0502', 0, CONCAT(@base_time, '003'), 
    1, 1, @order_start + 3, 1, 'admin', NOW(), 'admin', NOW(),
    '科研处'
);

-- 4. 杨凌校区管理办公室
INSERT INTO tp_dept_basicinfo (
    DEPT_ID, PDEPT_ID, DEPT_LEVELCODE, DEPT_FULL_NAME, DEPT_SIMPLE_NAME,
    DEPT_TYPE, CATEGORY, ASCN_ID, ENABLED, ACTIVED, 
    ORDER_INDEX, LEAF, CREATOR, CREATE_TIME, UPDATOR, UPDATE_TIME,
    FULL_DEPT_NAME
) VALUES (
    CONCAT(@base_time, '004'), '1111111111111111111', '101000004', 
    '杨凌校区管理办公室', '杨凌校区办', 'SYS0502', 0, CONCAT(@base_time, '004'), 
    1, 1, @order_start + 4, 1, 'admin', NOW(), 'admin', NOW(),
    '杨凌校区管理办公室'
);

-- 5. 马克思主义学院
INSERT INTO tp_dept_basicinfo (
    DEPT_ID, PDEPT_ID, DEPT_LEVELCODE, DEPT_FULL_NAME, DEPT_SIMPLE_NAME,
    DEPT_TYPE, CATEGORY, ASCN_ID, ENABLED, ACTIVED, 
    ORDER_INDEX, LEAF, CREATOR, CREATE_TIME, UPDATOR, UPDATE_TIME,
    FULL_DEPT_NAME
) VALUES (
    CONCAT(@base_time, '005'), '1111111111111111111', '101000005', 
    '马克思主义学院', '马院', 'SYS0502', 0, CONCAT(@base_time, '005'), 
    1, 1, @order_start + 5, 1, 'admin', NOW(), 'admin', NOW(),
    '马克思主义学院'
);

-- 6. 哲学教研部
INSERT INTO tp_dept_basicinfo (
    DEPT_ID, PDEPT_ID, DEPT_LEVELCODE, DEPT_FULL_NAME, DEPT_SIMPLE_NAME,
    DEPT_TYPE, CATEGORY, ASCN_ID, ENABLED, ACTIVED, 
    ORDER_INDEX, LEAF, CREATOR, CREATE_TIME, UPDATOR, UPDATE_TIME,
    FULL_DEPT_NAME
) VALUES (
    CONCAT(@base_time, '006'), '1111111111111111111', '101000006', 
    '哲学教研部', '哲学教研部', 'SYS0502', 0, CONCAT(@base_time, '006'), 
    1, 1, @order_start + 6, 1, 'admin', NOW(), 'admin', NOW(),
    '哲学教研部'
);

-- 7. 经济学教研部
INSERT INTO tp_dept_basicinfo (
    DEPT_ID, PDEPT_ID, DEPT_LEVELCODE, DEPT_FULL_NAME, DEPT_SIMPLE_NAME,
    DEPT_TYPE, CATEGORY, ASCN_ID, ENABLED, ACTIVED, 
    ORDER_INDEX, LEAF, CREATOR, CREATE_TIME, UPDATOR, UPDATE_TIME,
    FULL_DEPT_NAME
) VALUES (
    CONCAT(@base_time, '007'), '1111111111111111111', '101000007', 
    '经济学教研部', '经济学教研部', 'SYS0502', 0, CONCAT(@base_time, '007'), 
    1, 1, @order_start + 7, 1, 'admin', NOW(), 'admin', NOW(),
    '经济学教研部'
);

-- 8. 科学社会主义教研部
INSERT INTO tp_dept_basicinfo (
    DEPT_ID, PDEPT_ID, DEPT_LEVELCODE, DEPT_FULL_NAME, DEPT_SIMPLE_NAME,
    DEPT_TYPE, CATEGORY, ASCN_ID, ENABLED, ACTIVED, 
    ORDER_INDEX, LEAF, CREATOR, CREATE_TIME, UPDATOR, UPDATE_TIME,
    FULL_DEPT_NAME
) VALUES (
    CONCAT(@base_time, '008'), '1111111111111111111', '101000008', 
    '科学社会主义教研部', '科社教研部', 'SYS0502', 0, CONCAT(@base_time, '008'), 
    1, 1, @order_start + 8, 1, 'admin', NOW(), 'admin', NOW(),
    '科学社会主义教研部'
);

-- 9. 管理学教研部
INSERT INTO tp_dept_basicinfo (
    DEPT_ID, PDEPT_ID, DEPT_LEVELCODE, DEPT_FULL_NAME, DEPT_SIMPLE_NAME,
    DEPT_TYPE, CATEGORY, ASCN_ID, ENABLED, ACTIVED, 
    ORDER_INDEX, LEAF, CREATOR, CREATE_TIME, UPDATOR, UPDATE_TIME,
    FULL_DEPT_NAME
) VALUES (
    CONCAT(@base_time, '009'), '1111111111111111111', '101000009', 
    '管理学教研部', '管理学教研部', 'SYS0502', 0, CONCAT(@base_time, '009'), 
    1, 1, @order_start + 9, 1, 'admin', NOW(), 'admin', NOW(),
    '管理学教研部'
);

-- 10. 政治学教研部
INSERT INTO tp_dept_basicinfo (
    DEPT_ID, PDEPT_ID, DEPT_LEVELCODE, DEPT_FULL_NAME, DEPT_SIMPLE_NAME,
    DEPT_TYPE, CATEGORY, ASCN_ID, ENABLED, ACTIVED, 
    ORDER_INDEX, LEAF, CREATOR, CREATE_TIME, UPDATOR, UPDATE_TIME,
    FULL_DEPT_NAME
) VALUES (
    CONCAT(@base_time, '010'), '1111111111111111111', '101000010', 
    '政治学教研部', '政治学教研部', 'SYS0502', 0, CONCAT(@base_time, '010'), 
    1, 1, @order_start + 10, 1, 'admin', NOW(), 'admin', NOW(),
    '政治学教研部'
);

-- 11. 法学与社会治理教研部
INSERT INTO tp_dept_basicinfo (
    DEPT_ID, PDEPT_ID, DEPT_LEVELCODE, DEPT_FULL_NAME, DEPT_SIMPLE_NAME,
    DEPT_TYPE, CATEGORY, ASCN_ID, ENABLED, ACTIVED, 
    ORDER_INDEX, LEAF, CREATOR, CREATE_TIME, UPDATOR, UPDATE_TIME,
    FULL_DEPT_NAME
) VALUES (
    CONCAT(@base_time, '011'), '1111111111111111111', '101000011', 
    '法学与社会治理教研部', '法学教研部', 'SYS0502', 0, CONCAT(@base_time, '011'), 
    1, 1, @order_start + 11, 1, 'admin', NOW(), 'admin', NOW(),
    '法学与社会治理教研部'
);

-- 12. 生态文明教研部
INSERT INTO tp_dept_basicinfo (
    DEPT_ID, PDEPT_ID, DEPT_LEVELCODE, DEPT_FULL_NAME, DEPT_SIMPLE_NAME,
    DEPT_TYPE, CATEGORY, ASCN_ID, ENABLED, ACTIVED, 
    ORDER_INDEX, LEAF, CREATOR, CREATE_TIME, UPDATOR, UPDATE_TIME,
    FULL_DEPT_NAME
) VALUES (
    CONCAT(@base_time, '012'), '1111111111111111111', '101000012', 
    '生态文明教研部', '生态文明教研部', 'SYS0502', 0, CONCAT(@base_time, '012'), 
    1, 1, @order_start + 12, 1, 'admin', NOW(), 'admin', NOW(),
    '生态文明教研部'
);

-- 13. 文化与科技教研部
INSERT INTO tp_dept_basicinfo (
    DEPT_ID, PDEPT_ID, DEPT_LEVELCODE, DEPT_FULL_NAME, DEPT_SIMPLE_NAME,
    DEPT_TYPE, CATEGORY, ASCN_ID, ENABLED, ACTIVED, 
    ORDER_INDEX, LEAF, CREATOR, CREATE_TIME, UPDATOR, UPDATE_TIME,
    FULL_DEPT_NAME
) VALUES (
    CONCAT(@base_time, '013'), '1111111111111111111', '101000013', 
    '文化与科技教研部', '文化科技教研部', 'SYS0502', 0, CONCAT(@base_time, '013'), 
    1, 1, @order_start + 13, 1, 'admin', NOW(), 'admin', NOW(),
    '文化与科技教研部'
);

-- 14. 中共党史教研部
INSERT INTO tp_dept_basicinfo (
    DEPT_ID, PDEPT_ID, DEPT_LEVELCODE, DEPT_FULL_NAME, DEPT_SIMPLE_NAME,
    DEPT_TYPE, CATEGORY, ASCN_ID, ENABLED, ACTIVED, 
    ORDER_INDEX, LEAF, CREATOR, CREATE_TIME, UPDATOR, UPDATE_TIME,
    FULL_DEPT_NAME
) VALUES (
    CONCAT(@base_time, '014'), '1111111111111111111', '101000014', 
    '中共党史教研部', '党史教研部', 'SYS0502', 0, CONCAT(@base_time, '014'), 
    1, 1, @order_start + 14, 1, 'admin', NOW(), 'admin', NOW(),
    '中共党史教研部'
);

-- 15. 省延安精神研究会办公室
INSERT INTO tp_dept_basicinfo (
    DEPT_ID, PDEPT_ID, DEPT_LEVELCODE, DEPT_FULL_NAME, DEPT_SIMPLE_NAME,
    DEPT_TYPE, CATEGORY, ASCN_ID, ENABLED, ACTIVED, 
    ORDER_INDEX, LEAF, CREATOR, CREATE_TIME, UPDATOR, UPDATE_TIME,
    FULL_DEPT_NAME
) VALUES (
    CONCAT(@base_time, '015'), '1111111111111111111', '101000015', 
    '省延安精神研究会办公室', '延安精神办', 'SYS0502', 0, CONCAT(@base_time, '015'), 
    1, 1, @order_start + 15, 1, 'admin', NOW(), 'admin', NOW(),
    '省延安精神研究会办公室'
);

-- 16. 党的建设教研部
INSERT INTO tp_dept_basicinfo (
    DEPT_ID, PDEPT_ID, DEPT_LEVELCODE, DEPT_FULL_NAME, DEPT_SIMPLE_NAME,
    DEPT_TYPE, CATEGORY, ASCN_ID, ENABLED, ACTIVED, 
    ORDER_INDEX, LEAF, CREATOR, CREATE_TIME, UPDATOR, UPDATE_TIME,
    FULL_DEPT_NAME
) VALUES (
    CONCAT(@base_time, '016'), '1111111111111111111', '101000016', 
    '党的建设教研部', '党建教研部', 'SYS0502', 0, CONCAT(@base_time, '016'), 
    1, 1, @order_start + 16, 1, 'admin', NOW(), 'admin', NOW(),
    '党的建设教研部'
);

-- 17. 学员一部
INSERT INTO tp_dept_basicinfo (
    DEPT_ID, PDEPT_ID, DEPT_LEVELCODE, DEPT_FULL_NAME, DEPT_SIMPLE_NAME,
    DEPT_TYPE, CATEGORY, ASCN_ID, ENABLED, ACTIVED, 
    ORDER_INDEX, LEAF, CREATOR, CREATE_TIME, UPDATOR, UPDATE_TIME,
    FULL_DEPT_NAME
) VALUES (
    CONCAT(@base_time, '017'), '1111111111111111111', '101000017', 
    '学员一部', '学员一部', 'SYS0502', 0, CONCAT(@base_time, '017'), 
    1, 1, @order_start + 17, 1, 'admin', NOW(), 'admin', NOW(),
    '学员一部'
);

-- 18. 学员二部
INSERT INTO tp_dept_basicinfo (
    DEPT_ID, PDEPT_ID, DEPT_LEVELCODE, DEPT_FULL_NAME, DEPT_SIMPLE_NAME,
    DEPT_TYPE, CATEGORY, ASCN_ID, ENABLED, ACTIVED, 
    ORDER_INDEX, LEAF, CREATOR, CREATE_TIME, UPDATOR, UPDATE_TIME,
    FULL_DEPT_NAME
) VALUES (
    CONCAT(@base_time, '018'), '1111111111111111111', '101000018', 
    '学员二部', '学员二部', 'SYS0502', 0, CONCAT(@base_time, '018'), 
    1, 1, @order_start + 18, 1, 'admin', NOW(), 'admin', NOW(),
    '学员二部'
);

-- 19. 学员三部
INSERT INTO tp_dept_basicinfo (
    DEPT_ID, PDEPT_ID, DEPT_LEVELCODE, DEPT_FULL_NAME, DEPT_SIMPLE_NAME,
    DEPT_TYPE, CATEGORY, ASCN_ID, ENABLED, ACTIVED, 
    ORDER_INDEX, LEAF, CREATOR, CREATE_TIME, UPDATOR, UPDATE_TIME,
    FULL_DEPT_NAME
) VALUES (
    CONCAT(@base_time, '019'), '1111111111111111111', '101000019', 
    '学员三部', '学员三部', 'SYS0502', 0, CONCAT(@base_time, '019'), 
    1, 1, @order_start + 19, 1, 'admin', NOW(), 'admin', NOW(),
    '学员三部'
);

-- 20. 干部教育培训学院（延安精神培训中心）
INSERT INTO tp_dept_basicinfo (
    DEPT_ID, PDEPT_ID, DEPT_LEVELCODE, DEPT_FULL_NAME, DEPT_SIMPLE_NAME,
    DEPT_TYPE, CATEGORY, ASCN_ID, ENABLED, ACTIVED, 
    ORDER_INDEX, LEAF, CREATOR, CREATE_TIME, UPDATOR, UPDATE_TIME,
    FULL_DEPT_NAME
) VALUES (
    CONCAT(@base_time, '020'), '1111111111111111111', '101000020', 
    '干部教育培训学院（延安精神培训中心）', '干部培训学院', 'SYS0502', 0, CONCAT(@base_time, '020'), 
    1, 1, @order_start + 20, 1, 'admin', NOW(), 'admin', NOW(),
    '干部教育培训学院（延安精神培训中心）'
);

-- 21. 研究生院
INSERT INTO tp_dept_basicinfo (
    DEPT_ID, PDEPT_ID, DEPT_LEVELCODE, DEPT_FULL_NAME, DEPT_SIMPLE_NAME,
    DEPT_TYPE, CATEGORY, ASCN_ID, ENABLED, ACTIVED, 
    ORDER_INDEX, LEAF, CREATOR, CREATE_TIME, UPDATOR, UPDATE_TIME,
    FULL_DEPT_NAME
) VALUES (
    CONCAT(@base_time, '021'), '1111111111111111111', '101000021', 
    '研究生院', '研究生院', 'SYS0502', 0, CONCAT(@base_time, '021'), 
    1, 1, @order_start + 21, 1, 'admin', NOW(), 'admin', NOW(),
    '研究生院'
);

-- 22. 图书和文化馆
INSERT INTO tp_dept_basicinfo (
    DEPT_ID, PDEPT_ID, DEPT_LEVELCODE, DEPT_FULL_NAME, DEPT_SIMPLE_NAME,
    DEPT_TYPE, CATEGORY, ASCN_ID, ENABLED, ACTIVED, 
    ORDER_INDEX, LEAF, CREATOR, CREATE_TIME, UPDATOR, UPDATE_TIME,
    FULL_DEPT_NAME
) VALUES (
    CONCAT(@base_time, '022'), '1111111111111111111', '101000022', 
    '图书和文化馆', '图书文化馆', 'SYS0502', 0, CONCAT(@base_time, '022'), 
    1, 1, @order_start + 22, 1, 'admin', NOW(), 'admin', NOW(),
    '图书和文化馆'
);

-- 23. 校刊编辑部
INSERT INTO tp_dept_basicinfo (
    DEPT_ID, PDEPT_ID, DEPT_LEVELCODE, DEPT_FULL_NAME, DEPT_SIMPLE_NAME,
    DEPT_TYPE, CATEGORY, ASCN_ID, ENABLED, ACTIVED, 
    ORDER_INDEX, LEAF, CREATOR, CREATE_TIME, UPDATOR, UPDATE_TIME,
    FULL_DEPT_NAME
) VALUES (
    CONCAT(@base_time, '023'), '1111111111111111111', '101000023', 
    '校刊编辑部', '校刊编辑部', 'SYS0502', 0, CONCAT(@base_time, '023'), 
    1, 1, @order_start + 23, 1, 'admin', NOW(), 'admin', NOW(),
    '校刊编辑部'
);

-- 24. 组织人事处
INSERT INTO tp_dept_basicinfo (
    DEPT_ID, PDEPT_ID, DEPT_LEVELCODE, DEPT_FULL_NAME, DEPT_SIMPLE_NAME,
    DEPT_TYPE, CATEGORY, ASCN_ID, ENABLED, ACTIVED, 
    ORDER_INDEX, LEAF, CREATOR, CREATE_TIME, UPDATOR, UPDATE_TIME,
    FULL_DEPT_NAME
) VALUES (
    CONCAT(@base_time, '024'), '1111111111111111111', '101000024', 
    '组织人事处', '组织人事处', 'SYS0502', 0, CONCAT(@base_time, '024'), 
    1, 1, @order_start + 24, 1, 'admin', NOW(), 'admin', NOW(),
    '组织人事处'
);

-- 25. 宣传处
INSERT INTO tp_dept_basicinfo (
    DEPT_ID, PDEPT_ID, DEPT_LEVELCODE, DEPT_FULL_NAME, DEPT_SIMPLE_NAME,
    DEPT_TYPE, CATEGORY, ASCN_ID, ENABLED, ACTIVED, 
    ORDER_INDEX, LEAF, CREATOR, CREATE_TIME, UPDATOR, UPDATE_TIME,
    FULL_DEPT_NAME
) VALUES (
    CONCAT(@base_time, '025'), '1111111111111111111', '101000025', 
    '宣传处', '宣传处', 'SYS0502', 0, CONCAT(@base_time, '025'), 
    1, 1, @order_start + 25, 1, 'admin', NOW(), 'admin', NOW(),
    '宣传处'
);

-- 26. 党校（行政学院）工作处
INSERT INTO tp_dept_basicinfo (
    DEPT_ID, PDEPT_ID, DEPT_LEVELCODE, DEPT_FULL_NAME, DEPT_SIMPLE_NAME,
    DEPT_TYPE, CATEGORY, ASCN_ID, ENABLED, ACTIVED, 
    ORDER_INDEX, LEAF, CREATOR, CREATE_TIME, UPDATOR, UPDATE_TIME,
    FULL_DEPT_NAME
) VALUES (
    CONCAT(@base_time, '026'), '1111111111111111111', '101000026', 
    '党校（行政学院）工作处', '党校工作处', 'SYS0502', 0, CONCAT(@base_time, '026'), 
    1, 1, @order_start + 26, 1, 'admin', NOW(), 'admin', NOW(),
    '党校（行政学院）工作处'
);

-- 27. 财务处
INSERT INTO tp_dept_basicinfo (
    DEPT_ID, PDEPT_ID, DEPT_LEVELCODE, DEPT_FULL_NAME, DEPT_SIMPLE_NAME,
    DEPT_TYPE, CATEGORY, ASCN_ID, ENABLED, ACTIVED, 
    ORDER_INDEX, LEAF, CREATOR, CREATE_TIME, UPDATOR, UPDATE_TIME,
    FULL_DEPT_NAME
) VALUES (
    CONCAT(@base_time, '027'), '1111111111111111111', '101000027', 
    '财务处', '财务处', 'SYS0502', 0, CONCAT(@base_time, '027'), 
    1, 1, @order_start + 27, 1, 'admin', NOW(), 'admin', NOW(),
    '财务处'
);

-- 28. 总务处
INSERT INTO tp_dept_basicinfo (
    DEPT_ID, PDEPT_ID, DEPT_LEVELCODE, DEPT_FULL_NAME, DEPT_SIMPLE_NAME,
    DEPT_TYPE, CATEGORY, ASCN_ID, ENABLED, ACTIVED, 
    ORDER_INDEX, LEAF, CREATOR, CREATE_TIME, UPDATOR, UPDATE_TIME,
    FULL_DEPT_NAME
) VALUES (
    CONCAT(@base_time, '028'), '1111111111111111111', '101000028', 
    '总务处', '总务处', 'SYS0502', 0, CONCAT(@base_time, '028'), 
    1, 1, @order_start + 28, 1, 'admin', NOW(), 'admin', NOW(),
    '总务处'
);

-- 29. 信息技术部
INSERT INTO tp_dept_basicinfo (
    DEPT_ID, PDEPT_ID, DEPT_LEVELCODE, DEPT_FULL_NAME, DEPT_SIMPLE_NAME,
    DEPT_TYPE, CATEGORY, ASCN_ID, ENABLED, ACTIVED, 
    ORDER_INDEX, LEAF, CREATOR, CREATE_TIME, UPDATOR, UPDATE_TIME,
    FULL_DEPT_NAME
) VALUES (
    CONCAT(@base_time, '029'), '1111111111111111111', '101000029', 
    '信息技术部', '信息技术部', 'SYS0502', 0, CONCAT(@base_time, '029'), 
    1, 1, @order_start + 29, 1, 'admin', NOW(), 'admin', NOW(),
    '信息技术部'
);

-- 30. 保卫处
INSERT INTO tp_dept_basicinfo (
    DEPT_ID, PDEPT_ID, DEPT_LEVELCODE, DEPT_FULL_NAME, DEPT_SIMPLE_NAME,
    DEPT_TYPE, CATEGORY, ASCN_ID, ENABLED, ACTIVED, 
    ORDER_INDEX, LEAF, CREATOR, CREATE_TIME, UPDATOR, UPDATE_TIME,
    FULL_DEPT_NAME
) VALUES (
    CONCAT(@base_time, '030'), '1111111111111111111', '101000030', 
    '保卫处', '保卫处', 'SYS0502', 0, CONCAT(@base_time, '030'), 
    1, 1, @order_start + 30, 1, 'admin', NOW(), 'admin', NOW(),
    '保卫处'
);

-- 31. 后勤服务中心
INSERT INTO tp_dept_basicinfo (
    DEPT_ID, PDEPT_ID, DEPT_LEVELCODE, DEPT_FULL_NAME, DEPT_SIMPLE_NAME,
    DEPT_TYPE, CATEGORY, ASCN_ID, ENABLED, ACTIVED, 
    ORDER_INDEX, LEAF, CREATOR, CREATE_TIME, UPDATOR, UPDATE_TIME,
    FULL_DEPT_NAME
) VALUES (
    CONCAT(@base_time, '031'), '1111111111111111111', '101000031', 
    '后勤服务中心', '后勤中心', 'SYS0502', 0, CONCAT(@base_time, '031'), 
    1, 1, @order_start + 31, 1, 'admin', NOW(), 'admin', NOW(),
    '后勤服务中心'
);

-- 32. 机关党委
INSERT INTO tp_dept_basicinfo (
    DEPT_ID, PDEPT_ID, DEPT_LEVELCODE, DEPT_FULL_NAME, DEPT_SIMPLE_NAME,
    DEPT_TYPE, CATEGORY, ASCN_ID, ENABLED, ACTIVED, 
    ORDER_INDEX, LEAF, CREATOR, CREATE_TIME, UPDATOR, UPDATE_TIME,
    FULL_DEPT_NAME
) VALUES (
    CONCAT(@base_time, '032'), '1111111111111111111', '101000032', 
    '机关党委', '机关党委', 'SYS0502', 0, CONCAT(@base_time, '032'), 
    1, 1, @order_start + 32, 1, 'admin', NOW(), 'admin', NOW(),
    '机关党委'
);

-- 33. 离退休人员服务管理处
INSERT INTO tp_dept_basicinfo (
    DEPT_ID, PDEPT_ID, DEPT_LEVELCODE, DEPT_FULL_NAME, DEPT_SIMPLE_NAME,
    DEPT_TYPE, CATEGORY, ASCN_ID, ENABLED, ACTIVED, 
    ORDER_INDEX, LEAF, CREATOR, CREATE_TIME, UPDATOR, UPDATE_TIME,
    FULL_DEPT_NAME
) VALUES (
    CONCAT(@base_time, '033'), '1111111111111111111', '101000033', 
    '离退休人员服务管理处', '离退休处', 'SYS0502', 0, CONCAT(@base_time, '033'), 
    1, 1, @order_start + 33, 1, 'admin', NOW(), 'admin', NOW(),
    '离退休人员服务管理处'
);

-- 34. 中国特色社会主义理论研究中心
INSERT INTO tp_dept_basicinfo (
    DEPT_ID, PDEPT_ID, DEPT_LEVELCODE, DEPT_FULL_NAME, DEPT_SIMPLE_NAME,
    DEPT_TYPE, CATEGORY, ASCN_ID, ENABLED, ACTIVED, 
    ORDER_INDEX, LEAF, CREATOR, CREATE_TIME, UPDATOR, UPDATE_TIME,
    FULL_DEPT_NAME
) VALUES (
    CONCAT(@base_time, '034'), '1111111111111111111', '101000034', 
    '中国特色社会主义理论研究中心', '理论研究中心', 'SYS0502', 0, CONCAT(@base_time, '034'), 
    1, 1, @order_start + 34, 1, 'admin', NOW(), 'admin', NOW(),
    '中国特色社会主义理论研究中心'
);

-- 35. 应急管理培训中心
INSERT INTO tp_dept_basicinfo (
    DEPT_ID, PDEPT_ID, DEPT_LEVELCODE, DEPT_FULL_NAME, DEPT_SIMPLE_NAME,
    DEPT_TYPE, CATEGORY, ASCN_ID, ENABLED, ACTIVED, 
    ORDER_INDEX, LEAF, CREATOR, CREATE_TIME, UPDATOR, UPDATE_TIME,
    FULL_DEPT_NAME
) VALUES (
    CONCAT(@base_time, '035'), '1111111111111111111', '101000035', 
    '应急管理培训中心', '应急培训中心', 'SYS0502', 0, CONCAT(@base_time, '035'), 
    1, 1, @order_start + 35, 1, 'admin', NOW(), 'admin', NOW(),
    '应急管理培训中心'
);

-- 36. 专项办
INSERT INTO tp_dept_basicinfo (
    DEPT_ID, PDEPT_ID, DEPT_LEVELCODE, DEPT_FULL_NAME, DEPT_SIMPLE_NAME,
    DEPT_TYPE, CATEGORY, ASCN_ID, ENABLED, ACTIVED, 
    ORDER_INDEX, LEAF, CREATOR, CREATE_TIME, UPDATOR, UPDATE_TIME,
    FULL_DEPT_NAME
) VALUES (
    CONCAT(@base_time, '036'), '1111111111111111111', '101000036', 
    '专项办', '专项办', 'SYS0502', 0, CONCAT(@base_time, '036'), 
    1, 1, @order_start + 36, 1, 'admin', NOW(), 'admin', NOW(),
    '专项办'
);

-- 37. 长期病假
INSERT INTO tp_dept_basicinfo (
    DEPT_ID, PDEPT_ID, DEPT_LEVELCODE, DEPT_FULL_NAME, DEPT_SIMPLE_NAME,
    DEPT_TYPE, CATEGORY, ASCN_ID, ENABLED, ACTIVED, 
    ORDER_INDEX, LEAF, CREATOR, CREATE_TIME, UPDATOR, UPDATE_TIME,
    FULL_DEPT_NAME
) VALUES (
    CONCAT(@base_time, '037'), '1111111111111111111', '101000037', 
    '长期病假', '长期病假', 'SYS0502', 0, CONCAT(@base_time, '037'), 
    1, 1, @order_start + 37, 1, 'admin', NOW(), 'admin', NOW(),
    '长期病假'
);

-- 验证插入结果
SELECT '插入完成！' as 状态;

SELECT 
    COUNT(*) as 新增部门数
FROM tp_dept_basicinfo 
WHERE PDEPT_ID = '1111111111111111111' 
  AND ACTIVED = 1;

-- 显示新插入的部门
SELECT 
    DEPT_ID,
    DEPT_FULL_NAME,
    DEPT_SIMPLE_NAME,
    FULL_DEPT_NAME,
    ORDER_INDEX
FROM tp_dept_basicinfo 
WHERE PDEPT_ID = '1111111111111111111' 
  AND ACTIVED = 1
ORDER BY ORDER_INDEX;
