package com.jiuxi.shared.common.context;

/**
 * @ClassName: TenantContextHolder
 * @Description: 租户上下文
 * @Author: Ypp
 * @Date: 2020/6/11 20:46
 * @Copyright: 2020 Hangzhou Jiuxi Inc. All rights reserved.
 */
public class TenantContextHolder {


    private static final ThreadLocal<String> threadLocal = new ThreadLocal<String>();


    /**
     * @description: 获取当前的租户id
     * @author Ypp
     * @date 2020/6/11 20:48
     * @param
     * @return java.lang.Long
     */
    public static String  getTenantId(){
        return threadLocal.get();
    }


    /**
     * @description: 设置租户id
     * @author Ypp
     * @date 2020/6/11 20:49
     * @param tenantId
     * @return void
     */
    public static void setTenantId(String tenantId){
        threadLocal.set(tenantId);
    }


    /**
     * @description: 删除租户id
     * @author Ypp
     * @date 2020/6/11 20:50
     * @param
     * @return void
     */
    public static void removeTenantId(){
        threadLocal.remove();
    }

}
