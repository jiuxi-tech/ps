package com.jiuxi.security.core.service;


import com.jiuxi.security.core.entity.vo.AccountVO;

/**
 * 手机号/验证码登陆的特殊接口是需要发送验证码，所以继承PersonService服务实现查询用户，并定义自己的发送验证码特殊接口。
 *
 * @author pand
 * @date 2020-08-28 15:42
 */
public interface SmsAccountService extends AccountService {

    /**
     * 根据手机号验证用户是否存在，存在并发送验证码
     *
     * @param phone: 手机号
     * @return boolean
     * @author pand
     * @date 2020-08-28 15:44
     */
    boolean sendSmsValidateCode(String phone);

    /**
     * 根据手机号查询用户信息
     *
     * @param phone: 手机号
     * @return com.jiuxi.security.core.entity.vo.SmsPersonVO
     * @author pand
     * @date 2020-08-28 16:03
     */
    AccountVO queryPersonByPhone(String phone);

}
