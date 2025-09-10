package com.jiuxi.core.core.service.impl;

import com.jiuxi.core.core.service.RedisCacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @ClassName: RedisCacheServiceImpl
 * @Description: Redis缓存服务实现类
 * @Author: System
 * @Date: 2025-08-23
 * @Copyright: 2025 www.tuxun.net Inc. All rights reserved.
 */
@Service
public class RedisCacheServiceImpl implements RedisCacheService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public void putOpsForHash(String key, String hashKey, Object value) {
        redisTemplate.opsForHash().put(key, hashKey, value);
    }

    @Override
    public void expire(String key, long timeout, TimeUnit unit) {
        redisTemplate.expire(key, timeout, unit);
    }

    @Override
    public Object getOpsForHash(String key, String hashKey) {
        return redisTemplate.opsForHash().get(key, hashKey);
    }

    @Override
    public void deleteOpsForHash(String key) {
        redisTemplate.delete(key);
    }

    @Override
    public Object getOpsForValue(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    @Override
    public void putOpsForValue(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
    }

    @Override
    public void putOpsForValue(String key, String value, long timeout, TimeUnit unit) {
        redisTemplate.opsForValue().set(key, value, timeout, unit);
    }

    @Override
    public void delete(String key) {
        redisTemplate.delete(key);
    }

    @Override
    public Double opsForValueIncrement(String key, double value) {
        return redisTemplate.opsForValue().increment(key, value);
    }

    @Override
    public Long opsForValueIncrement(String key, long value) {
        return redisTemplate.opsForValue().increment(key, value);
    }
}