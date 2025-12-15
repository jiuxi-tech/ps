package com.jiuxi.security.core.service.impl;

import cn.hutool.core.util.StrUtil;
import com.jiuxi.security.core.service.TokenExinfoService;

/**
 * @Description: token扩展信息添加
 * @ClassName: TokenExinfoServiceImpl
 * @Author: pdd
 * @Date: 2020-11-09 10:33
 * @Copyright: 2020 Hangzhou Jiuxi Inc. All rights reserved.
 */
public class TokenExinfoServiceImpl implements TokenExinfoService {

    /**
     * 新增token的扩展信息
     *
     * @return java.lang.String
     * @author pand
     * @date 2020-11-09 10:14
     */
    @Override
    public String exinfo() {
        return "";
    }

    /**
     * 新增token的扩展信息,并从token中解析出原来的token扩展信息
     *
     * @return java.lang.String
     * @author pand
     * @date 2020-11-09 10:14
     */
    @Override
    public String exinfo(String token) {
        // String exinfo = StrUtil.isBlank(token) ? "" : JwtTokenUtils.getExinfo(token);
        return "";
    }
}
