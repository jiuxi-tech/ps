package com.jiuxi.common.constant;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName: Constant
 * @Description: 常量
 * @Author: 杨攀
 * @Date: 2020/11/20 15:05
 * @Copyright: 2020 www.tuxun.net Inc. All rights reserved.
 */
public class TpConstant {


    /**
     * tree
     *
     * @author 杨攀
     * @date 2020/11/27 14:34
     * @return
     */
    public static final class NODE {

        /**
         * tree 根节点pid
         *
         * @author 杨攀
         * @date 2020/11/20 15:34
         * @return
         */
        public static final String TOP_NODE_PID = "-1";

        /**
         * 跟节点 LEVELCODE
         *
         * @author 杨攀
         * @date 2020/11/20 15:34
         * @return
         */
        public static final String TOP_NODE_LEVELCODE = "101";

        /**
         * 跟节点 LEVELCODE
         *
         * @author 杨攀
         * @date 2020/11/20 15:34
         * @return
         */
        public static final String ENT_TOP_NODE_LEVELCODE = "101000";

        /**
         * 企业的 层级code开始值，后面的使用三位的 TOP_NODE_START
         *
         * @author pand
         * @date 2021-01-20 14:52
         */
        public static final String ENT_TOP_NODE_START = "000000001";

        /**
         * tree 根节点id
         *
         * @author pand
         * @date 2021-01-20 14:53
         */
        public static final String TOP_NODE_ID = "1111111111111111111";


        /**
         * LEVELCODE 节点开始
         *
         * @author 杨攀
         * @date 2020/11/27 18:01
         * @return
         */
        public static final String TOP_NODE_START = "001";


    }

    /**
     * 是
     *
     * @author 杨攀
     * @date 2020/11/30 17:50
     * @return
     */
    public static final int YES = 1;


    /**
     * 否
     *
     * @author 杨攀
     * @date 2020/11/30 17:50
     * @return
     */
    public static final int NO = 0;

    /**
     * 类型：单位
     *
     * @author pand
     * @date 2021-01-07 09:55
     */
    public static final String DEPT_TYPE = "SYS0501";

    /**
     * 超级管理员人员
     *
     * @author 杨攀
     * @date 2020/11/27 14:34
     * @return
     */
    public static final class ADMIN {

        /**
         * 人员ID
         */
        public static final String PERSONID = "1111111111111111111";

        /**
         * 单位id
         */
        public static final String ASCNID = NODE.TOP_NODE_ID;

        /**
         * 租户id
         */
        public static final String TENANTID = "1111111111111111111";

    }

    /**
     * 政府或企业类别
     *
     * @author 杨攀
     * @date 2020/11/20 15:07
     * @return
     */
    public static final class Category {

        /**
         * 政府类别
         */
        public static final int ORG = 0;

        /**
         * 企业类别
         */
        public static final int ENT = 1;

        /**
         * 中介
         */
        public static final int MED = 2;

        /**
         * 其它类型
         */
        public static final int OTHER = 3;
    }


    /**
     * 日期格式化
     *
     * @author 杨攀
     * @date 2020/11/30 17:45
     * @return
     */
    public static final class DateFormatter {

        public static final DateTimeFormatter yyyyMMdd = DateTimeFormatter.ofPattern("uuuuMMdd");

        public static final DateTimeFormatter yyyyMMddHHmmss = DateTimeFormatter.ofPattern("uuuuMMddHHmmss");

    }


    /**
     * 短信模版code定义
     *
     * @author pand
     * @date 2020/11/20 15:07
     * @return
     */
    public static final class SMSCode {
        /**
         * TODO 找回密码的短信模版code
         */
        public static final String PWDTEMPLATECODE = "pwdTemplatecode";
        public static final String PWDCODEKEY = "code";

    }

    /**
     * 动态表前缀
     */
    public static final String PRO_MCODE = "C_JSON_";


    /**
     * 缓存许可证校验结果
     */
    public static Map<String, Boolean> securityLicenceMap = new HashMap<>();

}
