package com.jiuxi.admin.core.config;

import com.jiuxi.admin.core.interceptor.ApiKeyInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 开放API配置类
 * 注册API Key验证拦截器
 * 
 * @author system
 * @date 2025-01-30
 */
@Configuration
public class OpenApiConfig implements WebMvcConfigurer {

    @Autowired
    private ApiKeyInterceptor apiKeyInterceptor;

    /**
     * 添加拦截器
     * 只拦截 /open-api/** 路径的请求
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(apiKeyInterceptor)
                .addPathPatterns("/open-api/**")
                .order(1); // 设置拦截器优先级，在其他拦截器之前执行
    }
}
