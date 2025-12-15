package com.jiuxi.security.core.service;


/**
 * @author Ypp
 * @description: 鉴权接口
 * @date 2020/5/22 16:49
 */
public interface AuthorizationService {

    /**
     * 校验鉴权
     *
     * @param token:
     * @param path:
     * @return boolean
     * @author pand
     * @date 2020-08-25 17:00
     */
    boolean authorization(String token, String path);


}
