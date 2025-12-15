package com.jiuxi.security.core.listener;

/**
 * @Description: 登陆监听器服务接口
 * @ClassName: LoginApplicationService
 * @Author: pdd
 * @Date: 2020-10-20 15:51
 * @Copyright: 2020 Hangzhou Jiuxi Inc. All rights reserved.
 */

public interface LoginApplicationService {


    /**
     * 执行方法
     *
     * @param loginSuccess: 登陆成功标识
     * @param accountId:    账号id
     * @return void
     * @author pand
     * @date 2020-10-20 15:55
     */
    void exceute(boolean loginSuccess, String accountId);

}
