package com.jiuxi.security.core.service.impl;

import com.jiuxi.common.bean.ErrorCode;
import com.jiuxi.common.util.JwtUtil;
import com.jiuxi.shared.common.exception.TopinfoRuntimeException;
import com.jiuxi.security.core.service.AuthenticationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @ClassName: AuthenticationServiceImpl
 * @Description: 认证默认实现
 * @Author: 杨攀
 * @Date: 2020/5/25 16:13
 * @Copyright: 2020 www.tuxun.net Inc. All rights reserved.
 */
public class AuthenticationServiceImpl implements AuthenticationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationServiceImpl.class);


    @Autowired(required = false)
    protected JdbcTemplate jdbcTemplate;


    @Override
    public boolean authentication(String token, HttpServletRequest request, HttpServletResponse response) {
        try {
            return JwtUtil.checkToken(token);
        }catch (RuntimeException e){
            LOGGER.error("token 认证失败，非法请求！请求地址为:{}", request.getRequestURI());
            throw new TopinfoRuntimeException(ErrorCode.TOKEN_ERROR.getCode(), ErrorCode.TOKEN_ERROR.getMsg());
        }
    }

}
