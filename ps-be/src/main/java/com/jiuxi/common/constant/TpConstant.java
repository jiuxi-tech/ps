package com.jiuxi.common.constant;

import com.jiuxi.shared.common.constants.BusinessConstants;
import com.jiuxi.shared.common.constants.SystemConstants;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName: Constant
 * @Description: 常量
 * @Author: Ypp
 * @Date: 2020/11/20 15:05
 * @Copyright: 2020 Hangzhou Jiuxi Inc. All rights reserved.
 * 
 * @deprecated 请使用 {@link BusinessConstants} 和 {@link SystemConstants} 替代
 */
@Deprecated
public class TpConstant {


    /**
     * tree
     *
     * @author Ypp
     * @date 2020/11/27 14:34
     * @return
     * @deprecated 请使用 {@link BusinessConstants.TreeNode} 替代
     */
    @Deprecated
    public static final class NODE {

        /**
         * tree 根节点pid
         * @deprecated 请使用 {@link BusinessConstants.TreeNode#ROOT_PARENT_ID} 替代
         */
        @Deprecated
        public static final String TOP_NODE_PID = BusinessConstants.TreeNode.ROOT_PARENT_ID;

        /**
         * 跟节点 LEVELCODE
         * @deprecated 请使用 {@link BusinessConstants.TreeNode#ROOT_LEVEL_CODE} 替代
         */
        @Deprecated
        public static final String TOP_NODE_LEVELCODE = BusinessConstants.TreeNode.ROOT_LEVEL_CODE;

        /**
         * 跟节点 LEVELCODE
         * @deprecated 请使用 {@link BusinessConstants.TreeNode#ENTERPRISE_ROOT_LEVEL_CODE} 替代
         */
        @Deprecated
        public static final String ENT_TOP_NODE_LEVELCODE = BusinessConstants.TreeNode.ENTERPRISE_ROOT_LEVEL_CODE;

        /**
         * 企业的 层级code开始值，后面的使用三位的 TOP_NODE_START
         * @deprecated 请使用 {@link BusinessConstants.TreeNode#ENTERPRISE_LEVEL_START} 替代
         */
        @Deprecated
        public static final String ENT_TOP_NODE_START = BusinessConstants.TreeNode.ENTERPRISE_LEVEL_START;

        /**
         * tree 根节点id
         * @deprecated 请使用 {@link BusinessConstants.TreeNode#ROOT_NODE_ID} 替代
         */
        @Deprecated
        public static final String TOP_NODE_ID = BusinessConstants.TreeNode.ROOT_NODE_ID;

        /**
         * LEVELCODE 节点开始
         * @deprecated 请使用 {@link BusinessConstants.TreeNode#LEVEL_NODE_START} 替代
         */
        @Deprecated
        public static final String TOP_NODE_START = BusinessConstants.TreeNode.LEVEL_NODE_START;
    }

    /**
     * 是
     * @deprecated 请使用 {@link BusinessConstants.YesNo#YES} 替代
     */
    @Deprecated
    public static final int YES = BusinessConstants.YesNo.YES;

    /**
     * 否
     * @deprecated 请使用 {@link BusinessConstants.YesNo#NO} 替代
     */
    @Deprecated
    public static final int NO = BusinessConstants.YesNo.NO;

    /**
     * 类型：单位
     * @deprecated 请使用 {@link BusinessConstants.DictType#DEPT_TYPE} 替代
     */
    @Deprecated
    public static final String DEPT_TYPE = BusinessConstants.DictType.DEPT_TYPE;

    /**
     * 超级管理员人员
     * @deprecated 请使用 {@link BusinessConstants.SuperAdmin} 替代
     */
    @Deprecated
    public static final class ADMIN {

        /**
         * 人员ID
         * @deprecated 请使用 {@link BusinessConstants.SuperAdmin#PERSON_ID} 替代
         */
        @Deprecated
        public static final String PERSONID = BusinessConstants.SuperAdmin.PERSON_ID;

        /**
         * 单位id
         * @deprecated 请使用 {@link BusinessConstants.SuperAdmin#ORG_ID} 替代
         */
        @Deprecated
        public static final String ASCNID = BusinessConstants.SuperAdmin.ORG_ID;

        /**
         * 租户id
         * @deprecated 请使用 {@link BusinessConstants.SuperAdmin#TENANT_ID} 替代
         */
        @Deprecated
        public static final String TENANTID = BusinessConstants.SuperAdmin.TENANT_ID;
    }

    /**
     * 政府或企业类别
     * @deprecated 请使用 {@link BusinessConstants.OrgCategory} 替代
     */
    @Deprecated
    public static final class Category {

        /**
         * 政府类别
         * @deprecated 请使用 {@link BusinessConstants.OrgCategory#GOVERNMENT} 替代
         */
        @Deprecated
        public static final int ORG = BusinessConstants.OrgCategory.GOVERNMENT;

        /**
         * 企业类别
         * @deprecated 请使用 {@link BusinessConstants.OrgCategory#ENTERPRISE} 替代
         */
        @Deprecated
        public static final int ENT = BusinessConstants.OrgCategory.ENTERPRISE;

        /**
         * 中介
         * @deprecated 请使用 {@link BusinessConstants.OrgCategory#INTERMEDIARY} 替代
         */
        @Deprecated
        public static final int MED = BusinessConstants.OrgCategory.INTERMEDIARY;

        /**
         * 其它类型
         * @deprecated 请使用 {@link BusinessConstants.OrgCategory#OTHER} 替代
         */
        @Deprecated
        public static final int OTHER = BusinessConstants.OrgCategory.OTHER;
    }


    /**
     * 日期格式化
     * @deprecated 请使用 {@link SystemConstants.DateFormat} 替代
     */
    @Deprecated
    public static final class DateFormatter {

        /**
         * @deprecated 请使用 {@link SystemConstants.DateFormat#COMPACT_DATE_FORMATTER} 替代
         */
        @Deprecated
        public static final DateTimeFormatter yyyyMMdd = SystemConstants.DateFormat.COMPACT_DATE_FORMATTER;

        /**
         * @deprecated 请使用 {@link SystemConstants.DateFormat#COMPACT_DATETIME_FORMATTER} 替代
         */
        @Deprecated
        public static final DateTimeFormatter yyyyMMddHHmmss = SystemConstants.DateFormat.COMPACT_DATETIME_FORMATTER;
    }


    /**
     * 短信模版code定义
     * @deprecated 请使用 {@link BusinessConstants.SmsTemplate} 替代
     */
    @Deprecated
    public static final class SMSCode {
        /**
         * TODO 找回密码的短信模版code
         * @deprecated 请使用 {@link BusinessConstants.SmsTemplate#PASSWORD_RESET} 替代
         */
        @Deprecated
        public static final String PWDTEMPLATECODE = BusinessConstants.SmsTemplate.PASSWORD_RESET;
        
        /**
         * @deprecated 请使用 {@link BusinessConstants.SmsTemplate#CODE_PARAM} 替代
         */
        @Deprecated
        public static final String PWDCODEKEY = BusinessConstants.SmsTemplate.CODE_PARAM;
    }

    /**
     * 动态表前缀
     * @deprecated 请使用 {@link SystemConstants.Database#DYNAMIC_TABLE_PREFIX} 替代
     */
    @Deprecated
    public static final String PRO_MCODE = SystemConstants.Database.DYNAMIC_TABLE_PREFIX;

    /**
     * 缓存许可证校验结果
     * @deprecated 建议使用专门的缓存服务类管理
     */
    @Deprecated
    public static Map<String, Boolean> securityLicenceMap = new HashMap<>();

}
