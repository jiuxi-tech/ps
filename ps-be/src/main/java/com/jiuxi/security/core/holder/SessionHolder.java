package com.jiuxi.security.core.holder;


import com.jiuxi.common.bean.SessionVO;

/**
 * @ClassName: SessionHolder
 * @Description: token 上下文 持有, 必须经过 http 请求才能用
 * @Author: Ypp
 * @Date: 2023/10/19 20:04
 * @Copyright: 2023 Hangzhou Jiuxi Inc. All rights reserved.
 */
public class SessionHolder {


    private static final ThreadLocal<SessionVO> threadLocal = new ThreadLocal<SessionVO>();


    /**
     * 获取当前的token
     *
     * @param
     * @return java.lang.String
     * @author Ypp
     * @date 2023/10/19 20:05
     */
    public static SessionVO get() {
        return threadLocal.get();
    }


    /**
     * 设置token
     *
     * @param token
     * @return void
     * @author Ypp
     * @date 2023/10/19 20:05
     */
    public static void set(SessionVO token) {
        threadLocal.set(token);
    }


    /**
     * 删除，在使用完成后，必须删除，避免内存溢出
     * @author Ypp
     * @date 2023/10/19 20:05
     * @param
     * @return void
     */
    public static void remove() {
        threadLocal.remove();
    }
}
