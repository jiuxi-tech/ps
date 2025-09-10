-- ========================================
-- 用户标签关系表
-- ========================================

-- 创建用户标签关系表
CREATE TABLE IF NOT EXISTS `tp_person_tag` (
  `PERSON_ID` varchar(32) NOT NULL COMMENT '人员ID',
  `TAG_ID` varchar(32) NOT NULL COMMENT '标签ID',
  `DEPT_ID` varchar(32) NOT NULL COMMENT '部门ID',
  PRIMARY KEY (`PERSON_ID`, `TAG_ID`, `DEPT_ID`),
  KEY `idx_person_id` (`PERSON_ID`),
  KEY `idx_tag_id` (`TAG_ID`),
  KEY `idx_dept_id` (`DEPT_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户标签关系表';

-- 添加外键约束（可选）
-- ALTER TABLE `tp_person_tag` ADD CONSTRAINT `fk_person_tag_person` FOREIGN KEY (`PERSON_ID`) REFERENCES `tp_person_basicinfo` (`PERSON_ID`) ON DELETE CASCADE;
-- ALTER TABLE `tp_person_tag` ADD CONSTRAINT `fk_person_tag_tag` FOREIGN KEY (`TAG_ID`) REFERENCES `tp_tag` (`TAG_ID`) ON DELETE CASCADE;

-- ========================================
-- 示例查询语句
-- ========================================

-- 1. 查询某个人员的所有标签
-- SELECT t.TAG_NAME, t.TAG_DESC 
-- FROM tp_tag t 
-- INNER JOIN tp_person_tag pt ON t.TAG_ID = pt.TAG_ID 
-- WHERE pt.PERSON_ID = '人员ID' AND pt.DEPT_ID = '部门ID' AND t.ACTIVED = 1;

-- 2. 查询某个标签下的所有人员
-- SELECT p.PERSON_NAME, p.PERSON_CODE 
-- FROM tp_person_basicinfo p 
-- INNER JOIN tp_person_tag pt ON p.PERSON_ID = pt.PERSON_ID 
-- WHERE pt.TAG_ID = '标签ID' AND pt.DEPT_ID = '部门ID' AND p.ACTIVED = 1;

-- 3. 统计每个标签的人员数量
-- SELECT t.TAG_NAME, COUNT(pt.PERSON_ID) as person_count 
-- FROM tp_tag t 
-- LEFT JOIN tp_person_tag pt ON t.TAG_ID = pt.TAG_ID 
-- WHERE t.ACTIVED = 1 
-- GROUP BY t.TAG_ID, t.TAG_NAME 
-- ORDER BY person_count DESC;

-- 4. 删除某个人员的所有标签
-- DELETE FROM tp_person_tag WHERE PERSON_ID = '人员ID' AND DEPT_ID = '部门ID';

-- 5. 删除某个标签的所有关联
-- DELETE FROM tp_person_tag WHERE TAG_ID = '标签ID';