package com.jiuxi.security.config;

import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.authz.ModularRealmAuthorizer;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.text.IniRealm;
import org.apache.shiro.session.mgt.DefaultSessionManager;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 默认 Shiro 配置类
 * 
 * 当 Keycloak SSO 未启用时使用的基础 Shiro 配置
 * 
 * @author SSO Integration
 * @since 2.2.2
 */
@Configuration
@ConditionalOnProperty(name = "keycloak.sso.enabled", havingValue = "false", matchIfMissing = true)
public class DefaultShiroConfiguration {
    
    public DefaultShiroConfiguration() {
        System.out.println("DefaultShiroConfiguration 已创建！");
    }
    
    /**
     * 配置认证器
     * 
     * @return 认证器
     */
    @Bean
    public ModularRealmAuthenticator authenticator() {
        ModularRealmAuthenticator authenticator = new ModularRealmAuthenticator();
        return authenticator;
    }
    
    /**
     * 配置授权器
     * 
     * @return 授权器
     */
    @Bean
    public ModularRealmAuthorizer authorizer() {
        ModularRealmAuthorizer authorizer = new ModularRealmAuthorizer();
        return authorizer;
    }
    
    /**
     * 配置会话管理器
     * 
     * @return 会话管理器
     */
    @Bean
    public SessionManager sessionManager() {
        DefaultSessionManager sessionManager = new DefaultSessionManager();
        // 启用会话验证调度器
        sessionManager.setSessionValidationSchedulerEnabled(true);
        return sessionManager;
    }
    
    /**
     * 配置默认的 Realm
     * 
     * @return 默认 Realm
     */
    @Bean
    public IniRealm defaultRealm() {
        IniRealm realm = new IniRealm();
        // 创建一个简单的用户配置
        realm.setResourcePath("classpath:shiro.ini");
        return realm;
    }
    
    /**
     * 配置安全管理器
     * 
     * @return 安全管理器
     */
    @Bean
    public SecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(defaultRealm());
        return securityManager;
    }
    
    /**
     * 配置 Shiro 过滤器工厂
     * 
     * @param securityManager 安全管理器
     * @return Shiro 过滤器工厂
     */
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        
        // 配置过滤器链 - 允许所有请求通过
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
        
        // 健康检查和静态资源不需要认证
        filterChainDefinitionMap.put("/health", "anon");
        filterChainDefinitionMap.put("/actuator/**", "anon");
        filterChainDefinitionMap.put("/static/**", "anon");
        filterChainDefinitionMap.put("/css/**", "anon");
        filterChainDefinitionMap.put("/js/**", "anon");
        filterChainDefinitionMap.put("/images/**", "anon");
        filterChainDefinitionMap.put("/favicon.ico", "anon");
        
        // 登录相关页面不需要认证
        filterChainDefinitionMap.put("/login", "anon");
        filterChainDefinitionMap.put("/login/**", "anon");
        filterChainDefinitionMap.put("/logout", "anon");
        filterChainDefinitionMap.put("/auth/**", "anon");
        
        // SSO 相关端点不需要认证
        filterChainDefinitionMap.put("/api/sso/callback", "anon");
        filterChainDefinitionMap.put("/api/sso/login-url", "anon");
        filterChainDefinitionMap.put("/api/sso/auth-status", "anon");
        
        // 暂时允许所有 API 请求通过（用于测试）
        filterChainDefinitionMap.put("/api/**", "anon");
        
        // 其他所有请求都允许通过
        filterChainDefinitionMap.put("/**", "anon");
        
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        
        return shiroFilterFactoryBean;
    }
}