package com.jiuxi.module.user;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * 用户模块配置类
 * 负责用户模块的Spring配置
 * 
 * @author DDD Refactor
 * @date 2025-09-14
 */
@Configuration
@ComponentScan(basePackages = {
    "com.jiuxi.module.user.app",
    "com.jiuxi.module.user.domain",
    "com.jiuxi.module.user.infra"
})
public class UserModuleConfiguration {
    
    // 用户模块相关的Bean配置可以在这里添加
    
}