package com.jiuxi.module.org;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * org模块配置类
 * 负责org模块的整体配置和组件扫描
 * 
 * @author DDD Refactor
 * @date 2025-09-17
 */
@Configuration
@ComponentScan(basePackages = {
    "com.jiuxi.module.org.app",
    "com.jiuxi.module.org.domain",
    "com.jiuxi.module.org.infra",
    "com.jiuxi.module.org.intf"
})
public class OrgModuleConfiguration {
    
    // 模块配置可以在这里添加特定的Bean定义
}