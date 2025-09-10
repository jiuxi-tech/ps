-- ========================================
-- 登录时间段控制规则表设计
-- 创建时间: 2025-01-20
-- 描述: 用于登录时间段权限控制功能的数据库表
-- ========================================

-- 删除已存在的表（如果存在）
DROP TABLE IF EXISTS `tp_time_rule`;

-- 创建登录时间段控制规则表
CREATE TABLE `tp_time_rule` (
    `id` VARCHAR(19) PRIMARY KEY COMMENT '规则ID（主键）',
    `rule_name` VARCHAR(100) NOT NULL COMMENT '规则名称',
    `start_time` VARCHAR(14) NOT NULL COMMENT '规则启动时间（yyyyMMddHHmmss格式）',
    `end_time` VARCHAR(14) NOT NULL COMMENT '规则结束时间（yyyyMMddHHmmss格式）',
    `status` INTEGER DEFAULT 1 COMMENT '规则状态（0：禁用，1：启用）',
    `allow_login` INTEGER DEFAULT 1 COMMENT '是否允许登录：1-允许，0-拒绝',
    `role_ids` TEXT COMMENT '角色ID串（逗号分隔）',
    `role_names` TEXT COMMENT '角色名称串（逗号分隔）',
    `user_ids` TEXT COMMENT '人员ID串（逗号分隔）',
    `user_names` TEXT COMMENT '人员名称串（逗号分隔）',
    `creator_id` VARCHAR(19) NOT NULL COMMENT '创建人ID',
    `create_time` VARCHAR(14) NOT NULL COMMENT '创建时间（yyyyMMddHHmmss格式）',
    `modifier_id` VARCHAR(19) COMMENT '修改人ID',
    `modify_time` VARCHAR(14) COMMENT '修改时间（yyyyMMddHHmmss格式）',

    `actived` INTEGER DEFAULT 1 COMMENT '是否有效（1：有效，0：无效）',
    `log_delete` INTEGER DEFAULT 0 COMMENT '逻辑删除（0：未删除，1：已删除）',
    `extend01` VARCHAR(128) COMMENT '扩展字段01',
    `extend02` VARCHAR(128) COMMENT '扩展字段02',
    `extend03` VARCHAR(128) COMMENT '扩展字段03'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='登录时间段控制规则表';

-- 创建索引以提高查询性能
CREATE INDEX `idx_rule_name` ON `tp_time_rule`(`rule_name`);
CREATE INDEX `idx_status` ON `tp_time_rule`(`status`);
CREATE INDEX `idx_allow_login` ON `tp_time_rule`(`allow_login`);
CREATE INDEX `idx_start_time` ON `tp_time_rule`(`start_time`);
CREATE INDEX `idx_end_time` ON `tp_time_rule`(`end_time`);
CREATE INDEX `idx_creator_id` ON `tp_time_rule`(`creator_id`);
CREATE INDEX `idx_create_time` ON `tp_time_rule`(`create_time`);

CREATE INDEX `idx_actived` ON `tp_time_rule`(`actived`);
CREATE INDEX `idx_log_delete` ON `tp_time_rule`(`log_delete`);

-- 创建复合索引以优化时间范围查询
CREATE INDEX `idx_time_range` ON `tp_time_rule`(`start_time`, `end_time`);
CREATE INDEX `idx_status_time` ON `tp_time_rule`(`status`, `start_time`, `end_time`);
CREATE INDEX `idx_active_status_time` ON `tp_time_rule`(`actived`, `log_delete`, `status`, `start_time`, `end_time`);

-- 插入示例数据
INSERT INTO `tp_time_rule` (
    `id`, `rule_name`, `start_time`, `end_time`, `status`, `allow_login`,
    `role_ids`, `role_names`, `user_ids`, `user_names`,
    `creator_id`, `create_time`
) VALUES 
(
    '1001', '管理员全天访问规则', '20250101000000', '20251231235959', 1, 1,
    'ADMIN', '管理员', '', '',
    'system', '20250120120000'
),
(
    '1002', '普通用户工作时间访问规则', '20250101080000', '20251231180000', 1, 1,
    'USER', '普通用户', '', '',
    'system', '20250120120000'
),
(
    '1003', '测试用户禁止访问规则', '20250101000000', '20251231235959', 1, 0,
    '', '', 'testuser', '测试用户',
    'system', '20250120120000'
);

-- 添加表注释说明
ALTER TABLE `tp_time_rule` COMMENT = '登录时间段控制规则表：用于控制特定用户或角色在指定时间段的登录权限';