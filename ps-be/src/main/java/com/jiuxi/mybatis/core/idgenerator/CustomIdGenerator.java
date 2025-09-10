package com.jiuxi.mybatis.core.idgenerator;

import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.jiuxi.common.util.SnowflakeIdUtil;

/**
 * @ClassName: CustomIdGenerator
 * @Description: 自定义ID生成器
 * @Author: 杨攀
 * @Date: 2020/6/10 17:24
 * @Copyright: 2020 www.tuxun.net Inc. All rights reserved.
 */
public class CustomIdGenerator implements IdentifierGenerator {

    @Override
    public Long nextId(Object entity) {
        return SnowflakeIdUtil.nextId();
    }
}
