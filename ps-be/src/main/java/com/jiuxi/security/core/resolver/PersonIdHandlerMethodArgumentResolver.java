package com.jiuxi.security.core.resolver;

import com.jiuxi.security.autoconfig.SecurityConfigurationProperties;
import com.jiuxi.common.bean.SessionVO;
import com.jiuxi.security.core.holder.SessionHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * @ClassName: PersonIdHandlerMethodArgumentResolver
 * @Description: PersonId参数解析器
 * @Author: 杨攀
 * @Date: 2020/5/28 14:58
 * @Copyright: 2020 www.tuxun.net Inc. All rights reserved.
 */
public class PersonIdHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {

    @Autowired
    private SecurityConfigurationProperties properties;

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        String name = methodParameter.getParameterName();
        return "jwtpid".equalsIgnoreCase(name);
    }

    /**
     * 解析头部token，获取用户id放到请求参数里面
     *
     * @param methodParameter:
     * @return boolean
     * @author pand
     * @date 2020-09-14 14:39
     */
    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {

        // 获取sessionVO
        SessionVO sessionVO = SessionHolder.get();

        if (null == sessionVO) {
            return null;
        }

        return sessionVO.getPersonId();
    }
}
