package com.jiuxi.module.auth;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Auth模块配置类
 * 采用DDD+CQRS架构的权限管理模块配置
 * 
 * @author DDD Refactor
 * @date 2025-09-19
 */
@Configuration
@ComponentScan(basePackages = {
    "com.jiuxi.module.auth.app",
    "com.jiuxi.module.auth.domain", 
    "com.jiuxi.module.auth.infra",
    "com.jiuxi.module.auth.intf"
})
public class AuthModuleConfiguration {
    
    /**
     * 模块名称
     */
    public static final String MODULE_NAME = "auth";
    
    /**
     * 模块版本
     */
    public static final String MODULE_VERSION = "v2.0";
    
    /**
     * 模块描述
     */
    public static final String MODULE_DESCRIPTION = "权限管理模块 - 基于DDD+CQRS架构";
}