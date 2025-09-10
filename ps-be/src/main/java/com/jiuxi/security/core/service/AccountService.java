package com.jiuxi.security.core.service;

import com.jiuxi.security.core.entity.vo.AccountVO;

/**
 * 统一用户接口类，具体查询用户实现接口即可
 *
 * @author pand
 * @date 2020-08-28 15:39
 */
public interface AccountService {

    /**
     * 查询用户信息
     *
     * @param vo: 用户的具体实体类 如SmsPersonVO，PwdPersonVO等。
     * @return com.jiuxi.security.core.entity.vo.PersonVO
     * @author pand
     * @date 2020-08-28 15:39
     */
    AccountVO queryAccount(AccountVO vo);


        /**
         * 根据用户名查询账户信息
         * 
         * @param userName 用户名
         * @return 返回对应的账户信息，如果未找到则返回null
         */
        AccountVO queryAccountByUsername(String userName);

}
