package com.jiuxi.security.core.interceptor;

import cn.hutool.cache.Cache;
import cn.hutool.cache.CacheUtil;
import cn.hutool.core.net.NetUtil;
import cn.hutool.core.util.StrUtil;
import com.jiuxi.common.constant.TpConstant;
import com.jiuxi.common.util.SmUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @ClassName: LicenceHandlerInterceptor
 * @Description:
 * @Author: pand
 * @Date: 2024/01/10 15:51
 * @Copyright: 2020 www.tuxun.net Inc. All rights reserved.
 */
public class SecurityLicenceHandlerInterceptor implements HandlerInterceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(SecurityLicenceHandlerInterceptor.class);

    /**
     * 根据服务器mac地址生成的的序列号本地缓存，项目启动生成缓存起来
     */
    private static final String SECURITY_SERIAL_NUMBER_CACHE_KEY = "security-serial-number-key";
    /**
     * 系统序列号本地缓存
     */
    private Cache<String, String> securitySerialNumberCache;

    @PostConstruct
    public void init() {
        // 许可证序列号本地缓存,不需要过期
        securitySerialNumberCache = CacheUtil.newFIFOCache(1);
    }

    public SecurityLicenceHandlerInterceptor() {
    }


    /**
     * @param request
     * @param response
     * @param handler
     * @return boolean 返回值类型：boolean：true代表放行可以访问controller，false不可以访问controller
     * @description: 执行时机：在执行controller之前来执行
     * @author 杨攀
     * @date 2020/5/22 15:55
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // sm3生成序列号
        String systemSerialNumber = this.generatorSystemSerialNumber();

        boolean result = TpConstant.securityLicenceMap.get(systemSerialNumber);

        return result;
    }


    /**
     * 获取当前服务器的mac地址生成系统序列号
     *
     * @return
     */
    private String generatorSystemSerialNumber() {

        String systemSerialNumber = securitySerialNumberCache.get(SECURITY_SERIAL_NUMBER_CACHE_KEY);
        if (StrUtil.isBlank(systemSerialNumber)) {
            // 获取本地mac地址
            String macAddress = NetUtil.getLocalMacAddress();

            // sm3生成序列号
            systemSerialNumber = SmUtils.digestHexSM3(macAddress);
            securitySerialNumberCache.put(SECURITY_SERIAL_NUMBER_CACHE_KEY, systemSerialNumber);
        }

        return systemSerialNumber;
    }


    /**
     * @param request
     * @param response
     * @param handler
     * @param modelAndView
     * @return void
     * @description: 执行时机：controller执行完，视图解析器没有把视图解析成页面,对视图做统一的修改，主要体现在Model上
     * @author 杨攀
     * @date 2020/5/22 15:55
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }


    /**
     * @param request
     * @param response
     * @param handler
     * @param ex
     * @return void
     * @description: 执行时机：视图已经被解析完毕,类似try catch 后的finally, 这个方法的主要作用是用于清理资源的，当然这个方法也只能在当前这个Interceptor的preHandle方法的返回值为true时才会执行。
     * @author 杨攀
     * @date 2020/5/22 15:56
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }

}
