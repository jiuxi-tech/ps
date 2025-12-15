package com.jiuxi.security.core.service;


/**
 * TopinfoSecurityLogoutService
 * @Description: 退出登录接口
 * @author jiuxx
 * @date 2024/10/8 13:44
 */
public interface TopinfoSecurityLogoutService {

    /**
     * 退出登录
     *
     * @param token
     * @return com.jiuxi.common.bean.JsonResponse
     * @author jiuxx
     * @date 2024/10/8 13:26
     */
    void logout(String token);


    /**
     * 判断当前token是否是无效的
     *
     * @param token
     * @return boolean 无效返回true
     * @author jiuxx
     * @date 2024/10/8 15:13
     */
    boolean isInvalidToken(String token);


    /**
     * 把token放入缓存，将token失效
     *
     * @param jti
     * @return void
     * @author jiuxx
     * @date 2025/2/7 17:49
     */
    void invalidateToken(String jti);
}
