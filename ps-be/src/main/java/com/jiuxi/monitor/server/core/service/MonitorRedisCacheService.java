package com.jiuxi.monitor.server.core.service;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * @ClassName: MonitorRedisCacheService
 * @Description: Redis缓存公共处理逻辑封装
 * @Author 杨占锐
 * @Date 2023/7/18 9:17
 * @Copyright: www.tuxun.net Inc. All rights reserved.
 */
public interface MonitorRedisCacheService {

    /**
     * 获取缓存数据
     *
     * @param redisPrefix redisKey前缀
     * @param id          业务数据id
     * @param days        缓存的天数
     * @param fun         查询业务数据的方法
     * @return java.lang.String
     * @author 杨占锐
     * @date 2023/7/18 9:21
     */
    default String getData(String redisPrefix, String id, int days, Supplier fun) {
        return "";
    }

    /**
     * 获取缓存数据
     *
     * @param redisPrefix redisKey前缀
     * @param id          业务数据id
     * @param time         缓存时间
     * @param timeUnit    时间单位
     * @param fun         查询业务数据的方法
     * @return java.lang.String
     * @author 杨占锐
     * @date 2023/7/18 9:21
     */
    default String getData(String redisPrefix, String id, int time, TimeUnit timeUnit, Supplier fun) {
        return "";
    }
}
