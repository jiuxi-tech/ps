package com.jiuxi.security.core.service.impl;

import com.jiuxi.security.core.entity.vo.AccountVO;
import com.jiuxi.security.core.service.AccountService;
import com.jiuxi.security.core.service.AbstractLoginService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Description: 手机号/验证码登陆实现
 * @ClassName: SmsLoginServiceImpl
 * @Author: pdd
 * @Date: 2020-08-28 13:41
 * @Copyright: 2020 Hangzhou Jiuxi Inc. All rights reserved.
 */
public class SmsLoginServiceImpl extends AbstractLoginService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SmsLoginServiceImpl.class);

    private AccountService accountService;

    public SmsLoginServiceImpl(AccountService accountService) {
        this.accountService = accountService;
    }

    /**
     * 手机号验证码登陆实现
     *
     * @param vo:
     * @return com.jiuxi.security.core.entity.vo.PersonVO
     * @author pand
     * @date 2020-08-28 15:46
     */
    @Override
    public AccountVO login(AccountVO vo) {
        // 根据手机号查询用户/账号
        vo = accountService.queryAccount(vo);
        // 查询用户所在部门，拥有角色
        vo = this.initPersonVo(vo);

        return vo;
    }

}
