package com.jiuxi.security.core.interceptor;

import cn.hutool.core.util.StrUtil;
import com.jiuxi.common.bean.ErrorCode;
import com.jiuxi.shared.common.exception.TopinfoRuntimeException;
import com.jiuxi.security.core.service.AuthenticationService;
import com.jiuxi.security.core.service.TopinfoSecurityLogoutService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @ClassName: AuthenticationHandlerInterceptor
 * @Description: 认证，身份认证/登录，验证用户是不是拥有相应的身份；
 * @Author: Ypp
 * @Date: 2020/5/22 15:52
 * @Copyright: 2020 Hangzhou Jiuxi Inc. All rights reserved.
 */
public class AuthenticationHandlerInterceptor implements HandlerInterceptor {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationHandlerInterceptor.class);

    /**
     * 在header中的key
     */
    private String token_header;

    /**
     * 认证服务
     */
    private AuthenticationService authenticationService;

    /**
     * redis缓存
     */
    @Autowired
    private TopinfoSecurityLogoutService topinfoSecurityLogoutService;


    public AuthenticationHandlerInterceptor(String token_header, AuthenticationService authenticationService) {
        this.token_header = token_header;
        this.authenticationService = authenticationService;
    }

    /**
     * @param request
     * @param response
     * @param handler
     * @return boolean 返回值类型：boolean：true代表放行可以访问controller，false不可以访问controller
     * @description: 执行时机：在执行controller之前来执行
     * @author Ypp
     * @date 2020/5/22 15:55
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        
        System.out.println("=== AuthenticationHandlerInterceptor.preHandle 被调用 ===" + request.getRequestURI());

        // 判断请求是否是一个方法，即handler是HandlerMethod类型请求
        // 如不是，则不需要拦截，比如静态资源的请求
        if (!(handler instanceof HandlerMethod)) {
            System.out.println("=== 不是HandlerMethod，直接放行 ===");
            return true;
        }

        // 获取头部的token信息
        String token = request.getHeader(token_header);

        // 判断用户是否登录
        if (StrUtil.isBlank(token)) {
            LOGGER.error("token 为空，请求地址为:{}", request.getRequestURI());
            throw new TopinfoRuntimeException(ErrorCode.TOKEN_ERROR.getCode(), ErrorCode.TOKEN_ERROR.getMsg());
        }

        // 认证token
        if (!authenticationService.authentication(token, request, response)) {
            LOGGER.error("token 解析失败！，请求地址为:{}", request.getRequestURI());
            throw new TopinfoRuntimeException(ErrorCode.TOKEN_ERROR.getCode(), ErrorCode.TOKEN_ERROR.getMsg());
        }

        if (null != topinfoSecurityLogoutService) {
            // 判断当前token是否是无效的
            boolean isInvalidToken = topinfoSecurityLogoutService.isInvalidToken(token);
            if (isInvalidToken) {
                LOGGER.error("token 无效！，请求地址为:{}， token: {}", request.getRequestURI(), token);
                throw new TopinfoRuntimeException(ErrorCode.TOKEN_ERROR.getCode(), ErrorCode.TOKEN_ERROR.getMsg());
            }
        }

        return true;
    }


    /**
     * @param request
     * @param response
     * @param handler
     * @param modelAndView
     * @return void
     * @description: 执行时机：controller执行完，视图解析器没有把视图解析成页面,对视图做统一的修改，主要体现在Model上
     * @author Ypp
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
     * @author Ypp
     * @date 2020/5/22 15:56
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }

}
