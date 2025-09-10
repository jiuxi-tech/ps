package com.jiuxi.security.core.config;

import com.jiuxi.admin.core.interceptor.IpAccessControlInterceptor;
import com.jiuxi.security.autoconfig.SecurityConfigurationProperties;
import com.jiuxi.security.core.interceptor.AnalysisTokenHandlerInterceptor;
import com.jiuxi.security.core.interceptor.AuthenticationHandlerInterceptor;
import com.jiuxi.security.core.interceptor.AuthorizationHandlerInterceptor;
import com.jiuxi.security.core.interceptor.SecurityLicenceHandlerInterceptor;
import com.jiuxi.security.core.resolver.AscnIdHandlerMethodArgumentResolver;
import com.jiuxi.security.core.resolver.CityCodeHandlerMethodArgumentResolver;
import com.jiuxi.security.core.resolver.DeptIdHandlerMethodArgumentResolver;
import com.jiuxi.security.core.resolver.PersonIdHandlerMethodArgumentResolver;
import com.jiuxi.security.core.resolver.RoleIdsHandlerMethodArgumentResolver;
import com.jiuxi.security.core.resolver.TokenHandlerMethodArgumentResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * @ClassName: SecurityWebMvcConfigurer
 * @Description: 把拦截器添加到配置中
 * @Author: 杨攀
 * @Date: 2020/5/22 17:02
 * @Copyright: 2020 www.tuxun.net Inc. All rights reserved.
 */
@Configuration
@Order(0)
public class SecurityWebMvcConfigurer implements WebMvcConfigurer {

    @Autowired
    private SecurityConfigurationProperties properties;

    /**
     * 解析 token
     */
    @Autowired
    private AnalysisTokenHandlerInterceptor analysisTokenHandlerInterceptor;

    /**
     * 认证拦截器
     */
    @Autowired
    private AuthenticationHandlerInterceptor authenticationHandlerInterceptor;

    /**
     * 鉴权拦截器
     */
    @Autowired
    private AuthorizationHandlerInterceptor authorizationHandlerInterceptor;

    /**
     * PersonId Argument 解析器
     */
    @Autowired
    private PersonIdHandlerMethodArgumentResolver personIdHandlerMethodArgumentResolver;


    /**
     * ascnId Argument 解析器
     */
    @Autowired
    private AscnIdHandlerMethodArgumentResolver ascnIdHandlerMethodArgumentResolver;

    /**
     * DeptId Argument 解析器
     */
    @Autowired
    private DeptIdHandlerMethodArgumentResolver deptIdHandlerMethodArgumentResolver;

    /**
     * cityCode Argument 解析器
     */
    @Autowired
    private CityCodeHandlerMethodArgumentResolver cityCodeHandlerMethodArgumentResolver;

    /**
     * RoleIds Argument 解析器
     */
    @Autowired
    private RoleIdsHandlerMethodArgumentResolver roleIdsHandlerMethodArgumentResolver;

    /**
     * Token  Argument 解析器
     */
    @Autowired
    private TokenHandlerMethodArgumentResolver tokenHandlerMethodArgumentResolver;

    /**
     * 许可证拦截器
     */
    @Autowired
    private SecurityLicenceHandlerInterceptor securityLicenceHandlerInterceptor;

    /**
     * IP访问控制拦截器
     */
    @Autowired
    private IpAccessControlInterceptor ipAccessControlInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        // 配置文件中排除的请求
        String[] excludePath = properties.getExcludePath();

        // 1、添加IP访问控制拦截器（最高优先级）
        registry.addInterceptor(ipAccessControlInterceptor)
                // 拦截所有
                .addPathPatterns("/**")
                // 排除静态资源和错误页面
                .excludePathPatterns("/static/**", "/error/**", "/platform/captcha/**");

        // 2、添加认证拦截器
        registry.addInterceptor(authenticationHandlerInterceptor)
                // 拦截所有
                .addPathPatterns("/**")
                // 排除不需要拦截的请求
                .excludePathPatterns(excludePath);

        // 3、解析token拦截器
        registry.addInterceptor(analysisTokenHandlerInterceptor)
                // 拦截所有
                .addPathPatterns("/**");

        // 4、添加鉴权拦截器
        registry.addInterceptor(authorizationHandlerInterceptor)
                // 拦截所有
                .addPathPatterns("/**")
                // 排除不需要拦截的请求
                .excludePathPatterns(excludePath);

        // 5、添加许可证拦截器
        registry.addInterceptor(securityLicenceHandlerInterceptor)
                // 拦截所有
                .addPathPatterns("/**")
                // 排除不需要拦截的请求
                .excludePathPatterns(excludePath);


    }

    /**
     * @param resolvers
     * @return void
     * @description: 添加方法参数解析器
     * @author 杨攀
     * @date 2020/5/28 15:26
     */
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(ascnIdHandlerMethodArgumentResolver);
        resolvers.add(personIdHandlerMethodArgumentResolver);
        resolvers.add(deptIdHandlerMethodArgumentResolver);
        resolvers.add(cityCodeHandlerMethodArgumentResolver);
        resolvers.add(tokenHandlerMethodArgumentResolver);
        resolvers.add(roleIdsHandlerMethodArgumentResolver);
    }

}
