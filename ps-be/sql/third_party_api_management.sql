-- ========================================
-- 第三方API管理相关表设计
-- 创建时间: 2025-01-30
-- 描述: 用于管理第三方应用、API清单、权限关联和调用日志
-- ========================================

-- 删除已存在的表（如果存在）
DROP TABLE IF EXISTS `tp_api_call_log`;
DROP TABLE IF EXISTS `tp_app_api_permission`;
DROP TABLE IF EXISTS `tp_api_definition`;
DROP TABLE IF EXISTS `tp_third_party_app`;

-- ========================================
-- 1. 第三方应用表
-- ========================================
CREATE TABLE `tp_third_party_app` (
    `app_id` VARCHAR(19) NOT NULL COMMENT '应用ID（主键）',
    `app_name` VARCHAR(100) NOT NULL COMMENT '应用名称',
    `api_key` VARCHAR(64) NOT NULL COMMENT 'API密钥（UUID格式）',
    `status` INTEGER NOT NULL DEFAULT 1 COMMENT '状态（1:启用 0:禁用）',
    `expire_time` VARCHAR(14) DEFAULT NULL COMMENT '过期时间（yyyyMMddHHmmss），为NULL表示永不过期',
    `description` TEXT DEFAULT NULL COMMENT '应用描述',
    `contact_person` VARCHAR(50) DEFAULT NULL COMMENT '联系人',
    `contact_phone` VARCHAR(20) DEFAULT NULL COMMENT '联系电话',
    `contact_email` VARCHAR(100) DEFAULT NULL COMMENT '联系邮箱',
    `ip_whitelist` TEXT DEFAULT NULL COMMENT 'IP白名单（逗号分隔）',
    `rate_limit` INTEGER NOT NULL DEFAULT 100 COMMENT '限流配置（次/秒）',
    `last_call_time` VARCHAR(14) DEFAULT NULL COMMENT '最后调用时间（yyyyMMddHHmmss）',
    `actived` INTEGER NOT NULL DEFAULT 1 COMMENT '是否有效（1:有效 0:无效）',
    `creator` VARCHAR(19) NOT NULL COMMENT '创建人',
    `create_time` VARCHAR(14) NOT NULL COMMENT '创建时间',
    `updator` VARCHAR(19) DEFAULT NULL COMMENT '修改人',
    `update_time` VARCHAR(14) DEFAULT NULL COMMENT '修改时间',
    `tenant_id` VARCHAR(19) DEFAULT NULL COMMENT '租户ID',
    PRIMARY KEY (`app_id`),
    UNIQUE KEY `uk_app_name` (`app_name`),
    UNIQUE KEY `uk_api_key` (`api_key`),
    KEY `idx_status_actived` (`status`, `actived`),
    KEY `idx_expire_time` (`expire_time`),
    KEY `idx_last_call_time` (`last_call_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='第三方应用表';

-- ========================================
-- 2. API定义表
-- ========================================
CREATE TABLE `tp_api_definition` (
    `api_id` VARCHAR(19) NOT NULL COMMENT 'API ID（主键）',
    `api_code` VARCHAR(50) NOT NULL COMMENT 'API编码（唯一）',
    `api_name` VARCHAR(100) NOT NULL COMMENT 'API名称',
    `api_path` VARCHAR(200) NOT NULL COMMENT 'API路径（如:/open-api/v1/users/{id}）',
    `http_method` VARCHAR(10) NOT NULL COMMENT 'HTTP方法（GET/POST/PUT/DELETE等）',
    `category` VARCHAR(50) DEFAULT NULL COMMENT 'API分类',
    `description` TEXT DEFAULT NULL COMMENT 'API描述',
    `is_sensitive` INTEGER NOT NULL DEFAULT 0 COMMENT '是否敏感接口（1:是 0:否）',
    `require_secret` INTEGER NOT NULL DEFAULT 0 COMMENT '是否需要签名验证（1:是 0:否）',
    `status` INTEGER NOT NULL DEFAULT 1 COMMENT '状态（1:启用 0:禁用）',
    `order_index` DOUBLE DEFAULT 0 COMMENT '排序序号',
    `actived` INTEGER NOT NULL DEFAULT 1 COMMENT '是否有效（1:有效 0:无效）',
    `creator` VARCHAR(19) NOT NULL COMMENT '创建人',
    `create_time` VARCHAR(14) NOT NULL COMMENT '创建时间',
    `updator` VARCHAR(19) DEFAULT NULL COMMENT '修改人',
    `update_time` VARCHAR(14) DEFAULT NULL COMMENT '修改时间',
    PRIMARY KEY (`api_id`),
    UNIQUE KEY `uk_api_code` (`api_code`),
    KEY `idx_api_path_method` (`api_path`, `http_method`),
    KEY `idx_category_status` (`category`, `status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='API定义表';

-- ========================================
-- 3. 应用API权限关联表
-- ========================================
CREATE TABLE `tp_app_api_permission` (
    `permission_id` VARCHAR(19) NOT NULL COMMENT '权限ID（主键）',
    `app_id` VARCHAR(19) NOT NULL COMMENT '应用ID（外键）',
    `api_id` VARCHAR(19) NOT NULL COMMENT 'API ID（外键）',
    `creator` VARCHAR(19) NOT NULL COMMENT '创建人',
    `create_time` VARCHAR(14) NOT NULL COMMENT '创建时间',
    PRIMARY KEY (`permission_id`),
    UNIQUE KEY `uk_app_api` (`app_id`, `api_id`),
    KEY `idx_app_id` (`app_id`),
    KEY `idx_api_id` (`api_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='应用API权限关联表';

-- ========================================
-- 4. API调用日志表
-- ========================================
CREATE TABLE `tp_api_call_log` (
    `log_id` VARCHAR(19) NOT NULL COMMENT '日志ID（主键）',
    `app_id` VARCHAR(19) NOT NULL COMMENT '应用ID',
    `app_name` VARCHAR(100) DEFAULT NULL COMMENT '应用名称（冗余）',
    `api_id` VARCHAR(19) DEFAULT NULL COMMENT 'API ID',
    `api_path` VARCHAR(200) NOT NULL COMMENT '请求路径',
    `http_method` VARCHAR(10) NOT NULL COMMENT 'HTTP方法',
    `request_ip` VARCHAR(50) DEFAULT NULL COMMENT '请求IP',
    `request_params` TEXT DEFAULT NULL COMMENT '请求参数（脱敏）',
    `response_status` INTEGER NOT NULL COMMENT '响应状态码',
    `response_time` INTEGER DEFAULT NULL COMMENT '响应时间（毫秒）',
    `error_message` TEXT DEFAULT NULL COMMENT '错误信息',
    `call_time` VARCHAR(14) NOT NULL COMMENT '调用时间',
    `tenant_id` VARCHAR(19) DEFAULT NULL COMMENT '租户ID',
    PRIMARY KEY (`log_id`),
    KEY `idx_app_call_time` (`app_id`, `call_time`),
    KEY `idx_api_call_time` (`api_id`, `call_time`),
    KEY `idx_response_status` (`response_status`),
    KEY `idx_call_time` (`call_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='API调用日志表';

-- ========================================
-- 常用查询示例
-- ========================================

-- 1. 查询所有启用的第三方应用
-- SELECT * FROM tp_third_party_app WHERE status = 1 AND actived = 1 ORDER BY create_time DESC;

-- 2. 查询应用的所有授权API
-- SELECT a.*, d.api_name, d.api_path 
-- FROM tp_app_api_permission p
-- INNER JOIN tp_third_party_app a ON p.app_id = a.app_id
-- INNER JOIN tp_api_definition d ON p.api_id = d.api_id
-- WHERE p.app_id = '应用ID' AND a.actived = 1 AND d.actived = 1;

-- 3. 查询某个API的调用统计
-- SELECT 
--     app_name, 
--     COUNT(*) as call_count,
--     AVG(response_time) as avg_time,
--     SUM(CASE WHEN response_status = 200 THEN 1 ELSE 0 END) as success_count
-- FROM tp_api_call_log
-- WHERE api_id = 'API_ID' AND call_time >= '20250101000000'
-- GROUP BY app_name;

-- 4. 查询最近的调用日志
-- SELECT * FROM tp_api_call_log 
-- ORDER BY call_time DESC 
-- LIMIT 100;

-- ========================================
-- 表结构验证
-- ========================================

-- 查看表结构
-- DESCRIBE tp_third_party_app;
-- DESCRIBE tp_api_definition;
-- DESCRIBE tp_app_api_permission;
-- DESCRIBE tp_api_call_log;

-- 查看索引信息
-- SHOW INDEX FROM tp_third_party_app;
-- SHOW INDEX FROM tp_api_definition;
-- SHOW INDEX FROM tp_app_api_permission;
-- SHOW INDEX FROM tp_api_call_log;
