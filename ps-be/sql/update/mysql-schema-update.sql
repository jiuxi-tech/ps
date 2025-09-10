-- 企业信息表
alter table tp_ent_basicinfo add column extend04 varchar(150) comment '扩展字段04' after extend03 ;
alter table tp_ent_basicinfo add column extend05 varchar(150) comment '扩展字段05' after extend04;
alter table tp_ent_basicinfo add column ent_domain varchar(50) comment '企业领域（业务字段，字典编码：HUHY）' after safety_director_tel;
alter table tp_ent_basicinfo add column ent_label varchar(50) comment '企业标签（业务字段，字典编码：JP7）'  after ent_domain;
alter table tp_ent_basicinfo add column state_code varchar(50) comment '营业状态（业务字段，字典编码：E08）'  after ent_label;
alter table tp_ent_basicinfo add column production_mode varchar(50) comment '生产经营方式（业务字段，字典编码：E30）'  after state_code;
alter table tp_ent_basicinfo add column administrative_sub_ordination varchar(50) comment '行政隶属关系（业务字段，字典编码：E06）'  after production_mode;
alter table tp_ent_basicinfo add column registration_type varchar(50) comment '国民经济类型（业务字段，字典编码：HYZ）'  after administrative_sub_ordination;
alter table tp_ent_basicinfo add column op_scope varchar(50) comment '主营业务（业务字段）'  after registration_type;

-- 添加唯一索引
alter table tp_city drop index IDX_CITY_CODE;
alter table tp_account drop index IDX_USERNAME;
alter table tp_ent_basicinfo drop index IDX_ENT_UNIFIED_CODE;
alter table tp_dept_basicinfo drop index IDX_DEPT_LEVELCODE;
alter table tp_dictionary drop index IDX_DIC_CODE;

alter table tp_city add unique key UK_tp_city_CODE(CITY_CODE);
alter table tp_account add unique key UK_TP_USERNAME(USERNAME);
alter table tp_ent_basicinfo add unique key UK_TP_ENT_UNIFIED_CODE(ENT_UNIFIED_CODE);
alter table tp_dept_basicinfo add unique key UK_TP_DEPT_LEVELCODE(DEPT_LEVELCODE);
alter table tp_dictionary add unique key UK_TP_DIC_CODE(DIC_CODE);

/*	建唯一索引时可能失败，需删除多余数据，下面给出sql，方便运维人员排查。
select CITY_CODE,count(1) from tp_city group by CITY_CODE HAVING count(1)>1;
select USERNAME,count(1) from tp_account group by USERNAME HAVING count(1)>1;
select DEPT_LEVELCODE,count(1) from tp_dept_basicinfo group by DEPT_LEVELCODE HAVING count(1)>1;
select DIC_CODE,count(1) from tp_dictionary group by DIC_CODE HAVING count(1)>1;
*/

-- 修改字段长度
alter table tp_person_basicinfo modify column phone varchar(50) comment '手机号码';
alter table tp_account modify column phone varchar(50) comment '手机号码';
-- 修改配置参数表
update tp_parameter_config set ENABLED = 1 where ENABLED is null;
update tp_parameter_config set ACTIVED = 1 where ACTIVED is null;
alter table tp_parameter_config modify column ENABLED int not null default '1' comment '是否启用';
alter table tp_parameter_config modify column ACTIVED int not null default 1 comment '是否有效';
alter table tp_parameter_config modify column PM_KEY varchar(100) not null comment '参数key';
alter table tp_parameter_config add unique index uk_tp_pm_key(PM_KEY) comment '参数key唯一索引';

-- 菜单表添加字段
alter table tp_menu add column MENU_TREE_PID varchar(19)  not null comment '菜单树父级id' after menu_pid;
update tp_menu set menu_tree_pid = menu_pid where menu_tree_pid = '' or menu_tree_pid is null;
alter table tp_menu modify column MENU_TYPE varchar(50) not null comment '菜单类型: SYS1901 菜单, SYS1902 内部接口, SYS1903 按钮, SYS1904 外部接口, SYS1905 外部菜单, SYS1906 大屏菜单, SYS1907 菜单分类节点';

-- 定时任务增加字段
alter table tp_scheduled_task add column app_name varchar(50)  not null comment '应用程序名称' ;

-- 修改定时任务字段长度
alter table tp_scheduled_task modify column task_id varchar(36)  not null comment '任务id';
alter table tp_scheduled_task modify column now_executed int default 1 comment '是否立即执行';
alter table tp_scheduled_task modify column serial_executed int default 1 comment '是否串行执行';
alter table tp_scheduled_task modify column serial_threshold int comment '串行阈值（秒）';
alter table tp_scheduled_task modify column last_result int comment '上次执行结果（1：执行成功；2：执行失败）';
alter table tp_scheduled_task modify column enabled int default 1 comment '是否启用';
alter table tp_scheduled_task modify column actived int default 1 comment '是否有效';
alter table tp_scheduled_task modify column creator varchar(36) comment '创建人';
alter table tp_scheduled_task modify column updator varchar(36) comment '修改人';
alter table tp_scheduled_task modify column del_id varchar(36) default -1 not null comment '删除id（删除时task_id）';