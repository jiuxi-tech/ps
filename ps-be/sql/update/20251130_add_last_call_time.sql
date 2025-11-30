-- ========================================
-- 添加最后调用时间字段
-- 创建时间: 2025-11-30
-- 描述: 为第三方应用表添加最后调用时间字段
-- ========================================

-- 检查字段是否存在，如果不存在则添加
ALTER TABLE `tp_third_party_app` 
ADD COLUMN IF NOT EXISTS `last_call_time` VARCHAR(14) DEFAULT NULL COMMENT '最后调用时间（yyyyMMddHHmmss）' 
AFTER `rate_limit`;

-- 创建索引以提高查询性能
CREATE INDEX IF NOT EXISTS `idx_last_call_time` ON `tp_third_party_app`(`last_call_time`);

-- 验证字段是否添加成功
-- DESCRIBE tp_third_party_app;
