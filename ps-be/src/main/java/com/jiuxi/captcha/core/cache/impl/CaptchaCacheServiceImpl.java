package com.jiuxi.captcha.core.cache.impl;

import com.jiuxi.captcha.core.cache.CaptchaCacheService;
import com.jiuxi.shared.infrastructure.cache.RedisCacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @ClassName: CaptchaCacheServiceImpl
 * @Description: 验证码缓存服务实现类
 * @Author: pand
 * @Date: 2022/12/13 14:26
 * @Copyright: 2022 www.tuxun.net Inc. All rights reserved.
 */
@Service
public class CaptchaCacheServiceImpl implements CaptchaCacheService {

    @Autowired(required = false)
    private RedisCacheService redisCacheService;

    @Override
    public void put(String key, Object value, long timeout) {
        if (redisCacheService != null) {
            redisCacheService.putOpsForValue(key, value.toString(), timeout, TimeUnit.SECONDS);
        }
    }

    @Override
    public Object get(String key) {
        if (redisCacheService != null) {
            return redisCacheService.getOpsForValue(key);
        }
        return null;
    }

    @Override
    public void remove(String key) {
        if (redisCacheService != null) {
            redisCacheService.delete(key);
        }
    }

    @Override
    public boolean exists(String key) {
        if (redisCacheService != null) {
            return redisCacheService.getOpsForValue(key) != null;
        }
        return false;
    }

    @Override
    public String getString(String key) {
        if (redisCacheService != null) {
            Object value = redisCacheService.getOpsForValue(key);
            return value != null ? value.toString() : null;
        }
        return null;
    }
}