package com.jiuxi.shared.infrastructure.persistence.generator;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;

/**
 * @ClassName: SnowflakeIdUtil
 * @Description: 雪花算法
 * @Author: 杨攀
 * @Date: 2020/6/10 17:37
 * @Copyright: 2020 www.tuxun.net Inc. All rights reserved.
 */
public class SnowflakeIdUtil {

    public Snowflake createSnowflake(long machineId, long datacenterId) {
        return IdUtil.createSnowflake(machineId, datacenterId);
    }

}