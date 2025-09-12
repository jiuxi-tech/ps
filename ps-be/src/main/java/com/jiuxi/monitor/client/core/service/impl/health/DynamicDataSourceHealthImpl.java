package com.jiuxi.monitor.client.core.service.impl.health;

import com.jiuxi.common.exception.ExceptionUtils;
import com.jiuxi.shared.infrastructure.persistence.dynamic.DynamicDataSource;
import com.jiuxi.monitor.client.constant.MonitorClientConstant;
import com.jiuxi.monitor.client.core.service.MonitorHealthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.Set;

/**
 * @ClassName: DynamicDataSourceHealthImpl
 * @Description: 单数据源实现
 * @Author 杨占锐
 * @Date 2024/11/20 17:11
 * @Copyright: www.tuxun.net Inc. All rights reserved.
 */
@Service
public class DynamicDataSourceHealthImpl implements MonitorHealthService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DynamicDataSourceHealthImpl.class);

    @Autowired(required = false)
    private DynamicDataSource dynamicDataSource;

    @Override
    public String getServerName() {
        return MonitorClientConstant.DYNAMIC_DATA_SOURCE;
    }

    /**
     * 判断mysql是否健康
     *
     * @return boolean
     * @author 杨占锐
     * @date 2024/11/20 17:12
     */
    @Override
    public boolean isHealth() {
        boolean bool = false;
        if (null != dynamicDataSource) {
            bool = true;
            Map<Object, DataSource> dataSourceMap = dynamicDataSource.getResolvedDataSources();
            Set<Map.Entry<Object, DataSource>> entries = dataSourceMap.entrySet();
            for (Map.Entry<Object, DataSource> entry : entries) {
                Object key = entry.getKey();
                DataSource dataSource = entry.getValue();
                Connection connection = null;
                try {
                    connection = dataSource.getConnection();
                } catch (Exception e) {
                    LOGGER.error("获取数据库连接失败，数据源key：{}，错误：{}", key, ExceptionUtils.getStackTrace(e));
                    return false;
                } finally {
                    if (null != connection) {
                        try {
                            connection.close();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        return bool;
    }
}
