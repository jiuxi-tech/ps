-- 开发环境系统配置数据
-- 此文件基于生产环境数据修改为开发环境配置
-- 数据量与生产环境完全一致，仅配置值有区别

-- 清空现有数据（可选，根据需要决定是否包含）
 DELETE FROM `tp_system_config`;

-- 插入开发环境配置数据（保持与生产环境相同的记录数量和结构）
INSERT INTO `tp_system_config` (`config_key`, `config_value`, `config_type`, `description`, `create_time`, `update_time`) VALUES
('11', '222', 'string', '111', '2025-08-29 22:55:27', '2025-08-29 23:09:03'),
('22', 'aa', 'string', 'bb', '2025-08-29 23:07:08', '2025-08-29 23:10:04'),
('admin.logo', './logo/jpx-logo.png', 'string', '管理后台Logo地址', '2025-08-29 21:24:54', '2025-08-29 23:18:37'),
('admin.title', 'JPX 3.0', 'string', '管理后台标题', '2025-08-29 21:24:54', '2025-08-29 23:13:47'),
('browser.title', 'JPX 3.0 ', 'string', '浏览器标题', '2025-08-31 20:25:03', '2025-08-31 20:25:03'),
('login.logo', './logo/jpx-logo.png', 'string', '登录页面Logo地址', '2025-08-29 21:24:54', '2025-08-29 23:22:40'),
('login.title', 'JPX', 'string', '登录页面标题', '2025-08-29 21:24:54', '2025-08-29 21:51:17'),
('password.expiry.reminder.days', '30', 'string', '密码到期提醒天数', '2025-08-30 00:07:40', '2025-08-30 00:07:40'),
('password.validity.months', '3', 'string', '密码有效期（月）', '2025-08-30 00:07:40', '2025-08-30 00:07:40'),
('person.audit.enabled', 'false', 'boolean', '是否启用个人信息修改审核功能', '2025-08-31 17:39:31', '2025-08-31 17:39:31'),
('person.audit.fields', 'sex,idcard', 'array', '需要审核的个人信息字段列表，逗号分隔', '2025-08-31 17:39:31', '2025-09-01 02:14:24'),
('security.ip.blacklist', '', 'string', 'IP黑名单', '2025-08-30 06:18:45', '2025-08-30 16:52:39'),
('security.ip.deny.message', '您的IP地址不在允许访问范围内，请联系管理员', 'string', 'IP访问拒绝消息', '2025-08-30 06:18:45', '2025-08-30 16:52:39'),
('security.ip.enabled', 'false', 'string', 'IP访问控制开关', '2025-08-30 06:18:45', '2025-08-30 16:52:38'),
('security.ip.log.enabled', 'true', 'string', 'IP访问日志开关', '2025-08-30 06:18:45', '2025-08-30 16:52:39'),
('security.ip.whitelist', '*', 'string', 'IP白名单', '2025-08-30 06:18:45', '2025-08-30 16:52:38'),
('security.user.access.admin.bypass', 'true', 'string', '管理员是否绕过访问控制，true绕过，false不绕过', '2025-08-30 18:46:40', '2025-08-30 18:46:40'),
('security.user.access.default.policy', 'allow', 'string', '默认访问策略，allow允许所有，deny拒绝所有', '2025-08-30 18:46:40', '2025-08-30 18:46:40'),
('security.user.access.deny.message', '您没有访问权限，请联系系统管理员', 'string', '用户访问被拒绝时的提示信息', '2025-08-30 18:46:40', '2025-08-30 18:46:40'),
('security.user.access.enabled', 'false', 'string', '用户访问控制开关，true启用，false禁用', '2025-08-30 18:46:40', '2025-08-30 18:46:40'),
('security.user.access.log.enabled', 'true', 'string', '是否记录用户访问日志，true启用，false禁用', '2025-08-30 18:46:40', '2025-08-30 18:46:40'),
('security.user.access.rules', '# 管理员访问规则\n#allow role:ADMIN\n#allow user:admin\n\n# 普通用户访问规则\n#allow role:USER AND dept:IT部门\n#allow role:MANAGER AND time:09:00-18:00\n\n# 拒绝规则（优先级最高）\n#deny user:guest\n#deny role:TEMP\n\n# 组合规则示例\n#allow (role:USER OR role:MANAGER) AND dept:销售部门 AND time:08:00-20:00', 'string', '用户访问控制规则，支持用户、角色、部门、时间等多种规则', '2025-08-30 18:46:40', '2025-08-30 18:58:55'),
('system.copyright', '© 2025 JPX 版权所有', 'string', '版权信息', '2025-08-29 21:24:54', '2025-08-29 21:57:52'),
('system.language', 'zh-CN', 'string', '系统语言', '2025-08-29 21:24:54', '2025-08-29 21:24:54'),
('system.name', '管理系统', 'string', '系统名称', '2025-08-29 21:24:54', '2025-08-29 22:09:38'),
('system.theme', 'default', 'string', '系统主题', '2025-08-29 21:24:54', '2025-08-29 21:24:54'),
('system.timezone', 'Asia/Shanghai', 'string', '系统时区', '2025-08-29 21:24:54', '2025-08-29 21:24:54'),
('system.version', '1.0.0', 'string', '系统版本', '2025-08-29 21:24:54', '2025-08-29 21:24:54'),
('test.config', 'test value111', 'string', 'Test configuration', '2025-08-29 21:30:49', '2025-08-29 23:05:33');

-- 添加ON DUPLICATE KEY UPDATE语句，确保数据可以重复执行
-- ON DUPLICATE KEY UPDATE `config_value` = VALUES(`config_value`), `update_time` = NOW();