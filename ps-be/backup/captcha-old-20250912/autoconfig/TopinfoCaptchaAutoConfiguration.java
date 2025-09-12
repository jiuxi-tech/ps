package com.jiuxi.captcha.autoconfig;

import com.jiuxi.captcha.core.cache.CaptchaCacheService;
import com.jiuxi.captcha.core.cache.impl.CaptchaCacheServiceImpl;
import com.jiuxi.captcha.core.service.CaptchaService;
import com.jiuxi.captcha.core.service.impl.CaptchaServiceImpl;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName: TopinfoCaptchaAutoConfiguration
 * @Description: Captcha 自动配置
 * @Author: 杨攀
 * @Date: 2021/3/8 11:02
 * @Copyright: 2021 www.tuxun.net Inc. All rights reserved.
 */
@Configuration
@EnableConfigurationProperties(TopinfoCaptchaAutoProperties.class)
@ComponentScan({"com.jiuxi.captcha.core.controller"})
public class TopinfoCaptchaAutoConfiguration {


    @Bean
    public CaptchaService captchaService(){
        return new CaptchaServiceImpl();
    }

    // CaptchaCacheService bean is now provided by @Service annotation in CaptchaCacheServiceImpl

    // RedisTemplate bean removed since Redis is disabled

}
