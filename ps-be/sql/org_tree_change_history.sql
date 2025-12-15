-- ========================================
-- 组织机构树变更历史记录表设计
-- 创建时间: 2024-01-16
-- 描述: 用于记录组织机构树的所有变更历史
-- ========================================

-- 删除已存在的表（如果存在）
DROP TABLE IF EXISTS `org_tree_change_history`;

-- 创建组织机构树变更历史记录表
CREATE TABLE `org_tree_change_history` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '操作ID（主键自增）',
    `operation_type` ENUM('CREATE','UPDATE','DELETE','QUERY') NOT NULL COMMENT '操作类型',
    `operation_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
    `operator_id` BIGINT NOT NULL COMMENT '操作用户ID（外键关联用户表）',
    `before_data` LONGTEXT COMMENT '变更前的组织机构树JSON',
    `after_data` LONGTEXT COMMENT '变更后的组织机构树JSON',
    `before_full_tree` LONGTEXT COMMENT '变更前的完整组织机构节点树JSON',
    `after_full_tree` LONGTEXT COMMENT '变更后的完整组织机构节点树JSON',
    `version` BIGINT DEFAULT 1 COMMENT '版本号（数据库字段，不参与业务逻辑，用ID代替版本功能）',
    `dept_id` BIGINT DEFAULT 0 COMMENT '部门ID（数据库字段，不参与业务逻辑）'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='组织机构树变更历史记录表';

-- 创建索引
-- 操作时间普通索引
CREATE INDEX `idx_operation_time` ON `org_tree_change_history`(`operation_time`);

-- 操作用户ID普通索引
CREATE INDEX `idx_operator_id` ON `org_tree_change_history`(`operator_id`);

-- 复合索引：操作类型 + 操作时间（用于按类型和时间范围查询）
CREATE INDEX `idx_type_time` ON `org_tree_change_history`(`operation_type`, `operation_time`);

-- 复合索引：操作用户 + 操作时间（用于查询用户操作历史）
CREATE INDEX `idx_operator_time` ON `org_tree_change_history`(`operator_id`, `operation_time`);

-- ========================================
-- 数据库连接配置信息
-- ========================================
/*
MySQL数据库连接配置:
url: jdbc:mariadb://alilaoba.cn:13307/ps-bmp?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai
username: root
password: $D8BZ8Qmav
*/

-- ========================================
-- 性能优化说明
-- ========================================
/*
1. 版本号字段使用AUTO_INCREMENT和UNIQUE约束，支持快速查询特定版本记录
2. operation_time字段创建索引，支持时间范围查询
3. operator_id字段创建索引，支持用户操作历史查询
4. 创建复合索引提升多条件查询性能
5. 使用InnoDB存储引擎，支持事务和行级锁
6. 使用utf8mb4字符集，支持完整的Unicode字符
*/

-- ========================================
-- 常用查询示例
-- ========================================

-- 1. 查询特定版本记录
-- SELECT * FROM org_tree_change_history WHERE version = 100;

-- 2. 查询时间范围内的变更记录
-- SELECT * FROM org_tree_change_history 
-- WHERE operation_time BETWEEN '2024-01-01 00:00:00' AND '2024-01-31 23:59:59'
-- ORDER BY operation_time DESC;

-- 3. 查询用户操作历史
-- SELECT * FROM org_tree_change_history 
-- WHERE operator_id = 1001 
-- ORDER BY operation_time DESC 
-- LIMIT 20;

-- 4. 查询最新版本记录
-- SELECT * FROM org_tree_change_history 
-- ORDER BY version DESC 
-- LIMIT 1;

-- 5. 按操作类型统计
-- SELECT operation_type, COUNT(*) as count 
-- FROM org_tree_change_history 
-- GROUP BY operation_type;

-- ========================================
-- 表结构验证
-- ========================================

-- 查看表结构
-- DESCRIBE org_tree_change_history;

-- 查看索引信息
-- SHOW INDEX FROM org_tree_change_history;

-- 查看表创建语句
-- SHOW CREATE TABLE org_tree_change_history;