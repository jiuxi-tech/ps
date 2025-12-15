package com.jiuxi.shared.config.web;

import com.jiuxi.core.autoconfig.CoreAutoConfiguration;
import com.jiuxi.shared.config.web.interceptor.TenantInterceptor;
import com.jiuxi.shared.config.web.interceptor.TokenInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.servlet.config.annotation.*;

/**
 * @ClassName: WebMvcConfigurer
 * @Description: Web MVC 配置器
 * @Author: Ypp
 * @Date: 2020/6/12 11:01
 * @Copyright: 2020 Hangzhou Jiuxi Inc. All rights reserved.
 */
@Configuration
@AutoConfigureAfter(CoreAutoConfiguration.class)
@Order
public class WebMvcConfigurer implements org.springframework.web.servlet.config.annotation.WebMvcConfigurer {

    @Autowired(required = false)
    private TenantInterceptor tenantInterceptor;

    @Autowired
    private TokenInterceptor tokenInterceptor;

    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;


    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        // 添加认证拦截器
        if (null != tenantInterceptor) {
            registry.addInterceptor(tenantInterceptor);
        }

        // 添加 token 拦截器
        registry.addInterceptor(tokenInterceptor)
                .addPathPatterns("/**")
                // 排除不需要拦截的请求
                .excludePathPatterns("/static/**");
    }

    /**
     * 方法不启用，给程序员自己在 application.yml 配置 spring.mvc.static-path-pattern: /static/**
     * @author Ypp
     * @date 2021/12/2 17:12 
     * @param registry
     * @return void
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 将/static/**访问映射到classpath:/static/ 
        // registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
    }

    /**
     * 设置异步执行的线程池
     * @author Ypp
     * @date 2023/7/31 16:02
     * @param configurer
     * @return void
     */
    @Override
    public void configureAsyncSupport(AsyncSupportConfigurer configurer) {
        configurer.setTaskExecutor(threadPoolTaskExecutor);
    }

}