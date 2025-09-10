-- 创建日志数据库 数据保存180天
CREATE DATABASE log_center REPLICA 1 QUORUM 1 DAYS 10 KEEP 180 BLOCKS 4;
-- 创建超级表 日志记录表
create table if not exists log_center.tp_log(ts timestamp, module_code nchar(50), operter_type nchar(50),operter_rid nchar(50),ascn_id nchar(50),city_code nchar(50),operter_browser nchar(500),operter_ip nchar(100),source nchar(1),extend01 nchar(1000),extend02 nchar(1000),extend03 nchar(1000)) TAGS (username nchar(50), category tinyint, app_name nchar(50), person_id nchar(50));

-- 添加字段示例
-- ALTER TABLE tp_log ADD COLUMN `extend03` NCHAR(500);