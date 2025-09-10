package com.jiuxi.mybatis.core.runner;

import com.jiuxi.mybatis.autoconfig.TopinfoMybatisAutoProperties;
import com.jiuxi.common.util.SnowflakeIdUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;

/**
 * @Description: 初始化类，当前初始化学法算法id生成工具
 * @ClassName: MybatisCommandLineRunner
 * @Author: pand
 * @Date: 2020-09-24 10:30
 * @Copyright: 2020 www.tuxun.net Inc. All rights reserved.
 */
public class MybatisCommandLineRunner implements CommandLineRunner {


    @Autowired
    private TopinfoMybatisAutoProperties properties;

    @Override
    public void run(String... args) throws Exception {
        /**
         * 初始化雪花算法工具
         */
        SnowflakeIdUtil.newInstance(properties.getMachineId(), properties.getDatacenterId());
    }
}
