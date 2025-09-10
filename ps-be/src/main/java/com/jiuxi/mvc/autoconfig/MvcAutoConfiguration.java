package com.jiuxi.mvc.autoconfig;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.jiuxi.mvc.core.config.CorsConfigurer;
import com.jiuxi.mvc.core.config.MvcWebMvcConfigurer;
import com.jiuxi.mvc.core.interceptor.TenantInterceptor;
import com.jiuxi.mvc.core.interceptor.TokenInterceptor;
import com.jiuxi.common.exception.GlobalExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.io.IOException;

/**
 * @ClassName: MvcAutoConfiguration
 * @Description: 自动配置
 * @Author: 杨攀
 * @Date: 2020/3/12 12:21
 * @Copyright: 2020 www.tuxun.net Inc. All rights reserved.
 */
@Configuration
@ConditionalOnWebApplication // web应用生效
@AutoConfigureBefore(WebMvcAutoConfiguration.class)
@ImportAutoConfiguration(GlobalExceptionHandler.class)
@EnableConfigurationProperties(MvcConfigurationProperties.class)
@Import({CorsConfigurer.class, MvcWebMvcConfigurer.class})
@ConditionalOnProperty(prefix = "topinfo.mvc", name = "tokenHeader", matchIfMissing = true)
public class MvcAutoConfiguration {


    @Autowired
    private MvcConfigurationProperties properties;


    /**
     * @param
     * @return com.jiuxi.mvc.core.interceptor.TenantInterceptor
     * @description: 租户拦截器
     * @author 杨攀
     * @date 2020/6/12 11:05
     */
    @Bean
    @ConditionalOnProperty(prefix = "topinfo.mybatis", name = "tenant", havingValue = "true")
    public TenantInterceptor tenantInterceptor() {
        return new TenantInterceptor(properties.getTokenHeader());
    }

    /**
     * token 拦截器
     * @author 杨攀
     * @date 2023/7/31 16:06
     * @param
     * @return com.jiuxi.mvc.core.interceptor.TokenInterceptor
     */
    @Bean
    public TokenInterceptor tokenInterceptor() {
        return new TokenInterceptor(properties.getTokenHeader());
    }

    // ObjectMapper configuration removed due to compilation issues

}
