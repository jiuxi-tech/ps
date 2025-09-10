package com.jiuxi.mvc.core.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

/**
 * 跨域过滤器：
 * 解决前后端分离或者其他项目中ajax调用接口产生跨域的问题。由于WebMvcConfigurer下的addCorsMappings在使用自定义拦截器时跨域相关配置就会失效，
 * 原因是请求经过的先后顺序问题，当请求到来时会先进入Interceptors拦截器中，而不是进入Mapping映射中，所以返回的头信息中并没有配置的跨域信息，浏览器就会报跨域异常，涉及方法addInterceptors，addCorsMappings。
 * 这里采用CorsFilter过滤器解决跨域问题。
 *
 * @ClassName: CORSConfigurer
 * @Description: CORSConfigurer
 * @Author: pand
 * @Date: 2020/8/21 11:01
 * @Copyright: 2020 www.tuxun.net Inc. All rights reserved.
 */
@Configuration
public class CorsConfigurer {

    /**
     * 支持跨域设置
     *
     * @return org.springframework.boot.web.servlet.FilterRegistrationBean
     * @author pand
     * @date 2020-08-24 11:18
     */
    @Bean
    public FilterRegistrationBean corsFilter() {
        // 注册CORS过滤器
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        // 是否支持安全证书
        config.setAllowCredentials(true);
        // 允许任何域名使用，默认是 * ，所以不设置
        config.addAllowedOriginPattern(CorsConfiguration.ALL);
        // 默认允许任何头，这里添加两个自定义的头，支持低版本浏览器
        config.addAllowedHeader(CorsConfiguration.ALL);
        config.addAllowedHeader("Token");
        config.addAllowedHeader("verification");
        // 允许任何方法（post、get等）
        config.setAllowedMethods(Arrays.asList("POST", "GET", "PUT", "OPTIONS", "DELETE"));
        // 预检请求的有效期，单位为秒。
        config.setMaxAge(18000L);

        source.registerCorsConfiguration("/**", config);
        FilterRegistrationBean bean = new FilterRegistrationBean(new CorsFilter(source));
        bean.setOrder(0);
        return bean;
    }

}
