package com.jiuxi.mybatis.core.dynamic;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * @ClassName: DynamicDataSource
 * @Description: 动态数据源
 * @Author: 杨攀
 * @Date: 2022/2/22 14:42
 * @Copyright: 2022 www.tuxun.net Inc. All rights reserved.
 */
public class DynamicDataSource extends AbstractRoutingDataSource {

    @Override
    protected Object determineCurrentLookupKey() {
        return DynamicDataSourceContextHolder.getDataSourceType();
    }
}
