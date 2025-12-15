package com.jiuxi.security.core.service.impl;

import com.jiuxi.security.core.service.AuthorizationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @ClassName: AuthorizationRedisServiceImpl
 * @Description: 鉴权实现类 - Redis模式
 * @Author: Ypp
 * @Date: 2020/5/22 16:51
 * @Copyright: 2020 Hangzhou Jiuxi Inc. All rights reserved.
 */
@Deprecated
public class AuthorizationRedisServiceImpl implements AuthorizationService {

    private static final Logger logger = LoggerFactory.getLogger(AuthorizationRedisServiceImpl.class);

    @Override
    public boolean authorization(String token, String uri) {

        logger.debug("执行Redis鉴权操作:{}", token);

        //TODO

        return false;
    }

}
