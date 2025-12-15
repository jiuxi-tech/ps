package com.jiuxi.admin.core.controller.app;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jiuxi.common.constant.TpConstant;
import com.jiuxi.admin.core.bean.query.TpPersonBasicQuery;
import com.jiuxi.admin.core.bean.vo.TpPersonBasicinfoVO;
import com.jiuxi.module.user.app.service.UserAccountService;
import com.jiuxi.admin.core.service.TpCityService;
import com.jiuxi.module.user.app.service.UserPersonService;
import com.jiuxi.admin.core.service.TpPersonBasicinfoService;
import com.jiuxi.admin.core.service.TpAccountService;
import com.jiuxi.common.bean.JsonResponse;
import com.jiuxi.shared.common.annotation.Authorization;
import com.jiuxi.shared.common.annotation.IgnoreAuthorization;
import com.jiuxi.shared.common.validation.group.UpdateGroup;
import com.jiuxi.security.core.entity.vo.PersonVO;
import com.jiuxi.security.core.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName: TpPersonBasicinfoController
 * @Description: 人员基本信息表
 * @Author pand
 * @Date 2020-11-18 11:05:18
 * @Copyright: Hangzhou Jiuxi Inc. All rights reserved.
 */
@RestController
@RequestMapping("/app/sys/person")
@Authorization
public class TpPersonBasicinfoControllerApp {

    @Autowired
    private TpPersonBasicinfoService tpPersonBasicinfoService;

    @Autowired
    private TpAccountService tpAccountService;

    @Autowired
    private PersonService personService;

    @Autowired
    private TpCityService cityService;

    @Autowired
    private UserAccountService userAccountService;

    /**
     * 登录成功后,获取用户信息
     */
    @RequestMapping("/getUserInfo")
    @IgnoreAuthorization
    public JsonResponse getUserInfo(String jwtdid, String jwtpid, String jwtrids, String jwtCityCode) {

        PersonVO vo = personService.getUserInfo(jwtdid, jwtpid);
        vo.setRoleIds(jwtrids);
        vo.setCityCode(jwtCityCode);
        String cityName = cityService.selectCityNameByCityCode(jwtCityCode);
        vo.setCityName(cityName);

        return JsonResponse.buildSuccess(vo);
    }

    /**
     * 政府人员列表
     */
    @RequestMapping(value = "/org/list")
    @IgnoreAuthorization
    public JsonResponse orgList(TpPersonBasicQuery query) {
        query.setCategory(TpConstant.Category.ORG);
        IPage<TpPersonBasicinfoVO> page = tpPersonBasicinfoService.queryPage(query);
        return JsonResponse.buildSuccess(page);
    }

    /**
     * 企业人员列表
     */
    @RequestMapping(value = "/ent/list")
    @IgnoreAuthorization
    public JsonResponse entList(TpPersonBasicQuery query) {
        if (StrUtil.isBlank(query.getDeptId())) {
            return JsonResponse.buildFailure("请选择部门查询！");
        }
        query.setCategory(TpConstant.Category.ENT);
        IPage<TpPersonBasicinfoVO> page = tpPersonBasicinfoService.queryPage(query);
        return JsonResponse.buildSuccess(page);
    }

    /**
     * 修改密码
     */
    @RequestMapping(value = "/update-pwd")
    @IgnoreAuthorization
    public JsonResponse updatePwd(String jwtpid, String oldUserpwd, String userpwd) {
        int count = userAccountService.updatePwd(jwtpid, oldUserpwd, userpwd);

        return JsonResponse.buildSuccess(count);
    }

    /**
     * 重置密码
     */
    @RequestMapping("/account-resetpwd")
    public JsonResponse accountResetpwd(String accountId) {
        String restPwd = userAccountService.accountResetpwd(accountId);

        return JsonResponse.buildSuccess(restPwd);
    }

    /**
     * 账号 冻结/解冻
     */
    @RequestMapping("/account-locked")
    public JsonResponse accountLocked(String accountId, Integer locked) {
        int count = userAccountService.accountLocked(accountId, locked);

        return JsonResponse.buildSuccess(count);
    }

    /**
     * 账号 启动/禁用
     */
    @RequestMapping("/account-enabled")
    public JsonResponse accountEnabled(String accountId, Integer enabled) {
        int count = userAccountService.accountEnabled(accountId, enabled);

        return JsonResponse.buildSuccess(count);
    }

    /**
     * 查看用户信息
     */
    @RequestMapping(value = "/view")
    public JsonResponse view(String personId, String deptId) {
        TpPersonBasicinfoVO vo = tpPersonBasicinfoService.view(personId, deptId);

        return JsonResponse.buildSuccess(vo);
    }

    /**
     * 修改用户基本信息
     */
    @RequestMapping(value = "/update")
    public JsonResponse update(@Validated(value = UpdateGroup.class) @RequestBody TpPersonBasicinfoVO vo, String jwtpid) {
        int count = tpPersonBasicinfoService.update(vo, jwtpid);

        return JsonResponse.buildSuccess(count);
    }
}
