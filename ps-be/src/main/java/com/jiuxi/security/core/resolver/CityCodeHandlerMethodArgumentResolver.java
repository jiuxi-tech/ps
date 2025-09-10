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
 * @ClassName: CityCodeHandlerMethodArgumentResolver
 * @Description: cityCode参数解析器
 * @Author: 杨攀
 * @Date: 2020/5/28 21:05
 * @Copyright: 2020 www.tuxun.net Inc. All rights reserved.
 */
public class CityCodeHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {

    @Autowired
    private SecurityConfigurationProperties properties;

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {

        String name = methodParameter.getParameterName();
        if ("jwtCityCode".equalsIgnoreCase(name)) {
            return true;
        }
        return false;
    }

    /**
     * 解析头部token，获取当前登陆人所在部门的行政区划code放到请求参数里面
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

        return sessionVO.getCityCode();
    }
}
