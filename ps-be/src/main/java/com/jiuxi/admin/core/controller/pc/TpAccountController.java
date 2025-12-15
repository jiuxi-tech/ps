package com.jiuxi.admin.core.controller.pc;

import cn.hutool.core.util.StrUtil;
import com.jiuxi.module.user.app.service.UserAccountService;
import com.jiuxi.admin.core.service.TpAccountService;
import com.jiuxi.common.bean.JsonResponse;
import com.jiuxi.shared.common.annotation.IgnoreAuthorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description: 账号表
 * @ClassName: TpAccountController
 * @Author: pdd
 * @Date: 2021-06-17 15:34
 * @Copyright: 2021 Hangzhou Jiuxi Inc. All rights reserved.
 */
@RestController
@RequestMapping("/sys/account")
public class TpAccountController {

    @Autowired
    private TpAccountService tpAccountService;
    
    @Autowired
    private UserAccountService userAccountService;

    /**
     * 修改密码
     */
    @RequestMapping("/update")
    @IgnoreAuthorization
    public JsonResponse update(String userpwd, String jwtpid) {
        if (StrUtil.isBlank(userpwd)) {
            return JsonResponse.buildFailure("请输入密码！");
        }
        
        if (StrUtil.isBlank(jwtpid)) {
            return JsonResponse.buildFailure("用户身份信息缺失！");
        }

        userAccountService.updatePwd(jwtpid, userpwd);
        return JsonResponse.buildSuccess();
    }

}