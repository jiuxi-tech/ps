-- =========================================
-- SSO 系统配置检查和修复脚本
-- 创建日期: 2025-12-03
-- 用途: 确保 SSO 登录所需的系统配置完整
-- =========================================

-- 1. 检查现有配置
SELECT 
    config_key,
    config_value,
    config_type,
    description,
    create_time,
    update_time
FROM tp_system_config
WHERE config_key IN (
    'sso.keycloak.server.url',
    'sso.keycloak.realm',
    'sso.keycloak.client.id',
    'sso.keycloak.redirect.uri',
    'sso.keycloak.callback.url',
    'sso.keycloak.logout.url'
)
ORDER BY config_key;

-- 2. 插入或更新 SSO 配置
-- 如果配置已存在则更新，不存在则插入

-- Keycloak 服务器地址
INSERT INTO tp_system_config (config_key, config_value, config_type, description, create_time, update_time)
VALUES ('sso.keycloak.server.url', 'https://sso.shxdx.com', 'string', 'Keycloak 服务器地址', NOW(), NOW())
ON DUPLICATE KEY UPDATE 
    config_value = 'https://sso.shxdx.com',
    description = 'Keycloak 服务器地址',
    update_time = NOW();

-- Keycloak Realm 名称
INSERT INTO tp_system_config (config_key, config_value, config_type, description, create_time, update_time)
VALUES ('sso.keycloak.realm', 'ps-realm', 'string', 'Keycloak Realm 名称', NOW(), NOW())
ON DUPLICATE KEY UPDATE 
    config_value = 'ps-realm',
    description = 'Keycloak Realm 名称',
    update_time = NOW();

-- Keycloak 客户端 ID
INSERT INTO tp_system_config (config_key, config_value, config_type, description, create_time, update_time)
VALUES ('sso.keycloak.client.id', 'ps-be', 'string', 'Keycloak 客户端 ID', NOW(), NOW())
ON DUPLICATE KEY UPDATE 
    config_value = 'ps-be',
    description = 'Keycloak 客户端 ID',
    update_time = NOW();

-- SSO 回调地址（重要：这个地址必须与 Keycloak 客户端配置中的回调地址一致）
INSERT INTO tp_system_config (config_key, config_value, config_type, description, create_time, update_time)
VALUES ('sso.keycloak.redirect.uri', 'https://mid.shxdx.com/ps-be/api/sso/callback', 'string', 'SSO 登录回调地址（后端接口）', NOW(), NOW())
ON DUPLICATE KEY UPDATE 
    config_value = 'https://mid.shxdx.com/ps-be/api/sso/callback',
    description = 'SSO 登录回调地址（后端接口）',
    update_time = NOW();

-- 前端基础 URL（用于 sso.html 中转页面）
INSERT INTO tp_system_config (config_key, config_value, config_type, description, create_time, update_time)
VALUES ('sso.keycloak.callback.url', 'https://mid.shxdx.com', 'string', '前端基础URL（用于SSO中转页面）', NOW(), NOW())
ON DUPLICATE KEY UPDATE 
    config_value = 'https://mid.shxdx.com',
    description = '前端基础URL（用于SSO中转页面）',
    update_time = NOW();

-- SSO 注销后重定向地址（post_logout_redirect_uri）
INSERT INTO tp_system_config (config_key, config_value, config_type, description, create_time, update_time)
VALUES ('sso.keycloak.logout.url', 'https://mid.shxdx.com/#/login', 'string', 'SSO注销后重定向地址', NOW(), NOW())
ON DUPLICATE KEY UPDATE 
    config_value = 'https://mid.shxdx.com/#/login',
    description = 'SSO注销后重定向地址',
    update_time = NOW();

-- 3. 验证配置是否正确插入
SELECT 
    '配置检查' AS check_type,
    COUNT(*) AS config_count,
    CASE 
        WHEN COUNT(*) = 6 THEN '✓ 配置完整'
        ELSE '✗ 配置不完整'
    END AS status
FROM tp_system_config
WHERE config_key IN (
    'sso.keycloak.server.url',
    'sso.keycloak.realm',
    'sso.keycloak.client.id',
    'sso.keycloak.redirect.uri',
    'sso.keycloak.callback.url',
    'sso.keycloak.logout.url'
);

-- 4. 显示最终配置（用于确认）
SELECT 
    config_key AS '配置项',
    config_value AS '配置值',
    config_type AS '类型',
    description AS '说明',
    update_time AS '更新时间'
FROM tp_system_config
WHERE config_key IN (
    'sso.keycloak.server.url',
    'sso.keycloak.realm',
    'sso.keycloak.client.id',
    'sso.keycloak.redirect.uri',
    'sso.keycloak.callback.url',
    'sso.keycloak.logout.url'
)
ORDER BY config_key;

-- =========================================
-- 重要提示
-- =========================================
-- 1. sso.keycloak.redirect.uri 必须与 Keycloak 客户端配置中的"有效的重定向 URI"一致
-- 2. 当前设置为: https://mid.shxdx.com/ps-be/api/sso/callback
-- 3. 请确保 Keycloak 客户端 ps-be 的配置中包含此 URI
-- 4. sso.keycloak.callback.url 用于构建 sso.html 中转页面的 URL
-- 5. sso.keycloak.logout.url 用于 SSO 注销后的重定向地址（post_logout_redirect_uri）
-- 6. 如果修改了此配置，需要同步更新 Keycloak 客户端配置
-- 7. 修改配置后无需重启应用，配置会即时生效

-- =========================================
-- Keycloak 客户端配置检查清单
-- =========================================
-- 请在 Keycloak 管理控制台确认以下配置：
--
-- 客户端 ID: ps-be
-- 客户端协议: openid-connect
-- 访问类型: confidential
-- 
-- 有效的重定向 URI（必须包含）:
--   - https://mid.shxdx.com/ps-be/api/sso/callback
--   - http://192.168.0.139/*  (如果还需要内网访问)
--
-- Web Origins（必须包含）:
--   - https://mid.shxdx.com/*
--   - http://192.168.0.139/*  (如果还需要内网访问)
--
-- 客户端密钥: 
--   - 请确保与后端配置文件 security-dev.yml 中的 client-secret 一致
