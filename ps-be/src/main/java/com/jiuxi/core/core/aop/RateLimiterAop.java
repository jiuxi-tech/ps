package com.jiuxi.core.core.aop;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.jiuxi.core.bean.TopinfoRuntimeException;
import com.jiuxi.core.core.annotation.RateLimiterAnnotation;
import com.jiuxi.core.core.service.RateLimiterCacheService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description: 限流注解
 * @ClassName: RateLimiterAop
 * @Author: pand
 * @Date: 2022-12-13 16:12
 * @Copyright: 2022 www.tuxun.net Inc. All rights reserved.
 */
@Aspect
@Component
public class RateLimiterAop {

    /**
     * 缓存
     */
    @Autowired
    private RateLimiterCacheService cacheService;

    /**
     * 定义切入点，切入点为RateLimiter注解
     */
    @Pointcut("@annotation(rateLimiter)")
    public void point(RateLimiterAnnotation rateLimiter) {
    }

    /**
     * 进入方法前拦截，判断访问次数
     *
     * @param joinPoint
     * @param rateLimiter
     * @throws Throwable
     */
    @Before("point(rateLimiter)")
    public void doBefore(JoinPoint joinPoint, RateLimiterAnnotation rateLimiter) {

        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();

        String requestId = rateLimiter.key();
        if (StrUtil.isBlank(requestId)) {
            // 获取用户的浏览器类型
            String userAgent = request.getHeader("User-Agent");
            // 获取用户的IP地址串值
            String ip = request.getHeader("X-Forwarded-For");
            // 如果没有指定key，默认使用ip和浏览器类型
            requestId = DigestUtil.md5Hex(userAgent + ip);
        }

        long count = cacheService.increment(requestId, rateLimiter.timeout());
        // 判断一分钟内访问的次数
        if (count > rateLimiter.count()) {
            // 限制的时间内，访问的次数，大于最大限制次数，拒绝访问，提示5分钟后再试
            throw new TopinfoRuntimeException(-1, rateLimiter.msg());
        }
    }

    /**
     * 后置拦截点，暂无处理
     *
     * @param ret
     * @param rateLimiter
     * @return void
     * @author pand
     * @date 2022-12-13 16:17
     */
    @AfterReturning(returning = "ret", pointcut = "point(rateLimiter)")
    public void doAfterReturning(Object ret, RateLimiterAnnotation rateLimiter) {
    }

}
