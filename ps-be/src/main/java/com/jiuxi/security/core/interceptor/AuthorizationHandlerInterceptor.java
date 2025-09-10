package com.jiuxi.security.core.interceptor;

import cn.hutool.core.util.StrUtil;
import com.jiuxi.common.bean.SessionVO;
import com.jiuxi.core.core.annotation.Authorization;
import com.jiuxi.core.core.annotation.IgnoreAuthorization;
import com.jiuxi.security.core.holder.SessionHolder;
import com.jiuxi.security.core.service.AuthorizationService;
import com.jiuxi.security.core.util.AuthorizationHorizontalUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * @ClassName: AuthorizationHandlerInterceptor
 * @Description: 授权，即权限验证，验证某个已认证的用户是否拥有某个权限；
 * @Author: 杨攀
 * @Date: 2020/5/22 15:51
 * @Copyright: 2020 www.tuxun.net Inc. All rights reserved.
 */
public class AuthorizationHandlerInterceptor implements HandlerInterceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthorizationHandlerInterceptor.class);

    /**
     * 在header中的key
     */
    private String token_header;

    /**
     * 鉴权的Service
     */
    private AuthorizationService authorizationService;

    public AuthorizationHandlerInterceptor(String token_header, AuthorizationService service) {
        this.token_header = token_header;
        this.authorizationService = service;
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

        // 判断请求是否是一个方法，即handler是HandlerMethod类型请求
        // 如不是，则不需要拦截，比如静态资源的请求
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        if (null == authorizationService) {
            LOGGER.error("在执行鉴权时，authorizationService为null，请在yml中添加鉴权的配置项，或者自定义实现鉴权逻辑...");
            throw new RuntimeException("鉴权失败，缺少鉴权逻辑！");
        }

        // handler强转为HandlerMethod
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        // 获取方法
        Method method = handlerMethod.getMethod();

        // 获取方法上忽略鉴权的注解
        IgnoreAuthorization ignoreAuthorization = method.getAnnotation(IgnoreAuthorization.class);
        // 判断方法上面是否有忽略鉴权的注解, 有则放行
        if (ignoreAuthorization != null) {
            return true;
        }

        // 获取方法上面的 鉴权注解
        Authorization authorization = method.getAnnotation(Authorization.class);
        // 如果方法上没有鉴权注解，则判断类上面是否有
        if (authorization == null) {
            // 获取类上鉴权注解
            authorization = method.getDeclaringClass().getAnnotation(Authorization.class);
            // 判断类上面有注解, 如果没有注解，则放行
            if (authorization == null) {
                return true;
            }
        }

        // 获取头部的token信息
        SessionVO sessionVO = SessionHolder.get();

        // 获取 人员id
        String jwtpid = sessionVO.getPersonId();

        if (StrUtil.equals(jwtpid, "1111111111111111111")) {
            // 如果是超级管理员，直接放行，操作所有的权限
            return true;
        }

        // 获取横向越权的 业务key
        String businessKey = authorization.businessKey();
        // 如果配置了 横向越权，则进入横向越权校验，如果没配置，则跳过
        if (StrUtil.isNotBlank(businessKey)) {
            AuthorizationHorizontalUtil.authHorizontal(businessKey, request);
        }

        // 角色鉴权，默认需要true
        boolean roleAuth = authorization.roleAuth();
        if (!roleAuth) {
            // 如果配置为false，则跳过角色鉴权
            return true;
        }

        // 获取头部的token信息
        String token = request.getHeader(token_header);

        // 获取当前的请求地址： http://localhost:8080/项目名/sys/login   返回：/sys/login
        String servletPath = request.getServletPath();

        // 鉴权
        if (!authorizationService.authorization(token, servletPath)) {
            throw new RuntimeException("你没有权限访问,请与管理员联系");
        }

        // 鉴权通过放行
        return true;
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
