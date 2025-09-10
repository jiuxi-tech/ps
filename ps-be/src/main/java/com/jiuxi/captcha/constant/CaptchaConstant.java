package com.jiuxi.captcha.constant;

/**
 * @ClassName: CaptchaConstant
 * @Description: 验证码 静态变量
 * @Author: 杨攀
 * @Date: 2022/12/12 16:50
 * @Copyright: 2022 www.tuxun.net Inc. All rights reserved.
 */
public class CaptchaConstant {

    /**
     * 验证码 默认开关
     */
    public static final String CAPTCHA_DEFAULT_SWITCH = "true";

    /**
     * 缓存 60 秒， 单位毫秒
     */
    public static final int CAPTCHA_TIMEOUT = 60 * 1000;

    /**
     * 票据本地缓存时间
     */
    public static final int CAPTCHA_TICKET_TIMEOUT = 10 * 1000;


    /**
     * redis的key前缀
     *
     * @author 杨攀
     * @date 2023/2/3 15:48
     */
    public static final class RedisKeyPro {

        /**
         * 验证码 clientuuid 缓存key前缀
         */
        public static final String PLATFORM_CAPTCHA_CLIENTUUID = "platform:captcha:clientuuid";

        /**
         * 验证码 ticket 缓存key前缀
         */
        public static final String PLATFORM_CAPTCHA_TICKET = "platform:captcha:ticket";

    }

    /**
     * 验证码类型
     */
    public class CaptchaType {

        /**
         * 滑块 验证码
         */
        public static final String SLIDER = "slider";

        /**
         * 拼接 验证码
         */
        public static final String CONCAT = "concat";

        /**
         * 旋转 验证码
         */
        public static final String ROTATE = "rotate";

    }


}
