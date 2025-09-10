package com.jiuxi.security.autoconfig;

import com.jiuxi.core.core.service.RedisCacheService;
import com.jiuxi.security.core.conditional.ConditionalAccountExinfo;
import com.jiuxi.security.core.config.SecurityWebMvcConfigurer;
import com.jiuxi.security.core.interceptor.AnalysisTokenHandlerInterceptor;
import com.jiuxi.security.core.interceptor.AuthenticationHandlerInterceptor;
import com.jiuxi.security.core.interceptor.AuthorizationHandlerInterceptor;
import com.jiuxi.security.core.interceptor.SecurityLicenceHandlerInterceptor;
import com.jiuxi.security.core.listener.LoginApplicationEventCollection;
import com.jiuxi.security.core.listener.LoginApplicationLisenter;
import com.jiuxi.security.core.listener.LoginApplicationService;
import com.jiuxi.security.core.listener.TpRoleAuthorizationLisenter;
import com.jiuxi.security.core.resolver.*;
import com.jiuxi.security.core.service.*;
import com.jiuxi.security.core.service.impl.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.*;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.*;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @ClassName: SecurityAutoConfiguration
 * @Description: 认证及鉴权自动配置
 * @Author: 杨攀
 * @Date: 2020/5/22 18:33
 * @Copyright: 2020 www.tuxun.net Inc. All rights reserved.
 */
@Configuration
@EnableConfigurationProperties({SecurityConfigurationProperties.class})
@Import({SecurityWebMvcConfigurer.class})
@ComponentScan({"com.jiuxi.security.core.controller", "com.jiuxi.security.core.aop"})
@EnableAsync
public class SecurityAutoConfiguration {

    @Autowired
    private SecurityConfigurationProperties properties;

    @Autowired
    private AuthorizationService authorizationService;

    @Bean
    public TopinfoSecurityCommonService topinfoSecurityCommonService() {
        return new TopinfoSecurityCommonServiceImpl();
    }

    /**
     * @param
     * @return com.jiuxi.security.core.interceptor.AuthenticationHandlerInterceptor
     * @description: 认证 拦截器
     * @author 杨攀
     * @date 2020/5/22 17:10
     */
    @Bean
    public AuthenticationHandlerInterceptor authenticationHandlerInterceptor() {
        String token = properties.getAuthorization().getTokenHeader();
        return new AuthenticationHandlerInterceptor(token, authenticationService());
    }

    /**
     * @param
     * @return com.jiuxi.security.core.service.AuthenticationService
     * @description: 认证
     * @author 杨攀
     * @date 2020/5/26 10:37
     */
    @Bean
    @ConditionalOnMissingBean(AuthenticationService.class)
    public AuthenticationService authenticationService() {
        return new AuthenticationServiceImpl();
    }


    /**
     * 解析 token
     *
     * @param
     * @return com.jiuxi.security.core.interceptor.AnalysisTokenHandlerInterceptor
     * @author 杨攀
     * @date 2023/11/1 19:52
     */
    @Bean
    public AnalysisTokenHandlerInterceptor analysisTokenHandlerInterceptor() {
        // 获取token
        String token = properties.getAuthorization().getTokenHeader();
        return new AnalysisTokenHandlerInterceptor(token);
    }

    /**
     * @param
     * @return com.jiuxi.security.core.interceptor.AuthorizationHandlerInterceptor
     * @description: 鉴权拦截器
     * @author 杨攀
     * @date 2020/5/22 17:11
     */
    @Bean
    public AuthorizationHandlerInterceptor authorizationHandlerInterceptor() {

        // 获取token
        String token = properties.getAuthorization().getTokenHeader();
        // 获取鉴权的模式
        //String mode = properties.getAuthorization().getAuthorizationMode();

        return new AuthorizationHandlerInterceptor(token, authorizationService);
    }

    /**
     * 自定义 token 扩展信息
     *
     * @return com.jiuxi.security.core.service.TokenExinfoService
     * @author pand
     * @date 2020-11-09 10:34
     */
    @Bean
    @ConditionalOnMissingBean
    public TokenExinfoService tokenExinfoService() {
        return new TokenExinfoServiceImpl();
    }

