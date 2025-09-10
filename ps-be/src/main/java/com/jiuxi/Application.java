package com.jiuxi;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @ClassName: Application
 * @Description: PS BMP Backend Application 主启动类
 * @Author: System
 * @Date: 2025-08-18
 * @Copyright: 2025 www.tuxun.net Inc. All rights reserved.
 */
@SpringBootApplication(scanBasePackages = {
    "com.jiuxi",
    "com.jiuxi.module.user",
    "com.jiuxi.module.org",
    "com.jiuxi.module.auth",
    "com.jiuxi.module.sys"
})
@MapperScan(basePackages = {
    "com.jiuxi.admin.core.mapper",
    "com.jiuxi.module.user.infra.persistence.mapper",
    "com.jiuxi.module.org.infra.persistence.mapper",
    "com.jiuxi.module.auth.infra.persistence.mapper",
    "com.jiuxi.module.sys.infra.persistence.mapper"
})
@EnableAsync
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}