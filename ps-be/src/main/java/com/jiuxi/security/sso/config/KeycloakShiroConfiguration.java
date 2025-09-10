package com.jiuxi.security.sso.config;

import com.jiuxi.security.sso.filter.KeycloakJwtAuthenticationFilter;
import com.jiuxi.security.sso.realm.KeycloakJwtRealm;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.authz.ModularRealmAuthorizer;
import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.DefaultSessionManager;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition;
import org.apache.shiro.spring.web.config.ShiroFilterChainDefinition;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Keycloak Shiro 配置类
 * 
 * 配置 Shiro 安全框架与 Keycloak JWT 的集成
 * 
 * @author SSO Integration
 * @since 2.2.2
 */
@Configuration
@ConditionalOnProperty(name = "keycloak.sso.enabled", havingValue = "true", matchIfMissing = false)
public class KeycloakShiroConfiguration {
    
    public KeycloakShiroConfiguration() {
        System.out.println("KeycloakShiroConfiguration 已创建！");
    }
    
    @Autowired
    private KeycloakSsoProperties properties;
    
    /**
     * 配置 Keycloak JWT Realm
     * 
     * @return Keycloak JWT Realm
     */
    @Bean
    public KeycloakJwtRealm keycloakJwtRealm() {
        KeycloakJwtRealm realm = new KeycloakJwtRealm();
        // 禁用缓存，因为 JWT 是无状态的
        realm.setCachingEnabled(false);
        realm.setAuthenticationCachingEnabled(false);
        realm.setAuthorizationCachingEnabled(false);
        return realm;
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
        // 禁用会话验证调度器，因为JWT是无状态的
        sessionManager.setSessionValidationSchedulerEnabled(false);
        return sessionManager;
    }
    
    /**
     * 配置安全管理器
     * 
     * @return 安全管理器
     */
    @Bean
    public SecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        
        // 设置 Realm
        securityManager.setRealm(keycloakJwtRealm());
        
        // 禁用 Session 存储，因为 JWT 是无状态的
        DefaultSubjectDAO subjectDAO = new DefaultSubjectDAO();
        DefaultSessionStorageEvaluator defaultSessionStorageEvaluator = new DefaultSessionStorageEvaluator();
        defaultSessionStorageEvaluator.setSessionStorageEnabled(false);
        subjectDAO.setSessionStorageEvaluator(defaultSessionStorageEvaluator);
        securityManager.setSubjectDAO(subjectDAO);
        
        return securityManager;
    }
    
    /**
     * 配置 Keycloak JWT 认证过滤器
     * 
     * @return JWT 认证过滤器
     */
    @Bean
    public KeycloakJwtAuthenticationFilter keycloakJwtAuthenticationFilter() {
        return new KeycloakJwtAuthenticationFilter();
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
        
        // 添加自定义过滤器
        Map<String, Filter> filters = new LinkedHashMap<>();
        filters.put("keycloakJwt", keycloakJwtAuthenticationFilter());
        shiroFilterFactoryBean.setFilters(filters);
        
        // 配置过滤器链
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
        
        // API 接口需要 JWT 认证
        //filterChainDefinitionMap.put("/api/**", "keycloakJwt");
        
        // 其他所有请求都需要认证
        //filterChainDefinitionMap.put("/**", "keycloakJwt");
        
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        
        return shiroFilterFactoryBean;
    }
    
    /**
     * 配置 Shiro 过滤器链定义
     * 
     * @return 过滤器链定义
     */
    @Bean
    public ShiroFilterChainDefinition shiroFilterChainDefinition() {
        DefaultShiroFilterChainDefinition chainDefinition = new DefaultShiroFilterChainDefinition();
        
        // 健康检查和静态资源
        chainDefinition.addPathDefinition("/health", "anon");
        chainDefinition.addPathDefinition("/actuator/**", "anon");
        chainDefinition.addPathDefinition("/static/**", "anon");
        chainDefinition.addPathDefinition("/css/**", "anon");
        chainDefinition.addPathDefinition("/js/**", "anon");
        chainDefinition.addPathDefinition("/images/**", "anon");
        chainDefinition.addPathDefinition("/favicon.ico", "anon");
        
        // 登录相关
        chainDefinition.addPathDefinition("/login", "anon");
        chainDefinition.addPathDefinition("/login/**", "anon");
        chainDefinition.addPathDefinition("/logout", "anon");
        chainDefinition.addPathDefinition("/auth/**", "anon");
        
        // SSO 相关端点不需要认证
        chainDefinition.addPathDefinition("/api/sso/callback", "anon");
        chainDefinition.addPathDefinition("/api/sso/login-url", "anon");
        chainDefinition.addPathDefinition("/api/sso/auth-status", "anon");
        
        // API 接口需要认证
        chainDefinition.addPathDefinition("/api/**", "keycloakJwt");
        
        // 默认需要认证
        chainDefinition.addPathDefinition("/**", "keycloakJwt");
        
        return chainDefinition;
    }
}