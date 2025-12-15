package com.jiuxi.monitor.server.core.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.jiuxi.monitor.server.core.service.MonitorRedisCacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * @ClassName: Addd
 * @Description:
 * @Author jiuxx
 * @Date 2023/7/18 8:58
 * @Copyright: Hangzhou Jiuxi Inc. All rights reserved.
 */
@Component
public class MonitorRedisCacheServiceImpl implements MonitorRedisCacheService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * redis 中存 null
     */
    public static final String REDIS_NULL = "redis_null";

    /**
     * 获取缓存数据
     *
     * @param redisPrefix redisKey前缀
     * @param id          业务数据id
     * @param days        缓存的天数
     * @param supplier    查询业务数据的方法
     * @return java.lang.String
     * @author jiuxx
     * @date 2023/7/18 9:21
     */
    @Override
    public String getData(String redisPrefix, String id, int days, Supplier supplier) {
        return getData(redisPrefix, id, days, TimeUnit.DAYS, supplier);
    }

    /**
     * 获取缓存数据
     *
     * @param redisPrefix redisKey前缀
     * @param id          业务数据id
     * @param time        缓存时间
     * @param timeUnit    时间单位
     * @param supplier    查询业务数据的方法
     * @return java.lang.String
     * @author jiuxx
     * @date 2023/7/18 9:21
     */
    @Override
    public String getData(String redisPrefix, String id, int time, TimeUnit timeUnit, Supplier supplier) {

        String redisKey = redisPrefix + id;
        // 从缓存中获取
        String json = stringRedisTemplate.opsForValue().get(redisKey);
        if (StrUtil.isBlank(json)) {
            // 如果缓存没有，则从数据库中获取
            Object configVO = supplier.get();
            // 判断数据库是否存在
            if (null == configVO) {
                // 如果数据库中也没有
                stringRedisTemplate.opsForValue().set(redisKey, REDIS_NULL, time, timeUnit);
                return null;
            } else {
                // 数据库中存在，则缓存到redis中
                json = JSONObject.toJSONString(configVO);
                stringRedisTemplate.opsForValue().set(redisKey, json, time, timeUnit);
                return json;
            }
        } else {
            // 判断是否保存的是 null 字符串
            if (REDIS_NULL.equals(json)) {
                return null;
            } else {
                return json;
            }
        }
    }
}
