-- 创建人员数据权限表
-- 表名: tp_data_permissions
-- 作者: DDD重构补充
-- 日期: 2025-09-13

CREATE TABLE IF NOT EXISTS `tp_data_permissions` (
  `PER_ID` varchar(50) NOT NULL COMMENT '主键',
  `PERSON_ID` varchar(50) DEFAULT NULL COMMENT '人员ID', 
  `DEPT_ID` varchar(50) DEFAULT NULL COMMENT '部门ID',
  `ASCN_ID` varchar(50) DEFAULT NULL COMMENT '单位ID',
  `EXTEND_01` varchar(100) DEFAULT NULL COMMENT '备用字段1',
  `EXTEND_02` varchar(100) DEFAULT NULL COMMENT '备用字段2', 
  `EXTEND_03` varchar(100) DEFAULT NULL COMMENT '备用字段3',
  PRIMARY KEY (`PER_ID`),
  KEY `idx_person_id` (`PERSON_ID`),
  KEY `idx_dept_id` (`DEPT_ID`),
  KEY `idx_ascn_id` (`ASCN_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='人员数据权限表';

-- 添加一些示例数据（可选，仅供测试）
-- INSERT INTO `tp_data_permissions` (`PER_ID`, `PERSON_ID`, `DEPT_ID`, `ASCN_ID`) VALUES
-- ('1', 'test_person_1', 'dept_001', 'ascn_001'),
-- ('2', 'test_person_2', 'dept_002', 'ascn_002');