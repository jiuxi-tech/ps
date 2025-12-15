package com.jiuxi.security.core.service;

import com.jiuxi.security.core.entity.vo.ValidateCodeVO;

/**
 * @Description: 验证码接口定义
 * @ClassName: ValidateCodeService
 * @Author: pdd
 * @Date: 2020-08-27 13:24
 * @Copyright: 2020 Hangzhou Jiuxi Inc. All rights reserved.
 */
public interface ValidateCodeService {
    /**
     * 生成验证码
     * TODO 需要自己实现
     *
     * @return com.jiuxi.security.core.entity.vo.ValidateCodeVO
     * @author pand
     * @date 2020-08-27 11:16
     */
    ValidateCodeVO generate();


    /**
     * 保存验证码
     * TODO 需要自己实现
     *
     * @param phone:
     * @param validateCode:
     * @return boolean true: 成功  false: 失败
     * @author pand
     * @date 2020-08-27 11:16
     */
    boolean save(String phone, ValidateCodeVO validateCode);


    /**
     * 根据手机号获取验证码
     * TODO 需要自己实现
     *
     * @param phone: 手机号
     * @return String 验证码的值
     * @author pand
     * @date 2020-08-27 11:16
     */
    ValidateCodeVO find(String phone);

    /**
     * 向手机发送验证码
     * TODO 需要自己实现
     *
     * @param phone:
     * @param code:
     * @return boolean true: 成功  false: 失败
     * @author pand
     * @date 2020-08-27 11:16
     */
    boolean send(String phone, String code);


    /**
     * 校验验证码
     *
     * @param phone: 手机号码
     * @param code:  验证码
     * @return boolean  true: 成功  false: 失败
     * @author pand
     * @date 2020-08-27 13:39
     */
    boolean validate(String phone, String code);
}
