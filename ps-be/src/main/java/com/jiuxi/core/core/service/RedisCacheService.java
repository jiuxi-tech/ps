package com.jiuxi.core.core.service;

import java.util.concurrent.TimeUnit;

/**
 * @ClassName: RedisCacheService
 * @Description: redis 缓存服务
 * @Author: 杨攀
 * @Date: 2022/11/25 17:40
 * @Copyright: 2022 www.tuxun.net Inc. All rights reserved.
 */
public interface RedisCacheService {

    /**
     * put Hash
     * @author 杨攀
     * @date 2022/11/25 17:45
     * @param key redis key
     * @param hashKey  map key
     * @param value map 值
     * @return void
     */
    public void putOpsForHash(String key, String hashKey, Object value);

    /**
     * put Hash
     * @author 杨攀
     * @date 2022/11/25 17:45
     * @param key redis key
     * @param timeout  过期时间
     * @param timeout 单位
     * @return void
     */
    public void expire(String key, long timeout, TimeUnit unit);

    /**
     * get Hash
     * @author 杨攀
     * @date 2022/11/25 17:45
     * @param key redis key
     * @param hashKey  map key
     * @return void
     */
    public Object getOpsForHash(String key, String hashKey);

    /**
     * 删除 Hash
     * @author 杨攀
     * @date 2022/11/25 17:45 
     * @param key redis key
     * @return void
     */
    public void deleteOpsForHash(String key);


    /**
     * 获取一个字符串类型的值
     * @author 杨攀
     * @date 2022/12/13 13:55
     * @param key
     * @return java.lang.String
     */
    public default Object getOpsForValue(String key){
        return null;
    }


    /**
     * 新增一个字符串类型的值
     * @author 杨攀
     * @date 2022/12/13 13:55
     * @param key
     * @param value
     * @return java.lang.String
     */
    public default void putOpsForValue(String key, String value){
    }


    /**
     * 新增一个字符串类型的值并设置过期时间
     * @author 杨攀
     * @date 2022/12/13 13:58
     * @param key
     * @param value
     * @param timeout
     * @param unit
     * @return java.lang.String
     */
    public default void putOpsForValue(String key, String value, long timeout, TimeUnit unit){
    }


    /**
     * 删除
     * @author 杨攀
     * @date 2022/12/14 17:15
     * @param key
     * @return void
     */
    public default void delete(String key){
    }


    /**
     * 以增量的方式将 value 存储在变量中。
     * @author 杨攀
     * @date 2022/12/13 14:04
     * @param key
     * @param value
     * @return double
     */
    public default Double opsForValueIncrement(String key, double value){
        return null;
    }

    /**
     * 以增量的方式将 value 存储在变量中。
     * @author 杨攀
     * @date 2022/12/13 14:04
     * @param key
     * @param value
     * @return double
     */
    public default Long opsForValueIncrement(String key, long value){
        return null;
    }
}
