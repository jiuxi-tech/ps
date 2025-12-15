package com.jiuxi.module.user.app.service;

import com.jiuxi.admin.core.bean.vo.TpAccountVO;

/**
 * @ClassName: TpPersonBasicinfoService
 * @Description: 人员基本信息表
 * @Author pand
 * @Date 2020-11-18 11:05:18
 * @Copyright: Hangzhou Jiuxi Inc. All rights reserved.
 */
public interface UserAccountService {

    /**
     * 新增人员账号信息
     *
     * @param vo: 账户信息
     * @return int
     * @author pand
     * @date 2020-11-24 14:56
     */
    int accountManage(TpAccountVO vo);

    /**
     * 新增账号
     *
     * @param vo              账号信息
     * @param decryptStrSM2   是否需要解密账号密码(后端直接调用时不需要，参数传false)
     * @return int
     * @author jiuxx
     * @date 2024/6/5 9:47
     */
    int accountManage(TpAccountVO vo, boolean decryptStrSM2);

    /**
     * 人员的账号数据入库，该接口接收的密码需要明文
     *
     * @param vo:
     * @return int
     * @author pand
     * @date 2021-03-03 16:51
     */
    int accountAdd(TpAccountVO vo);

    /**
     * 查看用户账号信息
     *
     * @param personId:
     * @return com.jiuxi.admin.core.bean.vo.TpAccountVO
     * @author pand
     * @date 2020-11-24 20:02
     */
    TpAccountVO accountView(String personId);

    /**
     * 查看用户账号信息
     *
     * @param accountId:
     * @return com.jiuxi.admin.core.bean.vo.TpAccountVO
     * @author pand
     * @date 2020-11-24 20:02
     */
    TpAccountVO selectByAccountId(String accountId);

    /**
     * 更新用户账号信息
     *
     * @param vo:
     * @return int
     * @author pand
     * @date 2020-11-24 20:13
     */
    int accountUpdate(TpAccountVO vo);

    /**
     * 账号入库操作
     *
     * @param vo:
     * @return int
     * @author pand
     * @date 2021-06-24 16:07
     */
    int accountInsert(TpAccountVO vo);

    /**
     * 找回密码
     *
     * @param phone: 手机号
     * @return int
     * @author pand
     * @date 2021-02-04 15:38
     */
    String accountFindpwd(String phone);

    /**
     * 邮箱找回密码
     *
     * @param email: 邮箱地址
     * @return String
     * @author system
     * @date 2024-01-21
     */
    String accountFindpwdByEmail(String email);

    /**
     * 新密码和账号验证码一起提交，修改密码
     *
     * @param phone:
     * @param vcode:
     * @return int
     * @author pand
     * @date 2021-02-04 15:38
     */
    int accountCheckVcode(String phone, String vcode, String userpwd);

    /**
     * 邮箱验证码校验并修改密码
     *
     * @param email: 邮箱地址
     * @param vcode: 验证码
     * @param userpwd: 新密码
     * @return int
     * @author system
     * @date 2024-01-21
     */
    int accountCheckVcodeByEmail(String email, String vcode, String userpwd);

    /**
     * 修改账号密码，先校验原密码
     *
     * @param personId:
     * @param oldUserpwd:
     * @param userpwd:
     * @return int
     * @author pand
     * @date 2020-12-08 18:26
     */
    int updatePwd(String personId, String oldUserpwd, String userpwd);

    /**
     * 强制修改密码，密码被管理员重置后，用户第一次登陆强制修改密码
     *
     * @param personId:
     * @param userpwd:
     * @return int
     * @author pand
     * @date 2020-12-08 18:26
     */
    int updatePwd(String personId, String userpwd);

    /**
     * 账号密码重置
     *
     * @param accountId: 账号id
     * @return int
     * @author pand
     * @date 2020-11-24 20:13
     */
    String accountResetpwd(String accountId);

    /**
     * 账号 冻结/解冻
     *
     * @param accountId: 账号id
     * @param locked:    冻结/解冻 0解冻，1冻结
     * @return int
     * @author pand
     * @date 2020-11-24 20:13
     */
    int accountLocked(String accountId, int locked);

    /**
     * 账号 启动/禁用
     *
     * @param accountId: 账号id
     * @param enabled:   启动/禁用
     * @return int
     * @author pand
     * @date 2020-11-24 20:15
     */
    int accountEnabled(String accountId, int enabled);

    /**
     * 根据手机号查询账户信息
     * @author Ypp
     * @date 2024/5/27 13:44
     * @param phone
     * @return com.jiuxi.admin.core.bean.vo.TpAccountVO
     */
    TpAccountVO getTpAccountByPhone(String phone);

    /**
     * 根据用户名查询账号信息
     *
     * @param username 用户名
     * @return com.jiuxi.admin.core.bean.vo.TpAccountVO
     * @author jiuxx
     * @date 2024/5/29 9:15
     */
    TpAccountVO getTpAccountByUsername(String username);

    /**
     * 根据邮箱查询账户信息
     *
     * @param email 邮箱地址
     * @return com.jiuxi.admin.core.bean.vo.TpAccountVO
     * @author system
     * @date 2024-01-21
     */
    TpAccountVO getTpAccountByEmail(String email);

    /**
     * 根据人员id，删除账号信息
     *
     * @param personId 人员id
     * @return void
     * @author jiuxx
     * @date 2024/5/29 11:08
     */
    void deleteByPersonId(String personId);

    /**
     * 根据人员id，物理删除账号信息
     *
     * @param personId 人员id
     * @return void
     * @author 系统生成
     * @date 2024/12/24
     */
    void physicalDeleteByPersonId(String personId);

    /**
     * 同步账号到Keycloak
     *
     * @param accountId 账号ID
     * @return boolean 同步是否成功
     * @author system
     * @date 2025-01-21
     */
    boolean syncAccountToKeycloak(String accountId);

}

