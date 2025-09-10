package com.jiuxi.security.core.service.impl;

import com.jiuxi.security.core.entity.vo.AccountVO;
import com.jiuxi.security.core.service.AbstractLoginService;
import com.jiuxi.security.core.service.AccountService;

/**
 * @Description: 客户端获取token信息，根据用户名/密码（等同于 clientId和clientSecurity）生成token
 * @ClassName: ClientLoginServiceImpl
 * @Author: pand
 * @Date: 2020-08-31 13:53
 * @Copyright: 2020 www.tuxun.net Inc. All rights reserved.
 */
public class ClientLoginServiceImpl extends AbstractLoginService {

    private AccountService personService;

    public ClientLoginServiceImpl(AccountService personService) {
        this.personService = personService;
    }

    /**
     * 根据用户名密码生成token
     *
     * @param vo:
     * @return com.jiuxi.security.core.entity.vo.PersonVO
     * @author pand
     * @date 2020-08-31 14:14
     */
    @Override
    public AccountVO login(AccountVO vo) {
        // 根据用户名查询账号/用户
        vo = personService.queryAccount(vo);
        return vo;
    }
}
