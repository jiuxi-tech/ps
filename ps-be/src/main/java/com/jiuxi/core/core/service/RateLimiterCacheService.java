package com.jiuxi.core.core.service;

/**
 * @ClassName: CaptchaCacheService
 * @Description: 限流缓存服务
 * @Author: pand
 * @Date: 2022/12/13 14:26
 * @Copyright: 2022 www.tuxun.net Inc. All rights reserved.
 */
public interface RateLimiterCacheService {


    /**
     * 自增加
     *
     * @param requestId key
     * @param timeout   过期时间，单位毫秒
     * @return long
     * @author pand
     * @date 2022-12-13 17:11
     */
    long increment(String requestId, long timeout);
}
