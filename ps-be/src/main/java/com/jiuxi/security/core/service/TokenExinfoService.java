package com.jiuxi.security.core.service;

/**
 * @Description: token自定义加入的信息，json字符串
 * @ClassName: TokenExinfoService
 * @Author: pand
 * @Date: 2020-11-09 10:13
 * @Copyright: 2020 www.tuxun.net Inc. All rights reserved.
 */

public interface TokenExinfoService {

    /**
     * 新增token的扩展信息
     *
     * @return java.lang.String
     * @author pand
     * @date 2020-11-09 10:14
     */
    String exinfo();

    /**
     * 新增token的扩展信息,并从token中解析出原来的token扩展信息
     *
     * @return java.lang.String
     * @author pand
     * @date 2020-11-09 10:14
     */
    String exinfo(String token);
}
