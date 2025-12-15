package com.jiuxi.admin.core.controller.pc;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jiuxi.admin.core.bean.entity.TpApiDefinition;
import com.jiuxi.admin.core.bean.query.TpThirdPartyAppQuery;
import com.jiuxi.admin.core.bean.vo.TpThirdPartyAppVO;
import com.jiuxi.admin.core.service.TpApiDefinitionService;
import com.jiuxi.admin.core.service.TpThirdPartyAppService;
import com.jiuxi.common.bean.JsonResponse;
import com.jiuxi.common.bean.SessionVO;
import com.jiuxi.security.core.holder.SessionHolder;
import com.jiuxi.shared.common.annotation.Authorization;
import com.jiuxi.shared.common.validation.group.AddGroup;
import com.jiuxi.shared.common.validation.group.UpdateGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @ClassName: TpThirdPartyAppController
 * @Description: 第三方应用管理
 * @Author system
 * @Date 2025-01-28
 * @Copyright: Hangzhou Jiuxi Inc. All rights reserved.
 */
@RestController
@RequestMapping("/sys/third-party-app")
@Authorization
public class TpThirdPartyAppController {

    /**
     * 接口配置 passKey
     */
    private static final String PASS_KEY = "appId";

    @Autowired
    private TpThirdPartyAppService tpThirdPartyAppService;

    @Autowired
    private TpApiDefinitionService tpApiDefinitionService;

    /**
     * 第三方应用列表（分页查询）
     */
    @RequestMapping("/list")
    public JsonResponse list(TpThirdPartyAppQuery query, String jwtpid) {
        IPage<TpThirdPartyAppVO> page = tpThirdPartyAppService.queryPage(query);
        return JsonResponse.buildSuccess(page).buildPassKey(jwtpid, PASS_KEY);
    }

    /**
     * 第三方应用列表（不分页）
     */
    @RequestMapping("/all-list")
    public JsonResponse allList(TpThirdPartyAppQuery query) {
        List<TpThirdPartyAppVO> list = tpThirdPartyAppService.getList(query);
        return JsonResponse.buildSuccess(list);
    }

    /**
     * 新增第三方应用
     */
    @PostMapping("/add")
    public JsonResponse add(@Validated(value = AddGroup.class) TpThirdPartyAppVO vo, String jwtpid) {
        TpThirdPartyAppVO result = tpThirdPartyAppService.add(vo, jwtpid);
        return JsonResponse.buildSuccess(result);
    }

    /**
     * 查看第三方应用详情
     */
    @GetMapping("/view")
    @Authorization(businessKey = PASS_KEY)
    public JsonResponse view(String appId) {
        TpThirdPartyAppVO vo = tpThirdPartyAppService.view(appId);
        return JsonResponse.buildSuccess(vo);
    }

    /**
     * 修改第三方应用
     */
    @PostMapping("/update")
    @Authorization(businessKey = PASS_KEY)
    public JsonResponse update(@Validated(value = UpdateGroup.class) TpThirdPartyAppVO vo, String jwtpid) {
        int count = tpThirdPartyAppService.update(vo, jwtpid);
        return JsonResponse.buildSuccess(count);
    }

    /**
     * 删除第三方应用
     */
    @PostMapping("/delete")
    @Authorization(businessKey = PASS_KEY)
    public JsonResponse delete(String appId, String jwtpid) {
        tpThirdPartyAppService.delete(appId, jwtpid);
        return JsonResponse.buildSuccess();
    }

    /**
     * 重新生成API Secret
     */
    @PostMapping("/regenerate-secret")
    @Authorization(businessKey = PASS_KEY)
    public JsonResponse regenerateSecret(String appId, String jwtpid) {
        String newSecret = tpThirdPartyAppService.regenerateSecret(appId, jwtpid);
        return JsonResponse.buildSuccess(newSecret);
    }

    /**
     * 配置应用API权限
     */
    @PostMapping("/config-permissions")
    @Authorization(businessKey = PASS_KEY)
    public JsonResponse configPermissions(@RequestBody TpThirdPartyAppVO vo, String jwtpid) {
        int count = tpThirdPartyAppService.configPermissions(vo.getAppId(), vo.getApiIds(), jwtpid);
        return JsonResponse.buildSuccess(count);
    }

    /**
     * 查询应用已授权的API列表
     */
    @GetMapping("/app-apis")
    @Authorization(businessKey = PASS_KEY)
    public JsonResponse getAppApis(String appId) {
        List<TpApiDefinition> apis = tpThirdPartyAppService.getAppApis(appId);
        return JsonResponse.buildSuccess(apis);
    }

    /**
     * 查询所有可用的API清单（用于权限配置页面）
     */
    @GetMapping("/available-apis")
    public JsonResponse getAvailableApis() {
        List<TpApiDefinition> apis = tpApiDefinitionService.getEnabledApis();
        return JsonResponse.buildSuccess(apis);
    }

    /**
     * 更新应用状态
     */
    @PostMapping("/update-status")
    @Authorization(businessKey = PASS_KEY)
    public JsonResponse updateStatus(String appId, Integer status, String jwtpid) {
        int count = tpThirdPartyAppService.updateStatus(appId, status, jwtpid);
        return JsonResponse.buildSuccess(count);
    }
}
