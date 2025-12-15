package com.jiuxi.security.core.service;

import com.jiuxi.security.core.entity.vo.AccountVO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Ypp
 * @description: 认证
 * @date 2020/5/25 16:11
 */
public interface AuthenticationService {


    /**
     * @param token
     * @param response
     * @return boolean
     * @description: 认证
     * @author Ypp
     * @date 2020/5/25 16:12
     */
    boolean authentication(String token, HttpServletRequest request, HttpServletResponse response);



}
