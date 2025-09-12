package com.jiuxi.shared.config.web.interceptor;

import cn.hutool.core.util.StrUtil;
import com.jiuxi.common.util.JwtTokenUtils;
import com.jiuxi.core.core.context.TenantContextHolder;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @ClassName: TenantInterceptor
 * @Description: 租户拦截器
 * @Author: 杨攀
 * @Date: 2020/6/12 9:38
 * @Copyright: 2020 www.tuxun.net Inc. All rights reserved.
 */
public class TenantInterceptor implements HandlerInterceptor {


    /** 在header中的key */
    private String token_header;


    public TenantInterceptor(String token_header) {
        this.token_header = token_header;
    }

    /**
     * @description: 执行时机：在执行controller之前来执行
     * @author 杨攀
     * @date 2020/6/12 15:55
     * @param request
     * @param response
     * @param handler
     * @return boolean 返回值类型：boolean：true代表放行可以访问controller，false不可以访问controller
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // 判断请求是否是一个方法，即handler是HandlerMethod类型请求
        // 如不是，则不需要拦截，比如静态资源的请求
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        // 获取头部的token信息
        String token = request.getHeader(token_header);
        // 没有token，则放行
        if(StrUtil.isBlank(token) || "null".equals(token)){
            return true;
        }

        // 没有tenantId，则放行
        String tenantId = JwtTokenUtils.getTenantId(token);
        if(StrUtil.isBlank(tenantId)){
            return true;
        }

        TenantContextHolder.setTenantId(tenantId);
        return true;
    }


    /**
     * 在DispatcherServlet完全处理完请求后被调用，可用于清理资源等。返回处理（已经渲染了页面），
     * @author 杨攀
     * @date 2021/6/18 15:52
     * @param request
     * @param response
     * @param handler
     * @param ex
     * @return void
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 使用完ThreadLocal后，执行remove操作，避免出现内存溢出情况。
        TenantContextHolder.removeTenantId();
    }
}