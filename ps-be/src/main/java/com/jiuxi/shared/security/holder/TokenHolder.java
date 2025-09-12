package com.jiuxi.shared.security.holder;

/**
 * @ClassName: TokenHolder
 * @Description: Token Holder
 * @Author: 杨攀
 * @Date: 2023/7/31 15:57
 * @Copyright: 2023 www.tuxun.net Inc. All rights reserved.
 */
public class TokenHolder {


    private static final ThreadLocal<String> tokenThreadLocal = new ThreadLocal<String>();


    /**
     * 获取本地当前的值
     * @author 杨攀
     * @date 2023/7/31 15:58
     * @param
     * @return java.lang.String
     */
    public static String  getToken(){
        return tokenThreadLocal.get();
    }


    /**
     * 设置本地当前值
     * @author 杨攀
     * @date 2023/7/31 15:58
     * @param val
     * @return void
     */
    public static void setToken(String val){
        tokenThreadLocal.set(val);
    }


    /**
     * 删除本地当前值，在使用完成后，必须删除，避免内存溢出
     * @author 杨攀
     * @date 2023/7/31 15:59
     * @param
     * @return void
     */
    public static void removeToken(){
        tokenThreadLocal.remove();
    }

}