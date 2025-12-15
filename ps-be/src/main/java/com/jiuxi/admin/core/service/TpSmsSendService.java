package com.jiuxi.admin.core.service;

import com.jiuxi.admin.core.bean.vo.TpMemVerificationCodeVO;

/**
 * @Description: 发送短信
 * @ClassName: SmsVerificationCodeSendService
 * @Author: pdd
 * @Date: 2021-04-22 17:00
 * @Copyright: 2021 Hangzhou Jiuxi Inc. All rights reserved.
 */

public interface TpSmsSendService {

    /**
     * 发送短信验证码，模版替换的key固定为code，如果不为code需要将key值传入
     * 1。生成验证码并发送短信（同一个号码同一种业务需要控制发送间隔时长）
     * 2。短信发送成功，将手机号，验证码等信息入库
     * 3。返回成功/失败
     *
     * @param vo:
     * @param templatCode: 发送短信的模版code
     * @param codeKey:     模版替换的key，默认code
     * @return java.lang.String
     * @author pand
     * @date 2021-04-22 17:15
     */
    void send(TpMemVerificationCodeVO vo, String templatCode, String codeKey);

    /**
     * 校验短信验证码
     * 1。根据手机号，业务类型查询最近的一条短信验证码
     * 2。判断短信验证码是否过期
     *
     * @param phone:
     * @param busType:
     * @param vcode:
     * @return com.jiuxi.admin.core.bean.entity.TpMemVerificationCode
     * @author pand
     * @date 2021-04-22 17:17
     */
    boolean check(String phone, String busType, String vcode);

}
