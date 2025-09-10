-- ========================================
-- 标签管理相关表设计
-- 创建时间: 2024-01-27
-- 描述: 用于标签管理功能的数据库表
-- ========================================

-- 删除已存在的表（如果存在）
DROP TABLE IF EXISTS `tp_person_tag`;
DROP TABLE IF EXISTS `tp_tag`;

-- 创建标签表
CREATE TABLE `tp_tag` (
    `tag_id` VARCHAR(19) PRIMARY KEY COMMENT '标签ID（主键）',
    `tag_name` VARCHAR(100) NOT NULL COMMENT '标签名称',
    `tag_desc` TEXT COMMENT '标签描述',
    `tag_color` VARCHAR(20) DEFAULT '#1890ff' COMMENT '标签颜色',
    `order_index` DOUBLE DEFAULT 0 COMMENT '排序号',
    `tenant_id` VARCHAR(19) COMMENT '租户ID',
    `ascn_id` VARCHAR(19) COMMENT '所属机构ID',
    `actived` INTEGER DEFAULT 1 COMMENT '是否有效（1：有效，0：无效）',
    `log_delete` INTEGER DEFAULT 0 COMMENT '逻辑删除（0：未删除，1：已删除）',
    `creator` VARCHAR(19) NOT NULL COMMENT '创建人',
    `create_time` VARCHAR(14) NOT NULL COMMENT '创建时间',
    `updator` VARCHAR(19) COMMENT '修改人',
    `update_time` VARCHAR(14) COMMENT '修改时间',
    `extend01` VARCHAR(128) COMMENT '扩展字段01',
    `extend02` VARCHAR(128) COMMENT '扩展字段02',
    `extend03` VARCHAR(128) COMMENT '扩展字段03'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='标签表';

-- 创建人员标签关系表
CREATE TABLE `tp_person_tag` (
    `person_id` VARCHAR(19) NOT NULL COMMENT '人员ID',
    `tag_id` VARCHAR(19) NOT NULL COMMENT '标签ID',
    `creator` VARCHAR(19) NOT NULL COMMENT '创建人',
    `create_time` VARCHAR(14) NOT NULL COMMENT '创建时间',
    PRIMARY KEY (`person_id`, `tag_id`),
    FOREIGN KEY (`person_id`) REFERENCES `tp_person_basicinfo`(`person_id`) ON DELETE CASCADE,
    FOREIGN KEY (`tag_id`) REFERENCES `tp_tag`(`tag_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='人员标签关系表';

-- 创建索引
-- 标签表索引
CREATE INDEX `idx_tag_name` ON `tp_tag`(`tag_name`);
CREATE INDEX `idx_tag_creator` ON `tp_tag`(`creator`);
CREATE INDEX `idx_tag_create_time` ON `tp_tag`(`create_time`);

-- 人员标签关系表索引
CREATE INDEX `idx_person_tag_person` ON `tp_person_tag`(`person_id`);
CREATE INDEX `idx_person_tag_tag` ON `tp_person_tag`(`tag_id`);
CREATE INDEX `idx_person_tag_creator` ON `tp_person_tag`(`creator`);

-- ========================================
-- 常用查询示例
-- ========================================

-- 1. 查询所有标签
-- SELECT * FROM tp_tag WHERE actived = 1 ORDER BY order_index, create_time DESC;

-- 2. 查询某个人员的所有标签
-- SELECT t.* FROM tp_tag t 
-- INNER JOIN tp_person_tag pt ON t.tag_id = pt.tag_id 
-- WHERE pt.person_id = '人员ID' AND t.actived = 1;

-- 3. 查询某个标签下的所有人员
-- SELECT p.* FROM tp_person_basicinfo p 
-- INNER JOIN tp_person_tag pt ON p.person_id = pt.person_id 
-- WHERE pt.tag_id = '标签ID' AND p.actived = 1;

-- 4. 统计每个标签的人员数量
-- SELECT t.tag_name, COUNT(pt.person_id) as person_count 
-- FROM tp_tag t 
-- LEFT JOIN tp_person_tag pt ON t.tag_id = pt.tag_id 
-- WHERE t.actived = 1 
-- GROUP BY t.tag_id, t.tag_name 
-- ORDER BY person_count DESC;

-- ========================================
-- 表结构验证
-- ========================================

-- 查看标签表结构
-- DESCRIBE tp_tag;

-- 查看人员标签关系表结构
-- DESCRIBE tp_person_tag;

-- 查看索引信息
-- SHOW INDEX FROM tp_tag;
-- SHOW INDEX FROM tp_person_tag;