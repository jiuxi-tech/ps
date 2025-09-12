package com.jiuxi.shared.infrastructure.cache.impl;

import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.TimedCache;
import cn.hutool.core.util.StrUtil;
import com.jiuxi.shared.infrastructure.cache.RateLimiterCacheService;
import com.jiuxi.shared.infrastructure.cache.RedisCacheService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.TimeUnit;

/**
 * @Description: 限流缓存实现
 * @ClassName: RateLimiterCacheServiceImpl
 * @Author: pand
 * @Date: 2022-12-13 16:34
 * @Copyright: 2022 www.tuxun.net Inc. All rights reserved.
 */
public class RateLimiterCacheServiceImpl implements RateLimiterCacheService {

    /**
     * redis 缓存
     */
    @Autowired(required = false)
    private RedisCacheService redisCacheService;


    /**
     * 本地缓存
     */
    private TimedCache<String, String> timedCache = null;

    /**
     * 本地缓存默认清理周期，单位毫秒
     */
    private int schedulePrune = 5;

    /**
     * 创建本地缓存
     *
     * @param timeout 过期时间
     * @return void
     * @author pand
     * @date 2022-12-13 17:08
     */
    public void init(Long timeout) {
        if (timedCache == null) {
            // 创建缓存，过期单位：毫秒
            timedCache = CacheUtil.newTimedCache(timeout);
            // 启动定时任务，每5毫秒清理一次过期条目，注释此行首次启动仍会清理过期条目
            timedCache.schedulePrune(schedulePrune);
        }
    }

    /**
     * 自增加
     *
     * @param requestId key
     * @param timeout   过期时间，单位毫秒
     * @return long
     * @author pand
     * @date 2022-12-13 17:11
     */
    @Override
    public long increment(String requestId, long timeout) {

        if (redisCacheService == null) {

            this.init(timeout);
            // 获取原来的请求次数, false， 不重新计算超时时间
            String count = timedCache.get(requestId, false);
            // 如果不为空，则说明一分钟内请求过
            if (StrUtil.isNotBlank(count)) {
                int n = Integer.parseInt(count);
                // + 1
                count = String.valueOf(++n);
            } else {
                // 初始化为 1
                count = 1 + "";
            }
            timedCache.put(requestId, count);
            return Long.valueOf(count);
        } else {

            long count = redisCacheService.opsForValueIncrement(requestId, 1);
            if (1 == count) {
                // 设置过期时间，单位毫秒
                redisCacheService.expire(requestId, timeout, TimeUnit.MILLISECONDS);
            }
            return count;
        }
    }
}
