alter table tp_ent_basicinfo add column extend04 varchar(150) ;
alter table tp_ent_basicinfo add column extend05 varchar(150) ;
alter table tp_ent_basicinfo add column ent_domain varchar(50);
alter table tp_ent_basicinfo add column ent_label varchar(50);
alter table tp_ent_basicinfo add column state_code varchar(50);
alter table tp_ent_basicinfo add column production_mode varchar(50);
alter table tp_ent_basicinfo add column administrative_sub_ordination varchar(50);
alter table tp_ent_basicinfo add column registration_type varchar(50);
alter table tp_ent_basicinfo add column op_scope varchar(300);

comment on column tp_ent_basicinfo.extend04 is '扩展字段04';
comment on column tp_ent_basicinfo.extend05 is '扩展字段05';
comment on column tp_ent_basicinfo.ent_domain is '企业领域';
comment on column tp_ent_basicinfo.ent_label is '企业标签';
comment on column tp_ent_basicinfo.state_code is '营业状态';
comment on column tp_ent_basicinfo.production_mode is '生产经营方式';
comment on column tp_ent_basicinfo.administrative_sub_ordination is '行政隶属关系';
comment on column tp_ent_basicinfo.registration_type is '国民经济类型';
comment on column tp_ent_basicinfo.op_scope is '主营业务';

-- 添加唯一索引
drop index IDX_CITY_CODE;
drop index IDX_USERNAME;
drop index IDX_DEPT_LEVELCODE;
drop index IDX_DIC_CODE;

update tp_dept_basicinfo 
set DEPT_LEVELCODE = concat('101',DEPT_ID)
WHERE CATEGORY =1
;

create unique index UK_tp_city_CODE on tp_city(CITY_CODE);
create unique index UK_TP_USERNAME on tp_account(USERNAME);
create unique index UK_TP_DEPT_LEVELCODE on tp_dept_basicinfo (DEPT_LEVELCODE);
create unique index UK_TP_DIC_CODE on tp_dictionary (DIC_CODE);

/*	建唯一索引时可能失败，需删除多余数据，下面给出sql，方便运维人员排查。
select CITY_CODE,count(1) from tp_city group by CITY_CODE HAVING count(1)>1;
select USERNAME,count(1) from tp_account group by USERNAME HAVING count(1)>1;
select DEPT_LEVELCODE,count(1) from tp_dept_basicinfo group by DEPT_LEVELCODE HAVING count(1)>1;
select DIC_CODE,count(1) from tp_dictionary group by DIC_CODE HAVING count(1)>1;
*/



-- 修改字段长度
alter table tp_person_basicinfo modify  phone varchar(50);
alter table tp_account modify  phone varchar(50) ;
-- 修改配置参数表
update tp_parameter_config set ENABLED = 1 where ENABLED is null;
update tp_parameter_config set ACTIVED = 1 where ACTIVED is null;
alter table tp_parameter_config modify  ENABLED int not null default '1' ;
alter table tp_parameter_config modify  ACTIVED int not null default 1 ;
alter table tp_parameter_config modify  PM_KEY varchar(100) not null ;
create unique index uk_tp_pm_key on tp_parameter_config(PM_KEY) ;

-- 菜单表添加字段
alter table tp_menu add column MENU_TREE_PID varchar(19)  not null default -1;
update tp_menu set menu_tree_pid = menu_pid where menu_tree_pid = '' or menu_tree_pid is null;
alter table tp_menu modify  MENU_TYPE varchar(50) not null ;
comment on  COLUMN tp_menu.MENU_TYPE is '菜单类型: SYS1901 菜单, SYS1902 内部接口, SYS1903 按钮, SYS1904 外部接口, SYS1905 外部菜单, SYS1906 大屏菜单, SYS1907 菜单分类节点';

-- 定时任务增加字段
alter table tp_scheduled_task add column app_name varchar(50)  not null DEFAULT '';
  comment on column tp_scheduled_task.app_name is '应用程序名称' ;

-- 修改定时任务字段长度
alter table tp_scheduled_task modify task_id varchar(36)  not null;
alter table tp_scheduled_task modify now_executed int default 1;
alter table tp_scheduled_task modify serial_executed int default 1;
alter table tp_scheduled_task modify serial_threshold int;
alter table tp_scheduled_task modify last_result int;
alter table tp_scheduled_task modify enabled int default 1;
alter table tp_scheduled_task modify actived int default 1;
alter table tp_scheduled_task modify creator varchar(36);
alter table tp_scheduled_task modify updator varchar(36);
alter table tp_scheduled_task modify del_id varchar(36) default -1 not null;
