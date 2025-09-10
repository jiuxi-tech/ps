package com.jiuxi.admin.core.controller.pc;

import com.jiuxi.admin.core.bean.entity.TpTenant;
import com.jiuxi.admin.core.service.TpTenantService;
import com.jiuxi.common.bean.JsonResponse;
import com.jiuxi.mybatis.util.PageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @ClassName: TpTenantController
 * @Description: 租户表
 * @Author pand
 * @Date 2020-11-18 11:05:18
 * @Copyright: www.tuxun.net Inc. All rights reserved.
 */
@RestController
@RequestMapping("admin/tptenant")
public class TpTenantController {

    @Autowired
    private TpTenantService tpTenantService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public JsonResponse list(@RequestParam Map<String, Object> params) {
        PageUtils page = tpTenantService.queryPage(params);
        return JsonResponse.buildSuccess(page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{tenantId}")
    public JsonResponse info(@PathVariable("tenantId") String tenantId) {
        // TpTenant tpTenant = tpTenantService.getById(tenantId);
        return JsonResponse.buildSuccess();
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public JsonResponse save(@RequestBody TpTenant tpTenant) {
        // tpTenantService.save(tpTenant);

        return JsonResponse.buildSuccess();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public JsonResponse update(@RequestBody TpTenant tpTenant) {
        // tpTenantService.updateById(tpTenant);

        return JsonResponse.buildSuccess();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public JsonResponse delete(@RequestBody String[] tenantIds) {
        // tpTenantService.removeByIds(Arrays.asList(tenantIds));

        return JsonResponse.buildSuccess();
    }

}
