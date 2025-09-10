package com.jiuxi.captcha.core.cache;

/**
 * @ClassName: CaptchaCacheService
 * @Description: 验证码缓存服务接口
 * @Author: pand
 * @Date: 2022/12/13 14:26
 * @Copyright: 2022 www.tuxun.net Inc. All rights reserved.
 */
public interface CaptchaCacheService {

    /**
     * 保存验证码信息
     *
     * @param key   缓存key
     * @param value 缓存值
     * @param timeout 过期时间，单位秒
     * @return void
     * @author pand
     * @date 2022-12-13 17:11
     */
    void put(String key, Object value, long timeout);

    /**
     * 获取验证码信息
     *
     * @param key 缓存key
     * @return Object
     * @author pand
     * @date 2022-12-13 17:11
     */
    Object get(String key);

    /**
     * 删除验证码信息
     *
     * @param key 缓存key
     * @return void
     * @author pand
     * @date 2022-12-13 17:11
     */
    void remove(String key);

    /**
     * 检查key是否存在
     *
     * @param key 缓存key
     * @return boolean
     * @author pand
     * @date 2022-12-13 17:11
     */
    boolean exists(String key);

    /**
     * 获取字符串类型的验证码信息
     *
     * @param key 缓存key
     * @return String
     * @author pand
     * @date 2022-12-13 17:11
     */
    String getString(String key);

    // Test comment for git add functionality
}