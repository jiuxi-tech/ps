-- ========================================
-- 密码历史表创建脚本
-- 创建时间: 2025-12-01
-- 描述: 创建密码修改历史记录表，记录所有密码修改操作
-- ========================================

-- 创建密码历史表
CREATE TABLE IF NOT EXISTS `tp_password_history` (
  `history_id` varchar(36) NOT NULL COMMENT '历史记录ID',
  `account_id` varchar(36) NOT NULL COMMENT '账号ID',
  `person_id` varchar(36) NOT NULL COMMENT '人员ID',
  `username` varchar(100) NOT NULL COMMENT '账号用户名',
  `password_hash` varchar(256) NOT NULL COMMENT '密码哈希值',
  `change_type` tinyint(4) NOT NULL COMMENT '修改类型：1-用户主动修改 2-管理员重置 3-密码过期强制修改 4-其他',
  `change_reason` varchar(500) DEFAULT NULL COMMENT '修改原因说明',
  `changed_by` varchar(36) DEFAULT NULL COMMENT '修改操作人ID',
  `changed_by_name` varchar(100) DEFAULT NULL COMMENT '修改操作人姓名',
  `change_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '密码修改时间',
  `ip_address` varchar(50) DEFAULT NULL COMMENT '修改操作的IP地址',
  `user_agent` varchar(500) DEFAULT NULL COMMENT '修改操作的用户代理信息',
  `tenant_id` varchar(36) DEFAULT NULL COMMENT '租户ID',
  `remark` varchar(1000) DEFAULT NULL COMMENT '备注信息',
  `created_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '记录创建时间',
  PRIMARY KEY (`history_id`),
  KEY `idx_account_id` (`account_id`),
  KEY `idx_person_id` (`person_id`),
  KEY `idx_change_time` (`change_time`),
  KEY `idx_change_type` (`change_type`),
  KEY `idx_tenant_id` (`tenant_id`),
  KEY `idx_account_change_time` (`account_id`,`change_time` DESC)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='密码修改历史记录表';

-- ========================================
-- 验证脚本
-- ========================================

-- 查看表结构
-- DESC tp_password_history;

-- 查看索引
-- SHOW INDEX FROM tp_password_history;

-- 查看表注释
-- SELECT TABLE_NAME, TABLE_COMMENT 
-- FROM INFORMATION_SCHEMA.TABLES 
-- WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'tp_password_history';
