package com.jiuxi.monitor.client.core.service.impl.health;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidPooledConnection;
import com.jiuxi.common.exception.ExceptionUtils;
import com.jiuxi.monitor.client.constant.MonitorClientConstant;
import com.jiuxi.monitor.client.core.service.MonitorHealthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @ClassName: DruidDataSourceImpl
 * @Description: 单数据源实现
 * @Author 杨占锐
 * @Date 2024/11/20 17:11
 * @Copyright: www.tuxun.net Inc. All rights reserved.
 */
@Service
public class DruidDataSourceHealthImpl implements MonitorHealthService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DruidDataSourceHealthImpl.class);

    @Autowired(required = false)
    private DruidDataSource druidDataSource;

    @Override
    public String getServerName() {
        return MonitorClientConstant.SINGLE_DATA_SOURCE;
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
        if (null != druidDataSource) {
            Connection connection = null;
            try {
                connection = druidDataSource.getConnection();
                bool = true;
            } catch (Exception e) {
                LOGGER.error("获取数据库连接失败，错误：{}", ExceptionUtils.getStackTrace(e));
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
        return bool;
    }
}
