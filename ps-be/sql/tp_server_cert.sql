-- 服务器证书管理表
CREATE TABLE IF NOT EXISTS `tp_server_cert` (
  `cert_id` varchar(36) NOT NULL COMMENT '证书ID',
  `cert_name` varchar(100) NOT NULL COMMENT '证书名称',
  `cert_desc` varchar(500) DEFAULT NULL COMMENT '证书描述',
  `pem_content` longtext NOT NULL COMMENT 'PEM证书文件内容（BASE64编码）',
  `key_content` longtext NOT NULL COMMENT '私钥文件内容（BASE64编码）',
  `domain_names` varchar(1000) DEFAULT NULL COMMENT '绑定域名（JSON数组字符串）',
  `issuer` varchar(200) DEFAULT NULL COMMENT '发证机构',
  `subject_cn` varchar(200) DEFAULT NULL COMMENT '公用名(CN)',
  `subject_o` varchar(200) DEFAULT NULL COMMENT '组织(O)',
  `subject_ou` varchar(200) DEFAULT NULL COMMENT '组织单位(OU)',
  `issue_date` datetime DEFAULT NULL COMMENT '颁发日期',
  `expire_date` datetime DEFAULT NULL COMMENT '到期日期',
  `status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '状态：0-未应用，1-已应用',
  `is_in_use` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否正在使用：0-否，1-是',
  `is_expired` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否过期：0-否，1-是',
  `applied_time` datetime DEFAULT NULL COMMENT '应用时间',
  `create_person_id` varchar(36) DEFAULT NULL COMMENT '创建人ID',
  `create_person_name` varchar(50) DEFAULT NULL COMMENT '创建人姓名',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_person_id` varchar(36) DEFAULT NULL COMMENT '更新人ID',
  `update_person_name` varchar(50) DEFAULT NULL COMMENT '更新人姓名',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `actived` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否有效：0-删除，1-有效',
  PRIMARY KEY (`cert_id`),
  KEY `idx_cert_name` (`cert_name`),
  KEY `idx_status` (`status`),
  KEY `idx_create_time` (`create_time`),
  KEY `idx_expire_date` (`expire_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='服务器证书管理表';

-- 插入nginx相关系统配置
INSERT INTO `tp_system_config` (`config_key`, `config_value`, `description`, `create_time`, `update_time`) 
VALUES 
('nginx_cert_dir', '/etc/nginx/ssl/', 'Nginx证书文件存放目录', NOW(), NOW()),
('nginx_cert_name', 'server.crt', 'Nginx证书文件名', NOW(), NOW()),
('nginx_cert_key', 'server.key', 'Nginx私钥文件名', NOW(), NOW()),
('nginx_restart_cmd', 'systemctl restart nginx', 'Nginx重启命令', NOW(), NOW())
ON DUPLICATE KEY UPDATE 
  `config_value` = VALUES(`config_value`),
  `update_time` = NOW();