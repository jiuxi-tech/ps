package com.jiuxi.module.user;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 用户模块配置
 * 
 * 定义用户模块的组件扫描范围和配置
 * 
 * @author 系统重构
 * @since 2.2.2-SNAPSHOT
 */
@Configuration
@ComponentScan({
    "com.jiuxi.module.user.app.service",
    "com.jiuxi.module.user.interfaces.web.controller",
    "com.jiuxi.module.user.infra.persistence.mapper"
})
@EnableTransactionManagement
public class UserModuleConfiguration {
    
    // 用户模块的特定配置可以在这里添加
    
}