package com.jiuxi.module.role;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName: RoleModuleConfiguration
 * @Description: Role模块配置类 - DDD架构配置
 * @Author DDD重构
 * @Date 2025-09-15
 * @Copyright: Hangzhou Jiuxi Inc. All rights reserved.
 */
@Configuration
@ComponentScan(basePackages = {
    "com.jiuxi.module.role.app",
    "com.jiuxi.module.role.domain",
    "com.jiuxi.module.role.infra",
    "com.jiuxi.module.role.intf"
})
public class RoleModuleConfiguration {
    
    // DDD模块配置类
    // 负责配置整个role模块的Spring组件扫描
    // 确保各层的组件能够被正确注册和依赖注入
    
}