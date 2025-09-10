package com.jiuxi.admin.core.controller.pc;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jiuxi.admin.core.bean.query.TpEntAccountQuery;
import com.jiuxi.admin.core.bean.query.TpEntBasicQuery;
import com.jiuxi.admin.core.bean.vo.TpEntAccountVO;
import com.jiuxi.admin.core.bean.vo.TpEntBasicinfoVO;
import com.jiuxi.admin.core.service.TpEntBasicinfoService;
import com.jiuxi.common.bean.JsonResponse;
import com.jiuxi.core.core.annotation.Authorization;
import com.jiuxi.core.core.context.TenantContextHolder;
import com.jiuxi.core.core.validator.group.AddGroup;
import com.jiuxi.core.core.validator.group.UpdateGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName: TpEntBasicinfoController
 * @Description: 企业基本信息表
 * @Author 杨攀
 * @Date 2020-11-18 11:05:18
 * @Copyright: www.tuxun.net Inc. All rights reserved.
 */
@RestController
@RequestMapping("/sys/ent")
@Authorization
public class TpEntBasicinfoController {

    /**
     * 接口配置 passKey
     */
    private static final String PASS_KEY = "entId";

    @Autowired
    private TpEntBasicinfoService tpEntBasicinfoService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public JsonResponse list(TpEntBasicQuery query, String jwtpid, String jwtaid) {
        IPage<TpEntBasicinfoVO> page = tpEntBasicinfoService.queryPage(query, jwtaid);
        return JsonResponse.buildSuccess(page).buildPassKey(jwtpid, PASS_KEY);
    }

    /**
     * 分页查询企业下的所有用户账号
     * <p>
     * 2021.12.10 过期原因，企业管理 -> 账号信息，根据企业查询管理账号，只管理企业管理员账号。该接口过期，使用entAdminList接口。
     */
    @Deprecated
    @RequestMapping("/ent-account-list")
    public JsonResponse entAccountList(TpEntAccountQuery query, String jwtpid, String jwtaid) {
        IPage<TpEntAccountVO> page = tpEntBasicinfoService.accountQueryPage(query, jwtpid, jwtaid);
        return JsonResponse.buildSuccess(page);
    }

    /**
     * 获取企业管理员列表
     */
    @RequestMapping("/ent-admin-list")
    public JsonResponse entAdminList(TpEntAccountQuery query, String jwtpid) {
        IPage<TpEntAccountVO> page = tpEntBasicinfoService.adminQueryPage(query);
        return JsonResponse.buildSuccess(page).buildPassKey(jwtpid,"personId");
    }

    /**
     * 查看企业基本信息
     *
     * @param entId
     * @return com.jiuxi.common.bean.JsonResponse
     * @author 杨攀
     * @date 2020/11/30 10:33
     */
    @RequestMapping("/view")
    @Authorization(businessKey = PASS_KEY)
    public JsonResponse view(String entId) {
        TpEntBasicinfoVO vo = tpEntBasicinfoService.view(entId);
        return JsonResponse.buildSuccess(vo);
    }


    /**
     * 新增企业基本信息
     */
    @RequestMapping("/add")
    public JsonResponse add(@Validated(value = AddGroup.class) TpEntBasicinfoVO vo, String jwtpid) {

        // 设置租户
        String tenantId = TenantContextHolder.getTenantId();
        if (StrUtil.isNotBlank(tenantId)) {
            vo.setTenantId(tenantId);
        }

        String entid = tpEntBasicinfoService.add(vo, jwtpid);
        if (null != entid) {
            return JsonResponse.buildSuccess(entid);
        } else {
            return JsonResponse.buildFailure();
        }
    }

    /**
     * 修改企业基本信息
     */
    @RequestMapping("/update")
    @Authorization(businessKey = PASS_KEY)
    public JsonResponse update(@Validated(value = UpdateGroup.class) TpEntBasicinfoVO vo, String jwtpid) {

        int count = tpEntBasicinfoService.update(vo, jwtpid);
        if (count == 0) {
            return JsonResponse.buildFailure("修改企业基本信息失败，找不到该条数据...");
        }
        return JsonResponse.buildSuccess();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @Authorization(businessKey = PASS_KEY)
    public JsonResponse delete(String entId, String jwtpid) {

        int count = tpEntBasicinfoService.delete(entId, jwtpid);
        if (count == 0) {
            return JsonResponse.buildFailure("删除企业信息失败，找不到该条数据...");
        }
        return JsonResponse.buildSuccess();
    }

}
