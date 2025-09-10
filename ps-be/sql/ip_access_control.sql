-- IP访问控制配置项
-- 添加到系统配置表的SQL脚本

-- 插入IP访问控制相关配置项
INSERT INTO `tp_system_config` (`config_key`, `config_value`, `description`) VALUES
('security.ip.whitelist', '*', 'IP白名单，多个IP用分号分隔，支持通配符和CIDR表示法，*表示允许所有IP'),
('security.ip.blacklist', '', 'IP黑名单，多个IP用分号分隔，支持通配符和CIDR表示法，优先级高于白名单'),
('security.ip.enabled', 'true', 'IP访问控制开关，true启用，false禁用'),
('security.ip.log.enabled', 'true', '是否记录IP访问日志，true启用，false禁用'),
('security.ip.deny.message', '您的IP地址不在允许访问范围内，请联系管理员', 'IP访问被拒绝时的提示信息')
ON DUPLICATE KEY UPDATE 
    config_value = VALUES(config_value),
    description = VALUES(description),
    update_time = CURRENT_TIMESTAMP;

-- 示例配置说明：
-- 白名单示例：
-- '192.168.1.100;192.168.1.0/24;10.0.*.1;172.16.0.1-172.16.0.100'
-- 
-- 黑名单示例：
-- '192.168.1.50;10.0.0.0/8;172.16.1.*'
--
-- 支持的格式：
-- 1. 精确IP：192.168.1.100
-- 2. CIDR表示法：192.168.1.0/24
-- 3. 通配符：192.168.1.*
-- 4. IP范围：192.168.1.1-192.168.1.100
-- 5. 多个规则用分号分隔