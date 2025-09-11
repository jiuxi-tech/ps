package com.jiuxi.shared.common.constants;

/**
 * 业务常量类
 * 定义系统业务相关的常量
 * 
 * @author System
 * @since 2.2.2-SNAPSHOT
 */
public final class BusinessConstants {

    private BusinessConstants() {
        throw new UnsupportedOperationException("Constants class cannot be instantiated");
    }

    /**
     * 通用状态
     */
    public static final class Status {
        /** 启用状态 */
        public static final Integer ENABLED = 1;
        /** 禁用状态 */
        public static final Integer DISABLED = 0;
        /** 删除状态 */
        public static final Integer DELETED = 1;
        /** 未删除状态 */
        public static final Integer NOT_DELETED = 0;
    }

    /**
     * 是否标识
     */
    public static final class YesNo {
        /** 是 */
        public static final Integer YES = 1;
        /** 否 */
        public static final Integer NO = 0;
        
        /** 是（字符串） */
        public static final String YES_STR = "1";
        /** 否（字符串） */
        public static final String NO_STR = "0";
        
        /** 是（布尔字符串） */
        public static final String TRUE_STR = "true";
        /** 否（布尔字符串） */
        public static final String FALSE_STR = "false";
    }

    /**
     * 组织机构类型
     */
    public static final class OrgCategory {
        /** 政府机构 */
        public static final Integer GOVERNMENT = 0;
        /** 企业 */
        public static final Integer ENTERPRISE = 1;
        /** 中介机构 */
        public static final Integer INTERMEDIARY = 2;
        /** 其他类型 */
        public static final Integer OTHER = 3;
    }

    /**
     * 树形结构节点
     */
    public static final class TreeNode {
        /** 根节点父ID */
        public static final String ROOT_PARENT_ID = "-1";
        /** 根节点ID */
        public static final String ROOT_NODE_ID = "1111111111111111111";
        /** 根节点层级码 */
        public static final String ROOT_LEVEL_CODE = "101";
        /** 企业根节点层级码 */
        public static final String ENTERPRISE_ROOT_LEVEL_CODE = "101000";
        /** 企业层级码起始值 */
        public static final String ENTERPRISE_LEVEL_START = "000000001";
        /** 层级码节点起始值 */
        public static final String LEVEL_NODE_START = "001";
    }

    /**
     * 超级管理员
     */
    public static final class SuperAdmin {
        /** 超级管理员人员ID */
        public static final String PERSON_ID = "1111111111111111111";
        /** 超级管理员单位ID */
        public static final String ORG_ID = TreeNode.ROOT_NODE_ID;
        /** 超级管理员租户ID */
        public static final String TENANT_ID = "1111111111111111111";
    }

    /**
     * 数据权限范围
     */
    public static final class DataScope {
        /** 全部数据权限 */
        public static final String ALL = "1";
        /** 自定义数据权限 */
        public static final String CUSTOM = "2";
        /** 部门数据权限 */
        public static final String DEPT = "3";
        /** 部门及以下数据权限 */
        public static final String DEPT_AND_CHILD = "4";
        /** 仅本人数据权限 */
        public static final String SELF = "5";
    }

    /**
     * 用户类型
     */
    public static final class UserType {
        /** 系统用户 */
        public static final String SYSTEM = "00";
        /** 普通用户 */
        public static final String NORMAL = "01";
        /** 企业用户 */
        public static final String ENTERPRISE = "02";
        /** 临时用户 */
        public static final String TEMPORARY = "03";
    }

    /**
     * 性别
     */
    public static final class Gender {
        /** 男性 */
        public static final Integer MALE = 1;
        /** 女性 */
        public static final Integer FEMALE = 0;
        /** 未知 */
        public static final Integer UNKNOWN = 2;
    }

    /**
     * 审核状态
     */
    public static final class AuditStatus {
        /** 待审核 */
        public static final Integer PENDING = 0;
        /** 审核通过 */
        public static final Integer APPROVED = 1;
        /** 审核拒绝 */
        public static final Integer REJECTED = 2;
        /** 已撤回 */
        public static final Integer WITHDRAWN = 3;
    }

    /**
     * 任务状态
     */
    public static final class TaskStatus {
        /** 待处理 */
        public static final Integer PENDING = 0;
        /** 处理中 */
        public static final Integer PROCESSING = 1;
        /** 已完成 */
        public static final Integer COMPLETED = 2;
        /** 已取消 */
        public static final Integer CANCELLED = 3;
        /** 失败 */
        public static final Integer FAILED = 4;
    }

    /**
     * 通知类型
     */
    public static final class NotificationType {
        /** 系统通知 */
        public static final String SYSTEM = "system";
        /** 业务通知 */
        public static final String BUSINESS = "business";
        /** 安全通知 */
        public static final String SECURITY = "security";
        /** 提醒通知 */
        public static final String REMINDER = "reminder";
    }

    /**
     * 短信模板
     */
    public static final class SmsTemplate {
        /** 找回密码模板 */
        public static final String PASSWORD_RESET = "pwdTemplatecode";
        /** 验证码参数 */
        public static final String CODE_PARAM = "code";
        /** 登录验证码 */
        public static final String LOGIN_CODE = "loginCode";
        /** 注册验证码 */
        public static final String REGISTER_CODE = "registerCode";
    }

    /**
     * 操作日志类型
     */
    public static final class LogType {
        /** 登录日志 */
        public static final String LOGIN = "LOGIN";
        /** 操作日志 */
        public static final String OPERATION = "OPERATION";
        /** 异常日志 */
        public static final String ERROR = "ERROR";
        /** 系统日志 */
        public static final String SYSTEM = "SYSTEM";
    }

    /**
     * 菜单类型
     */
    public static final class MenuType {
        /** 目录 */
        public static final String CATALOG = "C";
        /** 菜单 */
        public static final String MENU = "M";
        /** 按钮 */
        public static final String BUTTON = "F";
    }

    /**
     * 字典类型
     */
    public static final class DictType {
        /** 部门类型 */
        public static final String DEPT_TYPE = "SYS0501";
        /** 用户状态 */
        public static final String USER_STATUS = "SYS0502";
        /** 数据状态 */
        public static final String DATA_STATUS = "SYS0503";
    }
}