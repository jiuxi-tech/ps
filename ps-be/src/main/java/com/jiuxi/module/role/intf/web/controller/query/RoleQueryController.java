package com.jiuxi.module.role.intf.web.controller.query;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jiuxi.admin.core.bean.query.TpRoleAuthQuery;
import com.jiuxi.admin.core.bean.query.TpRoleQuery;
import com.jiuxi.admin.core.bean.vo.TpPersonRoleVO;
import com.jiuxi.admin.core.bean.vo.TpRoleVO;
import com.jiuxi.module.role.app.service.RoleService;
import com.jiuxi.common.bean.JsonResponse;
import com.jiuxi.common.bean.TreeNode;
import com.jiuxi.shared.common.annotation.Authorization;
import com.jiuxi.shared.common.annotation.IgnoreAuthorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashSet;
import java.util.List;

/**
 * @ClassName: RoleQueryController
 * @Description: 角色查询控制器 - CQRS查询端
 * @Author DDD重构
 * @Date 2025-09-15
 * @Copyright: www.tuxun.net Inc. All rights reserved.
 */
@RestController
@RequestMapping("/sys/role")
@Authorization
public class RoleQueryController {

    @Autowired
    private RoleService roleService;

    /**
     * 角色授权列表
     */
    @RequestMapping("/roleAuthList")
    public JsonResponse roleAuthList(TpRoleAuthQuery query, String jwtpid, String jwtdid, String jwtrids, String jwtaid) {
        LinkedHashSet<TpRoleVO> list = roleService.roleAuthList(query, jwtpid, jwtdid, jwtrids, jwtaid);
        return JsonResponse.buildSuccess(list);
    }

    /**
     * 人员授权，待选角色列表，查询自己角色创建的角色以及自己拥有的角色（向后兼容）
     */
    @RequestMapping("/role-auth-list")
    @IgnoreAuthorization
    public JsonResponse roleAuthListCompat(TpRoleAuthQuery query, String jwtpid, String jwtdid, String jwtrids, String jwtaid) {
        LinkedHashSet<TpRoleVO> list = roleService.roleAuthList(query, jwtpid, jwtdid, jwtrids, jwtaid);
        return JsonResponse.buildSuccess(list);
    }

    /**
     * 分页查询角色列表
     */
    @RequestMapping("/list")
    public JsonResponse list(TpRoleQuery query, String jwtpid, String jwtrids, String jwtaid) {
        IPage<TpRoleVO> page = roleService.queryPage(query, jwtpid, jwtrids, jwtaid);
        return JsonResponse.buildSuccess(page);
    }

    /**
     * 角色列表（政府），查询自己创建的政府角色
     */
    @RequestMapping("/org/list")
    public JsonResponse orgList(TpRoleQuery query, String jwtpid, String jwtrids, String jwtaid) {
        query.setCategory(0); // 0代表政府类别
        IPage<TpRoleVO> page = roleService.queryPage(query, jwtpid, jwtrids, jwtaid);
        return JsonResponse.buildSuccess(page);
    }

    /**
     * 获取角色列表（不分页）
     */
    @RequestMapping("/getList")
    public JsonResponse getList(TpRoleQuery query, String jwtaid) {
        List<TpRoleVO> list = roleService.getList(query, jwtaid);
        return JsonResponse.buildSuccess(list);
    }

    /**
     * 查看角色详情
     */
    @RequestMapping("/view")
    public JsonResponse view(String roleId) {
        TpRoleVO result = roleService.view(roleId);
        return JsonResponse.buildSuccess(result);
    }

    /**
     * 根据角色ID查询角色人员关系
     */
    @RequestMapping("/selectByRoleId")
    public JsonResponse selectByRoleId(String roleId) {
        List<TpPersonRoleVO> list = roleService.selectByRoleId(roleId);
        return JsonResponse.buildSuccess(list);
    }
    
    /**
     * 根据角色ID查询角色人员关系（小写连字符形式，兼容前端调用）
     */
    @RequestMapping("/select-by-roleid")
    public JsonResponse selectByRoleid(String roleId) {
        List<TpPersonRoleVO> list = roleService.selectByRoleId(roleId);
        return JsonResponse.buildSuccess(list);
    }

    /**
     * 获取权限树
     */
    @RequestMapping({"/authTree", "/auth-tree"})
    @IgnoreAuthorization
    public JsonResponse authTree(String roleId, String jwtrids, String jwtpid) {
        List<TreeNode> list = roleService.authTree(roleId, jwtrids, jwtpid);
        return JsonResponse.buildSuccess(list);
    }
}