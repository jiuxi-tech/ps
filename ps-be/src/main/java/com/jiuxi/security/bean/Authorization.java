package com.jiuxi.security.bean;

/**
 * @ClassName: Authorization
 * @Description: 鉴权
 * @Author: 杨攀
 * @Date: 2020/5/25 14:08
 * @Copyright: 2020 www.tuxun.net Inc. All rights reserved.
 */
public class Authorization {

    /**
     * token 在 header中的key
     */
    private String tokenHeader = "Token";

    public String getTokenHeader() {
        return tokenHeader;
    }

    public void setTokenHeader(String tokenHeader) {
        this.tokenHeader = tokenHeader;
    }

}
