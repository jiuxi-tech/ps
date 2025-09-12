package com.jiuxi.admin.core.controller.app;

import cn.hutool.core.util.StrUtil;
import com.jiuxi.admin.core.service.TpAccountService;
import com.jiuxi.common.bean.JsonResponse;
import com.jiuxi.shared.common.annotation.IgnoreAuthorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description: 账号表
 * @ClassName: TpAccountController
 * @Author: pand
 * @Date: 2021-06-17 15:34
 * @Copyright: 2021 www.tuxun.net Inc. All rights reserved.
 */
@RestController
@RequestMapping("/app/sys/account")
public class TpAccountControllerApp {


    @Autowired
    private TpAccountService tpAccountService;

    /**
     * 修改
     */
    @RequestMapping("/update")
    @IgnoreAuthorization
    public JsonResponse update(String userpwd, String confirmUserPwd, String jwtpid) {
        if (StrUtil.equals(userpwd, confirmUserPwd)) {
            JsonResponse.buildFailure("密码与确认密码不一致！");
        }

        tpAccountService.updatePwd(jwtpid, userpwd);
        return JsonResponse.buildSuccess();
    }

}
