package com.jiuxi.admin.core.runner;

import cn.hutool.core.util.StrUtil;
import com.jiuxi.admin.autoconfig.AdminConfigurationProperties;
import com.jiuxi.common.bean.LogicalDisk;
import com.jiuxi.common.util.FileUtils;
import com.jiuxi.mybatis.autoconfig.TopinfoMybatisAutoProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Map;

/**
 * @Description: 启动 Runner
 * @ClassName: AdminCommanLineRunner
 * @Author: pand
 * @Date: 2021-04-26 20:27
 * @Copyright: 2021 www.tuxun.net Inc. All rights reserved.
 */
public class AdminCommanLineRunner implements CommandLineRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdminCommanLineRunner.class);

    @Autowired
    private AdminConfigurationProperties properties;

    @Override
    public void run(String... args) throws Exception {
        Map<String, LogicalDisk> logicalDiskMap = properties.getLogicalDisk();
        if (null != logicalDiskMap) {
            FileUtils.initLogicalDisk(logicalDiskMap);
        }
    }

}
