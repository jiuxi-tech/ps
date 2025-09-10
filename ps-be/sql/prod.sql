-- ========================================
-- 系统配置表完整导出
-- 导出时间: 2025-01-31
-- 表名: tp_system_config
-- 描述: 包含表结构定义和现有数据
-- ========================================

-- 1. 表结构定义
CREATE TABLE IF NOT EXISTS `tp_system_config` (
  `config_key` varchar(100) NOT NULL COMMENT '配置键',
  `config_value` text COMMENT '配置值',
  `config_type` varchar(50) DEFAULT 'string' COMMENT '配置类型',
  `description` varchar(500) COMMENT '配置描述',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`config_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统配置表';

-- 2. 现有数据导出
-- 清空现有数据（可选）
 DELETE FROM `tp_system_config`;

-- 插入现有配置数据
INSERT INTO `tp_system_config` (`config_key`, `config_value`, `config_type`, `description`, `create_time`, `update_time`) VALUES
('11', '222', 'string', '111', '2025-08-29 22:55:27', '2025-08-29 23:09:03'),
('22', 'aa', 'string', 'bb', '2025-08-29 23:07:08', '2025-08-29 23:10:04'),
('admin.logo', './logo/admin-logo.png', 'string', '管理后台Logo地址', '2025-08-29 21:24:54', '2025-08-29 23:18:37'),
('admin.title', '陕西行政管理学院 业务中台', 'string', '管理后台标题', '2025-08-29 21:24:54', '2025-08-29 23:13:47'),
('browser.title', '陕西行政学院', 'string', '浏览器标题', '2025-08-31 20:25:03', '2025-08-31 20:25:03'),
('login.logo', './logo/admin-logo.png', 'string', '登录页面Logo地址', '2025-08-29 21:24:54', '2025-08-29 23:22:40'),
('login.title', '陕西行政管理学院业务中台', 'string', '登录页面标题', '2025-08-29 21:24:54', '2025-09-01 21:48:04'),
('password.expiry.reminder.days', '30', 'string', '密码到期提醒天数', '2025-08-30 00:07:40', '2025-08-30 00:07:40'),
('password.validity.months', '3', 'string', '密码有效期（月）', '2025-08-30 00:07:40', '2025-08-30 00:07:40'),
('person.audit.enabled', 'true', 'boolean', '是否启用个人信息修改审核功能', '2025-08-31 17:39:31', '2025-08-31 17:39:31'),
('person.audit.fields', 'sex,idcard', 'array', '需要审核的个人信息字段列表，逗号分隔', '2025-08-31 17:39:31', '2025-09-01 02:14:24'),
('security.ip.blacklist', '', 'string', 'IP黑名单', '2025-08-30 06:18:45', '2025-08-30 16:52:39'),
('security.ip.deny.message', '您的IP地址不在允许访问范围内，请联系管理员 测试', 'string', 'IP访问拒绝消息', '2025-08-30 06:18:45', '2025-08-30 16:52:39'),
('security.ip.enabled', 'false', 'string', 'IP访问控制开关', '2025-08-30 06:18:45', '2025-08-30 16:52:38'),
('security.ip.log.enabled', 'true', 'string', 'IP访问日志开关', '2025-08-30 06:18:45', '2025-08-30 16:52:39'),
('security.ip.whitelist', '*', 'string', 'IP白名单', '2025-08-30 06:18:45', '2025-08-30 16:52:38'),
('security.user.access.admin.bypass', 'true', 'string', '管理员是否绕过访问控制，true绕过，false不绕过', '2025-08-30 18:46:40', '2025-08-30 18:46:40'),
('security.user.access.default.policy', 'deny', 'string', '默认访问策略，allow允许所有，deny拒绝所有', '2025-08-30 18:46:40', '2025-08-30 18:46:40'),
('security.user.access.deny.message', '您没有访问权限，请联系系统管理员', 'string', '用户访问被拒绝时的提示信息', '2025-08-30 18:46:40', '2025-08-30 18:46:40'),
('security.user.access.enabled', 'false', 'string', '用户访问控制开关，true启用，false禁用', '2025-08-30 18:46:40', '2025-08-30 18:46:40'),
('security.user.access.log.enabled', 'true', 'string', '是否记录用户访问日志，true启用，false禁用', '2025-08-30 18:46:40', '2025-08-30 18:46:40'),
('security.user.access.rules', '# 管理员访问规则\n#allow role:ADMIN\n#allow user:admin\n\n# 普通用户访问规则\n#allow role:USER AND dept:IT部门\n#allow role:MANAGER AND time:09:00-18:00\n\n# 拒绝规则（优先级最高）\n#deny user:guest\n#deny role:TEMP\n\n# 组合规则示例\n#allow (role:USER OR role:MANAGER) AND dept:销售部门 AND time:08:00-20:00', 'string', '用户访问控制规则，支持用户、角色、部门、时间等多种规则', '2025-08-30 18:46:40', '2025-08-30 18:58:55'),
('system.copyright', '© 2025 陕西行政管理学院  版权所有', 'string', '版权信息', '2025-08-29 21:24:54', '2025-09-01 21:48:32'),
('system.language', 'zh-CN', 'string', '系统语言', '2025-08-29 21:24:54', '2025-08-29 21:24:54'),
('system.name', '陕西行政管理学院业务中台', 'string', '系统名称', '2025-08-29 21:24:54', '2025-09-01 21:48:53'),
('system.theme', 'default', 'string', '系统主题', '2025-08-29 21:24:54', '2025-08-29 21:24:54'),
('system.timezone', 'Asia/Shanghai', 'string', '系统时区', '2025-08-29 21:24:54', '2025-08-29 21:24:54'),
('system.version', '1.0.0', 'string', '系统版本', '2025-08-29 21:24:54', '2025-08-29 21:24:54'),
('test.config', 'test value111', 'string', 'Test configuration', '2025-08-29 21:30:49', '2025-08-29 23:05:33')
ON DUPLICATE KEY UPDATE 
    config_value = VALUES(config_value),
    config_type = VALUES(config_type),
    description = VALUES(description),
    update_time = CURRENT_TIMESTAMP;

-- ========================================
-- 配置项说明
-- ========================================

/*
主要配置分类：

1. 系统基础配置
   - system.name: 系统名称
   - system.version: 系统版本
   - system.language: 系统语言
   - system.timezone: 系统时区
   - system.theme: 系统主题
   - system.copyright: 版权信息

2. 界面配置
   - browser.title: 浏览器标题
   - admin.title: 管理后台标题
   - admin.logo: 管理后台Logo地址
   - login.title: 登录页面标题
   - login.logo: 登录页面Logo地址

3. 安全配置
   - password.validity.months: 密码有效期（月）
   - password.expiry.reminder.days: 密码到期提醒天数
   
4. IP访问控制
   - security.ip.enabled: IP访问控制开关
   - security.ip.whitelist: IP白名单
   - security.ip.blacklist: IP黑名单
   - security.ip.log.enabled: IP访问日志开关
   - security.ip.deny.message: IP访问拒绝消息

5. 用户访问控制
   - security.user.access.enabled: 用户访问控制开关
   - security.user.access.default.policy: 默认访问策略
   - security.user.access.admin.bypass: 管理员绕过控制
   - security.user.access.rules: 访问控制规则
   - security.user.access.log.enabled: 用户访问日志开关
   - security.user.access.deny.message: 用户访问拒绝消息

6. 个人信息审核
   - person.audit.enabled: 个人信息修改审核功能开关
   - person.audit.fields: 需要审核的字段列表

7. 测试配置
   - test.config: 测试配置项
   - 11, 22: 测试用配置
*/