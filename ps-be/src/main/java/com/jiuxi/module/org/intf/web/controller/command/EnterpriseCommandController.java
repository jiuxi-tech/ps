package com.jiuxi.module.org.intf.web.controller.command;

import cn.hutool.core.util.StrUtil;
import com.jiuxi.admin.core.bean.vo.TpEntBasicinfoVO;
import com.jiuxi.module.org.app.service.EnterpriseApplicationService;
import com.jiuxi.common.bean.JsonResponse;
import com.jiuxi.shared.common.annotation.Authorization;
import com.jiuxi.shared.common.context.TenantContextHolder;
import com.jiuxi.shared.common.validation.group.AddGroup;
import com.jiuxi.shared.common.validation.group.UpdateGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 企业命令控制器
 * 负责企业相关的写操作（创建、更新、删除）
 * 采用CQRS模式，专门处理Command操作
 * 
 * @author DDD重构
 * @date 2025-09-18
 */
@RestController
@RequestMapping("/sys/ent")
@Authorization(businessKey = "SYS0503")
public class EnterpriseCommandController {

    /**
     * 接口配置 passKey
     */
    private static final String PASS_KEY = "PASS";

    @Autowired
    private EnterpriseApplicationService enterpriseApplicationService;

    /**
     * 新增企业基本信息
     * 接口路径：/sys/ent/add
     * 保持原有接口格式完全兼容
     */
    @RequestMapping("/add")
    public JsonResponse add(@Validated(value = AddGroup.class) TpEntBasicinfoVO vo, String jwtpid) {
        try {
            // 设置租户
            String tenantId = TenantContextHolder.getTenantId();
            if (StrUtil.isNotBlank(tenantId)) {
                vo.setTenantId(tenantId);
            }

            // 使用原有服务保持兼容性
            String entid = enterpriseApplicationService.add(vo, jwtpid);
            if (null != entid) {
                return JsonResponse.buildSuccess(entid);
            } else {
                return JsonResponse.buildFailure("创建企业失败");
            }
        } catch (Exception e) {
            return JsonResponse.buildFailure("创建企业失败: " + e.getMessage());
        }
    }

    /**
     * 修改企业基本信息
     * 接口路径：/sys/ent/update
     * 保持原有接口格式完全兼容
     */
    @RequestMapping("/update")
    @Authorization(businessKey = PASS_KEY)
    public JsonResponse update(@Validated(value = UpdateGroup.class) TpEntBasicinfoVO vo, String jwtpid) {
        try {
            // 使用原有服务保持兼容性
            int count = enterpriseApplicationService.update(vo, jwtpid);
            if (count == 0) {
                return JsonResponse.buildFailure("修改企业基本信息失败，找不到该条数据...");
            }
            return JsonResponse.buildSuccess();
        } catch (Exception e) {
            return JsonResponse.buildFailure("修改企业信息失败: " + e.getMessage());
        }
    }

    /**
     * 删除企业信息
     * 接口路径：/sys/ent/delete
     * 保持原有接口格式完全兼容
     */
    @RequestMapping("/delete")
    @Authorization(businessKey = PASS_KEY)
    public JsonResponse delete(String entId, String jwtpid) {
        try {
            // 使用原有服务保持兼容性
            int count = enterpriseApplicationService.delete(entId, jwtpid);
            if (count == 0) {
                return JsonResponse.buildFailure("删除企业信息失败，找不到该条数据...");
            }
            return JsonResponse.buildSuccess();
        } catch (Exception e) {
            return JsonResponse.buildFailure("删除企业信息失败: " + e.getMessage());
        }
    }
}