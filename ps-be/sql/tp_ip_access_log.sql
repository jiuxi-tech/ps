-- IP访问控制日志表
CREATE TABLE `tp_ip_access_log` (
  `LOG_ID` varchar(32) NOT NULL COMMENT '日志ID',
  `CLIENT_IP` varchar(50) NOT NULL COMMENT '客户端IP地址',
  `ACCESS_TIME` datetime NOT NULL COMMENT '访问时间',
  `ACCESS_RESULT` varchar(20) NOT NULL COMMENT '访问结果(ALLOWED:允许, DENIED:拒绝)',
  `DENY_REASON` varchar(100) DEFAULT NULL COMMENT '拒绝原因(BLACKLIST:黑名单, NOT_IN_WHITELIST:不在白名单)',
  `REQUEST_URI` varchar(500) DEFAULT NULL COMMENT '请求URI',
  `REQUEST_METHOD` varchar(10) DEFAULT NULL COMMENT '请求方法(GET,POST等)',
  `USER_AGENT` varchar(1000) DEFAULT NULL COMMENT '用户代理信息',
  `USERNAME` varchar(100) DEFAULT NULL COMMENT '用户名(如果已登录)',
  `MATCHED_RULE` varchar(200) DEFAULT NULL COMMENT '匹配的规则(具体的IP规则)',
  `RULE_TYPE` varchar(20) DEFAULT NULL COMMENT '规则类型(WHITELIST:白名单, BLACKLIST:黑名单)',
  `CITY_CODE` varchar(20) DEFAULT NULL COMMENT '城市代码',
  `ACTIVED` varchar(1) DEFAULT '1' COMMENT '是否激活(1:激活 0:未激活)',
  `CREATE_TIME` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `EXTEND01` varchar(200) DEFAULT NULL COMMENT '扩展字段1',
  `EXTEND02` varchar(200) DEFAULT NULL COMMENT '扩展字段2',
  `EXTEND03` varchar(200) DEFAULT NULL COMMENT '扩展字段3',
  PRIMARY KEY (`LOG_ID`),
  KEY `idx_client_ip` (`CLIENT_IP`),
  KEY `idx_access_time` (`ACCESS_TIME`),
  KEY `idx_access_result` (`ACCESS_RESULT`),
  KEY `idx_username` (`USERNAME`),
  KEY `idx_city_code` (`CITY_CODE`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='IP访问控制日志表';

-- 创建索引以提高查询性能
CREATE INDEX `idx_ip_time` ON `tp_ip_access_log` (`CLIENT_IP`, `ACCESS_TIME`);
CREATE INDEX `idx_result_time` ON `tp_ip_access_log` (`ACCESS_RESULT`, `ACCESS_TIME`);

-- 插入示例数据
INSERT INTO `tp_ip_access_log` (
  `LOG_ID`, `CLIENT_IP`, `ACCESS_TIME`, `ACCESS_RESULT`, `DENY_REASON`, 
  `REQUEST_URI`, `REQUEST_METHOD`, `USER_AGENT`, `USERNAME`, 
  `MATCHED_RULE`, `RULE_TYPE`, `CITY_CODE`, `ACTIVED`
) VALUES 
(
  'IP_LOG_001', '192.168.1.100', NOW(), 'ALLOWED', NULL, 
  '/platform/login', 'POST', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64)', 'admin', 
  '192.168.1.*', 'WHITELIST', '110000', '1'
),
(
  'IP_LOG_002', '10.0.0.50', NOW(), 'DENIED', 'BLACKLIST', 
  '/platform/login', 'POST', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64)', NULL, 
  '10.0.0.0/24', 'BLACKLIST', '110000', '1'
),
(
  'IP_LOG_003', '172.16.1.200', NOW(), 'DENIED', 'NOT_IN_WHITELIST', 
  '/api/user/info', 'GET', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7)', NULL, 
  NULL, NULL, '110000', '1'
);

-- 查询示例
-- 1. 查询最近24小时的访问日志
-- SELECT * FROM tp_ip_access_log 
-- WHERE ACCESS_TIME >= DATE_SUB(NOW(), INTERVAL 24 HOUR) 
-- ORDER BY ACCESS_TIME DESC;

-- 2. 查询被拒绝的访问记录
-- SELECT CLIENT_IP, ACCESS_TIME, DENY_REASON, REQUEST_URI 
-- FROM tp_ip_access_log 
-- WHERE ACCESS_RESULT = 'DENIED' 
-- ORDER BY ACCESS_TIME DESC;

-- 3. 统计每个IP的访问次数
-- SELECT CLIENT_IP, COUNT(*) as access_count, 
--        SUM(CASE WHEN ACCESS_RESULT = 'ALLOWED' THEN 1 ELSE 0 END) as allowed_count,
--        SUM(CASE WHEN ACCESS_RESULT = 'DENIED' THEN 1 ELSE 0 END) as denied_count
-- FROM tp_ip_access_log 
-- WHERE ACCESS_TIME >= DATE_SUB(NOW(), INTERVAL 7 DAY)
-- GROUP BY CLIENT_IP 
-- ORDER BY access_count DESC;

-- 4. 查询特定时间段内的访问统计
-- SELECT DATE(ACCESS_TIME) as access_date, 
--        COUNT(*) as total_access,
--        SUM(CASE WHEN ACCESS_RESULT = 'ALLOWED' THEN 1 ELSE 0 END) as allowed_access,
--        SUM(CASE WHEN ACCESS_RESULT = 'DENIED' THEN 1 ELSE 0 END) as denied_access
-- FROM tp_ip_access_log 
-- WHERE ACCESS_TIME >= DATE_SUB(NOW(), INTERVAL 30 DAY)
-- GROUP BY DATE(ACCESS_TIME) 
-- ORDER BY access_date DESC;