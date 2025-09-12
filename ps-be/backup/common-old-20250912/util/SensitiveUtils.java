package com.jiuxi.common.util;

import cn.hutool.core.util.StrUtil;

/**
 * @Description: 脱敏工具类
 * @ClassName: SensitiveUtils
 * @Author: pand
 * @Date: 2020-08-28 16:37
 * @Copyright: 2020 www.tuxun.net Inc. All rights reserved.
 */
public class SensitiveUtils {

    /**
     * 中文脱敏，第一个字除外全部脱敏
     *
     * @param chinese: 中文
     * @return java.lang.String
     * @author pand
     * @date 2020-08-28 16:39
     */
    public static String chineseName(String chinese, int start, int end) {
        if (StrUtil.isBlank(chinese)) {
            return "";
        }
        int thisStart = 1;
        int thisEnd = chinese.length();
        if (-1 != start) {
            thisStart = start;
        }
        if (-1 != end) {
            thisEnd = end;
        }
        return StrUtil.hide(chinese, thisStart, thisEnd);
    }

    /**
     * 身份证号脱敏，6-14模糊
     *
     * @param cardNum: 身份证号
     * @return java.lang.String
     * @author pand
     * @date 2020-08-28 16:43
     */
    public static String idCardNum(String cardNum, int start, int end) {
        if (StrUtil.isBlank(cardNum)) {
            return "";
        }
        int thisStart = 6;
        int thisEnd = cardNum.length() - 4;
        if (-1 != start) {
            thisStart = start;
        }
        if (-1 != end) {
            thisEnd = end;
        }
        return StrUtil.hide(cardNum, thisStart, thisEnd);
    }

    public static void main(String[] args) {
        String str = "0370-8032030";
        System.out.println(StrUtil.hide(str, 5, str.length() - 3));
    }

    /**
     * 座机号，6-10位模糊
     *
     * @param phone: 座机号
     * @return java.lang.String
     * @author pand
     * @date 2020-08-28 16:43
     */
    public static String fixedPhone(String phone, int start, int end) {
        if (StrUtil.isBlank(phone)) {
            return "";
        }
        int thisStart = 5;
        int thisEnd = phone.length() - 3;
        if (-1 != start) {
            thisStart = start;
        }
        if (-1 != end) {
            thisEnd = end;
        }
        return StrUtil.hide(phone, thisStart, thisEnd);
    }

    /**
     * 手机号，前3位，后7位除外
     *
     * @param phone: 手机号
     * @return java.lang.String
     * @author pand
     * @date 2020-08-28 16:43
     */
    public static String mobilePhone(String phone, int start, int end) {
        if (StrUtil.isBlank(phone)) {
            return "";
        }
        int thisStart = 3;
        int thisEnd = 7;
        if (-1 != start) {
            thisStart = start;
        }
        if (-1 != end) {
            thisEnd = end;
        }
        return StrUtil.hide(phone, thisStart, thisEnd);
    }

    /**
     * 地址，前5位，后4位除外
     *
     * @param address: 地址
     * @return java.lang.String
     * @author pand
     * @date 2020-08-28 16:43
     */
    public static String address(String address, int start, int end) {
        if (StrUtil.isBlank(address)) {
            return "";
        }
        int thisStart = 6;
        int thisEnd = address.length();
        if (-1 != start) {
            thisStart = start;
        }
        if (-1 != end) {
            thisEnd = end;
        }
        return StrUtil.hide(address, thisStart, thisEnd);
    }

    /**
     * email，@之前的全部变*
     *
     * @param email: email
     * @return java.lang.String
     * @author pand
     * @date 2020-08-28 16:43
     */
    public static String email(String email, int start, int end) {
        if (StrUtil.isBlank(email)) {
            return "";
        }

        int thisStart = StrUtil.indexOf(email, '@');
        int thisEnd = 0;
        if (-1 != start) {
            thisStart = start;
        }
        if (-1 != end) {
            thisEnd = end;
        }
        return StrUtil.hide(email, thisStart, thisEnd);
    }

    /**
     * bankCard，银行卡号，前6位，后4位
     *
     * @param bankCard: bankCard
     * @return java.lang.String
     * @author pand
     * @date 2020-08-28 16:43
     */
    public static String bankCard(String bankCard, int start, int end) {
        if (StrUtil.isBlank(bankCard)) {
            return "";
        }

        int thisStart = 0;
        int thisEnd = bankCard.length() - 4;
        if (-1 != start) {
            thisStart = start;
        }
        if (-1 != end) {
            thisEnd = end;
        }
        return StrUtil.hide(bankCard, thisStart, thisEnd);
    }

    /**
     * 自定义信息
     *
     * @param other: other
     * @return java.lang.String
     * @author pand
     * @date 2020-08-28 16:43
     */
    public static String other(String other, int start, int end) {
        if (StrUtil.isBlank(other)) {
            return "";
        }

        int thisStart = 0;
        int thisEnd = other.length();
        if (-1 != start) {
            thisStart = start;
        }
        if (-1 != end) {
            thisEnd = end;
        }
        return StrUtil.hide(other, thisStart, thisEnd);
    }

}
