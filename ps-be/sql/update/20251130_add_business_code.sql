-- ========================================
-- API调用日志表增加业务状态码字段
-- 创建时间: 2025-11-30
-- 描述: 分离HTTP状态码和业务状态码，增强日志审计能力
-- ========================================

-- 添加业务状态码字段
ALTER TABLE `tp_api_call_log` 
ADD COLUMN `business_code` INTEGER DEFAULT NULL COMMENT '业务状态码（1:成功, -1:失败, NULL:未返回）' AFTER `response_status`;

-- 添加索引以支持业务状态查询
CREATE INDEX `idx_business_code` ON `tp_api_call_log`(`business_code`);

-- ========================================
-- 使用说明
-- ========================================
-- business_code 字段说明：
-- 1: 业务处理成功
-- -1: 业务处理失败（如用户不存在、参数错误等）
-- NULL: 请求未到达业务层（如认证失败、权限不足）
