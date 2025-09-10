-- 添加字典
INSERT INTO tp_dictionary (DIC_ID, DIC_CODE, DIC_NAME, DIC_TYPE, PDIC_ID, DIC_DESC, ORDER_INDEX, ENABLED, ACTIVED, TENANT_ID, CREATEOR, CREATE_TIME, UPDATEOR, UPDATE_TIME, EXTEND01, EXTEND02, EXTEND03)
VALUES ('1435156457443033088', 'SYS1905', '外部菜单', -1, '1334029176012128055', '嵌套第三方菜单页面,如帆软报表等', 61, 1, 1, '', '1111111111111111111', '20210907162218', null, '20210907162236', null, '', '');
INSERT INTO tp_dictionary (DIC_ID, DIC_CODE, DIC_NAME, DIC_TYPE, PDIC_ID, ORDER_INDEX, ENABLED, ACTIVED, TENANT_ID,
                           CREATE_TIME, DIC_DESC, EXTEND01, EXTEND02, EXTEND03) VALUES ('1790312686228078592','SYS1906','大屏菜单',-1,'1334029176012128055',62.0,1,1,'','20240514172621','','','','');

INSERT INTO tp_dictionary (DIC_ID, DIC_CODE, DIC_NAME, DIC_TYPE, PDIC_ID, ORDER_INDEX, ENABLED, ACTIVED, TENANT_ID,
                           CREATE_TIME, DIC_DESC, EXTEND01, EXTEND02, EXTEND03) VALUES ('1790312686232272896','SYS1907','菜单分类节点',-1,'1334029176012128055',63.0,1,1,'','20240514172621','','','','');


-- 删除无效菜单
update tp_menu set ACTIVED = '0' where MENU_CODE  = 'SYS_CRON_UPDATE';
update tp_menu set ACTIVED = '0' where MENU_CODE  = 'SYS_CRON_DELETE';
update tp_menu set ACTIVED = '0' where MENU_CODE  = 'SYS_CRON_PAUSE';
update tp_menu set ACTIVED = '0' where MENU_CODE  = 'SYS_CRON_RESUME';

-- 修改定时任务菜单
update tp_menu set MENU_NAME = '定时任务管理', MENU_PID = '1791353663302664192' where MENU_ID = '1693898501382995968';
update tp_menu set ACTIVED = 0 where MENU_ID = '1375038812119367680';
update tp_menu set ACTIVED = 0 where MENU_ID = '1375039154273910784';

-- 添加系统配置
INSERT INTO tp_menu (MENU_ID, MENU_NAME, MENU_CODE, MENU_URI, MENU_PID, menu_tree_pid, MENU_SOURCE, MENU_TYPE, MENU_ICON, MENU_DESC, ORDER_INDEX, ENABLED, ACTIVED, TENANT_ID, CREATE_TIME, CREATOR, UPDATE_TIME, UPDATOR, EXTEND01, EXTEND02, EXTEND03, LEAF) VALUES ('1791354103247405056', '参数配置', 'SYS_PARAM_CONFIG', '%2Fsys%2Fparameterconfig%2Flist', '1791353663302664192', '1791353663302664192', 1, 'SYS1901', '', '', 0, 1, 1, '', '20240517142434', '1111111111111111111', '20240517142531', null, '', '', '', 1);
INSERT INTO tp_menu (MENU_ID, MENU_NAME, MENU_CODE, MENU_URI, MENU_PID, menu_tree_pid, MENU_SOURCE, MENU_TYPE, MENU_ICON, MENU_DESC, ORDER_INDEX, ENABLED, ACTIVED, TENANT_ID, CREATE_TIME, CREATOR, UPDATE_TIME, UPDATOR, EXTEND01, EXTEND02, EXTEND03, LEAF) VALUES ('1791353663302664192', '系统配置', 'SYS_CONFIG', '', '1335881460376142000', '1335881460376142000', 1, 'SYS1901', '', '', 0, 1, 1, null, '20240517142249', '1111111111111111111', '20240517142249', null, null, null, null, 1);

-- 添加企业列表按钮
INSERT INTO tp_menu (MENU_ID, MENU_NAME, MENU_CODE, MENU_URI, MENU_PID, menu_tree_pid, MENU_SOURCE, MENU_TYPE, MENU_ICON, MENU_DESC, ORDER_INDEX, ENABLED, ACTIVED, TENANT_ID, CREATE_TIME, CREATOR, UPDATE_TIME, UPDATOR, EXTEND01, EXTEND02, EXTEND03, LEAF) VALUES ('1335881123376142411', '企业列表', 'SYS_ENTMANAGE_ENT_LIST', '/sys/ent/list', '1436231061037121536', '1436231061037121536', 1, 'SYS1903', '', '企业列表', 1, 1, 1, '', '20240802180100', '1111111111111111111', null, null, '', '', '', 1);

-- 修改定时任务字段长度
alter table tp_scheduled_task modify column task_id varchar(36)  not null comment '主键';
alter table tp_scheduled_task modify column now_executed int default 1 comment '是否立即执行';
alter table tp_scheduled_task modify column serial_executed int default 1 comment '是否串行执行';
alter table tp_scheduled_task modify column serial_threshold int comment '串行阈值（秒）';
alter table tp_scheduled_task modify column last_result int comment '上次执行结果（1：执行成功；2：执行失败）';
alter table tp_scheduled_task modify column enabled int default 1 comment '是否启用';
alter table tp_scheduled_task modify column actived int default 1 comment '是否有效';
alter table tp_scheduled_task modify column creator varchar(36) comment '创建人';
alter table tp_scheduled_task modify column updator varchar(36) comment '修改人';
alter table tp_scheduled_task modify column del_id varchar(36) default -1 not null comment '删除id（删除时=task_id）';








