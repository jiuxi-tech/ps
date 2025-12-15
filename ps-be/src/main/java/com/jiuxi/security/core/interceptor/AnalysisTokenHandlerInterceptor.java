package com.jiuxi.security.core.interceptor;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.auth0.jwt.interfaces.Claim;
import com.jiuxi.common.util.JwtUtil;
import com.jiuxi.common.bean.SessionVO;
import com.jiuxi.security.core.holder.SessionHolder;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @ClassName: AnalysisTokenHandlerInterceptor
 * @Description: 解析 token
 * @Author: Ypp
 * @Date: 2023/10/19 19:58
 * @Copyright: 2023 Hangzhou Jiuxi Inc. All rights reserved.
 */
public class AnalysisTokenHandlerInterceptor implements HandlerInterceptor {

    /**
     * token 空字符串
     */
    private static final String TOKEN_NULL = "null";

    /**
     * 在 header中的key
     */
    private String tokenKey;


    public AnalysisTokenHandlerInterceptor(String tokenKey) {
        this.tokenKey = tokenKey;
    }

    /**
     * 前置处理，在执行controller之前来执行
     * @author Ypp
     * @date 2023/10/17 14:08
     * @param request
     * @param response
     * @param handler
     * @return boolean 返回值类型：boolean：true代表放行可以访问controller，false不可以访问controller
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        
        System.out.println("=== AnalysisTokenHandlerInterceptor.preHandle 被调用 ===" + request.getRequestURI());

        // 判断请求是否是一个方法，即handler是HandlerMethod类型请求
        // 如不是，则不需要拦截，比如静态资源的请求
        if (!(handler instanceof HandlerMethod)) {
            System.out.println("=== AnalysisToken: 不是HandlerMethod，直接放行 ===");
            return true;
        }

        // 获取头部的token信息
        String token = request.getHeader(tokenKey);
        // 没有 token，则放行
        if(StrUtil.isBlank(token) || TOKEN_NULL.equalsIgnoreCase(token)){
            return true;
        }

        try{
            String sub = JwtUtil.getToken(token);
            SessionVO sessionVO = JSON.parseObject(sub, SessionVO.class);
            SessionHolder.set(sessionVO);
        } catch (Exception e){

        }

        return true;
    }

    /**
     * 后置处理：在controller执行完，视图解析器没有把视图解析成页面,对视图做统一的修改，主要体现在Model上
     * @author Ypp
     * @date 2023/10/17 14:09
     * @param request
     * @param response
     * @param handler
     * @param modelAndView
     * @return void
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    /**
     * 完成处理：执行时机：视图已经被解析完毕,类似try catch 后的finally, 这个方法的主要作用是用于清理资源的，
     * 当然这个方法也只能在当前这个Interceptor的preHandle方法的返回值为true时才会执行。
     * @author Ypp
     * @date 2023/10/17 14:09
     * @param request
     * @param response
     * @param handler
     * @param ex
     * @return void
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 使用完ThreadLocal后，执行remove操作，避免出现内存溢出情况。
        SessionHolder.remove();
    }
}
