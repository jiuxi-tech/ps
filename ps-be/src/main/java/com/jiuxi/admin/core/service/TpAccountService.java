package com.jiuxi.admin.core.service;

import com.jiuxi.admin.core.bean.vo.TpAccountVO;

/**
 * 账号服务适配器接口 - 为了兼容旧代码
 * 实际实现将委托给新的用户模块服务
 * 
 * @deprecated 请使用 com.jiuxi.module.user.app.service.UserAccountService
 */
@Deprecated
public interface TpAccountService {

    int accountManage(TpAccountVO vo);
    int accountManage(TpAccountVO vo, boolean decryptStrSM2);
    int accountAdd(TpAccountVO vo);
    TpAccountVO accountView(String personId);
    int updatePwd(String jwtpid, String oldUserpwd, String userpwd);
    void updatePwd(String jwtpid, String userpwd);
    String accountResetpwd(String accountId);
    String accountFindpwd(String phone);
    String accountFindpwdByEmail(String email);
    int accountCheckVcode(String phone, String vcode, String userpwd);
    int accountCheckVcodeByEmail(String email, String vcode, String userpwd);
    int accountLocked(String accountId, Integer locked);
    int accountEnabled(String accountId, Integer enabled);
    int accountInsert(TpAccountVO vo);
    boolean syncAccountToKeycloak(String accountId);
}