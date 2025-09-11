package com.jiuxi.shared.security.config;

import com.jiuxi.shared.security.authentication.JwtAuthenticationProvider;
import com.jiuxi.shared.security.authentication.KeycloakAuthenticationProvider;
import com.jiuxi.shared.security.authorization.PermissionEvaluator;
import com.jiuxi.shared.security.filter.JwtAuthenticationFilter;
import com.jiuxi.shared.security.filter.PermissionAuthorizationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.access.vote.RoleVoter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 统一安全配置类
 * 整合Spring Security, JWT认证, Keycloak集成和权限控制
 * 
 * @author Security Refactoring  
 * @since Phase 4.2.2
 */
@Configuration("unifiedSecurityConfig")
@EnableConfigurationProperties(SecurityProperties.class)
@ConditionalOnProperty(name = "ps.security.unified.enabled", havingValue = "true", matchIfMissing = false)
public class UnifiedSecurityConfig {

    @Autowired
    private SecurityProperties securityProperties;
    
    @Autowired(required = false)
    private JwtAuthenticationProvider jwtAuthenticationProvider;
    
    @Autowired(required = false)
    private KeycloakAuthenticationProvider keycloakAuthenticationProvider;
    
    @Autowired(required = false)
    private PermissionEvaluator customPermissionEvaluator;
    
    @Autowired(required = false)
    private TokenService tokenService;

    /**
     * 密码编码器
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 认证管理器配置
     */
    @Bean
    @Primary
    public AuthenticationManager authenticationManager() {
        // 根据配置动态添加认证提供者
        if (securityProperties.getKeycloak().isEnabled()) {
            // Keycloak SSO启用时，优先使用Keycloak认证
            if (keycloakAuthenticationProvider != null) {
                return new ProviderManager(Arrays.asList(
                    keycloakAuthenticationProvider,
                    jwtAuthenticationProvider != null ? jwtAuthenticationProvider : new JwtAuthenticationProvider()
                ));
            }
        }
        
        // 默认使用JWT认证
        return new ProviderManager(Arrays.asList(
            jwtAuthenticationProvider != null ? jwtAuthenticationProvider : new JwtAuthenticationProvider()
        ));
    }

    /**
     * 访问决策管理器
     */
    @Bean
    public AccessDecisionManager accessDecisionManager() {
        // 使用一票通过策略
        List<org.springframework.security.access.AccessDecisionVoter<?>> decisionVoters = Arrays.asList(
            new RoleVoter()
        );
        return new AffirmativeBased(decisionVoters);
    }

    /**
     * 安全过滤器链配置
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // 禁用CSRF保护（RESTful API通常不需要）
            .csrf().disable()
            
            // 配置CORS
            .cors().configurationSource(corsConfigurationSource())
            
            .and()
            
            // 配置会话管理
            .sessionManagement()
            .sessionCreationPolicy(
                securityProperties.getSession().isEnabled() ? 
                SessionCreationPolicy.IF_REQUIRED : 
                SessionCreationPolicy.STATELESS
            )
            
            .and()
            
            // 配置请求授权
            .authorizeRequests(authz -> {
                // 配置排除路径
                String[] excludePaths = securityProperties.getAuthentication().getExcludePaths();
                if (excludePaths != null && excludePaths.length > 0) {
                    authz.antMatchers(excludePaths).permitAll();
                }
                
                // 其他请求需要认证
                if (securityProperties.getAuthentication().isEnabled()) {
                    authz.anyRequest().authenticated();
                } else {
                    // 如果认证被禁用，允许所有请求
                    authz.anyRequest().permitAll();
                }
            });

        // 添加JWT认证过滤器
        if (securityProperties.getAuthentication().isEnabled()) {
            http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
            
            // 添加权限授权过滤器
            if (securityProperties.getAuthorization().isEnabled()) {
                http.addFilterAfter(permissionAuthorizationFilter(), JwtAuthenticationFilter.class);
            }
        }

        // 配置异常处理
        http.exceptionHandling()
            .authenticationEntryPoint((request, response, authException) -> {
                response.setStatus(401);
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write(
                    "{\"code\":401,\"message\":\"认证失败，请重新登录\",\"data\":null}"
                );
            })
            .accessDeniedHandler((request, response, accessDeniedException) -> {
                response.setStatus(403);
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write(
                    "{\"code\":403,\"message\":\"权限不足，访问被拒绝\",\"data\":null}"
                );
            });
        
        return http.build();
    }

    /**
     * JWT认证过滤器
     */
    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(authenticationManager(), securityProperties, tokenService);
    }

    /**
     * 权限授权过滤器
     */
    @Bean
    public PermissionAuthorizationFilter permissionAuthorizationFilter() {
        return new PermissionAuthorizationFilter(securityProperties, customPermissionEvaluator);
    }

    /**
     * CORS配置源
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        
        // 允许的源（生产环境应该配置具体的域名）
        configuration.setAllowedOriginPatterns(Collections.singletonList("*"));
        
        // 允许的HTTP方法
        configuration.setAllowedMethods(Arrays.asList(
            "GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS", "HEAD"
        ));
        
        // 允许的请求头
        configuration.setAllowedHeaders(Arrays.asList(
            "Authorization",
            "Content-Type", 
            "X-Requested-With",
            "Accept",
            "Origin",
            "Access-Control-Request-Method",
            "Access-Control-Request-Headers",
            "X-User-ID",
            "X-User-Name", 
            "X-User-Email",
            "X-User-Roles"
        ));
        
        // 暴露的响应头
        configuration.setExposedHeaders(Arrays.asList(
            "Access-Control-Allow-Origin",
            "Access-Control-Allow-Credentials", 
            "Authorization",
            "Content-Disposition"
        ));
        
        // 允许发送Cookie
        configuration.setAllowCredentials(true);
        
        // 预检请求的缓存时间（1小时）
        configuration.setMaxAge(3600L);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        
        return source;
    }

    /**
     * 权限评估器
     */
    @Bean
    public org.springframework.security.access.PermissionEvaluator permissionEvaluator() {
        if (customPermissionEvaluator != null) {
            return customPermissionEvaluator;
        }
        // 返回默认的权限评估器
        return new org.springframework.security.access.expression.DenyAllPermissionEvaluator();
    }
}