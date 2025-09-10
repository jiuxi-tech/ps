package com.jiuxi.security.sso.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * SSO 自动配置类
 * 
 * @author SSO Integration
 * @since 2.2.2
 */
@Configuration
@ComponentScan("com.jiuxi.security.sso")
@EnableConfigurationProperties(KeycloakSsoProperties.class)
public class SsoAutoConfiguration {
    
    public SsoAutoConfiguration() {
        System.out.println("SsoAutoConfiguration 已创建！");
    }
}