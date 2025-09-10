package com.jiuxi.admin.domain.system;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 系统管理领域模块配置
 * 
 * 定义系统管理领域模块的组件扫描范围和配置
 * 
 * @author 系统重构
 * @since 2.2.2
 */
@Configuration
@ComponentScan({
    "com.jiuxi.admin.core.controller.domain.system",
    "com.jiuxi.admin.core.service.domain.system",
    "com.jiuxi.admin.core.mapper.system"
})
@EnableTransactionManagement
public class SystemDomainConfiguration {
    
    // 系统管理领域模块的特定配置可以在这里添加
    
}