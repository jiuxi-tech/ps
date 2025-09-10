package com.jiuxi.security.core.resolver;

import cn.hutool.core.util.StrUtil;
import com.jiuxi.common.util.CommonRequestUtil;
import com.jiuxi.security.autoconfig.SecurityConfigurationProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * @ClassName: TokenHandlerMethodArgumentResolver
 * @Description: Token参数解析器
 * @Author: 杨攀
 * @Date: 2020/5/28 14:58
 * @Copyright: 2020 www.tuxun.net Inc. All rights reserved.
 */
public class TokenHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {

    /**
     * token 空字符串
     */
    private static final String TOKEN_NULL = "null";

    @Autowired
    private SecurityConfigurationProperties properties;

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        String name = methodParameter.getParameterName();
        return "token".equalsIgnoreCase(name) || "jwttoken".equalsIgnoreCase(name);
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {

        // 获取头部的token信息
        String token = CommonRequestUtil.getHeaderParam("token");
        // 没有 token，则放行
        if(StrUtil.isBlank(token) || TOKEN_NULL.equalsIgnoreCase(token)){
            return "";
        }

        return token;
    }
}
