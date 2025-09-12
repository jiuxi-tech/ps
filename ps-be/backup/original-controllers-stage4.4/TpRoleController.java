package com.jiuxi.admin.core.controller.pc;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jiuxi.common.constant.TpConstant;
import com.jiuxi.admin.core.bean.query.TpRoleAuthQuery;
import com.jiuxi.admin.core.bean.query.TpRoleQuery;
import com.jiuxi.admin.core.bean.vo.TpPersonRoleVO;
import com.jiuxi.admin.core.bean.vo.TpRoleVO;
import com.jiuxi.admin.core.service.TpRoleService;
import com.jiuxi.common.bean.JsonResponse;
import com.jiuxi.common.bean.TreeNode;
import com.jiuxi.shared.common.annotation.Authorization;
import com.jiuxi.shared.common.annotation.IgnoreAuthorization;
import com.jiuxi.shared.common.validation.group.AddGroup;
import com.jiuxi.shared.common.validation.group.UpdateGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashSet;
import java.util.List;

/**
 * @ClassName: TpRoleController
 * @Description: 角色表
 * @Author pand
 * @Date 2020-11-18 11:05:17
 * @Copyright: www.tuxun.net Inc. All rights reserved.
 */
@RestController
@RequestMapping("/sys/role")
@Authorization
public class TpRoleController {

    /**
     * 接口配置 passKey
     */
    private static final String PASS_KEY = "roleId";

    @Autowired
    private TpRoleService tpRoleService;

    /**
     * 人员授权，待选角色列表，查询自己角色创建的角色以及自己拥有的角色（新接口）
     */
    @RequestMapping("/role-auth-list")
    @IgnoreAuthorization
    public JsonResponse roleAuthList(TpRoleAuthQuery query, String jwtpid, String jwtdid, String jwtrids, String jwtaid) {
        LinkedHashSet<TpRoleVO> page = tpRoleService.roleAuthList(query, jwtpid, jwtdid, jwtrids, jwtaid);
        return JsonResponse.buildSuccess(page);
    }


    /**
     * 人员授权，待选角色列表，查询自己角色创建的角色(老接口)
     * 过期：原因是老接口不支持自己拥有的角色再次分配
     */
    @RequestMapping("/auth-list")
    @IgnoreAuthorization
    @Deprecated
    public JsonResponse authList(TpRoleQuery query, String jwtpid, String jwtrids, String jwtaid) {
        query.setSize(-1);
        IPage<TpRoleVO> page = tpRoleService.queryPage(query, jwtpid, jwtrids, jwtaid);
        return JsonResponse.buildSuccess(page);
    }

    /**
     * 角色列表（政府）,查询自己创建的政府角色
     */
    @RequestMapping("/org/list")
    public JsonResponse orgList(TpRoleQuery query, String jwtpid, String jwtrids, String jwtaid) {
        query.setCategory(TpConstant.Category.ORG);
        IPage<TpRoleVO> page = tpRoleService.queryPage(query, jwtpid, jwtrids, jwtaid);
        return JsonResponse.buildSuccess(page).buildPassKey(jwtpid, PASS_KEY);
    }


    /**
     * 角色列表（企业）,查询自己创建的企业角色
     */
    @RequestMapping("/ent/list")
    public JsonResponse entList(TpRoleQuery query, String jwtpid, String jwtrids, String jwtaid) {
        query.setCategory(TpConstant.Category.ENT);
        IPage<TpRoleVO> page = tpRoleService.queryPage(query, jwtpid, jwtrids, jwtaid);
        return JsonResponse.buildSuccess(page).buildPassKey(jwtpid, PASS_KEY);
    }

    /**
     * 根据角色创建单位查询角色，工作流查询使用
     */
    @RequestMapping("/aid-list")
    @IgnoreAuthorization
    public JsonResponse listByAid(TpRoleQuery query, String jwtaid) {
        List<TpRoleVO> list = tpRoleService.getList(query, jwtaid);
        return JsonResponse.buildSuccess(list);
    }


    /**
     * 新增角色（政府）
     */
    @RequestMapping(value = "/org/add")
    public JsonResponse orgAdd(@Validated(value = AddGroup.class) TpRoleVO vo, String jwtpid, String jwtaid) {
        int count = tpRoleService.add(vo, jwtpid, jwtaid, TpConstant.Category.ORG);

        return JsonResponse.buildSuccess(count);
    }

    /**
     * 新增角色（企业）
     */
    @RequestMapping(value = "/ent/add")
    public JsonResponse entAdd(@Validated(value = AddGroup.class) TpRoleVO vo, String jwtpid, String jwtaid) {
        int count = tpRoleService.add(vo, jwtpid, jwtaid, TpConstant.Category.ENT);

        return JsonResponse.buildSuccess(count);
    }

    /**
     * 查看角色信息
     */
    @RequestMapping(value = "/view")
    @Authorization(businessKey = PASS_KEY)
    public JsonResponse view(String roleId) {
        TpRoleVO vo = tpRoleService.view(roleId);

        return JsonResponse.buildSuccess(vo);
    }

    /**
     * 修改角色
     */
    @RequestMapping(value = "/update")
    @Authorization(businessKey = PASS_KEY)
    public JsonResponse update(@Validated(value = UpdateGroup.class) TpRoleVO vo, String jwtpid) {
        int count = tpRoleService.update(vo, jwtpid);

        return JsonResponse.buildSuccess(count);
    }

    /**
     * 删除角色前查询角色是否有被赋予人员，如果有绑定人员，提醒是否确定删除。
     */
    @RequestMapping("/select-by-roleid")
    @Authorization(businessKey = PASS_KEY, roleAuth = false)
    public JsonResponse selectByRoleid(String roleId) {
        List<TpPersonRoleVO> list = tpRoleService.selectByRoleId(roleId);

        if (null != list && !list.isEmpty()) {
            return JsonResponse.buildSuccess(list.size());
        } else {
            return JsonResponse.buildSuccess(0);
        }
    }

    /**
     * 删除角色
     */
    @RequestMapping("/delete")
    @Authorization(businessKey = PASS_KEY)
    public JsonResponse delete(String roleId, String creator, String jwtpid) {
        tpRoleService.delete(roleId, creator, jwtpid);

        return JsonResponse.buildSuccess();
    }

    /**
     * 授权菜单
     */
    @RequestMapping("/auth-tree")
    @IgnoreAuthorization
    public JsonResponse authTree(String roleId, String jwtrids, String jwtpid) {
        List<TreeNode> list = tpRoleService.authTree(roleId, jwtrids, jwtpid);

        return JsonResponse.buildSuccess(list);
    }

    /**
     * 角色授权
     */
    @RequestMapping(value = "/auth-add")
    @Authorization(businessKey = PASS_KEY)
    public JsonResponse authAdd(String roleId, String menuIds) {
        tpRoleService.roleMenus(roleId, menuIds);

        return JsonResponse.buildSuccess();
    }

}
