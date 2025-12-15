package com.jiuxi.shared.infrastructure.persistence.dynamic;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * @ClassName: DynamicDataSource
 * @Description: 动态数据源
 * @Author: Ypp
 * @Date: 2022/2/22 14:42
 * @Copyright: 2022 Hangzhou Jiuxi Inc. All rights reserved.
 */
public class DynamicDataSource extends AbstractRoutingDataSource {

    @Override
    protected Object determineCurrentLookupKey() {
        return DynamicDataSourceContextHolder.getDataSourceType();
    }
}