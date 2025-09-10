package com.jiuxi.security.core.service;

/**
 * @ClassName: AuthorizationCacheService
 * @Description: 鉴权缓存Service
 * @Author: 杨攀
 * @Date: 2022/11/25 11:18
 * @Copyright: 2022 www.tuxun.net Inc. All rights reserved.
 */
public interface AuthorizationCacheService {


    /**
     * 获取鉴权缓存信息
     * @author 杨攀
     * @date 2022/11/25 11:20
     * @param roles 角色类别，多个逗号分隔开
     * @param path 请求的路径
     * @return java.lang.String
     */
    String getAuthorizationCacheInfo(String roles, String path);

    /**
     * 保存鉴权缓存信息
     * @author 杨攀
     * @date 2022/11/25 11:21
     * @param roles 角色类别，多个逗号分隔开
     * @param path 请求的路径
     * @param result 保存结果
     * @return void
     */
    void putAuthorizationCacheInfo(String roles, String path, String result);

    /**
     * 删除缓存信息
     * @author 杨攀
     * @date 2022/11/25 11:28
     * @param
     * @return void
     */
    void  removeAuthorizationCacheInfo();
}
