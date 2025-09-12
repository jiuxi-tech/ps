package com.jiuxi.admin.core.service.impl;

import com.jiuxi.admin.core.bean.vo.TpAccountVO;
import com.jiuxi.admin.core.service.TpAccountService;
import com.jiuxi.module.user.app.service.UserAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 账号服务适配器实现 - 委托给新的用户模块服务
 * 
 * @deprecated 为了兼容旧代码而保留
 */
@Deprecated
@Service("tpAccountService")
public class TpAccountServiceAdapter implements TpAccountService {

    @Autowired
    private UserAccountService userAccountService;

    @Override
    public int accountManage(TpAccountVO vo) {
        return userAccountService.accountManage(vo);
    }

    @Override
    public int accountManage(TpAccountVO vo, boolean decryptStrSM2) {
        return userAccountService.accountManage(vo, decryptStrSM2);
    }

    @Override
    public int accountAdd(TpAccountVO vo) {
        return userAccountService.accountAdd(vo);
    }

    @Override
    public TpAccountVO accountView(String personId) {
        return userAccountService.accountView(personId);
    }

    @Override
    public int updatePwd(String jwtpid, String oldUserpwd, String userpwd) {
        return userAccountService.updatePwd(jwtpid, oldUserpwd, userpwd);
    }

    @Override
    public void updatePwd(String jwtpid, String userpwd) {
        userAccountService.updatePwd(jwtpid, userpwd);
    }

    @Override
    public String accountResetpwd(String accountId) {
        return userAccountService.accountResetpwd(accountId);
    }

    @Override
    public String accountFindpwd(String phone) {
        return userAccountService.accountFindpwd(phone);
    }

    @Override
    public String accountFindpwdByEmail(String email) {
        return userAccountService.accountFindpwdByEmail(email);
    }

    @Override
    public int accountCheckVcode(String phone, String vcode, String userpwd) {
        return userAccountService.accountCheckVcode(phone, vcode, userpwd);
    }

    @Override
    public int accountCheckVcodeByEmail(String email, String vcode, String userpwd) {
        return userAccountService.accountCheckVcodeByEmail(email, vcode, userpwd);
    }

    @Override
    public int accountLocked(String accountId, Integer locked) {
        return userAccountService.accountLocked(accountId, locked);
    }

    @Override
    public int accountEnabled(String accountId, Integer enabled) {
        return userAccountService.accountEnabled(accountId, enabled);
    }

    @Override
    public int accountInsert(TpAccountVO vo) {
        return userAccountService.accountInsert(vo);
    }

    @Override
    public boolean syncAccountToKeycloak(String accountId) {
        return userAccountService.syncAccountToKeycloak(accountId);
    }
}