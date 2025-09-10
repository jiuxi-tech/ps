package com.jiuxi.security.core.service.impl;

import cn.hutool.cache.Cache;
import cn.hutool.cache.CacheUtil;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.jiuxi.common.exception.ExceptionUtils;
import com.jiuxi.common.util.CommonDateUtil;
import com.jiuxi.common.util.JwtUtil;
// 暂时注释掉redis相关的import
// import com.jiuxi.plugin.redis.common.core.bean.RedisPubEvent;
// import com.jiuxi.plugin.redis.common.core.service.pub.RedisPublisherService;
import com.jiuxi.security.constant.SecurityConstant;
import com.jiuxi.security.core.service.TopinfoSecurityLogoutService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

/**
 * @ClassName: TopinfoSecurityLogoutServiceImpl
 * @Description: 退出登录接口
 * @Author 杨占锐
 * @Date 2024/10/8 13:45
 * @Copyright: www.tuxun.net Inc. All rights reserved.
 */
public class TopinfoSecurityLogoutServiceImpl implements TopinfoSecurityLogoutService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TopinfoSecurityLogoutServiceImpl.class);

    // 暂时注释掉redis发布服务
    // @Autowired(required = false)
    // private RedisPublisherService redisPublisherService;

    /**
     * 保存退出登录的token
     * key：token唯一id    val: time
     */
    private Cache<String, String> logoutTokenCacheService = null;

    @PostConstruct
    public void init() {

        // 缓存 1 小时
        long timeout = DateUnit.HOUR.getMillis();

        logoutTokenCacheService = CacheUtil.newTimedCache(timeout * 2);

    }

    /**
     * 退出登录
     * <pre>
     *     1. 获取token，获取不到则直接返回
     *     2. 校验token，校验失败则返回
     *     3. 添加到redis缓存，key:token, val: 当前时间，timeout: 配置时间
     * </pre>
     *
     * @param token
     * @return com.jiuxi.common.bean.JsonResponse
     * @author 杨占锐
     * @date 2024/10/8 13:26
     */
    @Override
    public void logout(String token) {

        LOGGER.debug("退出登录, token: {}", token);

        // 暂时禁用redis发布订阅，直接使用本地缓存
        invalidateToken(JwtUtil.getJti(token));
        
        // 暂时注释掉redis发布订阅功能
        /*
        if (null == redisPublisherService) {
            // 未引入redis时，直接放入本地缓存
            invalidateToken(JwtUtil.getJti(token));
            return;
        }
        
        try {
            // 组装消息
            RedisPubEvent msg = new RedisPubEvent();
            msg.setTopic(SecurityConstant.LOGOUT_TOPIC);
            msg.setMsgKey(IdUtil.fastSimpleUUID());
            msg.setData(JwtUtil.getJti(token));
            // 发布消息
            redisPublisherService.publish(msg);

        } catch (Exception e) {
            LOGGER.error("执行退出登录时，发布消息失败，错误：{}", ExceptionUtils.getStackTrace(e));
            // 未引入redis时，直接放入本地缓存
            invalidateToken(JwtUtil.getJti(token));
        }
        */
    }

    /**
     * 判断当前token是否是无效的
     *
     * @param token
     * @return boolean 无效返回true
     * @author 杨占锐
     * @date 2024/10/8 15:13
     */
    @Override
    public boolean isInvalidToken(String token) {

        String val = logoutTokenCacheService.get(JwtUtil.getJti(token));
        if (StrUtil.isNotBlank(val)) {
            // 缓存不为空，说明当前token已经退出登录，token不能再使用
            return true;
        }

        return false;
    }

    /**
     * 把token放入缓存，将token失效
     *
     * @param jti
     * @return void
     * @author 杨占锐
     * @date 2025/2/7 17:49
     */
    @Override
    public void invalidateToken(String jti) {
        logoutTokenCacheService.put(jti, CommonDateUtil.now());
    }

}
