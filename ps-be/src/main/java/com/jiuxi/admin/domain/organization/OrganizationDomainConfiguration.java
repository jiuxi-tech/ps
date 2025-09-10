package com.jiuxi.admin.domain.organization;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 组织架构领域模块配置
 * 
 * 定义组织架构领域模块的组件扫描范围和配置
 * 
 * @author 系统重构
 * @since 2.2.2
 */
@Configuration
@ComponentScan({
    "com.jiuxi.admin.core.controller.domain.organization",
    "com.jiuxi.admin.core.service.domain.organization",
    "com.jiuxi.admin.core.mapper.organization"
})
@EnableTransactionManagement
public class OrganizationDomainConfiguration {
    
    // 组织架构领域模块的特定配置可以在这里添加
    
}