    /**
     * 合作方登陆，并生成token
     *
     * @return com.jiuxi.security.core.service.TokenExinfoService
     * @author pand
     * @date 2020-11-09 10:34
     */
    @Bean
    @ConditionalOnMissingBean
    public ClientTokenService clientTokenService() {
        return new ClientTokenServiceImpl();
    }

    /**
     * @param
     * @return com.jiuxi.security.core.service.AuthorizationService
     * @description: DB 鉴权
     * @author 杨攀
     * @date 2020/5/22 18:07
     */
    @Bean
    @ConditionalOnMissingBean
    public AuthorizationService dbAuthorizationService() {
        return new AuthorizationDBServiceImpl();
    }

    // @ConditionalOnProperty(prefix = "topinfo.security.authorization", name = "authorizationMode", havingValue = "DB", matchIfMissing = false)

    /**
     * @description: 账号拓展表，记录登陆失败次数，基于db实现
     * @author pand
     * @date 2020/9/8 18:08
     */
    @Bean
    @Conditional(ConditionalAccountExinfo.class)
    public LoginApplicationService accountExinfoDBService() {
        return new AccountExinfoDBServiceImpl();
    }

    // @ConditionalOnProperty(prefix = "topinfo.security.authorization", name = "authorizationMode", havingValue = "DB", matchIfMissing = false)

    /**
     * @description: 账号拓展表，记录登陆失败次数，基于db实现
     * @author pand
     * @date 2020/9/8 18:08
     */
    @Bean
    public LoginApplicationEventCollection loginApplicationEventCollection() {
        return new LoginApplicationEventCollection();
    }

    /**
     * @param
     * @return com.jiuxi.security.core.service.AuthorizationService
     * @description: Redis鉴权
     * @author 杨攀
     * @date 2020/5/22 18:08
     */
    /*
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "topinfo.security.authorization", name = "authorizationMode", havingValue = "Redis", matchIfMissing = false)
    public AuthorizationService redisAuthorizationService() {
        return new AuthorizationRedisServiceImpl();
    }
    */

    /**
     * @param
     * @return com.jiuxi.security.core.service.AbstractLoginService
     * @description: 默认登录服务
     * @author 杨攀
     * @date 2020/5/26 16:25
     */
    @Bean
    @ConditionalOnMissingBean
    public AbstractLoginService pwdLoginService() {
        return new PwdLoginServiceImpl(pwdAccountService());
    }

    /**
     * @param
     * @return com.jiuxi.security.core.service.AbstractLoginService
     * @description: 短信登录服务
     * @author 杨攀
     * @date 2020/5/26 16:25
     */
    @Bean
    public AbstractLoginService smsLoginService() {
        return new SmsLoginServiceImpl(smsAccountService());
    }

    /**
     * @param
     * @return com.jiuxi.security.core.service.AbstractLoginService
     * @description: 客户端获取token服务
     * @author pand
     * @date 2020/8/31 16:25
     */
    @Bean
    public AbstractLoginService clientLoginService() {
        return new ClientLoginServiceImpl(pwdAccountService());
    }

    /**
     * @param
     * @return com.jiuxi.security.core.service.LoginService
     * @description: 用户名查用户
     * @author 杨攀
     * @date 2020/5/26 16:25
     */
    @Bean
    @ConditionalOnMissingBean
    public AccountService pwdAccountService() {
        return new PwdAccountServiceImpl();
    }

    /**
     * @param
     * @return com.jiuxi.security.core.service.LoginService
     * @description: 短信查用户
     * @author 杨攀
     * @date 2020/5/26 16:25
     */
    @Bean
    public AccountService smsAccountService() {
        return new SmsAccountServiceImpl();
    }

    /**
     * @param
     * @return com.jiuxi.security.core.service.LoginService
     * @description: 查询用户信息
     * @author 杨攀
     * @date 2020/5/26 16:25
     */
    @Bean
    @ConditionalOnMissingBean
    public PersonService personService() {
        return new PersonServiceImpl();
    }


