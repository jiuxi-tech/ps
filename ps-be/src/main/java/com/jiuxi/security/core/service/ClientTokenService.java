package com.jiuxi.security.core.service;

import com.jiuxi.security.core.entity.vo.AccountThirdVO;

/**
 * @Description: 合作方登陆认证，Token生成service
 * @ClassName: ClentTokenService
 * @Author: pand
 * @Date: 2022-05-25 16:33
 * @Copyright: 2022 www.tuxun.net Inc. All rights reserved.
 */

public interface ClientTokenService {

    AccountThirdVO login(String appKey, String appSecret);

}
