package com.jiuxi.mybatis.core.dynamic;

/**
 * @ClassName: DynamicDataSourceContextHolder
 * @Description: 动态数据源上下文管理
 * @Author: 杨攀
 * @Date: 2022/2/22 15:01
 * @Copyright: 2022 www.tuxun.net Inc. All rights reserved.
 */
public class DynamicDataSourceContextHolder {

    /** 存放当前线程使用的数据源类型信息 */
    private static final ThreadLocal<String> contextHolder = new ThreadLocal<>();

    /** 设置数据源 */
    public static void setDataSourceType(String dataSourceType) {
        // 删除原来的数据源，防止内存溢出
        clearDataSourceType();
        // 设置
        contextHolder.set(dataSourceType);
    }

    /** 获取数据源 */
    public static String getDataSourceType() {
        return contextHolder.get();
    }

    /** 清除数据源 */
    public static void clearDataSourceType() {
        contextHolder.remove();
    }
}
