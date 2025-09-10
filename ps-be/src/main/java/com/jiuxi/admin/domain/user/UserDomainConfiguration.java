package com.jiuxi.admin.domain.user;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 用户领域模块配置
 * 
 * 定义用户领域模块的组件扫描范围和配置
 * 
 * @author 系统重构
 * @since 2.2.2
 */
@Configuration
@ComponentScan({
    "com.jiuxi.admin.core.controller.domain.user",
    "com.jiuxi.admin.core.service.domain.user",
    "com.jiuxi.admin.core.mapper.user"
})
@EnableTransactionManagement
public class UserDomainConfiguration {
    
    // 用户领域模块的特定配置可以在这里添加
    
}