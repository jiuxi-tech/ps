-- ========================================
-- API调用日志表重构 - V2方案
-- 创建时间: 2025-11-30
-- 描述: response_status 字段改为存储业务状态码（不添加 business_code 字段）
-- ========================================

-- 方案说明：
-- response_status 字段直接存储业务状态码，统一表示请求处理结果
-- 
-- 状态码含义：
-- 1:    业务处理成功
-- -1:   业务处理失败（如用户不存在、参数错误等）
-- 401:  认证失败（API Key 无效、缺失）
-- 403:  权限不足（应用禁用、过期、IP受限、无API权限）
-- 500:  服务器内部错误
-- 503:  服务不可用

-- ========================================
-- 如果之前执行了 V1 方案（添加了 business_code 字段），需要回滚
-- ========================================

-- 删除 business_code 字段的索引（如果存在）
DROP INDEX IF EXISTS `idx_business_code` ON `tp_api_call_log`;

-- 删除 business_code 字段（如果存在）
ALTER TABLE `tp_api_call_log` 
DROP COLUMN IF EXISTS `business_code`;

-- ========================================
-- 更新 response_status 字段注释
-- ========================================
ALTER TABLE `tp_api_call_log` 
MODIFY COLUMN `response_status` INTEGER DEFAULT NULL 
COMMENT '业务状态码（1:成功, -1:失败, 401:认证失败, 403:权限不足, 500:服务器错误）';

-- ========================================
-- 验证修改结果
-- ========================================
-- DESCRIBE tp_api_call_log;
