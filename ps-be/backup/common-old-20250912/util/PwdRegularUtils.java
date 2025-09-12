package com.jiuxi.common.util;

import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import java.util.Random;

/**
 * @Description: 密码校验规则
 * @ClassName: PwdRegularUtils
 * @Author: pand
 * @Date: 2021-04-19 20:13
 * @Copyright: 2021 www.tuxun.net Inc. All rights reserved.
 */
public class PwdRegularUtils {

    /**
     * 8位以上且必须包含数字，字母，特殊符合的两种及以上
     */
    public static String DDFAULT_REGULAR = "^(?![0-9]+$)(?![a-zA-Z]+$)(?![\\W]+$)[a-zA-Z0-9\\W]{8,}$";


    /**
     * 密码是否符合正则表达式要求
     *
     * @param pwd: 密码
     * @return boolean
     * @author pand
     * @date 2021-04-19 20:16
     */
    public static boolean pwdRegular(String regular, String pwd) {
        if (StrUtil.isBlank(regular)) {
            DDFAULT_REGULAR = regular;
        }

        if (pwd.matches(DDFAULT_REGULAR)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 生成随机字符串，如生成随机密码
     *
     * @param leng: 生成字符串的长度
     * @return java.lang.String
     * @author pand
     * @date 2021-06-17 13:13
     */
    public static String randomPwd(int leng) {
        String allStr = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        String intStr = "0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 1; i <= leng; i++) {
            int number = random.nextInt(allStr.length());
            if (i == (leng >= 5 ? 5 : leng)) {
                number = random.nextInt(intStr.length());
                sb.append(intStr.charAt(number));
                continue;
            }
            sb.append(allStr.charAt(number));
        }
        return sb.toString();
    }

    // Removed main method with test code - should be in test classes instead

}
