-- 系统配置表
CREATE TABLE `tp_system_config` (
  `config_key` varchar(100) NOT NULL COMMENT '配置键',
  `config_value` text COMMENT '配置值',
  `description` varchar(255) DEFAULT NULL COMMENT '配置描述',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`config_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统配置表';

-- 初始化基础配置数据
INSERT INTO `tp_system_config` (`config_key`, `config_value`, `description`) VALUES
('system.name', 'JPX3.0', '系统名称'),
('browser.title', 'JPX3.0 管理系统', '浏览器标题'),
('login.title', 'JPX3.0', '登录页标题'),
('login.logo', '/static/images/logo.svg', '登录页Logo地址'),
('login.copyright', 'JPX3.0 © 2025-2025 JPX3.0', '登录页版权信息'),
('login.background', '', '登录页背景图片地址'),
('admin.logo', '/static/images/logo.svg', '后台Logo地址'),
('admin.title', 'JPX3.0', '后台标题');