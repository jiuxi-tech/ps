package com.jiuxi.shared.config.web.filter;


import org.apache.commons.lang3.StringUtils;

/**
 * @description: SQL过滤
 * @author Ypp
 * @date 2020/7/20 15:14
 */
public class SQLFilter {

    /**
     * @description:  SQL注入过滤
     * @author Ypp
     * @date 2020/7/20 15:14
     * @param str 待验证的字符串
     * @return java.lang.String
     */
    public static String sqlInject(String str){
        if(StringUtils.isBlank(str)){
            return null;
        }
        //去掉'|"|;|\字符
        str = StringUtils.replace(str, "'", "");
        str = StringUtils.replace(str, "\"", "");
        str = StringUtils.replace(str, ";", "");
        str = StringUtils.replace(str, "\\", "");

        //转换成小写
        str = str.toLowerCase();

        //非法字符
        String[] keywords = {"master", "truncate", "insert", "select", "delete", "update", "declare", "alter", "drop"};

        //判断是否包含非法字符
        for(String keyword : keywords){
            if(str.indexOf(keyword) != -1){
                throw new IllegalArgumentException("包含非法字符");
            }
        }

        return str;
    }
}
