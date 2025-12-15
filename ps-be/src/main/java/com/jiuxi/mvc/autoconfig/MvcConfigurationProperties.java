package com.jiuxi.mvc.autoconfig;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @ClassName: MvcConfigurationProperties
 * @Description: MVC 熟悉配置
 * @Author: Ypp
 * @Date: 2020/6/12 10:27
 * @Copyright: 2020 Hangzhou Jiuxi Inc. All rights reserved.
 */
@ConfigurationProperties(prefix = "topinfo.mvc")
public class MvcConfigurationProperties {


    /** token 在 header中的key */
    private String tokenHeader = "Token";


    public String getTokenHeader() {
        return tokenHeader;
    }

    public void setTokenHeader(String tokenHeader) {
        this.tokenHeader = tokenHeader;
    }
}
