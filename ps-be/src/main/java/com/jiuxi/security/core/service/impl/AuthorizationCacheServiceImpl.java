package com.jiuxi.security.core.service.impl;

import com.jiuxi.shared.infrastructure.cache.RedisCacheService;
import com.jiuxi.security.core.service.AuthorizationCacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.TimeUnit;

/**
 * @ClassName: AuthorizationCacheServiceImpl
 * @Description: 鉴权的缓存操作
 * @Author: 杨攀
 * @Date: 2022/11/25 13:17
 * @Copyright: 2022 www.tuxun.net Inc. All rights reserved.
 */
public class AuthorizationCacheServiceImpl implements AuthorizationCacheService {

    @Autowired
    private RedisCacheService redisCacheService;

    /** 缓存 鉴权 前缀 */
    private static final String TP_AUTHORIZATIONP_KEY = "tp_authorization";

    /** 缓存的天数 */
    private static final int TP_AUTHORIZATION_TIMEOUT = 3;

    /**
     * 获取鉴权缓存信息
     * @author 杨攀
     * @date 2022/11/23 17:41
     * @param roles
     * @param path
     * @return java.lang.String
     */
    @Override
    public String getAuthorizationCacheInfo(String roles, String path) {
        String hashKey = roles+path;
        Object result = redisCacheService.getOpsForHash(TP_AUTHORIZATIONP_KEY, hashKey);
        // 如果为空，则表示，没有缓存信息
        if(null == result){
            return null;
        }
        return result+"";
    }

    /**
     * 保存鉴权缓存信息
     * @author 杨攀
     * @date 2022/11/23 17:42
     * @param roles
     * @param path
     * @param result
     * @return void
     */
    @Override
    public void putAuthorizationCacheInfo(String roles, String path, String result) {
        String hashKey = roles+path;
        redisCacheService.putOpsForHash(TP_AUTHORIZATIONP_KEY, hashKey, result);
        redisCacheService.expire(TP_AUTHORIZATIONP_KEY,TP_AUTHORIZATION_TIMEOUT, TimeUnit.DAYS);
    }

    /**
     * 删除鉴权缓存信息
     * @author 杨攀
     * @date 2022/11/23 17:21
     * @param
     * @return void
     */
    @Override
    public void removeAuthorizationCacheInfo() {
        redisCacheService.deleteOpsForHash(TP_AUTHORIZATIONP_KEY);
    }
}
