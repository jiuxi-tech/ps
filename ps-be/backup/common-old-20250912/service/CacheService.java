package com.jiuxi.common.service;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @InterfaceName: CacheService
 * @Description: 缓存服务接口
 * @Author: System
 * @Date: 2025/01/07
 * @Copyright: 2025 www.jiuxi.com Inc. All rights reserved.
 */
public interface CacheService {
    
    // ========== 基础操作 ==========
    
    /**
     * 设置缓存
     */
    void set(String key, Object value);
    
    /**
     * 设置缓存并指定过期时间
     */
    void set(String key, Object value, Duration timeout);
    
    /**
     * 获取缓存
     */
    <T> T get(String key, Class<T> clazz);
    
    /**
     * 获取缓存（泛型方法）
     */
    Object get(String key);
    
    /**
     * 删除缓存
     */
    void delete(String key);
    
    /**
     * 批量删除缓存
     */
    void delete(String... keys);
    
    /**
     * 检查缓存是否存在
     */
    boolean exists(String key);
    
    /**
     * 设置过期时间
     */
    void expire(String key, Duration timeout);
    
    /**
     * 获取过期时间
     */
    Duration getExpire(String key);
    
    // ========== 模式匹配操作 ==========
    
    /**
     * 根据模式获取所有键
     */
    Set<String> keys(String pattern);
    
    /**
     * 根据模式删除缓存
     */
    void deleteByPattern(String pattern);
    
    // ========== Hash操作 ==========
    
    /**
     * 设置Hash缓存
     */
    void hSet(String key, String field, Object value);
    
    /**
     * 获取Hash缓存
     */
    <T> T hGet(String key, String field, Class<T> clazz);
    
    /**
     * 获取Hash所有字段
     */
    Map<String, Object> hGetAll(String key);
    
    /**
     * 删除Hash字段
     */
    void hDelete(String key, String... fields);
    
    /**
     * 检查Hash字段是否存在
     */
    boolean hExists(String key, String field);
    
    /**
     * 获取Hash所有字段名
     */
    Set<String> hKeys(String key);
    
    /**
     * 获取Hash字段数量
     */
    long hSize(String key);
    
    // ========== List操作 ==========
    
    /**
     * 从左侧推入List
     */
    void lPush(String key, Object value);
    
    /**
     * 从右侧推入List
     */
    void rPush(String key, Object value);
    
    /**
     * 从左侧弹出List
     */
    <T> T lPop(String key, Class<T> clazz);
    
    /**
     * 从右侧弹出List
     */
    <T> T rPop(String key, Class<T> clazz);
    
    /**
     * 获取List长度
     */
    long lSize(String key);
    
    /**
     * 获取List范围内的元素
     */
    <T> List<T> lRange(String key, long start, long end, Class<T> clazz);
    
    // ========== Set操作 ==========
    
    /**
     * 添加Set元素
     */
    void sAdd(String key, Object... values);
    
    /**
     * 移除Set元素
     */
    void sRemove(String key, Object... values);
    
    /**
     * 检查Set是否包含元素
     */
    boolean sIsMember(String key, Object value);
    
    /**
     * 获取Set所有元素
     */
    <T> Set<T> sMembers(String key, Class<T> clazz);
    
    /**
     * 获取Set大小
     */
    long sSize(String key);
    
    // ========== 分布式锁操作 ==========
    
    /**
     * 尝试获取分布式锁
     */
    boolean tryLock(String lockKey, String requestId, Duration expireTime);
    
    /**
     * 释放分布式锁
     */
    boolean releaseLock(String lockKey, String requestId);
    
    // ========== 计数器操作 ==========
    
    /**
     * 递增计数器
     */
    long increment(String key);
    
    /**
     * 递增计数器（指定步长）
     */
    long increment(String key, long delta);
    
    /**
     * 递减计数器
     */
    long decrement(String key);
    
    /**
     * 递减计数器（指定步长）
     */
    long decrement(String key, long delta);
    
    // ========== 缓存统计 ==========
    
    /**
     * 获取缓存信息
     */
    Map<String, Object> getCacheInfo();
    
    /**
     * 清空所有缓存
     */
    void flushAll();
    
    /**
     * 获取数据库大小
     */
    long getDbSize();
}