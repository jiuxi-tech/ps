package com.jiuxi.admin.core.controller.app;


import com.jiuxi.admin.autoconfig.AdminConfigurationProperties;
import com.jiuxi.common.constant.TpConstant;
import com.jiuxi.admin.core.bean.vo.TpMemVerificationCodeVO;
import com.jiuxi.module.user.app.service.UserAccountService;
import com.jiuxi.admin.core.service.TpSmsSendService;
import com.jiuxi.admin.core.service.TpAccountService;
import com.jiuxi.common.bean.JsonResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description: 短信服务
 * @ClassName: TestDemoController
 * @Author: pdd
 * @Date: 2021-03-08 10:03
 * @Copyright: 2021 Hangzhou Jiuxi Inc. All rights reserved.
 */
@RestController
@RequestMapping("/app/sys/sms")
public class SmsControllerApp {

    private static final Logger LOGGER = LoggerFactory.getLogger(SmsControllerApp.class);

    @Autowired(required = false)
    @Qualifier(value = "tpDBSmsSendService")
    private TpSmsSendService tpSmsSendService;

    @Autowired
    private TpAccountService tpAccountService;

    @Autowired
    private UserAccountService userAccountService;

    @Autowired
    private AdminConfigurationProperties adminProperties;


    /**
     * 找回密码,发送验证码
     */
    @RequestMapping("/account-findpwd")
    public JsonResponse accountFindpwd(@RequestParam(value = "phone") String phone) {
        String userName = userAccountService.accountFindpwd(phone);

        return JsonResponse.buildSuccess(userName);
    }

    /**
     * 找回密码,校验验证码并修改密码
     */
    @RequestMapping("/check-vcode")
    public JsonResponse accountCheckVcode(String phone, String vcode, String userpwd) {
        int count = userAccountService.accountCheckVcode(phone, vcode, userpwd);
        if (count == 1) {
            return JsonResponse.buildSuccess("密码修改成功！");
        } else {
            return JsonResponse.buildSuccess("密码修改失败！");
        }
    }

    /**
     * tag：每个项目注册短信模版不一样，该接口当作demo，发送注册短信具体项目可以参考该接口
     * 注册发送验证码。
     */
    // @RequestMapping("/send")
    public JsonResponse send(@Validated() TpMemVerificationCodeVO vo) {
        vo.setBusType("REGIST_BUS_TYPE_001");

        tpSmsSendService.send(vo, adminProperties.getSmsCode().getTemplatecode().get(TpConstant.SMSCode.PWDTEMPLATECODE), TpConstant.SMSCode.PWDCODEKEY);

        return JsonResponse.buildSuccess();
    }

    /**
     * tag：每个项目注册短信模版不一样，该接口当作demo，校验短信具体项目可以参考该接口
     * 校验短信验证码
     */
    // @RequestMapping("/check")
    public JsonResponse check(String phone, String vcode) {

        boolean flag = tpSmsSendService.check(phone, "REGIST_BUS_TYPE_001", vcode);
        return JsonResponse.buildSuccess(flag);
    }


}
