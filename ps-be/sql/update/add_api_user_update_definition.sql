-- ========================================
-- 添加用户管理相关的开放API接口定义
-- 创建时间: 2025-12-16
-- 描述: 新增用户信息修改、密码重置、SSO同步三个开放API接口
-- ========================================

-- 检查并插入API定义（幂等性脚本）

-- 1. 用户信息修改接口
INSERT INTO `tp_api_definition` (
    `api_id`,
    `api_code`,
    `api_name`,
    `api_path`,
    `http_method`,
    `category`,
    `description`,
    `is_sensitive`,
    `require_secret`,
    `status`,
    `order_index`,
    `actived`,
    `creator`,
    `create_time`
)
SELECT 
    '6',
    'api_user_update',
    '用户信息修改',
    '/open-api/v1/users/{personId}',
    'PUT',
    '用户管理',
    '通过人员ID修改用户基本信息，支持修改姓名、手机号、邮箱、固定电话等字段',
    1,
    0,
    1,
    4.0,
    1,
    'system',
    DATE_FORMAT(NOW(), '%Y%m%d%H%i%s')
FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM `tp_api_definition` WHERE `api_code` = 'api_user_update'
);

-- 2. 用户密码重置接口
INSERT INTO `tp_api_definition` (
    `api_id`,
    `api_code`,
    `api_name`,
    `api_path`,
    `http_method`,
    `category`,
    `description`,
    `is_sensitive`,
    `require_secret`,
    `status`,
    `order_index`,
    `actived`,
    `creator`,
    `create_time`
)
SELECT 
    '7',
    'api_user_password_reset',
    '用户密码重置',
    '/open-api/v1/users/{personId}/reset-password',
    'PUT',
    '用户管理',
    '通过人员ID重置用户登录密码，支持Keycloak多凭据（用户名、手机号、身份证号）密码同步',
    1,
    0,
    1,
    5.0,
    1,
    'system',
    DATE_FORMAT(NOW(), '%Y%m%d%H%i%s')
FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM `tp_api_definition` WHERE `api_code` = 'api_user_password_reset'
);

-- 3. 用户SSO同步接口
INSERT INTO `tp_api_definition` (
    `api_id`,
    `api_code`,
    `api_name`,
    `api_path`,
    `http_method`,
    `category`,
    `description`,
    `is_sensitive`,
    `require_secret`,
    `status`,
    `order_index`,
    `actived`,
    `creator`,
    `create_time`
)
SELECT 
    '8',
    'api_user_sso_sync',
    '用户SSO同步',
    '/open-api/v1/users/{personId}/sync-sso',
    'POST',
    '用户管理',
    '同步用户账号到Keycloak SSO系统，支持多凭据（USERNAME、PHONE、IDCARD）同步，等同于账号管理的"同步SSO"功能',
    1,
    0,
    1,
    6.0,
    1,
    'system',
    DATE_FORMAT(NOW(), '%Y%m%d%H%i%s')
FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM `tp_api_definition` WHERE `api_code` = 'api_user_sso_sync'
);

-- 验证插入结果
SELECT 
    api_id,
    api_code,
    api_name,
    api_path,
    http_method,
    category,
    is_sensitive,
    status,
    order_index,
    create_time
FROM `tp_api_definition`
WHERE api_code IN ('api_user_update', 'api_user_password_reset', 'api_user_sso_sync')
ORDER BY order_index;

-- 预期结果：返回3条记录
-- api_user_update | 用户信息修改 | /open-api/v1/users/{personId} | PUT | 用户管理
-- api_user_password_reset | 用户密码重置 | /open-api/v1/users/{personId}/reset-password | PUT | 用户管理
-- api_user_sso_sync | 用户SSO同步 | /open-api/v1/users/{personId}/sync-sso | POST | 用户管理
