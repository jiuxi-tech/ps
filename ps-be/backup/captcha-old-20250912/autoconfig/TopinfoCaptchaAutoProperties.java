package com.jiuxi.captcha.autoconfig;

import org.springframework.boot.context.properties.ConfigurationProperties;


/**
 * 验证
 *
 * @author 杨攀
 * @date 2021/3/15 10:21
 */
@ConfigurationProperties(prefix = "topinfo.captcha")
public class TopinfoCaptchaAutoProperties {


    /**
     * 是否启用验证码，有时候压力测试的时候，不需要验证码，默认是开启的
     */
    private String enable = "true";


    /**
     * 验证码过期时间，默认10秒
     */
    private Integer expTime = 10000;

    public String getEnable() {
        return enable;
    }

    public void setEnable(String enable) {
        this.enable = enable;
    }

    public Integer getExpTime() {
        return expTime;
    }

    public void setExpTime(Integer expTime) {
        this.expTime = expTime;
    }
}
