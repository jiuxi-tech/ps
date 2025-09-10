-- ========================================
-- 菜单修改历史记录表
-- 创建时间: 2025-01-30
-- 描述: 记录菜单树的增删改操作历史
-- ========================================

-- 创建菜单历史记录表
CREATE TABLE `tp_menu_history` (
    `HISTORY_ID` VARCHAR(19) PRIMARY KEY COMMENT '历史记录ID（主键）',
    `MENU_ID` VARCHAR(19) NOT NULL COMMENT '菜单ID',
    `MENU_NAME` VARCHAR(100) COMMENT '菜单名称',
    `OPERATION_TYPE` VARCHAR(20) NOT NULL COMMENT '操作类型（INSERT：新增，UPDATE：修改，DELETE：删除）',
    `NODE_DATA_BEFORE` LONGTEXT COMMENT '节点修改前数据（JSON格式）',
    `NODE_DATA_AFTER` LONGTEXT COMMENT '节点修改后数据（JSON格式）',
    `FULL_TREE_BEFORE` LONGTEXT COMMENT '全表修改前数据（JSON格式）',
    `FULL_TREE_AFTER` LONGTEXT COMMENT '全表修改后数据（JSON格式）',
    `OPERATOR_ID` VARCHAR(19) NOT NULL COMMENT '操作人ID',
    `OPERATOR_NAME` VARCHAR(100) NOT NULL COMMENT '操作人姓名',
    `OPERATION_TIME` VARCHAR(14) NOT NULL COMMENT '操作时间（yyyyMMddHHmmss）',
    `TENANT_ID` VARCHAR(19) COMMENT '租户ID',
    `ASCN_ID` VARCHAR(19) COMMENT '所属机构ID',
    `ACTIVED` INTEGER DEFAULT 1 COMMENT '是否有效（1：有效，0：无效）',
    `CREATOR` VARCHAR(19) NOT NULL COMMENT '创建人',
    `CREATE_TIME` VARCHAR(14) NOT NULL COMMENT '创建时间（yyyyMMddHHmmss）',
    `UPDATOR` VARCHAR(19) COMMENT '修改人',
    `UPDATE_TIME` VARCHAR(14) COMMENT '修改时间（yyyyMMddHHmmss）',
    `EXTEND01` VARCHAR(128) COMMENT '扩展字段01',
    `EXTEND02` VARCHAR(128) COMMENT '扩展字段02',
    `EXTEND03` VARCHAR(128) COMMENT '扩展字段03'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='菜单修改历史记录表';

-- 创建索引
CREATE INDEX `idx_menu_id` ON `tp_menu_history` (`MENU_ID`);
CREATE INDEX `idx_operation_type` ON `tp_menu_history` (`OPERATION_TYPE`);
CREATE INDEX `idx_operation_time` ON `tp_menu_history` (`OPERATION_TIME`);
CREATE INDEX `idx_operator_id` ON `tp_menu_history` (`OPERATOR_ID`);
CREATE INDEX `idx_tenant_id` ON `tp_menu_history` (`TENANT_ID`);

-- ========================================
-- 示例查询语句
-- ========================================

-- 1. 查询某个菜单的所有历史记录
-- SELECT * FROM tp_menu_history 
-- WHERE MENU_ID = '菜单ID' AND ACTIVED = 1 
-- ORDER BY OPERATION_TIME DESC;

-- 2. 查询某个时间段内的所有菜单操作记录
-- SELECT HISTORY_ID, MENU_ID, MENU_NAME, OPERATION_TYPE, OPERATOR_NAME, OPERATION_TIME 
-- FROM tp_menu_history 
-- WHERE OPERATION_TIME BETWEEN '20250101000000' AND '20250131235959' 
-- AND ACTIVED = 1 
-- ORDER BY OPERATION_TIME DESC;

-- 3. 查询某个操作人的所有菜单操作记录
-- SELECT HISTORY_ID, MENU_ID, MENU_NAME, OPERATION_TYPE, OPERATION_TIME 
-- FROM tp_menu_history 
-- WHERE OPERATOR_ID = '操作人ID' AND ACTIVED = 1 
-- ORDER BY OPERATION_TIME DESC;

-- 4. 查询某种操作类型的所有记录
-- SELECT HISTORY_ID, MENU_ID, MENU_NAME, OPERATOR_NAME, OPERATION_TIME 
-- FROM tp_menu_history 
-- WHERE OPERATION_TYPE = 'DELETE' AND ACTIVED = 1 
-- ORDER BY OPERATION_TIME DESC;