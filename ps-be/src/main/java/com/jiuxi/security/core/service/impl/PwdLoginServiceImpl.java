package com.jiuxi.security.core.service.impl;

import com.jiuxi.security.core.entity.vo.AccountVO;
import com.jiuxi.security.core.service.AbstractLoginService;
import com.jiuxi.security.core.service.AccountService;

/**
 * @Description: 用户名/密码登陆实现
 * @ClassName: PwdLoginServiceImpl
 * @Author: pand
 * @Date: 2020-08-28 13:42
 * @Copyright: 2020 www.tuxun.net Inc. All rights reserved.
 */
public class PwdLoginServiceImpl extends AbstractLoginService {

    private AccountService personService;

    public PwdLoginServiceImpl(AccountService personService) {
        this.personService = personService;
    }

    /**
     * 用户名/密码登陆实现
     *
     * @param vo:
     * @return com.jiuxi.security.core.entity.vo.PersonVO
     * @author pand
     * @date 2020-08-28 15:47
     */
    @Override
    public AccountVO login(AccountVO vo) {
        // 根据用户名查询账号/用户
        vo = personService.queryAccount(vo);
        // 查询用户所在部门，权限等
        vo = this.initPersonVo(vo);

        return vo;
    }
}
