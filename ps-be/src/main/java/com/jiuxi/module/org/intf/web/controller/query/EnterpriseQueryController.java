package com.jiuxi.module.org.intf.web.controller.query;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jiuxi.admin.core.bean.query.TpEntAccountQuery;
import com.jiuxi.admin.core.bean.query.TpEntBasicQuery;
import com.jiuxi.admin.core.bean.vo.TpEntAccountVO;
import com.jiuxi.admin.core.bean.vo.TpEntBasicinfoVO;
import com.jiuxi.module.org.app.service.EnterpriseApplicationService;
import com.jiuxi.common.bean.JsonResponse;
import com.jiuxi.shared.common.annotation.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 企业查询控制器
 * 负责企业相关的读操作（查询、列表、统计）
 * 采用CQRS模式，专门处理Query操作
 * 
 * @author DDD重构
 * @date 2025-09-18
 */
@RestController
@RequestMapping("/sys/ent")
@Authorization(businessKey = "SYS0503")
public class EnterpriseQueryController {

    /**
     * 接口配置 passKey
     */
    private static final String PASS_KEY = "PASS";

    @Autowired
    private EnterpriseApplicationService enterpriseApplicationService;

    /**
     * 分页查询企业列表
     * 接口路径：/sys/ent/list
     * 保持原有接口格式完全兼容
     */
    @RequestMapping("/list")
    public JsonResponse list(TpEntBasicQuery query, String jwtpid, String jwtaid) {
        try {
            // 使用原有服务保持兼容性
            IPage<TpEntBasicinfoVO> page = enterpriseApplicationService.queryPage(query, jwtaid);
            return JsonResponse.buildSuccess(page).buildPassKey(jwtpid, PASS_KEY);
        } catch (Exception e) {
            return JsonResponse.buildFailure("查询企业列表失败: " + e.getMessage());
        }
    }

    /**
     * 查看企业基本信息
     * 接口路径：/sys/ent/view
     * 保持原有接口格式完全兼容
     *
     * @param entId 企业ID
     * @return com.jiuxi.common.bean.JsonResponse
     */
    @RequestMapping("/view")
    @Authorization(businessKey = PASS_KEY)
    public JsonResponse view(String entId) {
        try {
            // 使用原有服务保持兼容性
            TpEntBasicinfoVO vo = enterpriseApplicationService.view(entId);
            return JsonResponse.buildSuccess(vo);
        } catch (Exception e) {
            return JsonResponse.buildFailure("查询企业信息失败: " + e.getMessage());
        }
    }

    /**
     * 获取企业管理员列表
     * 接口路径：/sys/ent/ent-admin-list
     * 保持原有接口格式完全兼容
     */
    @RequestMapping("/ent-admin-list")
    public JsonResponse entAdminList(TpEntAccountQuery query, String jwtpid) {
        try {
            // 使用原有服务保持兼容性
            IPage<TpEntAccountVO> page = enterpriseApplicationService.adminQueryPage(query);
            return JsonResponse.buildSuccess(page).buildPassKey(jwtpid,"personId");
        } catch (Exception e) {
            return JsonResponse.buildFailure("查询企业管理员列表失败: " + e.getMessage());
        }
    }

    /**
     * 分页查询企业下的所有用户账号
     * 接口路径：/sys/ent/ent-account-list
     * 保持原有接口格式完全兼容
     * <p>
     * 2021.12.10 过期原因，企业管理 -> 账号信息，根据企业查询管理账号，只管理企业管理员账号。该接口过期，使用entAdminList接口。
     */
    @Deprecated
    @RequestMapping("/ent-account-list")
    public JsonResponse entAccountList(TpEntAccountQuery query, String jwtpid, String jwtaid) {
        try {
            // 使用原有服务保持兼容性
            IPage<TpEntAccountVO> page = enterpriseApplicationService.accountQueryPage(query, jwtpid, jwtaid);
            return JsonResponse.buildSuccess(page);
        } catch (Exception e) {
            return JsonResponse.buildFailure("查询企业账号列表失败: " + e.getMessage());
        }
    }
}