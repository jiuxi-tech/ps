package com.jiuxi.shared.common.utils;

import cn.hutool.core.util.StrUtil;
import java.util.Collection;
import java.util.regex.Pattern;

/**
 * 字符串工具类
 * 提供字符串处理的常用方法
 * 
 * @author System
 * @since 2.2.2-SNAPSHOT
 */
public final class StringUtils {

    private StringUtils() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"
    );

    private static final Pattern PHONE_PATTERN = Pattern.compile(
        "^1[3-9]\\d{9}$"
    );

    /**
     * 判断字符串是否为空
     */
    public static boolean isEmpty(String str) {
        return StrUtil.isEmpty(str);
    }

    /**
     * 判断字符串是否不为空
     */
    public static boolean isNotEmpty(String str) {
        return StrUtil.isNotEmpty(str);
    }

    /**
     * 判断字符串是否为空白
     */
    public static boolean isBlank(String str) {
        return StrUtil.isBlank(str);
    }

    /**
     * 判断字符串是否不为空白
     */
    public static boolean isNotBlank(String str) {
        return StrUtil.isNotBlank(str);
    }

    /**
     * 验证邮箱格式
     */
    public static boolean isValidEmail(String email) {
        return isNotBlank(email) && EMAIL_PATTERN.matcher(email).matches();
    }

    /**
     * 验证手机号格式
     */
    public static boolean isValidPhone(String phone) {
        return isNotBlank(phone) && PHONE_PATTERN.matcher(phone).matches();
    }

    /**
     * 首字母大写
     */
    public static String capitalize(String str) {
        return StrUtil.upperFirst(str);
    }

    /**
     * 首字母小写
     */
    public static String uncapitalize(String str) {
        return StrUtil.lowerFirst(str);
    }

    /**
     * 驼峰转下划线
     */
    public static String camelToUnderscore(String str) {
        return StrUtil.toUnderlineCase(str);
    }

    /**
     * 下划线转驼峰
     */
    public static String underscoreToCamel(String str) {
        return StrUtil.toCamelCase(str);
    }

    /**
     * 集合转字符串
     */
    public static String join(Collection<?> collection, String delimiter) {
        return StrUtil.join(delimiter, collection);
    }

    /**
     * 数组转字符串
     */
    public static String join(Object[] array, String delimiter) {
        return StrUtil.join(delimiter, array);
    }
}