    /**
     * @param
     * @return com.jiuxi.security.core.resolver.PersonIdHandlerMethodArgumentResolver
     * @description: Token 中 PersonId 解析
     * @author 杨攀
     * @date 2020/5/28 15:06
     */
    @Bean
    public PersonIdHandlerMethodArgumentResolver personIdHandlerMethodArgumentResolver() {
        return new PersonIdHandlerMethodArgumentResolver();
    }

    /**
     * @param
     * @return com.jiuxi.security.core.resolver.AscnIdHandlerMethodArgumentResolver
     * @description: Token 中 AscnId 解析
     * @author 杨攀
     * @date 2020/7/2 11:40
     */
    @Bean
    public AscnIdHandlerMethodArgumentResolver ascnIdHandlerMethodArgumentResolver() {
        return new AscnIdHandlerMethodArgumentResolver();
    }

    /**
     * @param
     * @return com.jiuxi.security.core.resolver.DeptIdHandlerMethodArgumentResolver
     * @description: Token 中 DeptId 解析
     * @author 杨攀
     * @date 2020/7/2 11:40
     */
    @Bean
    public DeptIdHandlerMethodArgumentResolver deptIdHandlerMethodArgumentResolver() {
        return new DeptIdHandlerMethodArgumentResolver();
    }

    /**
     * @param
     * @return com.jiuxi.security.core.resolver.CityCodeHandlerMethodArgumentResolver
     * @description: Token 中 cityCode 解析
     * @author 杨攀
     * @date 2020/7/2 11:40
     */
    @Bean
    public CityCodeHandlerMethodArgumentResolver cityCodeHandlerMethodArgumentResolver() {
        return new CityCodeHandlerMethodArgumentResolver();
    }

    /**
     * @param
     * @return com.jiuxi.security.core.resolver.TokenHandlerMethodArgumentResolver
     * @description: Token 解析
     * @author 杨攀
     * @date 2020/7/2 11:39
     */
    @Bean
    public TokenHandlerMethodArgumentResolver tokenHandlerMethodArgumentResolver() {
        return new TokenHandlerMethodArgumentResolver();
    }

    /**
     * @param
     * @return com.jiuxi.security.core.resolver.RoleIdsHandlerMethodArgumentResolver
     * @description: roleIds 解析
     * @author 杨攀
     * @date 2020/7/2 11:39
     */
    @Bean
    public RoleIdsHandlerMethodArgumentResolver roleIdsHandlerMethodArgumentResolver() {
        return new RoleIdsHandlerMethodArgumentResolver();
    }

    /**
     * 实例化登陆监听器
     *
     * @return com.jiuxi.security.core.listener.LoginApplicationLisenter
     * @author pand
     * @date 2020-09-08 14:43
     */
    @Bean
    public LoginApplicationLisenter loginApplicationLisenter() {
        return new LoginApplicationLisenter();
    }

    /**
     * 角色授权的监听，用于清除 角色菜单的缓存
     *
     * @param
     * @return com.jiuxi.security.core.listener.TpRoleAuthorizationLisenter
     * @author 杨攀
     * @date 2022/11/24 9:53
     */
    @Bean
    public TpRoleAuthorizationLisenter tpRoleAuthorizationLisenter() {
        return new TpRoleAuthorizationLisenter();
    }

    /**
     * 鉴权的缓存操作
     *
     * @param
     * @return com.jiuxi.core.core.service.AuthorizationCacheService
     * @author 杨攀
     * @date 2022/11/25 13:28
     */
    @Bean
    @ConditionalOnBean(RedisCacheService.class)
    public AuthorizationCacheService authorizationCacheService() {
        return new AuthorizationCacheServiceImpl();
    }

    /**
     * 退出登录服务
     *
     * @return com.jiuxi.security.core.service.TopinfoSecurityLogoutService
     * @author 杨占锐
     * @date 2024/10/8 14:00
     */
    @Bean
    public TopinfoSecurityLogoutService topinfoSecurityLogoutService() {
        return new TopinfoSecurityLogoutServiceImpl();
    }

    /**
     * 许可证缓存服务
     * @return
     */
    @Bean
    public SecurityLicenceHandlerInterceptor securityLicenceHandlerInterceptor() {
        return new SecurityLicenceHandlerInterceptor();
    }
}
