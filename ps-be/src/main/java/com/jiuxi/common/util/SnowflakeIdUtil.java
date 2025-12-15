package com.jiuxi.common.util;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;

/**
 * @ClassName: SnowflakeIdUtil
 * @Description: 雪花算法
 * @Author: Ypp
 * @Date: 2020/6/10 17:37
 * @Copyright: 2020 Hangzhou Jiuxi Inc. All rights reserved.
 */
public class SnowflakeIdUtil {

    private volatile static Snowflake snowflake;

    public static Snowflake newInstance(long machineId, long datacenterId) {
        if (null == snowflake) {
            synchronized (SnowflakeIdUtil.class) {
                if (null == snowflake) {
                    snowflake = IdUtil.createSnowflake(machineId, datacenterId);
                }
            }
        }
        return snowflake;
    }

    /**
     * 生成整型id
     *
     * @return
     */
    public static Long nextId() {
        return snowflake.nextId();
    }

    /**
     * 生成字符串id
     *
     * @return
     */
    public static String nextIdStr() {
        return snowflake.nextIdStr();
    }

}
