package com.jiuxi.admin.domain.authorization;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 权限管理领域模块配置
 * 
 * 定义权限管理领域模块的组件扫描范围和配置
 * 
 * @author 系统重构
 * @since 2.2.2
 */
@Configuration
@ComponentScan({
    "com.jiuxi.admin.core.controller.domain.authorization",
    "com.jiuxi.admin.core.service.domain.authorization",
    "com.jiuxi.admin.core.mapper.authorization"
})
@EnableTransactionManagement
public class AuthorizationDomainConfiguration {
    
    // 权限管理领域模块的特定配置可以在这里添加
    
}