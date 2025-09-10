package com.jiuxi.security.core.listener;

import com.jiuxi.core.core.event.TpRoleAuthorizationEvent;
import com.jiuxi.security.core.service.AuthorizationCacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;

/**
 * @ClassName: TpRoleAuthorizationLisenter
 * @Description: 角色授权的监听，用于清除 角色菜单的缓存
 * @Author: 杨攀
 * @Date: 2022/11/24 9:49
 * @Copyright: 2022 www.tuxun.net Inc. All rights reserved.
 */
public class TpRoleAuthorizationLisenter implements ApplicationListener<TpRoleAuthorizationEvent> {

    @Autowired(required = false)
    private AuthorizationCacheService authorizationCacheService;

    @Override
    public void onApplicationEvent(TpRoleAuthorizationEvent event) {
        if(null != authorizationCacheService){
            // 清除 角色菜单的缓存
            authorizationCacheService.removeAuthorizationCacheInfo();
        }
    }
}
