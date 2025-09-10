package com.jiuxi.admin.core.controller.pc;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jiuxi.admin.core.bean.query.TpTimeRuleQuery;
import com.jiuxi.admin.core.bean.vo.TpTimeRuleVO;
import com.jiuxi.admin.core.service.TpTimeRuleService;
import com.jiuxi.common.bean.JsonResponse;
import com.jiuxi.core.core.annotation.Authorization;
import com.jiuxi.core.core.validator.group.AddGroup;
import com.jiuxi.core.core.validator.group.UpdateGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: TpTimeRuleController
 * @Description: 登录时间段控制规则管理
 * @Author system
 * @Date 2025-01-30
 * @Copyright: www.jiuxi.com Inc. All rights reserved.
 */
@RestController
@RequestMapping("/sys/timerule")
@Authorization
public class TpTimeRuleController {

    /**
     * 接口配置 passKey
     */
    private static final String PASS_KEY = "id";

    @Autowired
    private TpTimeRuleService tpTimeRuleService;

    /**
     * 时间规则列表（分页查询）
     */
    @RequestMapping("/list")
    public JsonResponse list(TpTimeRuleQuery query, String jwtpid, String jwtaid) {
        // 设置机构ID
        query.setInstitutionId(jwtaid);
        IPage<TpTimeRuleVO> page = tpTimeRuleService.queryPage(query);
        return JsonResponse.buildSuccess(page).buildPassKey(jwtpid, PASS_KEY);
    }

    /**
     * 获取所有有效的时间规则（不分页）
     */
    @RequestMapping("/all-valid")
    public JsonResponse allValid(String jwtpid, String jwtaid) {
        List<TpTimeRuleVO> list = tpTimeRuleService.getAllValidRules();
        return JsonResponse.buildSuccess(list).buildPassKey(jwtpid, PASS_KEY);
    }

    /**
     * 新增时间规则
     */
    @RequestMapping(value = "/add")
    public JsonResponse add(@Validated(value = AddGroup.class) TpTimeRuleVO vo, String jwtpid, String jwtaid) {
        // 设置创建人信息
        vo.setCreatorId(jwtpid);
        
        TpTimeRuleVO result = tpTimeRuleService.save(vo, jwtpid);
        return JsonResponse.buildSuccess(result.getId());
    }

    /**
     * 查看时间规则信息
     */
    @RequestMapping(value = "/view")
    @Authorization(businessKey = PASS_KEY)
    public JsonResponse view(String id) {
        TpTimeRuleVO vo = tpTimeRuleService.view(id);
        return JsonResponse.buildSuccess(vo);
    }

    /**
     * 修改时间规则
     */
    @RequestMapping(value = "/update")
    @Authorization(businessKey = PASS_KEY)
    public JsonResponse update(@Validated(value = UpdateGroup.class) TpTimeRuleVO vo, String jwtpid) {
        // 设置修改人信息
        vo.setModifierId(jwtpid);
        
        TpTimeRuleVO result = tpTimeRuleService.update(vo, jwtpid);
        return JsonResponse.buildSuccess(result);
    }

    /**
     * 删除时间规则
     */
    @RequestMapping("/delete")
    @Authorization(businessKey = PASS_KEY)
    public JsonResponse delete(String id, String jwtpid) {
        tpTimeRuleService.delete(id, jwtpid);
        return JsonResponse.buildSuccess("删除成功");
    }

    /**
     * 批量删除时间规则
     */
    @RequestMapping("/delete-batch")
    public JsonResponse deleteBatch(String[] ids, String jwtpid) {
        List<String> idList = Arrays.asList(ids);
        tpTimeRuleService.deleteBatch(idList, jwtpid);
        return JsonResponse.buildSuccess("批量删除成功");
    }

    /**
     * 验证用户登录时间权限
     */
    @RequestMapping(value = "/validate-login-time", method = RequestMethod.POST)
    public JsonResponse validateLoginTime(@RequestBody Map<String, Object> requestBody) {
        String userId = (String) requestBody.get("userId");
        Object roleIdsObj = requestBody.get("roleIds");
        
        List<String> roleIdList = null;
        if (roleIdsObj != null) {
            if (roleIdsObj instanceof List) {
                roleIdList = (List<String>) roleIdsObj;
            } else if (roleIdsObj instanceof String) {
                String roleIds = (String) roleIdsObj;
                if (!roleIds.trim().isEmpty()) {
                    roleIdList = Arrays.asList(roleIds.split(","));
                }
            }
        }
        
        TpTimeRuleService.LoginTimeValidationResult result = 
            tpTimeRuleService.validateLoginTimeWithReason(userId, roleIdList);
        
        return JsonResponse.buildSuccess(result);
    }

    /**
     * 获取用户和角色相关的时间规则
     */
    @RequestMapping("/user-rules")
    public JsonResponse getUserRules(String userId, String roleIds) {
        // 将逗号分隔的角色ID字符串转换为List
        List<String> roleIdList = null;
        if (roleIds != null && !roleIds.trim().isEmpty()) {
            roleIdList = Arrays.asList(roleIds.split(","));
        }
        
        List<TpTimeRuleVO> rules = tpTimeRuleService.getRulesByUserAndRoles(userId, roleIdList);
        return JsonResponse.buildSuccess(rules);
    }
}